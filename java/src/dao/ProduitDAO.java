package dao;

import model.Produit;
import exception.ProduitNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Produit> getAllProduits() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit";
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            Produit p = new Produit(
                    rs.getString("nom"),
                    rs.getDouble("prixF"),
                    rs.getDouble("prixC"),
                    rs.getInt("quantiteStock"),
                    rs.getInt("seuilAlerte")
            );
            p.setIdProduit(rs.getInt("idProduit"));
            produits.add(p);
        }

        rs.close();
        st.close();
        con.close();
        return produits;
    }

    public List<Produit> rechercherProduitParNom(String nom)
            throws SQLException, ProduitNotFoundException {

        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit WHERE nom LIKE ?";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, "%" + nom + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Produit p = new Produit(
                    rs.getString("nom"),
                    rs.getDouble("prixF"),
                    rs.getDouble("prixC"),
                    rs.getInt("quantiteStock"),
                    rs.getInt("seuilAlerte")
            );
            p.setIdProduit(rs.getInt("idProduit"));
            produits.add(p);
        }

        rs.close();
        ps.close();
        con.close();

        if (produits.isEmpty()) {
            throw new ProduitNotFoundException("Aucun produit trouvé avec ce nom");
        }

        return produits;
    }

    public void supprimerProduit(int idProduit) throws SQLException {
        String sql = "DELETE FROM produit WHERE idProduit = ?";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, idProduit);
        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void modifierProduit(Produit p) throws SQLException {
        String sql = "UPDATE produit SET nom=?, prixF=?, prixC=?, quantiteStock=?, seuilAlerte=? WHERE idProduit=?";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, p.getNom());
        ps.setDouble(2, p.getPrixF());
        ps.setDouble(3, p.getPrixC());
        ps.setInt(4, p.getQuantiteStock());
        ps.setInt(5, p.getSeuilAlerte());
        ps.setInt(6, p.getIdProduit());

        ps.executeUpdate();
        ps.close();
        con.close();
    }
}
