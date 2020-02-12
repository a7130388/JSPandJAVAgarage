<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>

<!--
�{���GXXZX0100.jsp
�\��G���u��Ƭd��	
�@�̡G�����o
�����G2019/03/15
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
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/validation.js'></script><!--TODO-->
<script type='text/JavaScript' src='${htmlBase}/CM/js/calendar.js'></script><!--TODO-->
<script type='text/javaScript' src='${htmlBase}/CM/js/ui/LocaleDisplay.js'></script>

<script type='text/javascript'>
<%-- ���͵e������ --%>
<%--�z�L prototype �� Event �����ť onload �ƥ�AĲ�o�ɶi�� initApp()--%>
Event.observe(window, 'load', new XXZX0100().initApp);

function XXZX0100(){

	<%-- TablE UI���� --%>
	var grid;		
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${dispatcher}/XXZX_0100/');	
	var FUNC_ID = 'XXZX0100';
	var form ;
	var table2;
	var tbs;
	
	/*
	<%--�e���ˮ�--%>
	function initValidation(){
		valid = new Validation('form1', {useTitles:false});
		valid.define( 'validate-Date-Interval' , [{ id : 'BAL_DT_S' },{ id : 'BAL_DT_E' }] );			
	}*/

	<%--�w�q���s���檺��k--%>
	var actions = {		
		doQuery: function(){
			<%-- �o�eAJAX --%>
			ajaxRequest.post( //post: function(action, params, successAction, failureAction){
				'query', //==form.action
				actions.getData(), //{ key value}
				function(resp){ <%-- ��ܬd�߸�� --%>//���\ //if(successAction) {successAction(resp);}								
					grid.load(resp.rtnList||[]);//resp.rtnList||[]
				},			
				function(resp){ 	//����
					grid.load(resp.rtnList||[]);
					valid.reset() //�M���Ҧ��ˮְT��
				});
		},
		<%--�M��--%>
		doClear: function(){
			form.reset();
			grid.load([]); 			 		
		},
		<%--�s�W--%>
		toinsert:function(){
			var LP_JSON = {
				action :'${dispatcher}/XXZX_0101/prompt',	 	
				ACTION_TYPE : "I" 				
			}			
			CSRUtil.linkTo(LP_JSON , 'form1');
		},
		<%--���oform�����--%>
		getData : function(){
			var reqData = {
				EMP_ID:$F('EMP_ID'), 
				DIV_NO:$F('DIV_NO')				
			}		
			return reqData;
		}

	
	}

		
	return {
		<%--��l�]�w--%>
		initApp : function() {
			<cathay:LocaleDisplay sysCode='XX' var='localeDisplay' />
			PageUI.createPageWithAllBodySubElement(
				'XXZX0100',
				'�{���}�o�V�m',
				'�s�i�H�������{��'
			);
			form = $("form1");	
					
			grid =
				 new TableUI({
                table: $("grid"), 				
                column:[
                        {header: "�Ǹ�" , key:'',render : function(record,value,sn){
                                  return sn ;
						}  },
                        {header: "���u�s��" , key:'EMP_ID',
						render:function(rec , val){ 							
							if(val){
								//�W�s����RGF0_0101(��ڴ��f�S���ʿ�J���ӧ@�~)�e���A�ѨϥΪ̭ק�δ���S���ʩ��Ӹ��
								var link = new Element( 'a' , { href : "#" } ).update(val);	
								link.observe( 'click', function (event){
									
									var LP_JSON = {
										action :'${dispatcher}/XXZX_0101/prompt',	 	
										EMP_ID :rec.EMP_ID,
										ACTION_TYPE : "U" 
										
									}			
									CSRUtil.linkTo(LP_JSON , 'form1');
								});										
								return link; 														
							}
							return '';
						} },
                        {header: "���u�m�W" , key:'EMP_NAME' },
                        {header: "���N��" , key:'DIV_NO' },
						{header: "�ͤ�" , key:'BIRTHDAY' },
						{header: "¾��" , key:'POSITION' }
                        ]
                });	

			var LP_JSON = CSRUtil.isBackLink('form1',actions.doQuery);
			$('btnQuery').observe('click', actions.doQuery);
			$('btnClear').observe('click', actions.doClear);
			$('btnInsert').observe('click', actions.toinsert);
			PageUI.resize();
			displayMessage();		
		}	
	};	
}	
	
</script>
</head>
<body>
<form name='form1' id='form1' width=97% border=0 align=center cellpadding="0"cellspacing="1" class="tbBox2"><!--TODO-->
	<table width=100% border=0 align=center cellpadding="0" cellspacing="1" bgcolor="#003366"class="textBold">
		<tr>
			<td width="10%" class="tbYellow" align="center">���u�s��</td>
			<td width="30%" class="tbYellow2" align="center">
				<input id="EMP_ID" name="EMP_ID" type="text" size="20" maxlength="10" class="textBox2" value="${EMP_ID}"  />
			</td>
			<td width="10%" class="tbYellow" align="center">���N��</td>
			<td width="30%" class="tbYellow2" align="center">
				<input id="DIV_NO" name="DIV_NO" type="text" size="20" maxlength="7" class="textBox2" value="${DIV_NO}" />
			</td>
			<td width="20%" class="tbYellow2" align="center">
				<button class="button" id="btnQuery" onClick="doQuery()">�d��</button>
				<button class="button" id="btnClear" onClick="doClear(0)">�M��</button>	
			</td>
		</tr>
		<tr>
			<table width=100% border=0 align=center cellpadding="0" cellspacing="1" >
				<td align="center" class="tbYellow2">
					<button class="button" id="btnInsert" onClick="toinsert()">�s�W</button>
				</td>
			</table>
		</tr>
	</table>
</form>
<table id="grid" class="grid">
</table>
</form>
</body>
</html>