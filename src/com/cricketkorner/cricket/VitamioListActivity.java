/*
 * Copyright (C) 2013 YIXIA.COM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cricketkorner.cricket;

import io.vov.vitamio.LibsChecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.banner.Banner;
import com.startapp.android.publish.splash.SplashConfig;
import com.startapp.android.publish.splash.SplashConfig.Theme;

public class VitamioListActivity extends Activity { 

	private class ReportFeedback  extends AsyncTask<String, Void, Void> { 
		 
        private final HttpClient Client = new DefaultHttpClient(); 
        private String Error = null;
        String  result;
        String  errorName;
        String  errorCode;
        private ProgressDialog Dialog = new ProgressDialog(VitamioListActivity.this); 
         
        // Call after onPreExecute method
        @Override
		protected Void doInBackground(String... urls) {
            try {
            	
                // Call long running operations here (perform background computation)
                // NOTE: Don't call UI Element here. 
                HttpPost httppost = new HttpPost(urls[0]);  
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("Group","2nd")); 
                nameValuePairs.add(new BasicNameValuePair("Message",urls[1]));
                nameValuePairs.add(new BasicNameValuePair("ReportId",urls[2]));
                
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
                // Execute HTTP Post Request
                HttpResponse response 		  = Client.execute(httppost); 
                HttpEntity entity 	  		  = response.getEntity();
                InputStream instream  		  = entity.getContent();
                result 						  = convertStreamToString(instream); 
                JSONObject jObject 			  = new JSONObject(result); 
                errorCode 			  		  = jObject.getString("errorCode"); 
                
                JSONObject dataObject 		  = jObject.getJSONObject("data");  
                JSONObject notficationsObject = jObject.getJSONObject("notifications"); 
                
                if(errorCode.equals("0")){  
                	   
                }
                else{
                	errorName 	= notficationsObject.getString("errorName"); 
                } 
                
            } catch (Exception e) { 
                Error = e.getMessage();   
                cancel(true);
                Log.i("Error: ",Error);
                Dialog.dismiss(); 
            }
			return null; 
        }
 
        @Override
		protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here. 
        	ListFunction();
        	Dialog.dismiss();
        	
        }
         
        @Override
		protected void onPreExecute() {
            // NOTE: You can call UI Element here.
             
            //UI Element 
        	 Dialog.setCanceledOnTouchOutside(false);
             Dialog.setMessage("Getting Live Channels");
             Dialog.show();  
        } 
	}

	private class ServerHitLinks  extends AsyncTask<String, Void, Void> { 
		 
        private final HttpClient Client = new DefaultHttpClient(); 
        private String Error = null;
        String  result;
        String  errorName;
        String  errorCode;
        private ProgressDialog Dialog = new ProgressDialog(VitamioListActivity.this);
         	  
        // Call after onPreExecute method
        @Override
		protected Void doInBackground(String... urls) {
            try {
            	
                // Call long running operations here (perform background computation)
                // NOTE: Don't call UI Element here. 
                HttpPost httppost = new HttpPost(urls[0]);  
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("Group","2nd")); 
                nameValuePairs.add(new BasicNameValuePair("Email",UserEmailFetcher.getEmail(getApplicationContext()))); 
                nameValuePairs.add(new BasicNameValuePair("From","CricketKorner")); 
                
                 
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
                // Execute HTTP Post Request
                HttpResponse response 		  = Client.execute(httppost); 
                HttpEntity entity 	  		  = response.getEntity();
                InputStream instream  		  = entity.getContent();
                result 						  = convertStreamToString(instream); 
                JSONObject jObject 			  = new JSONObject(result); 
                errorCode 			  		  = jObject.getString("errorCode"); 
                
                JSONObject dataObject 		  = jObject.getJSONObject("data"); 
                JSONArray linksArray		  = dataObject.getJSONArray("links");
                JSONObject notficationsObject = jObject.getJSONObject("notifications"); 
                
                if(errorCode.equals("0")){  
                    Singleton.getInstance().getChannelList().removeAllElements();
                	for(int i=0; i < linksArray.length(); i++){
                		
                		String id = linksArray.getJSONObject(i).getString("id");
                		String name = linksArray.getJSONObject(i).getString("name");
                    	String path = linksArray.getJSONObject(i).getString("path");
                		
                    	ChannelModel channelObj = new ChannelModel();
                		channelObj.setChannelId(id);
                		channelObj.setChannelName(name);
                		channelObj.setChannelPath(path);
                		
                		Singleton.getInstance().addChannelModelObject(channelObj);  
                	}   
                }
                else{
                	errorName 	= notficationsObject.getString("errorName"); 
                } 
                
            } catch (Exception e) { 
                Error = e.getMessage();   
                cancel(true);
                Log.i("Error: ",Error);
                Dialog.dismiss(); 
            }
			return null; 
        }
 
        @Override
		protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here. 
        	ListFunction();
        	Dialog.dismiss();
        	
        }
         
        @Override
		protected void onPreExecute() { 
             
            //UI Element 
             Dialog.setMessage("Getting Live Channels");
             Dialog.show(); 
        } 
	}
 
	class VersionAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;

		public VersionAdapter(VitamioListActivity dashboardActivity) { 
			layoutInflater = (LayoutInflater) dashboardActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() { 
			return title.size();
		}

		@Override
		public Object getItem(int position) { 
			return position;
		}

		@Override
		public long getItemId(int position) { 
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) { 

			View listItem = convertView;
			int pos = position;
			if (listItem == null) {
				listItem = layoutInflater.inflate(R.layout.dashboard_list_item, null);
			}

			// Initialize the views in the layout
			ImageView iv = (ImageView) listItem.findViewById(R.id.thumb);
			TextView tvTitle = (TextView) listItem.findViewById(R.id.title);
			TextView tvDesc = (TextView) listItem.findViewById(R.id.desc);

			// set the views in the layout
			iv.setBackgroundResource(thumb.get(pos));
			tvTitle.setText(title.get(pos));
			tvDesc.setText(desc.get(pos));

			return listItem;
		} 
	}

	public static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	} 
	  private ListView listView;  
	  private ArrayList<String> title;
	   
	  
	  private ArrayList<String> desc;
	  private ArrayList<Integer> thumb; 
		RelativeLayout relativeLayout;
 
	Banner adView;

	/** StartAppAd object declaration */
	  private StartAppAd startAppAd = new StartAppAd(this);

	public static final String FROM_ME = "fromVitamioInitActivity";
	/** StartApp Native Ad declaration */
