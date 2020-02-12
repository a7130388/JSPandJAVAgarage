package com.cathay.xx.zx.trx;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.cathay.common.bo.ReturnMessage;
import com.cathay.common.exception.DataNotFoundException;
import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.message.MessageHelper;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.trx.UCBean;
import com.cathay.common.util.IConstantMap;
import com.cathay.rz.n0.module.RZ_N0Z001;
import com.cathay.util.ReturnCode;
import com.cathay.util.Transaction;
import com.cathay.xx.vo.DTXXTP01;
import com.cathay.xx.zx.module.XX_ZX0100;

import com.igsapp.common.trx.ServiceException;
import com.igsapp.common.trx.TxException;
import com.igsapp.wibc.dataobj.Context.RequestContext;
import com.igsapp.wibc.dataobj.Context.ResponseContext;

@SuppressWarnings({ "unchecked", "deprecation" })
public class XXZX_0101 extends UCBean {

    /** �R�A�� log ���� **/
    private static final Logger log = Logger.getLogger(XXZX_0101.class); // TODO

    /** �� TxBean �{���X�@�Ϊ� ResponseContext */
    private ResponseContext resp;

    /** �� TxBean �{���X�@�Ϊ� ReturnMessage */
    private ReturnMessage msg;

    /** �� TxBean �{���X�@�Ϊ� UserObject */
    private UserObject user;

    /**
     * �мg�����O�� start() �H�j���C�� Dispatcher �I�s method �ɳ�����{���۩w����l�ʧ@ **/
     
    public ResponseContext start(RequestContext req) throws TxException, ServiceException {
        super.start(req); // �@�w�n invoke super.start() �H�����v���ˮ�
        initApp(req); // �I�s�۩w����l�ʧ@
        return null;
    }

    /**
     * �{���۩w����l�ʧ@�A�q�`�����X ResponseContext, UserObject, �γ]�w ReturnMessage �� response
     *  code.
     */
    private void initApp(RequestContext req) {
        // �إߦ� TxBean �q�Ϊ�����
        resp = this.newResponseContext();
        msg = new ReturnMessage();
        user = this.getUserObject(req);
        // ���N ReturnMessage �� reference �[�� response context
        resp.addOutputData(IConstantMap.ErrMsg, msg);

        // �b Cathay �q�`�u���@�� page �b�e�� display�A�ҥH�i�H���]�w
        resp.setResponseCode("success");
    }

    /**
     * ��l����
     * 
     * @param req
     * @return
     */
    public ResponseContext doPrompt(RequestContext req) {

        String ACTION_TYPE = req.getParameter("ACTION_TYPE");
        String EMP_ID = req.getParameter("EMP_ID");
        String EMP_NAME = req.getParameter("EMP_NAME");
        String DIV_NO = req.getParameter("DIV_NO");
        String BIRTHDAY = req.getParameter("BIRTHDAY");
        String POSITION = req.getParameter("POSITION");
        String OP_STATUS = req.getParameter("OP_STATUS");
        String FLOW_NO = req.getParameter("FLOW_NO");

        String UPDT_DATE = req.getParameter("UPDT_DATE");

        String userEMP_NAME = user.getEmpName();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // TODO DATE.
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String time = df.format(now);

        Map reqMap = new HashMap();
        reqMap.put("ACTION_TYPE", ACTION_TYPE);
        reqMap.put("EMP_ID", EMP_ID);
        reqMap.put("EMP_NAME", EMP_NAME);
        reqMap.put("DIV_NO", DIV_NO);
        reqMap.put("BIRTHDAY", BIRTHDAY);
        reqMap.put("POSITION", POSITION);
        if ("U".equals(ACTION_TYPE)) { // TODO "U".equals(ACTION_TYPE) StringUtils.equals(,)
            reqMap.put("OP_STATUS", OP_STATUS);
            reqMap.put("UPDT_DATE", UPDT_DATE.substring(0, 19));
        } else {
            reqMap.put("OP_STATUS", "��J��");
            reqMap.put("UPDT_DATE", time);
        }
        reqMap.put("UPDT_NM", userEMP_NAME);
        reqMap.put("FLOW_NO", FLOW_NO);
        resp.addOutputData("reqMap", reqMap);
        resp.addOutputData("hid_DEP_NM", "���{�]��"); // TODO? //TODO
        return resp;
    }

