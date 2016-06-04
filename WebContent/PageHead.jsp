<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pojo.UserBean" %>
<%{%>
<%
UserBean user = (UserBean)session.getAttribute("user");
boolean isLogged = !(user == null);
%>
<!-- Top Panel -->
<div class="top_panel">
	<div class="wrapper">
		<div class="user" onclick="<%=isLogged?"my":"login.jsp"%>">
		<%if(isLogged){%>
			<img src="Images/user_avatar.png" class="user_avatar" />
			<span class="label"><%=user.getName()%></span>
			
		<%}else{%>
			<span class="label">未登录</span>
		<%}%>
		</div>
		<div class="top_links">
			<ul>
			<%if(isLogged){%>
				<li class="i_22_logout">
					<a href="logout" title="安全退出">
						<span class="label">安全退出</span>
					</a>
				</li>
			<%}else{%>
				<li class="i_22_login">
					<a href="login.jsp" title="登录">
						<span class="label">登录</span>
					</a>
				</li>
			<%}%>
			</ul>
		</div>
	</div>
</div>

<header class="main_header">
	<div class="wrapper">
		<div class="logo">
			<a href="index.html" title="Kanrisha Home">
				<img src="Images/logo.png" width="284px" alt="logo" />
			</a>
		</div>
		<nav class="top_buttons">
			<ul>
				<li class="big_button">
					<div class="out_border">
						<div class="button_wrapper">
							<div class="in_border">
								<a href="about" title="关于我们" class="the_button">
									<span class="i_32_about"></span>
								</a>
							</div>
						</div>
					</div>
				</li>
				<li class="big_button">
					<div class="big_count">
						<span>0</span>
					</div>
					<div class="out_border">
						<div class="button_wrapper">
							<div class="in_border">
								<a href="my" title="个人信息" class="the_button">
									<span class="i_32_user"></span>
								</a>
							</div>
						</div>
					</div>
				</li>
				<li class="big_button">
					<div class="out_border">
						<div class="button_wrapper">
							<div class="in_border">
								<a href="forum" title="论坛" class="the_button">
									<span class="i_32_forum"></span>
								</a>
							</div>
						</div>
					</div>
				</li>
				<li class="big_button">
					<div class="out_border">
						<div class="button_wrapper">
							<div class="in_border">
								<a href="task" title="任务列表" class="the_button">
									<span class="i_32_task"></span>
								</a>
							</div>
						</div>
					</div>
				</li>
			</ul>
		</nav>
	</div>
</header>
<%}%>