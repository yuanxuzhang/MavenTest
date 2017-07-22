package com.juvenxu.mvnbook.account.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountPersistServiceTest {
	private AccountPersistService service;
	
	@Before
	public void prepare() throws AccountPersistException{
		//File persistDataFile = new File("\\target\\classes\\account-persist.xml");
		File persistDataFile = new File("E:\\persist-data.xml");
		if(persistDataFile.exists()){
			persistDataFile.delete();
		}
		ApplicationContext ctx = new ClassPathXmlApplicationContext("account-persist.xml");
		service = (AccountPersistService) ctx.getBean("accountPersistService");
		
		Account account= new Account();
		account.setId("juven");
		account.setName("Juven Xu");
		account.setEmail("juven@changeme.com");
		account.setPassword("this_should_be_encrypted");
		account.setActivated(true);
		
		service.createAccount(account);
	}
	
	@Test
	public void testReadAccount() throws AccountPersistException{
		Account account = service.readAccount("juven");
		
		assertNotNull(account);
		assertEquals("juven", account.getId());
		assertEquals("Juven Xu", account.getName());
		assertEquals("juven@changeme.com", account.getEmail());
		assertEquals("this_should_be_encrypted", account.getPassword());
		assertTrue(account.isActivated());
	}
	
	@Test
	public void testUpdateAccount() throws AccountPersistException{
		Account account= new Account();
		account.setId("juven");
		account.setName("Juven Xu_update");
		account.setEmail("juven@changeme.com_update");
		account.setPassword("this_should_be_encrypted_update");
		account.setActivated(false);
		
		service.updateAccount(account);
		account = service.readAccount("juven");
		assertNotNull(account);
		assertEquals("juven", account.getId());
		assertEquals("Juven Xu_update", account.getName());
		assertEquals("juven@changeme.com_update", account.getEmail());
		assertEquals("this_should_be_encrypted_update", account.getPassword());
		assertFalse(account.isActivated());
	}
	
	@Test
	public void testDeleteAccount() throws AccountPersistException{
		service.deleteAccount("juven");
		Account account = service.readAccount("juven");
		assertNull(account);
		//assertNotNull(account);

	}
}
