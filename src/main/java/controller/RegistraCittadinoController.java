package controller;

import client.ClientHandler;
import client.PacketReceivedListener;
import datatypes.CentroVaccinale;
import datatypes.Vaccinato;
import datatypes.Vaccinazione;
import datatypes.Vaccino;
import datatypes.protocolmessages.*;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe per controllare la registrazione del cittadino appena vaccinato
 * @author Stefanoni Gianluca
 * @version 1.0
 */
public class RegistraCittadinoController implements Initializable, PacketReceivedListener {

    /**
     * Variabili per i componenti dell'interfaccia grafica
     */
    //region Variabili FXML
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
    @FXML
    private TextField email;
    //endregion
    /**
     * date utilizzata per la formattazione in tipo Date e inserimento in vaccinazione
     */
    private String date;
    /**
     * Regex per verificare il codice fiscale e email
     */
    private static final String COD_FISCALE_REGEX = "^[a-zA-Z]{6}[0-9]{2}[abcdehlmprstABCDEHLMPRST]{1}[0-9]{2}([a-zA-Z]{1}[0-9]{3})[a-zA-Z]{1}$";
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private boolean verificaEmailDB = false;
    /**
     * pattern e matcher sono utilizzate pre verificare le regex
     */
    private static Pattern pattern;
    private Matcher matcher;
    /**
     * centriVaccinali contiene la lista di tutti i centri vaccinali
     */
    private ArrayList<CentroVaccinale> centriVaccinali;
    /**
     * nomiCV per inswerire i nomi di tutti i centri all'interno di ChoiceBox<String> centro
     */
    private ListProperty<String> nomiCV;
    /**
     * centroSel per salvare temporaneamente il centro selezionato in ChoiceBox<String> centro
     */
    private CentroVaccinale centroSel;
    /**
     * lista per salvare i vaccini disponibili
     */
    private ArrayList<Vaccino> vaccini;
    /**
     * vaccino selezionato
     */
    private Vaccino vaccinoSel;

    /**
     * client è l'istanza del client connesso al server
     */
    private ClientHandler client;
    /**
     * id per salvare temporaneament e mostrare all'utente l'id della vaccinazione
     */
    private String id;

    /**
     * Metodo invocato dal bottone @annulla al click, per chiudere la schermata
     * @param mouseEvent
     */
    public void annullaIserimento(MouseEvent mouseEvent) {
        chiudi();
    }

    /**
     * Metodo invocato dal bottone @crea al click, per inserire il cittadino appena vaccinato a DB
     * @param mouseEvent
     */
    public void verificaEmail(MouseEvent mouseEvent) {
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(email.getText());
        if(matcher.matches())
            client.requestEmailCheck(email.getText());
        else{
            Platform.runLater(() -> {
                Alert alertEmail = new Alert(Alert.AlertType.ERROR);
                alertEmail.setTitle("");
                alertEmail.setHeaderText("Errore nella compilazione dei campi");
                alertEmail.setContentText("Email non valida");

                alertEmail.showAndWait();
            });
        }
    }

    private void inserisciCittadino(){
        if (!verificaCampi()) return;
        Vaccinato vaccinato = new Vaccinato();
        vaccinato.setNome(nome.getText());
        vaccinato.setCognome(cognome.getText());
        vaccinato.setCodiceFiscale(codFiscale.getText());
        vaccinato.setEmail(email.getText());

        Vaccinazione vaccinazione = new Vaccinazione();
        vaccinazione.setVaccinato(vaccinato);
        vaccinazione.setCentroVaccinale(centroSel);

        java.sql.Date gettedDatePickerDate = java.sql.Date.valueOf(dataSomm.getValue());
        vaccinazione.setDataVaccinazione(gettedDatePickerDate);
        setVaccinoSel(vaccino.getValue());
        vaccinazione.setVaccino(vaccinoSel);

        client.insertVaccination(vaccinazione);
    }

    /**
     * Metodo invocato da inserisciCittadino(MouseEvent mouseEvent), per verificare la corretta compilazione dei campi
     * @return true se tutti  i campi sono stati compilati correttamente
     */
    private boolean verificaCampi() {
        boolean verified = true;
        codFiscale.setText(codFiscale.getText().replace(" ", ""));
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
            if (verificaCodFiscale(codFiscale.getText()))
                setColorBorder(codFiscale, "transparent");
            else {
                verified = setColorBorder(codFiscale, "red");
                Alert alertCod = new Alert(Alert.AlertType.ERROR);
                alertCod.setTitle("");
                alertCod.setHeaderText("Errore nella compilazione dei campi");
                alertCod.setContentText("Codice fiscale non valido");

                alertCod.showAndWait();
            }
        }

        if (dataSomm.getValue() == null)
            verified = setColorBorder(dataSomm, "red");
        else {
            setColorBorder(dataSomm, "transparent");
            date = dataSomm.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        if (centro.getValue() == null || centro.getValue().equals(""))
            verified = setColorBorder(centro, "red");
        else
            setColorBorder(centro, "transparent");

        if (vaccino.getValue().equals(""))
            verified = setColorBorder(vaccino, "red");
        else
            setColorBorder(vaccino, "transparent");


        return verified;
    }

