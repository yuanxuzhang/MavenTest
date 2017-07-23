package com.juvenxu.springmvc.account.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.juvenxu.mvnbook.account.service.AccountService;
import com.juvenxu.mvnbook.account.service.AccountServiceException;

@Controller
public class ActivateHandler {

	@RequestMapping("/activate")
	public void activateAction(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		req.setCharacterEncoding("utf-8");
		String key = req.getParameter("key");
		
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
		AccountService service = (AccountService) context.getBean("accountService");
		try {
			service.activate(key);
		} catch (AccountServiceException e) {
			// TODO º”»’÷æ
			e.printStackTrace();
		}
		RequestDispatcher dispatcher = req.getRequestDispatcher("success.html");
		dispatcher.forward(req, resp);
		//return "success.html";
		//resp.sendRedirect("success.html");
	}
}