    /**
     * �ק�
     * 
     * @param req
     * @return
     */
    public ResponseContext doUpdate(RequestContext req) {

        // TODO new XX_ZX0100()
        try {
            Map reqMap = VOTool.requestToMap(req);
            String hid_DEP_NM = req.getParameter("hid_DEP_NM");
            reqMap.put("UPDT_NM", user.getEmpName());

            resp.addOutputData("reqMap", reqMap); // TODO
            resp.addOutputData("hid_DEP_NM", hid_DEP_NM);
            theXX_ZX0100 = new XX_ZX0100();
            Transaction.begin();
            try {
                theXX_ZX0100.update(reqMap, user);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }

            try {
                reqMap = theXX_ZX0100.query(reqMap).get(0);
                reqMap.put("UPDT_DATE", MapUtils.getString(reqMap, "UPDT_DATE").substring(0, 19));
                reqMap.put("UPDT_NM", user.getEmpName());
                reqMap.put("ACTION_TYPE", ACTION_TYPE);
                reqMap.put("hid_DEP_NM", hid_DEP_NM);
                resp.addOutputData("hid_DEP_NM", hid_DEP_NM);
                resp.addOutputData("reqMap", reqMap);
            } catch (DataNotFoundException e) {
                log.error("�d�L���", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�ק令�\!");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�@�~����"); // TODO
            }
        } catch (Exception e) {
            log.error("�@�~����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�@�~����");
        }

        return resp;
    }

