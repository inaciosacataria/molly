package com.decode.molly.Helper;

import android.util.Base64;

public class Base64Helper {

    public static String encriptar(String valor){
        return Base64.encodeToString(valor.getBytes(),Base64.DEFAULT).replaceAll("(\\r|\\n)","");
    }
    public static String desincriptar(String valor){
        return new String(Base64.decode(valor.getBytes(),Base64.DEFAULT));
    }
}
