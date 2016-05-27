package com.ehelp.ehelp.util;

/**
 * Created by chenzhe on 2015/11/30.
 */
public class EventItem {
    private int event_id;
    private int type;
    private double longitude;
    private double latitude;
    public EventItem(int event_id, int type, double longitude, double latitude) {
        this.event_id = event_id;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public int getEventid() {
        return event_id;
    }
    public int getType() {
        return type;
    }
    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }
}
