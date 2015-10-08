package org.tourgune.emocionometro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.tourgune.emocionometro.dao.SurveyDAO;
import org.tourgune.emocionometro.preferences.PreferencesUtil;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

public class TimerUtil {

	private Context mContext;// context de main, necesario para ciertas
	// operaciones de sistema
	private AlarmManager alarm;// alarma utilizada
	private PendingIntent pending;// intent preparado para ser lanzado por la
	// alarma
	private final long alarmInterval;

	private final boolean isAlarmRandom;
	private final long alarmRandomLapse;

	// 10000;
	// AlarmManager.INTERVAL_HOUR;// intervalo
	// de tiempo
	// entre
	// alarmas

	public TimerUtil(Context mContext) {
		this.mContext = mContext;
		alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

		alarmInterval = PreferencesUtil.getSurveyPeriod(mContext);
		isAlarmRandom = PreferencesUtil.getIsSurveyRandom(mContext);
		alarmRandomLapse = PreferencesUtil.getSurveyRandomLapse(mContext);
	}

	// esta función (llamada desde el menú principal), se encargará de iniciar
	// la alarma de forma regular o aleatoria, según se haya configurado
	public void startAlarm() {

		showNotificationIcon();
		
		if (PreferencesUtil.getIsSurveyLimitedInTime(mContext)){
			PreferencesUtil.setStartingDate(mContext, new Date().getTime());
		}
		
		long alarmTime=0;

		if (isAlarmRandom) {
			System.out.println("Programming random alarm");
			alarmTime=startRandomAlarmEvent();
		} else {
			System.out.println("Programming regular alarm");
			alarmTime=startRegularAlarmEvent();
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		
		System.out.println("Alarm will sound on "+formatter.format(alarmTime));
		
		PreferencesUtil.setSingleSurveyStartingTime(mContext,0);
		
		PreferencesUtil.setNextAlarm(mContext, formatter.format(alarmTime));
	}

	public long programNextAlarm() {
		
		long alarmTime=0;
		
		if (isAlarmRandom) {
			System.out.println("Programming random alarm");
			alarmTime=startRandomAlarmEvent();
		} else {
			System.out.println("Programming regular alarm");
			alarmTime=nextRegularAlarmEvent();
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		
		System.out.println("Alarm will sound on "+formatter.format(alarmTime));
		System.out.println(alarmTime);
		
		PreferencesUtil.setSingleSurveyStartingTime(mContext,0);
		
		PreferencesUtil.setNextAlarm(mContext, formatter.format(alarmTime));
		
		return alarmTime;
	}

	// /programará la alarma para que suene de forma regular
	private long startRegularAlarmEvent() {
		
		long setAlarm=0;

		Intent intent = new Intent(mContext, AlarmReceiver.class);
		pending = PendingIntent.getBroadcast(mContext, Constants.alarmID,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		alarm.cancel(pending);
		long interval = alarmInterval;// milliseconds
		// alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock
		// .elapsedRealtime()+interval, pending);
//		long alarmHour = SystemClock.elapsedRealtime() + interval;
		long alarmHour = PreferencesUtil.getEmotionStartDate();//new Date().getTime() + interval;

		int alarmEvalResult=isThisAlarmValid(alarmHour);
		
		if (alarmEvalResult==0) {
			alarm.set(AlarmManager.RTC_WAKEUP, alarmHour, pending);
			setAlarm=alarmHour;
			
		} else if (alarmEvalResult==1){
			System.out.println("regular alarm is out of bounds");
			
			alarmHour=generateNextValidAlarm(interval);
			
			if (isThisAlarmValid(alarmHour) == 0) {
				
//				if (isAlarmInAGap(alarmHour)){
//					alarmHour=generateAlarmAfterTheGap(alarmHour);
//				}
				
				alarm.set(AlarmManager.RTC_WAKEUP, alarmHour, pending);
				setAlarm = alarmHour;
			} else {
				stopAlarmEvent();
			}
		}
		else{
			stopAlarmEvent();
		}
		
		return setAlarm;
		// SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		// String hourToshow = formatter.format(new Date().getTime());
		// System.out.println("starting time: " + hourToshow);
		//
		// showNotificationIcon();
	}
	
private long nextRegularAlarmEvent() {
		
		long setAlarm=0;

		Intent intent = new Intent(mContext, AlarmReceiver.class);
		pending = PendingIntent.getBroadcast(mContext, Constants.alarmID,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		alarm.cancel(pending);
		long interval = alarmInterval;// milliseconds

		long alarmHour = new Date().getTime() + interval;

		int alarmEvalResult=isThisAlarmValid(alarmHour);
		
		if (alarmEvalResult==0) {
							
//				if (isAlarmInAGap(alarmHour)){
//					alarmHour=generateAlarmAfterTheGap(alarmHour);
//				}
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy;HH:mm");
				
				System.out.println("Setting Alarm "+formatter.format(alarmHour));
				
			alarm.set(AlarmManager.RTC_WAKEUP, alarmHour, pending);
			setAlarm=alarmHour;
			
		} else if (alarmEvalResult==1){
			System.out.println("regular alarm is out of bounds");
			
			alarmHour=generateNextValidAlarm(interval);
			
			if (isThisAlarmValid(alarmHour) == 0) {
				
//				if (isAlarmInAGap(alarmHour)){
//					alarmHour=generateAlarmAfterTheGap(alarmHour);
//				}
				
				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
				
				System.out.println("Setting Alarm "+formatter.format(alarmHour));
				
				alarm.set(AlarmManager.RTC_WAKEUP, alarmHour, pending);
				setAlarm = alarmHour;
				
			} else {
				stopAlarmEvent();
			}
		}
		else{
			stopAlarmEvent();
		}
		
		return setAlarm;

	}

	// /programará la alarma para que suene de forma aleatoria pero con un
	// intervalo regular
	private long startRandomAlarmEvent() {
		
		long setAlarm=0;

		Intent intent = new Intent(mContext, AlarmReceiver.class);
		pending = PendingIntent.getBroadcast(mContext, Constants.alarmID,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		alarm.cancel(pending);
		Float randomHour = alarmRandomLapse * getRandomTime();// milliseconds

//		long alarmHour = SystemClock.elapsedRealtime() + alarmInterval
//				+ randomHour.longValue();

		long alarmHour = PreferencesUtil.getEmotionStartDate()//new Date().getTime()
		+ alarmInterval
		+ randomHour.longValue();
		
		int alarmEvalResult=isThisAlarmValid(alarmHour);
		System.out.println("alarme eval result:"+alarmEvalResult);
		if (alarmEvalResult==0) {

			System.out.println("random alarm will sound in the next ms:"
					+ (alarmInterval + randomHour.longValue()));
			alarm.set(AlarmManager.RTC_WAKEUP, alarmHour, pending);
			setAlarm=alarmHour;
			
		} else if (alarmEvalResult == 1) {
			System.out.println("random alarm is out of bounds");
			
			alarmHour = generateNextValidAlarm(alarmInterval + randomHour.longValue());
			//puede que se haya pasado la alarma al día siguiente así que hay que reevaluarla
			if (isThisAlarmValid(alarmHour) == 0) {
				
//				if (isAlarmInAGap(alarmHour)){
//					alarmHour=generateAlarmAfterTheGap(alarmHour);
//				}
//				
				alarm.set(AlarmManager.RTC_WAKEUP, alarmHour, pending);
				setAlarm = alarmHour;
			} else {
				stopAlarmEvent();
			}
		}
		else{
			stopAlarmEvent();
		}
		
		return setAlarm;


	}

	// devolverá una hora con el intervalo especificado con un factor de
	// aleatoriedad
	private Float getRandomTime() {

		Random rnd = new Random();
		Float rndNumber = new Float(rnd.nextInt() / 1000000);
		System.out.println("random number:" + rndNumber);
		Float max = new Float(Integer.MAX_VALUE / 1000000);
		System.out.println("max integer:" + max);
		Float rndResult = (rndNumber / max);

		System.out.println("random number was:" + rndResult);

		return rndResult;

	}

	
	//0= alarm is valid
	//1= alarm is invalid, the next alarm has to be arranged for the following morning
	//-1 alarm has expired
	private int isThisAlarmValid(long alarmTime) {

		if (!PreferencesUtil.getIsProgramRunning(mContext))
			{
			System.out.println("program wasn't running");
			return -1;
			
			}
		if (PreferencesUtil.getIsSurveyLimitedInTime(mContext)) {

			long timeForExpiration=PreferencesUtil.getEmotionEndDate();//PreferencesUtil.getStartingDate(mContext)+(PreferencesUtil.getSurveyDayNumber(mContext)* 86400000);
			
			if (alarmTime>=timeForExpiration){
				System.out.println("alarmTime>timeForExpire");
				return -1;
			}
			
			try {

				Calendar alarmCal = Calendar.getInstance();
				alarmCal.setTime(new Date(alarmTime));

				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

				Calendar startCal = Calendar.getInstance();

				startCal.setTime(formatter.parse(PreferencesUtil
						.getSurveyStartingTime(mContext)));

				Calendar endCal = Calendar.getInstance();
				endCal.setTime(formatter.parse(PreferencesUtil
						.getSurveyEndingTime(mContext)));

				// tenemos las 3 horas en formato Calendar,usando las funciones
				// asociadas obtendremos los distintos componenentes

				int startTimeMinutes = startCal.get(Calendar.MINUTE);
				int endTimeMinutes = endCal.get(Calendar.MINUTE);
				int alarmTimeMinutes = alarmCal.get(Calendar.MINUTE);

				int startTimeHours = startCal.get(Calendar.HOUR_OF_DAY);
				int endTimeHours = endCal.get(Calendar.HOUR_OF_DAY);
				int alarmTimeHours = alarmCal.get(Calendar.HOUR_OF_DAY);
				
				System.out.println("start Hour:"+startTimeHours+":"+ startTimeMinutes);
				System.out.println("alarm Hour:"+alarmTimeHours+":"+ alarmTimeMinutes);
				System.out.println("end Hour:"+endTimeHours+":"+ endTimeMinutes);
			
				//3 posibilidades: entre los rangos de horas, misma hora que inicio pero posterior en minutos, y misma hora que final pero anterior en minutos
				if(startTimeHours<alarmTimeHours&&alarmTimeHours<endTimeHours){
					return 0;
				}
				else if((startTimeHours==alarmTimeHours)&&(startTimeMinutes<alarmTimeMinutes)){
					return 0;
				}
				else if((endTimeHours==alarmTimeHours)&&(alarmTimeMinutes<endTimeMinutes)){
					return 0;
				}
				else
					return 1;
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return 0;

	}
	
	///se generará una fecha para el día siguiente con la hora incial+intervalo de alarma +1 día
	private long generateNextValidAlarm(long interval){
		Calendar alarmCal = Calendar.getInstance();
		alarmCal.setTime(new Date());
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		Calendar startCal = Calendar.getInstance();
		try {
			startCal.setTime(formatter.parse(PreferencesUtil
					.getSurveyStartingTime(mContext)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int dayOfYear;
		
		System.out.println("Alarm day of the year:"+alarmCal.get(Calendar.DAY_OF_YEAR));
		
		if (alarmCal.get(Calendar.DAY_OF_YEAR)==365)
			dayOfYear=1;
		else 
			dayOfYear=alarmCal.get(Calendar.DAY_OF_YEAR)+1;
		
		alarmCal.set(Calendar.DAY_OF_YEAR,dayOfYear); 
		alarmCal.set(Calendar.HOUR_OF_DAY, startCal.get(Calendar.HOUR_OF_DAY));
		alarmCal.set(Calendar.MINUTE, startCal.get(Calendar.MINUTE));
		
		System.out.println("Next alarm configured for day, hour: "+alarmCal.get(Calendar.DAY_OF_YEAR)+","+alarmCal.get(Calendar.HOUR_OF_DAY));
		
		formatter = new SimpleDateFormat("dd/MM/yyyy;HH:mm");
		System.out.println(formatter.format(alarmCal.getTime()));
		return alarmCal.getTimeInMillis()+interval;
		
	}
	

	public void stopAlarmEvent() {

		System.out.println("Stopping ");
		Intent intent = new Intent(mContext, AlarmReceiver.class);
		pending = PendingIntent.getBroadcast(mContext, Constants.alarmID,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		alarm.cancel(pending);
		PreferencesUtil.setIsProgramRunning(mContext, false);
		removeNotificationIcon();
		callCommitSurveys();		
	}
	
	public void callCommitSurveys(){
		String commitResult=SurveyDAO.instance(mContext).backupSurveysToSd(mContext,"");
		showToast(commitResult, 0);
	}
	
	public void showToast(String text, int duration) {
		Toast subscribedToast = new Toast(mContext);
		subscribedToast = Toast.makeText(mContext, text, duration);
		subscribedToast.setGravity(Gravity.CENTER, 0, 0);
		subscribedToast.show();
	}

	public void showNotificationIcon() {

//		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//		String hourToshow = formatter.format(new Date().getTime()
//				+ alarmInterval);

		CharSequence tickerText ="";
//			mContext.getString(R.string.notiMessage);
//				+ hourToshow;

		long when = System.currentTimeMillis();

		Notification notification = new Notification(1, tickerText, when);

		notification.flags = Notification.FLAG_NO_CLEAR;

		notification.icon = R.drawable.dss2016;

		Intent notificationIntent = new Intent(mContext, main.class);

		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(mContext, mContext
				.getString(R.string.app_name),
//				mContext.getString(R.string.notiMessage)
//				+ hourToshow
				"", contentIntent);

		((NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
				Constants.notiID, notification);
	}

	public void removeNotificationIcon() {
		((NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE))
				.cancel(Constants.notiID);
	}
	
}