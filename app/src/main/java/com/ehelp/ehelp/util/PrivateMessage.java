package com.ehelp.ehelp.util;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.square.AskMsg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by chenzhe on 2015/12/14.
 */
public class PrivateMessage {
/*db.execSQL("CREATE TABLE IF NOT EXISTS private_message" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, my_id integer, type integer, status integer, " +
                    "event_id integer, nickname varchar, is_like integer, love_coin integer, time varchar, " +
                    "title varchar,  content varchar, follow_number integer, support_number integer)");*/
    private int _id;
    private int type;
    private int status;
    private int event_id;
    private String nickname;
    private String author;
    private int is_like;
    private int love_coin;
    private String time;
    private String title;
    private String content;
    private int follow_number;
    private int support_number;

    public AskMsg convert() {
        AskMsg askMsg = new AskMsg();
        askMsg.setName(nickname);
        askMsg.setEvent_id(event_id);
        askMsg.setReplynum(support_number);
        askMsg.setTime(time);
        askMsg.setThumbnum(follow_number);
        askMsg.setHead(R.mipmap.head);
        askMsg.setTitle(title);
        askMsg.setIs_like(is_like);
        askMsg.setContent(content);
        askMsg.setLove_coin(love_coin);

        return askMsg;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public int getLove_coin() {
        return love_coin;
    }

    public void setLove_coin(int love_coin) {
        this.love_coin = love_coin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFollow_number() {
        return follow_number;
    }

    public void setFollow_number(int follow_number) {
        this.follow_number = follow_number;
    }

    public int getSupport_number() {
        return support_number;
    }

    public void setSupport_number(int support_number) {
        this.support_number = support_number;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
