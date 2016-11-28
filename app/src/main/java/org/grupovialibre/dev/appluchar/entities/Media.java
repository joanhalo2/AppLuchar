package org.grupovialibre.dev.appluchar.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joan on 21/11/16.
 */

public class Media implements Parcelable {

    private String mediaID;
    private String reportID;
    private String userID;
    private String userName;
    private String description;
    private String imageURI;
    private String audioURI;
    private String dateTime;

    public Media(){

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("reportID", reportID);
        result.put("userID", userID);
        result.put("username", userName);
        result.put("description", description);
        result.put("imageURI", imageURI);
        result.put("audioURI", audioURI);
        result.put("dateTime", dateTime);
        return result;
    }

    public Media(String reportID, String userID, String userName, String description, String imageURI, String audioURI, String dateTime) {
        this.reportID = reportID;
        this.userID = userID;
        this.userName = userName;
        this.description = description;
        this.imageURI = imageURI;
        this.audioURI = audioURI;
        this.dateTime = dateTime;
    }

    public  Media(Parcel input){
        this.mediaID = input.readString();
        this.reportID = input.readString();
        this.userID = input.readString();
        this.userName = input.readString();
        this.description = input.readString();
        this.imageURI = input.readString();
        this.audioURI = input.readString();
        this.dateTime = input.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Media> CREATOR
            = new Parcelable.Creator<Media>() {
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mediaID);
        dest.writeString(reportID);
        dest.writeString(userID);
        dest.writeString(userName);
        dest.writeString(description);
        dest.writeString(imageURI);
        dest.writeString(audioURI);
        dest.writeString(dateTime);
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getAudioURI() {
        return audioURI;
    }

    public void setAudioURI(String audioURI) {
        this.audioURI = audioURI;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
