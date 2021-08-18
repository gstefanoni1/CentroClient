package controller;
import client.ClientHandler;
import client.PacketReceivedListener;
import datatypes.CentroVaccinale;
import datatypes.protocolmessages.Packet;
import datatypes.protocolmessages.RegistrationCVResponse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistraCentroController implements Initializable, PacketReceivedListener {
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

    private ClientHandler client;


    public void annullaIserimento(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void inserisciCentro(MouseEvent mouseEvent) {
        //verifica compilazione campi
        if(!verificaCampi()) return;
        //Inserimento nel db
        CentroVaccinale cv = new CentroVaccinale();
        cv.setComune(comune.getText());
        cv.setTipologia(tipologia.getValue());
        cv.setNome(nCentro.getText());
        //cv.setId();
        cv.setCap(cap.getText());
        cv.setNomeIndirizzo(indirizzo.getText());
        cv.setNumero(nCivico.getText());
        cv.setQualificatore(qualificatore.getValue());
        cv.setSiglaProvincia(provincia.getText());

        client.insertCV(cv);
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

    @Override
    public void onPacketReceived(Packet packet) {
        if(packet instanceof RegistrationCVResponse){
            RegistrationCVResponse res = (RegistrationCVResponse) packet;
            System.out.println(res.getPacketName() + " " + res.isEsito());
            Stage stage = (Stage) nCentro.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = ClientHandler.getInstance();
        this.client.addListener(RegistrationCVResponse.class.toString(), this);
    }
}
