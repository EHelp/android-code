package com.ehelp.ehelp.util;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by chenzhe on 2015/12/7.
 */
public class SystemMessage implements Serializable{
    private int _id;
    private int type;
    private int user_id;
    private int event_id;
    private int status;
    private String nickname;
    private String realname;
    private String location;
    private String occupation;

    public HashMap<String, Object> convert() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("_id", _id);
        data.put("type", type);
        data.put("user_id", user_id);
        data.put("event_id", event_id);
        data.put("status", status);
        data.put("nickname", nickname);
        data.put("realname", realname);
        data.put("location", location);
        data.put("occupation", occupation);
        return data;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
