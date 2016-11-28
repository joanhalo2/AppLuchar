package org.grupovialibre.dev.appluchar.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joan on 20/11/16.
 */

public class Denunciation implements Parcelable {

    private String denunciationID;
    private String reportID;
    private String userID;
    private String userName;
    private String title;
    private String corpse;
    private String dateTime;


    public Denunciation(){

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("reportID", reportID);
        result.put("userID", userID);
        result.put("username", userName);
        result.put("title", title);
        result.put("corpse", corpse);
        result.put("dateTime", dateTime);
        return result;
    }


    public Denunciation(String denunciationID, String reportID, String userID, String userName, String title, String corpse, String dateTime) {
        this.denunciationID = denunciationID;
        this.reportID = reportID;
        this.userID = userID;
        this.userName = userName;
        this.title = title;
        this.corpse = corpse;
        this.dateTime = dateTime;
    }

    public Denunciation(String reportID, String userID, String userName, String title, String corpse, String dateTime) {
        this.reportID = reportID;
        this.userID = userID;
        this.userName = userName;
        this.title = title;
        this.corpse = corpse;
        this.dateTime = dateTime;
    }

    public  Denunciation(Parcel input){
        this.denunciationID = input.readString();
        this.reportID = input.readString();
        this.userID = input.readString();
        this.userName = input.readString();
        this.title = input.readString();
        this.corpse = input.readString();
        this.dateTime = input.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(denunciationID);
        dest.writeString(reportID);
        dest.writeString(userID);
        dest.writeString(userName);
        dest.writeString(title);
        dest.writeString(corpse);
        dest.writeString(dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Denunciation> CREATOR
            = new Parcelable.Creator<Denunciation>() {
        public Denunciation createFromParcel(Parcel in) {
            return new Denunciation(in);
        }

        public Denunciation[] newArray(int size) {
            return new Denunciation[size];
        }
    };

    public String getDenunciationID() {
        return denunciationID;
    }

    public void setDenunciationID(String denunciationID) {
        this.denunciationID = denunciationID;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCorpse() {
        return corpse;
    }

    public void setCorpse(String corpse) {
        this.corpse = corpse;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
