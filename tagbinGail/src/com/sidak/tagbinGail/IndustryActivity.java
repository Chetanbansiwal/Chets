package com.sidak.tagbinGail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class IndustryActivity extends Activity {
	private Button next;
	private RadioButton ind1;
	private RadioButton ind2;
	private RadioButton ind3;
	private RadioButton ind4;
	private EditText other;
	private RadioGroup rGroup;
	private MyApplication myApp;
	private static final String TAG = IndustryActivity.class.getSimpleName();

	// private LinearLayout lLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_industry);
		myApp = MyApplication.getInstance();
		ind1 = (RadioButton) findViewById(R.id.ind1);
		ind2 = (RadioButton) findViewById(R.id.ind2);
		ind3 = (RadioButton) findViewById(R.id.ind3);
		ind4 = (RadioButton) findViewById(R.id.ind4);
		rGroup = (RadioGroup) findViewById(R.id.radioGroup);
		other = (EditText) findViewById(R.id.others_val);
		next = (Button) findViewById(R.id.next_label);
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (saveInfo()) {
					Intent intent = new Intent(IndustryActivity.this,
							MaterialActivity.class);
					startActivity(intent);
				}
			}
		});

	}

	public void onRadioButtonClick(View v) {
		RadioButton button = (RadioButton) v;
		if (button != null && button != ind4) {
			other.setText("");
			other.setVisibility(View.INVISIBLE);
		} else {
			if (button.isChecked()) {
				other.setVisibility(View.VISIBLE);
			} else
				other.setVisibility(View.INVISIBLE);
		}
	}

	protected boolean saveInfo() {
		RadioButton selected = null;
		int id = rGroup.getCheckedRadioButtonId();
		String industry;
		selected = (RadioButton) findViewById(id);

		// if (ind1.isChecked())
		// selected = ind1;
		// else if (ind2.isChecked())
		// selected = ind2;
		// else if (ind3.isChecked())
		// selected = ind3;
		// else if (ind4.isChecked())
		// selected = ind4;
		if (id == -1 || selected == null) {
			Toast.makeText(this, getString(R.string.empty_radio),
					Toast.LENGTH_LONG).show();
			return false;
		} else if (selected == ind4) {
			industry = other.getText().toString();
			if (!industry.equals("")) {
				Log.d(TAG, "industry name (other) : " + industry);
				myApp.setIndustry(industry);
				return true;
			} else {
				Toast.makeText(this, getString(R.string.empty_others),
						Toast.LENGTH_LONG).show();
				return false;
			}
		} else {
			industry = (String) selected.getText();
			Log.d(TAG, "industry name  : " + industry);
			myApp.setIndustry(industry);
			return true;
		}

	}
}
