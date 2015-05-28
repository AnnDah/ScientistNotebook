package utility;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility to hash a password
 *
 * @author niffe
 * @version 1.0, 11/05/15
 */
public final class PasswordUtility {
    /**
     * Generates a hash derived from the input parameter with the specified algorithm
     * @param password The password to be hashed
     * @return A hashed string
     * @throws NoSuchAlgorithmException if the algorithm used doesn't exist
     * @throws IOException if the data can't be converted to a string
     */
    public static String generateHash(String password) throws NoSuchAlgorithmException, IOException {
        MessageDigest mdSha1 =  MessageDigest.getInstance("SHA-1");
        mdSha1.update(password.getBytes("ASCII"));
        byte[] data = mdSha1.digest();
        return convertToHex(data);
    }
    /**
     * Convert a byte array to a string
     * @param data The data to be converted
     * @return A string representation of the array
     * @throws IOException If the array can't be converted to a string
     */
    private static String convertToHex(byte[] data) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
