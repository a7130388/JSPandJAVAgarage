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

    /** �R�A�� log ���� **/
    private static final Logger log = Logger.getLogger(XXZX_0200.class);

    /** �� TxBean �{���X�@�Ϊ� ResponseContext */
    private ResponseContext resp;

    /** �� TxBean �{���X�@�Ϊ� ReturnMessage */
    private ReturnMessage msg;

    /** �� TxBean �{���X�@�Ϊ� UserObject */
    private UserObject user;

    /** �мg�����O�� start() �H�j���C�� Dispatcher �I�s method �ɳ�����{���۩w����l�ʧ@ **/
    public ResponseContext start(RequestContext req) throws TxException, ServiceException {
        super.start(req); //�@�w�n invoke super.start() �H�����v���ˮ�
        initApp(req); //�I�s�۩w����l�ʧ@
        return null;
    }

    /**
     * �{���۩w����l�ʧ@�A�q�`�����X ResponseContext, UserObject, 
     * �γ]�w ReturnMessage �� response code.
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
     * @param req
     * @return
     */
    @CallMethod(action = "prompt", url="/XX/ZX/XXZX_0200/XXZX0200.jsp")
    public ResponseContext doPrompt(RequestContext req){
        try{
            
            String COMPANY = "0200000";
            List<Map> rtnList = new RZ_B00301().unitListQuery(COMPANY, null, null, user.getEmpID(), 1);     
            resp.addOutputData("rtnList", VOTool.toJSON(rtnList));
            
            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�d�ߧ���");
        } catch (Exception e) {
            log.error("�d�ߥ���", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�d�L���");
        }
    	return resp;
    }    

}
