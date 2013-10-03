package com.example.myblued;

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
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	
	public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	protected static final int SUCCESS_CONNECT = 0;
	protected static final int MESSAGE_READ = 1;
	ArrayAdapter<String> listArray;
	Button connectNew;
	TextView pdiv;
	ListView listview;
	BluetoothAdapter btAdapter;
	Set<BluetoothDevice> devicesArray;
	ArrayList<String> pairedDevices;
	ArrayList<BluetoothDevice> devices;
	IntentFilter filter;
	BroadcastReceiver receiver;
	Handler mHandler= new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what){
			case SUCCESS_CONNECT:
				Toast.makeText(getApplicationContext(), "its Done Bro", Toast.LENGTH_SHORT).show();
				ConnectedThread connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
				String s = "successfully connected";
				connectedThread.write(s.getBytes());
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
			getPairedDevices();
			startDiscovery();
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
		connectNew = (Button)findViewById(R.id.bConnectNew);
		listview = (ListView)findViewById(R.id.listView1);
		listview.setOnItemClickListener(this);
		pdiv = (TextView)findViewById(R.id.Pdiv);
		listArray = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, 0);
		listview.setAdapter(listArray);
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		pairedDevices = new ArrayList<String>();
		devices = new ArrayList<BluetoothDevice>();

		filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				String action = arg1.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = arg1
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					devices.add(device);
					String s = "";
					for (int a = 0; a< pairedDevices.size();a++) {
						if(device.getName().equals(pairedDevices.get(a))){
							s = "(Paired)";
							break;
						}
					}
					listArray
							.add(device.getName() +" "+s+" "+"\n" + device.getAddress());
				} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
					

				} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
					if(btAdapter.getState() == BluetoothAdapter.STATE_OFF){
						btAdapter.enable();
						while(!btAdapter.isEnabled()) {
							
						}
					}
				}
			}

		};
		registerReceiver(receiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		registerReceiver(receiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(receiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(receiver, filter);
	}

	private void getPairedDevices() {
		devicesArray = btAdapter.getBondedDevices();
		if (devicesArray.size() > 0) {
			for (BluetoothDevice device : devicesArray) {
				pairedDevices.add(device.getName());
			}
		}
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
		if(btAdapter.isDiscovering()) {
			btAdapter.cancelDiscovery();
		}
			
		//if (listArray.getItem(arg2).contains("Paired")) {
			Toast.makeText(getApplicationContext(), "Awesome its Paired", Toast.LENGTH_SHORT).show();
			BluetoothDevice selectedDevice = devices.get(arg2);
			ConnectThread connecti = new ConnectThread(selectedDevice);
			connecti.start();
		//} else {
			//Toast.makeText(getApplicationContext(), "WTH its not wrking", Toast.LENGTH_SHORT).show();
		//}
	}
	
	
	private class ConnectThread extends Thread {
	    
		private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	 
	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
	            Toast.makeText(getApplicationContext(), tmp.toString(), Toast.LENGTH_SHORT).show();
	        } catch (IOException e) {
	        	Toast.makeText(getApplicationContext(), "Connected or not", Toast.LENGTH_SHORT).show();
	        }
	        mmSocket = tmp;
	        try {
				mmSocket.connect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        	if(mmSocket.isConnected()){
	        		Toast.makeText(getApplicationContext(), "Connected1", Toast.LENGTH_SHORT).show();
	        	}
	        Toast.makeText(getApplicationContext(), "Out of loop", Toast.LENGTH_SHORT).show();	 
	    }
	 
	    public void run() {
	        // Cancel discovery because it will slow down the connection
	    	//pdiv.append("runned \n");
	    	btAdapter.cancelDiscovery();
	        	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	            if(mmSocket.isConnected())
	            {
	            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
	            } else {
	            	Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
	            }
	        } catch (IOException connectException) {
	        	
	            // Unable to connect; close the socket and get out
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();
	    }
	    
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
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
