package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Client;

import exception.ClientNotFoundException;
import java.sql.*;

public class ClientDAO {

    // LISTE
    public ObservableList<Client> getAll() {
        ObservableList<Client> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Client(
                        rs.getInt("idClient"),
                        rs.getString("nom"),
                        rs.getString("telephone"),
                        rs.getString("cin")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // AJOUTER
    public void ajouter(String nom, String telephone, String cin) {
        String sql = "INSERT INTO client(nom, telephone, cin) VALUES(?, ?, ?)";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nom);
            ps.setString(2, telephone);
            ps.setString(3, cin);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // MODIFIER
    public void modifier(int id, String nom, String telephone, String cin) {
        String sql = "UPDATE client SET nom=?, telephone=?, cin=? WHERE idClient=?";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nom);
            ps.setString(2, telephone);
            ps.setString(3, cin);
            ps.setInt(4, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // SUPPRIMER
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM client WHERE idClient=?";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // CHERCHER

    public ObservableList<Client> chercher(String mot) throws ClientNotFoundException {

        ObservableList<Client> list = FXCollections.observableArrayList();
        String sql = """
            SELECT * FROM client
            WHERE nom LIKE ? OR telephone LIKE ? OR cin LIKE ?
        """;

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, "%" + mot + "%");
            ps.setString(2, "%" + mot + "%");
            ps.setString(3, "%" + mot + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Client(
                        rs.getInt("idClient"),
                        rs.getString("nom"),
                        rs.getString("telephone"),
                        rs.getString("cin")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        
        if (list.isEmpty()) {
            throw new ClientNotFoundException(
                    "Aucun client trouvé pour : " + mot
            );
        }

        return list;
    }

}
