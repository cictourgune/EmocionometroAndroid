package org.tourgune.emocionometro.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

/**
 * AppTrack
 *
 * Created by CICtourGUNE on 10/04/13.
 * Copyright (c) 2013 CICtourGUNE. All rights reserved.
 * 
 * Metodo para llamar a los servicios GET de manera asincrona (necesario a partir de la version 4 de android)
 */
public class GetService extends AsyncTask<String, Void, String>{


	private String dir;
	
	public GetService(String urlPar) {
		
		dir = urlPar;
	
	}

	protected String doInBackground(String... urls) {
		String response = "";
	        DefaultHttpClient client = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(dir);
	        try {
	          HttpResponse execute = client.execute(httpGet);
	          InputStream content = execute.getEntity().getContent();

	          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
	          String s = "";
	          while ((s = buffer.readLine()) != null) {
	            response += s;
	          }

	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	        return response;
	    }




}
