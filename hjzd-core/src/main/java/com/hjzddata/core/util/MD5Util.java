package com.hjzddata.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密类（封装jdk自带的md5加密方法）
 *
 * @author fengshuonan
 * @date 2016年12月2日 下午4:14:22
 */
public class MD5Util {

    /** add by eric 2018-09-29 start **/
    static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    public static String encrypt(String source) {
        return encodeMd5(source.getBytes());
    }

    public static String encrypt(String source,String charset) {
        try {
            return encodeMd5(source.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  null;
    }


    public static String encrypt(String source,String charset,boolean isHEX){
       if(isHEX) {
           byte [] buff = null;
           try {
               buff = MessageDigest.getInstance("MD5").digest(source.getBytes(charset));
               return toHex(buff);
           } catch (NoSuchAlgorithmException e) {
               return null;
           } catch (UnsupportedEncodingException e) {
               return null;
           }
       }else {
           return  null;
       }
    }

    /**
     * @param bytes
     * @return
     */
    private static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    /** add by eric 2018-09-29 end **/


    private static String encodeMd5(byte[] source) {
        try {
            return encodeHex(MessageDigest.getInstance("MD5").digest(source));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static String encodeHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10)
                buffer.append("0");
            buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(encrypt("123456"));
    }
}
