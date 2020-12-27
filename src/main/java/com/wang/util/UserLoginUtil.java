package com.wang.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.wang.pojo.User;



/* 用户登录相关的工具集
 * 
 */
public class UserLoginUtil
{
	// 登录以后，设置Session
	public static void login(HttpSession session, User user)throws Exception
	{		
		session.setAttribute("user", user);		
	}
	
	public static void logout(HttpSession session)
	{
		session.removeAttribute("user");
	}
			
	
	
	
}
