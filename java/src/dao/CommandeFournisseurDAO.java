package dao;

import model.CommandeFournisseur;
import exception.FournisseurNotFoundException;
import exception.MedicamentNotFoundException;
import exception.AnnulationCommandeImpossibleException;
import exception.ReceptionCommandeImpossibleException;
import ui.utils.DatabaseConnection;

import java.sql.*;

public class CommandeFournisseurDAO {
	public ResultSet getAllCommandes() throws SQLException {
	    String sql = "SELECT * FROM commandefournisseur";
	    Connection con = DatabaseConnection.getConnection();
	    Statement st = con.createStatement();
	    return st.executeQuery(sql);
	}


    public void creerCommande(CommandeFournisseur c)
            throws FournisseurNotFoundException, MedicamentNotFoundException {

        String checkFournisseur =
                "SELECT id FROM fournisseur WHERE id = ?";

        String checkMedicament =
                "SELECT fm.prixFournisseur " +
                "FROM medicament m " +
                "JOIN fournisseur_medicament fm ON m.idMedicament = fm.idMedicament " +
                "WHERE m.nom = ? AND fm.idFournisseur = ?";

        String insertCommande =
                "INSERT INTO commandefournisseur " +
                "(dateCommande, quantite, montantTotal, etat, idFournisseur, nom_medicamment) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            
            try (PreparedStatement ps = con.prepareStatement(checkFournisseur)) {
                ps.setInt(1, c.getIdFournisseur());
                if (!ps.executeQuery().next()) {
                    throw new FournisseurNotFoundException("Fournisseur introuvable");
                }
            }

            
            double prix;
            try (PreparedStatement ps = con.prepareStatement(checkMedicament)) {
                ps.setString(1, c.getNom_medicamment());
                ps.setInt(2, c.getIdFournisseur());

                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new MedicamentNotFoundException(
                            "Ce fournisseur ne fournit pas ce médicament"
                    );
                }
                prix = rs.getDouble("prixFournisseur");
            }

            double montant = prix * c.getQuantite();
            c.setMontantTotal(montant);

            
            try (PreparedStatement ps = con.prepareStatement(insertCommande)) {
                ps.setDate(1, c.getDateCommande());
                ps.setInt(2, c.getQuantite());
                ps.setDouble(3, montant);
                ps.setString(4, c.getEtat());
                ps.setInt(5, c.getIdFournisseur());
                ps.setString(6, c.getNom_medicamment());
                ps.executeUpdate();
            }

            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void annulerCommande(int idCommande)
            throws AnnulationCommandeImpossibleException {

        String sqlCheckEtat =
            "SELECT etat FROM commandefournisseur WHERE idCommande = ?";

        String sqlAnnuler =
            "UPDATE commandefournisseur SET etat = 'ANNULEE' WHERE idCommande = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false); 

            try {

                String etatCommande;

                
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

                
                if ("REÇUE".equalsIgnoreCase(etatCommande)) {
                    throw new AnnulationCommandeImpossibleException(
                        "Annulation impossible : la commande est déjà reçue."
                    );
                }

               
                try (PreparedStatement psUpdate = con.prepareStatement(sqlAnnuler)) {
                    psUpdate.setInt(1, idCommande);
                    psUpdate.executeUpdate();
                }

                con.commit();
                System.out.println("Commande annulée avec succès.");

            } catch (Exception e) {
                con.rollback(); 
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void receptionCommande(int idCommande)
            throws ReceptionCommandeImpossibleException {

        String sqlGetCommande =
            "SELECT nom_medicamment, quantite, etat " +
            "FROM commandefournisseur WHERE idCommande = ?";

        String sqlUpdateEtat =
            "UPDATE commandefournisseur SET etat = 'RECUE' WHERE idCommande = ?";

        String sqlUpdateStock =
            "UPDATE produit SET quantiteStock = quantiteStock + ? " +
            "WHERE nom = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false);

            try {

                String nomMedicament;
                int quantite;
                String etat;

               
                try (PreparedStatement psCmd = con.prepareStatement(sqlGetCommande)) {
                    psCmd.setInt(1, idCommande);
                    ResultSet rs = psCmd.executeQuery();

                    if (!rs.next()) {
                        throw new ReceptionCommandeImpossibleException(
                            "Commande introuvable (id=" + idCommande + ")"
                        );
                    }

                    nomMedicament = rs.getString("nom_medicamment");
                    quantite = rs.getInt("quantite");
                    etat = rs.getString("etat");
                }

                
                if ("ANNULEE".equalsIgnoreCase(etat)) {
                    throw new ReceptionCommandeImpossibleException(
                        "Réception impossible : la commande est annulée."
                    );
                }

                if ("RECUE".equalsIgnoreCase(etat)) {
                    throw new ReceptionCommandeImpossibleException(
                        "Réception impossible : la commande est déjà reçue."
                    );
                }

               
                try (PreparedStatement psStock = con.prepareStatement(sqlUpdateStock)) {
                    psStock.setInt(1, quantite);
                    psStock.setString(2, nomMedicament);
                    psStock.executeUpdate(); 
                }

               
                try (PreparedStatement psEtat = con.prepareStatement(sqlUpdateEtat)) {
                    psEtat.setInt(1, idCommande);
                    psEtat.executeUpdate();
                }

                con.commit();
                System.out.println("Commande reçue. Stock mis à jour si le produit existe.");

            } catch (Exception e) {
                con.rollback(); 
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
