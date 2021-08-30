package controller;

import datatypes.CentroVaccinale;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class VisualizzaListaCentriController implements Initializable {
    /**
     * Variqabili FXML
     */
    //region Variabili FXML
    @FXML
    private TextField filtro;
    @FXML
    private ListView<CentroVaccinale> lista;
    //endregion

    /**
     * selezione è il CentroVaccinale selezionato da GUI
     */
    private static CentroVaccinale selezione;
    /**
     * listaCompleta è la lista di tutti i CentriVaccinali presenti a DB
     */
    private ListProperty<CentroVaccinale> listaCompleta;
    /**
     * listaFiltrata è la lista dei CentriVaccinali filtrata per il nome
     */
    private ListProperty<CentroVaccinale> listaFiltrata;

    /**
     * Metodo invocato sull click del bottone ricerca. Filtra la listaCompleta per il nome
     * @param mouseEvent
     */
    public void ricerca(MouseEvent mouseEvent) {

        ObservableList<CentroVaccinale> simple = FXCollections.observableArrayList();
        for(CentroVaccinale cv : listaCompleta){
            if(cv.getNome().toUpperCase(Locale.ROOT).contains(filtro.getText().toUpperCase(Locale.ROOT)) ||
            cv.getComune().toUpperCase(Locale.ROOT).contains(filtro.getText().toUpperCase(Locale.ROOT)))
                simple.add(cv);
        }
        listaFiltrata = new SimpleListProperty<>(simple);
        lista.setItems(listaFiltrata);
    }

    /**
     * Metodo invocato dal bottone annulla al click, per chiudere la schermata
     * @param mouseEvent
     */
    public void annulla(MouseEvent mouseEvent) {
        Stage thisStage = (Stage) filtro.getScene().getWindow();
        thisStage.close();
    }
    /**
     * Metodo invocato dal bottone seleziona al click, per salvare il CentroVaccinale selezionato
     * @param mouseEvent
     */
    public void seleziona(MouseEvent mouseEvent) {
        selezione = lista.getSelectionModel().getSelectedItem();
        annulla(mouseEvent);
    }

    /**
     * Metodo invocato durante l'inizializzazione della finestra, Agginge tutti i CentriVaccinali alla lista
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaCompleta = RegistraCittadinoController.getNomiCV();
        listaFiltrata = new SimpleListProperty<>();
    }

    /**
     * Metodo statico invocato per ricevere il CentroVaccinale selezionato
     * @return CentroVaccinale selezionato
     */
    public static CentroVaccinale getSelezione() {
        return selezione;
    }
}
