package com.wang.filter;

import com.wang.task.DailyTaskManager;
import com.wang.task.MsgDelTask;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/** 系统启动初始化
 * 
 *  此 Filter不执行过滤操作，仅为初始化一些全局实例
 *  
 */

@WebFilter()
public class BootFilter implements Filter
{
	DailyTaskManager daily = new DailyTaskManager();
	
	@Override
	public void init(FilterConfig filterCfg) throws ServletException
	{	
		daily.addTask(new MsgDelTask(), "清理message表");
		daily.doStart();
	}
	
	@Override
	public void destroy()
	{
		daily.doQuit();
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, 
			FilterChain chain) throws IOException, ServletException
	{
		chain.doFilter(req, resp);
	}
}
