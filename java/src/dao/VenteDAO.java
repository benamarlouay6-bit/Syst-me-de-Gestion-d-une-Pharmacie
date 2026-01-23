package dao;

import ui.utils.DatabaseConnection;
import exception.StockInsuffisantException;
import model.Produit;

import java.sql.*;

public class VenteDAO {
	public ResultSet getAllVentes() throws SQLException {

	    String sql =
	            "SELECT idVente, dateVente, quantite, montantTotal, " +
	            "nom_medicamment, idClient " +
	            "FROM vente ORDER BY dateVente DESC";

	    Connection con = DatabaseConnection.getConnection();
	    Statement st = con.createStatement();

	    return st.executeQuery(sql);
	}

    public Produit enregistrerVente(
            Date dateVente,
            String nomMedicament,
            int quantite,
            int idClient
    ) throws SQLException, StockInsuffisantException {

        Connection con = DatabaseConnection.getConnection();
        con.setAutoCommit(false);

        try {

            String sqlProduit =
                    "SELECT prixC, quantiteStock, seuilAlerte " +
                    "FROM produit WHERE nom = ?";

            PreparedStatement psProduit = con.prepareStatement(sqlProduit);
            psProduit.setString(1, nomMedicament);
            ResultSet rsProduit = psProduit.executeQuery();

            if (!rsProduit.next()) {
                throw new SQLException("Produit introuvable");
            }

            double prixC = rsProduit.getDouble("prixC");
            int stock = rsProduit.getInt("quantiteStock");
            int seuil = rsProduit.getInt("seuilAlerte");

            if (stock < quantite) {
                throw new StockInsuffisantException("Stock insuffisant");
            }

            String sqlClient = "SELECT idClient FROM client WHERE idClient = ?";
            PreparedStatement psClient = con.prepareStatement(sqlClient);
            psClient.setInt(1, idClient);
            ResultSet rsClient = psClient.executeQuery();

            if (!rsClient.next()) {
                throw new SQLException("Client inexistant");
            }

            String insertVente =
                    "INSERT INTO vente (dateVente, quantite, montantTotal, nom_medicamment, idClient) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement psVente = con.prepareStatement(insertVente);
            psVente.setDate(1, dateVente);
            psVente.setInt(2, quantite);
            psVente.setDouble(3, quantite * prixC);
            psVente.setString(4, nomMedicament);
            psVente.setInt(5, idClient);
            psVente.executeUpdate();

            String updateStock =
                    "UPDATE produit SET quantiteStock = quantiteStock - ? WHERE nom = ?";

            PreparedStatement psUpdate = con.prepareStatement(updateStock);
            psUpdate.setInt(1, quantite);
            psUpdate.setString(2, nomMedicament);
            psUpdate.executeUpdate();

            con.commit();

            Produit p = new Produit(
                    nomMedicament,
                    0,
                    prixC,
                    stock - quantite,
                    seuil
            );

            return p;

        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }
}
