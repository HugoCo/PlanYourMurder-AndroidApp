package com.example.planyourmurder.ui.model;
import java.security.MessageDigest;

public class Hashage {

    public static String getHash(String password) throws Exception {


        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convertir le tableau de bits en une format hexadécimal

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }


        return sb.toString();
    }
}

