<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!--
程式：XXZX0101.jsp
功能：基本EBAF練習
作者：陳正穎
完成：2019/03/13
-->

<html>
<head>
<%@ include file='/html/CM/header.jsp'%>
<%@ include file='/html/CM/msgDisplayer.jsp'%>
<!--匯入外部Javascript 與 css-->

<title></title>
<link href='${cssBase}/cm.css' rel='stylesheet' type='text/css'>

<script type='text/javascript'>

function XXZX0101(){	
	return {
	
		initApp : function() {
			
			var GOBACK= '${GOBACK}';
			if(GOBACK==5){
				Goback();
			}
			
			displayMessage();
			status();	

		}	
	};	
}	

<%--判斷ACTION TYPE的值--%>
function status(){
	var ACTION_TYPE = "${reqMap.ACTION_TYPE}";
	var OP_STATUS = "${reqMap.OP_STATUS}";
	var UPDT_NM='${reqMap.UPDT_NM}';

	if(ACTION_TYPE == "I"){	//TODO == ===			
		document.getElementById("btnUpdate").disabled = true;
		document.getElementById("btnSubmit").disabled = true;
		document.getElementById("btnApprove").disabled = true;
		document.getElementById("btnBack").disabled = true;
		document.getElementById("btnDelete").disabled = true;		
	}
	if(ACTION_TYPE == "U"){
		document.getElementById("btnInsert").disabled = true;
		if(OP_STATUS =="10"){
			document.getElementById("btnApprove").disabled = true;
			document.getElementById("btnBack").disabled = true;
		}
		else if(OP_STATUS =="20"){
			document.getElementById("btnSubmit").disabled = true;
		}
		else if(OP_STATUS =="30"){
			document.getElementById("btnUpdate").disabled = true;
			document.getElementById("btnSubmit").disabled = true;
			document.getElementById("btnBack").disabled = true;
			document.getElementById("btnApprove").disabled = true;
			document.getElementById("btnDelete").disabled = true;
		}
	}
}

<%--執行修改--%>
function Update(){
	var form = document.getElementById("form1");
	form.ACTION_TYPE.value = "U"; //TODO
	form.action = "${dispatcher}/XXZX_0101/update";
	form.submit();	
}

<%--執行新增--%>
function Insert(){
	var form = document.getElementById("form1");
	form.ACTION_TYPE.value = "U"; //TODO?
	form.OP_STATUS.value = "10"; //TODO?
	form.action = "${dispatcher}/XXZX_0101/insert";
	form.submit();

}

<%--執行提交--%>
function Submit(){
	var form = document.getElementById("form1");
	form.ACTION_TYPE.value = "U";
	form.action = "${dispatcher}/XXZX_0101/submit";
	form.submit();
}

<%--執行審核--%>
function Approve(){
	var form = document.getElementById("form1");
	form.ACTION_TYPE.value = "U";
	form.action = "${dispatcher}/XXZX_0101/approve";
	form.submit();
}

<%--執行退回--%>
function Reject(){
	var form = document.getElementById("form1");
	form.ACTION_TYPE.value = "U";
	form.action = "${dispatcher}/XXZX_0101/reject";
	form.submit();
}

<%--執行刪除--%>
function Delete(){
	var form = document.getElementById("form1");
	form.ACTION_TYPE.value = "U";
	form.action = "${dispatcher}/XXZX_0101/delete";
	form.submit();
}

<%--執行回到XXZX_0100--%>
function Goback(){
	var form = document.getElementById("form1");
	form.action = "${dispatcher}/XXZX_0100/query";
	form.submit();
}

<%--索引按鈕--%>
function serch(){
	var form = document.getElementById("form1");
	window.open('${dispatcher}/XXZX_0200/prompt', '員工單位索引', '');
	form.DIV_NO.value='${reqMap.DEP_CODE}';
}	

</script>
</head>
<body bgcolor="#F0FBC6" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="new XXZX0101().initApp()" onResize="fix()" onScroll="fix()">
<span id="bar1" style="position:absolute; left:0; top:0; width:100%; z-index:9; visibility: visible;" > 
	<table width="100%" height="30" border="0" cellpadding="0" cellspacing="0">
		<tr> 
			<td width="4" background="${imageBase}/CM/border_01.gif">
				<img src="${imageBase}/CM/border_01.gif" width="4" height="12">
			</td>
			<td valign="top" bgcolor="#F0FBC6" class="RI_pageOutter">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" background="${imageBase}/CM/border_03.gif">
	        		<tr> 
	          			<td height="4"><img src="${imageBase}/CM/border_03.gif" width="12" height="4"></td>
	        		</tr>
	      		</table>
	      		<!-- InstanceBeginEditable name="title" -->
				<table width="100%" border="0" cellpadding="2" cellspacing="0" class="subTitle">
					<tr> 
						<td width="20" height="24"> <div align="center"><font size="-5">●</font></div></td>
						<td><b>程式開發訓練</b></td>
						<td> 
						  <div align="right">畫面編號：XXZX0101 </div>
						</td>
					</tr>
				</table>
	      	<!-- InstanceEndEditable -->
	      	</td>
		    <td width="4" background="${imageBase}/CM/border_02.gif"><img src="${imageBase}/CM/border_02.gif" width="4" height="12"></td>
		    <td width="5" class="tbBox"><img src="${imageBase}/CM/ecblank.gif" width="5" height="1"></td>
		</tr>
	</table>
