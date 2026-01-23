package ui.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.AuthService;

public class LoginView {

    public void start(Stage stage) {

        Label title = new Label("Connexion");
        title.getStyleClass().add("title");

        TextField txtNom = new TextField();
        txtNom.setPromptText("Nom");

        TextField txtLogin = new TextField();
        txtLogin.setPromptText("Login");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Mot de passe");

        Button btnLogin = new Button("Se connecter");

        btnLogin.setOnAction(e -> {

            String nom = txtNom.getText().trim();
            String login = txtLogin.getText().trim();
            String password = txtPassword.getText().trim();

            if (nom.isEmpty() || password.isEmpty()) {
                show("Nom et mot de passe obligatoires");
                return;
            }

            AuthService auth = new AuthService();
            boolean ok = auth.login(nom, login, password);

            if (!ok) {
                show("Informations incorrectes");
                return;
            }

            new MenuView().start(stage);
        });


        VBox root = new VBox(
                15,
                title,
                txtNom,      
                txtLogin,
                txtPassword,
                btnLogin
        );

        root.getStyleClass().add("container");

        Scene scene = new Scene(root, 360, 320);
        scene.getStylesheets().add("/css/style.css");

        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();}
    

    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);}
    
    
}
