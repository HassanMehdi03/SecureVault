package com.example.securevault;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database
{
    final String TABLE_USER_INFO="user_info";
    final String KEY_USER_ID="user_id";
    final String KEY_USER_NAME="user_name";
    final String KEY_USER_EMAIL="user_email";
    final String KEY_USER_PASSWORD="user_password";
    final String KEY_USER_MOBILE="user_mobile";
    final int version=1;
    final String DATABASE_NAME="SecureVaultDB";
    MyHelper myHelper;
    SQLiteDatabase sqLiteDatabase;
    Context context;

    public Database (Context context)
    {
        this.context=context;
    }


    public void addUserInfo(String name,String email,String password,String mobile)
    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_USER_NAME,name);
        cv.put(KEY_USER_EMAIL,email);
        cv.put(KEY_USER_PASSWORD,password);
        cv.put(KEY_USER_MOBILE,mobile);
        long count=sqLiteDatabase.insert(TABLE_USER_INFO,null,cv);

        if(count>0)
        {
            Toast.makeText(context, R.string.registered_successfully, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, R.string.registration_failed, Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<UserInfo> readUserInfos()
    {
        ArrayList<UserInfo> userInfos=new ArrayList<>();
        String[] columns=new String[]{KEY_USER_NAME,KEY_USER_EMAIL,KEY_USER_PASSWORD,KEY_USER_MOBILE};

        Cursor cursor=sqLiteDatabase.query(TABLE_USER_INFO,columns,null,null,null,null,null);

        int index_name=cursor.getColumnIndex(KEY_USER_NAME);
        int index_email=cursor.getColumnIndex(KEY_USER_EMAIL);
        int index_password=cursor.getColumnIndex(KEY_USER_PASSWORD);
        int index_mobile=cursor.getColumnIndex(KEY_USER_MOBILE);

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
        {
            UserInfo temp=new UserInfo();

            temp.setName(cursor.getString(index_name));
            temp.setEmail(cursor.getString(index_email));
            temp.setPassword(cursor.getString(index_password));
            temp.setMobile(cursor.getString(index_mobile));

            userInfos.add(temp);
        }

        cursor.close();

        return userInfos;
    }

    public void open()
    {
        myHelper=new MyHelper(context,DATABASE_NAME,null,version);
        sqLiteDatabase=myHelper.getWritableDatabase();
    }

    public void close()
    {
        sqLiteDatabase.close();
        myHelper.close();
    }


    public class MyHelper extends SQLiteOpenHelper
    {

        public MyHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String userInfoQuery="CREATE TABLE "+TABLE_USER_INFO+"("+
                    KEY_USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    KEY_USER_NAME+" TEXT,"+
                    KEY_USER_EMAIL+" TEXT,"+
                    KEY_USER_PASSWORD+" TEXT,"+
                    KEY_USER_MOBILE+" TEXT )";

            db.execSQL(userInfoQuery);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER_INFO);
            onCreate(db);

        }
    }

}
