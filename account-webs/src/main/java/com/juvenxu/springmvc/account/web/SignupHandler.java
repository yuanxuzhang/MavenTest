package com.juvenxu.springmvc.account.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.juvenxu.mvnbook.account.captcha.AccountCaptchaException;
import com.juvenxu.mvnbook.account.service.AccountService;
import com.juvenxu.mvnbook.account.service.AccountServiceException;
import com.juvenxu.mvnbook.account.service.SignUpRequest;

@Controller
public class SignupHandler {
	
	@RequestMapping("/signup")
	public void signupAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//super.doPost(req, resp);
		req.setCharacterEncoding("utf-8");
		String id = req.getParameter("id");
		String email = req.getParameter("email");
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String confirmPassword = req.getParameter("confirm_password");
		String captchaKey = req.getParameter("captcha_key");
		String captchaValue = req.getParameter("captcha_value");
		
		if(id == null || id.length() == 0 || email == null || email.length() == 0 ||
				name == null || name.length() == 0 || password == null || password.length() == 0 ||
				confirmPassword == null || confirmPassword.length() == 0 || captchaKey == null ||
				captchaKey.length() == 0 || captchaValue == null || captchaValue.length() == 0){
			resp.sendRedirect("./errorPage.html");
			return;
			//resp.sendError(400, "Parameter Incomplete");
			
		}
		
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
		AccountService service = (AccountService) context.getBean("accountService");
		SignUpRequest request = new SignUpRequest();
		
		request.setId(id);
		request.setEmail(email);
		request.setName(name);
		request.setPassword(password);
		request.setConfirmPassword(confirmPassword);
		request.setCaptchaKey(captchaKey);
		request.setCaptchaValue(captchaValue);
		
		// TODO 系统解析激活host地址
		request.setActivateServiceUrl("http://localhost:8080/account-webs/activate");
		
		try {
			service.signUp(request);
			resp.getWriter().println("Account is created, please check your mail box for activation link.");
		} catch (AccountServiceException e) {
			resp.sendError(400, e.getMessage());
		} catch (AccountCaptchaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
