package com.example.mybluedserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	protected static final int SUCCESS_CONNECT = 0;
	protected static final int MESSAGE_READ = 1;
	ArrayAdapter<String> listArray;
	Button connectNew;
	ListView listview;
	BluetoothAdapter btAdapter;
	Set<BluetoothDevice> devicesArray;
	ArrayList<String> pairedDevices;
	ArrayList<BluetoothDevice> devices;
	//IntentFilter filter;
	//BroadcastReceiver receiver;
	Handler mHandler= new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what){
			case SUCCESS_CONNECT:
				//Toast.makeText(getApplicationContext(), "its Done Bro", Toast.LENGTH_SHORT).show();
				//ConnectedThread connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
				String s = "successfully connected";
				//connectedThread.write(s.getBytes());
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[])msg.obj;
				String str = new String(readBuf);
				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		if (btAdapter == null) {
			Toast.makeText(getApplicationContext(), "No Bluetooth", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			if (!btAdapter.isEnabled()) {
				if (btAdapter.enable()) {
					Toast.makeText(getApplicationContext(), "connecting", Toast.LENGTH_SHORT)
							.show();
				}
			}
			while(!btAdapter.isEnabled()) {
				
			}
			//getPairedDevices();
			//startDiscovery();
		}

	}
	
	private void startDiscovery() {
		btAdapter.cancelDiscovery();
		btAdapter.startDiscovery();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void init() {
		listview = (ListView)findViewById(R.id.listView1);
		listArray = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, 0);
		listview.setAdapter(listArray);
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		pairedDevices = new ArrayList<String>();
		devices = new ArrayList<BluetoothDevice>();
		Intent discoverableIntent = new
				Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				startActivity(discoverableIntent);
				
		AcceptThread served = new AcceptThread();
		served.start();

		
		/*
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		registerReceiver(receiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(receiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(receiver, filter);
		*/
	}

	private void getPairedDevices() {
		devicesArray = btAdapter.getBondedDevices();
		if (devicesArray.size() > 0) {
			for (BluetoothDevice device : devicesArray) {
				pairedDevices.add(device.getName());
			}
		}
	}
	
	
	private class AcceptThread extends Thread {
	    private final BluetoothServerSocket mmServerSocket;

	    public AcceptThread() {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            String dude = "MybluedServer";
				// MY_UUID is the app's UUID string, also used by the client code
	            tmp = btAdapter.listenUsingInsecureRfcommWithServiceRecord(dude, MY_UUID);
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	    }

	    public void run() {
	        BluetoothSocket socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	                socket = mmServerSocket.accept();
	            } catch (IOException e) {
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	                //manageConnectedSocket(socket);
	            	try {
						mmServerSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	mHandler.obtainMessage(SUCCESS_CONNECT, mmServerSocket).sendToTarget();
	                break;
	            }
	        }
	    }

	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	private class ConnectedThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	 
	    public ConnectedThread(BluetoothSocket socket) {
	        mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void run() {
	        byte[] buffer;  // buffer store for the stream
	        int bytes; // bytes returned from read()
	 
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	            	buffer = new byte[1024];
	            	bytes = mmInStream.read(buffer);
	                // Send the obtained bytes to the UI activity
	                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
	                        .sendToTarget();
	            } catch (IOException e) {
	                break;
	            }
	        }
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) { }
	    }
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
}
