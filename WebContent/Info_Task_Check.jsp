<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<script>
var g_tchecks;
function rfs_tc()
{
	$.ajax({
		type : "POST",
		url : "gettchecks",
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
			var obj = $("#tclist");
			obj.html("");
			g_tchecks = ret.tchecks;
			$.each(g_tchecks, function(i, task)
			{
				var ctxt = "<tr data-tid='" + task.tid + "'><td class='ttitle'>" + task.title + "</td><td>"
					+ task.launcher + "</td><td><div class='simple_buttons' id='tcpass'><div>通过</div></div>"
					+ "<div class='simple_buttons' id='tcfail'><div>拒绝</div></div></td></tr>";
				obj.append(ctxt);
			});
			MsgTip("目前有"+g_tchecks.length+"个任务未被审核");
		}
	});
}
function chk_tc(tid, pass)
{
	$.ajax({
		type : "POST",
		data : "tid="+tid+"&pass="+pass,
		url : "dotcheck",
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
			rfs_tc();
		}
	});
}
$(document).ready(function()
{
	$("#rfs_tc").click(function()
	{
		rfs_tc();
	});
	$("#tclist").on("click","#tcpass",function()
	{
		chk_tc($(this).parents("tr").data("tid"),true);
	});
	$("#tclist").on("click","#tcfail",function()
	{
		chk_tc($(this).parents("tr").data("tid"),false);
	});
	$("#tclist").on("click",".ttitle",function()
	{
		window.open("taskview?tid=" + $(this).parents("tr").data("tid"));
	});
});
</script>
<div class="g_6 contents_header">
	<h3 class="i_16_dashboard tab_label">管理面板</h3>
</div>
<div class="g_6 contents_options" id="rfs_tc">
	<div class="simple_buttons">
		<div class="bwIcon i_16_help">刷新</div>
	</div>
</div>
<div class="g_12">
	<div class="widget_contents noPadding">
		<table class="tables">
			<thead><tr>
				<th width="60%">任务标题</th>
				<th width="20%">发布公司</th>
				<th>操作</th>
			</tr></thead>
			<tbody id="tclist">
			</tbody>
		</table>
	</div>
</div>

	
