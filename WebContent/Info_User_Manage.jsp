<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<script>
var g_umusers;
function rfs_um(type)
{
	$.ajax({
		type : "POST",
		data : "type="+type,
		url : "getusers",
		success : function(data,type)
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
			var obj = $("#umlist");
			obj.html("");
			g_umusers = ret.users;
			$.each(g_umusers, function(i, user)
			{
				var ctxt = "<tr data-idx='" + i + "'><td>" + user.name + "</td><td>" 
					+ user.task_ongoing + "/" + user.task_finish + "</td></tr>";
				obj.append(ctxt);
			});
			$('#umdetail').html("");
			MsgTip("目前有"+g_umusers.length+"个"+ret.msg);
		}
	});
}
function deluser(uid)
{
	$.ajax({
		type : "POST",
		data : "uid="+uid,
		url : "deluser",
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
			rfs_um("student");
		}
	});
}
$(document).ready(function()
{
	$("#umdetail").on("click","#umdel",function()
	{
		var uid = $(this).data("uid");
		$('#ret #msg').html("确定要删除用户<br>" + $(this).data("name") + "<br>吗？");
		$('#ret').dialog(
		{
			buttons: 
			{
		        "是": function() 
		        {
		        	deluser(uid);
		          	$(this).dialog("close");
		        },
		        "否": function() 
		        {
		          	$(this).dialog("close");
		        }
			}
		}).dialog("open");
		
	});
	$("#umlist").on("click","tr",function()
	{
		var obj = $("#umdetail");
		obj.html("");
		var user = g_umusers[$(this).data("idx")];
		var ctxt = "<tr><td>用户名</td><td>" + user.un + "</td></tr>";
		if(user.accountRole == "student")
		{
			ctxt += "<tr><td>姓名</td><td>" + user.name + "</td></tr>";
			ctxt += "<tr><td>性别</td><td>" + (user.gender?"男":"女") + "</td></tr>";
			ctxt += "<tr><td>身份证号</td><td>" + user.id_person + "</td></tr>";
			ctxt += "<tr><td>学校名</td><td>" + user.school + "</td></tr>";
			ctxt += "<tr><td>入学日期</td><td>" + user.time_enter + "</td></tr>";
			ctxt += "<tr><td>学号</td><td>" + user.id_student + "</td></tr>";
		}
		else if(user.accountRole == "company")
		{
			ctxt += "<tr><td>公司名</td><td>" + user.name + "</td></tr>";
			ctxt += "<tr><td>地址</td><td>" + user.addr + "</td></tr>";
			ctxt += "<tr><td>负责人姓名</td><td>" + user.name_legal + "</td></tr>";
			ctxt += "<tr><td>负责人身份证号</td><td>" + user.id_legal + "</td></tr>";
			ctxt += "<tr><td>公司联系电话</td><td>" + user.tel + "</td></tr>";
			ctxt += "<tr><td>负责人联系电话</td><td>" + user.cel + "</td></tr>";
			ctxt += "<tr><td>公司营业执照</td><td><img style='max-width: 100%;' src='img?obj=" + user.pic_coltd + "'></td></tr>";
			ctxt += "<tr><td>负责人身份证照</td><td><img style='max-width: 100%;' src='img?obj=" + user.pic_id + "'></td></tr>";
		}
		ctxt += "<tr><td colspan='2'><div class='simple_buttons' id='umdel' data-uid='"+ user.uid 
		+ "' data-name='" + user.name + "'><div>删除</div><div></td></tr>";
		obj.append(ctxt);
	});
});
</script>
	<div class="g_6 contents_header">
		<h3 class="i_16_dashboard tab_label">管理面板</h3>
	</div>
	<div class="g_6 contents_options">
		<div class="simple_buttons" onclick='rfs_um("student")'>
			<div class="bwIcon i_16_help">刷新学生</div>
		</div>
		<div class="simple_buttons" onclick='rfs_um("company")'>
			<div class="bwIcon i_16_help">刷新企业</div>
		</div>
	</div>
	<div class="g_4">
		<div class="widget_contents noPadding">
			<table class="tables">
				<thead><tr>
					<th width="50%">姓名/公司名</th>
					<th>任务数(进行中/已完成)</th>
				</tr></thead>
				<tbody id="umlist">
				</tbody>
			</table>
		</div>
	</div>
	<div class="g_8">
		<div class="widget_contents noPadding">
			<table class="tables">
				<tbody id="umdetail">
				</tbody>
			</table>
		</div>
	</div>

	
