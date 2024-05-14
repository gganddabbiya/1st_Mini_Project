package com.kosa.session;

public class Session {
    private static String userID;

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        Session.userID = userID;
    }
  
}