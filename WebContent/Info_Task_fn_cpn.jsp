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
				var ctxt = "<tr data-tid='" + t.tid + "'><td>" + t.title + "</td><td>"
					+ tsstatus[t.status]+"</td><td>"
					+ t.applycount + "</td></tr>";
				obj.append(ctxt);
			});
			MsgTip("目前有"+ret.fntasks.length+"个待结束的任务");
		}
	});
}
function preDoers(data)
{
	var obj = $('#tp_fn #aps .widget_contents');
	obj.html("");
	var dobj = $('#tp_fn #des');
	dobj.val("");
	$.each(data,function(i,ap)
	{
		var cont = "<div class='line_grid' data-uid='" + ap.uid + "'>"
			+ "<div class='g_2'><span class='label'>名称</span></div>"
			+ "<div class='g_4'>" + ap.name + "</div>"
			+ "<div class='g_2'><span class='label'>人数</span></div>"
			+ "<div class='g_4'>" + ap.people + "</div>"
			+ "</div>";
		obj.append(cont);
		dobj.data(ap.uid.toString(),ap.des);
	});
}
$(document).ready(function()
{
	$("#rfspart3").click(function()
	{
		rfs_og();
	});
	$("#fnlist").on("click","tr",function()
	{
		var tid = $(this).data("tid");
		$.ajax({
			type : "POST",
			url : "getapplyers",
			data : "tid=" + tid,
			success : function(data)
			{
				var ret = JSON.parse(data);
				if(!validRet(ret))
					return;

				preDoers(ret.applicants);
				$('#tp_fn .submitIt').data("tid", tid);
				$('#tpm_fn').fadeIn(100);
				$('#tp_fn').slideDown(200);
			}
		});
	});
	$('#tp_fn #aps').on("click",".line_grid",function()
	{
		var uid = $(this).data("uid");
		$('#tp_fn .submitIt').data("uid", uid);
		$(this).siblings().removeClass("chosen");
		$(this).addClass("chosen");
		$('#tp_fn #des').val($('#tp_fn #des').data(uid.toString()));
	});
	$('#tp_fn .submitIt').click(function()
	{
		var uid = $(this).data("uid");
		if(uid < 0)
		{
			alert("请选择申请人！");
			return false;
		}
		if(!$("input[name='agree']").is(':checked'))
		{
			alert("请阅读并同意协议！");
			return false;
		}
		$.ajax({
			type : "POST",
			url : "acceptapplyer",
			data : "uid=" + uid + "&tid=" + $(this).data("tid"),
			success : function(data)
			{
				var ret = JSON.parse(data);
				var words = "申请成功";
				var title = "申请成功";
				if(ret.success)
				{
					setTimeout(function(){location.reload(true);},3000);
				}
				else
				{
					title = "申请失败";
					switch(ret.msg)
					{
					case "unlogin":
						window.location.href = "login.jsp";
						break;
					case "error":
						words = "系统错误！";
						break;
					}
				}
				$('#ret #msg').html(words);
				$('#ret').dialog("option","title",title).dialog("open");
			}
		});
		return false;
	});
});
</script>

	<div class="theme-popover" id="tp_fn" style="display:none;">
		<div class="widget_header wwOptions">
			<h4 class="widget_header_title wwIcon i_16_tooltip">选择申请人</h4>
			<div class="w_Options i_16_close"><span class="aclose"></span></div>
		</div>
		
		<div class="widget_contents noPadding">
			<div class="g_6" id="aps">
				<div class="widget_contents noPadding" style="max-height:400px; overflow:auto;">
				
				</div>
			</div>
			<div class="g_6">
				<span class="label">申请人自述</span>
				<textarea class="simple_field" id="des" readonly></textarea>
			</div>
			<div class="g_12">
				<div class="g_9">
					<span class="label">
						同意签署<a href="liscen.html">业务外包协议</a>
						<span class="must">*</span>
					</span>
				</div>
				<div class="g_3">
					<input type="checkbox" class="simple_form" name="agree" required />
				</div>
			</div>
			<div class="g_12" style="text-align:center;">
				<div class="submitIt simple_buttons" data-uid="-1">接受申请</div>
			</div>
		</div>
	</div>
	<div class="theme-popover-mask" id="tpm_fn"></div>

	<div class="g_6 contents_header">
		<h3 class="i_16_dashboard tab_label">待结束的任务</h3>
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
						<th width="15%">状态</th>
						<th width="10%">申请人数</th>
					</tr>
				</thead>
				<tbody id="fnlist">
				</tbody>
			</table>
		</div>
	</div>


	
