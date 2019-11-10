package com.example.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.domain.CalendarEvent;

@Mapper
public interface CalendarEventMapper {

	List<CalendarEvent> findOne(int id);

	Optional<CalendarEvent> findId(int id);

	List<CalendarEvent> findAll();

	List<CalendarEvent> findTitle(@Param("selectedDay")String selectedDay);

	void save(CalendarEvent calendarEvent);

	void update(CalendarEvent calendarEvent);

	void delete(int id);

}
