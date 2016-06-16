<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<script>
$(document).ready(function()
{
	$('#stuform').submit(function() 
	{
		var oldpwd = $("input[name='oldpwd']").val(),
			newpwd = $("input[name='newpwd']").val(),
			des = $("input[name='describe']").val();
		if(newpwd != "" && oldpwd == "")
		{
			$('#ret #msg').html("需要输入旧密码才能更改新密码！");
			$('#ret').dialog("option","title","信息不完整").dialog("open");
			return false;
		}
		$.ajax({
			type : "POST",
			url : "chgmyinfo",
			data : $(this).serialize(),
			success : function(data)
			{
				var ret = JSON.parse(data);
				$('#ret #msg').html(JSON.stringify(ret.msg));
				var title = "修改失败";
				if(ret.success)
				{
					title = "修改成功";
					$('#ret #msg').append("<br>2秒后自动跳转");
					setTimeout(function(){location.reload(true);},2000);
				}
				$('#ret').dialog("option","title",title).dialog("open");
			}
		});
		return false;
	});
});
</script>
	<form id="stuform">
		<div class="line_grid half">
			<div class="g_3"><span class="label">旧密码</span></div>
			<div class="g_9">
				<input type="password" name="oldpwd" class="simple_field" pattern="[a-z,A-Z,0-9]{3,18}" data-type="pwd">
				<div class="field_notice">a-z-A-Z-0-9</div>
				<div class="field_alert"></div>
			</div>
		</div>
		<div class="line_grid half">
			<div class="g_3"><span class="label">新密码</span></div>
			<div class="g_9">
				<input type="password" name="newpwd" class="simple_field" pattern="[a-z,A-Z,0-9]{3,18}" data-type="pwd">
				<div class="field_notice">a-z-A-Z-0-9</div>
				<div class="field_alert"></div>
			</div>
		</div>
		<div class="line_grid half">
			<div class="g_3"><span class="label">简介</span></div>
			<div class="g_9">
				<input type="text" name="des" class="simple_field" value="${user.describe}" pattern="{2,50}" required>
				<div class="field_alert"></div>
			</div>
		</div>
		<div class="g_12" style="text-align: center;">
			<input class="submitIt simple_buttons" value="修改" type="submit"/>
		</div>
	</form>
			
			
