<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<script>
function rfs_og()
{
	$.ajax({
		type : "POST",
		url : "ogtasks",
		success : function(data)
		{
			var ret = JSON.parse(data);
			if(!validRet(ret))
				return;
			
			var obj = $("#oglist");
			obj.html("");
			$.each(ret.ogtasks, function(i, t)
			{
				var ctxt = "<tr data-tid='" + t.tid + "'><td>" + t.title + "</td><td>"
					+ tsstatus[t.status]+"</td><td>"
					+ t.launcher + "</td></tr>";
				obj.append(ctxt);
			});
			MsgTip("目前有"+ret.ogtasks.length+"个待确认协议的任务");
		}
	});
}
function preApplicants(data)
{
	var obj = $('#tp_og #aps .widget_contents');
	obj.html("");
	var dobj = $('#tp_og #des');
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
	$("#oglist").on("click","tr",function()
	{
		var tid = $(this).data("tid");
		$('#tp_og .submitIt').data("tid", tid);
		$('#tpm_og').fadeIn(100);
		$('#tp_og').slideDown(200);
	});
	$('#tp_og .submitIt').click(function()
	{
		var tid = $(this).data("tid");
		if(tid < 0)
		{
			return false;
		}
		if(!$("input[name='agree']").is(':checked'))
		{
			alert("请阅读并同意协议！");
			return false;
		}
		$.ajax({
			type : "POST",
			url : "confirmapplyer",
			data : "tid=" + tid + "&uid=" + $(this).data("uid"),
			success : function(data)
			{
				var ret = JSON.parse(data);
				var words = "确认成功";
				var title = "确认成功";
				if(ret.success)
				{
					setTimeout(function(){location.reload(true);},3000);
				}
				else
				{
					title = "确认失败";
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

	<div class="theme-popover" id="tp_og" style="display:none;">
		<div class="widget_header wwOptions">
			<h4 class="widget_header_title wwIcon i_16_tooltip">选择申请人</h4>
			<div class="w_Options i_16_close"><span class="aclose"></span></div>
		</div>
		
		<div class="widget_contents noPadding">
			<div class="g_12">
				<div class="widget_contents noPadding" style="max-height:400px; overflow:auto;"><pre>
协议：
甲方xxxxxx
乙方yyyyyy
具体条款：
1.
2.
3.
4.
5.
6.
7.
8.
9.
10.
11.
				</pre></div>
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
				<div class="submitIt simple_buttons" data-tid="-1" data-uid="${user.uid }">接受任务</div>
			</div>
		</div>
	</div>
	<div class="theme-popover-mask" id="tpm_og"></div>

	<div class="g_6 contents_header">
		<h3 class="i_16_dashboard tab_label">待确认的任务</h3>
	</div>
	<div class="g_6 contents_options" id="rfspart3">
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
						<th width="10%">发布方</th>
					</tr>
				</thead>
				<tbody id="oglist">
				</tbody>
			</table>
		</div>
	</div>


	
