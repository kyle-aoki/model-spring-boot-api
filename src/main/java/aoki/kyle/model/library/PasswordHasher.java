package aoki.kyle.model.library;

import java.security.MessageDigest;

public class PasswordHasher {

    public static String hash(String value) throws Exception {
        return sha512Hash(value);
    }

    // example hash:
    // 975C1AA955D41A7F872C7AF3E2B02EE91904F585722B5B24013363D98575CB47D2CFDD6916A60F45AE653234A6F4139D8F60671B1552DB4A1EDC1C9A4612782B
    private static String sha512Hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(input.getBytes());
        byte[] hashBytes = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            hexString.append(String.format("%02X", hashByte));
        }
        return hexString.toString();
    }
}
