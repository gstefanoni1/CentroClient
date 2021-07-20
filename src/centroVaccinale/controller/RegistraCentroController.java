package centroVaccinale.controller;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RegistraCentroController {
    @FXML
    private Button annulla;
    @FXML
    private Button crea;
    @FXML
    private TextField nCentro;
    @FXML
    private TextField indirizzo;
    @FXML
    private TextField nCivico;
    @FXML
    private TextField comune;
    @FXML
    private TextField cap;
    @FXML
    private TextField provincia;
    @FXML
    private ChoiceBox<String> qualificatore;
    @FXML
    private ChoiceBox<String> tipologia;


    public void annullaIserimento(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void inserisciCentro(MouseEvent mouseEvent) {
        //verifica compilazione campi
        if(!verificaCampi()) return;
        //Inserimento nel db
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private boolean verificaCampi() {
        if(nCentro.getText().equals("") || indirizzo.getText().equals("") || nCivico.getText().equals("")
        || comune.getText().equals("") || provincia.getText().equals("") || qualificatore.getValue().equals("")
        || tipologia.getValue().equals("")){
            return false;
        }

        return true;
    }
}
