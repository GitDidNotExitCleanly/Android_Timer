package com.app.stopwatch;

import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	// broadcast receiver required action
	public final static String UPDATE_UI = "com.app.stopwatch.action.UPDATE_UI";
	public final static String RESET_ALL = "com.app.stopwatch.action.RESET_ALL";
	public final static String RESET_LAPTIME = "com.app.stopwatch.action.RESET_LAPTIME";
	
	// local broadcast receiver
	private BroadcastReceiver receiver;
	
	// notification
	private NotificationManager nm;
	private NotificationCompat.Builder notificationBuilder;
	private final int notificationID = 1;
	private boolean running;
	
	// widgets
	private TextView largeTime;
	private TextView smallTime;
	
	private Button start_stop;
	private Button reset_lap;
	
	private ListView listView;
	private TimerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// set up UI and listener
		setupUI();
		
		// create broadcast receiver
		createBroadcastReceiver();
		
		// set up notification
		createNotification();
	}
	
	private void setupUI() {
		
		// textview
		largeTime = (TextView)findViewById(R.id.largeTime);
		smallTime = (TextView)findViewById(R.id.smallTime);
		
		// start/stop button
		start_stop = (Button)findViewById(R.id.start_stop);
		start_stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String btnText = start_stop.getText().toString();
				if (btnText.equals("Start")) {
					// start service
					Intent intent = new Intent(MainActivity.this,TimerService.class);
					intent.setAction(TimerService.UPDATE_TIME);
					startService(intent);
					
					// change running state
					running = true;
					
					// change button
					start_stop.setText("Stop");
					reset_lap.setEnabled(true);
					reset_lap.setText("Lap");
				}
				else {
					// start service
					Intent intent = new Intent(MainActivity.this,TimerService.class);
					intent.setAction(TimerService.STOP_UPDATE_TIME);
					startService(intent);
					
					// change running state
					running = false;
					
					// change button
					start_stop.setText("Start");
					reset_lap.setEnabled(true);
					reset_lap.setText("Reset");
				}
			}
		});
		
		// reset/lap button
		reset_lap = (Button)findViewById(R.id.reset_lap);
		reset_lap.setEnabled(false);
		reset_lap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String btnText = reset_lap.getText().toString();
				if (btnText.equals("Reset")) {
					// start service
					Intent intent = new Intent(MainActivity.this,TimerService.class);
					intent.setAction(TimerService.RESET_TIME);
					startService(intent);
					
					// change button
					reset_lap.setEnabled(false);
				}
				else {
					// start service
					if (adapter.getCount() < 20) {
						Intent intent = new Intent(MainActivity.this,TimerService.class);
						intent.setAction(TimerService.LAP);
						startService(intent);
						
						if (adapter.getCount() == 19) {
							reset_lap.setEnabled(false);
						}
					}
				}
			}
		});
		
		// listview
		listView = (ListView)findViewById(R.id.timeList);
		adapter = new TimerAdapter(this);
		listView.setAdapter(adapter);
	}
	
	// update time
	private void createBroadcastReceiver() {
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context ctx, Intent intent) {
				
				String action = intent.getAction();
				
				if (action.equals(MainActivity.UPDATE_UI)) {
					String totalTime = intent.getStringExtra("totalTime");
					largeTime.setText(totalTime);
					
					String lapTime = intent.getStringExtra("lapTime");
					smallTime.setText(lapTime);
				}
				else {
					if (action.equals(MainActivity.RESET_ALL)) {
						largeTime.setText("00:00.0");
						smallTime.setText("00:00.0");
						adapter.clearTime();
					}
					else {
						smallTime.setText("00:00.0");
						String lapTime = intent.getStringExtra("lapTime");
						adapter.appendTime(lapTime);
					}
				}
			}
		};
	}
	
	private void createNotification() {
		running = false;
		nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notificationBuilder = new NotificationCompat.Builder(this);
		notificationBuilder.setContentTitle("StopWatch In Operation ...");
		notificationBuilder.setContentText("Click to open StopWatch");
		notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
		notificationBuilder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(MainActivity.this, MainActivity.class), 0));
	}
	
	// if activity is destroyed, stop service
	protected void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent(this,TimerService.class);
		stopService(intent);
	}
	
	// when app is visible, register broadcast receiver
	protected void onStart() {
		super.onStart();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(UPDATE_UI);
		intentFilter.addAction(RESET_ALL);
		intentFilter.addAction(RESET_LAPTIME);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver,intentFilter);
		
		if (running == true) {
			// cancel notification
			nm.cancel(notificationID);
		}
	}
	
	// when app is invisible, unregister receiver
	protected void onStop() {
		super.onStop();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		
		if (running == true) {
			// set notification
			nm.notify(notificationID, notificationBuilder.build());
		}
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
