package com.juvenxu.mvnbook.account.dbpersist.model;

import java.util.List;

import org.apache.ibatis.annotations.Select;

public interface IUser {
	 //@Select("SELECT * FROM t_user where fid= #{id}")
	 public User selectUserById(int id);
	 
	 public List<User> selectUsers(String userName);
	 
	 public void addUser(User user);
	 
	 public void updateUser(User user);
	 
	 public void deleteUser(int id);
}
