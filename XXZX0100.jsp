<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>

<!--
程式：XXZX0100.jsp
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
<script type='text/javascript' >
	
	
	var form ; //TODO
	var table2;
	var tbs;
	var pagenum= 0 ;
	function XXZX0100() {
		
		return {

			initApp : function() {				
				form = document.getElementById("form1");				
				table2= document.getElementById("table2");
				tbs=table2.getElementsByTagName('tbody');
				document.getElementById("allpage").innerHTML = Math.ceil((table2.rows.length-1)/10);
				document.getElementById("alldata").innerHTML = table2.rows.length-1;
				selectpage();
				displayMessage();
				hideTable();
				if('${page}'==1){ //TODO TODOTODOTODOTODOTODO
					firstpage();
				}								
				document.getElementById("idpage").innerHTML = pagenum;				
			}
		};
	}
	
	<%--查詢員工基本資料--%>
	function doQuery() {					
		form.action = "${dispatcher}/XXZX_0100/query";		
		form.submit(); //TODO		
	}

	<%--點選員工編號連線至維護頁面--%>
	function toupdate(EMP_ID,EMP_NAME,DIV_NO,BIRTHDAY,POSITION,OP_STATUS,FLOW_NO,UPDT_DATE) {	//TODO	
		var form2 = document.getElementById("form2");		
		form2.ACTION_TYPE.value = "U";
		form2.EMP_ID.value = EMP_ID ;
		form2.EMP_NAME.value = EMP_NAME ;
		form2.DIV_NO.value = DIV_NO ;
		form2.BIRTHDAY.value = BIRTHDAY ;
		form2.POSITION.value = POSITION ;
		form2.OP_STATUS.value = OP_STATUS ;
		form2.FLOW_NO.value = FLOW_NO ;
		form2.UPDT_DATE.value = UPDT_DATE ;
		form2.UPDT_NM.value = '${userEMP_NAME}' ;
		form2.action = "${dispatcher}/XXZX_0101/prompt" ;		
		form2.submit();		
	}

	<%--連線至新增頁面--%>
	function toinsert() {	
		var form2 = document.getElementById("form2");		
		form2.action = "${dispatcher}/XXZX_0101/prompt" ;		
		form2.ACTION_TYPE.value = "I";
		form2.submit();		
	}
	
	<%--清除頁面資料--%>
	function doClear(i) {		 
		form.reset();
		if (tbs.length==0){ 
			alert('資料為空無法再清除'); 
		return; 
		} 		
		table2.removeChild(tbs[i]); 
		document.getElementById("allpage").innerHTML=0;	
		document.getElementById("idpage").innerHTML =0; 
		document.getElementById("alldata").innerHTML =0; 		
	}

	<%--隱藏table做分頁功能--%>
	function hideTable() {
    	for (var i = 1 ; i < table2.rows.length; i++) {
        	table2.rows[i].style.display = 'none';
    	}  		
	}

	<%--顯示至第一頁--%>
	function firstpage(){
		hideTable();
		pagenum =1;		
		for (var i = pagenum; i < pagenum*10+1; i++) {
			if(i<table2.rows.length)
			table2.rows[i].style.display = "";
		}
		document.getElementById("idpage").innerHTML = pagenum;			
	}

	<%--分頁切換至下一頁--%>
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

	<%--分頁切換至前一頁--%>
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

	<%--分頁切換至最後一頁--%>
	function lastpage(){
		hideTable();
		pagenum= Math.ceil((table2.rows.length-1)/10);
		for (var i = pagenum*10-9; i < table2.rows.length; i++) {
			if(i<table2.rows.length)
			table2.rows[i].style.display = "";
		}
		document.getElementById("idpage").innerHTML = pagenum;
	}	

	<%--下拉式選單的選單內容--%>
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

	<%--選取下拉式選單跳轉至選取頁面--%>
	function selectpage(){
                var select=document.getElementById("selectpage");
                for(var i=1;i<Math.ceil((table2.rows.length-1)/10)+1;i++){
                    var option=document.createElement("option");
                    option.text= "第"+ i +"頁";
                    option.value= i ;
                    select.add(option);
                }               
            }


</script>


