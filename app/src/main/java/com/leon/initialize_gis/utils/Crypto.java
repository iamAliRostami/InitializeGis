package com.leon.initialize_gis.utils;

import android.util.Base64;

public class Crypto {

    public static String encrypt(String password) {
        String encodedPassword = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
        return Base64.encodeToString(encodedPassword.getBytes(), Base64.NO_CLOSE);
    }

    public static String decrypt(String encodedPassword) {
        String password = new String(Base64.decode(encodedPassword, Base64.DEFAULT));
        return new String(Base64.decode(password, Base64.DEFAULT));
    }
}
