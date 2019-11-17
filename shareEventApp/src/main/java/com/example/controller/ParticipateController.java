package com.example.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.CalendarEvent;
import com.example.mapper.CalendarEventMapper;
import com.example.mapper.ParticipateEventMapper;

@Controller
public class ParticipateController {

	@Autowired
	CalendarEventMapper calenderEventMapper;

	@Autowired
	ParticipateEventMapper participateEventMapper;

	@Transactional
	@PostMapping("/calendar/eventDetailsJoin")
	public ModelAndView join(@ModelAttribute("username") String username,
			@ModelAttribute("id") int id, Principal principal, ModelAndView mav) {
		mav.setViewName("calendar/eventDetails");

		participateEventMapper.join(id, username, "参加");

		mav.addObject("findJoinResult", participateEventMapper.findJoin(id, username, "参加"));

		Optional<CalendarEvent> eventDetails = calenderEventMapper.findId(id);

		if (principal != null) {

			mav.addObject("login", "login");
			mav.addObject("loginUser", principal.getName());

			if (principal.getName().equals(eventDetails.get().getName()) == true) {
				mav.addObject("sameUser", "sameUser");
			}
		}

		mav.addObject("participateList", participateEventMapper.participateList(id));
		mav.addObject("eventDetails", eventDetails.get());
		mav.addObject("map", "https://maps.google.co.jp/maps?&output=embed&q=" + eventDetails.get().getPlace());

		return mav;
	}

	@Transactional
	@PostMapping("/calendar/eventDetailsUnjoin")
	public ModelAndView unjoin(@ModelAttribute(name = "username") String username,
			@ModelAttribute("id") int id, Principal principal, ModelAndView mav) {
		mav.setViewName("calendar/eventDetails");

		participateEventMapper.unjoin(username);

		Optional<CalendarEvent> eventDetails = calenderEventMapper.findId(id);

		if (principal != null) {

			mav.addObject("login", "login");
			mav.addObject("loginUser", principal.getName());

			if (principal.getName().equals(eventDetails.get().getName()) == true) {
				mav.addObject("sameUser", "sameUser");
			}
		}

		mav.addObject("participateList", participateEventMapper.participateList(id));
		mav.addObject("eventDetails", eventDetails.get());
		mav.addObject("map", "https://maps.google.co.jp/maps?&output=embed&q=" + eventDetails.get().getPlace());

		return mav;
	}

}
