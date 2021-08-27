package controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class VisualizzaListaCentriController {
    /**
     * Variqabili FXML
     */
    //region Variabili FXML
    @FXML
    private TextField filtro;
    @FXML
    private ListView lista;
    //endregion

    private ListProperty<String> listaCompleta;
    private ListProperty<String> listaFiltrata;

    public VisualizzaListaCentriController(ListProperty<String> nomiCV){
        listaCompleta = nomiCV;
        lista.getItems().addAll(nomiCV);
        listaFiltrata = new SimpleListProperty<>();
    }

    public void ricerca(MouseEvent mouseEvent) {
        listaFiltrata.removeAll();
        for(String s : listaCompleta){
            if(s.contains(filtro.getText())){
                listaFiltrata.add(s);
            }
        }
    }

    public void annulla(MouseEvent mouseEvent) {
        Stage thisStage = (Stage) filtro.getScene().getWindow();
        thisStage.close();
    }

    public void seleziona(MouseEvent mouseEvent) {
        RegistraCittadinoController.setCentroTxt(lista.getSelectionModel().getSelectedItem().toString());
        annulla(mouseEvent);
    }
}
