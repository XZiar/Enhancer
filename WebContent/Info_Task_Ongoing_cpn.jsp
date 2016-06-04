<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pojo.TaskBean, pojo.UserBean, pojo.StudentBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>

<script>
function preApplicants(data)
{
	var obj = $('#aps .widget_contents');
	obj.html("");
	$('#des').val("");
	$.each(data,function(i,ap)
	{
		var cont = "<div class='line_grid' data-uid='" + ap.uid + "'>"
			+ "<div class='g_2'><span class='label'>名称</span></div>"
			+ "<div class='g_4'>" + ap.name + "</div>"
			+ "<div class='g_2'><span class='label'>人数</span></div>"
			+ "<div class='g_4'>" + ap.people + "</div>"
			+ "</div>";
		obj.append(cont);
		$('#des').data(ap.uid.toString(),ap.des);
	});
}
$(document).ready(function()
{
	$('.i_16_close').click(function()
	{
		$('.theme-popover-mask').fadeOut(100);
		$('.theme-popover').slideUp(200);
	});

	$('#tasklist').on("click",".ttitle",function()
	{
		var tid = $(this).data("tid");
		$.ajax({
			type : "POST",
			url : "getapplyers",
			data : "tid=" + tid,
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
					if(ret.msg == "error")
					{
						window.location.href = "403.jsp";
						return;
					}
				}
				else
				{
					preApplicants(ret.applicants);
					$('#sendapply').data("tid", tid);
					$('#tpm_og').fadeIn(100);
					$('#tp_og').slideDown(200);
				}
			}
		});
	});

	$('#aps').on("click",".line_grid",function()
	{
		var uid = $(this).data("uid");
		$('#sendapply').data("uid", uid);
		$(this).siblings().removeClass("chosen");
		$(this).addClass("chosen");
		$('#des').val($('#des').data(uid.toString()));
	});
	
	$('#sendapply').click(function()
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



	<div class="theme-popover" id="tp_og" style="display:none;">
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
				<div class="submitIt simple_buttons" id="sendapply" data-uid="-1">接受申请</div>
			</div>
		</div>
	</div>
	<div class="theme-popover-mask" id="tpm_og"></div>
	
	<input type="hidden" name="tid" value='<s:property value="task.TID"/>'/>
	
	<div class="g_12">
		<div class="widget_contents noPadding">
			<table class="tables">
				<thead>
					<tr>
						<th>任务名</th>
						<th width="12%">状态</th>
						<th width="8%">申请人数</th>
					</tr>
				</thead>
				<tbody id="tasklist">
					<c:set var="tstatus" value="${fn:split('待审核,报名中,报名截止,进行中,已完结', ',')}" />
					<c:forEach var="t" items="${tasks}">
						<tr>
							<td class="ttitle" data-tid="<c:out value='${t.TID} '/>">
								<c:out value='${t.title} '/>
							</td>
							<td><c:out value='${tstatus[t.status]}'/></td>
							<td data-uid='c:out value="${t.UID} "/>'>
								<c:out value='${t.launcher} '/>
							</td>
							<td class="ttime"><c:out value='${t.time_start} '/></td>
							<td><c:out value='${t.applycount} '/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	
