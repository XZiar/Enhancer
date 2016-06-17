<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<script>
function rfs_fn()
{
	$.ajax({
		type : "POST",
		url : "fntasks",
		success : function(data)
		{
			var ret = JSON.parse(data);
			if(!validRet(ret))
				return;
			
			var obj = $("#fnlist");
			obj.html("");
			$.each(ret.fntasks, function(i, t)
			{
				var ctxt = "<tr data-tid='" + t.tid + "'><td class='ttitle'>" + t.title + "</td><td>"
					+ "<div class='simple_buttons tcmt' ><div>提交评价</div></div>"
					+ "</td><td>" + tsstatus[t.status]+"</td><td>" + t.launcher + "</td></tr>";
				obj.append(ctxt);
			});
			MsgTip("目前有"+ret.fntasks.length+"个可评价的任务");
		}
	});
}
$(document).ready(function()
{
	$("#rfspart4").click(function()
	{
		rfs_fn();
	});
	$("#fnlist").on("click",".ttitle",function()
	{
		window.location.href = "taskview?tid=" + $(this).parents("tr").data("tid");
	});
	$("#fnlist").on("click",".tcmt",function()
	{
		var tid = $(this).parents("tr").data("tid");
		$('#sendcmt').data("tid", tid);
		$('#tpm_fn').fadeIn(100);
		$('#tp_fn').slideDown(200);
	});
	$("#sendcmt").click(function()
	{
		$.ajax({
			type : "POST",
			url : "comment",
			data : $('[name="cmt"]').serialize() + "&tid=" + $(this).data("tid") + "&score=" + $('[name="score"]').val(),
			success : function(data)
			{
				var ret = JSON.parse(data);
				if(!validRet(ret))
					return;
				$("#tp_fn .i_16_close").trigger("click");
				rfs_fn();
			}
		});
		
	});
});
</script>

	<div class="theme-popover" id="tp_fn" style="display:none;">
		<div class="widget_header wwOptions">
			<h4 class="widget_header_title wwIcon i_16_tooltip">任务评价</h4>
			<div class="w_Options i_16_close"><span class="aclose"></span></div>
		</div>
		
		<div class="widget_contents noPadding">
			<div class="g_12">
				<div class="widget_contents noPadding" style="max-height:400px; overflow:auto;">
					<textarea class="simple_field" name="cmt"></textarea>
				</div>
			</div>
			<div class="g_12">
				<div class="g_6">
					<span class="label">本次任务对公司的评分</span>
				</div>
				<div class="g_6">
					<input type="number" class="simple_form" name="score" min="1" max="5" value="3" required />
				</div>
			</div>
			<div class="g_12" style="text-align:center;">
				<div class="submitIt simple_buttons" id="sendcmt">提交评价</div>
			</div>
		</div>
	</div>
	<div class="theme-popover-mask" id="tpm_fn"></div>

	<div class="g_6 contents_header">
		<h3 class="i_16_dashboard tab_label">可评价的任务</h3>
	</div>
	<div class="g_6 contents_options" id="rfspart4">
		<div class="simple_buttons">
			<div class="bwIcon i_16_help">刷新</div>
		</div>
	</div>
	<div class="g_12">
		<div class="widget_contents noPadding">
			<table class="tables">
				<thead>
					<tr>
						<th>任务名</th>
						<th width="15%">操作</th>
						<th width="15%">状态</th>
						<th width="10%">发起方</th>
					</tr>
				</thead>
				<tbody id="fnlist">
				</tbody>
			</table>
		</div>
	</div>


	
