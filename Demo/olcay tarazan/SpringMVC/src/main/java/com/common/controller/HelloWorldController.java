package com.common.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.common.Customer;


@Controller
public class HelloWorldController {

	@Autowired
	Customer c;
	
	@RequestMapping(value = "**/request.json", method = RequestMethod.GET)		
	public ModelAndView home(HttpServletResponse response, HttpServletRequest request, Locale locale) {
		return null;
	}
	
	@RequestMapping(value = "**/welcome.html", method = RequestMethod.GET)		
	public ModelAndView json(HttpServletResponse response, HttpServletRequest request, Locale locale) {
		
		ModelAndView model = new ModelAndView("HelloWorldPage");
		System.out.println("Test-:"+c.getType());
		model.addObject("msg", c.getAction());
        model.addObject("gcd", "[['Year', 'Sales',],['2004', 1170],['2005', 1000]]");		

		return model;
	}
}