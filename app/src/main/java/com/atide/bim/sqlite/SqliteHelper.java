package com.atide.bim.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import static android.os.Environment.*;

/**
 * Created by atide on 2016/3/21.
 */
public class SqliteHelper extends SQLiteOpenHelper{
    private static SqliteHelper mInstance;
    public static final String DB_DIR = getExternalStorageDirectory().getPath()
            + File.separator + "AppData" + File.separator
            + SqliteHelper.class.getPackage().getName();
    static {

        File dbFolder = new File(DB_DIR);
        // 目录不存在则自动创建目录
        if (!dbFolder.exists()){
            dbFolder.mkdirs();
        }
    }

    private String tableSql = "create table IF NOT EXISTS  notice(_id integer primary key autoincrement,typeId int not null,sheetId int not null,engineeringId int not null,sectionId int not null,rang text,color text,createDate text,upload boolean)";

    public SqliteHelper(Context context){
        super(context,DB_DIR+File.separator+"bim.db",null,1);


    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static SqliteHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SqliteHelper.class) {
                mInstance = new SqliteHelper(context);
            }

        }
        return mInstance;
    }


}
