<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!--
�{���GXXZX0200.jsp
�\��G���N������
�@�̡G�����o	
�����G2019/3/15
-->

<html>
<head>
<%@ include file='/html/CM/header.jsp'%>
<%@ include file='/html/CM/msgDisplayer.jsp'%>
<!--�פJ�~��Javascript �P css-->

<title></title>
<link href='${cssBase}/cm.css' rel='stylesheet' type='text/css'>

<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/prototype.js'></script>
<!-- ���n -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/CSRUtil.js'></script>
<!-- ���n -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/TableUI.js'></script>
<!-- �ثe�t�α`�Ϊ����u�� -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/PageUI.js'></script>
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/validation.js'></script>
<script type='text/JavaScript' src='${htmlBase}/CM/js/calendar.js'></script>
<script type='text/javaScript' src='${htmlBase}/CM/js/ui/LocaleDisplay.js'></script>

<script type='text/javascript'>
<%-- ���͵e������ --%>
<%--�z�L prototype �� Event �����ť onload �ƥ�AĲ�o�ɶi�� initApp()--%>
Event.observe(window, 'load', new XXZX0100().initApp);

function XXZX0100(){

	<%-- TablE UI���� --%>
	var grid;		
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${dispatcher}/XXZX_0200/');	
	var FUNC_ID = 'XXZX0100';
	
	/*
	<%--�e���ˮ�--%>
	function initValidation(){
		valid = new Validation('form1', {useTitles:false});
		valid.define( 'validate-Date-Interval' , [{ id : 'BAL_DT_S' },{ id : 'BAL_DT_E' }] );			
	}*/
	<%--��l�ɴN����d��--%>
	function prompt(){	
		grid.load(${rtnList});	
	}	
	<%--���s����k--%>
	var button = {
			Enter:function(){
				var records = grid.getCheckedRecords();
				if(records.length == 0){
					alert('�п���@�����');
					return;
				}			
				var dataMap = records[0];
				if(window.isPopupWin && window.popupWinBack){
					// �I�scallback Function(�Y��) ������
					window.popupWinBack(dataMap); 
				}else if(window.parent && window.parent.popupWin){
					//�I�scallback Function(�Y��) ������
					window.parent.popupWin.back(dataMap);
				}else{
					CSRUtil.linkBack();			   
				}
			},
			Back:function(){
				if(window.isPopupWin && window.popupWinBack){
				// �I�scallback Function(�Y��) ������
				window.popupWinBack(); 
				}else if(window.parent && window.parent.popupWin){
					//�I�scallback Function(�Y��) ������
				window.parent.popupWin.back();
				}else{
					CSRUtil.linkBack();			   
				}
			}
		};
		
	return {
	
		initApp : function() {
			<cathay:LocaleDisplay sysCode='XX' var='localeDisplay' />
			PageUI.createPageWithAllBodySubElement(
				'XXZX0200',
				'�{���}�o�V�m',
				'���N������'
			);
			grid = 
				 new TableUI({
                table: $("grid"),
				autoCheckBox:{type:'radio'}, 
                column:[
                        {header: "�����W��" , key:'text' },
                        {header: "�����N��" , key: 'value', render : function(record,value,sn){
								  var str=String(value);
								  str = str.substring(0,(str.length)-3); 
                                  return str ;
						} }
                       
                        ]
                });		
			$('btnEnter').observe('click', button.Enter);
			$('btnCancer').observe('click', button.Back);
			prompt();
			PageUI.resize();
			displayMessage();		
		}	
	};	
}	
	
</script>
</head>
<body>
<form name='form1' id='form1' style='PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px'>
<table id="grid" class="grid">
</table>
<table  width=100% border=0 align=center cellpadding="0"cellspacing="1" class="tbBox2" align="center" >
	<td align="center">
		<input type="button" class="button" id="btnEnter" name="btnEnter" value="�T�{" >
		<input type="button" class="button" id="btnCancer" name="btnCancer" value="����" >
	</td>
</table>
</form>
</body>
</html>