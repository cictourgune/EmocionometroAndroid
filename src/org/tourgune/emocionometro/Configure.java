package org.tourgune.emocionometro;

import org.tourgune.emocionometro.preferences.PreferencesUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Configure extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	// Preferences Form elements

	private EditText newUserText;
	private static final int newUserTextId = R.id.configUserText;

	private EditText seekBarMinmovementText;
	private static final int seekBarMinmovementTextId = R.id.configSeekBarMinMovementText;

	private CheckBox useLongClickChkbx;
	private static final int useLongClickChkbxId = R.id.configUseLongClick;
	
	private Button seekbarPrecisenessButton;
	private static final int seekbarPrecisenessButtonId = R.id.configSeekbarPreciseness;

	private CheckBox doesSeekbarStartInZeroChkbx;
	private static final int doesSeekbarStartInZeroChkbxId = R.id.configDoesSeekbarStartInZero;
	
	private CheckBox hasSeekbarMemoryChkbx;
	private static final int hasSeekbarMemoryChkbxId = R.id.configHasSeekbarMemoryChkbx;

	private CheckBox isSurveyRandomChkbx;
	private static final int isSurveyRandomChkbxId = R.id.configIsSurveyRandomChkbx;

	private TextView surveyRandomLapseTitle;
	private static final int surveyRandomLapseTitleId = R.id.configSurveyRandomLapseTitle;
	
	private EditText surveyRandomLapseText;
	private static final int surveyRandomLapseTextId = R.id.configSurveyRandomLapseText;

	private EditText surveyPeriodText;
	private static final int surveyPeriodTextId = R.id.configSurveyPeriodText;

	private CheckBox isSurveyLimitedInTimeChkbx;
	private static final int isSurveyLimitedInTimeChkbxId = R.id.configIsSurveyLimitedInTime;

	private TextView surveyStartingTimeTitle;
	private static final int surveyStartingTimeTitleId = R.id.configSurveyStartTimeTitle;

	private EditText surveyStartingTimeText;
	private static final int surveyStartingTimeTextId = R.id.configSurveyStartTime;

	private TextView surveyEndinTimeTitle;
	private static final int surveyEndinTimeTitleId = R.id.configSurveyEndTimeTitle;
	
	private EditText surveyEndingTimeText;
	private static final int surveyEndingTimeTextId = R.id.configSurveyEndTime;
	
	private TextView surveyNumberOfDaysTitle;
	private static final int surveyNumberOfDaysTitleId = R.id.configDayNumberTitle;

	 private EditText surveyNumberOfDaysText;
	 private static final int surveyNumberOfDaysTextId = R.id.configDayNumber;

	private Button commitButton;
	private static final int commitButtonId = R.id.configEdituserButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config);

		initVariables();
		listeners();
	}

	public void initVariables() {

		newUserText = (EditText) findViewById(newUserTextId);
		newUserText.setText(Integer.toString(PreferencesUtil.getUserId(this)));

		seekBarMinmovementText = (EditText) findViewById(seekBarMinmovementTextId);
		seekBarMinmovementText.setText(Integer.toString(PreferencesUtil
				.getSeekBarMinimumMovement(this)));
		
		useLongClickChkbx = (CheckBox) findViewById(useLongClickChkbxId);
		useLongClickChkbx.setChecked(PreferencesUtil
				.getUseLongClick(this));

		seekbarPrecisenessButton = (Button) findViewById(seekbarPrecisenessButtonId);
		seekbarPrecisenessButton.setText(PreferencesUtil
				.getSeekBarPreciseness(this));
		
		doesSeekbarStartInZeroChkbx= (CheckBox) findViewById(doesSeekbarStartInZeroChkbxId);
		doesSeekbarStartInZeroChkbx.setChecked(PreferencesUtil
				.getDoesSeekbarStartInZero(this));


		hasSeekbarMemoryChkbx = (CheckBox) findViewById(hasSeekbarMemoryChkbxId);
		hasSeekbarMemoryChkbx.setChecked(PreferencesUtil
				.getHasSeekbarMemory(this));

		isSurveyRandomChkbx = (CheckBox) findViewById(isSurveyRandomChkbxId);
		isSurveyRandomChkbx.setChecked(PreferencesUtil.getIsSurveyRandom(this));

		
		surveyRandomLapseTitle= (TextView) findViewById(surveyRandomLapseTitleId);
		surveyRandomLapseText = (EditText) findViewById(surveyRandomLapseTextId);
		
		surveyRandomLapseText.setText(Float.toString(fromMsToMin(PreferencesUtil
				.getSurveyRandomLapse(this))));

		if (isSurveyRandomChkbx.isChecked())
		{
			surveyRandomLapseTitle.setVisibility(android.view.View.VISIBLE);
			surveyRandomLapseText.setVisibility(android.view.View.VISIBLE);
			
		}
		else{
			surveyRandomLapseTitle.setVisibility(android.view.View.GONE);
			surveyRandomLapseText.setVisibility(android.view.View.GONE);
		}

		surveyPeriodText = (EditText) findViewById(surveyPeriodTextId);
		
		surveyPeriodText.setText(Float.toString(fromMsToMin(PreferencesUtil
				.getSurveyPeriod(this))));

		isSurveyLimitedInTimeChkbx = (CheckBox) findViewById(isSurveyLimitedInTimeChkbxId);
		isSurveyLimitedInTimeChkbx.setChecked(PreferencesUtil
				.getIsSurveyLimitedInTime(this));

		
		surveyStartingTimeTitle=(TextView) findViewById(surveyStartingTimeTitleId);
		
		surveyStartingTimeText = (EditText) findViewById(surveyStartingTimeTextId);
		surveyStartingTimeText.setText(PreferencesUtil
				.getSurveyStartingTime(this));

		surveyEndinTimeTitle=(TextView) findViewById(surveyEndinTimeTitleId);
		
		surveyEndingTimeText = (EditText) findViewById(surveyEndingTimeTextId);
		surveyEndingTimeText.setText(PreferencesUtil.getSurveyEndingTime(this));

		surveyNumberOfDaysTitle=(TextView) findViewById(surveyNumberOfDaysTitleId);
		
		 surveyNumberOfDaysText = (EditText)findViewById(surveyNumberOfDaysTextId);
		 surveyNumberOfDaysText.setText("DEPRECATED");

		if (isSurveyLimitedInTimeChkbx.isChecked()) {
			
			surveyStartingTimeTitle.setVisibility(android.view.View.VISIBLE);
			surveyEndinTimeTitle.setVisibility(android.view.View.VISIBLE);
			surveyNumberOfDaysTitle.setVisibility(android.view.View.VISIBLE);
			
			surveyStartingTimeText.setVisibility(android.view.View.VISIBLE);
			surveyEndingTimeText.setVisibility(android.view.View.VISIBLE);
			surveyNumberOfDaysText.setVisibility(android.view.View.VISIBLE);
		}

		else {
			surveyStartingTimeTitle.setVisibility(android.view.View.GONE);
			surveyEndinTimeTitle.setVisibility(android.view.View.GONE);
			surveyNumberOfDaysTitle.setVisibility(android.view.View.GONE);
			
			surveyStartingTimeText.setVisibility(android.view.View.GONE);
			surveyEndingTimeText.setVisibility(android.view.View.GONE);
			surveyNumberOfDaysText.setVisibility(android.view.View.GONE);
		}

		commitButton = (Button) findViewById(commitButtonId); 

	}
	
	public void listeners() {
		commitButton.setOnClickListener(this);

		seekbarPrecisenessButton.setOnClickListener(this);
		isSurveyRandomChkbx.setOnCheckedChangeListener(this);
		isSurveyLimitedInTimeChkbx.setOnCheckedChangeListener(this);
	}

	public boolean validateAndCommitForm() {

		// /hacemos commit de todos los elementos del formulario
		try {
			int userId = Integer.parseInt(newUserText.getText().toString());
			PreferencesUtil.setUserId(this, userId);
			newUserText.setTextColor(android.graphics.Color.BLACK);
		}

		catch (Exception e) {
			e.printStackTrace();
			newUserText.setTextColor(android.graphics.Color.RED);
			showToast("it must be a number", 0);
			return false;
		}

		try {
			int userId = Integer.parseInt(seekBarMinmovementText.getText()
					.toString());
			PreferencesUtil.setSeekBarMinimumMovement(this, userId);
			seekBarMinmovementText.setTextColor(android.graphics.Color.BLACK);
		}

		catch (Exception e) {
			e.printStackTrace();
			seekBarMinmovementText.setTextColor(android.graphics.Color.RED);
			showToast("it must be a number", 0);
			return false;
		}

		
		PreferencesUtil.setUseLongClick(this, useLongClickChkbx
				.isChecked());
		
		PreferencesUtil.setSeekBarPreciseness(this, seekbarPrecisenessButton.getText().toString());
		
		PreferencesUtil.setDoesSeekbarStartInZero(this, doesSeekbarStartInZeroChkbx.isChecked());

		PreferencesUtil.setHasSeekbarMemory(this, hasSeekbarMemoryChkbx
				.isChecked());

		PreferencesUtil
				.setIsSurveyRandom(this, isSurveyRandomChkbx.isChecked());

		if (isSurveyRandomChkbx.isChecked()) {
			try {
				float rndLapse = Float.parseFloat(surveyRandomLapseText.getText()
						.toString());
				PreferencesUtil.setSurveyRandomLapse(this, fromMinToMs(rndLapse));
				surveyRandomLapseText
						.setTextColor(android.graphics.Color.BLACK);
			}

			catch (Exception e) {
				e.printStackTrace();
				surveyRandomLapseText.setTextColor(android.graphics.Color.RED);
				showToast("it must be a number", 0);
				return false;
			}
		}

		try {
			float surveyPeriod = Float.parseFloat(surveyPeriodText.getText()
					.toString());
			PreferencesUtil.setSurveyPeriod(this, fromMinToMs(surveyPeriod));
			surveyPeriodText.setTextColor(android.graphics.Color.BLACK);
		} catch (Exception e) {
			e.printStackTrace();
			surveyPeriodText.setTextColor(android.graphics.Color.RED);
			showToast("it must be a number", 0);
			return false;
		}

		PreferencesUtil.setIsSurveyLimitedInTime(this,
				isSurveyLimitedInTimeChkbx.isChecked());

		if (isSurveyLimitedInTimeChkbx.isChecked()) {

			if (!PreferencesUtil.setSurveyStartingTime(this,
					surveyStartingTimeText.getText().toString()))
				return false;

			if (!PreferencesUtil.setSurveyEndingTime(this, surveyEndingTimeText
					.getText().toString()))
				return false;

		}

		return true;
	}
	
	public float fromMsToMin(int ms){
		float msAux=(float) ms;
		
		return (msAux/60/1000);
	}

	public int fromMinToMs(float min){
		
		return (int)(min*60*1000);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case commitButtonId:

			if (validateAndCommitForm())
				finish();

			break;
			
		case seekbarPrecisenessButtonId:

			String currentPreciseness=seekbarPrecisenessButton.getText().toString();
			if (currentPreciseness.equals("1/10"))
				seekbarPrecisenessButton.setText("1/2");
			else if (currentPreciseness.equals("1/2"))
				seekbarPrecisenessButton.setText("1");
			else if (currentPreciseness.equals("1"))
				seekbarPrecisenessButton.setText("1/10");

			break;

		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		switch (buttonView.getId()) {

		case isSurveyRandomChkbxId:
			if (isChecked)
			{
				surveyRandomLapseTitle.setVisibility(android.view.View.VISIBLE);
				surveyRandomLapseText.setVisibility(android.view.View.VISIBLE);
			}
			else{
				surveyRandomLapseTitle.setVisibility(android.view.View.GONE);
				surveyRandomLapseText.setVisibility(android.view.View.GONE);
			}
			break;

		case isSurveyLimitedInTimeChkbxId:
			if (isChecked) {
				surveyStartingTimeTitle.setVisibility(android.view.View.VISIBLE);
				surveyEndinTimeTitle.setVisibility(android.view.View.VISIBLE);
				surveyNumberOfDaysTitle.setVisibility(android.view.View.VISIBLE);
				
				surveyStartingTimeText.setVisibility(android.view.View.VISIBLE);
				surveyEndingTimeText.setVisibility(android.view.View.VISIBLE);
				surveyNumberOfDaysText.setVisibility(android.view.View.VISIBLE);
			}

			else {
				surveyStartingTimeTitle.setVisibility(android.view.View.GONE);
				surveyEndinTimeTitle.setVisibility(android.view.View.GONE);
				surveyNumberOfDaysTitle.setVisibility(android.view.View.GONE);
				
				surveyStartingTimeText.setVisibility(android.view.View.GONE);
				surveyEndingTimeText.setVisibility(android.view.View.GONE);
				surveyNumberOfDaysText.setVisibility(android.view.View.GONE);
			}
			break;

		}
	}
	
//	public boolean onKeyDown (int keyCode, KeyEvent event){
//	
//		return true;
//	}

	public void showToast(String text, int duration) {
		Toast subscribedToast = new Toast(this);
		subscribedToast = Toast.makeText(this, text, duration);
		subscribedToast.setGravity(Gravity.CENTER, 0, 0);
		subscribedToast.show();
	}

}