package com.example.tagbintrials;

import java.util.ArrayList;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RestnBars extends ListFragment {
	public static ArrayList<CustomRes> rlist = new ArrayList<CustomRes>();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		 //MainActivity.adapter = new RBAdapter(getActivity(),
			//	R.id.listview, MainActivity.fetch);
		//setListAdapter(new RBAdapter(getActivity(), android.R.id.list, rlist));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.restnbars, container, false);
	    return view;
		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}

}
