package com.cricketkorner.cricket;
 
public class  ChannelModel {
	
	String		channelId		=	""; 
	String 		name			=	"";
	public String path			=	"";
	public String quizType		=	"";
	String flag 				=	"";

	public ChannelModel(){
		 
	} 
	
	public String getChannelId(){ 
		return channelId;
	}
	public String getChannelName(){ 
		return name;
	}
	
	public String getChannelPath(){
		return this.path;
	} 
	public void setChannelId(String string){ 
		this.channelId = string;
	}  
	
	public void setChannelName(String type){ 
		this.name = type;
	}
	public void setChannelPath(String str){
		this.path = str;
	}  
}
