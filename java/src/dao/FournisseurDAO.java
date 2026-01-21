package dao;

import model.Fournisseur;
import util.DatabaseConnection;

import java.sql.*;

public class FournisseurDAO {

    public void ajouterFournisseur(Fournisseur f) throws SQLException {
        String sql = "INSERT INTO fournisseur (nom, telephone, mail) VALUES (?, ?, ?)";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, f.getNom());
        ps.setString(2, f.getTelephone());
        ps.setString(3, f.getMail());

        ps.executeUpdate();
        ps.close();
        con.close();
    }
}
