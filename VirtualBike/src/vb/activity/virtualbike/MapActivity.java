package vb.activity.virtualbike;

import java.util.ArrayList;
import java.util.List;

import vb.context.virtualbike.BikeContext;
import vb.data.virtualbike.DBDataModule;
import vb.data.virtualbike.IDataModule;
import vb.model.virtualbike.City;
import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class MapActivity extends Activity implements
OnGetGeoCoderResultListener{
	// ��ʾ��ͼ��View
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private GeoCoder mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	private LocationClient mLocClient=null;
	private LocationMode mCurrentMode;
	private List<Marker> mMarkerBike;
	BitmapDescriptor mCurrentMarker= null;
	private Button locateButton=null;
	public  MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	boolean locateflag = false;
	private String cityname = null;
	private String cityid = null;
	private String cityurl = null;
	private BikeContext _bikecontext = null;
	private Boolean _existcity = false;
	private City _city =null;
	BitmapDescriptor bdA ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SDKInitializer.initialize(getApplicationContext());
		bdA = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
		Intent intent = super.getIntent();
		Bundle bundle = intent.getExtras();
		cityid = bundle.getString("id");
		cityurl = bundle.getString("url");
		cityname= bundle.getString("name");
		setContentView(R.layout.activity_map);
		mMapView = (MapView) this.findViewById(R.id.bmapView);
		locateButton = (Button)this.findViewById(R.id.locate);
		mBaiduMap = mMapView.getMap();
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		locateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				// ��λ��ʼ��
				mLocClient = new LocationClient(getApplicationContext());
				mLocClient.registerLocationListener(myListener);
				LocationClientOption option = new LocationClientOption();
				if (!locateflag) {
					// ������λͼ��
					mBaiduMap.setMyLocationEnabled(true);
					option.setOpenGps(true);// ��gps
					option.setCoorType("bd09ll"); // ������������
					option.setScanSpan(1000);
					mLocClient.setLocOption(option);
					mLocClient.start();
					locateflag = true;
				}
				else {
					mBaiduMap.setMyLocationEnabled(false);
					option.setOpenGps(false);
					mLocClient.stop();
					locateflag = false;
				}
			}
		});
		
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		mSearch.geocode(new GeoCodeOption().city(cityname).address(cityname));
		_city = City.getInstance();
		IDataModule datamodulestratege = new DBDataModule();
		_bikecontext = new BikeContext(datamodulestratege);
		_existcity = _bikecontext.CheckExist(cityid);
		if (_existcity) {
			// ture stands for exits city ever load ,and load directly from db
			_city.setSlist(_bikecontext.ReadDBData());
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

	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				mMarkerBike = new ArrayList<Marker>();
				for(LatLng l:_bikecontext.SearchStation(new LatLng(31.2806200000,120.7373470000), 5))
				{
					OverlayOptions ooA = new MarkerOptions().position(l).icon(bdA)
							.zIndex(9).draggable(true);
					mBaiduMap.addOverlay(ooA);
					mMarkerBike.add((Marker) (mBaiduMap.addOverlay(ooA)));
				}
			}
			Log.v("location",locData.latitude+","+locData.longitude);
			/*
			 * Need Thinking ÿʱÿ�̽������ݼ���ѯ������ȡ��
			 * ���̲߳�ѯ��
			 * ���Ż���
			 * 31.2825180000,120.7343100000
			 * */
		};

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	// ��д���·���������API
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

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO �Զ����ɵķ������
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapActivity.this, "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO �Զ����ɵķ������
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapActivity.this, "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
	}
}
