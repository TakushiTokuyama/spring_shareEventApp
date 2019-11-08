package com.example.logic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.http.HttpMethod;

public class CalenderLogic {


	private LocalDate calenderOfTheMonth;

	private int calenderForOtherYears;
	private int calenderForOtherMonths;

	private ArrayList<Integer> month = new ArrayList<>();
	private ArrayList<Integer> year = new ArrayList<>();

	//いつのカレンダーを表示するか？
	public String selectedCalender(HttpMethod httpMethod , String selectedYear , String selectedMonth) {

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

		for (int i = 2019; i <= 2030; i++) {
			this.year.add(i);
		}

		return year;
	}

	//viewのselect月数
	public ArrayList<Integer> selectMonth() {

		for (int i = 1; i <= 12; i++) {
			this.month.add(i);
		}

		return month;

	}

}
