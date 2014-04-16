package com.example.tagbintrials;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity{
	
	@Override
	protected
	void onCreate(Bundle nothing) {
		super.onCreate(nothing);
		setContentView(R.layout.splash_activity);
		final Intent main = new Intent(this, MainActivity.class);
		Thread Main = new Thread() {
			public void run() {
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					startActivity(main);
				}
			}
		};
		Main.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	

}
