package com.example.domain;

import java.util.ArrayList;
import java.util.List;

public class CalendarEvents {

	private List<List<CalendarEvent>> titleLists = new ArrayList<List<CalendarEvent>>();

//	private List<CalendarEvent> lists = new ArrayList<>();

//	public List<CalendarEvent> getLists() {
//		return lists;
//	}

	public void setLists(List<CalendarEvent> lists) {
		titleLists.add(lists);
	}

	public List<List<CalendarEvent>> getTitleLists() {
		return titleLists;
	}

//	public void setTitleLists(List<List<CalendarEvent>> titleLists) {
//		this.titleLists = titleLists;
//	}



}
