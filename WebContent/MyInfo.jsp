<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
var tsstatus = ["待审核","报名中","报名截止","进行中","已完结","已关闭"];
function MsgTip(txt)
{
	var cont = '<div class="g_12"><div class="success iDialog">' + txt + '</div></div>';
	$("#msgtip").html(cont);
}
function validRet(ret)
{
	if(!ret.success)
	{
		if(ret.msg == "unlogin")
		{
			window.location.href = "login.jsp";
			return false;
		}
		window.location.href = "403.jsp";
		return false;
	}
	else
		return true;
}
$(document).ready(function()
{
	$.fx.speeds._default = 300;
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
		$('.rfs'+obj).trigger("click");
	});
	$('.i_32_dashboard').click();
	$('.i_16_close').on('click',function()
	{
		$('.theme-popover-mask').fadeOut(100);
		$('.theme-popover').slideUp(200);
	});
			
});
</script>
	<%@ include file="PageHead.jsp"%>

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
			<div class="g_12" id="msgtip">
			</div>

			<div id="part2" class="onepart">
				<%@ include file="Info_Account.jsp"%>
				<div class="g_12 separator">
					<span></span>
				</div>
			</div>

<c:if test="${user.role == 1 }">	
		<div id="part1" class="onepart">
			<%@ include file="Info_Job.jsp"%>
			<div class="g_12 separator">
				<span></span>
			</div>
		</div>
		
		<div id="part3" class="onepart">
			<%@ include file="Info_Task_og_stu.jsp"%>
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
</c:if>	

<c:if test="${user.role == 2 }">
		<div id="part1" class="onepart">
			<%@ include file="Info_Company.jsp"%>
			<div class="g_12 separator">
			<span></span>
			</div>
		</div>
		
		<div id="part3" class="onepart">
			<%@ include file="Info_Task_og_cpn.jsp"%>
			<div class="g_12 separator">
				<span></span>
			</div>
		</div>
		
		<div id="part4" class="onepart">
			<%@ include file="Info_Task_fn_cpn.jsp"%>
			<div class="g_12 separator">
				<span></span>
			</div>
		</div>
</c:if>	


		</div>
	</div>
	
</div>	
</body>
</html>