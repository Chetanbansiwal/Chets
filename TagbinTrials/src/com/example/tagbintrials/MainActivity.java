package com.example.tagbintrials;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.ViewFlipper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.facebook.*;



public class MainActivity extends ActionBarActivity implements OnClickListener,
		SensorEventListener {

	private float mLastX, mLastY, mLastZ;
	private ListView lv;
	public RBAdapter adapter;
	public ArrayList<CustomRes> fetch = new ArrayList<CustomRes>();

	private boolean mInitialized;
	String Name, vEmail, vSex, vMobile = null;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private final float NOISE = (float) 2.0;
	public static final String PREFS_NAME = "MyPrefsFile";
	private static final int SPLASH = 0;
	private static final int SELECTION = 1;
	private static final int MOBILENO = 2;
	private static final int RESTNBARS = 3;
	private static final int FRAGMENT_COUNT = RESTNBARS + 1;
	public TextView userNameView, sexview, mobileview, emailview;
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	private ProgressDialog pDialog;
	private boolean mobilesent;
	private boolean shaked = false;
	// private ViewFlipper viewFlipper;
	private EditText mobile;
	private Button Reg;
	FragmentManager fm = getSupportFragmentManager();
	JSONParser jsonParser = new JSONParser();
	private static final String HANDLE_URL = "http://tagbin.in/tagApp/nativeRequest/handler.php";
	private static final String LOGIN_URL = "http://www.tagbin.in/my/ut/android.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar aBar = getSupportActionBar();
		aBar.hide();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		mobilesent = settings.getBoolean("Mode", false);
		userNameView = (TextView) findViewById(R.id.selection_user_name);
		sexview = (TextView) findViewById(R.id.selection_gender);
		mobileview = (TextView) findViewById(R.id.selection_mobile);
		emailview = (TextView) findViewById(R.id.selection_email);
		mInitialized = false;
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mobile = (EditText) findViewById(R.id.Mobile);
		Reg = (Button) findViewById(R.id.Register);
		Reg.setOnClickListener(this);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
		fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
		fragments[MOBILENO] = fm.findFragmentById(R.id.moblieNo);
		fragments[RESTNBARS] = fm.findFragmentById(R.id.restNBars);

		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			transaction.hide(fragments[i]);
		}
		transaction.commit();
		new JSONRead().execute();		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Register:
			new AddMobile().execute();
		}
	}

	class AddMobile extends AsyncTask<String, String, String> {
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Creating User...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			int success;

			String Mobile = mobile.getText().toString();
			String accesst = Session.getActiveSession().getAccessToken();
			
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("mobile", Mobile));
				params.add(new BasicNameValuePair("accesst", accesst));
				Log.d("request!", "starting");

				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
						params);

				// full json response
				Log.d("Login attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					mobilesent = true;
					SharedPreferences settings = getSharedPreferences(
							PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("Mode", true);
					editor.commit();
					Log.d("User Created!", json.toString());

					FragmentManager fm = getSupportFragmentManager();
					FragmentTransaction transaction = fm.beginTransaction();
					transaction.hide(fragments[SELECTION]);
					transaction.show(fragments[MOBILENO]);

					transaction.commit();

					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}
	}

	private boolean isResumed = false;

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
		mSensorManager.unregisterListener(this);
	}

	private void showFragment(int fragmentIndex, boolean addToBackStack) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);
			} else {
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		// Only make changes if the activity is visible
		if (isResumed) {
			FragmentManager manager = getSupportFragmentManager();
			// Get the number of entries in the back stack
			int backStackSize = manager.getBackStackEntryCount();
			// Clear the back stack
			for (int i = 0; i < backStackSize; i++) {
				manager.popBackStack();
			}
			if (state.isOpened()) {
				// If the session state is open:
				// Show the authenticated fragment
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				mobilesent = settings.getBoolean("Mode", false);
				if (mobilesent) {
					//new JSONRead().execute();
					showFragment(MOBILENO, false);
				} else {
					showFragment(SELECTION, false);
				}
			} else if (state.isClosed()) {
				// If the session state is closed:
				// Show the login fragment
				showFragment(SPLASH, false);
			}
		}
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Session session = Session.getActiveSession();

		if (session != null && session.isOpened()) {
			// if the session is already open,
			// try to show the selection fragment

			if (mobilesent) {
				new JSONRead().execute();
				//showFragment(MOBILENO, false);
			} else {
				showFragment(SELECTION, false);
			}
		} else {
			// otherwise present the splash screen
			// and ask the person to login.
			showFragment(SPLASH, false);
		}
	}

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	class JSONRead extends AsyncTask<String, String, String> {

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			userNameView.setText(Name);
			sexview.setText(vSex);
			emailview.setText(vEmail);
			mobileview.setText(vMobile);
			showFragment(MOBILENO, false);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("_MAC", "12:34"));
				params.add(new BasicNameValuePair("_TYPE", "profile"));
				Log.d("request!", "starting");
				JSONObject json = jsonParser.makeHttpRequest(HANDLE_URL,
						"POST", params);

				if (json.getString("firstname") != null) {
					Name = json.getString("firstname");
				}
				if (json.getString("gender") != null) {
					vSex = json.getString("gender");
				}
				if (json.getString("tagid") != null) {
					vMobile = json.getString("tagid");
				}
				if (json.getString("email") != null) {
					vEmail = json.getString("email");
				}

				// full json response
				Log.d("Login attempt", json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

			}
			return null;
		}

	}

	public class ResList extends AsyncTask<String, String, String> {

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			lv = (ListView) findViewById(android.R.id.list);
			RBAdapter adapt = new RBAdapter(MainActivity.this,
					android.R.id.list, fetch);
			lv.setAdapter(adapt);
			showFragment(RESTNBARS, false);

			super.onPostExecute(result);
			shaked = false;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("_MAC", "12:34"));
				params.add(new BasicNameValuePair("_TYPE", "res_list"));
				Log.d("request!", "starting");
				JSONObject json = jsonParser.makeHttpRequest(HANDLE_URL,
						"POST", params);
				// String deals1 = json.toString();
				for (int i = 1; i <= json.length(); i++) {
					JSONArray deals = json.getJSONArray("res" + i);
					CustomRes a = new CustomRes(deals.getString(0),
							deals.getString(1), (float) deals.getDouble(2),
							deals.getString(3));
					fetch.add(a);
				}
				// full json response
				Log.d("Login attempt", json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

			}
			return null;
		}

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE)
				deltaX = (float) 0.0;
			if (deltaY < NOISE)
				deltaY = (float) 0.0;
			if (deltaZ < NOISE)
				deltaZ = (float) 0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			if (deltaX >= 5.0 && deltaY >= 5 && !shaked) {
				if (fm.findFragmentById(R.id.moblieNo).isVisible()) {
					shaked = true;
					new ResList().execute();
				}

			}
		}
	}

}
