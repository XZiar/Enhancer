<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="UTF-8">
	<title>发布任务</title>
	<!-- The Main CSS File -->
	<link rel="stylesheet" href="CSS/merge.css" />
	<link rel="stylesheet" href="CSS/jquery.cleditor.css" />
	<!-- jQuery -->
	<script src="Javascript/jQuery/jquery-1.12.0.min.js"></script>
	<script src="Javascript/jQuery/jquery-ui-1.11.4.min.js"></script>
	<script src="Javascript/ClEditor/jquery.cleditor-1.4.5.js"></script>
	
</head>
<body>
	<%@ include file="PageHead.jsp"%>

<script>
function getObjectURL(file)
{
	if (window.createObjectURL != undefined) 
		return window.createObjectURL(file);// basic
	else if (window.URL != undefined)
		return window.URL.createObjectURL(file);// mozilla(firefox)
	else if (window.webkitURL != undefined)
		return window.webkitURL.createObjectURL(file);// webkit or chrome
}

$(document).ready(function()
{
	$(".wysiwyg").cleditor({width:"100%", height:"100%"});
	
	$.fx.speeds._default = 300;
	$('#ret').dialog(
	{
		autoOpen: false,
		show: "fadeIn",
		modal: true,
		closeText: "",
	});

	$('input').on("invalid",function()
	{
		var item = $(this);
		var msg = item.siblings(".field_alert");
		
		if(item.val() == "")
			msg.text("请输入！");
		else
		{
			switch(item.data("type"))
			{
			default:
				msg.text("请输入正确格式");break;
			}
			
		}
	});
	
	$('#postform').submit(function() 
	{
		var fd = new FormData(this);
		$.ajax({
			type : "POST",
			url : "addpost",
			data : $(this).serialize(),
			success : function(data)
			{
				var ret = JSON.parse(data);
				var title = "发布失败";
				if(ret.success)
				{
					title = "发布成功";
					$('#ret #msg').html("<br>2秒后自动跳转");
					setTimeout(function(){window.location.href = "postview?pid=" + ret.msg},2000);
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

	<div class="wrapper contents">
		<div class="grid_wrapper">

			<div id="ret" class="dialog" title="">
				<span class="label lwParagraph" id="msg"></span>
			</div>


			<div style="text-align: center;">
				<h1>发布话题</h1>
			</div>

			<div class="g_12 separator">
				<span></span>
			</div>

			<form id="postform">
				<div class="g_12">
					<div class="g_3">
						<span class="label">话题标题<span class="must">*</span></span>
					</div>
					<div class="g_9">
						<input type="text" name="post.title" class="simple_field" required>
						<div class="field_alert"></div>
					</div>
				</div>

				<div class="g_12">
					<div class="widget_header">
						<h4 class="widget_header_title wwIcon i_16_wysiwyg">详细内容</h4>
					</div>
					<div class="widget_contents noPadding">
						<div class="line_grid">
							<div class="g_12">
								<textarea name="post.describe" class="simple_field wysiwyg"></textarea>
								<div class="field_notice"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="g_12" style="text-align: center;">
					<input class="submitIt simple_buttons" id="send" value="发布话题" type="submit" />
				</div>

			</form>

		</div>
	</div>


</body>
</html>