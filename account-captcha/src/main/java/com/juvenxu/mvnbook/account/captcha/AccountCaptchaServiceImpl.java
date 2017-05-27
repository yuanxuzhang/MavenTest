package com.juvenxu.mvnbook.account.captcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.InitializingBean;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

public class AccountCaptchaServiceImpl implements AccountCaptchaService, InitializingBean {
//com.juvenxu.mvnbook.account.captcha.AccountCaptchaServiceImpl
	private DefaultKaptcha producer;
	
	private Map<String, String> captchaMap = new HashMap<String, String>();
	private List<String> preDefineTexts;
	private int textCount = 0;
	
	//TODO
	public void afterPropertiesSet() throws Exception {
		producer = new DefaultKaptcha();
		producer.setConfig(new Config(new Properties()));
	}
	
	public String generateCaptchaKey() throws AccountCaptchaException {
		String key = RandomGenerator.getRandomString();
		String value = getCaptchaText();
		captchaMap.put(key, value);
		
		return key;
	}

	private String getCaptchaText() {
		if(preDefineTexts != null && !preDefineTexts.isEmpty()){
			String text = preDefineTexts.get(textCount);
			textCount = (textCount +1) % preDefineTexts.size();
			
			return text;
		}else{
			return producer.createText();
		}
	}

	public byte[] generateCaptchaImage(String captchaKey) throws AccountCaptchaException {
		String text = captchaMap.get(captchaKey);
		if(text == null){
			//TODO
			throw new AccountCaptchaException();
		}
		
		BufferedImage image = producer.createImage(text);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			ImageIO.write(image, "jpg", out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out.toByteArray();
	}

	public boolean validateCaptcha(String captchaKey, String captchaValue) throws AccountCaptchaException {
		String text = captchaMap.get(captchaKey);
		if(text == null){
			//TODO
			throw new AccountCaptchaException();
		}
		
		if(text.equals(captchaValue)){
			captchaMap.remove(captchaKey);
			
			return true;
		}else{
			return false;
		}
		
	}

	public List<String> getPreDefinedTexts() {
		return preDefineTexts;
	}

	public void setPreDefinedTexts(List<String> preDefinedTexts) {
		this.preDefineTexts = preDefinedTexts;
	}

}
