package com.example.main.monitor.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("SimpleDateFormat")
public class CurrentTime extends Activity {
	
	private Context ctx;
	
	private SharedPreferences sp;
	
	public void saveCurrentTime(Context context, CurrentTime st){
		ctx = context;       
        sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("date", st.getSystemTime());
        editor.commit();
	}
	public String getSystemTime(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate); 
		return str;
	}
	public Long checkTime(Context context, CurrentTime st){
		ctx = context;
		sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
        long diff=0;
        try {
			 diff = formatter.parse(st.getSystemTime()).getTime() 
					- formatter.parse(sp.getString("date", "2014-07-21 03:10:55")).getTime();
			 return diff;
        } catch (ParseException e) {
			e.printStackTrace();
		}
        return diff;
	}
}
