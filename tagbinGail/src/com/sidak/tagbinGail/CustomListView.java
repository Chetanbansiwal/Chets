package com.sidak.tagbinGail;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListView extends ListActivity {
	private static final String TAG = CustomListView.class.getSimpleName();

	// ArrayList that will hold the original Data
	ArrayList<HashMap<String, Object>> products;
	LayoutInflater inflater;
	private Button submit;
	private final int NUM_PRODUCTS = 8;
	// boolean array for storing
	// the state of each CheckBox
	boolean[] checkBoxState;
	MyApplication myApp;
	String[] names;

	// If you want to use the array initializer,
	// you cannot split the declaration and assignment.

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);
		myApp = MyApplication.getInstance();
		String[] name_copy = { getString(R.string.sector1),
				getString(R.string.sector2), getString(R.string.sector3),
				getString(R.string.sector4), getString(R.string.sector5),
				getString(R.string.sector6), getString(R.string.sector7),
				getString(R.string.sector8) };
		names = name_copy;
		submit = (Button) findViewById(R.id.submit_list);
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (saveInfo()) {
					Intent intent = new Intent(CustomListView.this,
							ConsumptionActivity.class);
					startActivity(intent);
				}
			}
		});

		// get the LayoutInflater for inflating the customomView
		// this will be used in the custom adapter
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// names= new String[NUM_PRODUCTS];
		// these arrays are just the data that
		// I'll be using to populate the ArrayList
		// You can use our own methods to get the data

		Integer[] photos = { R.drawable.p1, R.drawable.p2, R.drawable.p3_a,
				R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7,
				R.drawable.p8 };

		products = new ArrayList<HashMap<String, Object>>();

		// temporary HashMap for populating the
		// Items in the ListView
		HashMap<String, Object> temp;

		// total number of rows in the ListView
		int noOfProducts = names.length;

		// now populate the ArrayList products
		for (int i = 0; i < noOfProducts; i++) {
			temp = new HashMap<String, Object>();

			temp.put("name", names[i]);
			temp.put("photo", photos[i]);

			// add the row to the ArrayList
			products.add(temp);
		}

		/*
		 * create the adapterfirst param-the contextsecond param-the id of the
		 * layout file you will be using to fill a rowthird param-the set of
		 * values that will populate the ListView
		 */
		final CustomAdapter adapter = new CustomAdapter(this,
				R.layout.list_item, products);

		// finally,set the adapter to the default ListView
		setListAdapter(adapter);

	}

	protected boolean saveInfo() {
		String logged = "";
		boolean flag = false;
		for (int i = 0; i < NUM_PRODUCTS; i++) {
			if (checkBoxState[i]) {
				logged += names[i] + " ";
				myApp.addProduct(names[i]);
				flag = true;
			}
		}
		if (!flag) {
			Toast.makeText(this, getString(R.string.empty_checkboxes),
					Toast.LENGTH_LONG).show();
			return false;
		} else {
			Log.d(TAG,logged);
			return true;
		}
	}

	// define your custom adapter
	private class CustomAdapter extends ArrayAdapter<HashMap<String, Object>> {

		ViewHolder viewHolder;

		public CustomAdapter(Context context, int textViewResourceId,
				ArrayList<HashMap<String, Object>> products) {

			// let android do the initializing :)
			super(context, textViewResourceId, products);

			// create the boolean array with
			// initial state as false
			checkBoxState = new boolean[products.size()];
		}

		// class for caching the views in a row
		private class ViewHolder {
			ImageView photo;
			TextView name;
			CheckBox checkBox;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item, null);
				viewHolder = new ViewHolder();

				// cache the views
				viewHolder.photo = (ImageView) convertView
						.findViewById(R.id.thumbnail);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.title);
				viewHolder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkBox_listitem);

				// link the cached views to the convertview
				convertView.setTag(viewHolder);

			} else
				viewHolder = (ViewHolder) convertView.getTag();
			// posn comes from the argument of this method
			int photoId = (Integer) products.get(position).get("photo");

			// set the data to be displayed
			viewHolder.photo.setImageDrawable(getResources().getDrawable(
					photoId));
			viewHolder.name.setText(products.get(position).get("name")
					.toString());

			// VITAL PART!!! Set the state of the
			// CheckBox using the boolean array
			viewHolder.checkBox.setChecked(checkBoxState[position]);

			// for managing the state of the boolean
			// array according to the state of the
			// CheckBox

			viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					if (((CheckBox) v).isChecked())
						checkBoxState[position] = true;
					else
						checkBoxState[position] = false;

				}
			});

			// return the view to be displayed
			return convertView;
		}

	}
}
