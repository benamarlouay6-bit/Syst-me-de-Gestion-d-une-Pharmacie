package dao;

import model.Vente;
import ui.utils.DatabaseConnection;

import java.sql.*;

import exception.StockInsuffisantException;

public class VenteDAO {

	public void enregistrerVente(
	        Date dateVente,
	        String nomMedicament,
	        int quantite,
	        String cinClient
	) {

	    String checkClient =
	            "SELECT idClient FROM client WHERE cin = ?";

	    String insertClient =
	            "INSERT INTO client (nom, cin) VALUES (?, ?)";

	    String checkProduit =
	            "SELECT prixC, quantiteStock, seuilAlerte FROM produit WHERE nom = ?";

	    String insertVente =
	            "INSERT INTO vente (dateVente, quantite, montantTotal, nom_medicamment, idClient) " +
	            "VALUES (?, ?, ?, ?, ?)";

	    String updateStock =
	            "UPDATE produit SET quantiteStock = quantiteStock - ? WHERE nom = ?";

	    try (Connection con = DatabaseConnection.getConnection()) {

	        con.setAutoCommit(false);

	       
	        int idClient;

	        try (PreparedStatement ps = con.prepareStatement(checkClient)) {
	            ps.setString(1, cinClient);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                idClient = rs.getInt("idClient");
	            } else {
	                try (PreparedStatement psInsert =
	                             con.prepareStatement(insertClient, Statement.RETURN_GENERATED_KEYS)) {

	                    psInsert.setString(1, "Client " + cinClient);
	                    psInsert.setString(2, cinClient);
	                    psInsert.executeUpdate();

	                    ResultSet keys = psInsert.getGeneratedKeys();
	                    keys.next();
	                    idClient = keys.getInt(1);
	                }
	            }
	        }

	        
	        double prix;
	        int stock;
	        int seuil;

	        try (PreparedStatement ps = con.prepareStatement(checkProduit)) {
	            ps.setString(1, nomMedicament);
	            ResultSet rs = ps.executeQuery();

	            if (!rs.next()) {
	                throw new RuntimeException("Médicament introuvable !");
	            }

	            prix  = rs.getDouble("prixC");
	            stock = rs.getInt("quantiteStock");
	            seuil = rs.getInt("seuilAlerte");
	        }

	        if (stock < quantite) {
	            throw new RuntimeException("Stock insuffisant !");
	        }

	        if (stock <= seuil) {
	            System.out.println(" ALERTE : Stock sous le seuil !");
	        }

	        double montant = prix * quantite;

	        try (PreparedStatement ps = con.prepareStatement(insertVente)) {
	            ps.setDate(1, dateVente);
	            ps.setInt(2, quantite);
	            ps.setDouble(3, montant);
	            ps.setString(4, nomMedicament);
	            ps.setInt(5, idClient);
	            ps.executeUpdate();
	        }

	        
	        try (PreparedStatement ps = con.prepareStatement(updateStock)) {
	            ps.setInt(1, quantite);
	            ps.setString(2, nomMedicament);
	            ps.executeUpdate();
	        }

	        con.commit();
	        System.out.println(" Vente enregistrée avec succès");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

    public ResultSet getAllVentes() throws SQLException {
        String sql = "SELECT * FROM vente ORDER BY dateVente DESC";
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        return st.executeQuery(sql);
    }
}
