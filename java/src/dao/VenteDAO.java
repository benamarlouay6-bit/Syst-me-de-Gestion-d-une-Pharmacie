package dao;

import model.Vente;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenteDAO {

    package dao;

import model.Vente;
import util.DatabaseConnection;

import java.sql.*;

public class VenteDAO {

    public void ajouterVente(Vente v) throws SQLException {

        String sqlGetProduit =
                "SELECT prix, quantiteStock, nom_medicamment " +
                "FROM produit WHERE idProduit = ?";

        String sqlInsertVente =
                "INSERT INTO vente (dateVente, quantite, montantTotal, nom_medicamment, idProduit, idClient) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlUpdateStock =
                "UPDATE produit SET quantiteStock = quantiteStock - ? " +
                "WHERE idProduit = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false); // 🔒 début transaction

            try {

                double prix;
                int stockActuel;
                String nomMed;

                // 1) Récupérer prix + stock + nom du produit
                try (PreparedStatement psGet = con.prepareStatement(sqlGetProduit)) {
                    psGet.setInt(1, v.getIdProduit());

                    try (ResultSet rs = psGet.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Produit introuvable (idProduit=" + v.getIdProduit() + ")");
                        }

                        prix = rs.getDouble("prix");                 // <-- adapte si "prixVente"
                        stockActuel = rs.getInt("quantiteStock");
                        nomMed = rs.getString("nom_medicamment");   // <-- adapte si "nomMedicament"
                    }
                }

                // 2) Vérifier stock suffisant (sinon on annule)
                if (stockActuel < v.getQuantite()) {
                    throw new SQLException("Stock insuffisant pour '" + nomMed + "'. Stock=" +
                            stockActuel + ", demandé=" + v.getQuantite());
                }

                // 3) Calcul montant total
                double montantTotal = v.getQuantite() * prix;

                // 4) INSERT vente
                try (PreparedStatement psVente = con.prepareStatement(sqlInsertVente)) {
                    psVente.setDate(1, v.getDateVente());
                    psVente.setInt(2, v.getQuantite());
                    psVente.setDouble(3, montantTotal);
                    psVente.setString(4, nomMed);          // on sauvegarde le nom depuis produit
                    psVente.setInt(5, v.getIdProduit());
                    psVente.setInt(6, v.getIdClient());

                    psVente.executeUpdate();
                }

                // 5) UPDATE stock produit (diminue)
                try (PreparedStatement psStock = con.prepareStatement(sqlUpdateStock)) {
                    psStock.setInt(1, v.getQuantite());
                    psStock.setInt(2, v.getIdProduit());
                    psStock.executeUpdate();
                }

                con.commit();
                System.out.println("✅ Vente enregistrée + stock mis à jour.");

            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }
}

public void supprimerVente(int idVente) throws SQLException {
        String sql = "DELETE FROM vente WHERE idVente = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVente);
            ps.executeUpdate();
        }
    }

    
}
