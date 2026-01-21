package dao;

import model.CommandeFournisseur;
import util.DatabaseConnection;
import exceptions.FournisseurNotFoundException;
import exceptions.MedicamentNotFoundException;
import exceptions.AnnulationCommandeImpossibleException;
import exceptions.ReceptionCommandeImpossibleException;

import java.sql.*;

public class CommandeFournisseurDAO {
    


public void creerCommande(CommandeFournisseur c)
        throws FournisseurNotFoundException, MedicamentNotFoundException {

    String sqlCheckFournisseur =
        "SELECT idFournisseur FROM fournisseur WHERE idFournisseur = ?";

    String sqlCheckMedicament =
        "SELECT fm.prixFournisseur " +
        "FROM medicament m " +
        "JOIN fournisseur_medicament fm ON m.idMedicament = fm.idMedicament " +
        "WHERE m.nom = ? AND fm.idFournisseur = ?";

    String sqlInsert =
        "INSERT INTO commandefournisseur " +
        "(dateCommande, quantite, montantTotal, etat, idFournisseur, nomMedicament) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection con = DatabaseConnection.getConnection()) {

        con.setAutoCommit(false); // 🔒 début transaction

        try {

            // 1️⃣ Vérifier fournisseur
            try (PreparedStatement psF = con.prepareStatement(sqlCheckFournisseur)) {
                psF.setInt(1, c.getIdFournisseur());
                ResultSet rsF = psF.executeQuery();

                if (!rsF.next()) {
                    throw new FournisseurNotFoundException(
                        "Fournisseur introuvable (id=" + c.getIdFournisseur() + ")"
                    );
                }
            }

            // 2️⃣ Vérifier médicament + récupérer prix
            double prixFournisseur;

            try (PreparedStatement psM = con.prepareStatement(sqlCheckMedicament)) {
                psM.setString(1, c.getNom_medicamment());
                psM.setInt(2, c.getIdFournisseur());

                ResultSet rsM = psM.executeQuery();

                if (!rsM.next()) {
                    throw new MedicamentNotFoundException(
                        "Le médicament '" + c.getNom_medicamment() +
                        "' n'est pas fourni par ce fournisseur."
                    );
                }

                prixFournisseur = rsM.getDouble("prixFournisseur");
            }

            // 3️⃣ Calcul du montant total
            double montantTotal = c.getQuantite() * prixFournisseur;
            c.setMontantTotal(montantTotal);

            // 4️⃣ Insertion commande (avec nomMedicament)
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                psInsert.setDate(1, c.getDateCommande());
                psInsert.setInt(2, c.getQuantite());
                psInsert.setDouble(3, montantTotal);
                psInsert.setString(4, c.getEtat());
                psInsert.setInt(5, c.getIdFournisseur());
                psInsert.setString(6, c.getNom_medicamment());

                psInsert.executeUpdate();
            }

            con.commit();
            System.out.println("✅ Commande fournisseur créée avec succès.");

        } catch (Exception e) {
            con.rollback(); // ❌ annulation si erreur
            throw e;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

import exceptions.AnnulationCommandeImpossibleException;
import java.sql.*;

public void annulerCommande(int idCommande)
        throws AnnulationCommandeImpossibleException {

    String sqlCheckEtat =
        "SELECT etat FROM commandefournisseur WHERE idCommande = ?";

    String sqlAnnuler =
        "UPDATE commandefournisseur SET etat = 'ANNULEE' WHERE idCommande = ?";

    try (Connection con = DatabaseConnection.getConnection()) {

        con.setAutoCommit(false); // 🔒 transaction

        try {

            String etatCommande;

            // 1️⃣ Vérifier l'état de la commande
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheckEtat)) {
                psCheck.setInt(1, idCommande);
                ResultSet rs = psCheck.executeQuery();

                if (!rs.next()) {
                    throw new AnnulationCommandeImpossibleException(
                        "Commande introuvable (id=" + idCommande + ")"
                    );
                }

                etatCommande = rs.getString("etat");
            }

            // 2️⃣ Vérifier règle métier
            if ("REÇUE".equalsIgnoreCase(etatCommande)) {
                throw new AnnulationCommandeImpossibleException(
                    "Annulation impossible : la commande est déjà reçue."
                );
            }

            // 3️⃣ Annuler la commande
            try (PreparedStatement psUpdate = con.prepareStatement(sqlAnnuler)) {
                psUpdate.setInt(1, idCommande);
                psUpdate.executeUpdate();
            }

            con.commit();
            System.out.println("✅ Commande annulée avec succès.");

        } catch (Exception e) {
            con.rollback(); // ❌ annuler si erreur
            throw e;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


  




public void receptionCommande(int idCommande)
        throws ReceptionCommandeImpossibleException {

    String sqlGetCommande =
        "SELECT nomMedicament, quantite, etat " +
        "FROM commandefournisseur WHERE idCommande = ?";

    String sqlUpdateEtat =
        "UPDATE commandefournisseur SET etat = 'REÇUE' WHERE idCommande = ?";

    String sqlUpdateStock =
        "UPDATE produit SET quantiteStock = quantiteStock + ? " +
        "WHERE nomMedicament = ?";

    try (Connection con = DatabaseConnection.getConnection()) {

        con.setAutoCommit(false); // 🔒 transaction

        try {

            String nomMedicament;
            int quantite;
            String etat;

            // 1️⃣ Récupérer la commande
            try (PreparedStatement psCmd = con.prepareStatement(sqlGetCommande)) {
                psCmd.setInt(1, idCommande);
                ResultSet rs = psCmd.executeQuery();

                if (!rs.next()) {
                    throw new ReceptionCommandeImpossibleException(
                        "Commande introuvable (id=" + idCommande + ")"
                    );
                }

                nomMedicament = rs.getString("nomMedicament");
                quantite = rs.getInt("quantite");
                etat = rs.getString("etat");
            }

            // 2️⃣ Vérifier règles métier
            if ("ANNULÉE".equalsIgnoreCase(etat)) {
                throw new ReceptionCommandeImpossibleException(
                    "Réception impossible : la commande est annulée."
                );
            }

            if ("REÇUE".equalsIgnoreCase(etat)) {
                throw new ReceptionCommandeImpossibleException(
                    "Réception impossible : la commande est déjà reçue."
                );
            }

            // 3️⃣ Mise à jour du stock (SI le produit existe → sinon rien)
            try (PreparedStatement psStock = con.prepareStatement(sqlUpdateStock)) {
                psStock.setInt(1, quantite);
                psStock.setString(2, nomMedicament);
                psStock.executeUpdate(); // ← aucune vérification, aucune exception
            }

            // 4️⃣ Mise à jour de l’état de la commande
            try (PreparedStatement psEtat = con.prepareStatement(sqlUpdateEtat)) {
                psEtat.setInt(1, idCommande);
                psEtat.executeUpdate();
            }

            con.commit();
            System.out.println("✅ Commande reçue. Stock mis à jour si le produit existe.");

        } catch (Exception e) {
            con.rollback(); // ❌ annuler si erreur
            throw e;
        }

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
