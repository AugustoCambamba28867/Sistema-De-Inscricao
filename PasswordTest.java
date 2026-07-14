import com.gettraining.util.PasswordUtil;
public class PasswordTest {
    public static void main(String[] args) {
        String legacy = "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918";
        System.out.println("legacy valid: " + PasswordUtil.verifyPassword("admin", legacy));
        String hashed = PasswordUtil.hashPassword("admin");
        System.out.println("pbkdf2 hashed: " + hashed);
        System.out.println("pbkdf2 valid: " + PasswordUtil.verifyPassword("admin", hashed));
    }
}
