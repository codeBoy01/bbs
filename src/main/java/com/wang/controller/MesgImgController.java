package com.wang.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wang.util.FileStoreUtil;
import com.wang.util.MyUtil;
import com.wang.util.TmpFileUtil;
import com.wang.config.springConfig.AfRestData;
import com.wang.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Controller
public class MesgImgController
{	
	public static FileStoreUtil store
	= new FileStoreUtil("c:/bbsfile/message/", "/bbsfile/message/");
	@PostMapping(value = "/u/message/imageUp.do")
	public Object upload(HttpServletRequest request
			, @SessionAttribute User user) throws Exception
	{
		MultipartHttpServletRequest mhr = (MultipartHttpServletRequest) request;
		
		// 从表单里获取其他参数
//		String argValue = mhr.getParameter("your_arg_name"); 
		
		MultipartFile mf = mhr.getFile("file"); // 表单里的 name='file'
		Map<String,Object> result = new HashMap<>();
		
		if(mf != null && !mf.isEmpty())
		{					
			String realName = mf.getOriginalFilename();
			String suffix = MyUtil.getSuffix(realName);
			String tmpName = MyUtil.guid2() + suffix;
			
			File tmpFile = TmpFileUtil.getFile(request, tmpName);
			
			// 接收上传 ...
			mf.transferTo(tmpFile);
			System.out.println("** file: " + tmpFile.getAbsolutePath());
			
			if(tmpFile.length()>1000000)
				throw new Exception("图片太大!需小于1M!");
			
			// 回应给客户端的消息
			result.put("realName", realName);
			result.put("tmpName", tmpName);
			result.put("tmpUrl", TmpFileUtil.getUrl(request, tmpName));
			
		}				
		
		return new AfRestData(result);
	}


}
