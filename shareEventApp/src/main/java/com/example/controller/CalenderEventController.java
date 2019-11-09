package com.example.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
import com.example.domain.CalenderEvents;
import com.example.domain.ParticipateEvent;
import com.example.domain.Week;
import com.example.logic.CalenderLogic;
import com.example.logic.Event_formValidate;
import com.example.logic.FindLoginUserLogic;
import com.example.mapper.CalenderEventMapper;
import com.example.mapper.ParticipateEventMapper;

@Controller
public class CalenderEventController {

	@Autowired
	CalenderEventMapper calenderEventMapper;

	@Autowired
	ParticipateEventMapper participateEventMapper;

	@GetMapping("/calender/event_form")
	public String event_form(Principal principal, @ModelAttribute("formModel") CalenderEvent calenderEvent,
			Model model) {

		FindLoginUserLogic findLoginUserLogic = new FindLoginUserLogic();

		String loginUser = findLoginUserLogic.FindLoginUser(model, principal);

		model.addAttribute("loginUser", loginUser);

		return "calender/event_form";

	}

	@PostMapping("/calender/event_form")
	@Transactional(readOnly = false)
	public String event_form_Result(Principal principal,
			@ModelAttribute("formModel") @Validated CalenderEvent calenderEvent,
			BindingResult result,
			Model model) {

		FindLoginUserLogic findLoginUser = new FindLoginUserLogic();

		String loginUser = findLoginUser.FindLoginUser(model, principal);

		model.addAttribute("loginUser", loginUser);

		if (result.hasErrors()) {

			return "calender/event_form";

		}

		Event_formValidate event_formValidate = new Event_formValidate();

		boolean timeError = event_formValidate.timeValidate(calenderEvent);

		if(timeError == true) {

			model.addAttribute("error","正しい時間を入力してください");

			return "calender/event_form";

		}

		boolean dayError = event_formValidate.dayValidate(calenderEvent);

		if(dayError == true) {

			model.addAttribute("next","次の日以降を入力して下さい");

			return "calender/event_form";
		}

		if (!result.hasErrors()) {

			calenderEventMapper.save(calenderEvent);

			return "redirect:/calender/event";
		}

		return "calender/event_form";
	}

	@RequestMapping("/calender/event")
	public String manage(HttpServletRequest request, HttpMethod httpMethod, Model model) {

		String selectedYear = request.getParameter("selectedYear");
		String selectedMonth = request.getParameter("selectedMonth");

		CalenderLogic calenderLogic = new CalenderLogic();
		//viewに表示するカレンダーの選択年月
		String selectedCalender = calenderLogic.selectedCalender(httpMethod, selectedYear, selectedMonth);

		model.addAttribute("selectedCalender", selectedCalender);

		model.addAttribute("selectYear",calenderLogic.selectYear());

		model.addAttribute("selectMonth",calenderLogic.selectMonth());

		int currentYear = calenderLogic.selectedYear(httpMethod, selectedYear);

		int currentMonth = calenderLogic.selectedMonth(httpMethod, selectedMonth);

		//表示する月の合計日数を調べる
		int totalDay = calenderLogic.totalDay(currentYear, currentMonth);
		//月の最初の曜日
		int weekIndex = calenderLogic.weekIndex(currentYear, currentMonth);

		model.addAttribute("weekIndex", weekIndex);
		//カレンダーに表示させる日付
		ArrayList<Week> calenderDays = calenderLogic.calenderDays(weekIndex, totalDay);

		model.addAttribute("calenderDays", calenderDays);

		CalenderEvents calenderEvents = new CalenderEvents();

		for (int day = 1, a = 0; day <= totalDay; day++) {

			//日付をyyyyMMDDに変更
			LocalDate c = LocalDate.of(currentYear, currentMonth, day);
			DateTimeFormatter da = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String selectedDay = c.format(da);
			//最初の週
			while (a < 1) {
				for (int i = 0; i < weekIndex - 1; i++) {
					calenderEvents.setLists(new ArrayList<CalenderEvent>());
					a++;
				}
				a++;
			}

				calenderEvents.setLists(calenderEventMapper.findTitle(selectedDay));

		}
		//最後の週
		for (int i = 0; i + weekIndex + totalDay <= 42; i++) {
			calenderEvents.setLists(new ArrayList<CalenderEvent>());
		}

		model.addAttribute("titleLists", calenderEvents.getTitleLists());

		return "calender/event";

	}

	@GetMapping("/calender/event_details/{id}")
	public ModelAndView manage_details(@ModelAttribute CalenderEvent calenderEvent, @PathVariable int id,
			Principal principal, ModelAndView mav) {
		mav.setViewName("calender/event_details");

		Optional<CalenderEvent> eventDetails = calenderEventMapper.findId(id);

		if (principal != null) {

			ParticipateEvent findJoinResult = participateEventMapper.findJoin(id, principal.getName(), "参加");

			mav.addObject("findJoinResult", findJoinResult);

			mav.addObject("login", "login");

			mav.addObject("loginUser", principal.getName());

			if (principal.getName().equals(eventDetails.get().getName()) == true) {
				mav.addObject("sameUser", "sameUser");
			}
		}

		List<ParticipateEvent> participateList = participateEventMapper.participateList(id);

		mav.addObject("participateList", participateList);

		mav.addObject("map", "http://maps.google.co.jp/maps?&output=embed&q=" + eventDetails.get().getPlace());

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
