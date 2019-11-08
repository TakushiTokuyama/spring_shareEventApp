package com.example.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.CalenderEvent;
import com.example.domain.ParticipateEvent;
import com.example.domain.Week;
import com.example.logic.CalenderLogic;
import com.example.mapper.CalenderEventMapper;
import com.example.mapper.ParticipateEventMapper;

@Controller
public class CalenderEventController {

	@Autowired
	CalenderEventMapper calenderEventMapper;

	@Autowired
	ParticipateEventMapper participateEventMapper;

	@GetMapping("/calender/event_form")
	public ModelAndView namage_form(Principal principal, @ModelAttribute("formModel") CalenderEvent calenderEvent,
			ModelAndView mav) {
		mav.setViewName("calender/event_form");
		//loginUserName
		final String loginUser = principal.getName();
		mav.addObject("loginUser", loginUser);

        LocalDate date = LocalDate.now();

        mav.addObject(date);

		return mav;

	}

	@PostMapping("/calender/event_form")
	@Transactional(readOnly = false)
	public ModelAndView formResult(Principal principal,
			@ModelAttribute("formModel") @Validated CalenderEvent calenderEvent,
			BindingResult result,
			ModelAndView mav) {
		mav.setViewName("calender/event_form");
		//loginUserName
		final String loginUser = principal.getName();
		mav.addObject("loginUser", loginUser);

		if (result.hasErrors()) {

			return mav;

		}

		String a = calenderEvent.getStartTime().substring(0,2);
		String b = calenderEvent.getStartTime().substring(3);
		String e = a+b;
		int startTime = Integer.parseInt(e);

		String c = calenderEvent.getEndTime().substring(0,2);
		String d = calenderEvent.getEndTime().substring(3);
		String f = c+d;
		int endTime = Integer.parseInt(f);

		if(startTime >= endTime){
			mav.addObject("error","正しい時間を入れてください");

			return mav;
		}

		LocalDate nowTime = LocalDate.now();

		DateTimeFormatter da = DateTimeFormatter.ofPattern("yyyyMMdd");

		String nowString = nowTime.format(da);

		int nowInt = Integer.parseInt(nowString);

		String pastTime = calenderEvent.getEventDay();

		String year = pastTime.substring(0,4);
		String month = pastTime.substring(5,7);
		String minutes = pastTime.substring(8);

		String selectedFuture = year+month+minutes;

		int selectedFutureInt = Integer.parseInt(selectedFuture);

		if(selectedFutureInt <= nowInt) {

			mav.addObject("next","次の日以降を入力して下さい");

			return mav;
		}



		if (!result.hasErrors()) {
			calenderEventMapper.save(calenderEvent);

			return new ModelAndView("redirect:/calender/event");
		}

		return mav;
	}

