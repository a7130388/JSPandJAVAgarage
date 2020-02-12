<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>

<!--
程式：XXZX0100.jsp
功能：員工資料查詢	
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
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/validation.js'></script><!--TODO-->
<script type='text/JavaScript' src='${htmlBase}/CM/js/calendar.js'></script><!--TODO-->
<script type='text/javaScript' src='${htmlBase}/CM/js/ui/LocaleDisplay.js'></script>

<script type='text/javascript'>
<%-- 產生畫面物件 --%>
<%--透過 prototype 的 Event 物件監聽 onload 事件，觸發時進行 initApp()--%>
Event.observe(window, 'load', new XXZX0100().initApp);

function XXZX0100(){

	<%-- TablE UI物件 --%>
	var grid;		
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${dispatcher}/XXZX_0100/');	
	var FUNC_ID = 'XXZX0100';
	var form ;
	var table2;
	var tbs;
	
	/*
	<%--畫面檢核--%>
	function initValidation(){
		valid = new Validation('form1', {useTitles:false});
		valid.define( 'validate-Date-Interval' , [{ id : 'BAL_DT_S' },{ id : 'BAL_DT_E' }] );			
	}*/

	<%--定義按鈕執行的方法--%>
	var actions = {		
		doQuery: function(){
			<%-- 發送AJAX --%>
			ajaxRequest.post( //post: function(action, params, successAction, failureAction){
				'query', //==form.action
				actions.getData(), //{ key value}
				function(resp){ <%-- 顯示查詢資料 --%>//成功 //if(successAction) {successAction(resp);}								
					grid.load(resp.rtnList||[]);//resp.rtnList||[]
				},			
				function(resp){ 	//失敗
					grid.load(resp.rtnList||[]);
					valid.reset() //清除所有檢核訊息
				});
		},
		<%--清除--%>
		doClear: function(){
			form.reset();
			grid.load([]); 			 		
		},
		<%--新增--%>
		toinsert:function(){
			var LP_JSON = {
				action :'${dispatcher}/XXZX_0101/prompt',	 	
				ACTION_TYPE : "I" 				
			}			
			CSRUtil.linkTo(LP_JSON , 'form1');
		},
		<%--取得form表單資料--%>
		getData : function(){
			var reqData = {
				EMP_ID:$F('EMP_ID'), 
				DIV_NO:$F('DIV_NO')				
			}		
			return reqData;
		}

	
	}

		
	return {
		<%--初始設定--%>
		initApp : function() {
			<cathay:LocaleDisplay sysCode='XX' var='localeDisplay' />
			PageUI.createPageWithAllBodySubElement(
				'XXZX0100',
				'程式開發訓練',
				'新進人員模擬程式'
			);
			form = $("form1");	
					
			grid =
				 new TableUI({
                table: $("grid"), 				
                column:[
                        {header: "序號" , key:'',render : function(record,value,sn){
                                  return sn ;
						}  },
                        {header: "員工編號" , key:'EMP_ID',
						render:function(rec , val){ 							
							if(val){
								//超連結至RGF0_0101(國際期貨特殊異動輸入明細作業)畫面，供使用者修改及提交特殊異動明細資料
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
                        {header: "員工姓名" , key:'EMP_NAME' },
                        {header: "單位代號" , key:'DIV_NO' },
						{header: "生日" , key:'BIRTHDAY' },
						{header: "職位" , key:'POSITION' }
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
			<td width="10%" class="tbYellow" align="center">員工編號</td>
			<td width="30%" class="tbYellow2" align="center">
				<input id="EMP_ID" name="EMP_ID" type="text" size="20" maxlength="10" class="textBox2" value="${EMP_ID}"  />
			</td>
			<td width="10%" class="tbYellow" align="center">單位代號</td>
			<td width="30%" class="tbYellow2" align="center">
				<input id="DIV_NO" name="DIV_NO" type="text" size="20" maxlength="7" class="textBox2" value="${DIV_NO}" />
			</td>
			<td width="20%" class="tbYellow2" align="center">
				<button class="button" id="btnQuery" onClick="doQuery()">查詢</button>
				<button class="button" id="btnClear" onClick="doClear(0)">清除</button>	
			</td>
		</tr>
		<tr>
			<table width=100% border=0 align=center cellpadding="0" cellspacing="1" >
				<td align="center" class="tbYellow2">
					<button class="button" id="btnInsert" onClick="toinsert()">新增</button>
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