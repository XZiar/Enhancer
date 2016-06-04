<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pojo.TaskBean, pojo.UserBean, pojo.StudentBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>


<script>
$(document).ready(function()
{
	$('.i_16_close').click(function()
	{
		$('.theme-popover-mask').fadeOut(100);
		$('.theme-popover').slideUp(200);
	});
	
	$('#fntasks').on("click",".ttitle",function()
	{
		var tid = $(this).data("tid");
		$('#sendapply').data("tid", tid);
		$('#tpm_fn').fadeIn(100);
		$('#tp_fn').slideDown(200);
	});
	
	$('#sendapply').click(function()
	{
		$.ajax({
			type : "POST",
			url : "confirmapplyer",
			data : "&tid=" + $(this).data("tid"),
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
			<h4 class="widget_header_title wwIcon i_16_tooltip">发表评价</h4>
			<div class="w_Options i_16_close"><span class="aclose"></span></div>
		</div>
		<div class="widget_contents noPadding">
			<div class="g_12" id="aps">
				<div class="widget_contents noPadding" style="max-height:400px; overflow:auto;">
					<textarea class="simple_field" name="des"></textarea>
				</div>
			</div>
			<div class="g_12">
				<div class="g_6">
					<span class="label">
						本次任务评分
					</span>
				</div>
				<div class="g_6">
					<input type="number" class="simple_form" name="score" min="1" max="5" required />
				</div>
			</div>
			<div class="g_12" style="text-align:center;">
				<div class="submitIt simple_buttons" id="sendapply" data-uid="-1">提交评价</div>
			</div>
		</div>
	</div>
	<div class="theme-popover-mask" id="tpm_fn"></div>
	
	<input type="hidden" name="tid" value='<s:property value="task.TID"/>'/>
	
	<div class="g_12">
		<div class="widget_contents noPadding">
			<table class="tables">
				<thead>
					<tr>
						<th>任务名</th>
						<th width="15%">状态</th>
						<th width="15%">发布者</th>
					</tr>
				</thead>
				<tbody id="fntasks">
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
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	
