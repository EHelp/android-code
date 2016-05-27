package com.ehelp.ehelp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenzhe on 2015/12/7.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = DBHelper.getInstance(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }


    //添加未评价的条目
    public void addEvaluate(ArrayList<HashMap<String, Object>> datas, int my_id) {
        db.beginTransaction();  //开始事务
        try {
            for (HashMap<String, Object> data : datas) {
                /*_id , type integer, user_id integer, " +
                "event_id interger, nickname varchar, realname varchar(10), location varchar, " +
                "occupation integer*/
                db.execSQL("INSERT INTO sys_message VALUES(null, ?, 1, ?, ?, ?, ?, null, null, 0, null)",
                        new Object[]{my_id, data.get("user_id"), data.get("event_id"), data.get("nickname"), data.get("realname")});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void addContact(HashMap<String, Object> data, int my_id) {
        db.beginTransaction();  //开始事务
        try {

            db.execSQL("INSERT INTO sys_message VALUES(null, ?, 2, ?, null, ?, ?, ?, ?, 0, ?)",
                    new Object[]{my_id, data.get("user_id"), data.get("nickname"), data.get("realname"), data.get("location"), data.get("occupation"), data.get("content")});

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void addConResult(HashMap<String, Object> data, int my_id) {
        /*db.execSQL("CREATE TABLE IF NOT EXISTS private_message" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, my_id integer, type integer, status integer, " +
                    "event_id integer, nickname varchar, is_like integer, love_coin integer, time varchar, " +
                    "title varchar,  content varchar, follow_number integer, support_number integer)");*/
        db.beginTransaction();  //开始事务
        try {

            db.execSQL("INSERT INTO private_message VALUES(null, ?, 3, ?, null, ?, null, null, null, null, null, null, null, null)",
                    new Object[]{my_id, data.get("status"), data.get("nickname")});

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void addResponse(HashMap<String, Object> data, int my_id) {
        /*db.execSQL("CREATE TABLE IF NOT EXISTS private_message" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, my_id integer, type integer, status integer, " +
                    "event_id integer, nickname varchar, is_like integer, love_coin integer, time varchar, " +
                    "title varchar,  content varchar, follow_number integer, support_number integer)");*/
        db.beginTransaction();  //开始事务
        try {

            db.execSQL("INSERT INTO private_message VALUES(null, ?, 4, null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{my_id, data.get("event_id"), data.get("nickname"), data.get("is_like"),
                    data.get("love_coin"), data.get("time"), data.get("title"), data.get("content"),
                    data.get("follow_number"), data.get("support_number"), data.get("author")});

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void addAdopt(HashMap<String, Object> data, int my_id) {
        /*db.execSQL("CREATE TABLE IF NOT EXISTS private_message" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, my_id integer, type integer, status integer, " +
                    "event_id integer, nickname varchar, is_like integer, love_coin integer, time varchar, " +
                    "title varchar,  content varchar, follow_number integer, support_number integer)");*/
        db.beginTransaction();  //开始事务
        try {

            db.execSQL("INSERT INTO private_message VALUES(null, ?, 5, null, ?, ?, ?, ?, ?, ?, ?, ?, ?, null)",
                    new Object[]{my_id, data.get("event_id"), data.get("nickname"), data.get("is_like"),
                            data.get("love_coin"), data.get("time"), data.get("title"), data.get("content"),
                            data.get("follow_number"), data.get("support_number")});

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void deleteAllMessage() {
        db.delete("sys_message", null, null);
    }

    public int getCount(int my_id) {
        Cursor cursor = db.rawQuery("select count(*)from sys_message where my_id = ?", new String[]{my_id+""});
        cursor.moveToFirst();
        int temp = (int)cursor.getLong(0);
        cursor.close();

        cursor = db.rawQuery("select count(*)from private_message where my_id = ?", new String[]{my_id+""});
        cursor.moveToFirst();
        temp += (int)cursor.getLong(0);
        cursor.close();
        return temp;
    }

    public void deleteSysMessage(int id) {
        db.delete("sys_message", "_id = ?", new String[]{String.valueOf(id)});

    }

    public void deletePriMessage(int id) {
        db.delete("private_message", "_id = ?", new String[]{String.valueOf(id)});

    }

    /**
     * query all persons, return list
     *
     * @return List<Person>
     */

    public void UpdateStatus(SystemMessage systemMessage) {
        ContentValues cv = new ContentValues();
        cv.put("status", 1);
        db.update("sys_message", cv, "_id = ?",
                new String[] { systemMessage.get_id()+"" });
    }

    public List<SystemMessage> querySystem(int my_id) {
        ArrayList<SystemMessage> messages = new ArrayList<SystemMessage>();
        Cursor c = queryTheSysCursor(my_id, 1);
        while (c.moveToNext()) {
            /*_id , type integer, user_id integer, " +
                "event_id interger, nickname varchar, realname varchar(10), location varchar, " +
                "occupation integer*/
            SystemMessage message = new SystemMessage();
            message.set_id(c.getInt(c.getColumnIndex("_id")));
            message.setType(c.getInt(c.getColumnIndex("type")));
            message.setUser_id(c.getInt(c.getColumnIndex("user_id")));
            message.setEvent_id(c.getInt(c.getColumnIndex("event_id")));
            message.setNickname(c.getString(c.getColumnIndex("nickname")));
            message.setRealname(c.getString(c.getColumnIndex("realname")));
            message.setLocation(c.getString(c.getColumnIndex("location")));
            message.setStatus(c.getInt(c.getColumnIndex("status")));
            message.setOccupation(OccupationEncode.OccupationToString(c.getInt(c.getColumnIndex("occupation"))));
            messages.add(message);
        }
        c.close();

        c = queryTheSysCursor(my_id, 2);
        while (c.moveToNext()) {
            /*_id , type integer, user_id integer, " +
                "event_id interger, nickname varchar, realname varchar(10), location varchar, " +
                "occupation integer*/
            SystemMessage message = new SystemMessage();
            message.set_id(c.getInt(c.getColumnIndex("_id")));
            message.setType(c.getInt(c.getColumnIndex("type")));
            message.setUser_id(c.getInt(c.getColumnIndex("user_id")));
            message.setEvent_id(c.getInt(c.getColumnIndex("event_id")));
            message.setNickname(c.getString(c.getColumnIndex("nickname")));
            message.setRealname(c.getString(c.getColumnIndex("realname")));
            message.setLocation(c.getString(c.getColumnIndex("location")));
            message.setStatus(c.getInt(c.getColumnIndex("status")));
            message.setOccupation(OccupationEncode.OccupationToString(c.getInt(c.getColumnIndex("occupation"))));
            messages.add(message);
        }
        c.close();
        return messages;
    }

    public List<PrivateMessage> queryPrivate(int my_id) {
        ArrayList<PrivateMessage> messages = new ArrayList<PrivateMessage>();
        Cursor c = queryThePriCursor(my_id, 3);
        while (c.moveToNext()) {
            /*_id , type integer, user_id integer, " +
                "event_id interger, nickname varchar, realname varchar(10), location varchar, " +
                "occupation integer*/
            PrivateMessage message = new PrivateMessage();
            message.setIs_like(c.getInt(c.getColumnIndex("is_like")));
            message.set_id(c.getInt(c.getColumnIndex("_id")));
            message.setContent(c.getString(c.getColumnIndex("content")));
            message.setEvent_id(c.getInt(c.getColumnIndex("event_id")));
            message.setFollow_number(c.getInt(c.getColumnIndex("follow_number")));
            message.setLove_coin(c.getInt(c.getColumnIndex("love_coin")));
            message.setNickname(c.getString(c.getColumnIndex("nickname")));
            message.setStatus(c.getInt(c.getColumnIndex("status")));
            message.setType(c.getInt(c.getColumnIndex("type")));
            message.setTime(c.getString(c.getColumnIndex("time")));
            message.setTitle(c.getString(c.getColumnIndex("title")));
            message.setSupport_number(c.getInt(c.getColumnIndex("support_number")));
            message.setAuthor(c.getString(c.getColumnIndex("author")));

            messages.add(message);
        }
        c.close();

        c = queryThePriCursor(my_id, 4);
        while (c.moveToNext()) {
            /*_id , type integer, user_id integer, " +
                "event_id interger, nickname varchar, realname varchar(10), location varchar, " +
                "occupation integer*/
            PrivateMessage message = new PrivateMessage();
            message.setIs_like(c.getInt(c.getColumnIndex("is_like")));
            message.set_id(c.getInt(c.getColumnIndex("_id")));
            message.setContent(c.getString(c.getColumnIndex("content")));
            message.setEvent_id(c.getInt(c.getColumnIndex("event_id")));
            message.setFollow_number(c.getInt(c.getColumnIndex("follow_number")));
            message.setLove_coin(c.getInt(c.getColumnIndex("love_coin")));
            message.setNickname(c.getString(c.getColumnIndex("nickname")));
            message.setStatus(c.getInt(c.getColumnIndex("status")));
            message.setType(c.getInt(c.getColumnIndex("type")));
            message.setTime(c.getString(c.getColumnIndex("time")));
            message.setTitle(c.getString(c.getColumnIndex("title")));
            message.setSupport_number(c.getInt(c.getColumnIndex("support_number")));
            message.setAuthor(c.getString(c.getColumnIndex("author")));

            messages.add(message);
        }
        c.close();

        c = queryThePriCursor(my_id, 5);
        while (c.moveToNext()) {
            /*_id , type integer, user_id integer, " +
                "event_id interger, nickname varchar, realname varchar(10), location varchar, " +
                "occupation integer*/
            PrivateMessage message = new PrivateMessage();
            message.setIs_like(c.getInt(c.getColumnIndex("is_like")));
            message.set_id(c.getInt(c.getColumnIndex("_id")));
            message.setContent(c.getString(c.getColumnIndex("content")));
            message.setEvent_id(c.getInt(c.getColumnIndex("event_id")));
            message.setFollow_number(c.getInt(c.getColumnIndex("follow_number")));
            message.setLove_coin(c.getInt(c.getColumnIndex("love_coin")));
            message.setNickname(c.getString(c.getColumnIndex("nickname")));
            message.setStatus(c.getInt(c.getColumnIndex("status")));
            message.setType(c.getInt(c.getColumnIndex("type")));
            message.setTime(c.getString(c.getColumnIndex("time")));
            message.setTitle(c.getString(c.getColumnIndex("title")));
            message.setSupport_number(c.getInt(c.getColumnIndex("support_number")));
            message.setAuthor(c.getString(c.getColumnIndex("author")));

            messages.add(message);
        }
        c.close();
        return messages;
    }



    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheSysCursor(int my_id, int type) {
        Cursor c = db.rawQuery("SELECT * FROM sys_message where my_id = ? and type = ?", new String[]{my_id+"", type+""});
        return c;
    }

    public Cursor queryThePriCursor(int my_id, int type) {
        Cursor c = db.rawQuery("SELECT * FROM private_message where my_id = ? and type = ?", new String[]{my_id+"", type+""});
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
