<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="UTF-8">
	<title>登录</title>
	<!-- The Main CSS File -->
	<link rel="stylesheet" href="CSS/merge.css" />
	<!-- jQuery -->
	<script src="Javascript/jQuery/jquery-1.12.0.min.js"></script>
	<script src="Javascript/jQuery/jquery-ui-1.11.4.min.js"></script>

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

	$('#loginform').submit(function() 
	{
		var formdata = $(this).serialize();
		$.ajax({
			type : "POST",
			url : "login",
			data : formdata,
			success : function(data)
			{
				var ret = JSON.parse(data);
				$('#ret #msg').html(ret.msg);
				var title = "失败";
				if(ret.success)
				{
					title = "登陆成功";
					$('#ret #msg').append("<br>2秒后自动跳转");
					setTimeout(function(){window.location.href = "task"},2000);
				}
				$('#ret').dialog("option","title",title).dialog("open");
			}
		});
		return false;
	});
});
</script>
	
	<div class="wrapper contents_wrapper">
		<div class="login">
		
			<div id="ret" class="dialog" title="">
				<span class="label lwParagraph" id="msg">L</span>
			</div>
		
			<div class="widget_header">
				<h4 class="widget_header_title wwIcon i_16_login">登录</h4>
			</div>
			<div class="widget_contents lgNoPadding">
				<form id="loginform">
				<div class="line_grid">
					<div class="g_3"><span class="label">用户名</span></div>
					<div class="g_9">
						<input class="simple_field tooltip" name="un" type="text" placeholder="Username">
					</div>
					<div class="clear"></div>
				</div>
				<div class="line_grid">
					<div class="g_3"><span class="label">密码</span></div>
					<div class="g_9">
						<input class="simple_field tooltip" name="pwd" type="password" placeholder="password">
					</div>
					<div class="clear"></div>
				</div>
				<div class="line_grid">
					<div class="g_6">
						<input class="submitIt simple_buttons" value="登录" type="submit"/>
					</div>
					<div class="g_6">
						<!-- <div class="simple_buttons"><div>注册</div></div> -->
						<div class="submitIt simple_buttons">
							<a href="register.jsp">注册</a>
						</div>
					</div>
					<div class="clear"></div>
				</div>
				</form>
			</div>
		</div>
	</div>
	
	
</body>
</html>