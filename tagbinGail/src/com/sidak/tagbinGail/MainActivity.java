package com.sidak.tagbinGail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button next;
	private Button capture_pic;
	private EditText name;
	private EditText cName;
	private EditText email;
	private EditText phone;
	//private EditText state;
	
	private String[] state_arr={
			"Andaman and Nicobar Islands",
			"Andhra Pradesh",
			"ArunachalPradesh",
			"Assam",
			"Bihar",
			"Chandigarh",
			"Chhattisgarh",
			"Dadar and Nagar Haveli",
			"Daman and Diu",
			"Delhi (NCT)",
			"Goa",
			"Gujarat",
			"Haryana",
			"Himachal Pradesh",
			"Jammu Kashmir",
			"Jharkhand",
			"Karnataka",
			"Kerala",
			"Lakshadweep",
			"Madhya Pradesh",
			"Maharashtra",
			"Manipur",
			"Meghalaya",
			"Mizoram",
			"Nagaland",
			"Odisha",
			"Pondicherry",
			"Punjab",
			"Rajasthan",
			"Sikkim",
			"Tamil Nadu",
			"Telangana",
			"Tripura",
			"Uttar Pradesh",
			"Uttarakhand",
			"West Bengal"
	};
	
	
	private String nameStr;
	private String cNameStr;
	private String emailStr;
	private String phoneStr;
	private String stateStr;
	private static final String TAG=MainActivity.class.getSimpleName();
	private Spinner stateSpinner;
	private MyApplication myApp;
	String mCurrentPhotoPath;
	//static final int REQUEST_TAKE_PHOTO = 1;
	private Uri fileUri=null; // file url to store image/video
	private Uri imageUri=null;
	public static final int MEDIA_TYPE_IMAGE = 1;

	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "GAIL India Ltd.";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = MyApplication.getInstance();
		setContentView(R.layout.activity_main);
		stateSpinner=(Spinner)findViewById(R.id.state_spinner);
		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
			    android.R.layout.simple_spinner_item, state_arr);
		adapter_state
	    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  stateSpinner.setAdapter(adapter_state);
	  stateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		  
          @Override
          public void onItemSelected(AdapterView<?> adapter, View v,
                  int position, long id) {
        	  stateSpinner.setSelection(position);
			  stateStr = (String) stateSpinner.getSelectedItem();
          }

          @Override
          public void onNothingSelected(AdapterView<?> arg0) {
              // TODO Auto-generated method stub

          }
      });
		
		next = (Button) findViewById(R.id.next_label);
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(validateInfo()){
					saveInfo();
					Intent intent = new Intent(MainActivity.this,
							PrizeActivity.class);
					startActivity(intent);
				}
			}
		});
		capture_pic = (Button) findViewById(R.id.click_pic_label);
		capture_pic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				captureImage();
			}
		});
		//state = (EditText) findViewById(R.id.state_value);
		name = (EditText) findViewById(R.id.name_val);
		email = (EditText) findViewById(R.id.email_value);
		phone = (EditText) findViewById(R.id.phone_value);
		cName = (EditText) findViewById(R.id.company_value);

	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			   long id) {
			  
			  
			 }

	
	protected void getValueFromEdittext() {
		nameStr = name.getText().toString();
		emailStr = email.getText().toString();
		phoneStr = phone.getText().toString();
		cNameStr = cName.getText().toString();
		//stateStr = state.getText().toString();
	}

	protected boolean validateInfo() {
		getValueFromEdittext();
		if(nameStr.equals("")||emailStr.equals("")||phoneStr.equals("")
				||cNameStr.equals("")||stateStr.equals("")){
			Toast.makeText(this, R.string.empty_message, Toast.LENGTH_LONG).show();
			return false;
		}else if(!isValidEmail(emailStr)){
			Toast.makeText(this, R.string.wrong_email, Toast.LENGTH_LONG).show();
			return false;
		}
		else return true;
	}
	public final static boolean isValidEmail(CharSequence target) {
		  if (TextUtils.isEmpty(target)) {
		    return false;
		  } else {
		    return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		  }
		}
	protected void saveInfo() {
		myApp.setName(nameStr);
		myApp.setEmail(emailStr);
		myApp.setPhoneNo(phoneStr);
		myApp.setCompanyName(cNameStr);
		myApp.setState(stateStr);
		if(imageUri!=null){
			myApp.setImageUrl(imageUri.toString());
			Log.d(TAG, "name "+nameStr + "email "+emailStr+ "phone "+phoneStr+"company "+cNameStr
					+"state "+stateStr+"image uri "+imageUri.toString());
		}
		Log.d(TAG, "in other log"+"name "+nameStr + "email "+emailStr+ "phone "+phoneStr+"company "+cNameStr
				+"state "+stateStr);
	}

	/**
	 * Checking device has camera hardware or not
	 * */
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/**
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	private void captureImage() {
		if (!isDeviceSupportCamera()) {
			Toast.makeText(MainActivity.this,
					getResources().getString(R.string.no_camera),
					Toast.LENGTH_LONG).show();

		} else {
			dispatchTakePictureIntent();
			/*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

			// start the image capture Intent
			startActivityForResult(intent, REQUEST_TAKE_PHOTO);
			*/
		}
	}
	static final int REQUEST_IMAGE_CAPTURE = 1;

	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}
	/**
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/**
	 * returning image / video
	 */
	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Toast.makeText(MainActivity.this,
					getResources().getString(R.string.photo_clicked),
					Toast.LENGTH_SHORT).show();
			
			  Bitmap  photo = ( Bitmap ) data.getExtras(). get ("data"); 
		         // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
		         imageUri = getImageUri(getApplicationContext(), photo);
		         
			
		}
	}
	public   Uri  getImageUri( Context  inContext,  Bitmap  inImage) {
	     ByteArrayOutputStream  bytes =  new   ByteArrayOutputStream ();
	    inImage.compress( Bitmap . CompressFormat .JPEG, 100, bytes);
	     String  path =  Images . Media .insertImage(inContext.getContentResolver(), inImage, "Title",  null );
	     return   Uri .parse(path);
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
				   intent.addCategory(Intent.CATEGORY_HOME);
				   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				   startActivity(intent);
	}

}
