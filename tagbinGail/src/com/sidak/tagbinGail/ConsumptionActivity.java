package com.sidak.tagbinGail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConsumptionActivity extends Activity {
	private static final String TAG = ConsumptionActivity.class.getSimpleName();
	private EditText consum;
	private MyApplication myApp;
	private Button submit;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consumption);
		myApp = MyApplication.getInstance();
		consum = (EditText) findViewById(R.id.consum_value);
		submit = (Button) findViewById(R.id.final_submit_label);
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (saveInfo()) {
					Intent intent = new Intent(ConsumptionActivity.this,
							SummaryActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
								
				}
			}
		});
	}

	protected boolean saveInfo() {
		if (consum.getText().toString() != null) {
			myApp.setConsumption(consum.getText().toString());
			Log.d(TAG,consum.getText().toString() );
			return true;
		} else {
			Toast.makeText(this, getString(R.string.empty_consum_value), Toast.LENGTH_LONG).show();
			return false;
		}
	}
}
