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

    private static CentroVaccinale selezione;
    private ListProperty<CentroVaccinale> listaCompleta;
    private ListProperty<CentroVaccinale> listaFiltrata;

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

    public void annulla(MouseEvent mouseEvent) {
        Stage thisStage = (Stage) filtro.getScene().getWindow();
        thisStage.close();
    }

    public void seleziona(MouseEvent mouseEvent) {
        selezione = lista.getSelectionModel().getSelectedItem();
        annulla(mouseEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaCompleta = RegistraCittadinoController.getNomiCV();
        lista.getItems().addAll(listaCompleta); //FORSE VA CAMBIATO
        listaFiltrata = new SimpleListProperty<>();
    }

    public static CentroVaccinale getSelezione() {
        return selezione;
    }
}
