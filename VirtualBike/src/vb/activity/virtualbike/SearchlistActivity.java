package vb.activity.virtualbike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vb.context.virtualbike.BikeContext;
import vb.model.virtualbike.City;
import vb.model.virtualbike.Station;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SearchlistActivity extends Activity {
	private EditText tView =null;
	private Button searchButton=null;
	private ListView searchListView=null;
	private String LocatingAddress =null;
	private BikeContext _bikecontext =null;
	SimpleAdapter adapter = null;
	List<Map<String, Object>> datasetList = new ArrayList<Map<String, Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_bikecontext = City.getInstance().GetContext();
		Intent intent = super.getIntent();
		Bundle bundle = intent.getExtras();
		LocatingAddress = bundle.getString("LocatingAddress");
		setContentView(R.layout.activity_stationlists);
		tView= (EditText)findViewById(R.id.text_search_edit);
		searchButton= (Button)findViewById(R.id.searchbutton);
		searchListView= (ListView)findViewById(R.id.liststationView);
		tView.setText(LocatingAddress);
		Setdata();
		adapter = new SimpleAdapter(this,datasetList, R.layout.listitem, new String[]{"name","address","distense"}, new int[]{R.id.name,R.id.address,R.id.distense});
		searchListView.setAdapter(adapter);
		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Station station = _bikecontext.gPSLocateStations.get(arg2);
				// TODO 自动生成的方法存根
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("SER_KEY", station);
				intent.putExtras(bundle);
				intent.setClass(SearchlistActivity.this,StationDisplayActivity.class);
				startActivity(intent);
			}
		});
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				_bikecontext.SearchStation(tView.getText().toString());
				Setdata();
				adapter.notifyDataSetChanged();
			}
		});
	}
	private void Setdata() {
		// TODO 自动生成的方法存根
		datasetList.clear();
		if(_bikecontext.gPSLocateStations == null)
			return ;
		else {
			for(Station station :_bikecontext.gPSLocateStations)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", station.getName());
				map.put("address", station.getAddress());
				map.put("distense", station.getDistence_info());
				datasetList.add(map);
			}
		}
	}
}
