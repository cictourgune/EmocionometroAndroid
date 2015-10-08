package org.tourgune.emocionometro.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * AppTrack
 *
 * Created by CICtourGUNE on 10/04/13.
 * Copyright (c) 2013 CICtourGUNE. All rights reserved.
 */
public class DatabaseEmotions extends SQLiteOpenHelper {

	public static final String DATABASE_NAME ="emotions.db";
	public static final int DABASE_VERSION=1;
	public static final String TABLE_NAMES ="emotions";
	public static final String CAMPO1 ="user";
	public static final String CAMPO2 ="pleasure";
	public static final String CAMPO3 ="arousal";
	public static final String CAMPO4 ="dominance";
	public static final String CAMPO5 ="latitude";
	public static final String CAMPO6 ="longitude";
	public static final String CAMPO7 ="provider";
	public static final String CAMPO8 ="date";

	
	public static final String PROJECTION_ALL_FIELDS[] = new String[]{CAMPO1,CAMPO2, CAMPO3, CAMPO4, CAMPO5,CAMPO6, CAMPO7, CAMPO8};
	

    /** Constructor */
    public DatabaseEmotions(Context context) {
		super(context, DATABASE_NAME, null, DABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + TABLE_NAMES + "  (" + CAMPO1 + " TEXT, " + CAMPO2 + " TEXT, " + CAMPO3 + " TEXT, " + CAMPO4 + " TEXT, "
														+ CAMPO5 + " TEXT, " + CAMPO6 + " TEXT, " + CAMPO7 + " TEXT, " + CAMPO8 + " TEXT)");
		
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
 
    
}