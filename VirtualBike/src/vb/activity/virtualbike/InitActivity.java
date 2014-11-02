package vb.activity.virtualbike;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vb.context.virtualbike.BikeContext;
import vb.data.virtualbike.IDataModule;
import vb.data.virtualbike.XMLDataModule;
import vb.model.virtualbike.City;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
	Map<String, List<String>> res = new HashMap<String, List<String>>();
	private BikeContext _bikecontext = new BikeContext();
	private String[] province = new String[] { "   江苏省", "   安徽省" };
	private String[][] cities = new String[][] {
			{ "     苏州", "     常州", "     无锡" }, { "     宿州", "     马鞍山" } };

	/*
	 * function for load cities data created by Luoxiaobo 2014/10/24
	 */
	public void ReadData() throws IOException {
		// stub here
		IDataModule xmlDataModule = new XMLDataModule();
		_bikecontext.SetDataModuleStratege(xmlDataModule);
		res = _bikecontext.ReadXMLData(getResources().getAssets().open("cities.xml"));
		
		/*
		Map<String, List<String>> stub = new HashMap<String, List<String>>();
		stub.put("江苏省", Arrays.asList(" 苏州市", "无锡市", "镇江市"));
		stub.put("安徽省", Arrays.asList(" 宿州市", "马鞍山市"));
		return stub;*/
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		city = City.getInstance();
		setContentView(R.layout.activity_init);
		try {
			ReadData();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
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

				return ((List<String>) res.values().toArray()[arg0]).get(arg1).split("name=")[1].split("url=")[0];
				// return cities[arg0][arg1];
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
				return ((List<String>) res.values().toArray()[arg0]).size();
				// return cities[arg0].length;
			}

			@Override
			public Object getGroup(int arg0) {
				// TODO 自动生成的方法存根
				return ((String) res.keySet().toArray()[arg0]);
				// return province[arg0];
			}

			@Override
			public int getGroupCount() {
				// TODO 自动生成的方法存根
				return (res.keySet().size());
				// return province.length;
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
				String id = ((List<String>) res.values().toArray()[arg2]).get(arg3).split("id=")[1].split("name=")[0];
				String url = ((List<String>) res.values().toArray()[arg2]).get(arg3).split("url=")[1];
				String name = ((List<String>) res.values().toArray()[arg2]).get(arg3).split("name=")[1].split("url=")[0].trim();
 				//city.Initlization(id,id);		
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("id", id);
				bundle.putString("url", url);
				bundle.putString("name", name);
				intent.putExtras(bundle);
				intent.setClass(InitActivity.this, MapActivity.class);
				startActivity(intent);
				//finish();
				return false;
			}
		});

	}
}