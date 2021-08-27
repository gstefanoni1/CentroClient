import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUIStarter extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/mainLayout.fxml"));
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("img/icon.png"))));
        primaryStage.setTitle("Centro Vaccinale");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();
    }

    public GUIStarter(){}

    public static void exec(String[] args) {
        launch(args);
    }
}