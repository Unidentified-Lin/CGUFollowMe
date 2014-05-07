package com.cguim.cgufollowme;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class Gesture extends Activity{
	ImageView imageView01;
	ImageView imageView02;
	ImageView imageView03;
	ImageView imageView04;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesture);
	
	imageView01 = (ImageView) findViewById(R.id.imageView3);		
	imageView01.setImageResource(R.drawable.ges03);
	imageView02 = (ImageView) findViewById(R.id.imageView2);		
	imageView02.setImageResource(R.drawable.ges02);
	imageView03 = (ImageView) findViewById(R.id.imageView1);		
	imageView03.setImageResource(R.drawable.ges01);
	imageView04 = (ImageView) findViewById(R.id.imageView4);		
	imageView04.setImageResource(R.drawable.ges04);
	
}}
