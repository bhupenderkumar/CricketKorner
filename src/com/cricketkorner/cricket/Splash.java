package com.cricketkorner.cricket;
  
  
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class Splash extends Activity{

	String[] phoneNumberList;
	Button about;
	Button sms;  
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		 
        
        final int sleeptimer = 1800;
        
        Thread  myThread = new Thread()
        {
        	@Override
			public void run()
        	{ 
    			int currentwait = 0;
    			while(currentwait < sleeptimer)
				{
					try {
						sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					currentwait+=200;
					if(currentwait == sleeptimer){ 
						 Toast.makeText(getApplicationContext(),UserEmailFetcher.getEmail(getApplicationContext()),Toast.LENGTH_SHORT).show();
						 Intent main = new Intent(Splash.this , VitamioListActivity.class); 
	        			 startActivity(main);  
					} 
    			}  
        	}
        };
        myThread.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();  
	}
	
	/**
	 * Part of the activity's life cycle, StartAppAd should be integrated here
	 * for the home button exit ad integration.
	 */
	@Override
	public void onPause() {
		super.onPause();  
	}
	
	@Override
	public void onResume() {
		super.onResume();  
	} 
	 
}
