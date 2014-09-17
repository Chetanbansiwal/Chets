package com.example.mixorg;

import java.lang.reflect.Array;
import java.util.List;

import layer.sdk.ContactManager;
import layer.sdk.Layer;
import layer.sdk.LayerAddress;
import layer.sdk.MessageManager;
import layer.sdk.SessionManager;
import layer.sdk.UserManager;
import layer.sdk.contacts.Contact;
import layer.sdk.messages.Message;
import layer.sdk.messages.Recipient;
import layer.sdk.messages.Recipient.State;
import layer.sdk.messages.Tag;
import layer.sdk.user.User;
import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements MessageManager.MessageListener {

	List<LayerAddress> layerAddress;
	TextView t;
	List<Array> list;
	List<Contact> contlist;
	String Username;
	
	public void onCreate(Bundle savednstanceState) {
		super.onCreate(savednstanceState);
		setContentView(R.layout.activity_main);
		t = (TextView) findViewById(R.id.textView);
		Layer.init(getApplicationContext(), "7794a378a48d25e68054bf265964cd05");
		Toast.makeText(getApplicationContext(), "Initiated", Toast.LENGTH_LONG).show();
		/*
		Username = UserManager.getInfo();
		if (Username.isEmpty())
		{
			Toast.makeText(getApplicationContext(), "Username Khali Hai", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(getApplicationContext(), Username, Toast.LENGTH_LONG).show();
		}
		
		*/
		SessionManager.login("Saurabh", "262611", new SessionManager.SessionLogInCallback() {
			
			@Override
			public void onLoggedIn(User arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Login Ho gya", Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Login Ho gya", Toast.LENGTH_LONG).show();
			}
		});
		
		ContactManager.addContact(ContactManager.newContact("Ankit", "Sinha", "ankitsinha611@gmail.com", "8826804186"), new ContactManager.ContactAddCallback() {
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Contact add na ho paye", Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onContactsAdded() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "HURRAY Added!", Toast.LENGTH_LONG).show();
			}
		});
		
		
		ContactManager.getLayerContacts(new ContactManager.ContactGetCallBack() {
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Error Aa gya!!", Toast.LENGTH_LONG).show();								
			}
			
			@Override
			public void onContactGot(List<Contact> arg0) {
				// TODO Auto-generated method stub
				contlist = arg0;
				
				if (contlist.isEmpty())
				{
					Toast.makeText(getApplicationContext(), "Khali Hai", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Size: "+contlist.size(), Toast.LENGTH_LONG).show();
					Toast.makeText(getApplicationContext(), contlist.get(0).getFirstName(), Toast.LENGTH_LONG).show();
					Toast.makeText(getApplicationContext(), contlist.get(0).getLayerAddresses().get(0).toString(), Toast.LENGTH_LONG).show();
					
					MessageManager.sendMessageToContacts("THis is the first message if it ever reaches you", "Message 1", contlist);
					
				}
				
			}
		});
		
		
		
		/*UserManager.create("Saurabh", "262611", "Saurabh", "Bhhaik", "s.bhaik13@gmail.com", "", new UserManager.UserCreateCallback() {
			
			  @Override
			  public void onError(int code, String message) {
				Toast.makeText(getApplicationContext(), "User Creation Failed "+ message, Toast.LENGTH_LONG).show();
			    Log.e("CHK", "onError, code= " + code + "; message= " + message);
			  }

			@Override
			public void onCreated(User arg0) {
				// TODO Auto-generated method stub
				Log.d("CHK","User Created");
				Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_LONG).show();
				layerAddress = arg0.getLayerAddresses();
				
			}
			});
		SessionManager.login("Saurabh", "262611", new SessionManager.SessionLogInCallback() {

			  @Override
			  public void onLoggedIn(User user) {
				  Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
			    Log.d("CHK", "user successfully logged in");
			  }

			  @Override
			  public void onError(int code, String message) {
				  Toast.makeText(getApplicationContext(), " Message "+ message, Toast.LENGTH_LONG).show();
			    Log.d("CHK", "onError; code=" + code + "; message=" + message);
			  }
			});
		Contact john = ContactManager.newContact("saurabh", "Bhaik", "john@layer.com", null);

		ContactManager.addContact(john, new ContactManager.ContactAddCallback() {
		  @Override
		  public void onContactsAdded() {
		    Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_LONG).show();
		  }

		  @Override
		  public void onError(int code, String message) {
		    Log.d("TAG", "onError; code=" + code + "; message=" + message);
		    Toast.makeText(getApplicationContext(), "Contact ERROR: "+ message, Toast.LENGTH_LONG).show();
		  }
		});*/
		
		
		
		//MessageManager.sendMessageToContacts("hello!", john);
		
		User user = UserManager.getUser();
		//ContactManager c = c.importContactsFromDevice(null);

		
		//t.setText(user.getLayerAddresses().toString());
		//Toast.makeText(getApplicationContext(), user.getLayerAddresses().toString(), Toast.LENGTH_LONG).show();
		//MessageManager.sendMessageToLayerAddress("hello!", "kjhdg", layerAddress.get(0));
		
	}

	@Override
	public void onBodiesReceived(List<Message> arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "Body received", Toast.LENGTH_LONG).show();
		t.setText(arg0.get(0).toString() + "Body Received");
		//Log.d("CHK","BODY RECEIVED");
		
	}

	@Override
	public void onError(int arg0, String arg1) {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public boolean onHeadersReceived(List<Message> arg0) {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), "Header Received", Toast.LENGTH_LONG).show();
		return false;
	}

	@Override
	public void onMessageTagChanged(List<Tag> arg0, Message arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStateChanged(State arg0, Recipient arg1, Message arg2) {
		// TODO Auto-generated method stub
		
	}
	
	

}
