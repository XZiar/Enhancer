<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="xziar.enhancer.pojo.TaskBean,xziar.enhancer.pojo.UserBean,xziar.enhancer.pojo.CompanyBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="UTF-8">
	<title>任务列表 ---- Enhancer实战培训中心</title>
	<!-- The Main CSS File -->
	<link rel="stylesheet" href="CSS/merge.css" />
	<!-- jQuery -->
	<script src="Javascript/jQuery/jquery-1.12.0.min.js"></script>
	<style>
		.ttitle {cursor: pointer;}
	</style>
</head>
<body>
	<%@ include file="PageHead.jsp"%>
<script>
$(document).ready(function()
{
	$("#tasklist tr").each(function()
	{
		var obj = $(this).children(".ttime");
		obj.text(new Date(parseInt(obj.text())).toLocaleDateString());
	});
	$(".ttitle").on("click",function()
	{
		window.location.href = "taskview?tid=" + $(this).data("tid");
	});
	$("#addtask").click(function()
	{
		window.location.href = "PostTask.jsp";
	});
});
</script>
	<div class="wrapper contents">
		<div class="grid_wrapper">
		
<c:if test="${(! empty user) && user.role == 2 }">
			<div class="g_12" style="text-align: center;">
				<div class="simple_buttons" id="addtask">
					<div>发布任务</div>
				</div>
			</div>
</c:if>

			<div class="g_12">
				<div class="widget_contents noPadding">
					<table class="tables">
						<thead>
							<tr>
								<th>任务名</th>
								<th width="10%">状态</th>
								<th width="12%">发布者</th>
								<th width="10%">发布日期</th>
								<th width="8%">申请人数</th>
							</tr>
						</thead>
						<tbody id="tasklist">
							<c:set var="tstatus" value="${fn:split('待审核,报名中,报名截止,进行中,已完结,已关闭', ',')}" />
							<c:forEach var="t" items="${tasks}">
								<tr>
									<td class="ttitle" data-tid="<c:out value='${t.tid} '/>">
										<c:out value='${t.title} '/>
									</td>
									<td><c:out value='${tstatus[t.status]}'/></td>
									<td data-uid='c:out value="${t.uid} "/>'>
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
			
		</div>
	</div>
	
	
</body>
</html>