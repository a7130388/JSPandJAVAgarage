package com.cathay.xx.zx.trx;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cathay.common.bo.ReturnMessage;
import com.cathay.common.exception.DataNotFoundException;
import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.exception.OverCountLimitException;
import com.cathay.common.im.util.VOTool;
import com.cathay.common.message.MessageHelper;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.trx.UCBean;
import com.cathay.common.util.IConstantMap;
import com.cathay.util.ReturnCode;
import com.cathay.xx.zx.module.XX_ZX0100;
import com.ibm.wsspi.sib.exitpoint.ra.HashMap; //TODO
import com.igsapp.common.trx.ServiceException;
import com.igsapp.common.trx.TxException;
import com.igsapp.wibc.dataobj.Context.RequestContext;
import com.igsapp.wibc.dataobj.Context.ResponseContext;

@SuppressWarnings("unchecked") // TODO
public class XXZX_0100 extends UCBean {

    /** 靜態的 log 物件 **/
    private static final Logger log = Logger.getLogger(XXZX_0100.class);

    /** 此 TxBean 程式碼共用的 ResponseContext */
    private ResponseContext resp;

    /** 此 TxBean 程式碼共用的 ReturnMessage */
    private ReturnMessage msg;

    /** 此 TxBean 程式碼共用的 UserObject */
    // private UserObject user;

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
        // user = this.getUserObject(req);
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

        return resp;
    }

    /**
     * 查詢資料
     * 
     * @param req
     * @return
     */
    @SuppressWarnings("deprecation")
    public ResponseContext doQuery(RequestContext req) {

        try {
            // TODO Map reqMap =VOTool.requestToMap(req);

            Map reqMap = VOTool.requestToMap(req);
            List<Map> rtnList = new XX_ZX0100().query(reqMap);

            String empId = req.getParameter("EMP_ID"); // TODO
            String divno = req.getParameter("DIV_NO");
            resp.addOutputData("EMP_ID", empId);
            resp.addOutputData("DIV_NO", divno);

            resp.addOutputData("rtnList", rtnList);
            resp.addOutputData("page", "1");

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "查詢完成!");

        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error(dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "查無資料");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                if (me.getRootException() instanceof OverCountLimitException) {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "查詢筆數超出系統限制，請縮小查詢範圍");
                } else {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "查詢失敗");
                }
            }
        } catch (Exception e) {
            log.error("查詢失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "查無資料");
        }
        return resp;
    }

}
