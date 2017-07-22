package com.juvenxu.mvnbook.account.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.juvenxu.mvnbook.account.captcha.AccountCaptchaService;

public class AccountServiceTest {
	private GreenMail greenMail;  
	  
    private AccountService accountService;  
  
    @Before  
    public void prepare() throws Exception {  
        String[] springConfigFiles = { "account-email.xml", "account-persist.xml", "account-captcha.xml", "account-service.xml" };  
  
        ApplicationContext ctx = new ClassPathXmlApplicationContext(springConfigFiles);  
  
        AccountCaptchaService accountCaptchaService = (AccountCaptchaService) ctx.getBean("accountCaptchaService");  
  
        List<String> preDefinedTexts = new ArrayList<String>();  
        preDefinedTexts.add("12345");  
        preDefinedTexts.add("abcde");  
        accountCaptchaService.setPreDefinedTexts(preDefinedTexts);  
  
        accountService = (AccountService) ctx.getBean("accountService");  
  
        greenMail = new GreenMail(ServerSetup.SMTP);  
        greenMail.setUser("test@xujava.iteye.com", "123456");  
        greenMail.start();  
  
        File persistDataFile = new File("target/test-classes/persist-data.xml");  
        if (persistDataFile.exists()) {  
            persistDataFile.delete();  
        }  
    }  
  
    @Test  
    public void testAccountService() throws Exception {  
        // 1. Get captcha  
        String captchaKey = accountService.generateCaptchaKey();  
        accountService.generateCaptchaImage(captchaKey);  
        String captchaValue = "12345";  
  
        // 2. Submit sign up Request  
        SignUpRequest signUpRequest = new SignUpRequest();  
        signUpRequest.setCaptchaKey(captchaKey);  
        signUpRequest.setCaptchaValue(captchaValue);  
        signUpRequest.setId("xuj");  
        signUpRequest.setEmail("test2@xujava.iteye.com");  
        signUpRequest.setName("xuj");  
        signUpRequest.setPassword("admin");  
        signUpRequest.setConfirmPassword("admin");  
        signUpRequest.setActivateServiceUrl("http://localhost:8080/account/activate");  
        accountService.signUp(signUpRequest);  
  
        // 3. Read activation link  
        greenMail.waitForIncomingEmail(2000, 1);  
        Message[] msgs = greenMail.getReceivedMessages();  
        assertEquals(1, msgs.length);  
        assertEquals("Please Activate Your Account", msgs[0].getSubject());  
        String activationLink = GreenMailUtil.getBody(msgs[0]).trim();  
  
        // 3a. Try login but not activated  
        try {  
            accountService.login("xuj", "admin", captchaKey, captchaValue);  
            fail("Disabled account shouldn't be able to log in.");  
        } catch (AccountServiceException e) {  
        }  
  
        // 4. Activate account  
        String activationCode = activationLink.substring(activationLink.lastIndexOf("=") + 1);  
        accountService.activate(activationCode);  
  
        // 5. Login with correct id and password  
        accountService.login("xuj", "admin", captchaKey, captchaValue);  
  
        // 5a. Login with incorrect password  
        try {  
            accountService.login("xuj", "admin1", captchaKey, captchaValue);  
            fail("Password is incorrect, shouldn't be able to login.");  
        } catch (AccountServiceException e) {  
        }  
  
    }  
  
    @After  
    public void stopMailServer() throws Exception {  
        greenMail.stop();  
    }
}
