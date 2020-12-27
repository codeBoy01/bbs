package com.wang.task;

import java.io.File;
import java.util.List;

import com.wang.controller.MesgImgController;
import com.wang.pojo.Message;
import org.apache.commons.io.FileUtils;

import af.sql.c3p0.AfSimpleDB;


/** 清理 message.delFlag=1 的记录
 * 
 *
 */
public class MsgDelTask implements Task
{

	@Override
	public void execute() throws Exception
	{
		System.out.println("** 清理帖子(delFlag=1) ...");
		String s1 = "select id, ref1, storePath from message "
				+ " where delFlag=1 LIMIT 5000 ";
		
		List<Message> msgList = AfSimpleDB.query(s1, Message.class);
		for(Message msg : msgList)
		{
			try{
				clearMessage( msg );
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}		
	}
	
	private void clearMessage(Message msg) throws Exception
	{
		// 删除附件照片目录
		File storeDir = MesgImgController.store.getFile(msg.storePath);
		if(storeDir.exists()) 
		{
			try {
				FileUtils.deleteQuietly(storeDir);
			}catch(Exception e)
			{				
			}
		}
		
		// 删除数据库记录
		String s1 = "delete from message where id=" + msg.id;
		AfSimpleDB.execute(s1);		
	}
	
}
