<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="xziar.enhancer.pojo.AccountBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="UTF-8">
	<title>个人中心</title>
	<!-- The Main CSS File -->
	<link rel="stylesheet" href="CSS/merge.css" />
	<!-- jQuery -->
	<script src="Javascript/jQuery/jquery-1.12.0.min.js"></script>
	<script src="Javascript/jQuery/jquery-ui-1.11.4.min.js"></script>
	<style>
		.sidebar > ul > li{cursor: pointer;}
	</style>
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
	$('aside li').click(function()
	{
		var obj = $(this).data("obj");
		$('.onepart').hide();
		$('#'+obj).show();
		$(this).siblings().removeClass('active_tab');
		$(this).addClass('active_tab');
	});
	$('.i_32_dashboard').click();
});
</script>
	<%@ include file="PageHead.jsp"%>

<%
{
	UserBean user = (UserBean)request.getAttribute("user");
%>


<div class="wrapper contents" style="position: relative;">
<c:set var="urole" value="${fn:split('管理员,学生,企业', ',')}" />
	<aside class="sidebar" style="width:20%;">
		<ul class="tab_nav">
			<li class="active_tab i_32_dashboard" data-obj="part1"><a>
				<span class="tab_label">个人信息</span>
				<span class="tab_info"></span>
			</a></li>
			<li class="i_32_inbox" data-obj="part2"><a>
				<span class="tab_label">修改信息</span>
				<span class="tab_info"></span>
			</a></li>
			<li class="i_32_tables" data-obj="part3"><a>
				<span class="tab_label">任务</span>
				<span class="tab_info">进行中</span>
			</a></li>
			<li class="i_32_forms" data-obj="part4"><a>
				<span class="tab_label">任务</span>
				<span class="tab_info">已完成</span>
			</a></li>
		</ul>
	</aside>
	
	<div class="contents" style="float:right;width:80%;">
		<div class="grid_wrapper">

			<div id="ret" class="dialog" title="">
				<span class="label lwParagraph" id="msg"></span>
			</div>
			
			<div class="g_6 contents_header">
				<h3 class="i_16_dashboard tab_label">个人信息</h3>
			</div>
			<div class="g_6 contents_options">
				<div class="simple_buttons">
					<div class="bwIcon i_16_help">${urole[user.role]}</div>
				</div>
			</div>
			
			
			<div id="part2" class="onepart">
				<%@ include file="Info_Account.jsp"%>
				<div class="g_12 separator">
					<span></span>
				</div>
			</div>
			
			
<%
	if(user.getAccountRole() == AccountBean.Role.student)
	{
%>
		<div id="part1" class="onepart">
			<%@ include file="Info_Job.jsp"%>
			<div class="g_12 separator">
				<span></span>
			</div>
		</div>
		
		<div id="part3" class="onepart">
			<%@ include file="Info_Task_Ongoing_stu.jsp"%>
			<div class="g_12 separator">
				<span></span>
			</div>
		</div>
		
		<div id="part4" class="onepart">
			<%@ include file="Info_Task_Finish_stu.jsp"%>
			<div class="g_12 separator">
				<span></span>
			</div>
		</div>
<%
	}
	else if(user.getAccountRole() == AccountBean.Role.company)//company
	{
%>
		<div id="part1" class="onepart">
			<%@ include file="Info_Company.jsp"%>
			<div class="g_12 separator">
			<span></span>
			</div>
		</div>
		
		<div id="part3" class="onepart">
			<%@ include file="Info_Task_Ongoing_cpn.jsp"%>
			<div class="g_12 separator">
				<span></span>
			</div>
		</div>
		
		<div id="part4" class="onepart">
			<%@ include file="Info_Task_Finish_cpn.jsp"%>
			<div class="g_12 separator">
				<span></span>
			</div>
		</div>
<%
	}
}
%>
			
		</div>
	</div>
	
</div>	
</body>
</html>