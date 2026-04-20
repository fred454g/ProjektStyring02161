package dtu.example.ui;

import dtu.example.domain.OperationNotAllowedException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PrimaryController {


    @FXML
    private TextField initialerInput;

    @FXML
    private void handleLogin() {
        String indtastedeInitialer = initialerInput.getText();

        try {
            // 1. Forsøg at logge ind via den globale facade
            App.getFacade().userLogin(indtastedeInitialer);

            // 2. Hvis koden når hertil, var der ingen fejl. Skift til dashboard
            App.setRoot("secondary");

        } catch (OperationNotAllowedException e) {
            // 3. Hvis domænet kaster en fejl (fx ukendt bruger), vis en pop-up
            visFejl("Login mislykkedes", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // En praktisk hjælper-metode til at vise røde fejl-bokse på skærmen
    private void visFejl(String titel, String besked) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(besked);
        alert.showAndWait();
    }
}