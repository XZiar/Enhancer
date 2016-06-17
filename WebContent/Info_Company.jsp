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

	<div class="g_6 contents_header">
		<h3 class="i_16_dashboard tab_label">个人信息</h3>
	</div>
	<div class="g_6 contents_options">
		<div class="simple_buttons">
			<div class="bwIcon i_16_help">${urole[user.role]}</div>
		</div>
	</div>
	
	<div class="g_4 quick_stats">
		<div class="big_stats visitor_stats">
			<c:out value="${user.score}"/>
		</div>
		<h5 class="stats_info">得分</h5>
	</div>
	<div class="g_4 quick_stats" data-url="mytasks">
		<div class="big_stats tickets_stats">
			<c:out value="${user.task_finish}"/>
		</div>
		<h5 class="stats_info">发布任务数</h5>
	</div>
	<div class="g_4 quick_stats" data-url="mytasks">
		<div class="big_stats users_stats">
			<c:out value="${user.task_ongoing}"/>
		</div>
		<h5 class="stats_info">进行中任务数</h5>
	</div>
		
	<div class="g_12 separator">
		<span></span>
	</div>
	
	<div class="g_6">
		<div class="widget_header">
			<h4 class="widget_header_title wwIcon i_16_tables">负责人身份证照</h4>
		</div>
		<div class="widget_contents noPadding">
			<img src="img?obj=${user.pic_id}" alt="id" style="max-width: 100%;">
		</div>
	</div>
	<div class="g_6">
		<div class="widget_header">
			<h4 class="widget_header_title wwIcon i_16_tables">企业执照</h4>
		</div>
		<div class="widget_contents noPadding">
			<img src="img?obj=${user.pic_coltd}" alt="coltd" style="max-width: 100%;">
		</div>
	</div>	

	
