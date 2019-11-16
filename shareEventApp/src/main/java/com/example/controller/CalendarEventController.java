package com.example.controller;

import java.security.Principal;
import java.time.LocalDate;
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

import com.example.domain.CalendarEvent;
import com.example.domain.CalendarEvents;
import com.example.domain.ParticipateEvent;
import com.example.logic.CalendarLogic;
import com.example.logic.EventFormValidation;
import com.example.logic.FindLoginUser;
import com.example.mapper.CalendarEventMapper;
import com.example.mapper.ParticipateEventMapper;

@Controller
public class CalendarEventController {

	@Autowired
	CalendarEventMapper calendarEventMapper;

	@Autowired
	ParticipateEventMapper participateEventMapper;

	@GetMapping("/calendar/eventForm")
	public String eventFormView(Principal principal, @ModelAttribute("formModel") CalendarEvent calendarEvent,
			Model model) {

		FindLoginUser findLoginUser = new FindLoginUser();
		model.addAttribute("loginUser", findLoginUser.FindLoginUser(model, principal));

		return "calendar/eventForm";
	}

	@PostMapping("/calendar/eventForm")
	@Transactional(readOnly = false)
	public String eventFormCreate(Principal principal,
			@ModelAttribute("formModel") @Validated CalendarEvent calendarEvent,
			BindingResult result,
			Model model) {

		FindLoginUser findLoginUser = new FindLoginUser();

		model.addAttribute("loginUser", findLoginUser.FindLoginUser(model, principal));

		if (result.hasErrors()) {
			return "calendar/eventForm";
		}

		EventFormValidation efValidate = new EventFormValidation();

		if (efValidate.timeValidate(calendarEvent)) {
			model.addAttribute("error", "正しい時間を入力してください");
			return "calendar/eventForm";
		}

		if (efValidate.dayValidate(calendarEvent)) {
			model.addAttribute("next", "次の日以降を入力して下さい");
			return "calendar/eventForm";
		}

		if (!result.hasErrors()) {
			calendarEventMapper.save(calendarEvent);
			return "redirect:/calendar/event";
		}
		return "calendar/eventForm";
	}

	@RequestMapping("/calendar/event")
	public String event(HttpServletRequest request, HttpMethod httpMethod, Model model) {

		CalendarLogic calendarLogic = new CalendarLogic();
		//viewに表示するカレンダーの選択年月
		model.addAttribute("selectedcalendar", calendarLogic.selectedCalender(httpMethod,
				request.getParameter("selectedYear"), request.getParameter("selectedMonth")));
		model.addAttribute("selectYear", calendarLogic.selectYear());
		model.addAttribute("selectMonth", calendarLogic.selectMonth());

		int currentYear = calendarLogic.selectedYear(httpMethod, request.getParameter("selectedYear"));
		int currentMonth = calendarLogic.selectedMonth(httpMethod, request.getParameter("selectedMonth"));

		//表示する月の合計日数を調べる
		int totalDays = calendarLogic.totalDay(currentYear, currentMonth);
		//月の最初の曜日
		int weekIndex = calendarLogic.weekIndex(currentYear, currentMonth);

		model.addAttribute("weekIndex", weekIndex);
		//カレンダーに表示させる現在の月の日数
		model.addAttribute("currentDays", calendarLogic.currentDays(weekIndex, totalDays));

		CalendarEvents calendarEvents = new CalendarEvents();
		//最初の週
		for (int i = 0; i < weekIndex - 1; i++) {
			calendarEvents.setLists(new ArrayList<CalendarEvent>());
		}
		//2.3.4週
		for (int day = 1; day <= totalDays; day++) {
			calendarEvents.setLists(
					calendarEventMapper.findTitle(calendarLogic.selectedDays(currentYear, currentMonth, day)));
		}
		//最後の週
		for (int i = 0; i + weekIndex + totalDays <= 42; i++) {
			calendarEvents.setLists(new ArrayList<CalendarEvent>());
		}

		model.addAttribute("titleLists", calendarEvents.getTitleLists());

		return "calendar/event";
	}

	@GetMapping("/calendar/eventDetails/{id}")
	public ModelAndView EventDetailsView(@ModelAttribute CalendarEvent calendarEvent, @PathVariable int id,
			Principal principal, ModelAndView mav) {
		mav.setViewName("calendar/eventDetails");

		Optional<CalendarEvent> eventDetails = calendarEventMapper.findId(id);

		if (principal != null) {

			mav.addObject("findJoinResult", participateEventMapper.findJoin(id, principal.getName(), "参加"));
			mav.addObject("login", "login");
			mav.addObject("loginUser", principal.getName());

			if (principal.getName().equals(eventDetails.get().getName()) == true) {
				mav.addObject("sameUser", "sameUser");
			}
		}

		List<ParticipateEvent> participateList = participateEventMapper.participateList(id);

		mav.addObject("participateList", participateList);
		mav.addObject("map", "https://maps.google.co.jp/maps?&output=embed&q=" + eventDetails.get().getPlace());
		mav.addObject("eventDetails", eventDetails.get());

		return mav;
	}

	@GetMapping("/calendar/eventDetailsEdit/{id}")
	public ModelAndView eventDetailsEditView(@ModelAttribute CalendarEvent calendarEvent, @PathVariable int id,
			ModelAndView mav) {
		mav.setViewName("calendar/eventDetailsEdit");

		mav.addObject("eventDetails", calendarEventMapper.findId(id));

		return mav;
	}

	@PostMapping("/calendar/eventDetailsEdit")
	@Transactional(readOnly = false)
	public ModelAndView eventDetailsEdit(@ModelAttribute("eventDetails") @Validated CalendarEvent calendarEvent,
			BindingResult result, @ModelAttribute("id") int id, ModelAndView mav) {

		if (result.hasErrors()) {
			mav.setViewName("calendar/eventDetailsEdit");
			return mav;
		}

		EventFormValidation efValidate = new EventFormValidation();

		if (efValidate.timeValidate(calendarEvent)) {
			mav.addObject("error", "正しい時間を入力してください");
			return mav;
		}

		if (efValidate.dayValidate(calendarEvent)) {
			mav.addObject("next", "次の日以降を入力して下さい");
			return mav;
		}

		calendarEventMapper.update(calendarEvent);

		return new ModelAndView("redirect:/calendar/event");
	}

	@GetMapping("/calendar/eventDetailsDelete/{id}")
	public ModelAndView eventDetailsDeleteView(@ModelAttribute CalendarEvent calendarEvent, @PathVariable int id,
			ModelAndView mav) {
		mav.setViewName("calendar/eventDetailsDelete");

		Optional<CalendarEvent> eventDetails = calendarEventMapper.findId(id);
		mav.addObject("eventDetails", eventDetails.get());

		return mav;
	}

	@PostMapping(value = "/calendar/eventDetailsDelete")
	@Transactional(readOnly = false)
	public ModelAndView eventDetailsDelete(@RequestParam int id) {

		calendarEventMapper.delete(id);
		participateEventMapper.deleteJoin(id);

		return new ModelAndView("redirect:/calendar/event");
	}

	@GetMapping("/calendar/eventSoonSearch")
	@Transactional(readOnly = false)
	public ModelAndView eventSoonSearch(ModelAndView mav) {
		mav.setViewName("calendar/eventSoonSearch");

		mav.addObject("eventSoonList",calendarEventMapper.soonSearch(LocalDate.now()));
		mav.addObject("today",LocalDate.now());

		return mav;
	}


}