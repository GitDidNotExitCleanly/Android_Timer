package com.app.stopwatch;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class TimerService extends Service {
	
	public final static String UPDATE_TIME = "com.app.stopwatch.action.UPDATE_TIME";
	
	public class TimerBinder extends Binder {
		TimerService getService() {
			return TimerService.this;
		}
	}
	
	private IBinder timerBinder;
	
	private LocalBroadcastManager broadcaster;
	
	public TimerService() {
		timerBinder = new TimerBinder();
		broadcaster = LocalBroadcastManager.getInstance(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return timerBinder;
	}
	
	private void updateTime(String message) {
		Intent intent = new Intent();
		intent.setAction(UPDATE_TIME);
		
		// append data
		
		
		broadcaster.sendBroadcast(intent);
	}
	/*********************************************/
	// public functions for activity
	
	
	
}
