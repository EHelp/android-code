package com.ehelp.ehelp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chenzhe on 2015/12/7.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper mInstance = null;
    private static final String DATABASE_NAME = "system_information.db";
    private static final int DATABASE_VERSION = 5;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS sys_message" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, my_id integer, type integer, user_id integer, " +
                "event_id integer, nickname varchar, realname varchar(10), location varchar, " +
                "occupation integer, status integer, content varchar)");
        /*event_id: int（这个在custom里面）
nickname: string
time: date
title: string
content: string
is_like: int
love_coin: int
follow_number:int
support_number:int
*/
        db.execSQL("CREATE TABLE IF NOT EXISTS private_message" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, my_id integer, type integer, status integer, " +
                "event_id integer, nickname varchar, is_like integer, love_coin integer, time varchar, " +
                "title varchar,  content varchar, follow_number integer, support_number integer, author varchar)");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 3) {
            db.execSQL("DROP TABLE sys_message");
            db.execSQL("CREATE TABLE IF NOT EXISTS sys_message" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, my_id integer, type integer, user_id integer, " +
                    "event_id integer, nickname varchar, realname varchar(10), location varchar, " +
                    "occupation integer, status integer, content varchar)");
        } else if (newVersion == 4) {
            db.execSQL("DROP TABLE sys_message");
            db.execSQL("CREATE TABLE IF NOT EXISTS sys_message" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, my_id integer, type integer, user_id integer, " +
                    "event_id integer, nickname varchar, realname varchar(10), location varchar, " +
                    "occupation integer, status integer, content varchar)");
            db.execSQL("CREATE TABLE IF NOT EXISTS private_message" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, my_id integer, type integer, status integer, " +
                    "event_id integer, nickname varchar, is_like integer, love_coin integer, time varchar, " +
                    "title varchar,  content varchar, follow_number integer, support_number integer)");
        } else if (newVersion == 5) {
            db.execSQL("DROP TABLE sys_message");
            db.execSQL("DROP TABLE private_message");
            db.execSQL("CREATE TABLE IF NOT EXISTS sys_message" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, my_id integer, type integer, user_id integer, " +
                    "event_id integer, nickname varchar, realname varchar(10), location varchar, " +
                    "occupation integer, status integer, content varchar)");
            db.execSQL("CREATE TABLE IF NOT EXISTS private_message" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, my_id integer, type integer, status integer, " +
                    "event_id integer, nickname varchar, is_like integer, love_coin integer, time varchar, " +
                    "title varchar,  content varchar, follow_number integer, support_number integer, author varchar)");
        }
    }
}
