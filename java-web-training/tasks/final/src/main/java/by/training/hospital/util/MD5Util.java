package by.training.hospital.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String getMD5String(String st) {
        String md5Hex = DigestUtils.md5Hex(st);

        return md5Hex;
    }
}
