package com.wrkhalil.learningthroughlistening.Model;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String firebaseID;
    public String fullName;
    //public String dateOfBirth;
    public String email;
    public int score;

    public User() {
    }

    public User(String fireBaseID, String fullName, String email) {
        this.firebaseID = fireBaseID;
        this.fullName = fullName;
        //this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.score = 0;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firebaseID", firebaseID);
        result.put("fullName", fullName);
        result.put("email", email);
        result.put("score", score);
        return result;
    }

}
