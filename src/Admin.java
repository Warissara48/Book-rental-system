import javax.swing.*;
import java.awt.*;

public class Admin {
    private static final String USERNAME = "Alice";
    private static final String PASSWORD = "1234";

    public boolean validateCredentials(String username, String password) {
        return USERNAME.equals(username) && PASSWORD.equals(password);
    }
}
