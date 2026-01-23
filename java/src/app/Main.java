
import javafx.application.Application;
import javafx.stage.Stage;
import ui.view.LoginView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        System.out.println(" Application démarrée");
        new LoginView().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
