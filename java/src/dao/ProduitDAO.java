package dao;

import model.Produit;
import util.DatabaseConnection;

import java.sql.*;

public class ProduitDAO {

    public void ajouterProduit(Produit p) throws SQLException {
        String sql = "INSERT INTO produit (nom, prixF, prixC, quantiteStock, seuilAlerte) VALUES (?, ?, ?, ?, ?)";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, p.getNom());
        ps.setDouble(2, p.getPrixF());
        ps.setDouble(3, p.getPrixC());
        ps.setInt(4, p.getQuantiteStock());
        ps.setInt(5, p.getSeuilAlerte());

        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public Produit getProduitById(int id) throws SQLException {
        String sql = "SELECT * FROM produit WHERE idProduit = ?";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        Produit p = null;

        if (rs.next()) {
            p = new Produit();
            p.setIdProduit(rs.getInt("idProduit"));
            p.setNom(rs.getString("nom"));
            p.setPrixF(rs.getDouble("prixF"));
            p.setPrixC(rs.getDouble("prixC"));
            p.setQuantiteStock(rs.getInt("quantiteStock"));
            p.setSeuilAlerte(rs.getInt("seuilAlerte"));
        }

        rs.close();
        ps.close();
        con.close();
        return p;
    }
}
