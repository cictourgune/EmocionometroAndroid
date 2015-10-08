package org.tourgune.emocionometro;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.tourgune.apptrack.api.AppTrackAPI;
import org.tourgune.apptrack.store.ParamsException;
import org.tourgune.emocionometro.bean.Emotions;
import org.tourgune.emocionometro.dao.SurveyDAO;
import org.tourgune.emocionometro.preferences.PreferencesUtil;
import org.tourgune.emocionometro.store.Store;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class mainAct extends Activity implements OnClickListener, LocationListener,
GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener {
	
	
	 private static final String TAG = "LocationActivity";
	    private static final long INTERVAL = 1000 * 60 * 10;
	    private static final long FASTEST_INTERVAL = 1000 * 60 * 10;
	    static LocationRequest mLocationRequest;
	    GoogleApiClient mGoogleApiClient;
	    Location mCurrentLocation;
	    String mLastUpdateTime;

	    protected static void createLocationRequest() {
	    	mLocationRequest = new LocationRequest();
	    	mLocationRequest.setInterval(INTERVAL);
	    	mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
	    	mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	    }
	
//	Survey s = new Survey();
	private Button serviceButton;
	private static final int serviceButtonId = R.id.mainServiceButton;
	
	private TextView nextAlarmText;
	private static final int nextAlarmTextId = R.id.mainNextAlarm;

	private Button languageButton;
	private static final int languageButtonId = R.id.mainLanguageButton;
//	private TimerUtil timer;
	
	private String alertDialogText;
	private String commentText="";
	
	private static int arousal;
	private static int pleasure;
	private static int dominance;
	private static long time = 0;
	public AppTrackAPI api;
	private static int sendData = 0;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainact);


		initVariables();
		setTextFont();
		listeners();
		
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			Toast.makeText(this,
					"La aplicacion requiere de la activacion del GPS",
					Toast.LENGTH_SHORT).show();

			Intent settingsIntent = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			this.startActivityForResult(settingsIntent, 0);
		}
		

		if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

		
		
		if (this.getSharedPreferences(Constants.preferences, 0).getInt(Constants.USER_ID, 0) == 0) {
//			showUserIDDialog();
			
		}
		
		try {
			api = new AppTrackAPI(getApplicationContext(),"1363593e-d4ee-40c5-85e8-3c5a6fa9fc2c", "cf9fcd96-a3b1-4e06-8a42-cb1ea2925db7", Integer.toString(main.usuario));
			api.startTracking(getApplicationContext(), 0, 1000*60*60, 1000, 0, 0, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParamsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		
//		try {
//			api.pushParamValues();
//		} catch (ParamsException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	private void initVariables() {
		
//		System.out.println("device's locale is:"+Locale.getDefault());

		serviceButton = (Button) findViewById(serviceButtonId);
		
		languageButton = (Button) findViewById(languageButtonId);
		setLanguageButton();
		
		nextAlarmText = (TextView) findViewById(nextAlarmTextId);
		
		nextAlarmText.setText(PreferencesUtil.getNextAlarm(this));
		
		if (!PreferencesUtil.getIsProgramRunning(this))
			serviceButton.setText(R.string.mainServiceStart);
		else
			serviceButton.setText(R.string.mainServiceStop);

	}
	
	public void setTextFont(){
		
		 Typeface desiredFont = Typeface.createFromAsset(getAssets(), "fonts/optima.ttf");
		 
		 serviceButton.setTypeface(desiredFont);
//		 nextAlarmText.setTypeface(desiredFont);
	}

	private void listeners() {

		serviceButton.setOnClickListener(this);
		languageButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case serviceButtonId:
			if (!PreferencesUtil.getIsProgramRunning(this)) {
				showPrivacyDisclaimer();
			} else {
				showCommentDialog();
			}
			break;
			
		case languageButtonId:
			switchLanguage();
			break;
		}

	}
	
	public void setLanguageButton(){
		if (PreferencesUtil.getLanguageCode(this)==0){
			languageButton.setText(Html.fromHtml("<font color=\"blue\"><b>cas</b></font>/eus"));
		}
		else{
			languageButton.setText(Html.fromHtml("cas/<font color=\"blue\"><b>eus</b></font>"));
		}
	}
	
	public void switchLanguage(){
		
		if (PreferencesUtil.getLanguageCode(this)==0){//castellano-->pasa a euskera
			PreferencesUtil.setLanguageCode(this,1);
			languageButton.setText(Html.fromHtml("cas/<font color=\"blue\"><b>eus</b></font>"));
		}
		else{//euskera--> pasa a castellano
			PreferencesUtil.setLanguageCode(this,0);
			languageButton.setText(Html.fromHtml("<font color=\"blue\"><b>cas</b></font>/eus"));
		}
	}
	
	public void startService(){ 
		PreferencesUtil.setIsProgramRunning(this, true);
		
		TimerUtil timer = new TimerUtil(this);
		timer.startAlarm();
		
		serviceButton.setText(R.string.mainServiceStop);
		
		nextAlarmText.setText(PreferencesUtil.getNextAlarm(this));
		
	}
	

 	public void stopService(){
		
		TimerUtil timer = new TimerUtil(this);
		timer.stopAlarmEvent();
		
		PreferencesUtil.setIsProgramRunning(this, false);
		
		serviceButton.setText(R.string.mainServiceStart);
		api.stopTracking(getApplicationContext());
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, Constants.CONFIG_EDIT, 0, this.getResources().getString(
				R.string.editConfig));
		menu.add(0, Constants.CONFIG_HISTORY, 0, this.getResources().getString(
				R.string.surveyHistory));

		return true;
	}

	
	//no se llamará directamente a la opción seleccionada si no que se pasará por un procesod e contraseña de seguridad
	public boolean onOptionsItemSelected(MenuItem item) {
		if (!PreferencesUtil.getIsProgramRunning(this))
			showAlertDialog(item.getItemId());
		else
			showToast("Can't get access to options during the experiment",0);
		return true;
	}

	public void showAlertDialog(final int optionKey) {

		final EditText pass = new EditText(this);
		pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		alertDialogText="";
		
		//no se puede acceder al edittext despues de desaparecer así k se guardará su contenido en una variable
		pass.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				alertDialogText=s.toString();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// NO hará nada
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// NO hará nada
				
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.passNeeded);
		builder.setView(pass);

		builder.setPositiveButton(R.string.accept,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (alertDialogText.equals(Constants.CONFIG_PASSWORD)) {
							callOptionActivity(optionKey);
						}

					}
				});

		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		
		builder.show();
	}
	
	public void showUserIDDialog() {

		final EditText pass = new EditText(this);
		pass.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		alertDialogText="";
		
		//no se puede acceder al edittext despues de desaparecer así k se guardará su contenido en una variable
		pass.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				alertDialogText=s.toString();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// NO hará nada
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// NO hará nada
				
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("UserID?");
		builder.setView(pass);

		builder.setPositiveButton(R.string.accept,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!alertDialogText.equals(""))
						{
							System.out.println(alertDialogText);
							setUserId(Integer.parseInt(alertDialogText));
						}
						//else
							//showUserIDDialog();
					}
				});
		
		builder.show();
	}
	
	public void setUserId(int id){
		PreferencesUtil.setUserId(this, id);
	}

	
	public void showCommentDialog() {

		final EditText comment = new EditText(this);
		
		//no se puede acceder al edittext despues de desaparecer así k se guardará su contenido en una variable
		comment.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				commentText=s.toString();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// NO hará nada
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// NO hará nada	
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.experimentAbandoned);
		builder.setView(comment);

		builder.setPositiveButton(R.string.accept,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						stopService();
				   

//						callCommitSurveys();
					}
				});
		
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		
		builder.show();
	}
	
	//TODO terminar mensaje
	public void showPrivacyDisclaimer(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.privacyDisclaimer));

		
		
		builder.setPositiveButton(R.string.accept,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						startService();
					}
				});
		
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		
		builder.show();
		}
	
	
	
	
