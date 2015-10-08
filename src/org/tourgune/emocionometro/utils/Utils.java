package org.tourgune.emocionometro.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.tourgune.emocionometro.bean.Emotions;
import org.tourgune.emocionometro.db.DatabaseEmotions;
import org.tourgune.emocionometro.db.EmotionsContentProvider;
import org.tourgune.emocionometro.store.ParamsException;
import org.tourgune.emocionometro.store.Store;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * AppTrack
 *
 * Created by CICtourGUNE on 10/04/13.
 * Copyright (c) 2013 CICtourGUNE. All rights reserved.
 * 
 * Clase para almacenar los metodos utiles para la correcta ejecucion de la aplicacion
 */
public class Utils {

	
	public static String getDeviceID(TelephonyManager phonyManager){
		 
		String id = phonyManager.getDeviceId();
		if (id == null){
			id = "not available";
		}
		int phoneType = phonyManager.getPhoneType();
		switch(phoneType){
		case TelephonyManager.PHONE_TYPE_NONE:
			return id;
		 
		case TelephonyManager.PHONE_TYPE_GSM:
			return id;
		 
		case TelephonyManager.PHONE_TYPE_CDMA:
			return id; 
		default:
			return "UNKNOWN: ID=" + id;
		}
		 
	}
	
	/**
	 * Metodo que hace una llamada asincrona de tipo POST
	 * 
	 * @param json 
	 * @param url
	 * @return
	 */
	public static String callPOSTService(String json, String url) {
		
		PostService async = new PostService(json, url);
		AsyncTask<Void, Void, String> result = async.execute();
		try {
			return result.get();

		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}

	}
	
	/**
	 * Metodo que hace una llamada de tipo GET
	 * 
	 * @param url
	 * @return
	 */
	public static String callGETService(String url) {

		GetService async = new GetService(url);
		AsyncTask<String, Void, String> result = async.execute();
		try {
			return result.get();

		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
		
		
		
		
		
//		HttpClient httpclient = new DefaultHttpClient();
//		HttpGet request = new HttpGet(url);
//		String result = null;
//		ResponseHandler<String> handler = new BasicResponseHandler();
//		try {
//			result = httpclient.execute(request, handler);
//
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		httpclient.getConnectionManager().shutdown();
//
//		return result;
	}
	
	
	
	public static void insertEmoLocal(Context context, Emotions emo) {
			
		ContentValues contentValues = new ContentValues();
		contentValues.put("user", emo.getUser());
		contentValues.put("pleasure", emo.getPleasure());
		contentValues.put("arousal", emo.getArousal());
		contentValues.put("dominance", emo.getDominance());
		contentValues.put("latitude", emo.getLatitude());
		contentValues.put("longitude", emo.getLongitude());
		contentValues.put("provider", emo.getProvider());
		contentValues.put("date", emo.getDate());

		context.getContentResolver().insert(EmotionsContentProvider.CONTENT_URI, contentValues);
	
	}
	
	
	
	
	/**
	 * Metodo que vuelca todos los puntos almacenados localmente a la base de datos del servidor
	 * 
	 * @param context Contexto de la aplicacion
	 */
	public static void sendDatabaseEmotions(Context context) {
		
		Store store = new Store();
    			
		Cursor cursor = context.getContentResolver().query(
				EmotionsContentProvider.CONTENT_URI,
				DatabaseEmotions.PROJECTION_ALL_FIELDS, null, null, null);
		if(cursor!=null && cursor.getCount()>0){
		if (cursor.moveToFirst() && cursor.getCount() > 1) {
			do {
				Emotions emo = new Emotions(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), 
						Integer.parseInt(cursor.getString(3)), Double.parseDouble(cursor.getString(4)), Double.parseDouble(cursor.getString(5)), 
						cursor.getString(6), cursor.getString(7));
				try {
					store.sendPosToServer(emo, context);
					context.getContentResolver().delete(EmotionsContentProvider.CONTENT_URI, "date='" + emo.getDate() + "'", null);
				} catch (ParamsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (cursor.moveToNext());
			cursor.close();
			}
		}
	}
	


	/**
	 * Metodo que comprueba la validez de una fecha
	 * 
	 * @param fechax Fecha en formato de cadena de texto
	 * @return True si la vecha es valida. False en caso contrario
	 */
	public static boolean isFechaValida(String fechax) {
		  try {
		      SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		      formatoFecha.setLenient(false);
		      formatoFecha.parse(fechax);
		  } catch (Exception e) {
		      return false;
		  }
		  return true;
		}


	public static boolean isOnline(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
}
