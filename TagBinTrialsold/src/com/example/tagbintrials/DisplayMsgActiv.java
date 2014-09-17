package com.example.tagbintrials;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayMsgActiv extends Activity {
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_display_msg);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		Intent intent = getIntent();
		String message= intent.getStringExtra(MainActivity.EXTRA_MSG);
		TextView textview= new TextView(this);
		textview.setTextSize(40);
		textview.setText(message);
		setContentView(textview);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
