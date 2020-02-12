<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!--
程式：XXZX0200.jsp
功能：單位代號索引
作者：陳正穎	
完成：2019/3/15
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

<script type='text/javascript'>
<%-- 產生畫面物件 --%>
<%--透過 prototype 的 Event 物件監聽 onload 事件，觸發時進行 initApp()--%>
Event.observe(window, 'load', new XXZX0100().initApp);

function XXZX0100(){

	<%-- TablE UI物件 --%>
	var grid;		
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${dispatcher}/XXZX_0200/');	
	var FUNC_ID = 'XXZX0100';
	
	/*
	<%--畫面檢核--%>
	function initValidation(){
		valid = new Validation('form1', {useTitles:false});
		valid.define( 'validate-Date-Interval' , [{ id : 'BAL_DT_S' },{ id : 'BAL_DT_E' }] );			
	}*/
	<%--初始時就執行查詢--%>
	function prompt(){	
		grid.load(${rtnList});	
	}	
	<%--按鈕的方法--%>
	var button = {
			Enter:function(){
				var records = grid.getCheckedRecords();
				if(records.length == 0){
					alert('請選取一筆資料');
					return;
				}			
				var dataMap = records[0];
				if(window.isPopupWin && window.popupWinBack){
					// 呼叫callback Function(若有) 並關閉
					window.popupWinBack(dataMap); 
				}else if(window.parent && window.parent.popupWin){
					//呼叫callback Function(若有) 並關閉
					window.parent.popupWin.back(dataMap);
				}else{
					CSRUtil.linkBack();			   
				}
			},
			Back:function(){
				if(window.isPopupWin && window.popupWinBack){
				// 呼叫callback Function(若有) 並關閉
				window.popupWinBack(); 
				}else if(window.parent && window.parent.popupWin){
					//呼叫callback Function(若有) 並關閉
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
				'程式開發訓練',
				'單位代號索引'
			);
			grid = 
				 new TableUI({
                table: $("grid"),
				autoCheckBox:{type:'radio'}, 
                column:[
                        {header: "部門名稱" , key:'text' },
                        {header: "部門代號" , key: 'value', render : function(record,value,sn){
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
		<input type="button" class="button" id="btnEnter" name="btnEnter" value="確認" >
		<input type="button" class="button" id="btnCancer" name="btnCancer" value="取消" >
	</td>
</table>
</form>
</body>
</html>