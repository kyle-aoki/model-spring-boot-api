package unc.book_trader.library;

import java.util.Random;

public class RandomString {

    private static final Random RAND = new Random();
    private static final String POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String ofLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(POOL.charAt(RAND.nextInt(POOL.length())));
        }
        return sb.toString();
    }
}
