package com.wang.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class Message
{ 
 
	public Long id ; 
	public Integer creator ; 
	public String title ; 
	public String content ; 
	public Integer cat1 ; 
	public Integer cat2 ; 
	public Integer cat3 ; 
	public Long ref1 ; 
	public Long ref2 ; 
	public String refstr ; 
	public Date timeCreate ; 
	public Date timeUpdate ; 
	public Integer niceFlag ; 
	public Integer topFlag ; 
	public Boolean banFlag ; 
	public Boolean delFlag ; 
	public Boolean closeFlag ; 
	public Integer numReply ; 
	public Integer numLike ; 
	public String storePath ; 
	public Integer imgCount ; 
	public String img1 ; 
	public String img2 ; 
	public String img3 ; 
	public Integer replyUser ; 
	public String replyName ; 
	public Date replyTime ; 
	public String replyText ; 


} 
 