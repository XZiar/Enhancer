<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pojo.TaskBean,pojo.UserBean" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
		.ttitle {cursor: pointer;}
	</style>
</head>
<body>
	<%@ include file="PageHead.jsp"%>
<script>
$(document).ready(function()
{
	$("#forumlist tr").each(function()
	{
		var obj = $(this).children(".ttime");
		obj.text(new Date(parseInt(obj.text())).toLocaleDateString());
	});
	$(".ttitle").on("click",function()
	{
		window.location.href = "taskview?id=" + $(this).data("id");
	});
});
</script>
	<div class="wrapper contents">
		<div class="grid_wrapper">

			<div class="g_12">
				<div class="widget_contents noPadding">
					<table class="tables">
						<thead>
							<tr>
								<th>任务名</th>
								<th width="5%">状态</th>
								<th width="10%">发布者</th>
								<th width="10%">发布日期</th>
								<th width="5%">申请人数</th>
							</tr>
						</thead>
						<tbody id="forumlist">
							<s:set name="tstatus" value="{'待审核','报名中','报名截止','进行中','已完结'}"/>
							<s:iterator value="tasks" id='t'> 
							    <tr>
									<td class="ttitle" data-id='<s:property value="#t.TID"/>'>
										<s:property value='#t.title'/>
									</td>
									<td><s:property value='#tstatus[#t.status]'/></td>
									<td data-id='<s:property value="#t.UID"/>'>
										<s:property value='#t.launcher'/>
									</td>
									<td class="ttime"><s:property value='#t.time_start'/></td>
									<td><s:property value='#t.applycount'/></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
			</div>
			
		</div>
	</div>
	
	
</body>
</html>