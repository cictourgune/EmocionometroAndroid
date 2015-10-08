package org.tourgune.emocionometro;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.tourgune.emocionometro.dao.SurveyDAO;
import org.tourgune.emocionometro.preferences.PreferencesUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String hourToshow = formatter.format(new Date().getTime());
		System.out.println("finished time: " + hourToshow);

		Integer rowId=SurveyDAO.instance(context).getLastId()+1;
		
		callSurveyActivity(context,rowId);
		
	}
	
	public void callSurveyActivity(Context context,int rowId){
		
		PreferencesUtil.setIsSurveyAnswered(context, false);
		
		Intent surveyIntent = new Intent(context, Survey.class);
		
		surveyIntent.putExtra("ID", rowId);
		
		surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(surveyIntent);
	}
	
	//programa la siguiente alarma y muestra la notificación corerspondiente
	public void resetTimer(Context mContext){
		TimerUtil timer=new TimerUtil(mContext);
		
		timer.removeNotificationIcon();
		timer.showNotificationIcon();
		
		timer.programNextAlarm();
	}
}