package com.juvenxu.mvnbook.account.captcha;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountCaptchaServiceTest {
	private AccountCaptchaService service;
	
	@Before
	public void prepare(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("account-captcha.xml");
		service = (AccountCaptchaService) ctx.getBean("accountCaptchaService");
	}
	
	@Test
	public void testGenerateCaptcha() throws AccountCaptchaException{
		List<String> preDefinedTexts = new ArrayList<String>();
		preDefinedTexts.add("123456");
		service.setPreDefinedTexts(preDefinedTexts);
		String captchaKey = service.generateCaptchaKey();
		assertNotNull(captchaKey);
		
		byte[] captchaImage = service.generateCaptchaImage(captchaKey);
		assertTrue(captchaImage.length > 0);
		
		File image = new File("target/"+ captchaKey +".jpg");
		OutputStream output = null;
		try {
			output = new FileOutputStream(image);
			output.write(captchaImage);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(output != null){
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Test
	public void testValidateCaptchaCorrect() throws AccountCaptchaException{
		List<String> preDefinedTexts = new ArrayList<String>();
		preDefinedTexts.add("123456");
		preDefinedTexts.add("abcde");
		service.setPreDefinedTexts(preDefinedTexts);
		
		String captchaKey = service.generateCaptchaKey();
		service.generateCaptchaImage(captchaKey);
		assertTrue(service.validateCaptcha(captchaKey, "123456"));
		
		captchaKey = service.generateCaptchaKey();
		service.generateCaptchaImage(captchaKey);
		assertTrue(service.validateCaptcha(captchaKey, "abcde"));
	}
	
	@Test
	public void testValidateCaptchaInCorrect() throws AccountCaptchaException{
		List<String> preDefinedTexts = new ArrayList<String>();
		preDefinedTexts.add("123456");
		service.setPreDefinedTexts(preDefinedTexts);
		
		String captchaKey = service.generateCaptchaKey();
		service.generateCaptchaImage(captchaKey);
		assertFalse(service.validateCaptcha(captchaKey, "67890"));
	}
}
