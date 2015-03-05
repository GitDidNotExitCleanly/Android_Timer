package com.app.stopwatch;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	private BroadcastReceiver receiver;
	
	private TextView largeTime;
	private TextView smallTime;
	
	private Button start_stop;
	private Button reset_lap;
	
	
	private TimerService service = null;
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = ((TimerService.TimerBinder)binder).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			service = null;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// set up UI and listener
		setupUI();
		
		// create broadcast receiver
		createBroadcastReceiver();
		
		// bind service
		Intent intent = new Intent(this,TimerService.class);
		bindService(intent,conn,Service.BIND_AUTO_CREATE);
	}
	
	private void setupUI() {
		
		largeTime = (TextView)findViewById(R.id.largeTime);
		smallTime = (TextView)findViewById(R.id.smallTime);
		
		start_stop = (Button)findViewById(R.id.start_stop);
		start_stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String btnText = start_stop.getText().toString();
				if (btnText.equals("Start")) {
					
					
					start_stop.setText("Stop");
					reset_lap.setClickable(true);
					reset_lap.setText("Lap");
				}
				else {
					
					
					start_stop.setText("Start");
					reset_lap.setText("Reset");
				}
			}
		});
		
		reset_lap = (Button)findViewById(R.id.reset_lap);
		reset_lap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String btnText = reset_lap.getText().toString();
				if (btnText.equals("Reset")) {
					
					reset_lap.setClickable(false);
				}
				else {
					
					
				}
			}
		});
	}
	
	private void createBroadcastReceiver() {
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context ctx, Intent intent) {
				
			}
		};
	}
	
	protected void onDestroy() {
		if (service != null) {
			unbindService(conn);
		}
	}
	
	protected void onStart() {
		super.onStart();
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(TimerService.UPDATE_TIME));
	}
	
	protected void onStop() {
		super.onStop();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
