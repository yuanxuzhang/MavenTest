package com.juvenxu.mvnbook.account.service;

import com.juvenxu.mvnbook.account.captcha.AccountCaptchaException;

public interface AccountService {
	
	String generateCaptchaKey() throws AccountServiceException;

	byte[] generateCaptchaImage(String captchaKey) throws AccountServiceException, AccountCaptchaException;
	
	void signUp(SignUpRequest signUpRequest) throws AccountServiceException, AccountCaptchaException;
	
	void activate(String activationNumber) throws AccountServiceException;
	
	void login(String id, String password, String captchaKey, String captchaValue) throws AccountServiceException;
	
}
