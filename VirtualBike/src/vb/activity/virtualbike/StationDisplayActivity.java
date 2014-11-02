package vb.activity.virtualbike;

import vb.model.virtualbike.Station;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class StationDisplayActivity extends Activity{
	Station selectstation = null;
	WebView webview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = super.getIntent();
		Bundle bundle = intent.getExtras();
		selectstation = (Station) bundle.getSerializable("SER_KEY");
		setContentView(R.layout.activity_concretinfo);
		webview = (WebView) findViewById(R.id.webView1);
		webview.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		webview.loadUrl("http://218.93.33.59:85/map/zjmap/ibikegif.asp?id=13&flag=1");
		
	}
}
