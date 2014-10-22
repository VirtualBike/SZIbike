package vb.activity.virtualbike;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.SDKInitializer;

public class MapActivity extends Activity {
	// 显示地图的View
	private MapView mMapView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_map);
		mMapView = (MapView) this.findViewById(R.id.bmapView);
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

}
