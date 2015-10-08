package org.tourgune.emocionometro.dao;

import java.util.ArrayList;

import org.tourgune.emocionometro.bean.EgistourBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class EgistourDAO {

//	private String tag = "EGISTOUR DAO";//si se hace algun syste.out
	private static final String DATABASE_NAME = "encuestaDatabase";
	private static final String DATABASE_TABLE = "egistour";


	// id | title | content | type | read

	// The index (key) column name for use in where clauses.
	public static final String KEY_ID = "id";
	public static final int KEY_ID_COLUMN = 0;
	// The name and column index of each column in your database.
	public static final String VALUE_LAT = "valueLat";// nota obtenida
	public static final int VALUE_LAT_COLUMN = 2;
	public static final String VALUE_LNG = "valueLng";// nota obtenida
	public static final int VALUE_LNG_COLUMN = 3;
	public static final String NUM_SATELLITES = "numSatellites";// nota obtenida
	public static final int NUM_SATELLITES_COLUMN = 4;

	private SQLiteDatabase myDatabase;

	// SQL Statement to create a new database. integer primary key
	// autoincrement,
	private static final String DATABASE_CREATE_TABLE = "create table IF NOT EXISTS "
			+ DATABASE_TABLE
			+ " ("
			+ KEY_ID
			+ " integer primary key, "
			+ VALUE_LAT
			+ " real not null, "
			+ VALUE_LNG
			+ " real not null, "
			+ NUM_SATELLITES
			+ " integer not null);";

	// Context of the application using the database.
	private static Context _context;

	static private EgistourDAO _instance = null;

	protected EgistourDAO() {
		createDatabase();
	}

	static public EgistourDAO instance(Context context) {
		_context = context;
		if (null == _instance) {
			_instance = new EgistourDAO();
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

	public long insertNewEgistour(int id, float lat, float lng, int numSatellites) {

			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_ID,id);
			contentValues.put(VALUE_LAT, lat);
			contentValues.put(VALUE_LNG, lng);
			contentValues.put(NUM_SATELLITES, numSatellites);
			
			return myDatabase.insert(DATABASE_TABLE, null, contentValues);
	}

	public EgistourBean getLastEgistour() {

		Cursor cursor = myDatabase.query(DATABASE_TABLE, new String[] { KEY_ID,
				VALUE_LAT, VALUE_LNG, NUM_SATELLITES }, KEY_ID + "=="
				+ getLastId(), null, null, null, KEY_ID);

		EgistourBean e = new EgistourBean();
		e.setId(0);
		e.setValueLat(0);
		e.setValueLng(0);
		e.setNumSatellites(0);

		if (cursor.moveToFirst()) {

			Integer id = cursor.getInt(KEY_ID_COLUMN);
			float lat = cursor.getInt(VALUE_LAT_COLUMN);
			float lng = cursor.getInt(VALUE_LNG_COLUMN);
			Integer numSatellites = cursor.getInt(NUM_SATELLITES_COLUMN);

			e.setId(id);
			e.setValueLat(lat);
			e.setValueLng(lng);
			e.setNumSatellites(numSatellites);

		}

		cursor.close();

		return e;
	}

	
 	public ArrayList<EgistourBean> getSurveyList() {

		ArrayList<EgistourBean> results = new ArrayList<EgistourBean>();

		System.out.println("before get query");
		Cursor cursor = myDatabase.query(DATABASE_TABLE, new String[] { KEY_ID,
				VALUE_LAT, VALUE_LNG, NUM_SATELLITES  }, null, null, null,
				null, KEY_ID);

		if (cursor.moveToFirst()) {
			// Iterate over each cursor.
			System.out.println("there are elements in the cursor");
			do {
				Integer id = cursor.getInt(KEY_ID_COLUMN);
				float lat = cursor.getInt(VALUE_LAT_COLUMN);
				float lng = cursor.getInt(VALUE_LNG_COLUMN);
				Integer numSatellites = cursor.getInt(NUM_SATELLITES_COLUMN);

				EgistourBean e = new EgistourBean();
				e.setId(id);
				e.setValueLat(lat);
				e.setValueLng(lng);
				e.setNumSatellites(numSatellites);
				results.add(e);

			} while (cursor.moveToNext());
		}
		cursor.close();

		return results;
	}

	
	//esta función realizará un backup de los datos recogidos en un txt
	//al estar formateado con tabulaciones, este archivo será facilmente importable a excel
	//además recibe como parámetro un posible comentario en el caso de que el experimento haya sido
	//abandonado
	public String backupSurveysToSd(Context context,String comment) {

//		FileOutputStream fileOutputStream = null;
//		ArrayList<SurveyBean> listEncuesta;
//		SurveyBean encuesta;
//
//		mContext = context;
//
//		File sd = Environment.getExternalStorageDirectory();
//
//		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//		String filename = "encuestas_" + formatter.format(new Date()) +"_"+PreferencesUtil.getUserId(context)+ ".txt";
//
//		try {
//			File outputFile = new File(sd, filename);
//
//			fileOutputStream = new FileOutputStream(outputFile);
//
//			OutputStreamWriter out = new OutputStreamWriter(fileOutputStream);
//			listEncuesta = getSurveyList();
//
//			// /Primero se escribe un encabezado con todas las opciones
//			// configuradas
//			out.write(PreferencesUtil.getParameterHeading(mContext));
//			
//			//guardaremos los valores obtenidos en distintos Strings, de forma que luego se escriba
//			//cada parámetro en una única línea
//			
//			String id="ID:\t";
//			String userId="\n USERID:\t";
//			String value="\n VALUE:\t";
//			String reactTime="\n REACT TIME:\t";
//			String day="\n DAY:\t";
//			String hour="\n HOUR:\t";
//
//			for (int i = 0; i < listEncuesta.size(); i++) {
//				encuesta = listEncuesta.get(i);
//
//				SimpleDateFormat dayFormatter = new SimpleDateFormat("dd/MM/yyyy");
//				SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm:ss");
//				
//				String dayToPrint = dayFormatter.format(encuesta.getTime());
//				String hourToPrint= hourFormatter.format(encuesta.getTime());
//
//				id+=encuesta.getId()+"\t";
//				userId+=encuesta.getUserId()+"\t";
//				value+=getRealValue(encuesta.getValue())+"\t";
//				reactTime+=encuesta.getReactTime()+"\t";
//				day+=dayToPrint+"\t";
//				hour+=hourToPrint+"\t";
//				
//				// FORMATO EN EL QUE SE VERÁ EN EL TXT
////				out.write("ID:\t" + encuesta.getId() + "\n USERID:\t"
////						+ encuesta.getUserId() + "\n VALUE:\t"
////						+ getRealValue(encuesta.getValue()) + "\n REACT TIME:\t"
////						+ encuesta.getReactTime() + "\n DAY:\t" + dayToPrint
////						+ "\n HOUR:\t" + hourToPrint+ "\n\n");
//				
//			}
//			
//			if (!comment.equals(""))
//				out.write("Experiment abandoned because of:\t"+comment+"\n\n");
//			
//			out.write(id+userId+value+reactTime+day+hour);
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//
//			Log
//					.d(tag,
//							"FileNotFoundException in backupEncuestasToSd flushing output writer");
//			e.printStackTrace();
//
//			return "Error accesing SD card";
//		}
//
//		return "SD/" + filename;
		return "This function is deprecated";
	}

	public Float getLatitude(int id) {

		Float latitude = null;

		Cursor cursor = myDatabase.rawQuery("select valueLat from egistour where id=" + id, null);

		if (cursor.moveToFirst()) {
			latitude = cursor.getFloat(0);
			cursor.close();
		}
		cursor.close();

		return latitude;
	}
	
	public Float getLongitude(int id) {

		Float longitude = null;

		Cursor cursor = myDatabase.rawQuery("select valueLng from egistour where id=" + id, null);

		if (cursor.moveToFirst()) {
			longitude = cursor.getFloat(0);
			cursor.close();
		}
		cursor.close();

		return longitude;
	}
}