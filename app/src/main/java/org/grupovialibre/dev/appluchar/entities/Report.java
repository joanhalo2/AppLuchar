package org.grupovialibre.dev.appluchar.entities;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joan on 20/10/16.
 */

//@IgnoreExtraProperties
public class Report implements Parcelable{




    private String reportID;
    private String userID;
    private String location;
    private String place;
    private String actors;
    private String type;
    private String section;
    private String date;
    private String tags;
    private String description;
    private String userName;



    public Report(){

    }

    public Report(Parcel input){
        this.reportID = input.readString();
        this.userID = input.readString();
        this.location = input.readString();
        this.place = input.readString();
        this.actors = input.readString();
        this.type = input.readString();
        this.section = input.readString();
        this.date = input.readString();
        this.tags = input.readString();
        this.description = input.readString();
        this.userName = input.readString();
    }

    public Report(String userID, String location, String place, String actors, String type, String section, String date,String tags,String desc,String userName) {
        this.userID = userID;
        this.location = location;
        this.place = place;
        this.actors = actors;
        this.type = type;
        this.section = section;
        this.date = date;
        this.tags = tags;
        this.description = desc;
        this.userName = userName;
    }

    public void updateFields(String userID, String location, String place, String actors, String type, String section, String date,String tags,String desc,String userName){
        this.userID = userID;
        this.location = location;
        this.place = place;
        this.actors = actors;
        this.type = type;
        this.section = section;
        this.date = date;
        this.tags = tags;
        this.description = desc;
        this.userName = userName;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("location", location);
        result.put("place", place);
        result.put("actors", actors);
        result.put("type", type);
        result.put("section", section);
        result.put("date", date);
        result.put("tags", tags);
        result.put("desc", description);
        result.put("username", userName);
        return result;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(reportID);
        dest.writeString(userID);
        dest.writeString(location);
        dest.writeString(place);
        dest.writeString(actors);
        dest.writeString(type);
        dest.writeString(section);
        dest.writeString(date);
        dest.writeString(tags);
        dest.writeString(description);
        dest.writeString(userName);

    }

    public static final Parcelable.Creator<Report> CREATOR
            = new Parcelable.Creator<Report>() {
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

}
