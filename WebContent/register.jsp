<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="UTF-8">
	<title>注册</title>
	<!-- The Main CSS File -->
	<link rel="stylesheet" href="CSS/merge.css" />
	<!-- jQuery -->
	<script src="Javascript/jQuery/jquery-1.12.0.min.js"></script>
	<script src="Javascript/jQuery/jquery-ui-1.11.4.min.js"></script>
	
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
	$("#regTabs").tabs();

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
			case "tel":
				msg.text("请输入正确的电话号码");break;
			case "id":
				msg.text("请输入正确的身份证号码");break;
			case "pwd":
				msg.text("请输入符合要求的密码，至少3个字符");break;
			default:
				msg.text("请输入正确格式");break;
			}
			
		}
	});

	$(':file').on("change",function()
	{
		var isok = false;
		if(this.files.length > 1)
		{
			alert("只能选择一张图片！");
		}
		else if(this.files.length == 1)
		{
			var ext = this.files[0].name;
			ext = ext.substr(ext.lastIndexOf(".")).toLowerCase();
			var allowExt = ".jpg,.jpeg,.png";
			if(allowExt.indexOf(ext) <= 0)
			{
				alert("图片格式不正确，请处理后重新选择");
			}
			else if(this.files[0].size > 1024 * 1024)
			{
				alert("图片过大，请处理后重新上传");
			}
			else
			{
				isok = true;
			}
		}
		var fn = $(this).siblings(".filename");
		if(isok)
		{
			var f = this.files[0];
			fn.text(f.name);
			$(this).parents('.line_grid').find('img').attr("src", getObjectURL(f));
		}
		else
		{
			fn.text("请选择图片文件");
			$(this).parents('.line_grid').find('img').attr("src", "Images/LightBox/preview.png");
		}
	});
	
	$('#upimg').on("click",function()
	{
		$(this).siblings(":file").click();
	});
	
	$('form').submit(function() 
	{
		$.ajax({
			type : "POST",
			contentType : false,
			processData : false,
			url : "register",
			data : new FormData(this),
			success : function(data)
			{
				var ret = JSON.parse(data);
				$('#ret #msg').html(JSON.stringify(ret.msg));
				var title = "注册失败";
				if(ret.success)
				{
					title = "注册成功";
					$('#ret #msg').append("<br>2秒后自动跳转");
					setTimeout(function(){window.location.href = "login.jsp"},2000);
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
			
			<div class="g_12" id="regTabs">
				<div class="widget_header wwOptions">
					<h4 class="widget_header_title wwIcon i_16_add">注册新用户</h4>
					<ul class="w_Tabs">
						<li><a href="#regTabs-1" title="我是学生">我是学生</a></li>
						<li><a href="#regTabs-2" title="我是企业">我是企业</a></li>
					</ul>
				</div>
				<div class="widget_contents noPadding">
					<span class="label lwParagraph">带*号为必填项</span>
					<div id="regTabs-1">
						<form id="stuform">
							<div class="line_grid half">
								<div class="g_3"><span class="label">用户名<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="text" name="stu.un" placeholder="登陆时使用的用户名" class="simple_field" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">密码<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="password" name="stu.pwd" class="simple_field" pattern="[a-z,A-Z,0-9]{3,18}" data-type="pwd" required>
									<div class="field_notice">a-z-A-Z-0-9</div>
									<div class="field_alert"></div>
								</div>
							</div>
							
							<div class="g_12 separator">
								<span></span>
							</div>
							
							<div class="line_grid half">
								<div class="g_3"><span class="label">真实姓名<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="text" name="stu.name" placeholder="真实姓名" class="simple_field" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">性别<span class="must">*</span></span></div>
								<div class="g_9">
									<input class="simple_form" type="radio" name="stu.sgender" value="true" required/><span class="label ilC">男</span>
									<input class="simple_form" type="radio" name="stu.sgender" value="false" /><span class="label ilC">女</span>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">身份证号<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="tel" name="stu.id_person" placeholder="身份证号" pattern="^[0-9]{17}[0-9,x,X]$" class="simple_field" data-type="id" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">学校<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="text" name="stu.school" placeholder="就读学校" class="simple_field" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">入学时间<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="month" name="stu.stime_enter" class="simple_field" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">学号<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="tel" name="stu.id_student" class="simple_field" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="g_12" >
								<input class="submitIt simple_buttons" style="margin:auto;" value="注册" type="submit"/>
							</div>
						</form>
					</div>
					<div id="regTabs-2">
						<form id="cpnform">
							<div class="line_grid half">
								<div class="g_3"><span class="label">用户名<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="text" name="cpn.un" placeholder="登陆时使用的用户名" class="simple_field" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">密码<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="password" name="cpn.pwd" class="simple_field" pattern="[a-z,A-Z,0-9]{3,18}" data-type="pwd" required>
									<div class="field_notice">a-z-A-Z-0-9</div>
									<div class="field_alert"></div>
								</div>
							</div>
							
							<div class="g_12 separator">
								<span></span>
							</div>
							
							<div class="line_grid half">
								<div class="g_3"><span class="label">公司名<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="text" name="cpn.name" placeholder="完整公司名" class="simple_field" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">负责人姓名<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="text" name="cpn.name_legal" placeholder="负责人姓名" class="simple_field" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">负责人身份证号<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="tel" name="cpn.id_legal" pattern="^[0-9]{17}[0-9,x,X]$" placeholder="负责人身份证号" class="simple_field" data-type="id" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">公司地址<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="text" name="cpn.addr" placeholder="完整公司地址" class="simple_field" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">公司联系电话<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="tel" name="cpn.tel" pattern="^1[345678][0-9]{9}$" class="simple_field" data-type="tel" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">负责人联系电话<span class="must">*</span></span></div>
								<div class="g_9">
									<input type="tel" name="cpn.cel" pattern="^1[345678][0-9]{9}$" class="simple_field" data-type="tel" required>
									<div class="field_alert"></div>
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">上传法人证件照</span></div>
								<div class="g_9">
									<div class="uploader" id="uniform-undefined">
										<input type="file" class="simple_form" name="cpn.img_id.img" size="27" style="opacity: 0;" accept="image/png, image/jpeg">
										<span class="filename">请选择图片文件</span>
										<span class="action" id="upimg"></span>
									</div>
								</div>
								<div class="lightbox cboxElement">
									<img src="Images/LightBox/preview.png" alt="lightbox">
								</div>
							</div>
							<div class="line_grid half">
								<div class="g_3"><span class="label">上传公司营业执照</span></div>
								<div class="g_9">
									<div class="uploader" id="uniform-undefined">
										<input type="file" class="simple_form" name="cpn.img_coltd.img" size="27" style="opacity: 0;" accept="image/png, image/jpeg">
										<span class="filename">请选择图片文件</span>
										<span class="action" id="upimg"></span>
									</div>
								</div>
								<div class="lightbox cboxElement">
									<img src="Images/LightBox/preview.png" alt="lightbox">
								</div>
							</div>
							<div class="g_12" >
								<input class="submitIt simple_buttons" style="margin:auto;" value="注册" type="submit"/>
							</div>
						</form>
					</div>
				</div>
			</div>
			
			
			
			
		</div>
	</div>
	
	
</body>
</html>