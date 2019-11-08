package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.domain.Menu;
import com.example.demo.mapper.MenuMapper;

@RestController
public class IndexController {

	@Autowired
	MenuMapper menuMapper;

	@GetMapping("/form/{id}")
	public Optional<Menu> form(@PathVariable int id) {
		Optional<Menu> list = menuMapper.findById(id);
		return list;
	}


	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@ModelAttribute("formModel") Menu menu,
			ModelAndView mav) {
		mav.setViewName("form");
		List<Menu> list = menuMapper.findAll();
		mav.addObject("menus", list);
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView formResult(@ModelAttribute("formModel") @Validated Menu menu,
			BindingResult result,
			ModelAndView mav) {
		if (!result.hasErrors()) {
			menuMapper.save(menu);
			return new ModelAndView("redirect:/form");
		}
		mav.setViewName("form");
		List<Menu> list = menuMapper.findAll();
		mav.addObject("menus", list);
		return mav;
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@ModelAttribute Menu menu,
			@PathVariable int id, ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title", "edit page");
		Optional<Menu> editUser = menuMapper.findById(id);
		mav.addObject("formModel", editUser.get());
		return mav;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView update(@ModelAttribute("formModel") Menu menu,
			ModelAndView mav) {
		menuMapper.update(menu);
		return new ModelAndView("redirect:/form");
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@ModelAttribute Menu menu,
			@PathVariable int id, ModelAndView mav) {
		mav.setViewName("delete");
		mav.addObject("title", "削除画面");
		Optional<Menu> deliteUser = menuMapper.findById(id);
		mav.addObject("formModel", deliteUser.get());
		return mav;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView remove(@RequestParam int id, ModelAndView mav) {
		menuMapper.delete(id);
		return new ModelAndView("redirect:/form");
	}

}