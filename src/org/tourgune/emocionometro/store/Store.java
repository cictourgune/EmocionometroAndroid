package org.tourgune.emocionometro.store;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.tourgune.emocionometro.bean.Emotions;
import org.tourgune.emocionometro.utils.Utils;

import android.content.Context;
import android.location.Location;
import android.util.Log;

/**
 * AppTrack
 *
 * Created by CICtourGUNE on 10/04/13.
 * Copyright (c) 2013 CICtourGUNE. All rights reserved.
 * 
 */
public class Store {

	public String urlbase = "http://tomcatdev.tourgune.org:80/emocionometro";
//	public String urlbase = "http://10.10.0.16:8080/emocionometro"; 
	private String queryParams = "?devToken=" + "1363593e-d4ee-40c5-85e8-3c5a6fa9fc2c" + "&appToken=" + "cf9fcd96-a3b1-4e06-8a42-cb1ea2925db7"; 
	
	public String emotionValues(Emotions emo, int idPunto,  Context contexto) throws ParamsException{
		if(!(Utils.isOnline(contexto))){
			Utils.insertEmoLocal(contexto, emo);
			return "0";
		}
		else{
			String urls = urlbase + "/open/sdk/value/addemo" + queryParams;
			
			String json = "{\"user\": \"" + emo.getUser() + "\", ";
			json = json + "\"pleasure\": \"" + emo.getPleasure() + "\", ";
			json = json + "\"arousal\": \"" + emo.getArousal() + "\", ";
			json = json + "\"dominance\": \"" + emo.getDominance() + "\", ";
			json = json + "\"idpunto\": \"" + idPunto + "\", ";
			json = json + "\"date\": \"" + emo.getDate() + "\"} ";
			Log.e("json", json);
			Log.e("url", urls);
			
			String result = Utils.callPOSTService(json, urls);
			Log.e("result", result);
			if (result.equalsIgnoreCase("1")){
				return "1";
			}
			
			else {
				
				return "0";
			}
		}
	
	}

	public int sendPosToServer(Emotions emo, Context contexto) throws ParamsException{
		
		if(!(Utils.isOnline(contexto))){
			Utils.insertEmoLocal(contexto, emo);
			return 0;
		}
		else{
			
			String url = urlbase + "/open/sdk/point/addemo" + queryParams;
					
			String json = "{\"idusuario\": \"" + emo.getUser() + "\", ";
			json = json + "\"latitud\": \"" + Double.toString(emo.getLatitude()) + "\", ";
			json = json + "\"longitud\": \"" + Double.toString(emo.getLongitude()) + "\", ";		
			json = json + "\"fecha\": \"" + emo.getDate().toString() + "\", ";
			json = json + "\"provider\": \"" + emo.getProvider() + " \" }";
	
			Log.e("json sendPosToServer", json);
			Log.e("url", url);
			
			String result = Utils.callPOSTService(json, url);
			
			Log.e("result sendPosToServer", result);
			
			if (result==null || result.equalsIgnoreCase("0")) {
				//INSERTAMOS EN LOCAL
				Utils.insertEmoLocal(contexto, emo);
			}
			else{
				emotionValues(emo, Integer.parseInt(result), contexto);
			}
			return 1;
		}
	}
}
