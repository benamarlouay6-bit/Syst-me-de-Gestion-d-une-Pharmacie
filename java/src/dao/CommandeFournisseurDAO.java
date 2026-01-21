package dao;

import model.CommandeFournisseur;
import util.DatabaseConnection;

import java.sql.*;

public class CommandeFournisseurDAO {


    private double getPrixFournisseur(int idProduit, int idFournisseur, Connection con)
        throws SQLException, FournisseurNotFoundException {

    String sql = "SELECT prix_fournisseur FROM produit WHERE idProduit = ? AND idFournisseur = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idProduit);
        ps.setInt(2, idFournisseur);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("prix_fournisseur");
            }
        }
    }

    throw new FournisseurNotFoundException(
        "Aucun fournisseur trouvé (idFournisseur=" + idFournisseur +
        ") pour le produit idProduit=" + idProduit
    );
}

   import exceptions.FournisseurNotFoundException;
import java.sql.*;

public void creerCommande(CommandeFournisseur c)
        throws FournisseurNotFoundException {

    String insertCommande = "INSERT INTO commandefournisseur " +
        "(dateCommande, quantite, montantTotal, etat, idFournisseur, idProduit) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection con = DatabaseConnection.getConnection()) {

        con.setAutoCommit(false); // 🔒 début transaction

        try {

            
            double prixFournisseur = getPrixFournisseur(
                    c.getIdProduit(),
                    c.getIdFournisseur(),
                    con
            );

            
            double montantTotal = c.getQuantite() * prixFournisseur;

            
            try (PreparedStatement ps = con.prepareStatement(insertCommande)) {

                ps.setDate(1, c.getDateCommande());
                ps.setInt(2, c.getQuantite());
                ps.setDouble(3, montantTotal);
                ps.setString(4, c.getEtat());
                ps.setInt(5, c.getIdFournisseur());
                ps.setInt(6, c.getIdProduit());

                ps.executeUpdate();
            }

            con.commit(); 
            System.out.println("Commande fournisseur créée avec succès !");

        } catch (SQLException | FournisseurNotFoundException e) {
            con.rollback(); 
            throw e;
        }

    } catch (SQLException e) {
        System.err.println("Erreur SQL lors de la création de la commande");
        e.printStackTrace();
    }
}


    public void modifierCommande(CommandeFournisseur c) {
        String sql = "UPDATE commandefournisseur SET " +
                     "dateCommande = ?, quantite = ?, montantTotal = ?, etat = ?, " +
                     "idFournisseur = ?, idProduit = ? WHERE idCommande = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, c.getDateCommande());
            ps.setInt(2, c.getQuantite());
            ps.setDouble(3, c.getMontantTotal());
            ps.setString(4, c.getEtat());
            ps.setInt(5, c.getIdFournisseur());
            ps.setInt(6, c.getIdProduit());
            ps.setInt(7, c.getIdCommande());

            ps.executeUpdate();
            System.out.println("Commande fournisseur modifiée avec succès !");

        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification");
            e.printStackTrace();
        }
    }

    public void annulerCommande(int idCommande) {
        String sql = "UPDATE commandefournisseur SET etat = 'ANNULÉE' WHERE idCommande = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCommande);
            ps.executeUpdate();

            System.out.println("Commande fournisseur annulée.");

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'annulation");
            e.printStackTrace();
        }
    }

    public void receptionnerCommande(int idCommande) {

        String getCommande = "SELECT idProduit, quantite FROM commande_fournisseur WHERE idCommande = ?";
        String updateStock = "UPDATE produit SET quantiteStock = quantiteStock + ? WHERE id = ?";
        String updateCommande = "UPDATE commandefournisseur SET etat = 'REÇUE', dateReception = ? WHERE idCommande = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false); // start transaction

            int idProduit = 0;
            int quantite = 0;

            // 1️⃣ Récupérer la commande
            try (PreparedStatement psGet = con.prepareStatement(getCommande)) {
                psGet.setInt(1, idCommande);
                ResultSet rs = psGet.executeQuery();

                if (rs.next()) {
                    idProduit = rs.getInt("idProduit");
                    quantite = rs.getInt("quantite");
                } else {
                    System.out.println("Commande introuvable.");
                    return;
                }
            }

            // 2️⃣ Mettre à jour le stock
            try (PreparedStatement psStock = con.prepareStatement(updateStock)) {
                psStock.setInt(1, quantite);
                psStock.setInt(2, idProduit);
                psStock.executeUpdate();
            }

            // 3️⃣ Mettre à jour la commande
            try (PreparedStatement psCmd = con.prepareStatement(updateCommande)) {
                psCmd.setDate(1, new Date(System.currentTimeMillis()));
                psCmd.setInt(2, idCommande);
                psCmd.executeUpdate();
            }

            con.commit(); // success

            System.out.println("Commande reçue et stock mis à jour avec succès !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
            
    
    
    public void rapportPerformanceFournisseurs() {
        String sql = "SELECT idFournisseur, SUM(quantite) AS totalLivree " +
                     "FROM commandefournisseur WHERE etat = 'RECUE' GROUP BY idFournisseur";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("🚚 PERFORMANCE DES FOURNISSEURS :");

            while (rs.next()) {
                System.out.println(
                    "Fournisseur ID: " + rs.getInt("idFournisseur") +
                    " | Quantité livrée: " + rs.getInt("totalLivree")
                );  // a afficher par eya :)
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
