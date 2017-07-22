package com.juvenxu.mvnbook.account.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
		
        
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//super.doGet(req, resp);

		String key = req.getParameter("key");
		
		if(key == null || key.length() == 0){
			resp.sendError(400, "No captcha Key Found");
		} else {
			AccountService service = (AccountService) context.getBean("accountService");
			try {
				
				//resp.setHeader("Pragma", "No-cache");  
		        //resp.setHeader("Cache-Control", "No-cache");  
		        //resp.setDateHeader("Expires", 0);  
		        resp.setContentType("image/jpeg");
				ServletOutputStream out = resp.getOutputStream();
				out.write(service.generateCaptchaImage(key));
				
				
				/*// FIMME 直接生成验证码回传现在有问题
				byte[] captchaImage = service.generateCaptchaImage(key);
				String path = "D:/ProgramFolder/workspace/account-webs/src/main/webapp/images/";
				File image = new File(path + key +".jpg");
				FileOutputStream output = null;
				output = new FileOutputStream(image);
				output.write(captchaImage);
				output.flush();
				output.close();
				
				//req.getRequestDispatcher("./images/" + key +".jpg");
				resp.sendRedirect("./images/" + key +".jpg");
				return;*/
				
			} catch (AccountServiceException e) {
				resp.sendError(404, e.getMessage());
			} catch (AccountCaptchaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}
	
}
