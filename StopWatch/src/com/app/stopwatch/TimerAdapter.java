package com.app.stopwatch;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TimerAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	
	// data set
	private ArrayList<String> lapTimes;
	
	public TimerAdapter(Context ctx) {
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		lapTimes = new ArrayList<String>();
	}

	@Override
	public int getCount() {
		return lapTimes.size();
	}

	@Override
	public Object getItem(int index) {
		return lapTimes.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = inflater.inflate(R.layout.timer_list_item, parent, false);
		
		TextView tv_index = (TextView)rowView.findViewById(R.id.index);
		TextView tv_lapTime = (TextView)rowView.findViewById(R.id.lapTime);
		
		tv_index.setText("lap "+(position+1));
		tv_lapTime.setText(this.lapTimes.get(position));
		
		return rowView;
	}

	public void appendTime(String time) {
		if (this.lapTimes.size() < 20) {
			this.lapTimes.add(time);
			notifyDataSetChanged();
		}
	}
	
	public void clearTime() {
		if (this.lapTimes.size() > 0) {
			this.lapTimes.clear();
			notifyDataSetChanged();
		}
	}
}
