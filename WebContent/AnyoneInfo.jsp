<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="xziar.enhancer.pojo.UserBean,xziar.enhancer.pojo.CompanyBean,xziar.enhancer.pojo.StudentBean" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="UTF-8">
	<title><s:property value="user.name"/>的个人信息</title>
	<!-- The Main CSS File -->
	<link rel="stylesheet" href="CSS/merge.css" />
	<!-- jQuery -->
	<script src="Javascript/jQuery/jquery-1.12.0.min.js"></script>
	<script src="Javascript/jQuery/jquery-ui-1.11.4.min.js"></script>
	
</head>
<body>
<script>
$(document).ready(function()
{
	$.fx.speeds._default = 300;
	$('#ret').dialog(
	{
		autoOpen: false,
		show: "fadeIn",
		modal: true,
		closeText: "",
	});
});
</script>
	<%@ include file="PageHead.jsp"%>
	<div class="wrapper contents">
		<div class="grid_wrapper">

			<div id="ret" class="dialog" title="">
				<span class="label lwParagraph" id="msg"></span>
			</div>
			
			<div class="g_6 contents_header">
				<h3 class="i_16_dashboard tab_label">个人信息</h3>
			</div>	
<c:if test="${user.role == 2 }">
		<div class="g_6 contents_options">
			<div class="simple_buttons">
				<div class="bwIcon i_16_help">学生账户</div>
			</div>
		</div>
		
		<div class="g_12 separator">
			<span></span>
		</div>
		<%@ include file="Info_Job.jsp"%>
</c:if>
<c:if test="${user.role == 1 }">
		<div class="g_6 contents_options">
			<div class="simple_buttons">
				<div class="bwIcon i_16_help">企业账户</div>
			</div>
		</div>
		
		<div class="g_12 separator">
			<span></span>
		</div>
		<%@ include file="Info_Company.jsp"%>
</c:if>
		</div>
	</div>
	
	
</body>
</html>