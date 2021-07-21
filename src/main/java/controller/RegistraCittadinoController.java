package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class RegistraCittadinoController {

    @FXML
    private Button annulla;
    @FXML
    private Button crea;
    @FXML
    private TextField nome;
    @FXML
    private TextField cognome;
    @FXML
    private TextField codFiscale;
    @FXML
    private DatePicker dataSomm;
    @FXML
    private ChoiceBox<String> centro;
    @FXML
    private ChoiceBox<String> vaccino;

    private String date;

    public void annullaIserimento(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void inserisciCittadino(MouseEvent mouseEvent) {
        //verifica compilazione campi
        if(!verificaCampi()) return;
        //Inserimento nel db
        //Se andato a buon fine deve restituire l'id della vaccinazione
        int id = 12345;

        //Alert con id da dare al cittadino
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazioni cittadino: " + codFiscale.getText());
        alert.setHeaderText(null);
        alert.setContentText("ID vaccinazione: " + id);
        alert.showAndWait();

        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private boolean verificaCampi() {
        if(nome.getText().equals("") || cognome.getText().equals("") || codFiscale.getText().equals("")
                || dataSomm.getValue().equals("") || centro.getValue().equals("")
                || vaccino.getValue().equals("")){
            return false;
        }
        date = dataSomm.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return true;
    }
}
