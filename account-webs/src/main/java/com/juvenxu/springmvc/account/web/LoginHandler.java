package com.juvenxu.springmvc.account.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.juvenxu.mvnbook.account.service.AccountService;
import com.juvenxu.mvnbook.account.service.AccountServiceException;

//@Component 注释外，还定义了几个拥有特殊语义的注释，它们分别是：@Repository、@Service 和 @Controller
@Controller
//@RequestMapping("/account-webs")
public class LoginHandler {	
	
	@RequestMapping("/login")
	public void loginAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
		// XXX req.getSession().getServletContext()
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
		AccountService service = (AccountService) context.getBean("accountService");
		
		try {
			service.login(id, password, captchaKey, captchaValue);
			resp.sendRedirect("./mainPage.html");
		} catch (AccountServiceException e) {
			resp.sendError(400, e.getMessage());
		}
	}
}
