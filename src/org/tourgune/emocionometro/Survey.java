package org.tourgune.emocionometro;

import java.util.Date;

import org.tourgune.emocionometro.main;
import org.tourgune.emocionometro.dao.SurveyDAO;
import org.tourgune.emocionometro.preferences.DefaultPreferences;
import org.tourgune.emocionometro.preferences.PreferencesUtil;
import org.tourgune.emocionometro.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Survey extends Activity implements OnClickListener,
		OnSeekBarChangeListener, OnLongClickListener {
	
	private ImageView titleImage;
	private static final int titleImageId = R.id.emotionsurveyTitleImage;
	
	private Button submitButton;
	private static final int submitButtonId = R.id.emotionsurveySubmitButton;

	private String title1Text;
	private TextView titleArousal;
	private static final int titleArousalId = R.id.emotionsurveyTitle1;
	
	private SeekBar seekBarArousal;
	private static final int seekBarArousalId = R.id.emotionsurveySeekBarArousal;

	private SeekBar historySeekBarArousal;
	private static final int historySeekBarArousalId = R.id.emotionsurveyHistorySeekbarArousal;

	private String title2Text;
	private TextView titlePleasure;
	private static final int titlePleasureId = R.id.emotionsurveyTitle2;
	
	private SeekBar seekBarPleasure;
	private static final int seekBarPleasureId = R.id.emotionsurveySeekBarPleasure;

	private SeekBar historySeekBarPleasure;
	private static final int historySeekBarPleasureId = R.id.emotionsurveyHistorySeekbarPleasure;

	private String title3Text;
	private TextView titleDominance;
	private static final int titleDominanceId = R.id.emotionsurveyTitle3;
	
	private SeekBar seekBarDominance;
	private static final int seekBarDominanceId = R.id.emotionsurveySeekBarDominance;

	private SeekBar historySeekBarDominance;
	private static final int historySeekBarDominanceId = R.id.emotionsurveyHistorySeekbarDominance;


	private int seekBarStartingValueArousal = 0;
	private int seekBarStartingValuePleasure = 0;
	private int seekBarStartingValueDominance = 0;

	private int surveyId;
	private long startTime;
	private long reactionTime;

	// private Drawable seekBarGreenThumb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emotionsurvey);
		// setContentView(R.layout.survey);
		initVariables();
//		setTextFont();
		setLanguage();
		listeners();
		alertUser();
		alertUserTimer();
		
	}

	public void initVariables() {

		if (PreferencesUtil.getSingleSurveyStartingTime(this) == 0) {
			startTime = new Date().getTime();
			PreferencesUtil.setSingleSurveyStartingTime(this, startTime);
		} else {
			startTime = PreferencesUtil.getSingleSurveyStartingTime(this);
		}

		surveyId= this.getIntent().getIntExtra("ID", 0);
		
		titleImage= (ImageView) findViewById(titleImageId);
		
		submitButton = (Button) findViewById(submitButtonId);
//		submitButton.setEnabled(false);

		
		titleArousal=(TextView) findViewById(titleArousalId);
		
			
		seekBarArousal = (SeekBar) findViewById(seekBarArousalId);
		historySeekBarArousal = (SeekBar) findViewById(historySeekBarArousalId);

		titlePleasure=(TextView) findViewById(titlePleasureId);
		
		seekBarPleasure = (SeekBar) findViewById(seekBarPleasureId);
		historySeekBarPleasure = (SeekBar) findViewById(historySeekBarPleasureId);

		titleDominance=(TextView) findViewById(titleDominanceId);
		
		seekBarDominance = (SeekBar) findViewById(seekBarDominanceId);
		historySeekBarDominance = (SeekBar) findViewById(historySeekBarDominanceId);
	
		seekBarStartingValueArousal = SurveyDAO.instance(this).getLastSurvey().getValueArousal();
		historySeekBarArousal.setProgress(seekBarStartingValueArousal);
		
		seekBarStartingValuePleasure = SurveyDAO.instance(this).getLastSurvey().getValuePleasure();
		historySeekBarPleasure.setProgress(seekBarStartingValuePleasure);
		
		seekBarStartingValueDominance = SurveyDAO.instance(this).getLastSurvey().getValueDominance();
		historySeekBarDominance.setProgress(seekBarStartingValueDominance);
		
		// seekBarGreenThumb = ((ImageView)
		// findViewById(R.id.emotionsurveygreenthumb))
		// .getDrawable();

	}

