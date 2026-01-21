package ui.utils; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	public static String url = "jdbc:mysql://localhost:3306/pharma"
	           + "?useSSL=false"
	           + "&allowPublicKeyRetrieval=true"
	           + "&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "eyaabbes29204518";

    public static Connection getConnection() {
        try {
            Connection cnx = DriverManager.getConnection(url, USER, PASSWORD);
            System.out.println("✅ Connexion à MySQL réussie");
            return cnx;
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion à MySQL");
            e.printStackTrace();
            return null;
        }
    }
}
