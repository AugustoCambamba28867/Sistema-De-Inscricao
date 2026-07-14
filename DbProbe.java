import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbProbe {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/sdpfrequencia";
        String user = "postgres";
        String password = "atac";
        System.out.println("Connecting to: " + url + " user=" + user);
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("SELECT id, username, password, papel, must_change_password FROM administrador");
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Connected successfully.");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("username") + " / " + rs.getString("password") + " / " + rs.getString("papel") + " / must_change_password=" + rs.getBoolean("must_change_password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
