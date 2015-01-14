package com.example.main.monitorapp;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.main.monitor.xml.CurrentTime;
import com.example.monitorapp.MusicService;
import com.example.monitorapp.R;

@SuppressLint("NewApi")
public class HttpURLActivity extends Activity {
	
	private static final int TEXT = 1;
	
	private static final int FLESH_AUTO = 2;
	
	private TextView tvIE;
	
	private static Timer refleshTimer;
	
	private TimerTask mTimerTask;

	private static boolean stop = false;

	private Handler handler;

	private String content;

	private NotificationManager notificationManager;

	private Intent intent;
	
	private boolean reflesh = false;
	
	private CurrentTime st = new CurrentTime();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		intView();
		intent = new Intent(HttpURLActivity.this, MusicService.class);
		timer();
		handler();
	}

	public void handler() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case TEXT:
					tvIE.setText(content);
					break;
				case FLESH_AUTO:
					update();
					break;
				default:
					break;
				}
			}
		};
	}

	private void setupViews(String content) {
		String service = Context.NOTIFICATION_SERVICE;
		notificationManager = (NotificationManager) getSystemService(service);
		Notification notification = new Notification();
		notification.icon = R.drawable.listen;
		notification.tickerText = "Notification";
		notification.when = System.currentTimeMillis();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		Intent intent = new Intent(HttpURLActivity.this, HttpURLActivity.class);
		PendingIntent pi = PendingIntent.getActivity(HttpURLActivity.this, 0, intent, 0);
		notification.setLatestEventInfo(HttpURLActivity.this, "游戏服务监控", content, pi);
		notificationManager.notify(1, notification);
	}

	public void timer() {
		if(refleshTimer != null) {
			return;
		}
		refleshTimer = new Timer();
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				long strr = st.checkTime(HttpURLActivity.this, st);
				if (strr >= 600000) {
					st.saveCurrentTime(HttpURLActivity.this, st);
					handler.sendEmptyMessage(FLESH_AUTO);
				}
			}
		};
		refleshTimer.schedule(mTimerTask, 1000, 1000);
	}

	public void intView() {
		tvIE = (TextView) findViewById(R.id.tv_ie);
	}

	public void update() {
		reflesh = checkNetworkInfo();
		reflesh = HttpUtils.sendGet();
		if (reflesh) {
			content = HttpUtils.linkService();
			if (!content.equals("业务服务器不正常")) {
				if(!stop) {	
					startService(intent);
				}
			}
			tvIE.setText(content);
			setupViews(content);
		} else {
			setupViews("打开警报");
		}
	}
	
	public void reFresh(View v){
		update();
	}
	
	public void setMusic(View v) {
		if(stop) {
			startService(intent);
			stop = false;
			((Button)this.findViewById(R.id.sp_play)).setText("关闭警报");
		} else {
			stopService(intent);
			stop = true;
			((Button)this.findViewById(R.id.sp_play)).setText("打开警报");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private boolean checkNetworkInfo() {
		ConnectivityManager conMan = (ConnectivityManager)this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		State gprs = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			return true;
		}
		if (gprs == State.CONNECTED || gprs == State.CONNECTING) {
			return true;
		}
		else{
			return false;
		}
	}
}
