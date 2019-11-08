package com.example.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.domain.CalenderEvent;

@Mapper
public interface CalenderEventMapper {

	List<CalenderEvent> findOne(int id);

	Optional<CalenderEvent> findId(int id);

	List<CalenderEvent> findAll();

	List<CalenderEvent> findTitle(@Param("selectedDay")String selectedDay);

	void save(CalenderEvent calenderEvent);

	void update(CalenderEvent calenderEvent);

	void delete(int id);

}
