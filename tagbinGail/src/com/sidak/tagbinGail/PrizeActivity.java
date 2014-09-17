package com.sidak.tagbinGail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PrizeActivity extends Activity {
	private ImageButton prize_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prize);
		prize_button=(ImageButton)findViewById(R.id.prize_text_image);
		
		
		
		prize_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PrizeActivity.this,
						DisplayPrizeActivity.class);
				startActivity(intent);
			}
		});
		
		
	}
}
