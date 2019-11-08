package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.domain.ParticipateEvent;

@Mapper
public interface ParticipateEventMapper {

	void join(@Param("id")int id,@Param("username")String username,@Param("join")String join);

	ParticipateEvent findJoin(@Param("id")int id,@Param("username")String username,@Param("join")String join);

	List<ParticipateEvent> participateList(int id);

	void unjoin(String username);
	
	void deleteJoin(int id);
}