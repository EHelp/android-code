package com.ehelp.ehelp.bank;

/**
 * Created by benson on 2015/11/18.
 */
public class Icon {
    private int iID;
    private String iName;

    public Icon(){
    }
    public Icon(int iID,String iName){
        this.iID = iID;
        this.iName = iName;
    }

    public int getiID() {
        return iID;
    }

    public String getiName() {
        return iName;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }
}
