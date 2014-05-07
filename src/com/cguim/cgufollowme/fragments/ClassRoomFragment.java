package com.cguim.cgufollowme.fragments;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cguim.cgufollowme.MyApplication;
import com.cguim.cgufollowme.R;
import com.zijunlin.Zxing.Demo.CaptureActivity;

public class ClassRoomFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public static class Tab3Fragment extends Fragment {

		private String[] management_building, engineer_building,
				medical_building, second_medical_building;
		private String[] b01xx, b02xx, b03xx, e01xx, e02xx, e03xx, m01xx,
				m02xx, m03xx, llx, pblx, c01xx, c02xx;
		private MyApplication myApp;

		static enum Group {
			MANAGEMENT_BUILDING("管理大樓"), ENGINEER_BUILDING("工學大樓"), MEDICAL_BUILDING(
					"第一醫學大樓"), SECOND_MEDICAL_BUILDING("第二醫學大樓");

			private String name;

			Group(String name) {
				this.name = name;
			}

			public String getName() {
				return name;
			}
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);

			management_building = getResources().getStringArray(
					R.array.management_building);
			engineer_building = getResources().getStringArray(
					R.array.engineer_building);
			medical_building = getResources().getStringArray(
					R.array.medical_building);
			second_medical_building = getResources().getStringArray(
					R.array.second_medical_building);

			b01xx = getResources().getStringArray(R.array.B01xx);
			b02xx = getResources().getStringArray(R.array.B02xx);
			b03xx = getResources().getStringArray(R.array.B03xx);
			e01xx = getResources().getStringArray(R.array.E01xx);
			e02xx = getResources().getStringArray(R.array.E02xx);
			e03xx = getResources().getStringArray(R.array.E03xx);
			m01xx = getResources().getStringArray(R.array.M01xx);
			m02xx = getResources().getStringArray(R.array.M02xx);
			m03xx = getResources().getStringArray(R.array.M03xx);
			llx = getResources().getStringArray(R.array.LLx);
			pblx = getResources().getStringArray(R.array.PBLx);
			c01xx = getResources().getStringArray(R.array.C01xx);
			c02xx = getResources().getStringArray(R.array.C02xx);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View v = inflater.inflate(R.layout.expandable_layout, container,
					false);
			ExpandableListView elv = (ExpandableListView) v
					.findViewById(R.id.expendableList);

			final Map<Group, String[]> items = new HashMap<Group, String[]>();

			items.put(Group.MANAGEMENT_BUILDING, management_building);
			items.put(Group.ENGINEER_BUILDING, engineer_building);
			items.put(Group.MEDICAL_BUILDING, medical_building);
			items.put(Group.SECOND_MEDICAL_BUILDING, second_medical_building);

			elv.setAdapter(new ExpandAdapter(getActivity(), items));
			elv.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					// TODO Auto-generated method stub
					Log.i("position", "" + groupPosition + "-" + childPosition);
					ExpandAdapter expAdapter = new ExpandAdapter(getActivity(), items);
					String item = expAdapter.getChild(groupPosition, childPosition);
					if (item.equals("五樓_電腦教室(B050C)")){// 選B050C
						Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();
						openScanner(item);
						return true;
					}
					else
						showDialog(groupPosition, childPosition);
					return true;
				}
			});

			return v;
		}

		private void showDialog(int group, int child) {
			String[] list = null;
			switch(group){
			case 0://管理
				switch(child){
				case 0://F1
					list = b01xx;
					break;
				case 1://F2
					list = b02xx;
					break;
				case 2://F3
					list = b03xx;
					break;
				}
				break;
			case 1://工學
				switch(child){
				case 0://F1
					list = e01xx;
					break;
				case 1://F2
					list = e02xx;
					break;
				case 2://F3
					list = e03xx;
					break;
				}
				break;
			case 2://一醫
				switch(child){
				case 0://F1
					list = m01xx;
					break;
				case 1://F2
					list = m02xx;
					break;
				case 2://F3
					list = m03xx;
					break;
				case 3://F3-language
					list = llx;
					break;
				}
				break;
			case 3://二醫
				switch(child){
				case 0://B1
					list = pblx;
					break;
				case 1://F1
					list = c01xx;
					break;
				case 2://F2
					list = c02xx;
					break;
				}
				break;
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			ListView listView = new ListView(getActivity());
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_1, list);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String item = (String) parent.getItemAtPosition(position);
					Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();
					openScanner(item);
				}
			});

			builder.setNegativeButton("取消", null);

			builder.setView(listView);
			builder.create().show();
		}
		
		private void openScanner(String destanation){
			myApp = (MyApplication)getActivity().getApplication();
			myApp.setDestination(destanation);
			Intent intent = new Intent();
			intent.setClass(getActivity(), CaptureActivity.class);
			startActivity(intent);
		}

		private static final class ExpandAdapter extends
				BaseExpandableListAdapter {

			private static final int COLORS[] = new int[] { 0xFF0099CC,
					0xFF9933CC, 0xFF669900, 0xFFFF8800, 0xFFCC0000 };

			private Map<Group, String[]> mItems;
			private LayoutInflater mInflater;
			private Group[] mKeys;

			public ExpandAdapter(Context context, Map<Group, String[]> items) {
				mInflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				mItems = items;
				mKeys = Group.values();
			}

			@Override
			public String getChild(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return mItems.get(mKeys[groupPosition])[childPosition];
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				final String item = getChild(groupPosition, childPosition);
				final ViewHolder holder;

				if (convertView == null) {
					convertView = mInflater.inflate(
							R.layout.expand_list_child_item, null);
					holder = new ViewHolder(convertView);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.textViewItemTitle.setText(item);

				return convertView;
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				// TODO Auto-generated method stub
				return mItems.get(mKeys[groupPosition]).length;
			}

			@Override
			public Group getGroup(int groupPosition) {
				// TODO Auto-generated method stub
				return mKeys[groupPosition];
			}

			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				return mKeys.length;
			}

			@Override
			public long getGroupId(int groupPosition) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				final String groupName = getGroup(groupPosition).getName();
				final ViewHolder holder;

				if (convertView == null) {
					convertView = mInflater.inflate(
							R.layout.expand_list_group_item, null);
					holder = new ViewHolder(convertView);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.imageViewItemColor
						.setBackgroundColor(COLORS[groupPosition
								% COLORS.length]);
				holder.textViewItemTitle.setText(groupName);

				return convertView;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isChildSelectable(int groupPosition,
					int childPosition) {
				// TODO Auto-generated method stub
				return true;
			}

		}

		private static final class ViewHolder {
			public ImageView imageViewItemColor;
			public TextView textViewItemTitle;

			public ViewHolder(View convertView) {
				imageViewItemColor = (ImageView) convertView
						.findViewById(R.id.item_color);
				textViewItemTitle = (TextView) convertView
						.findViewById(R.id.item_text);
				convertView.setTag(this);
			}
		}
	}

}
