package com.example.logic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.domain.CalendarEvent;

public class EventFormValidation {

	public boolean timeValidate(CalendarEvent calenderEvent) {

		String startHour = calenderEvent.getStartTime().substring(0,2);

		String startMinutes = calenderEvent.getStartTime().substring(3);

		int startTime = Integer.parseInt(startHour + startMinutes);

		String endHour = calenderEvent.getEndTime().substring(0,2);

		String endMinutes = calenderEvent.getEndTime().substring(3);

		int endTime = Integer.parseInt(endHour + endMinutes);

		if(startTime >= endTime) {
			return true;
		}

		return false;

	}



public boolean dayValidate(CalendarEvent calenderEvent){

	LocalDate today = LocalDate.now();

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	int today_yyyyMMdd = Integer.parseInt(today.format(formatter));

	String selectedDay = calenderEvent.getEventDay();

	String selectedYear = selectedDay.substring(0,4);

	String selectedMonth = selectedDay.substring(5,7);

	String selectedMinutes = selectedDay.substring(8);

	int selectedEventDay = Integer.parseInt(selectedYear + selectedMonth + selectedMinutes);

	if(today_yyyyMMdd >= selectedEventDay) {
		return true;
	}

	return false;

}

}
