package com.txl.bin.txl.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.txl.bin.txl.copySqlite;

import java.io.IOException;

public class UsersDb {
	
	private String path;
	private SQLiteDatabase db;
	
	public UsersDb(Context context){
		try {
			path= copySqlite.CopySqliteToFile("txl",context);
			db=SQLiteDatabase.openDatabase(path, null, 0);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	public boolean isExit(String name) {
		// TODO Auto-generated method stub
		Cursor cursor=db.rawQuery("select * from users where name=?",new String[]{name});
		if(cursor.moveToNext()){
			return true;
		}else{
			return false;
		}
				
		
	}
	
	public void insertUser(String name, String password) {
		// TODO Auto-generated method stub
		db.execSQL("insert into users(name,passsword) values(?,?)",new String[]{name,password});
			
			
	}


	public boolean isRight(String name, String password) {
		// TODO Auto-generated method stub
		
		Cursor cursor=db.rawQuery("select * from users where name=? and passsword=?",new String[]{name,password});
		if(cursor.moveToNext()){
			return true;
		}
		
		return false;
	}

}
