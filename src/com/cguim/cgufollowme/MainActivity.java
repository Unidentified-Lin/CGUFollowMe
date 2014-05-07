package com.cguim.cgufollowme;

import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.cguim.cgufollowme.fragments.AdminFragment;
import com.cguim.cgufollowme.fragments.ClassRoomFragment;
import com.cguim.cgufollowme.fragments.LabFragment;
import com.cguim.cgufollowme.fragments.MeetingRoomFragment;
import com.cguim.cgufollowme.fragments.OfficeFragment;

public class MainActivity extends Activity {
	
	public String[] Group;
	
	public Map<String, String> list = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActionBar actBar = getActionBar();
		actBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, 0);

		actBar.addTab(actBar
				.newTab()
				.setText("行政")
				.setTabListener(
						new TabListener<AdminFragment.Tab1Fragment>(this, "行政",
								AdminFragment.Tab1Fragment.class)));
		actBar.addTab(actBar
				.newTab()
				.setText("系辦")
				.setTabListener(
						new TabListener<OfficeFragment.Tab2Fragment>(this,
								"系辦", OfficeFragment.Tab2Fragment.class)));
		actBar.addTab(actBar
				.newTab()
				.setText("教室")
				.setTabListener(
						new TabListener<ClassRoomFragment.Tab3Fragment>(this,
								"教室", ClassRoomFragment.Tab3Fragment.class)));
		actBar.addTab(actBar
				.newTab()
				.setText("實驗室")
				.setTabListener(
						new TabListener<LabFragment.Tab4Fragment>(this,
								"實驗室", LabFragment.Tab4Fragment.class)));
		actBar.addTab(actBar
				.newTab()
				.setText("會議室")
				.setTabListener(
						new TabListener<MeetingRoomFragment.Tab5Fragment>(this,
								"會議室", MeetingRoomFragment.Tab5Fragment.class)));

		if (savedInstanceState != null) {
			actBar.setSelectedNavigationItem(savedInstanceState
					.getInt("tab", 0));
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {

		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private final Bundle mArgs;
		private FragmentManager FragManager;
		private FragmentTransaction FragTran;
		private Fragment frag;

		public TabListener(Activity activity, String tag, Class<T> clz) {
			this(activity, tag, clz, null);
		}

		public TabListener(Activity activity, String tag, Class<T> clz,
				Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mArgs = args;

			// 檢查這個Tab是否已經存在一個fragment，如果存在，則取消其作用
			FragManager = mActivity.getFragmentManager();
			frag = FragManager.findFragmentByTag(mTag);
			if (frag != null && !frag.isDetached()) {
				FragTran = FragManager.beginTransaction();
				FragTran.detach(frag);
				FragTran.commit();
			}
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			Toast.makeText(mActivity, "Reselected!" + mTag, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if (frag == null) {
				frag = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
				ft.add(android.R.id.content, frag, mTag);
			} else {
				ft.attach(frag);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.detach(frag);
		}

	}

	
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

}
