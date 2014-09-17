package com.sidak.tagbinGail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MaterialActivity extends Activity {
	private static final String TAG = MaterialActivity.class.getSimpleName();
	private Button next;
	private CheckBox mat1;
	private CheckBox mat2;
	private CheckBox mat3;
	private CheckBox mat4;
	private CheckBox mat5;
	private CheckBox mat_others;
	private MyApplication myApp;
	private CheckBox[] cBoxes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_material);
		myApp = MyApplication.getInstance();
		mat1 = (CheckBox) findViewById(R.id.mat1);
		mat2 = (CheckBox) findViewById(R.id.mat2);
		mat3 = (CheckBox) findViewById(R.id.mat3);
		mat4 = (CheckBox) findViewById(R.id.mat4);
		mat5 = (CheckBox) findViewById(R.id.mat5);
		mat_others = (CheckBox) findViewById(R.id.mat_others);
		cBoxes = new CheckBox[6];
		cBoxes[0] = mat1;
		cBoxes[1] = mat2;
		cBoxes[2] = mat3;
		cBoxes[3] = mat4;
		cBoxes[4] = mat5;
		cBoxes[5] = mat_others;

		next = (Button) findViewById(R.id.next_label);
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (saveInfo()) {
					Intent intent = new Intent(MaterialActivity.this,
							CustomListView.class);
					startActivity(intent);
				}
			}
		});

	}

	protected boolean saveInfo() {
		String logged="";
		boolean flag = false;
		for (int i = 0; i < 6; i++) {
			if (cBoxes[i].isChecked()) {
				logged+=cBoxes[i].getText().toString()+ " ";
				myApp.addMaterial(cBoxes[i].getText().toString());
				flag = true;
			}
		}
		if (!flag) {
			Toast.makeText(this, getString(R.string.empty_checkboxes),
					Toast.LENGTH_LONG).show();
			return false;
		} else{
			Log.d(TAG, logged);
			return true;
		}
	}

}
