package com.juvenxu.mvnbook.account.dbpersist;

import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.juvenxu.mvnbook.account.dbpersist.model.*;

/**
 * 
 * @author yiibai
 * @copyright http://www.yiibai.com
 * @date 2015/09/22
 */
public class HelloWord {
	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;

	static {
		try {
			reader = Resources.getResourceAsReader("myBatisConfig.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static SqlSessionFactory getSession() {
		return sqlSessionFactory;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*SqlSession session = sqlSessionFactory.openSession();
		try {
			User user = (User) session.selectOne(
					"com.juvenxu.mvnbook.account.dbpersist.model.UserMapper.selectUserById");
			if(user!=null){
				String userInfo = "名字："+user.getName()+", 所属部门："+user.getEmail()+", 主页："+user.getPassword();
				System.out.println(userInfo);
			}
		} finally {
			session.close();
		}*/
		

        /*SqlSession session = sqlSessionFactory.openSession();
        try {
            IUser userOperation=session.getMapper(IUser.class);
            User user = userOperation.selectUserById(1);

            System.out.println(user.getFemail());
            System.out.println(user.getFname());
        } finally {
            session.close();
        }*/
		
		//addUser();
		//getUserList("%张%");
		//updateUser();
		deleteUser(2);
	}
	
	 public static void addUser(){
	        User user=new User();
	        user.setFname("张四");
	        user.setFemail("zhangsi@gmail.com");
	        user.setFpassword("123456");
	        user.setFactivivated(true);
	        SqlSession session = sqlSessionFactory.openSession();
	        try {
	            IUser userOperation=session.getMapper(IUser.class);
	            userOperation.addUser(user);
	            session.commit();
	            System.out.println("当前增加的用户 id为:"+user.getFid());
	        } finally {
	            session.close();
	        }
	    }
	 
	 public static void getUserList(String userName){
	        SqlSession session = sqlSessionFactory.openSession();
	        try {
	            IUser userOperation=session.getMapper(IUser.class);           
	            List<User> users = userOperation.selectUsers(userName);
	            for(User user:users){
	                System.out.println(user.getFid()+":"+user.getFname()+":"+user.getFpassword());
	            }
	            
	        } finally {
	            session.close();
	        }
	    }
	 
	 public static void updateUser(){
	        //先得到用户,然后修改，提交。
	        SqlSession session = sqlSessionFactory.openSession();
	        try {
	            IUser userOperation=session.getMapper(IUser.class);
	            User user = userOperation.selectUserById(1);            
	            user.setFactivivated(true);
	            userOperation.updateUser(user);
	            session.commit();
	            
	        } finally {
	            session.close();
	        }
	    }
	 
	    public static void deleteUser(int id){
	        SqlSession session = sqlSessionFactory.openSession();
	        try {
	            IUser userOperation=session.getMapper(IUser.class);           
	            userOperation.deleteUser(id);
	            session.commit();            
	        } finally {
	            session.close();
	        }
	    }
}
