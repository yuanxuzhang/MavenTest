package com.juvenxu.mvnbook.account.persist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class AccountPersistServiceImpl implements AccountPersistService {
	
	/*public static void  main(String[] args){
		Account account = new Account();
		account.setId("testID");
		account.setName("testName");
		account.setEmail("testEmail");
		account.setPassword("testPassword");
		account.setActivated(true);
		try {
			new AccountPersistServiceImpl().createAccount(account);
		} catch (AccountPersistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	private static final String ELEMENT_ROOT = "account-persist";
	private static final String ELEMENT_ACCOUNTS = "accounts";
	private static final String ELEMENT_ACCOUNT = "account";
	private static final String ELEMENT_ACCOUNT_ID = "id";
	private static final String ELEMENT_ACCOUNT_NAME = "name";
	private static final String ELEMENT_ACCOUNT_EMAIL = "email";
	private static final String ELEMENT_ACCOUNT_PASSWORD = "password";
	private static final String ELEMENT_ACCOUNT_ACTIVATED = "activated";
	
	private String file;
	//XML读取的类
	private SAXReader reader = new SAXReader();
	
	private Document readDocument() throws AccountPersistException {
		File dataFile = new File(file);
		if(!dataFile.exists()){
			dataFile.getParentFile().mkdirs();
			Document doc = DocumentFactory.getInstance().createDocument();
			Element rootEle = doc.addElement(ELEMENT_ROOT);
			rootEle.addElement(ELEMENT_ACCOUNTS);
			writeDocument(doc);
		}
		
		try {
			return reader.read(new File(file));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private void writeDocument(Document doc) {
		Writer out = null;
		
		try {
			out = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
			//XML写的类
			XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
			writer.write(doc);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public Account createAccount(Account account) throws AccountPersistException {
		File dataFile = new File(file);
		if(!dataFile.exists()){
			dataFile.getParentFile().mkdirs();	
			Document doc = DocumentFactory.getInstance().createDocument();
			Element rootEle = doc.addElement(ELEMENT_ROOT);
			rootEle.addElement(ELEMENT_ACCOUNTS);
			writeDocument(doc);
		}
		Document doc = null;
		try {
			doc = reader.read(new File(file));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		//TODO 不应当重复创建
		if(readAccount(account.getId()) == null){
			Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);
			Element accountEle = accountsEle.addElement(ELEMENT_ACCOUNT);
			accountEle.addElement(ELEMENT_ACCOUNT_ID).addText(account.getId());
			accountEle.addElement(ELEMENT_ACCOUNT_NAME).addText(account.getName());
			accountEle.addElement(ELEMENT_ACCOUNT_EMAIL).addText(account.getEmail());
			accountEle.addElement(ELEMENT_ACCOUNT_PASSWORD).addText(account.getPassword());
			accountEle.addElement(ELEMENT_ACCOUNT_ACTIVATED).addText(String.valueOf(account.isActivated()));	
		}
				
		writeDocument(doc);
		return account;
	}

	@SuppressWarnings("unchecked")
	public Account readAccount(String id) throws AccountPersistException {
		Document doc = readDocument();
		Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);
		for(Element accountEle : (List<Element>)accountsEle.elements()){ 
			if(accountEle.elementText(ELEMENT_ACCOUNT_ID).equals(id)){
				return buildAccount(accountEle);
			}
		}
		return null;
	}

	private Account buildAccount(Element element) {
		Account account = new Account();
		
		account.setId(element.elementText(ELEMENT_ACCOUNT_ID));
		account.setName(element.elementText(ELEMENT_ACCOUNT_NAME));
		account.setEmail(element.elementText(ELEMENT_ACCOUNT_EMAIL));
		account.setPassword(element.elementText(ELEMENT_ACCOUNT_PASSWORD));
		account.setActivated("true".equals(element.elementText(ELEMENT_ACCOUNT_ACTIVATED)));
		return account;
	}

	@SuppressWarnings("unchecked")
	public Account updateAccount(Account account) throws AccountPersistException {
		Document doc = readDocument();
		Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);
		for(Element accountEle : (List<Element>)accountsEle.elements()){ 
			if(accountEle.elementText(ELEMENT_ACCOUNT_ID).equals(account.getId())){
				accountEle.element(ELEMENT_ACCOUNT_NAME).setText(account.getName());
				accountEle.element(ELEMENT_ACCOUNT_EMAIL).setText(account.getEmail());
				accountEle.element(ELEMENT_ACCOUNT_PASSWORD).setText(account.getPassword());
				accountEle.element(ELEMENT_ACCOUNT_ACTIVATED).setText(String.valueOf(account.isActivated()));
			}
		}
		File persistDataFile = new File(file);
		persistDataFile.delete();
		writeDocument(doc);
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public void deleteAccount(String id) throws AccountPersistException {
		//boolean deleteFlag = false;
		Document doc = readDocument();
		Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);
		for(Element accountEle : (List<Element>)accountsEle.elements()){ 
			if(accountEle.elementText(ELEMENT_ACCOUNT_ID).equals(id)){
				accountsEle.remove(accountEle);
				//deleteFlag = true;
			}
		}
		
		//if(deleteFlag){
			//全部删除，然后在写入
			File persistDataFile = new File(file);
			persistDataFile.delete();
			writeDocument(doc);
		//}
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
