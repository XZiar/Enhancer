<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<style>
.quick_stats {cursor: pointer;}
</style>
<script>
$(document).ready(function()
{
	$('.quick_stats').click(function()
	{
		window.location.href = $(this).data("url");
	});
});
</script>
	<!-- Quick Statistics -->
	<div class="g_4 quick_stats">
		<div class="big_stats visitor_stats">
			<c:out value="${obj.score}"/>
		</div>
		<h5 class="stats_info">得分</h5>
	</div>
	<div class="g_4 quick_stats" data-url="mytasks">
		<div class="big_stats tickets_stats">
			<c:out value="${obj.task_finish}"/>
		</div>
		<h5 class="stats_info">完成任务数</h5>
	</div>
	<div class="g_4 quick_stats" data-url="mytasks">
		<div class="big_stats users_stats">
			<c:out value="${obj.task_progress}"/>
		</div>
		<h5 class="stats_info">待完成任务数</h5>
	</div>
			
			
