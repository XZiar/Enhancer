<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="xziar.enhancer.pojo.TaskBean, xziar.enhancer.pojo.UserBean, xziar.enhancer.pojo.StudentBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="UTF-8">
	<title><c:out value="${post.title }"/>  ----  Enhancer</title>
	<!-- The Main CSS File -->
	<link rel="stylesheet" href="CSS/merge.css" />
	<link rel="stylesheet" href="CSS/jquery.cleditor.css" />
	<!-- jQuery -->
	<script src="Javascript/jQuery/jquery-1.12.0.min.js"></script>
	<script src="Javascript/jQuery/jquery-ui-1.11.4.min.js"></script>
	<script src="Javascript/ClEditor/jquery.cleditor-1.4.5.js"></script>
	<style>
		.perAP {cursor: pointer;}
	</style>
</head>
<body>
	<%@ include file="PageHead.jsp"%>
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
	$(".wysiwyg").cleditor({width:"100%", height:"100%"});
	$(".rtime").each(function()
	{
		$(this).text(new Date(parseInt($(this).text())).toLocaleDateString());
	});
	$('.i_16_close').click(function()
	{
		$('.theme-popover-mask').fadeOut(100);
		$('.theme-popover').slideUp(200);
	});

	$('#replyform').submit(function() 
	{
		var fd = new FormData(this);
		$.ajax({
			type : "POST",
			url : "addreply",
			data : $(this).serialize(),
			success : function(data)
			{
				var ret = JSON.parse(data);
				var title = "发布失败";
				if(ret.success)
				{
					title = "发布成功";
					$('#ret #msg').html("<br>2秒后自动跳转");
					setTimeout(function(){location.reload(true)},2000);
				}
				else
				{
					switch(ret.msg)
					{
					case "unlogin":
						window.location.href = "login.jsp";break;
					case "nopermission":
						$('#ret #msg').html("身份不符");break;
					case "error":
					default:
						$('#ret #msg').html("系统错误");break;
					}
				}
				$('#ret').dialog("option","title",title).dialog("open");
			}
		});
		return false;
	});
});
</script>
	<c:set var="isAdmin" value="${(! empty sessionScope.user) && user.role == 0 }" />
	<div class="theme-popover" style="display:none;">
		<div id="ret" class="dialog" title="">
			<span class="label lwParagraph" id="msg"></span>
		</div>
	</div>

	<div class="wrapper contents">
		<div class="grid_wrapper">

			<div class="g_6 contents_header">
				<h3 class="i_16_chats tab_label"><c:out value="${post.title }"/></h3>
				<div>
					<span class="label">发贴人：<c:out value="${post.poster }"/></span>
				</div>
			</div>
			<div class="g_6 contents_options">
				<div class="simple_buttons">
					<div class="bwIcon i_16_help">关于话题发帖人</div>
				</div>
			</div>
			
			<c:if test="${isAdmin }">
<script>
function dodel(type,id)
{
	$.ajax({
		type : "POST",
		data : (type?"pid=":"rid=") + id,
		url : type?"delpost":"delreply",
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
			if(ret.msg == "reply")
				location.reload(true);
			else
				window.location.href = "forum";
		}
	});
}
function delpost(pid)
{
	$('#ret #msg').html("确定删除这个话题？");
	$('#ret').dialog(
	{
		buttons: 
		{
	        "是": function() 
	        {
	        	dodel(true,pid);
	          	$(this).dialog("close");
	        },
	        "否": function() 
	        {
	          	$(this).dialog("close");
	        }
		}
	}).dialog("open");
}
function delreply(rid)
{
	$('#ret #msg').html("确定删除这条回复？");
	$('#ret').dialog(
	{
		buttons: 
		{
	        "是": function() 
	        {
	        	dodel(false,rid);
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
					<div class="simple_buttons" onclick="delpost(${post.pid })" >
						<div>删除此话题</div>
					</div>
				</div>
			</c:if>

			<div class="g_12 separator">
				<span></span>
			</div>
			
			<div class="g_12">
				<div class="widget_header">
					<h4 class="widget_header_title">详细内容</h4>
				</div>
				<div class="widget_contents">
					<c:out value="${post.describe }" escapeXml="false"/>
				</div>
			</div>
			
			<c:if test="${replys != null && fn:length(replys) > 0}">
			<div class="g_12" id="replylist">
				<div class="widget_header">
					<h4 class="widget_header_title">回复讨论<c:out value='${fn:length(replys)} '/></h4>
				</div>
				<div class="widget_contents">
				<c:forEach var="r" items="${replys}">
					<div class="line_grid">
						<div class="g_3">
							<span data-id='${r.uid}'>
								<c:out value='${r.replyer} '/>
							</span><br>
							<div class="field_notice">
								发表于
								<span class="rtime">
									<c:out value='${r.time_reply} '/>
								</span>
							</div>
							<c:if test="${isAdmin }">
								<div class="g_12" style="text-align: center;">
									<div class="simple_buttons" onclick="delreply(${r.rid})">
										<div>删除此回复</div>
									</div>
								</div>
							</c:if>
						</div>
						<div class="g_9">
							<div class="message"><c:out value='${r.describe} ' escapeXml="false"/></div>
						</div>
					</div>
				</c:forEach>
				</div>
			</div>
			</c:if>
			<c:if test="${! empty sessionScope.user }">
			<form id="replyform">
				<input type="hidden" name="reply.pid" value='${post.pid }' />
				<div class="g_12">
					<div class="widget_header">
						<h4 class="widget_header_title wwIcon i_16_wysiwyg">发表回复</h4>
					</div>
					<div class="widget_contents noPadding">
						<div class="line_grid">
							<div class="g_12">
								<textarea name="reply.describe" class="simple_field wysiwyg"></textarea>
								<div class="field_notice"></div>
							</div>
							<div class="g_12" style="text-align: center;">
								<input class="submitIt simple_buttons" id="send" value="发表回复" type="submit" />
							</div>
						</div>
					</div>
				</div>
			</form>
			</c:if>
		</div>
	</div>
	
	
</body>
</html>