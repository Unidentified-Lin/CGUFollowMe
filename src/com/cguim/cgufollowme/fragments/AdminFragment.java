package com.cguim.cgufollowme.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cguim.cgufollowme.MapActivity;
import com.cguim.cgufollowme.MapActivity;
import com.cguim.cgufollowme.MyApplication;
import com.cguim.cgufollowme.R;
import com.zijunlin.Zxing.Demo.CaptureActivity;

public class AdminFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public static class Tab1Fragment extends Fragment {

		private String[] department;
		ListView list;
		private MyApplication myApp;
		

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);

			department = getResources().getStringArray(R.array.department);

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View v = inflater.inflate(R.layout.list_layout, container,false);
			list = (ListView) v.findViewById(R.id.list_layout);
			list.setAdapter(new ArrayAdapter(getActivity(), R.layout.expand_list_group_item, R.id.item_text, department));
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Log.i("Click", "Click:"+position);
					String item = (String) parent.getItemAtPosition(position);
					myApp = (MyApplication)getActivity().getApplication();
					myApp.setDestination(item);
//					Intent intent = new Intent();
//					intent.setClass(getActivity(), MapActivity.class);
//					startActivity(intent);
					openScanner(item);
				}
			});
			return v;
		}
		
		private void openScanner(String destanation){
			myApp = (MyApplication)getActivity().getApplication();
			myApp.setDestination(destanation);
			Intent intent = new Intent();
			intent.setClass(getActivity(), CaptureActivity.class);
			startActivity(intent);
		}

	}
}
