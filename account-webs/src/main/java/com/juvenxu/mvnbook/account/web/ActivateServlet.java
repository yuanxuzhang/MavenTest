package com.juvenxu.mvnbook.account.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.juvenxu.mvnbook.account.service.AccountService;
import com.juvenxu.mvnbook.account.service.AccountServiceException;

public class ActivateServlet extends HttpServlet {
	
	private static final long serialVersionUID = 2764601130995390419L;
	
	private ApplicationContext context;
	
	@Override
	public void init() throws ServletException {
		super.init();
		context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String key = req.getParameter("key");
		
		AccountService service = (AccountService) context.getBean("accountService");
		try {
			service.activate(key);
		} catch (AccountServiceException e) {
			// TODO º”»’÷æ
			e.printStackTrace();
		}
		RequestDispatcher dispatcher = req.getRequestDispatcher("success.html");
		dispatcher.forward(req, resp);
		//resp.sendRedirect("success.html");
	}

}
