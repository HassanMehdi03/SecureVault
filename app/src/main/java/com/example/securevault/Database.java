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
    // Database Info
    final String DATABASE_NAME="SecureVaultDB";
    MyHelper myHelper;
    SQLiteDatabase sqLiteDatabase;
    Context context;
    final int version=3;

    // Saved Passwords and UserName
    final String TABLE_SAVED_LOGIN_INFO="saved_login_info";
    final String KEY_SAVED_LOGIN_ID="saved_login_id";
    final String KEY_SAVED_LOGIN_USERNAME="saved_login_username";
    final String KEY_SAVED_LOGIN_PASSWORD="saved_login_password";
    final String KEY_SAVED_LOGIN_URL="saved_login_url";

    // Restore Records
    final String TABLE_RESTORE_RECORDS="restore_records";
    final String KEY_RESTORE_ID="restore_id";
    final String KEY_RESTORE_USERNAME="restore_username";
    final String KEY_RESTORE_PASSWORD="restore_password";
    final String KEY_RESTORE_URL="restore_url";

    // Login and Signup Records
    final String TABLE_USER_INFO="user_info";
    final String KEY_USER_ID="user_id";
    final String KEY_USER_NAME="user_name";
    final String KEY_USER_EMAIL="user_email";
    final String KEY_USER_PASSWORD="user_password";
    final String KEY_USER_MOBILE="user_mobile";

    public Database (Context context)
    {
        this.context=context;
    }

    public void addSavedLoginInfo(String username,String password,String url,int i)
    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_SAVED_LOGIN_USERNAME,username);
        cv.put(KEY_SAVED_LOGIN_PASSWORD,password);
        cv.put(KEY_SAVED_LOGIN_URL,url);

        long count=sqLiteDatabase.insert(TABLE_SAVED_LOGIN_INFO,null,cv);

        if(i==0)
        {
            if(count>0)
            {
                Toast.makeText(context, R.string.record_added_successfully, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, R.string.record_not_added, Toast.LENGTH_SHORT).show();
            }
        }
        else if(i==1)
        {
            if(count>0)
            {
                Toast.makeText(context, R.string.record_restore_successfully, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, R.string.record_not_restored, Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void addRestoreRecords(String username,String password,String url)
    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_RESTORE_USERNAME,username);
        cv.put(KEY_RESTORE_PASSWORD,password);
        cv.put(KEY_RESTORE_URL,url);

        long count=sqLiteDatabase.insert(TABLE_RESTORE_RECORDS,null,cv);

        if(count>0)
        {
            Toast.makeText(context, R.string.record_added_to_bin_successfully, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, R.string.record_not_added_to_bin, Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<SavedLoginsRecord> readRestoreRecords()
    {
        ArrayList<SavedLoginsRecord> restoreRecords=new ArrayList<>();
        String[]columns=new String[]{KEY_RESTORE_ID,KEY_RESTORE_USERNAME,KEY_RESTORE_PASSWORD,KEY_RESTORE_URL};

        Cursor cursor=sqLiteDatabase.query(TABLE_RESTORE_RECORDS,columns,null,null,null,null,null);

        int index_restore_id=cursor.getColumnIndex(KEY_RESTORE_ID);
        int index_restore_username=cursor.getColumnIndex(KEY_RESTORE_USERNAME);
        int index_restore_password=cursor.getColumnIndex(KEY_RESTORE_PASSWORD);
        int index_restore_url=cursor.getColumnIndex(KEY_RESTORE_URL);

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
        {
            SavedLoginsRecord temp=new SavedLoginsRecord();
            temp.setId(cursor.getInt(index_restore_id));
            temp.setUsername(cursor.getString(index_restore_username));
            temp.setPassword(cursor.getString(index_restore_password));
            temp.setUrl(cursor.getString(index_restore_url));

            restoreRecords.add(temp);
        }

        return restoreRecords;
    }

    public boolean updateSavedLoginInfo(int id,String username,String password,String url)
    {
        ContentValues cv=new ContentValues();
        cv.put(KEY_SAVED_LOGIN_USERNAME,username);
        cv.put(KEY_SAVED_LOGIN_PASSWORD,password);
        cv.put(KEY_SAVED_LOGIN_URL,url);

        long count=sqLiteDatabase.update(TABLE_SAVED_LOGIN_INFO,cv,KEY_SAVED_LOGIN_ID+"=?",new String[]{id+""});

        if(count>0)
        {
            Toast.makeText(context, R.string.record_updated, Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            Toast.makeText(context, R.string.record_not_updated, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public ArrayList<SavedLoginsRecord> readSavedLoginsRecords()
    {
        ArrayList<SavedLoginsRecord> savedLoginsRecords=new ArrayList<>();
        String[] columns=new String[]{KEY_SAVED_LOGIN_ID,KEY_SAVED_LOGIN_USERNAME,KEY_SAVED_LOGIN_PASSWORD,KEY_SAVED_LOGIN_URL};


        Cursor cursor=sqLiteDatabase.query(TABLE_SAVED_LOGIN_INFO,columns,null,null,null,null,null);

        int index_saved_login_id=cursor.getColumnIndex(KEY_SAVED_LOGIN_ID);
        int index_saved_login_username=cursor.getColumnIndex(KEY_SAVED_LOGIN_USERNAME);
        int index_saved_login_password=cursor.getColumnIndex(KEY_SAVED_LOGIN_PASSWORD);
        int index_saved_login_url=cursor.getColumnIndex(KEY_SAVED_LOGIN_URL);

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
        {
            SavedLoginsRecord temp=new SavedLoginsRecord();
            temp.setId(cursor.getInt(index_saved_login_id));
            temp.setUsername(cursor.getString(index_saved_login_username));
            temp.setPassword(cursor.getString(index_saved_login_password));
            temp.setUrl(cursor.getString(index_saved_login_url));

            savedLoginsRecords.add(temp);
        }

        cursor.close();

        return savedLoginsRecords;
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
        String[] columns=new String[]{KEY_USER_ID,KEY_USER_NAME,KEY_USER_EMAIL,KEY_USER_PASSWORD,KEY_USER_MOBILE};

        Cursor cursor=sqLiteDatabase.query(TABLE_USER_INFO,columns,null,null,null,null,null);

        int index_user_id=cursor.getColumnIndex(KEY_USER_ID);
        int index_name=cursor.getColumnIndex(KEY_USER_NAME);
        int index_email=cursor.getColumnIndex(KEY_USER_EMAIL);
        int index_password=cursor.getColumnIndex(KEY_USER_PASSWORD);
        int index_mobile=cursor.getColumnIndex(KEY_USER_MOBILE);

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
        {
            UserInfo temp=new UserInfo();

            temp.setId(cursor.getInt(index_user_id));
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

    public boolean deleteSavedLoginInfo(int id)
    {
        long count=sqLiteDatabase.delete(TABLE_SAVED_LOGIN_INFO,KEY_SAVED_LOGIN_ID+"=?",new String[]{id+""});

        if(count>0)
        {
            Toast.makeText(context, R.string.record_deleted, Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            Toast.makeText(context, R.string.record_not_deleted, Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public boolean deleteRecyclceBinRecord(int id,int i)
    {
        long count=sqLiteDatabase.delete(TABLE_RESTORE_RECORDS,KEY_RESTORE_ID+"=?",new String[]{id+""});

        if(i==0)
        {
            if(count>0)
            {
                Toast.makeText(context, R.string.record_deleted, Toast.LENGTH_SHORT).show();
                return true;
            }
            else
            {
                Toast.makeText(context, R.string.record_not_deleted, Toast.LENGTH_SHORT).show();
            }
        }
        return false;
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

            String savedLoginQuery = "CREATE TABLE " + TABLE_SAVED_LOGIN_INFO + "(" +
                    KEY_SAVED_LOGIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_SAVED_LOGIN_USERNAME + " TEXT, " +
                    KEY_SAVED_LOGIN_PASSWORD + " TEXT, " +
                    KEY_SAVED_LOGIN_URL + " TEXT )";

            String restoreRecordsQuery="CREATE TABLE "+TABLE_RESTORE_RECORDS+"("+
                    KEY_RESTORE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    KEY_RESTORE_USERNAME+" TEXT, "+
                    KEY_RESTORE_PASSWORD+" TEXT, "+
                    KEY_RESTORE_URL+" TEXT )";


            db.execSQL(userInfoQuery);
            db.execSQL(savedLoginQuery);
            db.execSQL(restoreRecordsQuery);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER_INFO);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_SAVED_LOGIN_INFO);

            onCreate(db);

        }
    }

}
