package com.example.tagbintrials;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.facebook.*;
import com.facebook.model.*;


public class MainActivity extends ActionBarActivity implements OnClickListener{
	
	public static final String PREFS_NAME = "MyPrefsFile";
	private static final int SPLASH = 0;
	private static final int SELECTION = 1;
	private static final int MOBILENO = 2;
	private static final int FRAGMENT_COUNT = MOBILENO +1;

	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	
	private ProgressDialog pDialog;
	private boolean mobilesent;
	private ViewFlipper viewFlipper;
	private EditText mobile;
	private Button Reg, blueT;
	
	JSONParser jsonParser  = new JSONParser();
	private static final String LOGIN_URL = "http://www.tagbin.in/my/ut/android.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar aBar = getSupportActionBar();
        aBar.hide();
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        mobilesent = settings.getBoolean("Mode", false);
        
        mobile = (EditText)findViewById(R.id.Mobile);
        Reg = (Button)findViewById(R.id.Register);
        blueT = (Button)findViewById(R.id.bluet);
        Reg.setOnClickListener(this);
        blueT.setOnClickListener(this);
        
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        
        FragmentManager fm = getSupportFragmentManager();
        fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
        fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
        fragments[MOBILENO]=fm.findFragmentById(R.id.moblieNo);

        FragmentTransaction transaction = fm.beginTransaction();
        for(int i = 0; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();
        
        /*
        Thread timer = new Thread(){
        	public void run(){
        		try {
        			sleep(5000);
        		} catch (InterruptedException e){
        			e.printStackTrace();
        		} finally{
        			Intent mob = new Intent("com.example.tagbintrials.MOBILESEND");
        			startActivity(mob);
        		}
        	}
        };
        timer.start();
        
        Session.openActiveSession(this, true, new Session.StatusCallback() {
        	// callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
            	if (session.isOpened()) {
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
        }); */
    }
    
    public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.Register:
			new AddMobile().execute();
		case R.id.bluet:
			new BlueAct().execute();
		}
	}
    
    class BlueAct extends AsyncTask<String, String, String>{
    	
    	@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
    		
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
    	
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Activating...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
    }
    
    class AddMobile extends AsyncTask<String, String, String>{
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
			/*try
			{
				MobNo = Integer.parseInt(Mobile);
			}
			catch (NumberFormatException e)
			{
				
			}
			*/
			try {
				List<NameValuePair> params = new ArrayList <NameValuePair>();
				params.add(new BasicNameValuePair("mobile", Mobile));
				params.add(new BasicNameValuePair("accesst", accesst));
				Log.d("request!", "starting");
				
				JSONObject json = jsonParser.makeHttpRequest(
	                       LOGIN_URL, "POST", params);

	                // full json response
	                Log.d("Login attempt", json.toString());

	                // json success element
	                success = json.getInt(TAG_SUCCESS);
	                if (success == 1) {
	                	mobilesent = true;
	                	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
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
	                }else{
	                	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
	                	return json.getString(TAG_MESSAGE);
	                }
	            } 
			catch (JSONException e) {
	                e.printStackTrace();
	            }
			return null;
			}
		
		protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		}
    private boolean isResumed = false;
    
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

    @Override
    protected void onPause(){
    	super.onPause();
    	uiHelper.onPause();
    	isResumed = false;
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
    
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
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
            	if(mobilesent){
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
        	
        	if(mobilesent){
        		showFragment(MOBILENO, false);
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
    private Session.StatusCallback callback = 
        new Session.StatusCallback() {
        @Override
        public void call(Session session, 
                SessionState state, Exception exception) {
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
    
}