</span> 
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" style="padding-top:30px">
	<tr> 
		<td width="4" background="${imageBase}/CM/border_01.gif"><img src="${imageBase}/CM/border_01.gif" width="4" height="12"></td>
		<td width="100%" valign="top" bgcolor="#F0FBC6">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr> 
					<td valign="top">
						<table width=97% border=0 align=center cellpadding="0" cellspacing="1" bgcolor="#003366">
							<thead>
								<tr> 
									<td class="tbBox2"> <img src="${imageBase}/CM/icon_dot11.gif" width="18" height="16">員工基本資料維護</td>
								</tr>
								<tr bgcolor=#FFFFFF>
								</tr>
							</thead>												
						</table>
						<table width=97% border=0 align=center cellpadding="0" cellspacing="1" bgcolor="#003366">
							<form id="form1" width=97% border=0 align=center cellpadding="0"cellspacing="1" class="tbBox2" >
								<tr>
									<td width="10%" class="tbYellow" align="center">身分證字號</td>
									<td width="30%" class="tbYellow2" align="center">
										<input id="EMP_ID" name="EMP_ID" type="text" size="20" maxlength="10" class="textBox2" value="${reqMap.EMP_ID}" />
									</td>
									<td width="10%" class="tbYellow" align="center">姓名</td>
									<td width="30%" class="tbYellow2" align="center">
										<input id="EMP_NAME" name="EMP_NAME" type="text" size="20" maxlength="7" class="textBox2" value="${reqMap.EMP_NAME}" />
									</td>									
								</tr>
								<tr>
									<td width="10%" class="tbYellow" align="center">單位代號</td>
									<td width="30%" class="tbYellow2" align="center">
										<input id="DIV_NO" name="DIV_NO" type="text" size="10" maxlength="10" class="textBox2" value="${reqMap.DIV_NO}" />
										<span id="DEP_NM" name="DEP_NM" >${hid_DEP_NM}</span>
										<input type="hidden" id="hid_DEP_NM" name="hid_DEP_NM" value= "${hid_DEP_NM}"/>								
										<input type="button" class="button" id="btnSerch" name="btnSerch" value="索引" onClick="serch()"/>
									</td>
									<td width="10%" class="tbYellow" align="center">生日</td>
									<td width="30%" class="tbYellow2" align="center">
										<input id="BIRTHDAY" name="BIRTHDAY" type="text" size="20" maxlength="20" class="textBox2" value="${reqMap.BIRTHDAY}" />
									</td>									
								</tr>
								<tr>
									<td width="10%" class="tbYellow" align="center">職級</td>
									<td width="30%" class="tbYellow2" align="center">
										<input id="POSITION" name="POSITION" type="text" size="20" maxlength="10" class="textBox2" value="${reqMap.POSITION}" />
									</td>
									<td width="10%" class="tbYellow" align="center">審批狀態</td>
									<td width="30%" class="tbYellow2" align="center">
										<input type="hidden" id="OP_STATUS" name="OP_STATUS" value= "${reqMap.OP_STATUS}">
										${reqMap.OP_STATUS_NM}
									</td>									
								</tr>
								<tr>
									<td width="10%" class="tbYellow" align="center">輸入人員</td>
									<td width="30%" class="tbYellow2" align="center">
										<input type="hidden" id="UPDT_NM" name="UPDT_NM" value= "${reqMap.UPDT_NM}">
										${reqMap.UPDT_NM}										
									</td>
									<td width="10%" class="tbYellow" align="center">輸入時間</td>
									<td width="30%" class="tbYellow2" align="center">
										<input type="hidden" id="ACTION_TYPE" name="ACTION_TYPE" value= "${reqMap.ACTION_TYPE}">
										<input type="hidden" id="UPDT_DATE" name="UPDT_DATE" value= "${reqMap.UPDT_DATE}">
										<input type="hidden" id="FLOW_NO" name="FLOW_NO" value= "${reqMap.FLOW_NO}">
										${reqMap.UPDT_DATE}									
									</td>									
								</tr>
								<table width=97% border=0 align=center cellpadding="0" cellspacing="1" bgcolor="#003366">
									<tr>
										<td width="97%" class="tbYellow" align="center">
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
							</form>
						</table>
					</td>
				</tr>
			</table>
		<!-- InstanceEndEditable -->
		</td>
		<td width="4" background="${imageBase}/CM/border_02.gif"><img src="${imageBase}/CM/border_02.gif" width="4" height="12"></td>
		<td width="5" class="tbBox"><img src="${imageBase}/CM/ecblank.gif" width="5" height="1"></td>
	</tr>
</table>
</body>
</html>