</head>
<body bgcolor="#F0FBC6" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="new XXZX0100().initApp()" onResize="fix()" onScroll="fix()">
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
						  <div align="right">畫面編號：XXZX0100 </div>
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
	<table width="100%" height="100%" border="0" cellpadding="0"
		cellspacing="0" style="padding-top: 30px">
		<tr>
			<td width="4" background="${imageBase}/CM/border_01.gif"><img
				src="${imageBase}/CM/border_01.gif" width="4" height="12"></td>
			<td width="100%" valign="top" bgcolor="#F0FBC6">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>						
						<td valign="top">
							<table width=97% border=0 align=center cellpadding="0" cellspacing="1" bgcolor="#003366">
							<thead>
								<tr> 
									<td class="tbBox2"> <img src="${imageBase}/CM/icon_dot11.gif" width="18" height="16">新進人員模擬程式</td>
								</tr>
								<tr bgcolor=#FFFFFF>
								</tr>
							</thead>	
							 										
						</table>
							<table width=97% border=0 align=center cellpadding="0" cellspacing="1" bgcolor="#003366">
								<form id="form1" width=97% border=0 align=center cellpadding="0"cellspacing="1" class="tbBox2"><!--TODO-->
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
											<!--TODO <button class="button" id="btnQuery" onClick="doQuery()">查詢</button>-->
											<button class="button" id="btnQuery" onClick="doQuery()">查詢</button>
											<button class="button" id="btnClear" onClick="doClear(0)">清除</button>											
										</td>
									</tr>
								</form>
							</table>	
							<table width=97% border=0 align=center cellpadding="0" cellspacing="1" bgcolor="#003366">
								<tr>
									<td class="tbYellow" width="100%" align="center">
										<button class="button" id="btnInsert" onClick="toinsert()">新增</button>												
										<form id="form2"style="display:none;" method="POST" ><!--TODO-->
											<input type="hidden" id="ACTION_TYPE" name="ACTION_TYPE" >
											<input type="hidden" id="EMP_ID" name="EMP_ID" >
											<input type="hidden" id="EMP_NAME" name="EMP_NAME" >
											<input type="hidden" id="DIV_NO" name="DIV_NO" >
											<input type="hidden" id="BIRTHDAY" name="BIRTHDAY" >
											<input type="hidden" id="POSITION" name="POSITION" >
											<input type="hidden" id="OP_STATUS" name="OP_STATUS" >
											<input type="hidden" id="UPDT_NM" name="UPDT_NM" >
											<input type="hidden" id="FLOW_NO" name="FLOW_NO" >
											<input type="hidden" id="UPDT_DATE" name="UPDT_DATE" >
										</form>
									</td>
								</tr>			
							</table>
							<table id="table2" width=97% border=1 align=center cellpadding="0"cellspacing="1" class="tbBox2" align="center" >
								<thead>
									<tr id="ROWID" class="tbBlue" align="center" >
										<td width = "15%">序號</td><!--TODO-->
										<td width = "15%">員工編號</td>
										<td width = "15%">員工姓名</td>
										<td width = "15%">單位代號</td>
										<td width = "15%">生日</td>
										<td width = "15%">職位</td>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${rtnList}" var="rtnMap" varStatus="status">								
										<tr>
											<td><c:out value="${status.count}" />&nbsp;</td>
											<td>
												<a href="javascript:toupdate('${rtnMap.EMP_ID}','${rtnMap.EMP_NAME}','${rtnMap.DIV_NO}','${rtnMap.BIRTHDAY}','${rtnMap.POSITION}','${rtnMap.OP_STATUS}','${rtnMap.FLOW_NO}','${rtnMap.UPDT_DATE}')" ><c:out value="${rtnMap.EMP_ID}"/></a>&nbsp;												
											</td>
											<td><c:out value="${rtnMap.EMP_NAME}"/>&nbsp;</td>
											<td><c:out value="${rtnMap.DIV_NO}"/>&nbsp;</td>
											<td><c:out value="${rtnMap.BIRTHDAY}"/>&nbsp;</td>
											<td><c:out value="${rtnMap.POSITION}"/>&nbsp;</td>
										</tr>								
									</c:forEach>
								</tbody>							   		    
							</table>							
							<table  width=97% border=1 align=center cellpadding="0"cellspacing="1" class="tbBox2" align="center" >
								<td colspan=10><div align="right">
								<img src="<%=imageBase%>/CM/previous2.gif" width="20" height="15" onclick="firstpage()">
								<img src="<%=imageBase%>/CM/previous1.gif" width="20" height="15" onclick="prevpage()">
								<select id="selectpage" onchange="select()"></select>
								<img src="<%=imageBase%>/CM/next1.gif" width="20" height="15" onclick="nextpage()">
								<img src="<%=imageBase%>/CM/next2.gif" width="20" height="15" onclick="lastpage()">
								第<b id="idpage">0</b>頁/共<b id="allpage">0</b>頁/共 <b id="alldata">0</b>筆
								</div>
								</td>
							</table>
						</td>
					</tr>
				</table> <!-- InstanceEndEditable -->
			</td>
			<td width="4" background="${imageBase}/CM/border_02.gif"><img
				src="${imageBase}/CM/border_02.gif" width="4" height="12"></td>
			<td width="5" class="tbBox"><img
				src="${imageBase}/CM/ecblank.gif" width="5" height="1"></td>
		</tr>
	</table>
</body>
</html>