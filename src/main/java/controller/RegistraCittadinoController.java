package controller;

import datatypes.CentroVaccinale;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistraCittadinoController implements Initializable {

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
    private static final String COD_FISCALE_REGEX = "^[a-zA-Z]{6}[0-9]{2}[abcdehlmprstABCDEHLMPRST]{1}[0-9]{2}([a-zA-Z]{1}[0-9]{3})[a-zA-Z]{1}$";
    private static Pattern pattern;
    private Matcher matcher;
    private ArrayList<CentroVaccinale> centriVaccinali;
    private ObservableList<String> nomiCV;
    private CentroVaccinale centroSel;

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
        boolean verified = true;
        if (nome.getText().equals(""))
            verified = setColorBorder(nome, "red");
        else
            setColorBorder(nome, "transparent");

        if (cognome.getText().equals(""))
            verified = setColorBorder(cognome, "red");
        else
            setColorBorder(cognome, "transparent");

        if (codFiscale.getText().equals(""))
            verified = setColorBorder(codFiscale, "red");
        else {
            if(verificaCodFiscale(codFiscale.getText()))
                setColorBorder(codFiscale, "transparent");
            else{
                verified = setColorBorder(codFiscale, "red");
                Alert alertCod = new Alert(Alert.AlertType.ERROR);
                alertCod.setTitle("");
                alertCod.setHeaderText("Errore nella compilazione dei campi");
                alertCod.setContentText("Codice fiscale non valido");

                alertCod.showAndWait();
            }
        }

        if (dataSomm.getValue().equals(""))
            verified = setColorBorder(dataSomm, "red");
        else {
            setColorBorder(dataSomm, "transparent");
            date = dataSomm.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        if (centro.getValue().equals(""))
            verified = setColorBorder(centro, "red");
        else
            setColorBorder(centro, "transparent");

        if (vaccino.getValue().equals(""))
            verified = setColorBorder(vaccino, "red");
        else
            setColorBorder(vaccino, "transparent");


        return verified;
    }

    private boolean verificaCodFiscale(String codFiscale) {
        pattern = Pattern.compile(COD_FISCALE_REGEX, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(codFiscale);
        return matcher.matches();
    }

    private boolean setColorBorder(Control component, String color){
        component.setStyle("-fx-border-color: " + color + ";");
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Recuperare tutti cv
        for (CentroVaccinale c: centriVaccinali){
            nomiCV.add(c.getNome());
        }
        centro.setItems(nomiCV);
        centro.getSelectionModel()
                .selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue)
                        -> setCentroSel(newValue) );
    }

    private void setCentroSel(String newValue) {
        for(CentroVaccinale cv: centriVaccinali){
            if(cv.getNome().equals(newValue))
                centroSel = cv;
        }
    }
}
