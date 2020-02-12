package com.cathay.xx.zx.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.im.util.VOTool;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.util.DATE;
import com.cathay.common.util.db.DBUtil;
import com.cathay.rz.n0.module.RZ_N0Z001;
import com.cathay.util.Transaction;
import com.cathay.xx.vo.DTXXTP01;
import com.igsapp.db.DataSet;

/**
 * . �@�B �{���\�෧�n�����G �ҲզW�� ���u�򥻸�Ƭd�߼Ҳ� �Ҳ�ID XX_ZX0100 ���n���� ���u�򥻸�Ƭd�߼Ҳ�
 * 
 * @author i9400563 �����o //TODO
 * @since 2019/02/25
 */
@SuppressWarnings("rawtypes")
public class XX_ZX0100 {

    private static final String SQL_QUERY_001 = "com.cathay.xx.zx.module.XX_ZX0100.SQL_QUERY_001"; // TODO

    private static final String SQL_INSERT_001 = "com.cathay.xx.zx.module.XX_ZX0100.SQL_INSERT_001";

    private static final String SQL_UPDATE_001 = "com.cathay.xx.zx.module.XX_ZX0100.SQL_UPDATE_001";

    private static final String SQL_DELETE_001 = "com.cathay.xx.zx.module.XX_ZX0100.SQL_DELETE_001";

    /**
     * �d�߭��u�򥻸��
     * 
     * @param reqMap
     * @return
     * @throws ModuleException
     */
    public List<Map> query(Map reqMap) throws ModuleException {

        String EMP_ID = MapUtils.getString(reqMap, "EMP_ID");
        String DIV_NO = MapUtils.getString(reqMap, "DIV_NO");

        DataSet ds = Transaction.getDataSet(); // ���o�s�u

        if (StringUtils.isNotBlank(EMP_ID)) {
            ds.setField("EMP_ID", EMP_ID); // �]�w�Ѽ�
        }
        StringBuilder DIVNO = new StringBuilder();
        if (StringUtils.isNotBlank(DIV_NO)) {
            ds.setField("DIV_NO", DIVNO.append("%").append(DIV_NO).append("%").toString()); // TODO string ->
                                                                                            // stringBuilder
        }

        DBUtil.searchAndRetrieve(ds, SQL_QUERY_001); // ��ƳB�z
        List<Map> rtnList = new ArrayList<Map>(); // TODO
        rtnList = VOTool.dataSetToMaps(ds);

        new RZ_N0Z001().putOP_STATUS_NM(rtnList, "XXZX0101");

        return rtnList;

    }

    /**
     * �s�W�H���򥻸��
     * 
     * @param reqMap
     * @param user
     * @throws ModuleException
     */
    public void insert(Map reqMap, UserObject user) throws ModuleException {

        String EMP_ID = MapUtils.getString(reqMap, "EMP_ID");
        String DIV_NO = MapUtils.getString(reqMap, "DIV_NO");
        String EMP_NAME = MapUtils.getString(reqMap, "EMP_NAME");
        String BIRTHDAY = MapUtils.getString(reqMap, "BIRTHDAY");
        String POSITION = MapUtils.getString(reqMap, "POSITION");

        ErrorInputException eie = null;

        if (StringUtils.isBlank(EMP_ID)) { // �ˬd�O�_����J�榡���~
            eie = this.getEie(eie, "���u�s�����i����");
        }
        if (StringUtils.isBlank(DIV_NO)) {
            eie = this.getEie(eie, "���N�����i����");
        }
        if (StringUtils.isBlank(EMP_NAME)) {
            eie = this.getEie(eie, "���u�m�W���i����");
        }
        if (StringUtils.isBlank(BIRTHDAY)) {
            eie = this.getEie(eie, "���u�ͤ餣�i����");
        }
        if (StringUtils.isBlank(POSITION)) {
            eie = this.getEie(eie, "���u¾�줣�i����");
        }

        if (eie != null) {
            throw eie;
        }

        RZ_N0Z001 theRZ_N0Z001 = new RZ_N0Z001();

        String FLOW_NO = theRZ_N0Z001.startFlow("XXZX0101", "�s�W", "", user.getEmpID(), user.getDivNo());

        DataSet ds = Transaction.getDataSet();

        ds.setField("EMP_ID", reqMap.get("EMP_ID")); // �]�wds���e
        ds.setField("UPDT_ID", user.getEmpID());
        ds.setField("UPDT_DATE", DATE.currentTime());
        ds.setField("FLOW_NO", FLOW_NO);
        ds.setField("DIV_NO", DIV_NO);
        ds.setField("EMP_NAME", reqMap.get("EMP_NAME"));
        ds.setField("BIRTHDAY", reqMap.get("BIRTHDAY"));
        ds.setField("POSITION", reqMap.get("POSITION"));
        ds.setField("OP_STATUS", "10");

        DBUtil.executeUpdate(ds, SQL_INSERT_001); // �Nds���e�a�JSQL����
    }

