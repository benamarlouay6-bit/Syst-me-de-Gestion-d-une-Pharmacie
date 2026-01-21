package ui.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.utils.Session;

public class LoginView extends Application {

    @Override
    public void start(Stage stage) {

        
        Label title = new Label("Connexion - Pharmacie");
        title.getStyleClass().add("title");

        
        TextField loginField = new TextField();
        loginField.setPromptText("Login");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        
        Button btnLogin = new Button("Se connecter");

        
        btnLogin.setOnAction(e -> {

            
            if (passwordField.getText().equals("eyaeya2")) {

                
                if (loginField.getText().equalsIgnoreCase("admin")) {
                    Session.setRole("ADMIN");
                } else {
                    Session.setRole("EMPLOYE");
                }

                
                new MenuView().start(stage);

            } else {
                new Alert(
                        Alert.AlertType.ERROR,
                        "Mot de passe incorrect"
                ).show();
            }
        });

        
        VBox root = new VBox(15);
        root.getChildren().addAll(
                title,
                loginField,
                passwordField,
                btnLogin
        );
        root.getStyleClass().add("container");

        
        Scene scene = new Scene(root, 350, 250);
        scene.getStylesheets().add("/css/style.css");

        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
