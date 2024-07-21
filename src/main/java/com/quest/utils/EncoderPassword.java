package com.quest.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class EncoderPassword {
    /**
     * [hashPassword]
     * Hash the password using BCrypt
     * 
     * @param password
     * @return String
     */
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    /**
     * [verifyPassword]
     * Verify the password using BCrypt
     * 
     * @param password
     * @param hash
     * @return boolean
     */
    public static boolean verifyPassword(String password, String hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
        return result.verified;
    }
}
