<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<script>
var g_tmans;
function rfs_tm()
{
	$.ajax({
		type : "POST",
		url : "gettmans",
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
			var obj = $("#tmlist");
			obj.html("");
			g_tmans = ret.tmans;
			$.each(g_tmans, function(i, task)
			{
				var ctxt = "<tr data-tid='" + task.tid + "'><td class='ttitle'>" + task.title + "</td><td>"
					+ task.launcher + "</td><td><div class='simple_buttons' id='tmpass'><div>结束</div></div>"
					+ "<div class='simple_buttons' id='tmfail'><div>关闭</div></div></td></tr>";
				obj.append(ctxt);
			});
			MsgTip("目前有"+g_tmans.length+"个任务未被审核");
		}
	});
}
function chk_tm(tid, pass)
{
	$.ajax({
		type : "POST",
		data : "tid="+tid+"&pass="+pass,
		url : "dotman",
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
	$("#rfs_tm").click(function()
	{
		rfs_tm();
	});
	$("#tmlist").on("click","#tmpass",function()
	{
		chk_tm($(this).parents("tr").data("tid"),true);
	});
	$("#tmlist").on("click","#tmfail",function()
	{
		chk_tm($(this).parents("tr").data("tid"),false);
	});
	$("#tmlist").on("click",".ttitle",function()
	{
		window.open("taskview?tid=" + $(this).parents("tr").data("tid"));
	});
});
</script>
<div class="g_6 contents_header">
	<h3 class="i_16_dashboard tab_label">管理面板</h3>
</div>
<div class="g_6 contents_options" id="rfs_tm">
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
			<tbody id="tmlist">
			</tbody>
		</table>
	</div>
</div>

	
