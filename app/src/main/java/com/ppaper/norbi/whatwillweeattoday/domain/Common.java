package com.ppaper.norbi.whatwillweeattoday.domain;

public class Common {
    public static String currentUserName;

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public static void setCurrentUserName(String currentUserName) {
        Common.currentUserName = currentUserName;
    }
}
