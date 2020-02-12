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

    /** �R�A�� log ���� **/
    private static final Logger log = Logger.getLogger(XXZX_0100.class);

    /** �� TxBean �{���X�@�Ϊ� ResponseContext */
    private ResponseContext resp;

    /** �� TxBean �{���X�@�Ϊ� ReturnMessage */
    private ReturnMessage msg;

    /** �� TxBean �{���X�@�Ϊ� UserObject */
    // private UserObject user;

    /** �мg�����O�� start() �H�j���C�� Dispatcher �I�s method �ɳ�����{���۩w����l�ʧ@ **/
    public ResponseContext start(RequestContext req) throws TxException, ServiceException {
        super.start(req); // �@�w�n invoke super.start() �H�����v���ˮ�
        initApp(req); // �I�s�۩w����l�ʧ@
        return null;
    }

    /**
     * �{���۩w����l�ʧ@�A�q�`�����X ResponseContext, UserObject, �γ]�w ReturnMessage �� response
     * code.
     */
    private void initApp(RequestContext req) {
        // �إߦ� TxBean �q�Ϊ�����
        resp = this.newResponseContext();
        msg = new ReturnMessage();
        // user = this.getUserObject(req);
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

        return resp;
    }

    /**
     * �d�߸��
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

            MessageHelper.setReturnMessage(msg, ReturnCode.OK, "�d�ߧ���!");

        } catch (ErrorInputException eie) {
            log.error(eie);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_INPUT, eie.getMessage());
        } catch (DataNotFoundException dnfe) {
            log.error(dnfe);
            MessageHelper.setReturnMessage(msg, ReturnCode.DATA_NOT_FOUND, "�d�L���");
        } catch (ModuleException me) {
            if (me.getRootException() == null) {
                log.error(me);
                MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, me.getMessage());
            } else {
                log.error(me.getMessage(), me.getRootException());
                if (me.getRootException() instanceof OverCountLimitException) {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�d�ߵ��ƶW�X�t�έ���A���Y�p�d�߽d��");
                } else {
                    MessageHelper.setReturnMessage(msg, ReturnCode.ERROR_MODULE, "�d�ߥ���");
                }
            }
        } catch (Exception e) {
            log.error("�d�ߥ���", e);
            MessageHelper.setReturnMessage(msg, ReturnCode.ERROR, "�d�L���");
        }
        return resp;
    }

}
