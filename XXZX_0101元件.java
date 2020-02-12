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
import com.cathay.common.im.util.VOTool;
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
import com.igsapp.common.util.annotation.CallMethod;
import com.igsapp.common.util.annotation.TxBean;
import com.igsapp.wibc.dataobj.Context.RequestContext;
import com.igsapp.wibc.dataobj.Context.ResponseContext;

@SuppressWarnings({ "unchecked", "deprecation" })
@TxBean
public class XXZX_0101 extends UCBean {

    /** 靜態的 log 物件 **/
    private static final Logger log = Logger.getLogger(XXZX_0100.class);

    /** 此 TxBean 程式碼共用的 ResponseContext */
    private ResponseContext resp;

    /** 此 TxBean 程式碼共用的 ReturnMessage */
    private ReturnMessage msg;

    /** 此 TxBean 程式碼共用的 UserObject */
    private UserObject user;

    /** 覆寫父類別的 start() 以強制於每次 Dispatcher 呼叫 method 時都執行程式自定的初始動作 **/
    public ResponseContext start(RequestContext req) throws TxException, ServiceException {
        super.start(req); // 一定要 invoke super.start() 以執行權限檢核
        initApp(req); // 呼叫自定的初始動作
        return null;
    }

    /**
     * 程式自定的初始動作，通常為取出 ResponseContext, UserObject, 及設定 ReturnMessage 及 response
     * code.
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
    @CallMethod(action = "prompt", url = "/XX/ZX/XXZX_0100/XXZX0101.jsp")
    public ResponseContext doPrompt(RequestContext req) {

        try {
            VOTool.setParamsFromLP_JSON(req);
            Map reqMap = VOTool.requestToMap(req);
            List<Map> rtnList = new XX_ZX0100().query(reqMap);

            String ACTION_TYPE = req.getParameter("ACTION_TYPE");

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String time = df.format(now);

            reqMap.put("OP_STATUS", "輸入中");
            reqMap.put("UPDT_DATE", time);
            reqMap.put("UPDT_NM", user.getEmpName());
            resp.addOutputData("reqMap", reqMap);
            resp.addOutputData("hid_DEP_NM", "投資程設科");

            if (EMP_ID != null) {
                reqMap = new XX_ZX0100().query(reqMap).get(0);
                reqMap.put("UPDT_DATE", MapUtils.getString(reqMap, "UPDT_DATE").substring(0, 19));
                reqMap.put("UPDT_NM", user.getEmpName());
                reqMap.put("ACTION_TYPE", "U");
                resp.addOutputData("hid_DEP_NM", "投資程設科");
                resp.addOutputData("reqMap", reqMap);
            }
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "查詢完成");
        } catch (Exception e) {
            log.error("查詢失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "查無資料");
        }

        return resp;
    }

    /**
     * 修改
     * 
     * @param req
     * @return
     */
    @CallMethod(action = "update", type = CallMethod.TYPE_AJAX)
    public ResponseContext doUpdate(RequestContext req) {

        try {
            VOTool.setParamsFromLP_JSON(req);
            Map reqMap = VOTool.requestToMap(req);

            Transaction.begin();
            try {
                new XX_ZX0100().update(reqMap, user);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }

            try {
                reqMap = new XX_ZX0100().query(reqMap).get(0);
                reqMap.put("UPDT_DATE", MapUtils.getString(reqMap, "UPDT_DATE").substring(0, 19));
                reqMap.put("UPDT_NM", user.getEmpName());
                reqMap.put("ACTION_TYPE", "U");
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
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "作業失敗");
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
    @CallMethod(action = "insert", type = CallMethod.TYPE_AJAX)
    public ResponseContext doInsert(RequestContext req) {

        try {
            VOTool.setParamsFromLP_JSON(req);
            Map reqMap = VOTool.requestToMap(req);

            Transaction.begin();
            try {
                new XX_ZX0100().insert(reqMap, user);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }

            try {
                reqMap = new XX_ZX0100().query(reqMap).get(0);
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
    @CallMethod(action = "submit", type = CallMethod.TYPE_AJAX)
    public ResponseContext doSubmit(RequestContext req) {

        try {

            VOTool.setParamsFromLP_JSON(req);
            Map reqMap = VOTool.requestToMap(req);
            String EMP_ID = req.getParameter("EMP_ID");

            DTXXTP01 vo1 = new DTXXTP01();
            vo1.setEMP_ID(EMP_ID);

            Transaction.begin();
            try {
                new RZ_N0Z001().approveFlow(FLOW_NO, "提交", "", user.getEmpID(), user.getDivNo());
                new XX_ZX0100().confirm(vo1);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            try {
                reqMap = new XX_ZX0100().query(reqMap).get(0);
                reqMap.put("ACTION_TYPE", "U");
                reqMap.put("UPDT_NM", user.getEmpName());
                reqMap.put("UPDT_DATE", MapUtils.getString(reqMap, "UPDT_DATE").substring(0, 19));
                resp.addOutputData("reqMap", reqMap);
                resp.addOutputData("hid_DEP_NM", hid_DEP_NM);
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
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "員工編號錯誤");
            }
        } catch (Exception e) {
            log.error("作業失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "員工編號錯誤");
        }
        return resp;
    }

    /**
     * 審核
     * 
     * @param req
     * @return
     */
    @CallMethod(action = "approve", type = CallMethod.TYPE_AJAX)
    public ResponseContext doApprove(RequestContext req) {

        try {
            VOTool.setParamsFromLP_JSON(req);
            Map reqMap = VOTool.requestToMap(req);
            String EMP_ID = req.getParameter("EMP_ID");

            DTXXTP01 vo1 = new DTXXTP01();
            vo1.setEMP_ID(EMP_ID);

            Transaction.begin();
            try {
                new RZ_N0Z001().approveFlow(FLOW_NO, "審核", "", user.getEmpID(), user.getDivNo());
                new XX_ZX0100().approve(vo1);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            try {
                reqMap = new XX_ZX0100().query(reqMap).get(0);
                reqMap.put("ACTION_TYPE", "U");
                reqMap.put("UPDT_NM", user.getEmpName());
                reqMap.put("UPDT_DATE", MapUtils.getString(reqMap, "UPDT_DATE").substring(0, 19));
                resp.addOutputData("reqMap", reqMap);
                resp.addOutputData("hid_DEP_NM", hid_DEP_NM);
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
    @CallMethod(action = "reject", type = CallMethod.TYPE_AJAX)
    public ResponseContext doReject(RequestContext req) {

        try {
            VOTool.setParamsFromLP_JSON(req);
            Map reqMap = VOTool.requestToMap(req);
            String EMP_ID = req.getParameter("EMP_ID");

            DTXXTP01 vo1 = new DTXXTP01();
            vo1.setEMP_ID(EMP_ID);

            Transaction.begin();
            try {
                new RZ_N0Z001().rejectFlow(FLOW_NO, "退回", "", user.getEmpID(), user.getDivNo());
                new XX_ZX0100().reject(vo1);
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                throw e;
            }
            try {
                reqMap = new XX_ZX0100().query(reqMap).get(0);
                reqMap.put("ACTION_TYPE", "U");
                reqMap.put("UPDT_NM", user.getEmpName());
                reqMap.put("UPDT_DATE", MapUtils.getString(reqMap, "UPDT_DATE").substring(0, 19));
                resp.addOutputData("reqMap", reqMap);
                resp.addOutputData("hid_DEP_NM", hid_DEP_NM);
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
    @CallMethod(action = "delete", type = CallMethod.TYPE_AJAX)
    public ResponseContext doDelete(RequestContext req) {

        try {
            VOTool.setParamsFromLP_JSON(req);
            Map reqMap = VOTool.requestToMap(req);

            Transaction.begin();
            try {
                new XX_ZX0100().delete(reqMap);
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
