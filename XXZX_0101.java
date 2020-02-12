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

    /** 靜態的 log 物件 **/
    private static final Logger log = Logger.getLogger(XXZX_0101.class); // TODO

    /** 此 TxBean 程式碼共用的 ResponseContext */
    private ResponseContext resp;

    /** 此 TxBean 程式碼共用的 ReturnMessage */
    private ReturnMessage msg;

    /** 此 TxBean 程式碼共用的 UserObject */
    private UserObject user;

    /**
     * 覆寫父類別的 start() 以強制於每次 Dispatcher 呼叫 method 時都執行程式自定的初始動作 **/
     
    public ResponseContext start(RequestContext req) throws TxException, ServiceException {
        super.start(req); // 一定要 invoke super.start() 以執行權限檢核
        initApp(req); // 呼叫自定的初始動作
        return null;
    }

    /**
     * 程式自定的初始動作，通常為取出 ResponseContext, UserObject, 及設定 ReturnMessage 及 response
     *  code.
     */
    private void initApp(RequestContext req) {
        // 建立此 TxBean 通用的物件
        resp = this.newResponseContext();
        msg = new ReturnMessage();
        user = this.getUserObject(req);
        // 先將 ReturnMessage 的 reference 加到 response context
        resp.addOutputData(IConstantMap.ErrMsg, msg);

        // 在 Cathay 通常只有一個 page 在前面 display，所以可以先設定
        resp.setResponseCode("success");
    }

    /**
     * 初始頁面
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
            reqMap.put("OP_STATUS", "輸入中");
            reqMap.put("UPDT_DATE", time);
        }
        reqMap.put("UPDT_NM", userEMP_NAME);
        reqMap.put("FLOW_NO", FLOW_NO);
        resp.addOutputData("reqMap", reqMap);
        resp.addOutputData("hid_DEP_NM", "投資程設科"); // TODO? //TODO
        return resp;
    }

    /**
     * 修改
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
                log.error("查無資料", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "修改成功!");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "作業失敗"); // TODO
            }
        } catch (Exception e) {
            log.error("作業失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "作業失敗");
        }

        return resp;
    }

    /**
     * 新增
     * 
     * @param req
     * @return
     */
    public ResponseContext doInsert(RequestContext req) {

        try {
            Map reqMap = VOTool.requestToMap(req);

            String hid_DEP_NM = req.getParameter("hid_DEP_NM");

            reqMap.put("ACTION_TYPE", "I");
            reqMap.put("OP_STATUS", "輸入中");
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
                log.error("查無資料", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "新增完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());

        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "作業失敗");
            }
        } catch (Exception e) {
            log.error("作業失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "作業失敗");
        }

        return resp;
    }

    /**
     * 提交
     * 
     * @param req
     * @return
     */
    public ResponseContext doSubmit(RequestContext req) {

        try {
            Map reqMap = VOTool.requestToMap(req);

            reqMap.put("UPDT_NM", user.getEmpName());

            resp.addOutputData("reqMap", reqMap);
            resp.addOutputData("hid_DEP_NM", "投資程設科");
            theXX_ZX0100 = new XX_ZX0100();
            DTXXTP01 vo = new DTXXTP01(); // TODO
            vo.setEMP_ID(EMP_ID);

            Transaction.begin();
            try {
                new RZ_N0Z001().approveFlow(FLOW_NO, "提交", "", user.getEmpID(), user.getDivNo());
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
                resp.addOutputData("hid_DEP_NM", "投資程設科");
            } catch (DataNotFoundException e) {
                log.error("查無資料", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "提交完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "員工編號錯誤"); // TODO
            }
        } catch (Exception e) {
            log.error("作業失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "員工編號錯誤"); // TODO
        }

        return resp;
    }

    /**
     * 審核
     * 
     * @param req
     * @return
     */
    public ResponseContext doApprove(RequestContext req) {

        try {
            Map reqMap = VOTool.requestToMap(req);

            reqMap.put("UPDT_NM", user.getEmpName());

            resp.addOutputData("reqMap", reqMap);
            resp.addOutputData("hid_DEP_NM", "投資程設科");
            theXX_ZX0100 = new XX_ZX0100();

            DTXXTP01 vo = new DTXXTP01();
            vo.setEMP_ID(EMP_ID);

            Transaction.begin();
            try {
                new RZ_N0Z001().approveFlow(FLOW_NO, "審核", "", user.getEmpID(), user.getDivNo());
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
                resp.addOutputData("hid_DEP_NM", "投資程設科");
            } catch (DataNotFoundException e) {
                log.error("查無資料", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "審核完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "作業失敗");
            }
        } catch (Exception e) {
            log.error("作業失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "作業失敗");
        }
        return resp;

    }

    /**
     * 退回
     * 
     * @param req
     * @return
     */
    public ResponseContext doReject(RequestContext req) {

        try {
            Map reqMap = VOTool.requestToMap(req);

            reqMap.put("UPDT_NM", user.getEmpName());

            resp.addOutputData("reqMap", reqMap);
            resp.addOutputData("hid_DEP_NM", "投資程設科");
            theXX_ZX0100 = new XX_ZX0100();

            DTXXTP01 vo1 = new DTXXTP01();
            vo1.setEMP_ID(EMP_ID);

            Transaction.begin();
            try {
                new RZ_N0Z001().rejectFlow(FLOW_NO, "退回", "", user.getEmpID(), user.getDivNo());
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
                resp.addOutputData("hid_DEP_NM", "投資程設科");
            } catch (DataNotFoundException e) {
                log.error("查無資料", e);
            }

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "退回完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "作業失敗");
            }
        } catch (Exception e) {
            log.error("作業失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "作業失敗");
        }

        return resp;
    }

    /**
     * 刪除
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
            resp.addOutputData("hid_DEP_NM", "投資程設科");
            Transaction.begin();
            try {
                new XX_ZX0100().delete(reqMap);
                resp.addOutputData("GOBACK", "5"); // TODO
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;

            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "刪除完成");
        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "作業失敗");
            }
        } catch (Exception e) {
            log.error("作業失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "作業失敗");
        }

        return resp;
    }

}
