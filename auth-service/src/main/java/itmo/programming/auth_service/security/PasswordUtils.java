package itmo.programming.auth_service.security;

import java.security.NoSuchAlgorithmException;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hash(String password) throws NoSuchAlgorithmException {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean verify(String stored, String current) throws NoSuchAlgorithmException {
        if (stored == null || current == null) return false;
        return BCrypt.checkpw(current, stored);
    }

}
