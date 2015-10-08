package org.tourgune.emocionometro.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.tourgune.emocionometro.bean.SurveyBean;
import org.tourgune.emocionometro.preferences.PreferencesUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class SurveyDAO {

//	private String tag = "ENCUESTA DAO";//si se hace algun syste.out
	private static final String DATABASE_NAME = "encuestaDatabase";
	private static final String DATABASE_TABLE = "encuesta";


	// id | title | content | type | read

	// The index (key) column name for use in where clauses.
	public static final String KEY_ID = "id";
	public static final int KEY_ID_COLUMN = 0;
	// The name and column index of each column in your database.
	public static final String USERID = "user";// id de usuario
	public static final int USERID_COLUMN = 1;
	public static final String VALUE_AROUSAL = "valueArousal";// nota obtenida
	public static final int VALUE_AROUSAL_COLUMN = 2;
	public static final String VALUE_PLEASURE = "valuePleasure";// nota obtenida
	public static final int VALUE_PLEASURE_COLUMN = 3;
	public static final String VALUE_DOMINANCE = "valueDominance";// nota obtenida
	public static final int VALUE_DOMINANCE_COLUMN = 4;
	public static final String TIME = "hour"; // timestamp
	public static final int TIME_COLUMN = 5;
	public static final String REACTION_TIME = "reaction"; // tiempo que ha
															// tardado el
															// usuario en
															// contestar
	public static final int REACTION_TIME_COLUMN = 6;

	private SQLiteDatabase myDatabase;

	// SQL Statement to create a new database. integer primary key
	// autoincrement,
	private static final String DATABASE_CREATE_TABLE = "create table IF NOT EXISTS "
			+ DATABASE_TABLE
			+ " ("
			+ KEY_ID
			+ " integer primary key, "
			+ USERID
			+ " integer not null, "
			+ VALUE_AROUSAL
			+ " integer not null, "
			+ VALUE_PLEASURE
			+ " integer not null, "
			+ VALUE_DOMINANCE
			+ " integer not null, "
			+ TIME
			+ " TIMESTAMP, "
			+ REACTION_TIME
			+ " TIMESTAMP);";

	// Context of the application using the database.
	private static Context _context;

	static private SurveyDAO _instance = null;

	protected SurveyDAO() {
		createDatabase();
	}

	static public SurveyDAO instance(Context context) {
		_context = context;
		if (null == _instance) {
			_instance = new SurveyDAO();
		}
		return _instance;
	}

	public void createDatabase() {
		myDatabase = _context.openOrCreateDatabase(DATABASE_NAME,
				Context.MODE_PRIVATE, null);

		// if (myDatabase.isOpen()) {// Borrar si existia
		// dropDatabase();
		// }
		myDatabase.execSQL(DATABASE_CREATE_TABLE);
		System.out.println(DATABASE_CREATE_TABLE);

	}

	public void close() {
		myDatabase.close();
	}

	public void dropDatabase() {
		myDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
	}

	public int getLastId() {

		Integer id = -1;

		Cursor cursor = myDatabase.rawQuery("select max(" + KEY_ID + ") from "
				+ DATABASE_TABLE, null);

		if (cursor.moveToFirst()) {
			id = cursor.getInt(0);
			cursor.close();
		}
		cursor.close();

		return id;
	}

	public long insertNewSurvey(int id,int userId, int valueArousal, int valuePleasure, int valueDominance, Long time,
			Long reactTime) {

			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ID,id);
			contentValues.put(USERID, userId);
			contentValues.put(VALUE_AROUSAL, valueArousal);
			contentValues.put(VALUE_PLEASURE, valuePleasure);
			contentValues.put(VALUE_DOMINANCE, valueDominance);
			contentValues.put(TIME, Long.toString(time));
			contentValues.put(REACTION_TIME, Long.toString(reactTime));

			return myDatabase.insert(DATABASE_TABLE, null, contentValues);
	}

	public SurveyBean getLastSurvey() {

		Cursor cursor = myDatabase.query(DATABASE_TABLE, new String[] { KEY_ID,
				USERID, VALUE_AROUSAL, VALUE_PLEASURE,VALUE_DOMINANCE, TIME, REACTION_TIME }, KEY_ID + "=="
				+ getLastId(), null, null, null, TIME + " ASC");

		SurveyBean e = new SurveyBean();
		e.setId(0);
		e.setUserId(0);
		e.setValueArousal(0);
		e.setValuePleasure(0);
		e.setValueDominance(0);
		e.setTime(0);
		e.setReactTime(0);

		if (cursor.moveToFirst()) {

			Integer id = cursor.getInt(KEY_ID_COLUMN);
			Integer userId = cursor.getInt(USERID_COLUMN);
			Integer valueArousal = cursor.getInt(VALUE_AROUSAL_COLUMN);
			Integer valuePleasure = cursor.getInt(VALUE_PLEASURE_COLUMN);
			Integer valueDominance = cursor.getInt(VALUE_DOMINANCE_COLUMN);
			String time = cursor.getString(TIME_COLUMN);
			String reactTime = cursor.getString(REACTION_TIME_COLUMN);

			e.setId(id);
			e.setUserId(userId);
			e.setValueArousal(valueArousal);
			e.setValuePleasure(valuePleasure);
			e.setValueDominance(valueDominance);
			e.setTime(Long.parseLong(time));
			e.setReactTime(Long.parseLong(reactTime));

		}

		cursor.close();

		return e;
	}

	
	public ArrayList<SurveyBean> getSurveyList() {

		ArrayList<SurveyBean> results = new ArrayList<SurveyBean>();

		System.out.println("before get query");
		Cursor cursor = myDatabase.query(DATABASE_TABLE, new String[] { KEY_ID,
				USERID, VALUE_AROUSAL, VALUE_PLEASURE,VALUE_DOMINANCE, TIME, REACTION_TIME }, null, null, null,
				null, TIME + " ASC");

		if (cursor.moveToFirst()) {
			// Iterate over each cursor.
			System.out.println("there are elements in the cursor");
			do {
				Integer id = cursor.getInt(KEY_ID_COLUMN);
				Integer userId = cursor.getInt(USERID_COLUMN);
				Integer valueArousal = cursor.getInt(VALUE_AROUSAL_COLUMN);
				Integer valuePleasure = cursor.getInt(VALUE_PLEASURE_COLUMN);
				Integer valueDominance = cursor.getInt(VALUE_DOMINANCE_COLUMN);
				String time = cursor.getString(TIME_COLUMN);
				String reactTime = cursor.getString(REACTION_TIME_COLUMN);

				SurveyBean e = new SurveyBean();
				e.setId(id);
				e.setUserId(userId);
				e.setValueArousal(valueArousal);
				e.setValuePleasure(valuePleasure);
				e.setValueDominance(valueDominance);
				e.setTime(Long.parseLong(time));
				e.setReactTime(Long.parseLong(reactTime));
				results.add(e);

			} while (cursor.moveToNext());
		}
		cursor.close();

		System.out.println("Returning a total of " + results.size()
				+ " surveys");
		return results;
	}

	
	//esta función realizará un backup de los datos recogidos en un txt
	//al estar formateado con tabulaciones, este archivo será facilmente importable a excel
	//además recibe como parámetro un posible comentario en el caso de que el experimento haya sido
	//abandonado
	public String backupSurveysToSd(Context context,String comment) {

		FileOutputStream fileOutputStream = null;
		ArrayList<SurveyBean> listEncuesta;
		SurveyBean encuesta;

		Context mContext = context;

		File sd = Environment.getExternalStorageDirectory();

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String filename = "encuestas_" + formatter.format(new Date()) +"_"+PreferencesUtil.getUserId(context)+ ".xml";

		try {
			File outputFile = new File(sd, filename);
			fileOutputStream = new FileOutputStream(outputFile);

			PrintWriter out = new PrintWriter(fileOutputStream);
			listEncuesta = getSurveyList();

			// /Primero se escribe un encabezado con todas las opciones
			// configuradas
			//out.write(PreferencesUtil.getParameterHeading(mContext));
			
			//guardaremos los valores obtenidos en distintos Strings, de forma que luego se escriba
			//cada parámetro en una única línea
			
			/*String id="ID:\t";
			String userId="\n USERID:\t";
			String value="\n VALUE:\t";
			String reactTime="\n REACT TIME:\t";
			String day="\n DAY:\t";
			String hour="\n HOUR:\t";*/
			String xml="<usuario>";

			for (int i = 0; i < listEncuesta.size(); i++) {
 				encuesta = listEncuesta.get(i);

				SimpleDateFormat dayFormatter = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm:ss");
				
				String dayToPrint = dayFormatter.format(encuesta.getTime());
				String hourToPrint= hourFormatter.format(encuesta.getTime());
				
				xml = xml + "<encuesta>";
				
					xml = xml + "<id>" +encuesta.getId() +"</id>";
					xml = xml + "<language>" +PreferencesUtil.getLanguageCode(context) +"</language>";
					xml = xml + "<userId>" +encuesta.getUserId() +"</userId>";
					xml = xml + "<valueArousal>" + encuesta.getValueArousal()+"</valueArousal>";
					xml = xml + "<valuePleasure>" +encuesta.getValuePleasure() +"</valuePleasure>";
					xml = xml + "<valueDominance>" + encuesta.getValueDominance()+"</valueDominance>";
					xml = xml + "<reactTime>"+encuesta.getReactTime()+"</reactTime>";
					xml = xml + "<day>"+dayToPrint+"</day>";
					xml = xml + "<hour>"+hourToPrint+"</hour>";
					xml = xml + "<latitude>"+EgistourDAO.instance(mContext).getLatitude(encuesta.getId())+"</latitude>";
					xml = xml + "<longitude>"+EgistourDAO.instance(mContext).getLongitude(encuesta.getId())+"</longitude>";
					
					
				xml = xml + "</encuesta>";	
				/*id+=encuesta.getId()+"\t";
				userId+=encuesta.getUserId()+"\t";
				value+=getRealValue(encuesta.getValue())+"\t";
				reactTime+=encuesta.getReactTime()+"\t";
				day+=dayToPrint+"\t";
				hour+=hourToPrint+"\t";*/
				
				// FORMATO EN EL QUE SE VERÁ EN EL TXT
				/*out.write("ID:\t" + encuesta.getId() + "\n USERID:\t"
						+ encuesta.getUserId() + "\n VALUE:\t"
					+ getRealValue(encuesta.getValue()) + "\n REACT TIME:\t"
						+ encuesta.getReactTime() + "\n DAY:\t" + dayToPrint
						+ "\n HOUR:\t" + hourToPrint+ "\n\n");*/
				
			}
			
			xml=xml + "</usuario>";
			
			if (!comment.equals(""))
				out.write("Experiment abandoned because of:\t"+comment+"\n\n");
			
			//out.write(id+userId+value+reactTime+day+hour);
			out.println(xml);
			out.flush();
			out.close();
		} catch (IOException e) {

			Log
					.d("",
							"FileNotFoundException in backupEncuestasToSd flushing output writer");
			e.printStackTrace();

			return "Error accesing SD card";
		}

		return "SD/" + filename;
		//return "This function is deprecated";
	}

	public Float getRealValue(int value) {

		float realVal = new Float(value);

		realVal -= 50;
		realVal = realVal / 10;

		return realVal;

	}

}