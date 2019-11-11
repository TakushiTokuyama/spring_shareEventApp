package com.example.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CalendarEvent {

	private int id;
	@Size(min = 0, max = 20, message = "0-20文字")
	private String name;
	@NotEmpty(message = "空白は不可")
	private String eventDay;
	@NotEmpty(message = "空白は不可")
	private String startTime;
	@NotEmpty(message = "空白は不可")
	private String endTime;
	@NotEmpty(message = "空白は不可")
	@Size(min = 0, max = 20, message = "0-20文字")
	private String title;
	@Size(min = 0, max = 160, message = "0-160文字")
	private String content;
	@Size(min = 0, max = 20, message = "0-20文字")
	@NotEmpty(message = "空白は不可")
	private String place;

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEventDay(String eventDay) {
		this.eventDay = eventDay;
	}

	public String getEventDay() {
		return eventDay;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

}
