package com.sooRyeo.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {

	@RequestMapping(value = "/admin/dashboard", method = RequestMethod.GET)
	public String admin_Main() {

		return "admin_Main.admin";
	}
	
	
	@RequestMapping(value = "/admin/MemberCheck.lms", method = RequestMethod.GET)
	public String MemberCheck() {
		
		return "MemberCheck.admin";
	}
	
}