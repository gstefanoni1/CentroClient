package controller;
import client.ClientHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Classe per controllare gli eventi nel menu principale
 * @author Stefanoni Gianluca
 * @version 1.0
 */
public class MainController implements Initializable {
    /**
     * Variabili per i componenti dell'interfaccia grafica
     */
    //region Variabili FXML
    @FXML
    private Button buttCentro;
    @FXML
    private Button buttVaccinato;
    @FXML
    private AnchorPane serverError;
    //endregion
    /**
     * client è l'istanza del client connesso al server
     */
    private static ClientHandler client;

    /**
     * Metodo per aprire la schermata di registrazione centro vaccinale
     * @param mouseEvent
     */
    public void registraCentro(MouseEvent mouseEvent) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/registraCentroLayout.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 400);
            Stage stage = new Stage();
            stage.setTitle("Registra Centro Vaccinale");
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/icon.png"))));
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();
            Node source = (Node) mouseEvent.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Metodo per aprire la schermata di registrazione cittadino appena vaccinato
     * @param mouseEvent
     */
    public void registraCittadino(MouseEvent mouseEvent) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/registraCittadinoLayout.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 400);
            Stage stage = new Stage();
            stage.setTitle("Registra Cittadino");
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/icon.png"))));
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();

            Node source = (Node) mouseEvent.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per iniziallizzare il collegamento tra client e server
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = ClientHandler.getInstance();
        if(!client.isConnected()) {
            //SE IL CLIENT NON E' CONNESSO
            try {
                if(!client.connect()){
                    buttCentro.setDisable(true);
                    buttCentro.setOpacity(0.5);
                    buttVaccinato.setDisable(true);
                    buttVaccinato.setOpacity(0.5);
                    serverError.setVisible(true);
                }else{
                    buttCentro.setDisable(false);
                    buttCentro.setOpacity(1);
                    buttVaccinato.setDisable(false);
                    buttVaccinato.setOpacity(1);
                    serverError.setVisible(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            //IL CLIENT E' CONNESSO
            serverError.setVisible(false);
        }
    }

    /**
     * Metodo per ritentare la connesione dopo che è fallita
     * @param mouseEvent
     */
    public void retryConnect(MouseEvent mouseEvent) {
        try {
            if(!client.connect()){
                buttCentro.setDisable(true);
                buttCentro.setOpacity(0.5);
                buttVaccinato.setDisable(true);
                buttVaccinato.setOpacity(0.5);
                serverError.setVisible(true);
            }else{
                buttCentro.setDisable(false);
                buttCentro.setOpacity(1);
                buttVaccinato.setDisable(false);
                buttVaccinato.setOpacity(1);
                serverError.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
