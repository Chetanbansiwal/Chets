package com.example.layertest;

import layer.sdk.Layer;
import layer.sdk.UserManager;
import layer.sdk.herald.Log;
import layer.sdk.user.User;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected static final String TAG = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Layer.init(getApplicationContext(), "7794a378a48d25e68054bf265964cd05");
		setContentView(R.layout.activity_main);
		Toast.makeText(getApplicationContext(), "DUde Initiated", Toast.LENGTH_LONG).show();
		UserManager.create("Chetan", "Ch5t2n", "Haskell", "Curry", "haskell@curry.com", "", new UserManager.UserCreateCallback() {
			  @Override
			  public void onCreated(User user) {
			    
				  //Log.d(TAG, "user successfully registered");
				  Toast.makeText(getApplicationContext(), "DUde created", Toast.LENGTH_LONG).show();
			  }

			  @Override
			  public void onError(int code, String message) {
			    Log.e(TAG, "onError, code= " + code + "; message= " + message);
			    Toast.makeText(getApplicationContext(), "DUde ERROR", Toast.LENGTH_LONG).show();
			  }
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
