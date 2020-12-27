package com.wang.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import com.wang.pojo.User;

/**
 *
 */

public class UserLoginFilter implements Filter
{
	@Override
	public void init(FilterConfig filterCfg) throws ServletException
	{		
	}
	
	@Override
	public void destroy()
	{
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, 
			FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		String uri = request.getRequestURI();
			
		// 检查用户登录
		if(true)
		{
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			if(user== null)
			{					
				// 用户尚未登录
				if(uri.endsWith(".do"))
				{
					// REST请求：返回-100错误
					JSONObject json = new JSONObject(true);
					json.put("error", -100);
					json.put("reason","尚未登录!");
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json");
					response.getWriter().write(json.toJSONString());
					return;
				}
				else
				{
					// ?号后面的部分也得带上
					String query = request.getQueryString();
					if(query!=null && query.length()>0)
						uri += "?" + query;
									
					//MVC请求： 返回302重定向
					response.sendRedirect(request.getContextPath()
							+ "/login"
							+ "?returnUrl=" + uri
							);
					return;
				}
				
			}
		}
		
		chain.doFilter(req, resp);		
	}


}
