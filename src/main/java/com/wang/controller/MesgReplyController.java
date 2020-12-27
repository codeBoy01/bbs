package com.wang.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wang.util.MapUtil;
import com.wang.config.springConfig.AfRestData;
import com.wang.config.springConfig.AfRestError;
import com.wang.dao.MessageMapper;
import com.wang.pojo.Message;
import com.wang.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.alibaba.fastjson.JSONObject;



@Controller
public class MesgReplyController
{
	@Autowired
	private MessageMapper messageMapper;
	// 发表主贴
	@GetMapping("/u/reply/add")
	public String add(Model model
			, @SessionAttribute User user
			, long msgId
			) throws Exception
	{
		
		Message ref=null;
		try{
			ref=messageMapper.selectMessage(msgId);}
		catch (Exception e){
			System.out.println("打开帖子失败");
		}
//		try (SqlSession sqlsession = Mybatis.factory.openSession()) {
//
//
//            ref= sqlsession.selectOne("af.test.selectMessage",msgId);
//
//	}
		// 取出原贴
//		String s1 = "select * from message where id=" + msgId;
//		Message ref = (Message)AfSimpleDB.get(s1, Message.class);
		model.addAttribute("ref", ref);
		if(ref == null)
			throw new Exception("无此记录, messageId=" + msgId);
		
		return "reply/add";
	}

	@PostMapping("/u/reply/save.do")
	public Object save(@RequestBody JSONObject jreq
			, @SessionAttribute User user
			) throws Exception
	{		
		// 取出原帖子（此处可以优化，没必要取出所有字段)
		long msgId = jreq.getLongValue("msgId");
//		String s1 = "select * from message where id=" + msgId;
//		Message ref = (Message)AfSimpleDB.get(s1, Message.class);

		Message ref=null;
		try{
			ref=messageMapper.selectMessage(msgId);
		}catch (Exception e){
			System.out.println("取出帖子失败");
		}

//		try (SqlSession sqlsession = Mybatis.factory.openSession()) {
//
//
//            ref= sqlsession.selectOne("af.test.selectMessage",msgId);
//
//	}
		if(ref == null)
			return new AfRestError("无此记录, messageId=" + msgId);
		
		// 创建一条回复记录
		Message row = new Message();
		row.creator = user.id; // 创建者
		row.cat1 = ref.cat1;
		row.cat2 = ref.cat2;
		row.cat3 = ref.cat3;
		row.title = jreq.getString("title"); // title即缩略显示
		row.content = jreq.getString("content"); // 回复内容
		row.ref1= msgId; // 父级ID (即1楼原文ID)
		row.ref2=0L;
		row.timeCreate=new Date();
		row.numReply=0;
		row.numLike =0;
		row.imgCount = 0;
		row.niceFlag = 0;
		row.topFlag = 0;
		row.banFlag = false;
		row.delFlag = false;
		row.closeFlag = false;
		row.timeCreate = new Date();		
		row.storePath = MessageController.makeStorePath();
		System.out.println(row.storePath);
		
		if(row.img1==null) row.img1="";
		if(row.img2==null) row.img2="";
		if(row.img3==null) row.img3="";
		
//		AfSimpleDB.insert( row);
		try{
			int i=messageMapper.insertMessage(row);
		}catch (Exception e){
			System.out.println("");
		}
//		try (SqlSession sqlsession = Mybatis.factory.openSession()) {
//			   sqlsession.insert("af.test.insertMessage",row);
//
//				sqlsession.commit(); // 因为默认开启了事务，所以要commit()一下
//
//		}
 		// 更新统计数据
		if(true)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			AfSqlWhere w3 = new AfSqlWhere();
			HashMap<String,Object>w3=new HashMap<String, Object>();
		
		
			w3.put("id", msgId);
			// 回复数+1，并且记录最后一次回复数据
//			AfSqlUpdate u3 = new AfSqlUpdate("message");
			
			w3.put("replyUser", row.creator);
			w3.put("replyText", row.title);		
			w3.put("replyName", user.name);
			w3.put("replyTime", sdf.format(new Date()));
			 try{
			 	int j=messageMapper.updateMessage(w3);
			 }catch (Exception e){
				 System.out.println("更新失败");
			 }
//			try (SqlSession sqlsession = Mybatis.factory.openSession()) {
//				   sqlsession.update("af.test.updateMessage",w3);
//
//					sqlsession.commit(); // 因为默认开启了事务，所以要commit()一下
//
//			}
//			String s3 = u3 + "" + w3;
//			//System.out.println("更新统计 : " + s3);
//			AfSimpleDB.execute( s3 );
		}
		
