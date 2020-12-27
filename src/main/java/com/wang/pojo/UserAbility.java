package com.wang.pojo;
import java.util.Date;
public class UserAbility 
{ 
 
	public Integer userId ; 
	public Integer banFlag ; 
	public Date banDate ; 
	public Integer imageCount ; 
	public Integer imageMax ; 
	public Integer msgCount ; 
	public Integer msgMax ; 
	public Integer replyCount ; 
	public Integer replyMax ; 


	public void setUserId(Integer userId)
	{
		this.userId=userId;
	}
	public Integer getUserId()
	{
		return this.userId;
	}
	public void setBanFlag(Integer banFlag)
	{
		this.banFlag=banFlag;
	}
	public Integer getBanFlag()
	{
		return this.banFlag;
	}
	public void setBanDate(Date banDate)
	{
		this.banDate=banDate;
	}
	public Date getBanDate()
	{
		return this.banDate;
	}
	public void setImageCount(Integer imageCount)
	{
		this.imageCount=imageCount;
	}
	public Integer getImageCount()
	{
		return this.imageCount;
	}
	public void setImageMax(Integer imageMax)
	{
		this.imageMax=imageMax;
	}
	public Integer getImageMax()
	{
		return this.imageMax;
	}
	public void setMsgCount(Integer msgCount)
	{
		this.msgCount=msgCount;
	}
	public Integer getMsgCount()
	{
		return this.msgCount;
	}
	public void setMsgMax(Integer msgMax)
	{
		this.msgMax=msgMax;
	}
	public Integer getMsgMax()
	{
		return this.msgMax;
	}
	public void setReplyCount(Integer replyCount)
	{
		this.replyCount=replyCount;
	}
	public Integer getReplyCount()
	{
		return this.replyCount;
	}
	public void setReplyMax(Integer replyMax)
	{
		this.replyMax=replyMax;
	}
	public Integer getReplyMax()
	{
		return this.replyMax;
	}

} 
 