//	public void callCommitSurveys(){
//		String commitResult=SurveyDAO.instance(this).backupSurveysToSd(this,commentText);
//		showToast(commitResult, 0);
//	}
	
	public void callOptionActivity(int optionKey) {

		switch (optionKey) {

		case Constants.CONFIG_EDIT:
				Intent configIntent = new Intent(this, Configure.class);
				this.startActivity(configIntent);
			break;

		case Constants.CONFIG_HISTORY:
			Intent historyIntent = new Intent(this, History.class);
			this.startActivity(historyIntent);
			break;

		}
	}

	public void showToast(String text, int duration) {
		Toast subscribedToast = new Toast(this);
		subscribedToast = Toast.makeText(this, text, duration);
		subscribedToast.setGravity(Gravity.CENTER, 0, 0);
		subscribedToast.show();
	}

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    public void startLocationUpdates() {
    	LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    

    @Override
    protected void onPause() {
        super.onPause();
//        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        try {
			sendData(arousal, pleasure, dominance, location);
		} catch (org.tourgune.emocionometro.store.ParamsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        stopLocationUpdates();
        time = System.currentTimeMillis();
    }
    
    public void sendData(int arousal, int pleasure, int dominance, Location loc) throws org.tourgune.emocionometro.store.ParamsException{
    	if (sendData == 1){
	    	Log.e("DATOS!!!!:", "Arousal: " + arousal + ", Pleasure: " + pleasure + ", Dominance: " + dominance );

	    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   	    
			Calendar cal = Calendar.getInstance();
			String actualDate = dateformat.format(cal.getTime());
	    	
	    	Emotions emo = new Emotions(main.usuario, pleasure, arousal, dominance, loc.getLatitude(), loc.getLongitude(), loc.getProvider(), actualDate);
	    	Store store = new Store();
	    	store.sendPosToServer(emo, getApplicationContext());
    	}
    	sendData = 0;
    }

    public static void start(int arousal2, int pleasure2, int dominance2){
    	arousal= arousal2;
    	pleasure = pleasure2;
    	dominance = dominance2;
    	sendData = 1;
    	
    	createLocationRequest();    }
    
    
}