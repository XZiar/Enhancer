<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<script>
var g_uchecks;
function rfs_uc()
{
	$.ajax({
		type : "POST",
		url : "getuchecks",
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
			var obj = $("#uclist");
			obj.html("");
			g_uchecks = ret.uchecks;
			$.each(g_uchecks, function(i, user)
			{
				var ctxt = "<tr data-idx='" + i + "'><td>" + user.name + "</td></tr>";
				obj.append(ctxt);
			});
			$('#ucdetail').html("");
			MsgTip("目前有"+g_uchecks.length+"个用户未被审核");
		}
	});
}
function chk_uc(uid, pass)
{
	$.ajax({
		type : "POST",
		data : "uid="+uid+"&pass="+pass,
		url : "doucheck",
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
			rfs_uc();
		}
	});
}
$(document).ready(function()
{
	$("#rfs_uc").click(function()
	{
		rfs_uc();
	});
	$("#ucdetail").on("click","#ucpass",function()
	{
		chk_uc($(this).data("uid"),true);
	});
	$("#ucdetail").on("click","#ucfail",function()
	{
		chk_uc($(this).data("uid"),false);
	});
	$("#uclist").on("click","tr",function()
	{
		var obj = $("#ucdetail");
		obj.html("");
		var user = g_uchecks[$(this).data("idx")];
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
		ctxt += "<tr><td><div class='simple_buttons' id='ucpass' data-uid='"+ user.uid +"'><div>通过</div></div></td>"
			+ "<td><div class='simple_buttons' id='ucfail' data-uid='"+ user.uid +"'><div>拒绝</div></div></td></tr>";
		obj.append(ctxt);
		
	});
});
</script>
	<div class="g_6 contents_header">
		<h3 class="i_16_dashboard tab_label">管理面板</h3>
	</div>
	<div class="g_6 contents_options" id="rfs_uc">
		<div class="simple_buttons">
			<div class="bwIcon i_16_help">刷新</div>
		</div>
	</div>
	<div class="g_4">
		<div class="widget_contents noPadding">
			<table class="tables">
				<thead>
					<tr><th>姓名/公司名</th></tr>
				</thead>
				<tbody id="uclist">
				</tbody>
			</table>
		</div>
	</div>
	<div class="g_8">
		<div class="widget_contents noPadding">
			<table class="tables">
				<tbody id="ucdetail">
				</tbody>
			</table>
		</div>
	</div>


	
