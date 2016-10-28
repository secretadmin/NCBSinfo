package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportHelper;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;

/**
 * This class handles all Shared Preferences related to user and user based customizations.
 */
public class User implements AppConstants {

    private SharedPreferences pref;
    private Context context;
    private String CURRENT_USER = "currentUsername";
    private String CURRENT_EMAIL = "currentEmail";
    private String REGISTERED = "registeredUser";
    private String FIREBASE_TOKEN = "firebaseRegistrationToken";
    private String USER_TYPE = "userType";
    private String DEFAULT_ROUTE = "defaultRoute";
    private String NOTIFICATIONS = "notification_preference";
    private String NOTIFICANTION_ONSET = "pref_notificationOnset";
    private String FIREBASE_ID = "fireBaseID";
    private String NUMBER_OF_EVENTS = "numberOfEventsToKeep";
    private String HURRY_UP_TIME = "hurryUp_Time";

    protected User(SharedPreferences pref, Context context) {
        this.pref = pref;
        this.context = context;

    }

    public String getName() {
        return pref.getString(CURRENT_USER, "User name");
    }

    public void setName(String name) {
        pref.edit().putString(CURRENT_USER, name).apply();
    }

    public String getEmail() {
        return pref.getString(CURRENT_EMAIL, "email@domain.com");
    }

    public void setEmail(String email) {
        pref.edit().putString(CURRENT_EMAIL, email).apply();
    }

    public String getToken() {
        return pref.getString(FIREBASE_TOKEN, "null");
    }

    public void setToken(String token) {
        pref.edit().putString(FIREBASE_TOKEN, token).apply();
    }

    public boolean isAuthorized() {
        return pref.getBoolean(REGISTERED, false);
    }

    public void setAuthorization(loginStatus login) {
        switch (login) {
            case SUCCESS:
                pref.edit().putBoolean(REGISTERED, true).apply();
                break;
            case FAIL:
                pref.edit().putBoolean(REGISTERED, false).apply();
                break;
        }
    }

    public String getFirebaseID() {
        return pref.getString(FIREBASE_ID, "none");
    }

    public void setFirebaseID(String id) {
        pref.edit().putString(FIREBASE_ID, id).apply();
    }

    public userType getUserType() {

        for (userType u : userType.values()) {
            if (u.getValue().equals(pref.getString(USER_TYPE, userType.VISITOR.getValue()))) {
                return u;
            }
        }
        return userType.VISITOR; //Default
    }

    public void setUserType(userType user) {
        switch (user) {
            case VISITOR:
                pref.edit().putString(USER_TYPE, userType.VISITOR.getValue()).apply();
                break;
            case NEW_USER:
                pref.edit().putString(USER_TYPE, userType.NEW_USER.getValue()).apply();
                break;
            case OLD_USER:
                pref.edit().putString(USER_TYPE, userType.OLD_USER.getValue()).apply();
                break;
            case REGULAR_USER:
                pref.edit().putString(USER_TYPE, userType.REGULAR_USER.getValue()).apply();
                break;
        }
    }

    public int getDefaultRouteValue() {
        return pref.getInt(DEFAULT_ROUTE, 0);
    }

    public Routes getDefaultRoute() {
        return new TransportHelper().getRoute(getDefaultRouteValue());
    }

    public void setDefaultRoute(Routes route) {
        pref.edit().putInt(DEFAULT_ROUTE, route.getRouteNo()).apply();
    }

    public boolean isNotificationAllowed() {
        return pref.getBoolean(NOTIFICATIONS, true);
    }

    public void notificationAllowed(boolean value) {
        pref.edit().putBoolean(NOTIFICATIONS, value).apply();
    }

    public int getNotificationOnset() {
        return pref.getInt(NOTIFICANTION_ONSET, 10);
    }

    public void setNotificationOnset(int minutes) {
        pref.edit().putInt(NOTIFICANTION_ONSET, minutes).apply();
    }

    public int getNumberOfEventsToKeep() {
        return pref.getInt(NUMBER_OF_EVENTS, 25); //25 is default
    }

    public void setNumberOfEventsToKeep(int numberOfEvents) {
        pref.edit().putInt(NUMBER_OF_EVENTS, numberOfEvents).apply();
    }

    public void setHurryUpTime(int time) {
        pref.edit().putInt(HURRY_UP_TIME, time).apply();
    }

    public int getHurryUpTime() {
        return pref.getInt(HURRY_UP_TIME, 5); //5 is default
    }

}
