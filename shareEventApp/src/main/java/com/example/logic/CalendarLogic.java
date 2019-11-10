package com.example.logic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import com.example.domain.Week;
import com.example.mapper.CalendarEventMapper;

public class CalendarLogic {

	@Autowired
	CalendarEventMapper calendarEventMapper;

	//いつのカレンダーを表示するか？
	public String selectedCalender(HttpMethod httpMethod , String selectedYear , String selectedMonth) {

			LocalDate calenderOfTheMonth;

		    int calenderForOtherYears;
			int calenderForOtherMonths;


		if (httpMethod == HttpMethod.GET) {

			calenderOfTheMonth = LocalDate.now();

		} else {

			calenderForOtherYears = Integer.parseInt(selectedYear);
			calenderForOtherMonths = Integer.parseInt(selectedMonth);

			calenderOfTheMonth = LocalDate.of(calenderForOtherYears, calenderForOtherMonths, 1);
		}

		DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy年MM月");
		String selectedCalender = calenderOfTheMonth.format(d);

		return selectedCalender;

	}

	//viewのselect年数
	public ArrayList<Integer> selectYear(){

		ArrayList<Integer> year = new ArrayList<>();

		for (int i = 2019; i <= 2030; i++) {
			year.add(i);
		}

		return year;
	}

	//viewのselect月数
	public ArrayList<Integer> selectMonth() {

		ArrayList<Integer> month = new ArrayList<>();

		for (int i = 1; i <= 12; i++) {
			month.add(i);
		}

		return month;

	}

	public int selectedYear(HttpMethod httpMethod , String selectedYear) {

		LocalDate localDate = LocalDate.now();

		if (httpMethod == HttpMethod.GET) {

			int currentYear = localDate.getYear();

			return currentYear;

		} else {

			int currentYear = Integer.parseInt(selectedYear);

			return currentYear;
		}

	}

	public int selectedMonth(HttpMethod httpMethod , String selectedMonth) {

		LocalDate localDate = LocalDate.now();

		if (httpMethod == HttpMethod.GET) {

			int currentMonth = localDate.getMonthValue();

			return currentMonth;

		} else {

			int currentMonth = Integer.parseInt(selectedMonth);

			return currentMonth;
		}

	}

	//月の日数
	public int totalDay(int currentYear, int currentMonth) {

		int totalDay = LocalDate.of(currentYear, currentMonth, 1).lengthOfMonth();
		return totalDay;

	}

	//月の最初の曜日を調べる 日曜1-土曜日7
	public int weekIndex(int currentYear , int currentMonth) {

		Calendar cal = Calendar.getInstance();
		cal.set(currentYear, currentMonth - 1, 1);
		int weekIndex = cal.get(Calendar.DAY_OF_WEEK);
		return weekIndex;
	}

	public ArrayList<Week> currentDays(int weekIndex , int totalDay){

		ArrayList<Week> currentDays = new ArrayList<>();

		Week week = new Week();
		int weekDay = 0;

		//日曜日が最初のカレンダー 10月は火曜からなのでweekDay+2
				if (weekIndex == 2) {
					weekDay += 1;
				}
				if (weekIndex == 3) {
					weekDay += 2;
				}
				if (weekIndex == 4) {
					weekDay += 3;
				}
				if (weekIndex == 5) {
					weekDay += 4;
				}
				if (weekIndex == 6) {
					weekDay += 5;
				}
				if (weekIndex == 7) {
					weekDay += 6;
				}
				//日付を格納
				for (int i = 1; i <= totalDay; i++) {

					week.setMonthDay(weekDay, i);
					weekDay++;

				}

				currentDays.add(week);

		return currentDays;

	}

	/*
	public List<List<CalendarEvent>> findEventTitleLists(int totalDays , int weekIndex , int currentYear , int currentMonth){

		CalendarEvents calendarEvents = new CalendarEvents();

		for (int i = 0; i < weekIndex - 1; i++) {
			calendarEvents.setLists(new ArrayList<CalendarEvent>());
		}

		for (int day = 1 ; day <= totalDays; day++) {
			//日付をyyyyMMDDに変更
			LocalDate c = LocalDate.of(currentYear, currentMonth, day);
			DateTimeFormatter da = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String selectedDay = c.format(da);
			//最初の週
			calendarEvents.setLists(calendarEventMapper.findTitle(selectedDay));
		}

		//最後の週
		for (int i = 0; i + weekIndex + totalDays <= 42; i++) {
			calendarEvents.setLists(new ArrayList<CalendarEvent>());
		}

		return calendarEvents.getTitleLists();
		*/

	public String selectedDays(int currentYear , int currentMonth , int day) {

		LocalDate c = LocalDate.of(currentYear, currentMonth, day);
		DateTimeFormatter da = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String selectedDays = c.format(da);

		return selectedDays;

	}

}

/*
 * //日付をyyyyMMDDに変更
			LocalDate c = LocalDate.of(currentYear, currentMonth, day);
			DateTimeFormatter da = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String selectedDay = c.format(da);
			//最初の週
 * */

