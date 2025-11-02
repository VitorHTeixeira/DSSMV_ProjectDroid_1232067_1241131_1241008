package com.lvhm.covertocover.repo;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.UUID;

public class UserTokenContainer {
    private static UserTokenContainer instance;
    private static final String PREFERENCES_FILE = "CTCPreferences";
    private static final String KEY_USER_TOKEN = "profile_unique_token";
    private static final String KEY_TOKEN_CREATED = "token_creation_timestamp";
    private static final String KEY_IS_FIRST_LAUNCH = "is_first_app_launch";

    private SharedPreferences shared_preferences;
    private String current_token;

    private UserTokenContainer(Context context) {
        shared_preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        current_token = shared_preferences.getString(KEY_USER_TOKEN, null);
    }

    public static synchronized UserTokenContainer getInstance(Context context) {
        if (instance == null) {
            instance = new UserTokenContainer(context.getApplicationContext());
        }
        return instance;
    }

    public boolean isFirstLaunch() {
        return shared_preferences.getBoolean(KEY_IS_FIRST_LAUNCH, true);
    }

    public void setFirstLaunch() {
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.putBoolean(KEY_IS_FIRST_LAUNCH, false);
        editor.apply();
    }

    public boolean hasToken() {
        return current_token != null && !current_token.isEmpty();
    }

    public String getToken() {
        return current_token;
    }

    public String generateToken() {
        String user_token = UUID.randomUUID().toString();
        saveToken(user_token);
        return user_token;
    }

    public boolean saveToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        current_token = token.trim();
        long timestamp = System.currentTimeMillis();

        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.putString(KEY_USER_TOKEN, current_token);
        editor.putLong(KEY_TOKEN_CREATED, timestamp);

        return editor.commit();
    }

    public void clearToken() {
        current_token = null;
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.remove(KEY_USER_TOKEN);
        editor.remove(KEY_TOKEN_CREATED);
        editor.apply();
    }

    public boolean isValidToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        String uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        return token.trim().matches(uuidRegex);
    }

    public long getTokenCreationTimestamp() {
        return shared_preferences.getLong(KEY_TOKEN_CREATED, 0);
    }
}