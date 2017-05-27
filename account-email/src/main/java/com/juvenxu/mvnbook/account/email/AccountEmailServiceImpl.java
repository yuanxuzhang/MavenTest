package com.juvenxu.mvnbook.account.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class AccountEmailServiceImpl implements AccountEmailService{
	private JavaMailSender javaMailSender;
	
	private String systemEmail;
	
	public void sendMail(String to, String subject, String htmlText){
		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper msgHelper = new MimeMessageHelper(msg);
		
		try {
			msgHelper.setFrom(systemEmail);
			msgHelper.setTo(to);
			msgHelper.setSubject(subject);
			msgHelper.setText(htmlText);
			
			javaMailSender.send(msg);
		} catch (MailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getSystemEmail() {
		return systemEmail;
	}

	public void setSystemEmail(String systemEmail) {
		this.systemEmail = systemEmail;
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
}
