package com.juvenxu.mvnbook.account.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.portable.OutputStream;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.juvenxu.mvnbook.account.captcha.AccountCaptchaException;
import com.juvenxu.mvnbook.account.service.AccountService;
import com.juvenxu.mvnbook.account.service.AccountServiceException;

public class CaptchaImageServlet extends HttpServlet {
	private static final long serialVersionUID = 8654459574928762167L;
	private ApplicationContext context;
	
	@Override
	public void init() throws ServletException {
		super.init();
		context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		String key = req.getParameter("key");
		
		if(key == null || key.length() == 0){
			resp.sendError(400, "No captcha Key Found");
		} else {
			AccountService service = (AccountService) context.getBean("account-Service");
			
			try {
				resp.setContentType("image/jpeg");
				ServletOutputStream out = resp.getOutputStream();
				out.write(service.generateCaptchaImage(key));
			} catch (AccountServiceException e) {
				resp.sendError(404, e.getMessage());
			} catch (AccountCaptchaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
