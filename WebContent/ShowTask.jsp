<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="xziar.enhancer.pojo.TaskBean, xziar.enhancer.pojo.UserBean, xziar.enhancer.pojo.StudentBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="UTF-8">
	<title><c:out value="${task.title }"/>  ----  Enhancer</title>
	<!-- The Main CSS File -->
	<link rel="stylesheet" href="CSS/merge.css" />
	<!-- jQuery -->
	<script src="Javascript/jQuery/jquery-1.12.0.min.js"></script>
	<script src="Javascript/jQuery/jquery-ui-1.11.4.min.js"></script>
	<style>
		.perAP {cursor: pointer;}
	</style>
</head>
<body>
	<%@ include file="PageHead.jsp"%>
<script>
function preApplicants(data)
{
	var obj = $('#aps .widget_contents');
	obj.html("");
	$.each(data,function(i,ap)
	{
		var cont = "<div class='line_grid' data-uid='" + ap.uid + "'>"
			+ "<div class='g_2'><span class='label'>名称</span></div>"
			+ "<div class='g_4'>" + ap.name + "</div>"
			+ "<div class='g_2'><span class='label'>人数</span></div>"
			+ "<div class='g_4'>" + ap.people + "</div>"
			+ "</div>";
		obj.append(cont);
	});
}
$(document).ready(function()
{
	tid = $("input[name='tid']").val();
	
	$.fx.speeds._default = 300;
	$('#ret').dialog(
	{
		autoOpen: false,
		show: "fadeIn",
		modal: true,
		closeText: "",
	});
	
	$('.i_16_close').click(function()
	{
		$('.theme-popover-mask').fadeOut(100);
		$('.theme-popover').slideUp(200);
	});
	
	$("#apply").click(function()
	{
		$.ajax({
			type : "POST",
			url : "apply",
			data : "tid=" + tid,
			success : function(data)
			{
				var ret = JSON.parse(data);
				if(!ret.success)
				{
					if(ret.msg == "unlogin")
					{
						window.location.href = "login.jsp";
						return;
					}
					if(ret.msg == "error")
					{
						window.location.href = "403.jsp";
						return;
					}
				}
				else
				{
					preApplicants(ret.applicants);
					$('.theme-popover-mask').fadeIn(100);
					$('.theme-popover').slideDown(200);
				}
			}
		});
	});

	$('#aps').on("click",".line_grid",function()
	{
		$('#sendapply').data("uid", $(this).data("uid"));
		$(this).siblings().removeClass("chosen");
		$(this).addClass("chosen");
	});
	
	$('#sendapply').click(function()
	{
		var uid = $(this).data("uid");
		if(uid < 0)
		{
			alert("请选择申请人！");
			return;
		}
		$.ajax({
			type : "POST",
			url : "apply",
			data : $('[name="des"]').serialize() + "&uid=" + uid + "&tid=" + tid,
			success : function(data)
			{
				var ret = JSON.parse(data);
				var words = "申请成功";
				var title = "申请成功";
				if(ret.success)
				{
					setTimeout(function(){location.reload(true);},3000);
				}
				else
				{
					title = "申请失败";
					switch(ret.msg)
					{
					case "unsatify":
						words = "不满足申请条件！";
						break;
					case "already":
						words = "请勿重复申请！";
						break;
					case "error":
						words = "系统错误！";
						break;
					}
				}
				$('#ret #msg').html(words);
				$('#ret').dialog("option","title",title).dialog("open");
			}
		});
	});
});
</script>
	<div class="theme-popover" style="display:none;">
		<div id="ret" class="dialog" title="">
			<span class="label lwParagraph" id="msg"></span>
		</div>
		<div class="widget_header wwOptions">
			<h4 class="widget_header_title wwIcon i_16_tooltip">选择申请人</h4>
			<div class="w_Options i_16_close"><span class="aclose"></span></div>
		</div>
		<div class="widget_contents noPadding">
			<div class="g_6" id="aps">
				<div class="widget_contents noPadding" style="max-height:400px; overflow:auto;">
				
				</div>
			</div>
			<div class="g_6">
				<textarea class="simple_field" name="des"></textarea>
			</div>
			<div class="g_12" style="text-align:center;">
				<div class="submitIt simple_buttons" id="sendapply" data-uid="-1">提交申请</div>
			</div>
		</div>
	</div>
	<div class="theme-popover-mask"></div>
	
	<input type="hidden" name="tid" value='<c:out value="${task.tid }"/>'/>
	
	<div class="wrapper contents">
		<div class="grid_wrapper">

			<div class="g_6 contents_header">
				<h3 class="i_16_dashboard tab_label"><c:out value="${task.title }"/></h3>
				<div>
					<span class="label">发起人：<c:out value="${task.launcher }"/></span>
				</div>
			</div>
			<div class="g_6 contents_options">
				<div class="simple_buttons">
					<div class="bwIcon i_16_help">关于发起人</div>
				</div>
			</div>

			<div class="g_12 separator">
				<span></span>
			</div>
			
			<div class="g_12">
				<div class="widget_header">
					<h4 class="widget_header_title">硬性要求</h4>
				</div>
				<div class="widget_contents">
					<div class="line_grid">
						<div class="g_6">
							<div class="g_3"><span class="label">人数</span></div>
							<div class="g_9"><c:out value="${task.limit_people }"/></div>
						</div>
						<div class="g_6">
							<div class="g_3"><span class="label">薪酬</span></div>
							<div class="g_9"><c:out value="${task.payment }"/></div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="g_12">
				<div class="widget_header">
					<h4 class="widget_header_title">发布人自述</h4>
				</div>
				<div class="widget_contents">
					<c:out value="${task.describe }" escapeXml="false"/>
				</div>
			</div>
			
<%
{
	UserBean user = (UserBean)session.getAttribute("user");
	if(user != null)
	{
%>
		<c:if test="${user.role == 1 && task.status == 1 }">
			<div class="g_12" style="text-align: center;">
				<div class="simple_buttons" id="apply">
					<div>提交申请</div>
				</div>
			</div>
		</c:if>
		<c:if test="${user.role == 0 && task.status != 0}">
<script>
function dodel()
{
	$.ajax({
		type : "POST",
		data : "tid=" + tid,
		url : "deltask",
		success : function(data)
		{
			var ret = JSON.parse(data);
			if(!ret.success)
			{
				if(ret.msg == "unlogin")
				{
					window.location.href = "login.jsp";
					return;
				}
				window.location.href = "403.jsp";
				return;
			}
			window.location.href = "task";
		}
	});
}
function deltask()
{
	$('#ret #msg').html("确定要关闭这项任务吗？");
	$('#ret').dialog(
	{
		buttons: 
		{
	        "是": function() 
	        {
	        	dodel();
	          	$(this).dialog("close");
	        },
	        "否": function() 
	        {
	          	$(this).dialog("close");
	        }
		}
	}).dialog("open");
}
</script>
			<div class="g_12" style="text-align: center;">
				<div class="simple_buttons" onclick='deltask()'>
					<div>关闭任务</div>
				</div>
			</div>

		</c:if>
<%
	}
}
%>
			
		</div>
	</div>
	
	
</body>
</html>