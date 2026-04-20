package dtu.example.ui;

import dtu.example.domain.OperationNotAllowedException;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SecondaryController {

    // --- Fane: Projekter ---
    @FXML private TextField nytProjektNavnInput;

    // --- Fane: Aktiviteter ---
    @FXML private TextField projektIdForAktivitetInput;
    @FXML private TextField aktivitetsNavnInput;
    @FXML private TextField budgetInput;

    // --- Fane: Tidsregistrering ---
    @FXML private TextField projektIdTidInput;
    @FXML private TextField aktivitetsNavnTidInput;
    @FXML private TextField timerInput;

    // --- Fane: Rapporter ---
    @FXML private TextField rapportProjektIdInput;
    @FXML private TextArea rapportOutput;

    // ==========================================
    // LOGIK FOR PROJEKTER
    // ==========================================
    @FXML
    private void handleOpretProjekt() {
        try {
            String navn = nytProjektNavnInput.getText();
            App.getFacade().opretProjekt(navn); // Backend opretter og gemmer til fil
            nytProjektNavnInput.clear();
            visInfo("Succes", "Projektet er oprettet og gemt.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke oprette projekt", e.getMessage());
        }
    }

    // ==========================================
    // LOGIK FOR AKTIVITETER
    // ==========================================
    @FXML
    private void handleOpretAktivitet() {
        try {
            String pId = projektIdForAktivitetInput.getText();
            String navn = aktivitetsNavnInput.getText();
            double budget = Double.parseDouble(budgetInput.getText());

            // Vi bruger dummy uger (10 til 12) for nu, som i dine tests
            App.getFacade().opretAktivitet(pId, navn, budget, 10, 12);
            visInfo("Succes", "Aktivitet tilføjet til projekt " + pId);
        } catch (Exception e) {
            visFejl("Fejl ved oprettelse", e.getMessage());
        }
    }

    // ==========================================
    // LOGIK FOR TID
    // ==========================================
    @FXML
    private void handleRegistrerTid() {
        try {
            String pId = projektIdTidInput.getText();
            String aNavn = aktivitetsNavnTidInput.getText();
            double timer = Double.parseDouble(timerInput.getText());

            App.getFacade().registrerTid(pId, aNavn, timer);
            visInfo("Tid registreret", timer + " timer er tilføjet.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke registrere tid", e.getMessage());
        }
    }

    // ==========================================
    // LOGIK FOR RAPPORTER
    // ==========================================
    @FXML
    private void handleGenererRapport() {
        try {
            String pId = rapportProjektIdInput.getText();
            String rapport = App.getFacade().genererRapport(pId);
            rapportOutput.setText(rapport);
        } catch (OperationNotAllowedException e) {
            visFejl("Rapport-fejl", e.getMessage());
        }
    }

    @FXML
    private void handleLogout() throws Exception {
        App.getFacade().userLogout();
        App.setRoot("primary");
    }

    // --- Hjælpemetoder til UI ---
    private void visFejl(String titel, String besked) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titel);
        a.setContentText(besked);
        a.showAndWait();
    }

    private void visInfo(String titel, String besked) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titel);
        a.setContentText(besked);
        a.showAndWait();
    }
}