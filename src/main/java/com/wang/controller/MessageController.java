package com.wang.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.wang.config.springConfig.AfRestError;
import com.wang.util.MapUtil;
import com.wang.util.MyUtil;
import com.wang.util.TimeStrUtil;
import com.wang.util.TmpFileUtil;
import com.wang.config.springConfig.AfRestData;
import com.wang.dao.MessageMapper;
import com.wang.pojo.Message;
import com.wang.pojo.User;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttribute;



@Controller
public class  MessageController
{
	@Autowired
	private MessageMapper messageMapper;
	// 发表主贴
	@GetMapping("/u/message/add")
	public String add(Model model
			, @SessionAttribute User user	)throws Exception
	{

		return "message/add";
	}

	@PostMapping("u/message/save.do")
	public Object save(@RequestBody Message row
			, @SessionAttribute User user
			, HttpServletRequest request)
			throws Exception
	{
		// title, content字段由前端传送过来
		row.creator = user.id; // 创建者
		row.cat1 = row.cat2 = row.cat3 = 0;
		row.ref1 = 0L; // ref1=0表示这一条为主帖(楼主) 
		row.ref2 = 0L;		
		row.timeCreate = new Date(); // 发贴时间
		row.numReply = 0;
		row.numLike = 0;
		row.imgCount = 0;		
		row.niceFlag = 0;
		row.topFlag = 0;
		row.banFlag = false;
		row.delFlag = false;
		row.closeFlag = false;
		row.replyUser = user.id;
		row.replyTime = row.timeCreate;
		row.replyText = row.title;
		row.replyName = user.name;
	
		row.storePath = makeStorePath();

		// 图片处理
		row.img1 = moveTmpToStore(request, row.storePath, row.img1);
		row.img2 = moveTmpToStore(request, row.storePath, row.img2);
		row.img3 = moveTmpToStore(request, row.storePath, row.img3);
//		// 附件图片存储路径 示例 201911/01/15725791906031/
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/dd/");
//		row.storePath = sdf.format(new Date()) + MyUtil.guid2() + "/";

	try{
		int i=messageMapper.insertMessage(row);

	}catch (Exception e){
		System.out.println("添加帖子失败！");

	}
		

		// 扣除当天的额度
		return new AfRestData("");
	}
	// 将临时文件存储到Store，并返回存储路径 (此处只存储文件名)
		private String moveTmpToStore(HttpServletRequest request
				, String storePath,	String tmpName)
		{
			if(tmpName==null || tmpName.length()==0) return "";
			
			File tmpFile = TmpFileUtil.getFile(request, tmpName);
			File storeDir = MesgImgController.store.getFile(storePath);
			try
			{
				FileUtils.moveFileToDirectory(tmpFile, storeDir, true);
				//String path = storePath + tmpName;
				System.out.println("存储文件目录："+tmpName);
				return tmpName;
			} catch (IOException e)
			{
				throw new RuntimeException(e.getMessage());
			}		
		}
		
	// 不需要登录，即可浏览，所以不加 /u 前缀
		@GetMapping("/message/list")
		public String list(Model model
				, HttpSession session
				, Integer pageNumber
				) throws Exception
		{		
			if(pageNumber==null) pageNumber = 1;
			Map<String,Integer> row=new HashMap<>();
			row.put("ref1", 0);
			row.put("delFlag", 0);
			int count = 0;
			try{
                 count=messageMapper.selectMessageNum(row);
			}
			catch (Exception e){
				System.out.println("获取帖子数量失败");
			}

			
//			AfSqlWhere asw = new AfSqlWhere();
//			asw.add2("ref1", 0); //1楼，1级主帖子
//			asw.add2("delFlag", 0); // 没有删除标识的
			
			// count:符合条件的记录一共有多少条
//			int count = 0;
//			if(true) {
//				String s1 = "select count(id) from message " + asw;
//				String[] result = AfSimpleDB.get(s1);			
//				count = Integer.valueOf( result[0]);
//			}
			
			int pageSize = 10;
			int pageCount = count / pageSize;
			if(count % pageSize != 0) pageCount += 1;

			// 查询
			int startIndex = pageSize *(pageNumber-1);
//			String limit = String.format(" LIMIT %d,%d ", startIndex, pageSize);
//			String s2 = "select a.* ,b.name as userName,b.vipName "
//					+ " from message a JOIN user b "
//					+ " ON a.creator = b.id "
//					+ asw
//					+ " ORDER BY topFlag DESC, replyTime DESC "
//					+ limit;
//
//			
//			// 返回的每一行记录是一个Map<String,String>		
//			List<Map> messageList = AfSimpleDB.query(s2, 0);	
			row.put("startIndex",startIndex);
			row.put("pageSize",pageSize);
			 List<Map>  messageList=null;
			 try{
			 	messageList=messageMapper.selectMessageList(row);
			 }
			 catch (Exception e)
			 {
				 System.out.println("获取帖子列表失败");
			 }

		
			// 需要进一步处理处理成前端需要的格式
			TimeStrUtil tts = new TimeStrUtil();
			for(Map m : messageList)
			{			
				String timeCreate = m.get("timeCreate").toString();
				m.put("timeCreate", tts.format(timeCreate));
				
				String replyTime = m.get("replyTime").toString();
				m.put("replyTime", tts.format(replyTime));
				
				MapUtil mapu = new MapUtil(m);
				mapu.asInt("topFlag", 0);
				mapu.asInt("niceFlag", 0);
			}
			
			model.addAttribute("messageList", messageList);
			model.addAttribute("count", count); // 共习题数量
			model.addAttribute("pageCount", pageCount); // 总页数
			model.addAttribute("pageNumber", pageNumber); // 当前页码：1,2,3...
			model.addAttribute("baseIndex", count - pageSize * (pageNumber-1)); // 共习题数量
			
			
			return "message/list";
		}
		// 附件图片存储路径 示例 201911/01/15725791906031/
		public static String makeStorePath()
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/dd/");
			System.out.println("makeStorePath"+sdf.format(new Date()) + MyUtil.guid2() + "/");
			return sdf.format(new Date()) + MyUtil.guid2() + "/";
		}
	// 用户对帖子的操作
	@PostMapping("/u/message/userSetFlags.do")
	public Object userSetFlags(@SessionAttribute User user
			,@RequestBody JSONObject jreq)throws Exception
	{
		long msgId = jreq.getLongValue("msgId");
		String cmd = jreq.getString("cmd");
		if(cmd.equals("del"))
		{
			// 用户自己删贴
//			String s1 = "select id,creator from message where id=" + msgId;
//			Message ref = (Message)AfSimpleDB.get(s1,Message.class);
			Message ref=null;
			try {
				ref=messageMapper.selectMessage(msgId);
			}
			catch (Exception e){
				System.out.println("取出原帖出错");
			}
			if(ref.creator.intValue() != user.id) // Integer的比较用equals
			{
				// 不能删除别人的帖子
				return new AfRestError("无权删除别人的帖子");
			}

			// 删除此贴，以及它的子帖
//			String s2 = String.format(
//					"update message set delFlag=1 WHERE id=%d OR ref1=%d "
//					, msgId, msgId);
//			AfSimpleDB.execute(s2);
			try{
				int i=messageMapper.delMessage(msgId);
			}catch (Exception e)
			{
				System.out.println("删除帖子失败。");
			}
		}

		return new AfRestData("");
	}

}
