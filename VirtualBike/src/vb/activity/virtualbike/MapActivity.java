package vb.activity.virtualbike;

import java.util.ArrayList;
import java.util.List;

import vb.context.virtualbike.BikeContext;
import vb.data.virtualbike.DBDataModule;
import vb.data.virtualbike.IDataModule;
import vb.model.virtualbike.City;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
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
	// 显示地图的View
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private LocationClient mLocClient=null;
	private LocationMode mCurrentMode;
	private List<Marker> mMarkerBike;
	BitmapDescriptor mCurrentMarker= null;
	private Button locateButton=null;
	private Button searchlistButton=null;
	public  MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true;// 是否首次定位
	boolean locateflag = false;
	private String cityname = null;
	private String cityid = null;
	private String cityurl = null;
	private BikeContext _bikecontext = null;
	private Boolean _existcity = false;
	private City _city =null;
	private String LocatingAddress =null;
	BitmapDescriptor bdA ;
	List<LatLng> stationsLatLngs = new ArrayList<LatLng>();
	MyHandler handler ;
	ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SDKInitializer.initialize(getApplicationContext());
		_bikecontext= new BikeContext();
		bdA = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
		Intent intent = super.getIntent();
		Bundle bundle = intent.getExtras();
		cityid = bundle.getString("id");
		cityurl = bundle.getString("url");
		cityname= bundle.getString("name");
		setContentView(R.layout.activity_map);
		mMapView = (MapView) this.findViewById(R.id.bmapView);
		locateButton = (Button)this.findViewById(R.id.locate);
		searchlistButton = (Button)this.findViewById(R.id.searchlist);
		locateButton.setEnabled(false);
		searchlistButton.setEnabled(false);
		progressBar = (ProgressBar)this.findViewById(R.id.progressBar1);
		progressBar.setIndeterminate(false);
		progressBar.setVisibility(View.VISIBLE);
		
		handler =  new MyHandler();
		mBaiduMap = mMapView.getMap();
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		searchlistButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("LocatingAddress", LocatingAddress);
				intent.putExtras(bundle);
				intent.setClass(MapActivity.this, SearchlistActivity.class);
				startActivity(intent);
			}
		});
		locateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				// 定位初始化
				mLocClient = new LocationClient(getApplicationContext());
				mLocClient.registerLocationListener(myListener);
				LocationClientOption option = new LocationClientOption();
				if (!locateflag) {
					// 开启定位图层
					mBaiduMap.setMyLocationEnabled(true);
					option.setOpenGps(true);// 打开gps
					option.setCoorType("bd09ll"); // 设置坐标类型
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
		
		IDataModule datamodulestratege = new DBDataModule();
		_bikecontext.SetDataModuleStratege(datamodulestratege);
		_city = City.getInstance();
		_city.SetContext(_bikecontext);
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
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			mMarkerBike = new ArrayList<Marker>();
			if (isFirstLoc) {
				isFirstLoc = false;
				LocatingAddress = location.getAddrStr();
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				stationsLatLngs = _bikecontext.SearchStation(new LatLng(31.2806200000,120.7373470000), 5);
			}
			for(LatLng stationl:stationsLatLngs)
			{
				OverlayOptions ooA = new MarkerOptions().position(stationl).icon(bdA)
						.zIndex(9).draggable(true);
				mBaiduMap.addOverlay(ooA);
				mMarkerBike.add((Marker) (mBaiduMap.addOverlay(ooA)));
			}
			Log.v("location",locData.latitude+","+locData.longitude);
			/*
			 * Need Thinking 每时每刻接收数据即查询？不可取；
			 * 多线程查询？
			 * 需优化！
			 * 31.2825180000,120.7343100000
			 * */
		};

		public void onReceivePoi(BDLocation poiLocation) {
		}
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
		_city.Initlization(handler,cityid, cityurl);
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO 自动生成的方法存根
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
	}
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO 自动生成的方法存根
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
	}
	
	 class MyHandler extends Handler {
         public MyHandler() {
         }
  
         public MyHandler(Looper L) {
             super(L);
         }
         // 子类必须重写此方法,接受数据
         @Override
         public void handleMessage(Message msg) {
             // TODO Auto-generated method stub
             Log.d("MyHandler", "handleMessage......");
             if (msg.what==1) {
				progressBar.setVisibility(View.GONE);
				locateButton.setEnabled(true);
				searchlistButton.setEnabled(true);
			}
             super.handleMessage(msg);
             // 此处可以更新UI
         }
     }
	
}
