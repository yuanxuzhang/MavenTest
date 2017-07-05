<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.juvenxu.mvnbook.account.service.*"%>
<%@ page import="org.springframework.context.ApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<% ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	   AccountService accountservice = (AccountService)context.getBean("accountService");
	   String captchaKey = accountservice.generateCaptchaKey();
	%>
	
	<div class = "text-field">
		<h2>注册新用户</h2>
		<form name="signup" action="signup" method="post">
			<label>账户ID:</label>
			<input type="text" name="id" /><br />
			<label>Email:</label>
			<input type="text" name="email"></input><br />
			<label>显示名称:</label>
			<input type="text" name="name"></input><br />
			<label>密码:</label>
			<input type="password" name="password"></input><br />
			<label>确认密码:</label>
			<input type="password" name="confirm_password"></input><br />
			<label>验证码:</label>
			<input type="text" name="captcha_value"></input><br />
			<input type="hidden" name="captcha_key" value="<%=captchaKey%>"></input>
			<img src="<%=request.getContextPath() %>/captcha_image?key=<%=captchaKey %>" />
			<br />
			<button>确认并提交</button>
		</form>
	</div>
</body>
</html>