//		private StartAppNativeAd startAppNativeAd = new StartAppNativeAd(this);
//		private NativeAdDetails nativeAd = null;

	List <Map<String, Object>> myData;
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    // if no network is available networkInfo will be null
	    // otherwise check if we are connected
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    return false;
	} 
	private void ListFunction() { 
		
		listView = (ListView) findViewById(R.id.listView); 
		//copying array 
				for(int i = 0; i < Singleton.getInstance().getChannelList().size(); i++){ 
					
					title.add(Singleton.getInstance().getChannelList().get(i).getChannelName());
					desc.add( "Long Press This Channel To Report, Id: "+Singleton.getInstance().getChannelList().get(i).getChannelId());
					thumb.add(R.drawable.ic_launcher);
				} 
		listView.setAdapter(new VersionAdapter(VitamioListActivity.this)); 
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				AlertDialog.Builder alert = new AlertDialog.Builder(VitamioListActivity.this);

				alert.setTitle("Report Broken Link");
				alert.setMessage("Message: (Channel not working?)");

				// Set an EditText view to get user input 
				final EditText input = new EditText(VitamioListActivity.this);
				alert.setView(input);

				alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
				  String value = input.getText().toString();
					  if(isNetworkAvailable()){
						  new ReportFeedback().execute("http://ahmadshahwaiz.com/LiveStreaming/reportFeedback.php",value,Singleton.getInstance().getChannelList().get(position).getChannelId());
					   }
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  @Override
				public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
				  }
				}); 
				alert.show(); 
				return false;
			} 
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) { 
				int pos = arg2;

				LayoutInflater layoutInflator = getLayoutInflater();

				View layout = layoutInflator.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView iv = (ImageView) layout.findViewById(R.id.toast_iv);
				TextView tv = (TextView) layout.findViewById(R.id.toast_tv);

				iv.setBackgroundResource(thumb.get(pos));
				tv.setText(title.get(pos));  
				Intent intent = new Intent(VitamioListActivity.this, VideoViewDemo.class);
				intent.putExtra("path", Singleton.getInstance().getChannelList().get(pos).getChannelPath());
				startAppAd.onBackPressed();
				startActivity(intent); 
			}
		});  
	} 
	@Override
	public void onBackPressed() {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(VitamioListActivity.this);

		alert.setTitle("Please Rate This App.");
		alert.setMessage("We highly appreciate you to rate us.");
 

		alert.setPositiveButton("I will Rate. :-)", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int whichButton) { 
			String url = "https://play.google.com/store/apps/details?id=com.cricketkorner.cricket";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		  }
		});

		alert.setNegativeButton("No Thanks :-(", new DialogInterface.OnClickListener() {
		  @Override
		public void onClick(DialogInterface dialog, int whichButton) {
		    	finish(); 
		    	 
		  }
		});
		alert.show(); 
	}  
	/**
	 * Part of the activity's life cycle, StartAppAd should be integrated here
	 * for the back button exit ad integration.
	 */ 
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		myData = new ArrayList <Map<String, Object>>();
		StartAppSDK.init(this, "106556515", "206801872"); 
		startAppAd	  	=   new StartAppAd(this);
		// relativeLayout  = 	(RelativeLayout)findViewById(R.id.channelLayout); 
		
		/** Create Splash Ad **/
		StartAppAd.showSplash(this, savedInstanceState,
				new SplashConfig()
					.setTheme(Theme.OCEAN)
					.setLogo(R.drawable.ic_launcher)
					.setAppName("Cricket Korner!")
		);
		setContentView(R.layout.channel_list);
		StartAppAd.showSlider(this);
 
		
		title	= 	new ArrayList<String>();
		desc 	= 	new ArrayList<String>();
		thumb	=	new ArrayList<Integer>();  

		RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.channelLayout);

		// Create new StartApp banner
		Banner startAppBanner = new Banner(this);
		RelativeLayout.LayoutParams bannerParameters = 
				new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
		bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
		bannerParameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		// Add the banner to the main layout
		mainLayout.addView(startAppBanner, bannerParameters);
 
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		 
		if(isNetworkAvailable()){
		 
			new ServerHitLinks().execute("http://ahmadshahwaiz.com/LiveStreaming/getLinks.php");
		}
		else
		{
			 Toast.makeText(getApplicationContext(), "Please Connect With Internet", Toast.LENGTH_LONG).show();
			 finish();
			 finish();
		} 
	}
	/**
	 * Part of the activity's life cycle, StartAppAd should be integrated here
	 * for the home button exit ad integration.
	 */
	@Override
	public void onPause() {
		super.onPause();
		startAppAd.onPause();
	} 
	
	/**
	 * Part of the activity's life cycle, StartAppAd should be integrated here. 
	 */
	@Override
	public void onResume() {
		super.onResume();
		startAppAd.onResume();
	} 
}
