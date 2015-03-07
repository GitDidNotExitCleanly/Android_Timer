package com.example.stopwatch.fragments;

import com.example.stopwatch.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

/*
 * Settings page fragment
 * 
 * @author Sheng Wang (psysw1@nottingham.ac.uk)
 * */

public class SettingsFragment extends Fragment {
	
	private SharedPreferences settings;
	
	// widgets
	private ToggleButton do_not_notify;
	
	public final static int record_number_min = 10;
	public final static int record_number_max = 30;
	private SeekBar record_number_seekbar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_settings, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		do_not_notify = (ToggleButton)getActivity().findViewById(R.id.do_not_notify);
		
		record_number_seekbar = (SeekBar)getActivity().findViewById(R.id.record_number_seekbar);
		
		// get preference
		settings = getActivity().getPreferences(Context.MODE_PRIVATE);
		
		// load data
		loadData();
		
		// set listener
		setListener();
	}
	
	private void loadData() {
		// set toggle button
		boolean do_not_notify = settings.getBoolean("do_not_notify", false);
		this.do_not_notify.setChecked(do_not_notify);
		
		// set seek bar
		int record_number = settings.getInt("record_number", (record_number_max-record_number_min)/2);
		this.record_number_seekbar.setMax(record_number_max-record_number_min);
		this.record_number_seekbar.setProgress(record_number);
	}
	
	// set up listeners for toggle button and seek bar
	private void setListener() {
		this.do_not_notify.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				settings.edit().putBoolean("do_not_notify", isChecked).commit();
			}
			
		});
		
		this.record_number_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int currentProgress = 0;
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				currentProgress = progress;				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				settings.edit().putInt("record_number", currentProgress).commit();
			}
			
		});
	}
}
