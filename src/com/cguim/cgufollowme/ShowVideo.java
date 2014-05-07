package com.cguim.cgufollowme;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;


public class ShowVideo extends Activity {

	 private final int SPLASH_DISPLAY_LENGTH = 2500; 
	 private StringBuilder data;
	 TextView msg;

		
		public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.splashvideo);
	        
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
 
	       
	        data=new StringBuilder();
	        
	        VideoView videoView = (VideoView)findViewById(R.id.videoView1);
	        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/"+R.raw.splash));
	        videoView.requestFocus();
	        videoView.start();
	       	    
	        
	        new Handler().postDelayed(new Runnable(){ 
	              @Override 
	              public void run() { 
	                   /* Create an Intent that will start the Menu-Activity. */ 
	                   Intent mainIntent = new Intent(ShowVideo.this, MainActivity.class); 
	                   ShowVideo.this.startActivity(mainIntent); 
	                   ShowVideo.this.finish(); 
	            	//   jumpToMainLayout();
	              } 
	         }, SPLASH_DISPLAY_LENGTH); 
	    } 
		
		/* public void jumpToMainLayout()
		    {
		    	setContentView(R.layout.activity_good_main);
		    }  */
		
		
	
   
}