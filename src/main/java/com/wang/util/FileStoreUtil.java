package com.wang.util;

import java.io.File;

/** 文件的本地存储与访问，path和url的换算
 * 
 */
public class FileStoreUtil
{
	public File rootDir = new File("c:/bbsfile/message/");	
	public String urlPrefix = "/bbsfile/message";
	
	public FileStoreUtil(String dir, String urlPrefix)
	{	
		this(new File(dir), urlPrefix);
	}

	public FileStoreUtil(File dir, String urlPrefix)
	{	
		this.rootDir = dir;
		this.urlPrefix = urlPrefix;
		this.rootDir.mkdirs();
	}
	
	// 传入相对路径path，返回File
	public File getFile(String path)
	{		
		rootDir.mkdirs();
		return new File(rootDir, path);
	}
	
	// 传入相对路径path，返回URL
	public String getUrl(String path)
	{
		return urlPrefix + path;
	}
	
	// 根据URL，找到相对路径
	public String pathOfUrl(String url)
	{
		return url.substring(urlPrefix.length());
	}
	
	// 根据URL，找到相应的文件
	public File fileOfUrl(String url)
	{
		return new File(rootDir, pathOfUrl(url));
	}

}
