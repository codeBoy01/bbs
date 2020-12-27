package com.wang.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.wang.util.UserLoginUtil;
import com.wang.config.springConfig.AfRestData;
import com.wang.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wang.config.springConfig.AfRestError;
import com.wang.pojo.User;
import com.wang.util.MyUtil;

@Controller
public class LoginController
{
	@Autowired
	private UserMapper userMapper;
	@GetMapping("/register")
	public String register(Model model)
	{		
		return "user/register";
	}
	
	@PostMapping("/register.do")
	public Object register(HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, @RequestBody User user
			) throws Exception
	{
		user.name = user.name.trim();
		user.password = user.password.trim();
		if(user.password.length() > 0)
		{
			// 数据库里存储密码的MD5值, 参考《项目应用篇》之数据的校验
			user.password = MyUtil.md5( user.password);
		}
		
		// 注意： 在数据库 user.`name`字段被设置为唯一索引Unique Index
		//第一步，获取SqlSession对象

		try {
			int i =userMapper.addUser(user);
            System.out.println("添加用户成功");
		}catch (Exception e){
		    return new AfRestError("用户名已被占用!");
		}
		UserLoginUtil.login(session, user);
		return new AfRestData("");
	}
    @GetMapping("/login")
    public String login(Model model,String returnUrl)
    {
        if(returnUrl==null) returnUrl="";
        model.addAttribute("returnUrl",returnUrl);
        return "user/login";
    }

    @PostMapping("/login.do")
    public Object login(HttpServletRequest request
            , HttpServletResponse response
            , HttpSession session
            , @RequestBody JSONObject jreq
    ) throws Exception
    {
        String name = jreq.getString("name");
        String password = jreq.getString("password");
        User user=null;
        user=userMapper.selectUser(name);

        if(user == null)
            return new AfRestError("用户名不存在");

        String passmd5= MyUtil.md5(password);
        if(!user.password.equals(passmd5))
        {
            return new AfRestError("用户名密码不匹配");
        }

        // 设置当前会话
        UserLoginUtil.login(session, user);

        return new AfRestData("");
    }
	@GetMapping("/logout")
	public String logout(HttpSession session
			, HttpServletRequest request
			, HttpServletResponse response) throws Exception
	{
		UserLoginUtil.logout(session);

		//return "redirect:/";
		return "redirect:/message/list";
	}
	
	
}