public void setLanguage() {
	if (PreferencesUtil.getLanguageCode(this)==1){//euskera, cambiar textos
		titleArousal.setText("Aktibazioa");
		titlePleasure.setText("Alaitasuna");
		titleDominance.setText("Burujabetasuna");
		
		submitButton.setText("Eginda");
		
		titleImage.setImageResource(R.drawable.surveytitleeus);
	}
	
	title1Text=titleArousal.getText().toString();
	title2Text=titlePleasure.getText().toString();
	title3Text=titleDominance.getText().toString();
}

	public void listeners() {

		// se da la opción de utilizar un longcliker en vez de uno normal
		if (PreferencesUtil.getUseLongClick(this)) {
			submitButton.setOnLongClickListener(this);
		} else {
			submitButton.setOnClickListener(this);
		}
		
		seekBarArousal.setOnSeekBarChangeListener(this);
		seekBarPleasure.setOnSeekBarChangeListener(this);
		seekBarDominance.setOnSeekBarChangeListener(this);
	}

	// //vibrar movil, encender pantalla...
	public void alertUser() {
		long[] pattern={0,200,200,200,200,500};
		((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(pattern,-1);
	}

	public void onClick(View v) {

		switch (v.getId()) {

		case submitButtonId:

			surveyAnswered();
			submitValue();

			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {

		case submitButtonId:
			submitValue();
			break;
		}

		return false;
	}

	public void submitValue() {

		int valueArousal = seekBarArousal.getProgress();
		int valuePleasure = seekBarPleasure.getProgress();
		int valueDominance = seekBarDominance.getProgress();

		reactionTime = new Date().getTime() - startTime;
	
		int progressArousal = getProgress(valueArousal);
		int progressPleasure = getProgress(valuePleasure);
		int progressDominance = getProgress(valueDominance);
		
		mainAct.start(progressArousal,progressPleasure, progressDominance);
			
		TimerUtil timer = new TimerUtil(this);

		SurveyDAO.instance(this).insertNewSurvey(surveyId, main.usuario, valueArousal, valuePleasure, valueDominance, new Date().getTime(), reactionTime);
		if (Utils.isOnline(getApplicationContext())){
				Utils.sendDatabaseEmotions(getApplicationContext());
		
		}
		if (timer.programNextAlarm()==0){
			showGratitudeMessage();
		}
		else{
			finish();
		}
	}

	//TODO terminar mensaje
	public void showGratitudeMessage(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Gracias por participar");

		builder.setPositiveButton(R.string.accept,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		
		builder.show();
	}
	
	public void surveyAnswered(){
		PreferencesUtil.setIsSurveyAnswered(this,true);
	}
	
	public void alertUserTimer() {
		AnswerCounter counter = new AnswerCounter(this,DefaultPreferences.alertingFrequency, DefaultPreferences.alertingFrequency);
		counter.start();
	}
	
	class AnswerCounter extends CountDownTimer {

		public Context mContext;
		
		public AnswerCounter(Context mContext,long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			this.mContext=mContext;
		}

		@Override
		public void onFinish() {
			
			if (!PreferencesUtil.isSurveyAnswered(mContext)){
				alertUser();
				this.start();
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {

		}
	}
	
	// /Con esta llamada conseguimos encerrar al usuario
	// /no podrá salir pulsando atrás, tendrá que pulsar el botón finish
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showToast("Rellena antes el cuestionario", 0);
			return true;

		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			showToast("Rellena antes el cuestionario", 0);
			return true;
		} else {
			super.onKeyDown(keyCode, event);
		}
		return false;
	}

	public void showToast(String text, int duration) {
		Toast subscribedToast = new Toast(this);
		subscribedToast = Toast.makeText(this, text, duration);
		subscribedToast.setGravity(Gravity.CENTER, 0, 0);
		subscribedToast.show();
	}

	public String getRealValue(int value) {

		float realVal = new Float(value);

		realVal -= 50;
		realVal = realVal / 10;

		if (realVal < 0) {
			return Float.toString(realVal);
		} else {
			return "+" + Float.toString(realVal);
		}

	}



	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

		surveyAnswered();

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

		// /si se requiere un valor discreto se redondeará el valor obtenido
		
		String seekbarPreciseness=PreferencesUtil.getSeekBarPreciseness(this);
		
		if (seekbarPreciseness.equals("1/10")){
			//no hacemos nada, la resolución es máxima
		}
		else if (seekbarPreciseness.equals("1/2")){
			//multiplicamos por dos, para luego dividirlo otra vez, de esta forma se redondeará a medios en vez de a enterps
			float discreteValue = seekBar.getProgress()*2;
			discreteValue = (Math.round(discreteValue / 10) * 10)/2;

			seekBar.setProgress((int) discreteValue);
		}
		else if (seekbarPreciseness.equals("1")){
			float discreteValue = seekBar.getProgress();
			discreteValue = Math.round(discreteValue / 10) * 10;

			seekBar.setProgress((int) discreteValue);
			int progress = getProgress(seekBar.getProgress());
			
			switch (seekBar.getId()) {
			
			case seekBarArousalId:

				titleArousal.setText(title1Text+": " + progress);

				break;
				
			case seekBarPleasureId:

				titlePleasure.setText(title2Text+": " + progress);

				break;
				
			case seekBarDominanceId:

				titleDominance.setText(title3Text+": " + progress);

				break;
			}
		}	
		

	}
	
	
	public int getProgress(int value){
		
		int progress = 0;
		
		if (value < 20)
			progress = 1;
		else if (value >= 20 && value<40)
			progress = 2;
		else if (value >= 40 && value<60)
			progress = 3;
		else if (value >= 60 && value<80)
			progress = 4;
		else if (value >= 80 )
			progress = 5;
		
		return progress;
	}
	

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

}