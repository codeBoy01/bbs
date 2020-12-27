package com.wang.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.wang.config.springConfig.AfRestData;
import com.wang.dao.UserMapper;
import com.wang.pojo.User;
import com.wang.util.FileStoreUtil;
import com.wang.util.MyUtil;
import com.wang.util.TmpFileUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


/** 负责用户头像的上传
 * 
 *
 */

@Controller
public class UserPhotoController
{
	@Autowired
	private UserMapper userMapper;
	public static FileStoreUtil store
		= new FileStoreUtil("c:/bbsfile/photo/", "/bbsfile/photo/");
	
	public UserPhotoController(ServletContext servletContext)
	{
	}
		
	@PostMapping(value = "/u/profile/setPhoto.do")
	public Object setPhoto(HttpServletRequest request
			, @SessionAttribute User user


			) throws Exception
	{		
		MultipartHttpServletRequest mhr = (MultipartHttpServletRequest) request;
		
		// 获取表单参数
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
			
			if(tmpFile.length()> 500* 1000)
				throw new Exception("图片太大!需小于500KB!");
			
			// 头像的正式URL
			String url = usePhoto(user, tmpFile);
			
			// 回应给客户端的消息
			result.put("realName", realName);
			result.put("tmpName", tmpName);
			result.put("tmpUrl", TmpFileUtil.getUrl(request, tmpName));
			result.put("url", url);
		}				
		
		return new AfRestData(result);
	}
		
	private String usePhoto(User user, File tmpImage) throws Exception
	{
		// 示例： 000/1_15728601766396.jpg
		String path = String.format("%03d/%d_%s.jpg", 
				user.id/1000, 
				user.id,
				MyUtil.guid2()
				);
		
		// 先删除旧的照片
		if(user.thumb!=null && user.thumb.length()>0)
		{
			File oldFile = store.fileOfUrl(user.thumb);
			try{
				FileUtils.deleteQuietly(oldFile);			
			}catch(Exception e)
			{
				System.out.println("** 不能删除旧的头像: " + oldFile);
			}
		}
		
		// 保存新的头像图片
		File dstFile = store.getFile(path);
		dstFile.getParentFile().mkdirs(); // 创建层次目录		
		//FileUtils.moveFile(tmpImage, dstFile); // 转移图片
		clipPhoto(tmpImage,dstFile);//裁剪成正方形
		// 头像图片的正式URL
		String url = store.getUrl(path);
		user.thumb = url;
		
//		// 记录到 user 表
//		String sql = String.format(
//				"update user set thumb='%s' where id='%d'"
//				, url
//				, user.id
//				);
//		AfSimpleDB.execute( sql);
		HashMap<String,Object> aws=new HashMap<>();
		aws.put("url",url);
		aws.put("id",user.id);
		int n=userMapper.updateUserImage(aws);

		
		return url;
	}
	private void clipPhoto(File srcFile, File dstFile) throws Exception
	{
		// 读取源图，缩小为100x100 (最大尺寸)
		BufferedImage img = Thumbnails.of(srcFile)
				.size(100,100)
				.asBufferedImage();

		// 截取中间的正方形
		int w = img.getWidth();
		int h = img.getHeight();
		int size = w < h ? w : h; // 取最小值
		int x = (w-size)/2;
		int y = (h-size)/2;

		// 截取
		Thumbnails.of(img)
				.scale(1.0f)
				.sourceRegion(x,y,size,size)
				.outputFormat("jpg")
				.toFile( dstFile );
	}



}
