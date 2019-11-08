package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.loginUser.Account;

@Mapper
public interface UserMapper {

	Account findUsername(@Param("username")String username);

	void saveUser(Account account);

	Account findUser(Account account);

}

