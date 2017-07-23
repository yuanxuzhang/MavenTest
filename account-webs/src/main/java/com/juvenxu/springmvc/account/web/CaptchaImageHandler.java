package com.juvenxu.springmvc.account.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.juvenxu.mvnbook.account.captcha.AccountCaptchaException;
import com.juvenxu.mvnbook.account.service.AccountService;
import com.juvenxu.mvnbook.account.service.AccountServiceException;

@Controller
// account-webs这个不要拦截
//@RequestMapping("/account-webs")
public class CaptchaImageHandler {
	
	@RequestMapping("/captcha_image")
	public void captchaImageAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//super.doGet(req, resp);

		String key = req.getParameter("key");
		
		if(key == null || key.length() == 0){
			resp.sendError(400, "No captcha Key Found");
		} else {
			
			ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
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
				resp.sendRedirect("./images/" + key +".jpg")
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
