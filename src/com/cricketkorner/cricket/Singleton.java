package com.cricketkorner.cricket;
 
import java.util.Vector;
 
public class  Singleton {
 	 
	public Vector<ChannelModel> channelModel; 
	 
	private static Singleton singleton;
	
	public static Singleton getInstance(){
		if(singleton == null){
			singleton = new Singleton();
		}
		return singleton;
	} 
	
	private  Singleton(){
		 
		channelModel				=	new Vector<ChannelModel>(); 
	}
	 

	public void addChannelModelObject(ChannelModel quizResultModelObj) {
		 channelModel.add(quizResultModelObj);
	}
	public Vector<ChannelModel> getChannelList() {
		 return channelModel;
	}
}
