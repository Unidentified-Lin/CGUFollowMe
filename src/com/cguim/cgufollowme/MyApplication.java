package com.cguim.cgufollowme;

import android.app.Application;

public class MyApplication extends Application{
	
	private static String DESTINATION, LOCATION;
	
	@Override
    public void onCreate()
    {
        super.onCreate();
        setDestination("");//��l��Destination
        setLocation("");//��l��Location
    }
	
	public void setDestination(String destination){
		this.DESTINATION = destination;
	}
	
	public String getDestination(){
		return DESTINATION;
	}
	
	public void setLocation(String location){
		this.LOCATION = location;
	}
	
	public String getLocation(){
		return LOCATION;
	}

}
