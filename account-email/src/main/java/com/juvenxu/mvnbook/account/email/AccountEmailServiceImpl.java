package com.juvenxu.mvnbook.account.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class AccountEmailServiceImpl implements AccountEmailService{
	
	private static final Logger log = Logger.getLogger(AccountEmailServiceImpl.class);
	
	private JavaMailSender javaMailSender;
	
	private String systemEmail;
	
	public void sendMail(String to, String subject, String htmlText){
		
		log.error("sendMail");
		
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
	/*public void sendMail(String to, String subject, String htmlText){
		try {  
	        Properties props = new Properties();  
	        props.put("username", );   
	        props.put("password", "xxxxxx");   
	        props.put("mail.transport.protocol", "smtp" );  
	        props.put("mail.smtp.host", "smtp.163.com");  
	        props.put("mail.smtp.port", "25" );  
	
	        Session mailSession = Session.getDefaultInstance(props);  
	
	        Message msg = new MimeMessage(mailSession);     
	        msg.setFrom(new InternetAddress("13326349318@163.com"));  
	        msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));   
	        msg.setSubject(subject);   
	        msg.setContent(htmlText,"text/html;charset=UTF-8");
	
	        msg.saveChanges();  
	
	        Transport transport = mailSession.getTransport("smtp");  
	        transport.connect(props.getProperty("mail.smtp.host"), props  
	                .getProperty("username"), props.getProperty("password"));   
	        transport.sendMessage(msg, msg.getAllRecipients());  
	        transport.close();     
	    } catch (Exception e) {  
	        e.printStackTrace();  
	        System.out.println(e);  
	    }   
	}   */

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
