package com.carpool;

import java.util.List;

import javax.xml.datatype.Duration;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import com.facebook.*;
import com.facebook.model.*;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public class MainActivity extends ActionBarActivity {
	
	WifiScanReceiver wifiReciever;
	WifiManager mainWifiObj;
	ListView list;
	String wifis[];
	View fView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiReciever = new WifiScanReceiver();
		mainWifiObj.startScan();
		registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		//if (savedInstanceState == null) {
			Fragment Frag = new PlaceholderFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, Frag).commit();
		//}
		
		
		//Toast.makeText(getApplicationContext(), list.toString(), Toast.LENGTH_LONG).show();
		
		
		Session.openActiveSession(this, true, new Session.StatusCallback() {

		    // callback when session changes state
		    @SuppressWarnings("deprecation")
			@Override
		    public void call(Session session, SessionState state, Exception exception) {
		    	if(session.isOpened()) {
		    		Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

		    			  // callback after Graph API response with user object
		    			  @Override
		    			  public void onCompleted(GraphUser user, Response response) {
		    				  
		    				  if (user != null) {
		    					  TextView welcome = (TextView) findViewById(R.id.welcome);
		    					  welcome.setText("Hello " + user.getName() + "!");
		    				}
		    			  }
		    			});
		    	}

		    }
		  });
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		
		super.onPostResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	class WifiScanReceiver extends BroadcastReceiver {
		   public void onReceive(Context c, Intent intent) {
			   List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
		         wifis = new String[wifiScanList.size()];
		         for(int i = 0; i < wifiScanList.size(); i++){
		            wifis[i] = ((wifiScanList.get(i)).toString());
		         }
		         list = (ListView)findViewById(R.id.listView1);
		         list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
		         android.R.layout.simple_list_item_1,wifis));
		   }
	}

}


