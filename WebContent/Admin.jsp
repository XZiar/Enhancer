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
		.ttitle {cursor: pointer;}
	</style>
</head>
<body>
<script>
function MsgTip(txt)
{
	var cont = '<div class="g_12"><div class="success iDialog">' + txt + '</div></div>';
	$("#msgtip").html(cont);
}
function MsgTip(txt)
{
	var cont = '<div class="g_12"><div class="success iDialog">' + txt + '</div></div>';
	$("#msgtip").html(cont);
}
$(document).ready(function()
{
	$.fx.speeds._default = 300;
	$('#ret').dialog(
	{
		resizable: false,
		autoOpen: false,
		show: "fadeIn",
		modal: true,
		closeText: "",
	});
	$("#msgtip").on("click", ".iDialog", function()
	{
		$(this).fadeOut("slow").promise().done(function()
		{
			$(this).parent().remove();
		});
	});
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

<div class="wrapper contents" style="position: relative;">
	<aside class="sidebar" style="width:20%;">
		<ul class="tab_nav">
			<li class="active_tab i_32_dashboard" data-obj="part1"><a>
				<span class="tab_label">用户审核</span>
				<span class="tab_info"></span>
			</a></li>
			<li class="i_32_inbox" data-obj="part2"><a>
				<span class="tab_label">用户管理</span>
				<span class="tab_info"></span>
			</a></li>
			<li class="i_32_tables" data-obj="part3"><a>
				<span class="tab_label">任务审核</span>
				<span class="tab_info"></span>
			</a></li>
		</ul>
	</aside>
	
	<div class="contents" style="float:right;width:80%;">
		<div class="grid_wrapper">

			<div id="ret" class="dialog" title="">
				<span class="label lwParagraph" id="msg"></span>
			</div>
			<div class="g_12" id="msgtip">
			</div>
			
			<div id="part1" class="onepart">
				<%@ include file="Info_User_Check.jsp"%>
				<div class="g_12 separator">
					<span></span>
				</div>
			</div>

			<div id="part2" class="onepart">
				<%@ include file="Info_User_Manage.jsp"%>
				<div class="g_12 separator">
					<span></span>
				</div>
			</div>
			
			<div id="part3" class="onepart">
				<%@ include file="Info_Task_Check.jsp"%>
				<div class="g_12 separator">
					<span></span>
				</div>
			</div>
					
		</div>
	</div>
	
</div>	
</body>
</html>