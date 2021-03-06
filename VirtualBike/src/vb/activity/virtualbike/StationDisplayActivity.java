package vb.activity.virtualbike;

import java.io.IOException;


import vb.context.virtualbike.BikeContext;
import vb.helpers.virtualbike.GifOpenHelper;
import vb.helpers.virtualbike.HttpRequestHelper;
import vb.model.virtualbike.City;
import vb.model.virtualbike.Station;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;



public class StationDisplayActivity extends Activity {
	
	Station selectstation = null;
	String selectid =null;
	String selectaddresString=null;
	City city = null;
	BikeContext context;
	StationHandler handler;
	ImageView imageviewborrowinfo;
	ImageView imageviewreturninfo;
	Bitmap borrowinfoBitmap;
	Bitmap returninfoBitmap;
	TextView textviewborrow;
	TextView textviewreturn;
	TextView textviewaddress;
	String stationinfotemplate = "http://218.93.33.59:85/map/szmap/ibikegif.asp?id=idinput&flag=flaginput";
	String borrowinfoString;
	String returninfoString;
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private GeoCoder mSearch = null; 
	double lat ;
	double lon ;
	int number;
	final static int RECIEVE_BORROW_INFO = 2;
	final static int RECIEVE_RETURN_INFO = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = super.getIntent();
		Bundle bundle = intent.getExtras();
		selectid = bundle.getString("id");
		selectaddresString = bundle.getString("address");
		lat = Double.valueOf(bundle.getString("lat"));
		lon = Double.valueOf(bundle.getString("lon"));
		number = bundle.getInt("number");
		city = City.getInstance();
		handler = new StationHandler();
		context = city.GetContext();
		stationinfotemplate = city.getStationurl();
		
		if (city.dnotwrite2db) {
			// stand for the data just need textview instead of webview
			setContentView(R.layout.activity_stationlistswithtext);
			textviewborrow = (TextView)findViewById(R.id.textviewborrowinfo);
			textviewreturn = (TextView)findViewById(R.id.textviewreturninfo);
			textviewaddress = (TextView)findViewById(R.id.textviewaddress2);
			
			textviewaddress.setText(selectaddresString);
			textviewborrow.setText(context.gPSLocateStations.get(number).getBorrow_info());
			textviewreturn.setText(context.gPSLocateStations.get(number).getReturn_info());
		
		}else {
			setContentView(R.layout.activity_concretinfo);
			imageviewborrowinfo = (ImageView)findViewById(R.id.imageViewborrowinfo);
			imageviewreturninfo = (ImageView)findViewById(R.id.imageViewreturninfo);
		
			borrowinfoString = stationinfotemplate.replace("idinput", selectid).replace("flaginput", "1");
			returninfoString = stationinfotemplate.replace("idinput", selectid).replace("flaginput", "2");
			StationThreadborrow stb = new StationThreadborrow(borrowinfoString);
			StationThreadreturn str = new StationThreadreturn(returninfoString);
			stb.start();
			str.start();
			/*webviewborrow = (WebView) findViewById(R.id.webViewborrowinfo);
			webviewborrow.setWebChromeClient(new WebChromeClient());
			webviewborrow.setWebViewClient(new WebViewClient());
			webviewborrow.getSettings().setJavaScriptEnabled(true);
			webviewborrow.getSettings().setBlockNetworkImage(false);
			webviewborrow.getSettings().setUserAgentString("Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X;en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334bSafari/531.21.10");
			webviewreturn = (WebView) findViewById(R.id.webViewreturninfo);
			webviewreturn.getSettings().setJavaScriptEnabled(true);*/
			textviewaddress = (TextView)findViewById(R.id.textviewaddressinfo);
			textviewaddress.setText(selectaddresString);
			/*webviewborrow.loadUrl(stationinfotemplate.replace("idinput", selectid).replace("flaginput", "1"));
			webviewreturn.loadUrl(stationinfotemplate.replace("idinput", selectid).replace("flaginput", "2"));*/
		}
		mMapView = (MapView) this.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		LatLng stationloc = new LatLng(lat, lon);
		CoordinateConverter converter  = new CoordinateConverter();  
		converter.from(CoordType.COMMON);  
		// sourceLatLng待转换坐标  
		converter.coord(stationloc);  
		LatLng desLatLng = converter.convert();
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
			    .fromResource(R.drawable.station);  
			//构建MarkerOption，用于在地图上添加Marker  
			OverlayOptions option = new MarkerOptions()  
			    .position(desLatLng)  
			    .icon(bitmap).zIndex(9);  
			//在地图上添加Marker，并显示  
			mBaiduMap.addOverlay(option);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(desLatLng, 15);
			mBaiduMap.animateMapStatus(u);
	}
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
	
	 class StationHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			if (msg.what==RECIEVE_RETURN_INFO) {
				imageviewreturninfo.setImageBitmap(returninfoBitmap);
			}
			else if(msg.what == RECIEVE_BORROW_INFO)
			{
				imageviewborrowinfo.setImageBitmap(borrowinfoBitmap);
			}
			super.handleMessage(msg);
		}
		
	}
	
	 class StationThreadborrow extends Thread{
		 String url ;
		 Message msg = new Message();
		 public StationThreadborrow (String borrowurl) {
			 url = borrowurl;
		}
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			HttpRequestHelper hrh = new HttpRequestHelper(url);
			GifOpenHelper go = new GifOpenHelper();
			try {
				if(hrh.GetInputStream() != null){
					go.read(hrh.GetInputStream());
				}
				borrowinfoBitmap = go.getImage();
				msg.what = RECIEVE_BORROW_INFO;
				handler.sendMessage(msg);
			} catch (IllegalStateException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			super.run();
		}
		
	}
	 class StationThreadreturn extends Thread{
		 Message msg =new Message();
		 String url ;
		 public StationThreadreturn (String returnurl) {
			 url = returnurl;
		}
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				HttpRequestHelper hrh = new HttpRequestHelper(url);
				GifOpenHelper go = new GifOpenHelper();
				try {
					if(hrh.GetInputStream() != null){
						go.read(hrh.GetInputStream());
					}
					returninfoBitmap = go.getImage();
					msg.what  = RECIEVE_RETURN_INFO;
					handler.sendMessage(msg);
				} catch (IllegalStateException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				super.run();
			}
			
		}
	
	
}
