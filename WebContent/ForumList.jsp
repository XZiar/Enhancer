<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="xziar.enhancer.pojo.UserBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="UTF-8">
	<title>论坛 ---- Enhancer实战培训中心</title>
	<!-- The Main CSS File -->
	<link rel="stylesheet" href="CSS/merge.css" />
	<!-- jQuery -->
	<script src="Javascript/jQuery/jquery-1.12.0.min.js"></script>
	<style>
		.ptitle {cursor: pointer;}
	</style>
</head>
<body>
	<%@ include file="PageHead.jsp"%>
<script>
$(document).ready(function()
{
	$("#forumlist tr").each(function()
	{
		var obj = $(this).children(".ptime");
		obj.text(new Date(parseInt(obj.text())).toLocaleDateString());
	});
	$(".ptitle").on("click",function()
	{
		window.location.href = "postview?pid=" + $(this).data("id");
	});
	$("#addpost").click(function()
	{
		window.location.href = "PostPost.jsp";
	});
});
</script>
	<div class="wrapper contents">
		<div class="grid_wrapper">
		
<c:if test="${! empty user }">	
			<div class="g_12" style="text-align: center;">
				<div class="simple_buttons" id="addpost">
					<div>发布新话题</div>
				</div>
			</div>
</c:if>

			<div class="g_12">
				<div class="widget_contents noPadding">
					<table class="tables">
						<thead>
							<tr>
								<th>话题</th>
								<th width="15%">发布者</th>
								<th width="15%">发布日期</th>
								<th width="10%">回帖数量</th>
							</tr>
						</thead>
						<tbody id="forumlist">
							<c:forEach var="p" items="${posts }">
							    <tr>
									<td class="ptitle" data-id='<c:out value="${p.pid }"/>'>
										<c:out value='${p.title }'/>
									</td>
									<td data-id='<c:out value="${p.uid }"/>'>
										<c:out value='${p.poster }'/>
									</td>
									<td class="ptime"><c:out value='${p.time_post }'/></td>
									<td><c:out value='${p.replycount }'/></td>
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