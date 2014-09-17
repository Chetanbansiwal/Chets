package com.man.faltu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapView extends Activity {
	
	Button btn2;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);


        //mLocMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //mProvider = mLocMan.getBestProvider(new Criteria(), true);
        //LocationListener mListener = new Geocoord();



    //Button 0
        /*
    final Button btn = (Button)findViewById(R.id.btn);
    btn.setOnClickListener(new Button.OnClickListener() {
    public void onClick(View v) {
        ....    
    }
    }); //ends button0
*/
    //Button 1
    btn2 = (Button)findViewById(R.id.btn2);
    btn2.setOnClickListener(new Button.OnClickListener() {
        public void onClick(View v) {
            //btn.performClick();



            Intent intent = new Intent(MapView.this, MainActivity.class);
            startActivity(intent);
        }//ends onClick 

        });

    }
}
