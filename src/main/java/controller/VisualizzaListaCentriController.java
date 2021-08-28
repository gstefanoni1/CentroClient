package controller;

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
import java.util.ResourceBundle;

public class VisualizzaListaCentriController implements Initializable {
    /**
     * Variqabili FXML
     */
    //region Variabili FXML
    @FXML
    private TextField filtro;
    @FXML
    private ListView lista;
    //endregion

    private static String selezione;
    private ListProperty<String> listaCompleta;
    private ListProperty<String> listaFiltrata;

    public void ricerca(MouseEvent mouseEvent) {

        ObservableList<String> simple = FXCollections.observableArrayList();
        for(String s : listaCompleta){
            if(s.contains(filtro.getText())){
                simple.add(s);
            }
        }
        listaFiltrata = new SimpleListProperty<String>(simple);
        lista.setItems(listaFiltrata);
    }

    public void annulla(MouseEvent mouseEvent) {
        Stage thisStage = (Stage) filtro.getScene().getWindow();
        thisStage.close();
    }

    public void seleziona(MouseEvent mouseEvent) {
        selezione = lista.getSelectionModel().getSelectedItem().toString();
        annulla(mouseEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaCompleta = RegistraCittadinoController.getNomiCV();
        lista.getItems().addAll(listaCompleta);
        listaFiltrata = new SimpleListProperty<>();
    }

    public static String getSelezione() {
        return selezione;
    }
}