    /**
     * Metodo per verificare con regex se il codice fiscale è valido
     * @param codFiscale stringa da verificare
     * @return true se il codice è valido
     */
    private boolean verificaCodFiscale(String codFiscale) {
        pattern = Pattern.compile(COD_FISCALE_REGEX, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(codFiscale);
        return matcher.matches();
    }

    /**
     * Metodo per settare il colore del bordo di un componentre grafico
     * @param component componente da modificare
     * @param color colore da settare
     * @return sempre false
     */
    private boolean setColorBorder(Control component, String color) {
        component.setStyle("-fx-border-color: " + color + ";");
        return false;
    }

    /**
     * Metodo invocato durante l'inizializzazione della finestra, per settare il client e richidere al server
     * l'elenco di tutti i centri vaccinali
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        centriVaccinali = new ArrayList<>();
        vaccini = new ArrayList<>();
        nomiCV = new SimpleListProperty<>(FXCollections.observableArrayList());
        client = ClientHandler.getInstance();
        this.client.addListener(GetCVResponse.class.toString(), this);
        this.client.addListener(RegistrationVaccinatedResponse.class.toString(), this);
        this.client.addListener(CheckEmailResponse.class.toString(), this);
        this.client.addListener(GetVaccinesResponse.class.toString(), this);
        client.getAllCV();
        client.getVaccines();
        dataSomm.getStyleClass().removeIf(style -> style.equals("text-field"));
    }

    /**
     * Metodo per caricare i nomi dei Centri allinterno di ChoiceBox<String> centro,
     * inoltre aggiunge un listener per verificare il cambiamento di item selezionato
     */
    private void setNomiList() {
        for (CentroVaccinale c : centriVaccinali) {
            nomiCV.add(c.getNome());
        }
        centro.setItems(nomiCV);
        centro.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)
                        -> setCentroSel(newValue));
    }

    /**
     * Metodo per settare il centro selezionato in ChoiceBox<String> centro
     * @param newValue
     */
    private void setCentroSel(String newValue) {
        for (CentroVaccinale cv : centriVaccinali) {
            if (cv.getNome().equals(newValue))
                centroSel = cv;
        }
    }

    private void setVaccinoSel(String vaccino){
        for(Vaccino v: vaccini){
            if(v.getNome().equals(vaccino))
                vaccinoSel = v;
        }
    }

    /**
     * Metodo per gestire la ricezione dei pacchetti: RegistrationVaccinatedResponse, GetCVResponse
     * @param packet pacchetto ricevuto
     */
    @Override
    public void onPacketReceived(Packet packet) {
        if (packet instanceof RegistrationVaccinatedResponse) {
            RegistrationVaccinatedResponse res = (RegistrationVaccinatedResponse) packet;
            System.out.println(res.getPacketName() + " " + res.isEsito());
            if (res.isEsito()) {
                id = res.getChiave();
                Platform.runLater(() -> {
                    String txt = "Vaccinazione registrata con successo.\n" + "ID vaccinazione: " + id;
                    TextArea textArea = new TextArea(txt);
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
                    GridPane gridPane = new GridPane();
                    gridPane.setMaxWidth(Double.MAX_VALUE);
                    gridPane.add(textArea, 0, 0);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Informazioni cittadino: " + codFiscale.getText());
                    alert.setHeaderText(null);
                    alert.getDialogPane().setContent(gridPane);
                    alert.showAndWait();

                    chiudi();
                });
            } else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setHeaderText(null);
                    alert.setContentText("Errore nell'invio delle informazioni, riprovare");
                    alert.showAndWait();
                });
            }
        }

        if (packet instanceof GetCVResponse) {
            GetCVResponse res = (GetCVResponse) packet;
            if(res.isEsito()) {
                List<CentroVaccinale> list = res.getCvList();
                centriVaccinali.addAll(list);
                setNomiList();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setContentText("Impossibile recuperare informazioni dal server");
                alert.showAndWait();
                chiudi();
            }
        }

        if (packet instanceof GetVaccinesResponse){
            GetVaccinesResponse res = (GetVaccinesResponse) packet;
            vaccino.getItems().removeAll();
            if(res.isEsito()){
                vaccini.addAll(res.getVaccines());
                for (Vaccino v : res.getVaccines()){
                    vaccino.getItems().add(v.getNome());
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setContentText("Impossibile recuperare informazioni dal server");
                alert.showAndWait();
                chiudi();
            }
        }

        if (packet instanceof CheckEmailResponse){
            CheckEmailResponse res = (CheckEmailResponse) packet;
            if(res.isEsito()) {
                verificaEmailDB = true;
                inserisciCittadino();
            }
        }
    }

    /**
     * Metodo utilizzato per chiudere la finsestra
     */
    private void chiudi(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../view/mainLayout.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 300);
            Stage stage = new Stage();
            stage.setTitle("Centro Vaccinale");
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("../img/icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            stage.show();

            Stage thisStage = (Stage) nome.getScene().getWindow();
            thisStage.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