	@RequestMapping("/calender/event")
	public ModelAndView manage(HttpServletRequest request, HttpMethod httpMethod, ModelAndView mav) {
		mav.setViewName("calender/event");

		String selectedYear = request.getParameter("selectedYear");
		String selectedMonth = request.getParameter("selectedMonth");

		//calenderロジックのインスタンス生成
		CalenderLogic calenderLogic = new CalenderLogic();

		String selectedCalender = calenderLogic.selectedCalender(httpMethod, selectedYear, selectedMonth);

		mav.addObject("selectedCalender", selectedCalender);

		mav.addObject("selectYear",calenderLogic.selectYear());

		mav.addObject("selectMonth",calenderLogic.selectMonth());
		
		LocalDateTime lo = LocalDateTime.now();

		int yearNow;

		int monthNow;

		//HTTPメソッドGETとPOSTで条件分岐
		if (httpMethod == HttpMethod.GET) {

			yearNow = lo.getYear();
			monthNow = lo.getMonthValue();

		} else {

			yearNow = Integer.parseInt(selectedYear);
			monthNow = Integer.parseInt(selectedMonth);

		}

		//現在の月の合計日数を調べる
		int totalDay = LocalDate.of(yearNow, monthNow, 1).lengthOfMonth();

		//月初の曜日の日付 日曜1-土曜日7
		Calendar cal = Calendar.getInstance();
		cal.set(yearNow, monthNow - 1, 1);
		int weekIndex = cal.get(Calendar.DAY_OF_WEEK);
		mav.addObject("weekIndex", weekIndex);

		ArrayList<Week> calenderDays = new ArrayList<>();
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
		//日付をカレンダーに表示
		for (int i = 1; i <= totalDay; i++) {

			week.setMonthDay(weekDay, i);
			weekDay++;

		}

		calenderDays.add(week);

		mav.addObject("calenderDays", calenderDays);

		//1日分のtitle格納 31日分
		ArrayList<CalenderEvent> titleListOne = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTwo = new ArrayList<>();
		ArrayList<CalenderEvent> titleListThree = new ArrayList<>();
		ArrayList<CalenderEvent> titleListFour = new ArrayList<>();
		ArrayList<CalenderEvent> titleListFive = new ArrayList<>();
		ArrayList<CalenderEvent> titleListSix = new ArrayList<>();
		ArrayList<CalenderEvent> titleListSeven = new ArrayList<>();
		ArrayList<CalenderEvent> titleListEight = new ArrayList<>();
		ArrayList<CalenderEvent> titleListNine = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTen = new ArrayList<>();

		ArrayList<CalenderEvent> titleListEleven = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTwelve = new ArrayList<>();
		ArrayList<CalenderEvent> titleListThirteen = new ArrayList<>();
		ArrayList<CalenderEvent> titleListFourteen = new ArrayList<>();
		ArrayList<CalenderEvent> titleListFifteen = new ArrayList<>();
		ArrayList<CalenderEvent> titleListSixteen = new ArrayList<>();
		ArrayList<CalenderEvent> titleListSeventeen = new ArrayList<>();
		ArrayList<CalenderEvent> titleListEighteen = new ArrayList<>();
		ArrayList<CalenderEvent> titleListNineteen = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTwenty = new ArrayList<>();

		ArrayList<CalenderEvent> titleListTwentyOne = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTwentyTwo = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTwentyThree = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTwentyFour = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTwentyFive = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTwentySix = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTwentySeven = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTwentyEight = new ArrayList<>();
		ArrayList<CalenderEvent> titleListTwentyNine = new ArrayList<>();

		ArrayList<CalenderEvent> titleListThirty = new ArrayList<>();
		ArrayList<CalenderEvent> titleListThirtyOne = new ArrayList<>();

		//31日分のtitle格納
		List<ArrayList<CalenderEvent>> titleLists = new ArrayList<ArrayList<CalenderEvent>>();

		for (int day = 1, a = 0; day <= totalDay; day++) {

			//日付をyyyyMMDDに変更
			LocalDate c = LocalDate.of(yearNow, monthNow, day);
			DateTimeFormatter da = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String selectedDay = c.format(da);
			//最初の週
			while (a < 1) {
				for (int i = 0; i < weekIndex - 1; i++) {
					titleLists.add(new ArrayList<CalenderEvent>());
					a++;
				}
				a++;
			}
			//31日分
			switch (day) {
			case 1:
				titleListOne.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListOne);
				break;
			case 2:
				titleListTwo.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTwo);
				break;
			case 3:
				titleListThree.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListThree);
				break;
			case 4:
				titleListFour.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListFour);
				break;
			case 5:
				titleListFive.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListFive);
				break;
			case 6:
				titleListSix.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListSix);
				break;
			case 7:
				titleListSeven.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListSeven);
				break;
			case 8:
				titleListEight.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListEight);
				break;
			case 9:
				titleListNine.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListNine);
				break;
			case 10:
				titleListTen.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTen);
				break;
			case 11:
				titleListEleven.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListEleven);
				break;
			case 12:
				titleListTwelve.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTwelve);
				break;
			case 13:
				titleListThirteen.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListThirteen);
				break;
			case 14:
				titleListFourteen.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListFourteen);
				break;
			case 15:
				titleListFifteen.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListFifteen);
				break;
			case 16:
				titleListSixteen.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListSixteen);
				break;
			case 17:
				titleListSeventeen.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListSeventeen);
				break;
			case 18:
				titleListEighteen.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListEighteen);
				break;
			case 19:
				titleListNineteen.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListNineteen);
				break;
			case 20:
				titleListTwenty.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTwenty);
				break;
			case 21:
				titleListTwentyOne.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTwentyOne);
				break;
			case 22:
				titleListTwentyTwo.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTwentyTwo);
				break;
			case 23:
				titleListTwentyThree.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTwentyThree);
				break;
			case 24:
				titleListTwentyFour.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTwentyFour);
				break;
			case 25:
				titleListTwentyFive.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTwentyFive);
				break;
			case 26:
				titleListTwentySix.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTwentySix);
				break;
			case 27:
				titleListTwentySeven.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTwentySeven);
				break;
			case 28:
				titleListTwentyEight.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListEight);
				break;
			case 29:
				titleListTwentyNine.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListTwentyNine);
				break;
			case 30:
				titleListThirty.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListThirty);
				break;
			case 31:
				titleListThirtyOne.addAll(calenderEventMapper.findTitle(selectedDay));
				titleLists.add(titleListThirtyOne);
				break;

			default:
			}

		}
		//最後の週
		for (int i = 0; i + weekIndex + totalDay <= 42; i++) {
			titleLists.add(new ArrayList<CalenderEvent>());
		}

		mav.addObject("titleLists", titleLists);

		return mav;
	}

	@GetMapping("/calender/event_details/{id}")
	public ModelAndView manage_details(@ModelAttribute CalenderEvent calenderEvent, @PathVariable int id,
			Principal principal, ModelAndView mav) {
		mav.setViewName("calender/event_details");

		Optional<CalenderEvent> eventDetails = calenderEventMapper.findId(id);

		if (principal != null) {

			 String join ="参加";

			 ParticipateEvent findResult = participateEventMapper.findJoin(id,principal.getName(),join);

			 mav.addObject("join",findResult);
			 mav.addObject("login","login");

			 List<ParticipateEvent> participateList = participateEventMapper.participateList(id);

			 mav.addObject("participateList",participateList);

			final String loginUser = principal.getName();
			final String user = eventDetails.get().getName();

			mav.addObject("loginUser",loginUser);

			if (loginUser.equals(user) == true) {
				mav.addObject("same", "same");
			}
		}

		mav.addObject("map","http://maps.google.co.jp/maps?&output=embed&q="+eventDetails.get().getPlace());

		mav.addObject("eventDetails", eventDetails.get());
		return mav;
	}

	@GetMapping("/calender/event_details_edit/{id}")
	public ModelAndView manage_details_editView(@ModelAttribute CalenderEvent calenderEvent, @PathVariable int id,
			ModelAndView mav) {
		mav.setViewName("calender/event_details_edit");
		Optional<CalenderEvent> eventDetails = calenderEventMapper.findId(id);
		mav.addObject("eventDetails", eventDetails);
		return mav;

	}

	@PostMapping("/calender/event_details_edit")
	@Transactional(readOnly = false)
	public ModelAndView manage_details_edit(@ModelAttribute("eventDetails") CalenderEvent calenderEvent,
			ModelAndView mav) {

		calenderEventMapper.update(calenderEvent);

		return new ModelAndView("redirect:/calender/event");
	}

	@GetMapping("/calender/event_details_delete/{id}")
	public ModelAndView manage_details_deleteView(@ModelAttribute CalenderEvent calenderEvent, @PathVariable int id,
			ModelAndView mav) {
		mav.setViewName("calender/event_details_delete");
		Optional<CalenderEvent> eventDetails = calenderEventMapper.findId(id);
		mav.addObject("eventDetails", eventDetails.get());
		return mav;
	}

	@PostMapping(value = "/calender/event_details_delete")
	@Transactional(readOnly = false)
	public ModelAndView manage_details_delete(@RequestParam int id) {

		calenderEventMapper.delete(id);
		participateEventMapper.deleteJoin(id);


		return new ModelAndView("redirect:/calender/event");
	}

}
