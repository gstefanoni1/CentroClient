package controller;

import client.ClientHandler;
import client.PacketReceivedListener;
import datatypes.CentroVaccinale;
import datatypes.Vaccinato;
import datatypes.Vaccinazione;
import datatypes.Vaccino;
import datatypes.protocolmessages.GetCVResponse;
import datatypes.protocolmessages.Packet;
import datatypes.protocolmessages.RegistrationCVResponse;
import datatypes.protocolmessages.RegistrationVaccinatedResponse;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistraCittadinoController implements Initializable, PacketReceivedListener {

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
    private ClientHandler client;
    private String id;

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
        Vaccinato vaccinato = new Vaccinato();
        vaccinato.setNome(nome.getText());
        vaccinato.setCognome(cognome.getText());
        vaccinato.setCodiceFiscale(codFiscale.getText());

        Vaccinazione vaccinazione = new Vaccinazione();
        vaccinazione.setVaccinato(vaccinato);
        vaccinazione.setCentroVaccinale(centroSel);

        //default time zone
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //local date + atStartOfDay() + default time zone + toInstant() = Date
        Date date = Date.from(dataSomm.getValue().atStartOfDay(defaultZoneId).toInstant());
        vaccinazione.setDataVaccinazione(date);
        vaccinazione.setVaccino(new Vaccino(vaccino.getValue()));

        client.insertVaccination(vaccinazione);


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
        client = ClientHandler.getInstance();
        this.client.addListener(GetCVResponse.class.toString(), this);
        this.client.addListener(RegistrationVaccinatedResponse.class.toString(), this);
        //Recuperare tutti cv

    }

    private void setNomiList(){
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

    @Override
    public void onPacketReceived(Packet packet) {
        if(packet instanceof RegistrationVaccinatedResponse){
            RegistrationVaccinatedResponse res = (RegistrationVaccinatedResponse) packet;
            System.out.println(res.getPacketName() + " " + res.isEsito());
            if(res.isEsito()) {
                //Alert con id da dare al cittadino
                id = res.getChiave();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informazioni cittadino: " + codFiscale.getText());
                alert.setHeaderText(null);
                alert.setContentText("ID vaccinazione: " + id);
                alert.showAndWait();

                Stage stage = (Stage) nome.getScene().getWindow();
                stage.close();
            }else{
                //Alert errore
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText(null);
                alert.setContentText("Errore nell'invio delle informazioni, riprovare");
                alert.showAndWait();
            }
        }

        if(packet instanceof GetCVResponse){
            GetCVResponse res = (GetCVResponse) packet;
            List<CentroVaccinale> list = res.getCvList();
            centriVaccinali.addAll(list);
            setNomiList();
        }
    }
}
