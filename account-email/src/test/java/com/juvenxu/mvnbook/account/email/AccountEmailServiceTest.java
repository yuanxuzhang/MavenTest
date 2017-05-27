package com.juvenxu.mvnbook.account.email;

import static junit.framework.Assert.assertEquals;

import javax.mail.Message;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;

public class AccountEmailServiceTest {
	private GreenMail greenMail;
	
	@Before
	public void startMailService()
		throws Exception
	{
		greenMail = new GreenMail(ServerSetup.SMTP);
		greenMail.setUser("test@juvenxu.com", "123456");
		greenMail.start();
	}
	
	@Test
	public void testSendMail()
		throws Exception
	{
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("account-email.xml");
		AccountEmailService accountEmailService = (AccountEmailService) ctx.getBean("accountEmailService");
		//ctx.
		
		String subject = "Test Subject";
		String htmlText = "<h3>Test</h3>";
		accountEmailService.sendMail("test3@juvenxu.com", subject, htmlText);
		
		greenMail.waitForIncomingEmail(2000, 1);
		
		Message[] msgs = greenMail.getReceivedMessages();
		assertEquals(1, msgs.length);
		assertEquals(subject, msgs[0].getSubject());
		assertEquals(htmlText, GreenMailUtil.getBody(msgs[0]).trim());
	}
	public void stopMailServer()
		throws Exception
	{
		greenMail.stop();
	}
}
