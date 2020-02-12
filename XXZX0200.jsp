<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!--
程式：XXZX0200.jsp
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

var pagenum=1;
var table2;
function XXZX0200(){	
	
	return {
	
		initApp : function() {
			table2= document.getElementById("table2");
			document.getElementById("allpage").innerHTML = Math.ceil((table2.rows.length-1)/10);
			document.getElementById("alldata").innerHTML = table2.rows.length-1;
			selectpage();
			hideTable();
			firstpage();			
			displayMessage();		
		}	
	};	
}	

<%--隱藏table作分頁--%>
function hideTable() {
	for (var i = 1 ; i < table2.rows.length; i++) {
		table2.rows[i].style.display = 'none';
	}  		
}

<%--顯示第一頁--%>
function firstpage(){
	hideTable();
	pagenum =1;
	for (var i = pagenum; i < pagenum*10+1; i++) {
		if(i<table2.rows.length)
		table2.rows[i].style.display = "";
	}
	document.getElementById("idpage").innerHTML = pagenum;
}

<%--分頁顯示下一頁--%>
function nextpage() {
	
	if(pagenum < Math.ceil((table2.rows.length-1)/10)){
		hideTable();
		pagenum++;
		document.getElementById("idpage").innerHTML = pagenum;
					
		for (var i = pagenum*10-9 ; i < pagenum*10+1 ; i++) {
			if(i<table2.rows.length)
			table2.rows[i].style.display = "";
		}   
	}    
		
}


<%--分頁顯示前一頁--%>
function prevpage() {
	if( pagenum >= 2 ){
		hideTable();
		pagenum--;
		for (var i = pagenum*10-9 ; i < pagenum*10+1 ; i++) {
			if(i<table2.rows.length)
			table2.rows[i].style.display = "";
		}   					 
	}   
	document.getElementById("idpage").innerHTML = pagenum;			
}

<%--分頁顯示最後一頁--%>
function lastpage(){
	hideTable();
	pagenum= Math.ceil((table2.rows.length-1)/10);
	for (var i = pagenum*10-9; i < table2.rows.length; i++) {
		if(i<table2.rows.length)
		table2.rows[i].style.display = "";
	}
	document.getElementById("idpage").innerHTML = pagenum;
}	

<%--依照選定下拉式選單切換頁面--%>
function select(){
	hideTable();
	var selectnum= document.getElementById("selectpage").value;
	pagenum=selectnum;
	document.getElementById("idpage").innerHTML = pagenum;
	for (var i = pagenum*10-9; i < pagenum*10+1; i++) {
		if(i<table2.rows.length)
		table2.rows[i].style.display = "";
		
	}
	
}

<%--設定下拉式選單的值--%>
function selectpage(){
		var select=document.getElementById("selectpage");
	for(var i=1;i<Math.ceil((table2.rows.length-1)/10)+1;i++){
		var option=document.createElement("option");
		option.text= "第"+ i +"頁";
		option.value= i ;
		select.add(option);
	}               
}

<%--將radio選取的值存回form--%>
function pick(a,b){
	var form2 = document.getElementById("form2");
	form2.DEP_NM.value = a ;
	form2.DEP_CODE.value = b ;	
}

<%--按下確認後輸出資料回XXZX_0100--%>
function Enter(){
	var form2 = document.getElementById("form2");
	if(form2.DEP_NM.value == 0){
		alert("請選取一筆資料");
	}
	else{
		window.opener.document.getElementById('DIV_NO').value = document.getElementById('DEP_CODE').value;
		window.opener.document.getElementById('hid_DEP_NM').value = document.getElementById('DEP_NM').value;
		window.opener.document.getElementById('DEP_NM').innerHTML = document.getElementById('DEP_NM').value;
		window.close();
	} 
}

<%--取消關閉視窗--%>
function Back(){	
	window.close(); 
}


</script>
</head>
<body bgcolor="#F0FBC6" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="new XXZX0200().initApp()" onResize="fix()" onScroll="fix()">
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
						  <div align="right">畫面編號：XXZX0200 </div>
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
							<tbody>
								<tr> 
									<td class="tbBox2"> <img src="${imageBase}/CM/icon_dot11.gif" width="18" height="16">單位代號索引</td>
								</tr>								
							</tbody>							
						</table>
						<table id="table2" width=97% border=1 align=center cellpadding="0"cellspacing="1" class="tbBox2" align="center" >
							<thead>
								<tr id="ROWID" class="tbBlue" align="center" >
									<td width = 10%>選取</td>
									<td width = 40%>部門名稱</td>
									<td width = 40%>部門代號</td>										
								</tr>
							</thead>
							<tbody>								
								<c:forEach items="${rtnList}" var="rtnMap" varStatus="status">								
									<tr align="center">																					
										<td><input type="radio" name="pick" onclick="pick('${rtnMap.text}','${fn:substring(rtnMap.value,0,fn:length(rtnMap.value)-3)}')"></td>
										<td><c:out value="${rtnMap.text}"/>&nbsp;</td>
										<td><c:out value="${fn:substring(rtnMap.value,0,fn:length(rtnMap.value)-3)}"/>&nbsp;</td>
										<form id="form2"style="display:none;" method="POST" >
											<input type="hidden" id="DEP_NM" name="DEP_NM" value="0">
											<input type="hidden" id="DEP_CODE" name="DEP_CODE" value="0" >
										</form>
									</tr>								
								</c:forEach>								
							</tbody>							   		    
						</table>
						<table  width=97% border=1 align=center cellpadding="0"cellspacing="1" class="tbBox2" align="center" >
							<td colspan=10 class="tbBlue"><div align="right">
							<img src="<%=imageBase%>/CM/previous2.gif" width="20" height="15" onclick="firstpage()">
							<img src="<%=imageBase%>/CM/previous1.gif" width="20" height="15" onclick="prevpage()">
							<select id="selectpage" onchange="select()"></select>
							<img src="<%=imageBase%>/CM/next1.gif" width="20" height="15" onclick="nextpage()">
							<img src="<%=imageBase%>/CM/next2.gif" width="20" height="15" onclick="lastpage()">
							第<b id="idpage">0</b>頁/共<b id="allpage">0</b>頁/共 <b id="alldata">0</b>筆
							</div>
							</td>
						</table>
						<table  width=97% border=1 align=center cellpadding="0"cellspacing="1" class="tbBox2" align="center" >
							<td align="center">
								<input type="button" class="button" id="btnEnter" name="btnEnter" value="確認" onClick="Enter()">
								<input type="button" class="button" id="btnCancer" name="btnCancer" value="取消" onClick="Back()">
							</td>
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