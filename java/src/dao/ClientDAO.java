package dao;

import model.Client;
import util.DatabaseConnection;

import java.sql.*;

public class ClientDAO {

    public void ajouterClient(Client c) throws SQLException {
        String sql = "INSERT INTO client (nom, telephone, cin) VALUES (?, ?, ?)";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, c.getNom());
        ps.setString(2, c.getTelephone());
        ps.setString(3, c.getCin());

        ps.executeUpdate();
        ps.close();
        con.close();
    }
}
