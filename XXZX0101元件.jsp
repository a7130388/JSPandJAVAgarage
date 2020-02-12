<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>

<!--
程式：XXZX0101.jsp
功能：員工基本資料維護	
作者：陳正穎
完成：2019/03/15
-->

<html>
<head>
<%@ include file='/html/CM/header.jsp'%>
<%@ include file='/html/CM/msgDisplayer.jsp'%>
<!--匯入外部Javascript 與 css-->

<title></title>
<link href='${cssBase}/cm.css' rel='stylesheet' type='text/css'>

<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/prototype.js'></script>
<!-- 必要 -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/CSRUtil.js'></script>
<!-- 必要 -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/TableUI.js'></script>
<!-- 目前系統常用的表格工具 -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/PageUI.js'></script>
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/validation.js'></script>
<script type='text/JavaScript' src='${htmlBase}/CM/js/calendar.js'></script>
<script type='text/javaScript' src='${htmlBase}/CM/js/ui/LocaleDisplay.js'></script>
<script type="text/javascript" src="${htmlBase}/CM/js/ui/popupWin.js"></script>

<script type='text/javascript'>
<%-- 產生畫面物件 --%>
<%--透過 prototype 的 Event 物件監聽 onload 事件，觸發時進行 initApp()--%>
Event.observe(window, 'load', new XXZX0100().initApp);

