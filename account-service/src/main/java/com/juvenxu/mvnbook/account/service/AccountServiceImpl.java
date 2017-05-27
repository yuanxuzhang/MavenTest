package com.juvenxu.mvnbook.account.service;

import java.util.HashMap;
import java.util.Map;

import com.juvenxu.mvnbook.account.captcha.AccountCaptchaException;
import com.juvenxu.mvnbook.account.captcha.AccountCaptchaService;
import com.juvenxu.mvnbook.account.captcha.RandomGenerator;
import com.juvenxu.mvnbook.account.email.AccountEmailService;
import com.juvenxu.mvnbook.account.persist.Account;
import com.juvenxu.mvnbook.account.persist.AccountPersistException;
import com.juvenxu.mvnbook.account.persist.AccountPersistService;

public class AccountServiceImpl implements AccountService {
	
	private AccountPersistService accountPersistService;
	private AccountEmailService accountEmailService;
	private AccountCaptchaService accountCaptchaService;

	public AccountPersistService getAccountPersistService() {
		return accountPersistService;
	}

	public void setAccountPersistService(AccountPersistService accountPersistService) {
		this.accountPersistService = accountPersistService;
	}

	public AccountEmailService getAccountEmailService() {
		return accountEmailService;
	}

	public void setAccountEmailService(AccountEmailService accountEmailService) {
		this.accountEmailService = accountEmailService;
	}

	public AccountCaptchaService getAccountCaptchaService() {
		return accountCaptchaService;
	}

	public void setAccountCaptchaService(AccountCaptchaService accountCaptchaService) {
		this.accountCaptchaService = accountCaptchaService;
	}

	public String generateCaptchaKey() throws AccountServiceException {
		try {
			return accountCaptchaService.generateCaptchaKey();
		} catch (AccountCaptchaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//TODO 这里的return和try里的不冲突吗？
			return null;
		}
	}

	//接口同样会限定实现类异常的抛出
	public byte[] generateCaptchaImage(String captchaKey) throws AccountServiceException, AccountCaptchaException {
		return accountCaptchaService.generateCaptchaImage(captchaKey);
	}

	private Map<String, String> activationMap = new HashMap<String, String>();
	public void signUp(SignUpRequest signUpRequest) throws AccountServiceException, AccountCaptchaException {
		if(!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())){
			throw new AccountServiceException("2 password do not match.");
		}
		if(!accountCaptchaService.validateCaptcha(signUpRequest.getCaptchaKey(), signUpRequest.getCaptchaValue())){
			throw new AccountServiceException("Incorrect Captcha.");
		}
		Account account = new Account();
		account.setId(signUpRequest.getId());
		account.setEmail(signUpRequest.getEmail());
		account.setName(signUpRequest.getName());
		account.setPassword(signUpRequest.getPassword());
		account.setActivated(false);
		
		try {
			accountPersistService.createAccount(account);
		} catch (AccountPersistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//TODO
		String activationId = RandomGenerator.getRandomString();
		activationMap.put(activationId, account.getId());
		String link = signUpRequest.getActivateServiceUrl().endsWith("/") ? signUpRequest.getActivateServiceUrl()
				+activationId : signUpRequest.getActivateServiceUrl() + "?key=" + activationId;
		accountEmailService.sendMail(account.getEmail(), "Please Activate Your Account", link);
				
	}

	public void activate(String activationNumber) throws AccountServiceException {
		String acocuntId = activationMap.get(activationNumber);
		if(acocuntId == null){
			throw new AccountServiceException("Invalid account activation ID.");
		}
		try {
			Account account = accountPersistService.readAccount(acocuntId);
			account.setActivated(true);
			accountPersistService.updateAccount(account);
		} catch (AccountPersistException e) {
			throw new AccountServiceException("Unable to activate account");
		}
		
	}

	public void login(String id, String password) throws AccountServiceException {
		try {
			Account account = accountPersistService.readAccount(id);
			if(account == null){
				throw new AccountServiceException("Account does not exist.");
			}
			if(!account.isActivated()){
				throw new AccountServiceException("Account is disable");
			}
			if(!account.getPassword().equals(password)){
				throw new AccountServiceException("Incorrect password");
			}
		} catch (AccountPersistException e) {
			// TODO Auto-generated catch block
			throw new AccountServiceException("Unable to login.", e);
		}
	}

}
