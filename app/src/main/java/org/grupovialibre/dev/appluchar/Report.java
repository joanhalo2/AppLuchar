package org.grupovialibre.dev.appluchar;

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




    public String reportID;
    public String userID;
    public String location;
    public String actors;
    public String type;
    public int attendees;
    public String section;
    public String status;
    public String date;


    public Report(){

    }

    public Report(Parcel input){
        this.reportID = input.readString();
        this.userID = input.readString();
        this.location = input.readString();
        this.actors = input.readString();
        this.type = input.readString();
        this.attendees = input.readInt();
        this.section = input.readString();
        this.status = input.readString();
        this.date = input.readString();
    }

    public Report(String userID, String location, String actors, String type, int attendees, String section, String status, String date) {
        this.userID = userID;
        this.location = location;
        this.actors = actors;
        this.type = type;
        this.attendees = attendees;
        this.section = section;
        this.status = status;
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("location", location);
        result.put("actors", actors);
        result.put("type", type);
        result.put("attendees", attendees);
        result.put("section", section);
        result.put("status", status);
        result.put("date", date);

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

    public int getAttendees() {
        return attendees;
    }

    public void setAttendees(int attendees) {
        this.attendees = attendees;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(reportID);
        dest.writeString(userID);
        dest.writeString(location);
        dest.writeString(actors);
        dest.writeString(type);
        dest.writeInt(attendees);
        dest.writeString(section);
        dest.writeString(status);
        dest.writeString(date);


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
