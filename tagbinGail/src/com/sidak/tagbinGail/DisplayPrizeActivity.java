package com.sidak.tagbinGail;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class DisplayPrizeActivity extends Activity {
	private VideoView vidView;
	private ImageView logo;
	private TextView prize_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
		    WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		     WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_display_prize);
		logo=(ImageView)findViewById(R.id.gail_logo);
		prize_text=(TextView)findViewById(R.id.prize_text);
		vidView = (VideoView) findViewById(R.id.prize_video);
		Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"
				+ R.raw.vid);
		vidView.setVideoURI(uri);
		prize_text.setVisibility(View.INVISIBLE);

		vidView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Random rand = new Random();
				int n = rand.nextInt(2);
				if (n == 1) {
					prize_text.setText(R.string.prize_won);
					logo.setVisibility(View.VISIBLE);
					prize_text.setVisibility(View.VISIBLE);
				} else{
					prize_text.setText(R.string.prize_lost);
					logo.setVisibility(View.INVISIBLE);
					prize_text.setVisibility(View.VISIBLE);
				}
				moveNextAfterTime(3000);

			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		vidView.start();
	}
	private void moveNextAfterTime(int milli) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				Intent intent = new Intent(DisplayPrizeActivity.this,
						IndustryActivity.class);
				startActivity(intent);
				finish();
			}
		}, milli);
	}
}
