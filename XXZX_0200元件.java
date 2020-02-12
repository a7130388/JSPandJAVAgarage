package com.cathay.xx.zx.trx;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cathay.common.bo.ReturnMessage;
import com.cathay.common.im.util.VOTool;
import com.cathay.common.message.MessageHelper;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.trx.UCBean;
import com.cathay.common.util.IConstantMap;
import com.cathay.rz.b0.module.RZ_B00301;
import com.cathay.util.ReturnCode;
import com.igsapp.common.trx.ServiceException;
import com.igsapp.common.trx.TxException;
import com.igsapp.common.util.annotation.CallMethod;
import com.igsapp.common.util.annotation.TxBean;
import com.igsapp.wibc.dataobj.Context.RequestContext;
import com.igsapp.wibc.dataobj.Context.ResponseContext;


@SuppressWarnings("unchecked")
@TxBean
public class XXZX_0200 extends UCBean {

    /** 靜態的 log 物件 **/
    private static final Logger log = Logger.getLogger(XXZX_0200.class);

    /** 此 TxBean 程式碼共用的 ResponseContext */
    private ResponseContext resp;

    /** 此 TxBean 程式碼共用的 ReturnMessage */
    private ReturnMessage msg;

    /** 此 TxBean 程式碼共用的 UserObject */
    private UserObject user;

    /** 覆寫父類別的 start() 以強制於每次 Dispatcher 呼叫 method 時都執行程式自定的初始動作 **/
    public ResponseContext start(RequestContext req) throws TxException, ServiceException {
        super.start(req); //一定要 invoke super.start() 以執行權限檢核
        initApp(req); //呼叫自定的初始動作
        return null;
    }

    /**
     * 程式自定的初始動作，通常為取出 ResponseContext, UserObject, 
     * 及設定 ReturnMessage 及 response code.
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
     * @param req
     * @return
     */
    @CallMethod(action = "prompt", url="/XX/ZX/XXZX_0200/XXZX0200.jsp")
    public ResponseContext doPrompt(RequestContext req){
        try{
            
            String COMPANY = "0200000";
            List<Map> rtnList = new RZ_B00301().unitListQuery(COMPANY, null, null, user.getEmpID(), 1);     
            resp.addOutputData("rtnList", VOTool.toJSON(rtnList));
            
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "查詢完成");
        } catch (Exception e) {
            log.error("查詢失敗", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "查無資料");
        }
    	return resp;
    }    

}
