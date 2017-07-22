package com.juvenxu.mvnbook.account.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.juvenxu.mvnbook.account.service.AccountService;
import com.juvenxu.mvnbook.account.service.AccountServiceException;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = -1343144793708857662L;
	private ApplicationContext context;
	
	@Override
	public void init() throws ServletException {
		super.init();
		context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String id = req.getParameter("id");
		String password = req.getParameter("password");
		String captchaKey = req.getParameter("captcha_key");
		String captchaValue = req.getParameter("captcha_value");
		
		if(id == null || id.length() == 0 || password == null || password.length() == 0 || captchaKey == null ||
				captchaKey.length() == 0 || captchaValue == null || captchaValue.length() == 0){
			resp.sendRedirect("./errorPage.html");
			return;
			//resp.sendError(400, "Parameter Incomplete");
		}
		
		AccountService service = (AccountService) context.getBean("accountService");
		
		try {
			service.login(id, password, captchaKey, captchaValue);
			resp.sendRedirect("./mainPage.html");
		} catch (AccountServiceException e) {
			resp.sendError(400, e.getMessage());
		}
	}

}
