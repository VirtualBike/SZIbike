package vb.activity.virtualbike;

import vb.model.virtualbike.City;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InitActivity extends Activity {
	City city = null;
	ExpandableListView expandablelistview = null;
	private String[] province = new String[] { "   江苏省", "   安徽省" };
	private String[][] cities = new String[][] {
			{ "     苏州", "     常州", "     无锡" }, { "     宿州", "     马鞍山" } };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		city = City.getInstance();
		setContentView(R.layout.activity_init);
		final ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
			public TextView gettextview() {
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, 64);
				TextView textView = new TextView(InitActivity.this);
				textView.setLayoutParams(lp);
				textView.setGravity(Gravity.CENTER_VERTICAL);
				textView.setPadding(36, 0, 0, 0);
				textView.setTextSize(20);
				textView.setTextColor(Color.BLACK);
				return textView;
			}

			@Override
			public Object getChild(int arg0, int arg1) {
				// TODO 自动生成的方法存根
				return cities[arg0][arg1];
			}

			@Override
			public long getChildId(int arg0, int arg1) {
				// TODO 自动生成的方法存根
				return arg1;
			}

			@Override
			public View getChildView(int arg0, int arg1, boolean arg2,
					View arg3, ViewGroup arg4) {
				// TODO 自动生成的方法存根
				LinearLayout ll = new LinearLayout(InitActivity.this);
				ll.setOrientation(0);
				TextView textView = gettextview();
				textView.setText(getChild(arg0, arg1).toString());
				ll.addView(textView);
				return ll;
			}

			@Override
			public int getChildrenCount(int arg0) {
				// TODO 自动生成的方法存根
				return cities[arg0].length;
			}

			@Override
			public Object getGroup(int arg0) {
				// TODO 自动生成的方法存根
				return province[arg0];
			}

			@Override
			public int getGroupCount() {
				// TODO 自动生成的方法存根
				return province.length;
			}

			@Override
			public long getGroupId(int arg0) {
				// TODO 自动生成的方法存根
				return arg0;
			}

			@Override
			public View getGroupView(int arg0, boolean arg1, View arg2,
					ViewGroup arg3) {
				// TODO 自动生成的方法存根
				LinearLayout ll = new LinearLayout(InitActivity.this);
				ll.setOrientation(0);
				TextView textView = gettextview();
				textView.setText(getGroup(arg0).toString());
				ll.addView(textView);
				return ll;
			}

			@Override
			public boolean hasStableIds() {
				// TODO 自动生成的方法存根
				return false;
			}

			@Override
			public boolean isChildSelectable(int arg0, int arg1) {
				// TODO 自动生成的方法存根
				return true;
			}

		};

		expandablelistview = (ExpandableListView) this
				.findViewById(R.id.province);
		expandablelistview.setAdapter(adapter);

		expandablelistview.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int arg2, int arg3, long arg4) {
				// TODO 自动生成的方法存根
				// stub here for city id
				String id = "0101";
				city.Initlization(id);
				Intent intent = new Intent();
				intent.setClass(InitActivity.this,MapActivity.class);
				startActivity(intent);
				finish();
				return false;
			}
		});

	}
}