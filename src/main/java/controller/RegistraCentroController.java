package controller;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
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
        boolean verified = true;
        if (nCentro.getText().equals(""))
            verified = setColorBorder(nCentro, "red");
        else
            setColorBorder(nCentro, "transparent");

        if (indirizzo.getText().equals(""))
            verified = setColorBorder(indirizzo, "red");
        else
            setColorBorder(indirizzo, "transparent");

        if (nCivico.getText().equals(""))
            verified = setColorBorder(nCivico, "red");
        else
            setColorBorder(nCivico, "transparent");

        if (cap.getText().equals(""))
            verified = setColorBorder(cap, "red");
        else
            setColorBorder(cap, "transparent");

        if (comune.getText().equals(""))
            verified = setColorBorder(comune, "red");
        else
            setColorBorder(comune, "transparent");

        if (provincia.getText().equals(""))
            verified = setColorBorder(provincia, "red");
        else
            setColorBorder(provincia, "transparent");

        if (qualificatore.getValue().equals(""))
            verified = setColorBorder(qualificatore, "red");
        else
            setColorBorder(qualificatore, "transparent");

        if (tipologia.getValue().equals(""))
            verified = setColorBorder(tipologia, "red");
        else
            setColorBorder(tipologia, "transparent");

        return verified;
    }

    private boolean setColorBorder(Control component, String color){
        component.setStyle("-fx-border-color: " + color + ";");
        return false;
    }
}
