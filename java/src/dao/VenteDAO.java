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
            "SELECT prix, quantiteStock " +
            "FROM produit WHERE nom = ?";

        String sqlInsertVente =
            "INSERT INTO vente (dateVente, quantite, montantTotal, nom_medicamment, idClient) " +
            "VALUES (?, ?, ?, ?, ?)";

        String sqlUpdateStock =
            "UPDATE produit SET quantiteStock = quantiteStock - ? " +
            "WHERE nom = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false); // 🔒 début transaction

            try {

                double prix;
                int stockActuel;

                // 1️⃣ Récupérer prix + stock du produit par NOM
                try (PreparedStatement psGet = con.prepareStatement(sqlGetProduit)) {
                    psGet.setString(1, v.getNom_medicamment());

                    ResultSet rs = psGet.executeQuery();

                    if (!rs.next()) {
                        throw new SQLException(
                            "Produit introuvable : " + v.getNom_medicamment()
                        );
                    }

                    prix = rs.getDouble("prix");
                    stockActuel = rs.getInt("quantiteStock");
                }

                // 2️⃣ Vérifier stock suffisant
                if (stockActuel < v.getQuantite()) {
                    throw new SQLException(
                        "Stock insuffisant pour " + v.getNom_medicamment()
                    );
                }

                // 3️⃣ Calcul du montant total
                double montantTotal = v.getQuantite() * prix;
                v.setMontantTotal(montantTotal);

                // 4️⃣ Insertion de la vente
                try (PreparedStatement psVente = con.prepareStatement(sqlInsertVente)) {
                    psVente.setDate(1, v.getDateVente());
                    psVente.setInt(2, v.getQuantite());
                    psVente.setDouble(3, montantTotal);
                    psVente.setString(4, v.getNom_medicamment());
                    psVente.setInt(5, v.getIdClient());

                    psVente.executeUpdate();
                }

                // 5️⃣ Mise à jour du stock (diminue)
                try (PreparedStatement psStock = con.prepareStatement(sqlUpdateStock)) {
                    psStock.setInt(1, v.getQuantite());
                    psStock.setString(2, v.getNom_medicamment());
                    psStock.executeUpdate();
                }

                con.commit();
                System.out.println("✅ Vente enregistrée et stock mis à jour.");

            } catch (Exception e) {
                con.rollback(); // ❌ annuler si problème
                throw e;
            }
        }
    }
}


    public void afficherVentes() {

        String sql = "SELECT * FROM vente ORDER BY dateVente DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("========== LISTE DES VENTES ==========");

            while (rs.next()) {
                System.out.println(
                    "ID Vente : " + rs.getInt("idVente") +
                    " | Date : " + rs.getDate("dateVente") +
                    " | Médicament : " + rs.getString("nom_medicamment") +
                    " | Quantité : " + rs.getInt("quantite") +
                    " | Montant Total : " + rs.getDouble("montantTotal") +
                    " | Client ID : " + rs.getInt("idClient")
                );
            }

            System.out.println("======================================");

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'affichage des ventes");
            e.printStackTrace();
        }
    }
}


    
}
