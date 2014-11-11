package vb.activity.virtualbike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
	    new Handler().postDelayed(new Runnable() {     
            @Override     
            public void run() {     
            	Intent intent = new Intent();
        		intent.setClass(WelcomeActivity.this, InitActivity.class);
        		startActivity(intent);
        		finish();
                overridePendingTransition(R.anim.zoomin,     
                            R.anim.zoomout);     
            }     
    }, 3000);     
	
	}
}
