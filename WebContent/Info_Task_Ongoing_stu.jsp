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
	
	$('#tasklist').on("click",".ttitle",function()
	{
		var tid = $(this).data("tid");
		$('#sendapply').data("tid", tid);
		$('#tpm_og').fadeIn(100);
		$('#tp_og').slideDown(200);
	});
	
	$('#sendapply').click(function()
	{
		if(!$("input[name='agree']").is(':checked'))
		{
			alert("请阅读并同意协议！");
			return false;
		}
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
	<div class="theme-popover" id="tp_og" style="display:none;">
		<div class="widget_header wwOptions">
			<h4 class="widget_header_title wwIcon i_16_tooltip">签署协议</h4>
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
				<div class="submitIt simple_buttons" id="sendapply" data-uid="-1">接受任务</div>
			</div>
		</div>
	</div>
	<div class="theme-popover-mask" id="tpm_og"></div>
	
	<input type="hidden" name="tid" value='<s:property value="task.tid"/>'/>
	
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
				<tbody id="tasklist">
					<c:set var="tstatus" value="${fn:split('待审核,报名中,报名截止,进行中,已完结', ',')}" />
					<c:forEach var="t" items="${tasks}">
						<tr>
							<td class="ttitle" data-tid="<c:out value='${t.tid} '/>">
								<c:out value='${t.title} '/>
							</td>
							<td><c:out value='${tstatus[t.status]}'/></td>
							<td data-uid='c:out value="${t.uid} "/>'>
								<c:out value='${t.launcher} '/>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	
