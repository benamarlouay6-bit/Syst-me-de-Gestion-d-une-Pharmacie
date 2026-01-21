package dao;

import model.Vente;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenteDAO {

    public void ajouterVente(Vente v) throws SQLException {
        String sql = "INSERT INTO vente (dateVente, quantite, montantTotal, idClient, idProduit) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, v.getDateVente());
            ps.setInt(2, v.getQuantite());
            ps.setDouble(3, v.getMontantTotal());
            ps.setInt(4, v.getIdClient());
            ps.setInt(5, v.getIdProduit());

            ps.executeUpdate();
        }
    }

    public Vente getVenteById(int idVente) throws SQLException {
        String sql = "SELECT * FROM vente WHERE idVente = ?";
        Vente v = null;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    v = mapRow(rs);
                }
            }
        }
        return v;
    }

    // READ ALL
    public List<Vente> getAllVentes() throws SQLException {
        String sql = "SELECT * FROM vente";
        List<Vente> ventes = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ventes.add(mapRow(rs));
            }
        }
        return ventes;
    }

    public void supprimerVente(int idVente) throws SQLException {
        String sql = "DELETE FROM vente WHERE idVente = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVente);
            ps.executeUpdate();
        }
    }

    // Mapper ResultSet -> Vente
    private Vente mapRow(ResultSet rs) throws SQLException {
        Vente v = new Vente();
        v.setIdVente(rs.getInt("idVente"));
        v.setDateVente(rs.getDate("dateVente"));
        v.setQuantite(rs.getInt("quantite"));
        v.setMontantTotal(rs.getDouble("montantTotal"));
        v.setIdClient(rs.getInt("idClient"));
        v.setIdProduit(rs.getInt("idProduit"));
        return v;
    }
}