		return new AfRestData("");
	}
	// 不需要登录，即可浏览，所以不加 /u 前缀
		@GetMapping("reply/list")
		public String list(Model model
				, @RequestParam("msgId") Integer msgId
				, Integer pageNumber
				) throws Exception
		{		
			if(pageNumber==null) pageNumber = 1;
			
			// 取出原文
//			String s1 = " select a.*, b.name as userName "
//					+ " from message a, user b "
//					+ " where a.creator=b.id and a.id="+ msgId;
//			
//			Map<String,Object> ref = AfSimpleDB.get(s1, 0);
			Map<String,Object> ref =null;
			try{
				ref=messageMapper.selectMessageMap(msgId);
			}
			catch (Exception e){
				System.out.println("取出原文失败");
			}


//			try (SqlSession sqlsession = Mybatis.factory.openSession()) {
//
//
//	            ref= sqlsession.selectOne("selectMessageMap",msgId);
//
//		}
//
			if(ref == null)
				throw new Exception("无此记录 ，messageId=" + msgId);
			else 
			{			
				MapUtil mapu = new MapUtil(ref);
				mapu.asInt("creator", 0);	
				// 将img1,img2,img3转成可以显示的URL
				handleImageUrl(ref, "img1");
				handleImageUrl(ref, "img2");
				handleImageUrl(ref, "img3");
				

				model.addAttribute("ref", ref);
			}		
			
//			AfSqlWhere asw = new AfSqlWhere();
			HashMap<String,Integer> asw=new HashMap<>();
			
			asw.put("ref1", msgId);
			asw.put("delFlag", 0); // 没有删除标识的
			
			// 查询总记录数
			int count = 0;
			if(true) {
//				String s2 = "select count(id) from message " + asw;
//				String[] result = AfSimpleDB.get(s2);			
//				count = Integer.valueOf( result[0]);
				try{
					count=messageMapper.selectMessageNum(asw);
				}
				catch (Exception e){
					System.out.println("查询帖子总数量");
				}
//				try (SqlSession sqlsession = Mybatis.factory.openSession()) {
//
//					  count= sqlsession.selectOne("af.test.selectMessageNum", asw);
//
//				}
			}
			int pageSize = 10;
			int pageCount = count / pageSize;
			if(count % pageSize != 0) pageCount += 1;

			// 查询
			int startIndex = pageSize *(pageNumber-1);
//			String limit = String.format(" LIMIT %d,%d ", startIndex, pageSize);
//			String s3 = "select a.* ,b.name as userName,b.vipName "
//					+ " from message a JOIN user b "
//					+ " ON a.creator = b.id "
//					+ asw
//					+ " order by id asc "
//					+ limit;

			// 查询
//			List<Map> messageList = AfSimpleDB.query(s3, 0);
			asw.put("startIndex",startIndex);
			asw.put("pageSize",pageSize);
			 List<Map>  messageList=null;
			 try{
				 messageList=messageMapper.selectMessageReplyList(asw);
			 }
			 catch (Exception e){
				 System.out.println("获取回帖列表失败");
			 }

//			try (SqlSession sqlsession = Mybatis.factory.openSession()) {
//
//
//                     messageList= sqlsession.selectList("selectMessageReplyList", asw);
//
//			}
//			// 需要进一步处理处理成前端需要的格式
//			TimeStrUtil tts = new TimeStrUtil();
//			for(Map m : messageList)
//			{			
//				String timeCreate = m.get("timeCreate").toString();
//				m.put("timeCreate", tts.format(timeCreate));
//				
//				String replyTime = m.get("replyTime").toString();
//				m.put("replyTime", tts.format(replyTime));
//				
//				MapUtil mapu = new MapUtil(m);
//				mapu.asInt("topFlag", 0);
//				mapu.asInt("niceFlag", 0);
//			}
			
			model.addAttribute("messageList", messageList);
			model.addAttribute("count", count); // 共习题数量
			model.addAttribute("pageCount", pageCount); // 总页数
			model.addAttribute("pageNumber", pageNumber); // 当前页码：1,2,3...
			model.addAttribute("baseIndex", pageSize * (pageNumber-1)); // 共习题数量
			
			
			return "reply/list";
		}
		// 处理 'img1', 'img2', 'img3'字段，转成显示用的URL
		public void handleImageUrl(Map msg, String column)
		{
			String url = "";
			String imgName = (String)msg.get(column);
			if(imgName != null && imgName.length() >0)
			{
				String storePath = (String) msg.get("storePath");
				String path = storePath + imgName;	
				url = MesgImgController.store.getUrl(path);
			}		
			System.out.println(url);
			msg.put(column, url);
		}
	
}
