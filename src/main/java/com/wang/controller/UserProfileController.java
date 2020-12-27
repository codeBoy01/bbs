package com.wang.controller;

import com.wang.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;


@Controller
@RequestMapping("/u/profile")
public class UserProfileController
{
	@GetMapping("/edit")
	public String edit(@SessionAttribute User user
			, Model model) throws Exception
	{
		model.addAttribute("user", user);
		
		return "user/profile";
	}
}
