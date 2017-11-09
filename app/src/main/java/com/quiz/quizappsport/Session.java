package com.quiz.quizappsport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.quiz.quizappsport.activities.LoginActivity;

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;

    private static final String PREF_NAME = "QuizAppPref";
    private static final String KEY_ID_USER = "idUser";
    private static final String IS_LOGIN = "IsLoggedIn";

    public Session(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createLoginSession(int idUser) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_ID_USER, idUser);
        editor.commit();
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        }

    }

    public int getUserId() {
        return prefs.getInt(KEY_ID_USER, -1);
    }

    public boolean isLoggedIn(){
        return prefs.getBoolean(IS_LOGIN, false);
    }
}