function XXZX0100(){

	<%-- TablE UI物件 --%>
	var grid;		
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${dispatcher}/XXZX_0101/');	
	var FUNC_ID = 'XXZX0100';	
	var valid;
	
	<%--畫面檢核--%>
	function initValidation(){
		valid = new Validation('form1', {useTitles:false});
		valid.define( 'required' , [{ id : 'EMP_ID' }]);			
	}
	
	<%--檢查ACTION_TYPE和OP_STATUS來顯示按鈕--%>
	function status(){ debugger;
		var ACTION_TYPE = $F('ACTION_TYPE'); //TODO  $F('ACTION_TYPE')  //setValue getValue  //update....
		var OP_STATUS = $F('OP_STATUS');
		if(ACTION_TYPE == "I"){				
			$('btnUpdate').disabled = true; //TODO $('btnUpdate').enable();
			$('btnSubmit').disabled = true;
			$('btnApprove').disabled = true;
			$('btnBack').disabled = true;
			$('btnDelete').disabled = true;		
		}
		if(ACTION_TYPE == "U"){
			$('btnInsert').disabled = true;
			$('btnUpdate').disabled = false;
			$('btnBack').disabled = false;
			$('btnDelete').disabled = false;
			if(OP_STATUS =="10"){
				$('btnApprove').disabled = true;
				$('btnBack').disabled = true;
				$('btnSubmit').disabled = false;
				$('OP_STATUS_STR').update("輸入件");
			}
			if(OP_STATUS =="20"){
				$('btnSubmit').disabled = true;
				$('btnApprove').disabled = false;
				$('btnBack').disabled = false;
				$('OP_STATUS_STR').update("待審件");
			}
			if(OP_STATUS =="30"){
				$('btnUpdate').disabled = true;
				$('btnSubmit').disabled = true;
				$('btnBack').disabled = true;
				$('btnApprove').disabled = true;
				$('btnDelete').disabled = true;
				$('OP_STATUS_STR').update("已審件");
			}
		}
	}

	<%--將從後端讀取到的資料顯示在頁面表格上--%>
	function formset(i){ debugger;
		$('EMP_ID').value = i.reqMap.EMP_ID;
		$('EMP_NAME').value = i.reqMap.EMP_NAME;
		$('DIV_NO').value = i.reqMap.DIV_NO;
		$('BIRTHDAY').value = i.reqMap.BIRTHDAY;
		$('POSITION').value = i.reqMap.POSITION;
		$('ACTION_TYPE').value =i.reqMap.ACTION_TYPE;
		$('FLOW_NO').value = i.reqMap.FLOW_NO;
		$('OP_STATUS').value = i.reqMap.OP_STATUS;
		
		$('OP_STATUS_STR').update(i.reqMap.OP_STATUS_NM); //TODO
				
		$('DEP_NM').update(i.hid_DEP_NM);
		$('UPDT_DATE_SHOW').update(i.reqMap.UPDT_DATE);
	}

	<%--定義按鈕執行的方法--%>
	var actions = {
			<%--新增--%>
			Insert:function(){
				if(!valid.validate()){
					return;
				}							
				ajaxRequest.post( //post: function(action, params, successAction, failureAction){
					'insert', //==form.action
					actions.getData(), //{ key value}
					function(resp){ 	debugger;
						formset(resp);
						status();						
					}		
					);
			},
			<%--修改--%>
			Update:function(){
				if(!valid.validate()){
					return;
				}				
				ajaxRequest.post( //post: function(action, params, successAction, failureAction){
					'update', //==form.action
					actions.getData(), //{ key value}
					function(resp){ 	
						formset(resp);
						status();						
					}				
					);
			},
			<%--提交--%>
			Submit:function(){
				if(!valid.validate()){
					return;
				}				
				ajaxRequest.post( //post: function(action, params, successAction, failureAction){
					'submit', //==form.action
					actions.getData(), //{ key value}
					function(resp){ 	
						formset(resp);
						status();						
					}					
					);
			},
			<%--審核--%>
			Approve:function(){
				if(!valid.validate()){
					return;
				}				
				ajaxRequest.post( //post: function(action, params, successAction, failureAction){
					'approve', //==form.action
					actions.getData(), //{ key value}
					function(resp){ 	
						formset(resp);
						status();						
					}				
					);
			},
			<%--退回--%>
			Reject:function(){
				if(!valid.validate()){
					return;
				}				
				ajaxRequest.post( //post: function(action, params, successAction, failureAction){
					'reject', //==form.action
					actions.getData(), //{ key value}
					function(resp){ 	
						formset(resp);
						status();						
					}				
					);
			},
			<%--刪除--%>
			Delete:function(){
				if(!valid.validate()){
					return;
				}				
				ajaxRequest.post( //post: function(action, params, successAction, failureAction){
					'delete', //==form.action
					actions.getData(), //{ key value}
					function(resp){ 	
						//formset(resp);
						CSRUtil.linkBack();
						//status();						
					}				
					);
			},	
			<%--回上一頁--%>		
			Goback:function(){
				CSRUtil.linkBack();
			},
			<%--索引--%>			
			serch:function(){
				popupWin.popup({									
				src : '${dispatcher}/XXZX_0200/prompt' ,		
				scrolling : 'yes',	
				parameters : { 	
									
				},  	
				cb : function(obj){
					if(obj){		
						var str= obj.value;			
						$('DIV_NO').setValue(str.substring(0,(str.length)-3));						
						$('DEP_NM').update(obj.text);
						$('hid_DEP_NM').setValue(obj.text);
					}
				}		
			});		
			},	
			<%--取得form表單資料--%>		
			getData : function(){
			var reqData = {

				EMP_ID:$F('EMP_ID'),
				EMP_NAME:$F('EMP_NAME'), 
				DIV_NO:$F('DIV_NO'),
				hid_DEP_NM:$F('hid_DEP_NM'),				
				OP_STATUS:$F('OP_STATUS'),				
				ACTION_TYPE:$F('ACTION_TYPE'),
				UPDT_DATE:$F('UPDT_DATE'),
				FLOW_NO:$F('FLOW_NO'),
				BIRTHDAY:$F('BIRTHDAY'),
				POSITION:$F('POSITION')

			}		
			return reqData;
		}
	}
		
	return {
		<%--初始設定--%>
		initApp : function() {
			<cathay:LocaleDisplay sysCode='RG' var='localeDisplay' dateFields='BIRTHDAY' />
			PageUI.createPageWithAllBodySubElement(
				'XXZX0101',
				'程式開發訓練',
				'員工基本資料維護'
			);
			$('btnInsert').observe('click', actions.Insert);	
			$('btnUpdate').observe('click', actions.Update);
			$('btnSubmit').observe('click', actions.Submit);
			$('btnApprove').observe('click', actions.Approve);
			$('btnBack').observe('click', actions.Reject);
			$('btnDelete').observe('click', actions.Delete);
			$('btnGoback').observe('click', actions.Goback);
			$('btnSerch').observe('click', actions.serch);		 
		
			initValidation();
			PageUI.resize();
			status();
			displayMessage();		
		}	
	};	
}	


	
</script>
</head>
<body>
<form name='form1' id='form1' border=0 align=center cellpadding="0"cellspacing="1" class="tbBox2">
	<table id="grid2"  width=100% border=0 align=center cellpadding="0" cellspacing="1" bgcolor="#003366"class="textBold">
		<tr>
			<td width="10%" class="tbYellow" >身分證字號</td>
			<td width="30%" class="tbYellow2" >
				<input id="EMP_ID" name="EMP_ID" type="text" size="20" maxlength="10" class="textBox2" value="${reqMap.EMP_ID}" />
			</td>
			<td width="10%" class="tbYellow" >姓名</td>
			<td width="30%" class="tbYellow2" >
				<input id="EMP_NAME" name="EMP_NAME" type="text" size="20" maxlength="7" class="textBox2" value="${reqMap.EMP_NAME}" />
			</td>									
		</tr>
		<tr>
			<td width="10%" class="tbYellow" >單位代號</td>
			<td width="30%" class="tbYellow2" >
				<input id="DIV_NO" name="DIV_NO" type="text" size="10" maxlength="10" class="textBox2" value="${reqMap.DIV_NO}" />
				<span id="DEP_NM" name="DEP_NM" >${hid_DEP_NM}</span>
				<input type="hidden" id="hid_DEP_NM" name="hid_DEP_NM" value= "${hid_DEP_NM}"/>								
				<input type="button" class="button" id="btnSerch" name="btnSerch" value="索引"/>
			</td>
			<td width="10%" class="tbYellow" >生日</td>
			<td width="30%" class="tbYellow2" >
				<input id="BIRTHDAY" name="BIRTHDAY" type="text" size="20" maxlength="20" class="textBox2" value="${reqMap.BIRTHDAY}" />

			</td>									
		</tr>
		<tr>
			<td width="10%" class="tbYellow" >職級</td>
			<td width="30%" class="tbYellow2" >
				<input id="POSITION" name="POSITION" type="text" size="20" maxlength="10" class="textBox2" value="${reqMap.POSITION}" />
			</td>
			<td width="10%" class="tbYellow" >審批狀態</td>
			<td width="30%" class="tbYellow2" >
				<input type="hidden" id="OP_STATUS" name="OP_STATUS" value= "${reqMap.OP_STATUS}">
				<span id="OP_STATUS_STR">${reqMap.OP_STATUS}</span>
			</td>									
		</tr>
		<tr>
			<td width="10%" class="tbYellow" >輸入人員</td>
			<td width="30%" class="tbYellow2" >
				<input type="hidden" id="UPDT_NM" name="UPDT_NM" value= "${reqMap.UPDT_NM}">
				${reqMap.UPDT_NM}										
			</td>
			<td width="10%" class="tbYellow" >輸入時間</td>
			<td width="30%" class="tbYellow2" >
				<input type="hidden" id="ACTION_TYPE" name="ACTION_TYPE" value= "${reqMap.ACTION_TYPE}">
				<input type="hidden" id="UPDT_DATE" name="UPDT_DATE" value= "${reqMap.UPDT_DATE}">
				<input type="hidden" id="FLOW_NO" name="FLOW_NO" value= "${reqMap.FLOW_NO}">
				<span id="UPDT_DATE_SHOW">${reqMap.UPDT_DATE}</span>									
			</td>									
		</tr>
		<table width=100% border=0 align=center cellpadding="0" cellspacing="1" bgcolor="#003366">
			<tr>
				<td width="100%" class="tbYellow2" align="center">
					<input type="button" class="button" id="btnInsert" name="btnInsert" value="新增">
					<input type="button" class="button" id="btnUpdate" name="btnUpdate" value="修改">
					<input type="button" class="button" id="btnSubmit" name="btnSubmit" value="提交">
					<input type="button" class="button" id="btnApprove" name="btnApprove" value="審核" >
					<input type="button" class="button" id="btnBack" name="btnBack" value="退回">
					<input type="button" class="button" id="btnDelete" name="btnDelete" value="刪除">
					<input type="button" class="button" id="btnGoback" name="btnGoback" value="回上一頁">
				</td>
			</tr>
		</table>
	</table>
</form>
<table id="grid" class="grid">
</table>
</form>
</body>
</html>