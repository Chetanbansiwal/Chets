package com.example.tagbintrials;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RBAdapter extends ArrayAdapter<CustomRes>{
	private ArrayList<CustomRes> entries;
	private Activity activity;

	public RBAdapter(Activity a , int textViewResourceId,
			ArrayList<CustomRes> objects) {
		super(a, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.activity = a;
		this.entries = objects;
	}
	
	public static class ViewHolder{
        public TextView item1;
        public ImageView item2;
        public RatingBar item3;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
            LayoutInflater vi =
                (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.rowlayout, null);
            holder = new ViewHolder();
            holder.item1 = (TextView) v.findViewById(R.id.RBname);
            holder.item2 = (ImageView) v.findViewById(R.id.icon);
            holder.item3 = (RatingBar) v.findViewById(R.id.secondLine);
            v.setTag(holder);
        }
		
		else
            holder=(ViewHolder)v.getTag();
 
        final CustomRes custom = entries.get(position);
        if (custom != null) {
            holder.item1.setText(custom.getName());
            holder.item3.setRating(custom.getRating());
            holder.item2.setImageResource(R.drawable.icon);
        }
        return v;
		
		
	}
	
	
	
}
