package com.sidak.tagbinGail;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<ListModel> {
	private final List<ListModel> list;
	private final Context context;

	public CustomListAdapter(Context context, List<ListModel> list) {
		super(context, R.layout.list_item, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected ImageView image;
		protected TextView text;
		protected CheckBox checkbox;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
	    if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	      view = inflator.inflate(R.layout.list_item, null);
	      final ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) view.findViewById(R.id.title);
	      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkBox_listitem);
	      viewHolder.checkbox
	          .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

	            @Override
	            public void onCheckedChanged(CompoundButton buttonView,
	                boolean isChecked) {
	              ListModel element = (ListModel) viewHolder.checkbox
	                  .getTag();
	              element.setSelected(buttonView.isChecked());

	            }
	          });
	      viewHolder.image= (ImageView)view.findViewById(R.id.thumbnail);
	      view.setTag(viewHolder);
	      viewHolder.checkbox.setTag(list.get(position));
	    } else {
	      view = convertView;
	      ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
	    }
	    ViewHolder holder = (ViewHolder) view.getTag();
	    holder.image.setImageResource(context.getResources().getIdentifier("com.sidak.tagbinGail:drawable/"+list.get(position).getImage(), null, null));
	    holder.text.setText(list.get(position).getName());
	    holder.checkbox.setChecked(list.get(position).isSelected());
	    return view;
	}
}
