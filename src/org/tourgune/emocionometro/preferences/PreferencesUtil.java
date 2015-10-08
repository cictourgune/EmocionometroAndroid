package org.tourgune.emocionometro.preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.tourgune.emocionometro.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesUtil {

	private static SharedPreferences getPreferenceFile(Context mContext) {
		return mContext.getSharedPreferences(Constants.preferences, 0);
	}

	public static boolean getIsProgramRunning(Context mContext) {
		return getPreferenceFile(mContext).getBoolean(
				Constants.PROGRAM_RUNNING, false);
	}

	public static void setIsProgramRunning(Context mContext, boolean running) {
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putBoolean(Constants.PROGRAM_RUNNING, running);
		editor.commit();
	}

	public static void setDefaultUserID(Context mContext) {
		if (getPreferenceFile(mContext).getInt(Constants.USER_ID, 0) == 0) {
			Random rnd = new Random();
			int rndNumber = Math.abs(rnd.nextInt());
			setUserId(mContext, rndNumber);
		}
	}

	public static int getUserId(Context mContext) {
		return getPreferenceFile(mContext).getInt(Constants.USER_ID, 0);
	}

	public static void setUserId(Context mContext, int userId) {
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putInt(Constants.USER_ID, userId);
		editor.commit();
	}

	public static int getSeekBarMinimumMovement(Context mContext) {
		return getPreferenceFile(mContext).getInt(Constants.SEEKBARMIN,
				DefaultPreferences.seekbarMovement);
	}

	public static void setSeekBarMinimumMovement(Context mContext,
			int minMovement) {
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putInt(Constants.SEEKBARMIN, minMovement);
		editor.commit();
	}

	public static boolean getUseLongClick(Context mContext) {
		return getPreferenceFile(mContext).getBoolean(
				Constants.LONGCLICK_NEEDED, DefaultPreferences.useLongClick);
	}

	public static void setUseLongClick(Context mContext, boolean isNeeded) {
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putBoolean(Constants.LONGCLICK_NEEDED, isNeeded);
		editor.commit();
	}

	public static String getSeekBarPreciseness(Context mContext) {
		return getPreferenceFile(mContext).getString(
				Constants.SEEKBAR_PRECISENESS,
				DefaultPreferences.seekbarPreciseness);
	}

	public static void setSeekBarPreciseness(Context mContext,
			String preciseness) {
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putString(Constants.SEEKBAR_PRECISENESS, preciseness);
		editor.commit();
	}

	public static boolean getDoesSeekbarStartInZero(Context mContext) {
		return getPreferenceFile(mContext).getBoolean(
				Constants.DOES_SEEKBAR_START_IN_ZERO,
				DefaultPreferences.doesSeekbarStartInZero);
	}

	public static void setDoesSeekbarStartInZero(Context mContext,
			boolean startInZero) {
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putBoolean(Constants.DOES_SEEKBAR_START_IN_ZERO, startInZero);
		editor.commit();
	}

	public static boolean getHasSeekbarMemory(Context mContext) {
		return getPreferenceFile(mContext).getBoolean(
				Constants.HAS_SEEKBAR_MEMORY,
				DefaultPreferences.hasSeekbarMemory);
	}

	public static void setHasSeekbarMemory(Context mContext, boolean hasMemory) {
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putBoolean(Constants.HAS_SEEKBAR_MEMORY, hasMemory);
		editor.commit();
	}

	public static boolean getIsSurveyRandom(Context mContext) {

		System.out.println("Getting is survey random="
				+ getPreferenceFile(mContext).getBoolean(
						Constants.IS_SEEKBAR_RANDOM,
						DefaultPreferences.isSurveyRandom));

		return getPreferenceFile(mContext).getBoolean(
				Constants.IS_SEEKBAR_RANDOM, DefaultPreferences.isSurveyRandom);
	}

	public static void setIsSurveyRandom(Context mContext, boolean isRandom) {

		System.out.println("Setting is survey random=" + isRandom);

		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putBoolean(Constants.IS_SEEKBAR_RANDOM, isRandom);
		editor.commit();
	}

	public static int getSurveyRandomLapse(Context mContext) {

		System.out.println("Getting survey random lapse="
				+ getPreferenceFile(mContext).getInt(
						Constants.SURVEY_RANDOM_LAPSE,
						fromMinToMs(DefaultPreferences.randomLapse)));

		return getPreferenceFile(mContext).getInt(
				Constants.SURVEY_RANDOM_LAPSE,
				fromMinToMs(DefaultPreferences.randomLapse));
	}

	public static void setSurveyRandomLapse(Context mContext, int rndLapse) {

		System.out.println("Setting survey random lapse=" + rndLapse);

		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putInt(Constants.SURVEY_RANDOM_LAPSE, rndLapse);
		editor.commit();
	}

	public static int getSurveyPeriod(Context mContext) {

		System.out.println("Getting survey period="
				+ getPreferenceFile(mContext).getInt(Constants.SURVEY_PERIOD,
						fromMinToMs(DefaultPreferences.surveyPeriod)));

		return getPreferenceFile(mContext).getInt(Constants.SURVEY_PERIOD,
				fromMinToMs(DefaultPreferences.surveyPeriod));
	}

	public static void setSurveyPeriod(Context mContext, int surveyPeriod) {

		System.out.println("Setting survey period=" + surveyPeriod);

		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putInt(Constants.SURVEY_PERIOD, surveyPeriod);
		editor.commit();
	}

	public static boolean getIsSurveyLimitedInTime(Context mContext) {
		return getPreferenceFile(mContext).getBoolean(
				Constants.IS_SURVEY_LIMITED_IN_TIME,
				DefaultPreferences.isLimitedByDateTime);
	}

	public static void setIsSurveyLimitedInTime(Context mContext,
			boolean isLimited) {
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putBoolean(Constants.IS_SURVEY_LIMITED_IN_TIME, isLimited);
		editor.commit();
	}

	public static String getSurveyStartingTime(Context mContext) {

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String startTime;
		try {

			Long time = formatter.parse(DefaultPreferences.startTime).getTime();

			startTime = formatter.format(getPreferenceFile(mContext).getLong(
					Constants.SURVEY_START_TIME, time));

		} catch (ParseException e) {
			startTime = formatter.format(getPreferenceFile(mContext).getLong(
					Constants.SURVEY_START_TIME, 0));
			e.printStackTrace();
		}
		return startTime;
	}

	public static boolean setSurveyStartingTime(Context mContext,
			String startTime) {

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		Long time;
		try {
			time = formatter.parse(startTime).getTime();
			SharedPreferences.Editor editor = getPreferenceFile(mContext)
					.edit();
			editor.putLong(Constants.SURVEY_START_TIME, time);
			editor.commit();
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static String getSurveyEndingTime(Context mContext) {

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String endTime;

		try {
			Long time = formatter.parse(DefaultPreferences.endTime).getTime();

			endTime = formatter.format(getPreferenceFile(mContext).getLong(
					Constants.SURVEY_END_TIME, time));

		} catch (ParseException e) {
			endTime = formatter.format(getPreferenceFile(mContext).getLong(
					Constants.SURVEY_END_TIME, 0));
			e.printStackTrace();
		}
		return endTime;
	}

	public static boolean setSurveyEndingTime(Context mContext, String endTime) {

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		Long time;
		try {
			time = formatter.parse(endTime).getTime();
			SharedPreferences.Editor editor = getPreferenceFile(mContext)
					.edit();
			editor.putLong(Constants.SURVEY_END_TIME, time);
			editor.commit();
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static long getStartingDate(Context mContext) {
		return getPreferenceFile(mContext).getLong(
				Constants.SURVEY_STARTING_DATE, 1);
	}

	public static void setStartingDate(Context mContext, long date) {
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putLong(Constants.SURVEY_STARTING_DATE, date);
		editor.commit();
	}

	public static String getNextAlarm(Context mContext) {
		return getPreferenceFile(mContext).getString(
				Constants.SURVEY_NEXT_ALARM, "NOT SET");
	}

	public static void setNextAlarm(Context mContext, String alarm) {
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putString(Constants.SURVEY_NEXT_ALARM, alarm);
		editor.commit();
	}

	public static float fromMsToMin(int ms) {
		float msAux = (float) ms;

		return (msAux / 60 / 1000);
	}

	public static int fromMinToMs(float min) {

		return (int) (min * 60 * 1000);
	}

	// /////////APPLICATION VARIABLES

	public static void setSingleSurveyStartingTime(Context mContext, long time) {
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putLong(Constants.SINGLE_SURVEY_STARTING_TIME, time);
		editor.commit();
	}

	public static long getSingleSurveyStartingTime(Context mContext) {
		return getPreferenceFile(mContext).getLong(
				Constants.SINGLE_SURVEY_STARTING_TIME, 0);
	}

	// ///EMOCIONOMETRO
	public static long getEmotionStartDate() {
		return getEmotionDate(DefaultPreferences.emotionStartDate);
	}

	public static long getEmotionEndDate() {
		return getEmotionDate(DefaultPreferences.emotionEndDate);
	}

	public static long getEmotionDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy;HH:mm:ss");
		Date date = new Date();

		try {

			date = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date.getTime();
	}

	public static boolean isSurveyAnswered(Context mContext) {
		return getPreferenceFile(mContext).getBoolean(
				Constants.IS_SURVEY_ANSWERED, false);
	}

	public static void setIsSurveyAnswered(Context mContext,
			boolean surveyAnswered) {

		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putBoolean(Constants.IS_SURVEY_ANSWERED, surveyAnswered);
		editor.commit();
	}
	
	//0 Spanish 1 Euskera
	public static int getLanguageCode(Context mContext){
				
		return getPreferenceFile(mContext).getInt(
				Constants.LANGUAGE_CODE, 0);
		
	}
	
	public static void setLanguageCode(Context mContext,int code){
		System.out.println("Language is now "+code);
		SharedPreferences.Editor editor = getPreferenceFile(mContext).edit();
		editor.putInt(Constants.LANGUAGE_CODE, code);
		editor.commit();
	}

}