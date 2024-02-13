package aoki.kyle.model.library;

import java.security.SecureRandom;
import java.util.Random;

public class RandomString {

    private static final SecureRandom RAND = new SecureRandom();
    private static final String POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String ofLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(POOL.charAt(RAND.nextInt(POOL.length())));
        }
        return sb.toString();
    }
}
