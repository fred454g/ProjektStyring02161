package dtu.example.ui;

import dtu.example.domain.Medarbejder;
import dtu.example.domain.OperationNotAllowedException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class SecondaryController {

    // --- Fane: Projekter ---
    @FXML private TextField nytProjektNavnInput;

    // --- Fane: Aktiviteter ---
    @FXML private TextField projektIdForAktivitetInput;
    @FXML private TextField aktivitetsNavnInput;
    @FXML private TextField budgetInput;
    @FXML private ComboBox<String> medarbejderInitialerVælger;

    // --- Fane: Tidsregistrering ---
    @FXML private TextField projektIdTidInput;
    @FXML private TextField aktivitetsNavnTidInput;
    @FXML private TextField timerInput;

    // --- Fane: Rapporter ---
    @FXML private TextField rapportProjektIdInput;
    @FXML private TextArea rapportOutput;

    // --- Fane: Fravær ---
    @FXML private TextField fravaerTypeInput;
    @FXML private TextField startUgeInput;
    @FXML private TextField slutUgeInput;

    /**
     * initialize() kører automatisk, når FXML-filen er indlæst.
     * Det er her, vi fylder data i vores dropdowns.
     */
    @FXML
    public void initialize() {
        opdaterMedarbejderListe();
    }

    public void opdaterMedarbejderListe() {
        medarbejderInitialerVælger.getItems().clear();

        // Hent medarbejdere fra facaden
        List<Medarbejder> alle = App.getFacade().getMedarbejdere();

        for (Medarbejder m : alle) {
            medarbejderInitialerVælger.getItems().add(m.getInitialer());
        }

        medarbejderInitialerVælger.setPromptText("Vælg medarbejder");
    }

    // ==========================================
    // LOGIK FOR PROJEKTER
    // ==========================================
    @FXML
    private void handleOpretProjekt() {
        try {
            String navn = nytProjektNavnInput.getText();
            App.getFacade().opretProjekt(navn);
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

            App.getFacade().opretAktivitet(pId, navn, budget, 10, 12);
            visInfo("Succes", "Aktivitet tilføjet til projekt " + pId);
        } catch (Exception e) {
            visFejl("Fejl ved oprettelse", e.getMessage());
        }
    }

    @FXML
    private void handleTilknytMedarbejder() {
        try {
            String pId = projektIdForAktivitetInput.getText();
            String aNavn = aktivitetsNavnInput.getText();

            // Læs værdien fra ComboBox i stedet for TextField
            String initialer = medarbejderInitialerVælger.getValue();

            if (initialer == null || initialer.isEmpty()) {
                visFejl("Input mangler", "Vælg venligst en medarbejder i listen.");
                return;
            }

            App.getFacade().tilknytMedarbejderTilProjekt(pId, initialer);
            App.getFacade().tilknytMedarbejderTilAktivitet(pId, aNavn, initialer);

            visInfo("Medarbejder tilknyttet", initialer + " er nu på opgaven.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke tilknytte", e.getMessage());
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
    // LOGIK FOR FRAVÆR
    // ==========================================
    @FXML
    private void handleRegistrerFravaer() {
        try {
            String type = fravaerTypeInput.getText();
            int start = Integer.parseInt(startUgeInput.getText());
            int slut = Integer.parseInt(slutUgeInput.getText());

            App.getFacade().registrerFravaer(type, start, slut);
            visInfo("Fravær gemt", type + " registreret i uge " + start + "-" + slut);

        } catch (NumberFormatException e) {
            visFejl("Input fejl", "Uger skal være tal (f.eks. 12)");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke gemme fravær", e.getMessage());
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