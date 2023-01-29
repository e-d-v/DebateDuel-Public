package com.evanv.debateduel.logic;

import android.content.SharedPreferences;

public class EncryptSharedPreferences {
    private SharedPreferences mSP;
    private SharedPreferences.Editor mEditor;

    public EncryptSharedPreferences(SharedPreferences sp) {
        this.mSP = sp;
    }

    public void edit() {
        mEditor = mSP.edit();
    }

    public void apply() {
        mEditor.apply();
    }

    public void putString(String key, String value) {
        // TODO: Encrypt value

        mEditor.putString(key, value);
    }

    public String getString(String key, String defaultValue) {
        String toReturn = mSP.getString(key, defaultValue);

        if (toReturn.equals(defaultValue)) {
            return defaultValue;
        }

        // TODO: Decrypt toReturn

        return toReturn;
    }
}