    /**
     * �s�W
     * 
     * @param req
     * @return
     */
    public ResponseContext doInsert(RequestContext req) {

        try {
            Map reqMap = VOTool.requestToMap(req);

            String hid_DEP_NM = req.getParameter("hid_DEP_NM");

            reqMap.put("ACTION_TYPE", "I");
            reqMap.put("OP_STATUS", "��J��");
            reqMap.put("UPDT_NM", user.getEmpName());
            resp.addOutputData("hid_DEP_NM", hid_DEP_NM);
            resp.addOutputData("reqMap", reqMap);
            theXX_ZX0100 = new XX_ZX0100();

            Transaction.begin();
            try {
                theXX_ZX0100.insert(reqMap, user);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }

            try {
                reqMap = theXX_ZX0100.query(reqMap).get(0);
                reqMap.put("ACTION_TYPE", "U");
                reqMap.put("UPDT_NM", user.getEmpName());
                reqMap.put("UPDT_DATE", MapUtils.getString(reqMap, "UPDT_DATE").substring(0, 19));
                resp.addOutputData("reqMap", reqMap);
                resp.addOutputData("hid_DEP_NM", hid_DEP_NM);
            } catch (DataNotFoundException e) {
                log.error("�d�L���", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�s�W����");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());

        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�@�~����");
            }
        } catch (Exception e) {
            log.error("�@�~����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�@�~����");
        }

        return resp;
    }

    /**
     * ����
     * 
     * @param req
     * @return
     */
    public ResponseContext doSubmit(RequestContext req) {

        try {
            Map reqMap = VOTool.requestToMap(req);

            reqMap.put("UPDT_NM", user.getEmpName());

            resp.addOutputData("reqMap", reqMap);
            resp.addOutputData("hid_DEP_NM", "���{�]��");
            theXX_ZX0100 = new XX_ZX0100();
            DTXXTP01 vo = new DTXXTP01(); // TODO
            vo.setEMP_ID(EMP_ID);

            Transaction.begin();
            try {
                new RZ_N0Z001().approveFlow(FLOW_NO, "����", "", user.getEmpID(), user.getDivNo());
                // vo1.setFLOW_NO(FLOW_NO);
                theXX_ZX0100.confirm(vo);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            try {
                reqMap = theXX_ZX0100.query(reqMap).get(0);
                reqMap.put("ACTION_TYPE", "U");
                reqMap.put("UPDT_NM", user.getEmpName());
                reqMap.put("UPDT_DATE", MapUtils.getString(reqMap, "UPDT_DATE").substring(0, 19));
                resp.addOutputData("reqMap", reqMap);
                resp.addOutputData("hid_DEP_NM", "���{�]��");
            } catch (DataNotFoundException e) {
                log.error("�d�L���", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "���槹��");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "���u�s�����~"); // TODO
            }
        } catch (Exception e) {
            log.error("�@�~����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "���u�s�����~"); // TODO
        }

        return resp;
    }

    /**
     * �f��
     * 
     * @param req
     * @return
     */
    public ResponseContext doApprove(RequestContext req) {

        try {
            Map reqMap = VOTool.requestToMap(req);

            reqMap.put("UPDT_NM", user.getEmpName());

            resp.addOutputData("reqMap", reqMap);
            resp.addOutputData("hid_DEP_NM", "���{�]��");
            theXX_ZX0100 = new XX_ZX0100();

            DTXXTP01 vo = new DTXXTP01();
            vo.setEMP_ID(EMP_ID);

            Transaction.begin();
            try {
                new RZ_N0Z001().approveFlow(FLOW_NO, "�f��", "", user.getEmpID(), user.getDivNo());
                theXX_ZX0100.approve(vo);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            try {
                reqMap = theXX_ZX0100.query(reqMap).get(0);
                reqMap.put("ACTION_TYPE", "U");
                reqMap.put("UPDT_NM", user.getEmpName());
                reqMap.put("UPDT_DATE", MapUtils.getString(reqMap, "UPDT_DATE").substring(0, 19));
                resp.addOutputData("reqMap", reqMap);
                resp.addOutputData("hid_DEP_NM", "���{�]��");
            } catch (DataNotFoundException e) {
                log.error("�d�L���", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�f�֧���");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�@�~����");
            }
        } catch (Exception e) {
            log.error("�@�~����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�@�~����");
        }
        return resp;

    }

    /**
     * �h�^
     * 
     * @param req
     * @return
     */
    public ResponseContext doReject(RequestContext req) {

        try {
            Map reqMap = VOTool.requestToMap(req);

            reqMap.put("UPDT_NM", user.getEmpName());

            resp.addOutputData("reqMap", reqMap);
            resp.addOutputData("hid_DEP_NM", "���{�]��");
            theXX_ZX0100 = new XX_ZX0100();

            DTXXTP01 vo1 = new DTXXTP01();
            vo1.setEMP_ID(EMP_ID);

            Transaction.begin();
            try {
                new RZ_N0Z001().rejectFlow(FLOW_NO, "�h�^", "", user.getEmpID(), user.getDivNo());
                theXX_ZX0100.reject(vo1);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            try {
                reqMap = theXX_ZX0100.query(reqMap).get(0);
                reqMap.put("ACTION_TYPE", "U");
                reqMap.put("UPDT_NM", user.getEmpName());
                reqMap.put("UPDT_DATE", MapUtils.getString(reqMap, "UPDT_DATE").substring(0, 19));
                resp.addOutputData("reqMap", reqMap); // TODO
                resp.addOutputData("hid_DEP_NM", "���{�]��");
            } catch (DataNotFoundException e) {
                log.error("�d�L���", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�h�^����");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�@�~����");
            }
        } catch (Exception e) {
            log.error("�@�~����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�@�~����");
        }

        return resp;
    }

    /**
     * �R��
     * 
     * @param req
     * @return
     */

    public ResponseContext doDelete(RequestContext req) {

        try {
            Map reqMap = VOTool.requestToMap(req);

            String userEMP_NAME = user.getEmpName();

            reqMap.put("UPDT_NM", userEMP_NAME);

            resp.addOutputData("reqMap", reqMap);
            resp.addOutputData("hid_DEP_NM", "���{�]��");
            Transaction.begin();
            try {
                new XX_ZX0100().delete(reqMap);
                resp.addOutputData("GOBACK", "5"); // TODO
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;

            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�R������");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�@�~����");
            }
        } catch (Exception e) {
            log.error("�@�~����", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�@�~����");
        }

        return resp;
    }

}
