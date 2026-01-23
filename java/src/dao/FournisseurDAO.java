package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Fournisseur;


import java.sql.*;

public class FournisseurDAO {

    // 🔹 RÉCUPÉRER TOUS LES FOURNISSEURS
    public ObservableList<Fournisseur> getAll() {

        ObservableList<Fournisseur> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM fournisseur";

        try (Connection cn =DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Fournisseur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("telephone"),
                        rs.getString("mail")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    String nom;
    String telephone;
    String mail;
    Fournisseur f=new Fournisseur( nom,  telephone,  mail);
    // 🔹 AJOUTER FOURNISSEUR
    public void ajouter(Fournisseur f) {

        String sql = "INSERT INTO fournisseur(nom, telephone, mail) VALUES(?, ?, ?)";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, f.getNom());
            ps.setString(2, f.getTelephone());
            ps.setString(3, f.getMail());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 MODIFIER FOURNISSEUR
    public void modifier(int id, String nom, String telephone, String mail) {

        String sql = "UPDATE fournisseur SET nom=?, telephone=?, mail=? WHERE id=?";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nom);
            ps.setString(2, telephone);
            ps.setString(3, mail);
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 SUPPRIMER FOURNISSEUR
    public void supprimer(int id) {

        String sql = "DELETE FROM fournisseur WHERE id=?";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
