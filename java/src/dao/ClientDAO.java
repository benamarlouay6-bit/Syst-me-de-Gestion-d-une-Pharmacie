package dao;

import ui.utils.DatabaseConnection;
import java.sql.*;

public class ClientDAO {

    public int getOrCreateClientByCin(String cin) throws SQLException {

        String check = "SELECT idClient FROM client WHERE cin = ?";
        String insert = "INSERT INTO client (nom, cin) VALUES (?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {

            try (PreparedStatement ps = con.prepareStatement(check)) {
                ps.setString(1, cin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getInt("idClient");
                }
            }

            try (PreparedStatement ps =
                         con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, "Client " + cin);
                ps.setString(2, cin);
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                return keys.getInt(1);
            }
        }
    }
}
