package com.jasoftsolutions.mikhuna.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Hugo on 27/03/2015.
 */
public class EncryptUtil {

//    public static byte[] toSHA1(String text){
//        MessageDigest digest = null;
//
//        try {
//            digest = MessageDigest.getInstance("SHA-1");
//            digest.reset();
//            digest.update(text.getBytes("UTF-8"));
//
//            digest.digest();
//            return digest.digest();
//        } catch (NoSuchAlgorithmException e) {
//        } catch (UnsupportedEncodingException e) {
//        }
//
//        return null;
//    }

    public static String toSHA1(String text){
        byte[] digest, buffer;
        MessageDigest md = null;

        try {
            buffer = text.getBytes("UTF-8");
            md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(buffer);

            digest  = md.digest();

            String hash = "";
            for (byte aux : digest){
                int b = aux & 0xff;
                if (Integer.toHexString(b).length() == 1) hash += "0";
                hash += Integer.toHexString(b);
            }

            return hash;
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }

        return null;
    }


}
