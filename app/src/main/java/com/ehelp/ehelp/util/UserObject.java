package com.ehelp.ehelp.util;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by chenzhe on 2015/12/5.
 */
public class UserObject implements Serializable{
    private int id;
    private String name;
    private String nickname;
    private int gender;
    private int age;
    private String phone;
    private String email;
    private String location;
    private double longitude;
    private double latitude;
    private int occupation;
    private double reputation;
    private String avatar;
    private String identity_id;
    private int is_verify;
    private int support_number;
    private int relative_type;
    private String alias;

    public UserObject(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            nickname = jsonObject.getString("nickname");
            gender = jsonObject.getInt("gender");
            age = jsonObject.getInt("age");
            phone = jsonObject.getString("phone");
            email = jsonObject.getString("email");
            location = jsonObject.getString("location");
            longitude = jsonObject.getDouble("longitude");
            latitude = jsonObject.getDouble("latitude");
            occupation = jsonObject.getInt("occupation");
            reputation = jsonObject.getDouble("reputation");
            avatar = jsonObject.getString("avatar");
            identity_id = jsonObject.getString("identity_id");
            is_verify = jsonObject.getInt("is_verify");
            support_number = jsonObject.getInt("support_number");
            relative_type = jsonObject.getInt("relative_type");
            alias = jsonObject.getString("alias");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    public double getReputation() {
        return reputation;
    }

    public void setReputation(double reputation) {
        this.reputation = reputation;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    public int getIs_verify() {
        return is_verify;
    }

    public void setIs_verify(int is_verify) {
        this.is_verify = is_verify;
    }

    public int getSupport_number() {
        return support_number;
    }

    public void setSupport_number(int support_number) {
        this.support_number = support_number;
    }

    public int getRelative_type() {
        return relative_type;
    }

    public void setRelative_type(int relative_type) {
        this.relative_type = relative_type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