    /**
     * ��s�H���򥻸��
     * 
     * @param reqMap
     * @param user
     * @throws ModuleException
     */
    public void update(Map reqMap, UserObject user) throws ModuleException {

        String EMP_ID = MapUtils.getString(reqMap, "EMP_ID");

        ErrorInputException eie = null;

        if (StringUtils.isBlank(EMP_ID)) {
            eie = this.getEie(eie, "���u�s�����i����");
        }

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet();

        ds.setField("EMP_ID", reqMap.get("EMP_ID"));

        ds.setField("UPDT_ID", user.getEmpID());
        ds.setField("UPDT_DATE", DATE.currentTime());
        ds.setField("DIV_NO", reqMap.get("DIV_NO"));
        ds.setField("EMP_NAME", reqMap.get("EMP_NAME"));
        ds.setField("BIRTHDAY", reqMap.get("BIRTHDAY"));
        ds.setField("POSITION", reqMap.get("POSITION"));

        DBUtil.executeUpdate(ds, SQL_UPDATE_001);
    }

    /**
     * �R�����u�򥻸��
     * 
     * @param reqMap
     * @throws ModuleException
     */
    public void delete(Map reqMap) throws ModuleException {

        String EMP_ID = MapUtils.getString(reqMap, "EMP_ID");

        ErrorInputException eie = null;

        if (StringUtils.isBlank(EMP_ID)) {
            eie = this.getEie(eie, "���u�s�����i����");
        }

        if (eie != null) {
            throw eie;
        }

        DataSet ds = Transaction.getDataSet();
        ds.setField("EMP_ID", reqMap.get("EMP_ID"));
        DBUtil.executeUpdate(ds, SQL_DELETE_001);
    }

    /**
     * confirm(vo)����
     * 
     * @param vo
     * @throws ModuleException
     */
    public void confirm(DTXXTP01 vo) throws ModuleException {
        vo.setOP_STATUS(20);
        VOTool.update(vo);
    }

    /**
     * reject(vo)�h�^
     * 
     * @param vo
     * @throws ModuleException
     */
    public void reject(DTXXTP01 vo) throws ModuleException {
        vo.setOP_STATUS(10);
        VOTool.update(vo);
    }

    /**
     * approve(vo)�f��
     * 
     * @param vo
     * @throws ModuleException
     */
    public void approve(DTXXTP01 vo) throws ModuleException {
        vo.setOP_STATUS(30);
        VOTool.update(vo);
    }

    /**
     * �]�w���~�T��
     * 
     * @param eie
     * @param msg
     * @return
     */

    private ErrorInputException getEie(ErrorInputException eie, String msg) {
        if (eie == null) {
            eie = new ErrorInputException();
        }
        eie.appendMessage(msg);
        return eie;
    }

}
