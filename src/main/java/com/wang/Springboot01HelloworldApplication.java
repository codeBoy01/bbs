package com.wang;

import com.wang.filter.BootFilter;
import com.wang.filter.UserLoginFilter;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.wang.dao")
public class Springboot01HelloworldApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot01HelloworldApplication.class, args);
	}



	@Bean
	public FilterRegistrationBean registFilter(){
		FilterRegistrationBean bean = new FilterRegistrationBean();
		// 定义filter的过滤路径规则。
		bean.addUrlPatterns("/u/*");
		bean.setFilter(new UserLoginFilter());
		return bean;
	}
	@Bean
	public FilterRegistrationBean delMessageFilter() {

		FilterRegistrationBean bean = new FilterRegistrationBean();
		// 定义filter的过滤路径规则。
		bean.addUrlPatterns("/*");
		bean.setFilter(new BootFilter());
		return bean;
	}



}
