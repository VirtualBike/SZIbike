package vb.activity.virtualbike;

import vb.context.virtualbike.BikeContext;
import vb.data.virtualbike.DBDataModule;
import vb.data.virtualbike.IDataModule;
import vb.model.virtualbike.City;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.SDKInitializer;

public class MapActivity extends Activity {
	// 显示地图的View
	private MapView mMapView = null;
	private String cityid = null;
	private String cityurl = null;
	private BikeContext _bikecontext = null;
	private Boolean _existcity = false;
	private City _city =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_map);
		mMapView = (MapView) this.findViewById(R.id.bmapView);
		_city = City.getInstance();
		Intent intent = super.getIntent();
		Bundle bundle = intent.getExtras();
		cityid = bundle.getString("id");
		cityurl = bundle.getString("url");
		IDataModule datamodulestratege = new DBDataModule();
		BikeContext context = new BikeContext(datamodulestratege);
		_existcity = context.CheckExist(cityid);
		if (_existcity) {
			// ture stands for exits city ever load ,and load derectly from db
			_city.setSlist(context.ReadDBData());
		}
		else {
			Initlization();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	// 重写以下方法，管理API
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	public void Initlization() {
		//load data from common system by a new thread In City
		_city.Initlization(cityid, cityurl);
	}

}
