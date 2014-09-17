package com.example.tagbintrials;
import android.view.View;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MSG = "com.example.tagbintrials.MSG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void sendMsg(View view){
    	Intent intent = new Intent(this, DisplayMsgActiv.class);
    	EditText editText = (EditText) findViewById(R.id.edit_msg);
    	String msg = editText.getText().toString();
    	intent.putExtra(EXTRA_MSG, msg);
    	startActivity(intent);
    }
    
}
