package org.tourgune.emocionometro;

public class Constants{
	
//status bar notification ID, no puede coincidir con ninguna otra de otro app en funcionamiento
	public static final int notiID=1234;
	
	//request code del registro de la alarma
	public static final int alarmID=4321;
	
	//ID de las opciones  de menu secundario del menú principal
	public static final int CONFIG_EDIT=4567;
	public static final int CONFIG_HISTORY=7654;
	public static final String CONFIG_PASSWORD="1234";
	
	////preferences file name
	public static final String preferences="surveyPreferences";
	//Preferences parameters IDs
	public static final String PROGRAM_RUNNING="isProgramRunning";
	
	public static final String USER_ID="user";
	public static final String SEEKBARMIN="seekBarMinimumMovement";
	public static final String LONGCLICK_NEEDED="longClickNeeded";
	public static final String SEEKBAR_PRECISENESS="seekBarPreciseness";
	public static final String DOES_SEEKBAR_START_IN_ZERO="doesSeekbarStartInZero";
	public static final String HAS_SEEKBAR_MEMORY="hasSeekbarMemory";
	public static final String IS_SEEKBAR_RANDOM="isSurveyRandom";
	public static final String SURVEY_RANDOM_LAPSE="surveyRandomLapse";
	public static final String SURVEY_PERIOD="surveyPeriod";
	public static final String IS_SURVEY_LIMITED_IN_TIME="isSurveyLimitedInTime";
	public static final String SURVEY_START_TIME="surveyStartTime";
	public static final String SURVEY_END_TIME="surveyEndTime";
	public static final String SURVEY_DAY_NUMBER="surveyDayNumber";
	public static final String SURVEY_STARTING_DATE="surveyStartingDate";
	
	public static final String SURVEY_NEXT_ALARM="surveyNextAlarm";
	
	
	////servirá para medir el reaction time, no puede ir en survey.java porque se reinicia cada vez que se enciende la pantalla
	public static final String SINGLE_SURVEY_STARTING_TIME="singleSurveyStartingTime";
	
	
	///EMOCIONOMETRO
	
	public static final String IS_SURVEY_ANSWERED="isSurveyAnswered";
	public static final String LANGUAGE_CODE="languageCode";
}