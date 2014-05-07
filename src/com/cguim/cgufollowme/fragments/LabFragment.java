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


public class LabFragment extends Fragment {

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public static class Tab4Fragment extends Fragment {

		private String[] medical, engineer, management;
		private MyApplication myApp;

		static enum Group {
			MEDICAL("醫學院"), ENGINEER("工學院"), MANAGEMENT("管理學院");

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

			medical = getResources().getStringArray(R.array.medical);
			engineer = getResources().getStringArray(R.array.engineer);
			management = getResources().getStringArray(R.array.management);

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

			items.put(Group.MEDICAL, medical);
			items.put(Group.ENGINEER, engineer);
			items.put(Group.MANAGEMENT, management);

			elv.setAdapter(new ExpandAdapter(getActivity(), items));
			elv.setOnChildClickListener( new OnChildClickListener() {
				
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					// TODO Auto-generated method stub
					Log.i("position", "" + groupPosition + "-" + childPosition);
					showDialog();			
					return true;
				}
			});

			return v;
		}
		
		private void showDialog() {
			String[] list = {"LAB1","LAB2","LAB3","LAB4","LAB5","LAB6","LAB7","LAB8","LAB9"};
			
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
