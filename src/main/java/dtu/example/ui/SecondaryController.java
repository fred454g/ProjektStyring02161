package dtu.example.ui;

import dtu.example.domain.Medarbejder;
import dtu.example.domain.OperationNotAllowedException;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class SecondaryController implements PropertyChangeListener {

    // --- Projekter ---
    @FXML private TextField nytProjektNavnInput;

    // --- Projektleder ---
    @FXML private ComboBox<String> projektIdForLederInput;
    @FXML private ComboBox<String> projektlederInitialerVaelger;

    // --- Projektmedarbejdere ---
    @FXML private ComboBox<String> projektIdForProjektMedarbejderInput;
    @FXML private ComboBox<String> medarbejderTilProjektVaelger;
    @FXML private ComboBox<String> projektIdForFjernProjektMedarbejderInput;
    @FXML private ComboBox<String> medarbejderFraProjektVaelger;

    // --- Omdøb projekt ---
    @FXML private ComboBox<String> projektIdForOmdobInput;
    @FXML private TextField nytProjektNavnOmdobInput;

    // --- Opret aktivitet ---
    @FXML private ComboBox<String> projektIdForAktivitetInput;
    @FXML private TextField aktivitetsNavnInput;
    @FXML private TextField budgetInput;
    @FXML private TextField startUgeAktivitetInput;
    @FXML private TextField slutUgeAktivitetInput;

    // --- Opdater aktivitet ---
    @FXML private ComboBox<String> projektIdForOpdaterAktivitetInput;
    @FXML private ComboBox<String> aktivitetForOpdateringInput;
    @FXML private TextField nytBudgetInput;
    @FXML private TextField nyStartUgeInput;
    @FXML private TextField nySlutUgeInput;

    // --- Slet aktivitet ---
    @FXML private ComboBox<String> projektIdForSletAktivitetInput;
    @FXML private ComboBox<String> aktivitetForSletningInput;

    // --- Tilknyt medarbejder til aktivitet ---
    @FXML private ComboBox<String> projektIdForTilknytningInput;
    @FXML private ComboBox<String> aktivitetForTilknytningInput;
    @FXML private ComboBox<String> medarbejderInitialerVaelger;

    //--- ledighed ---
    @FXML private TextField ledigStartUgeInput;
    @FXML private TextField ledigSlutUgeInput;
    @FXML private TextArea ledigeOutput;

    // --- Fjern medarbejder fra aktivitet ---
    @FXML private ComboBox<String> projektIdForFjernMedarbejderInput;
    @FXML private ComboBox<String> aktivitetForFjernMedarbejderInput;
    @FXML private ComboBox<String> medarbejderForFjernelseVaelger;

    // --- Tidsregistrering ---
    @FXML private ComboBox<String> projektIdTidInput;
    @FXML private ComboBox<String> aktivitetsNavnTidInput;
    @FXML private TextField timerInput;
    @FXML private TextArea egneTimerOutput;

    // --- Fravær ---
    @FXML private TextField fravaerTypeInput;
    @FXML private TextField startUgeInput;
    @FXML private TextField slutUgeInput;

    // --- Rapport ---
    @FXML private ComboBox<String> rapportProjektIdInput;
    @FXML private TextArea rapportOutput;

    @FXML
    public void initialize() {
        App.getFacade().addPropertyChangeListener(this);

        opdaterMedarbejderLister();
        opdaterProjektLister();

        projektIdForTilknytningInput.setOnAction(event -> opdaterAktiviteterTilTilknytning());
        projektIdTidInput.setOnAction(event -> opdaterAktiviteterTilTidsregistrering());
        projektIdForOpdaterAktivitetInput.setOnAction(event -> opdaterAktiviteterTilOpdatering());
        projektIdForSletAktivitetInput.setOnAction(event -> opdaterAktiviteterTilSletning());
        projektIdForFjernMedarbejderInput.setOnAction(event -> opdaterAktiviteterTilFjernMedarbejder());
    }

    private void opdaterMedarbejderLister() {
        List<Medarbejder> alle = App.getFacade().getMedarbejdere();

        medarbejderInitialerVaelger.getItems().clear();
        projektlederInitialerVaelger.getItems().clear();
        medarbejderTilProjektVaelger.getItems().clear();
        medarbejderFraProjektVaelger.getItems().clear();
        medarbejderForFjernelseVaelger.getItems().clear();

        for (Medarbejder m : alle) {
            String initialer = m.getInitialer();

            medarbejderInitialerVaelger.getItems().add(initialer);
            projektlederInitialerVaelger.getItems().add(initialer);
            medarbejderTilProjektVaelger.getItems().add(initialer);
            medarbejderFraProjektVaelger.getItems().add(initialer);
            medarbejderForFjernelseVaelger.getItems().add(initialer);
        }
    }

    private void opdaterProjektLister() {
        List<String> ids = App.getFacade().getAlleProjektIds();

        projektIdForLederInput.getItems().setAll(ids);
        projektIdForAktivitetInput.getItems().setAll(ids);
        projektIdForTilknytningInput.getItems().setAll(ids);
        projektIdTidInput.getItems().setAll(ids);
        rapportProjektIdInput.getItems().setAll(ids);

        projektIdForProjektMedarbejderInput.getItems().setAll(ids);
        projektIdForFjernProjektMedarbejderInput.getItems().setAll(ids);
        projektIdForOmdobInput.getItems().setAll(ids);
        projektIdForOpdaterAktivitetInput.getItems().setAll(ids);
        projektIdForSletAktivitetInput.getItems().setAll(ids);
        projektIdForFjernMedarbejderInput.getItems().setAll(ids);
    }

    private void opdaterAktiviteterTilTilknytning() {
        opdaterAktivitetsDropdown(projektIdForTilknytningInput, aktivitetForTilknytningInput);
    }

    private void opdaterAktiviteterTilTidsregistrering() {
        opdaterAktivitetsDropdown(projektIdTidInput, aktivitetsNavnTidInput);
    }

    private void opdaterAktiviteterTilOpdatering() {
        opdaterAktivitetsDropdown(projektIdForOpdaterAktivitetInput, aktivitetForOpdateringInput);
    }

    private void opdaterAktiviteterTilSletning() {
        opdaterAktivitetsDropdown(projektIdForSletAktivitetInput, aktivitetForSletningInput);
    }

    private void opdaterAktiviteterTilFjernMedarbejder() {
        opdaterAktivitetsDropdown(projektIdForFjernMedarbejderInput, aktivitetForFjernMedarbejderInput);
    }

    private void opdaterAktivitetsDropdown(ComboBox<String> projektBox, ComboBox<String> aktivitetBox) {
        try {
            String projektId = projektBox.getValue();
            aktivitetBox.getItems().clear();

            if (projektId == null || projektId.isEmpty()) {
                return;
            }

            aktivitetBox.getItems().setAll(App.getFacade().getAktivitetsNavneForProjekt(projektId));
            aktivitetBox.setPromptText("Vælg aktivitet");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke hente aktiviteter", e.getMessage());
        }
    }

    @FXML
    private void handleOpretProjekt() {
        try {
            String navn = nytProjektNavnInput.getText();

            App.getFacade().opretProjekt(navn);

            nytProjektNavnInput.clear();

            visInfo("Projekt oprettet", "Projektet er oprettet og kan nu vælges i systemet.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke oprette projekt", e.getMessage());
        }
    }

    @FXML
    private void handleOmdobProjekt() {
        try {
            String projektId = projektIdForOmdobInput.getValue();
            String nytNavn = nytProjektNavnOmdobInput.getText();

            App.getFacade().omdoebProjekt(projektId, nytNavn);

            nytProjektNavnOmdobInput.clear();

            visInfo("Projekt omdøbt", "Projektet er omdøbt.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke omdøbe projekt", e.getMessage());
        }
    }

    @FXML
    private void handleTilknytProjektleder() {
        try {
            String projektId = projektIdForLederInput.getValue();
            String initialer = projektlederInitialerVaelger.getValue();

            App.getFacade().opdaterProjektMedProjektleder(projektId, initialer);

            visInfo("Projektleder tilknyttet",
                    initialer + " er nu projektleder for projekt " + projektId + ".");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke tilknytte projektleder", e.getMessage());
        }
    }

    @FXML
    private void handleTilknytMedarbejderTilProjekt() {
        try {
            String projektId = projektIdForProjektMedarbejderInput.getValue();
            String initialer = medarbejderTilProjektVaelger.getValue();

            App.getFacade().tilknytMedarbejderTilProjekt(projektId, initialer);

            visInfo("Medarbejder tilknyttet",
                    initialer + " er tilknyttet projekt " + projektId + ".");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke tilknytte medarbejder", e.getMessage());
        }
    }

    @FXML
    private void handleFjernMedarbejderFraProjekt() {
        try {
            String projektId = projektIdForFjernProjektMedarbejderInput.getValue();
            String initialer = medarbejderFraProjektVaelger.getValue();

            App.getFacade().fjernMedarbejderFraProjekt(projektId, initialer);

            visInfo("Medarbejder fjernet",
                    initialer + " er fjernet fra projekt " + projektId + ".");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke fjerne medarbejder", e.getMessage());
        }
    }

    @FXML
    private void handleOpretAktivitet() {
        try {
            String projektId = projektIdForAktivitetInput.getValue();
            String navn = aktivitetsNavnInput.getText();
            double budget = Double.parseDouble(budgetInput.getText());
            int startUge = Integer.parseInt(startUgeAktivitetInput.getText());
            int slutUge = Integer.parseInt(slutUgeAktivitetInput.getText());

            App.getFacade().opretAktivitet(projektId, navn, budget, startUge, slutUge);

            aktivitetsNavnInput.clear();
            budgetInput.clear();
            startUgeAktivitetInput.clear();
            slutUgeAktivitetInput.clear();

            visInfo("Aktivitet oprettet", "Aktiviteten er oprettet på projekt " + projektId + ".");
        } catch (NumberFormatException e) {
            visFejl("Inputfejl", "Budget, startuge og slutuge skal være tal.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke oprette aktivitet", e.getMessage());
        }
    }

    @FXML
    private void handleOpdaterAktivitet() {
        try {
            String projektId = projektIdForOpdaterAktivitetInput.getValue();
            String aktivitet = aktivitetForOpdateringInput.getValue();
            double budget = Double.parseDouble(nytBudgetInput.getText());
            int start = Integer.parseInt(nyStartUgeInput.getText());
            int slut = Integer.parseInt(nySlutUgeInput.getText());

            App.getFacade().opdaterAktivitet(projektId, aktivitet, budget, start, slut);

            nytBudgetInput.clear();
            nyStartUgeInput.clear();
            nySlutUgeInput.clear();

            visInfo("Aktivitet opdateret", "Aktiviteten er opdateret.");
        } catch (NumberFormatException e) {
            visFejl("Inputfejl", "Budget, startuge og slutuge skal være tal.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke opdatere aktivitet", e.getMessage());
        }
    }

    @FXML
    private void handleSletAktivitet() {
        try {
            String projektId = projektIdForSletAktivitetInput.getValue();
            String aktivitet = aktivitetForSletningInput.getValue();

            App.getFacade().sletAktivitet(projektId, aktivitet);

            visInfo("Aktivitet slettet", "Aktiviteten er slettet.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke slette aktivitet", e.getMessage());
        }
    }

    @FXML
    private void handleTilknytMedarbejder() {
        try {
            String projektId = projektIdForTilknytningInput.getValue();
            String aktivitetsNavn = aktivitetForTilknytningInput.getValue();
            String initialer = medarbejderInitialerVaelger.getValue();

            App.getFacade().tilknytMedarbejderTilAktivitet(projektId, aktivitetsNavn, initialer);

            visInfo("Medarbejder tilknyttet",
                    initialer + " er tilknyttet aktiviteten " + aktivitetsNavn + ".");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke tilknytte medarbejder", e.getMessage());
        }
    }

    @FXML
    private void handleFjernMedarbejderFraAktivitet() {
        try {
            String projektId = projektIdForFjernMedarbejderInput.getValue();
            String aktivitet = aktivitetForFjernMedarbejderInput.getValue();
            String initialer = medarbejderForFjernelseVaelger.getValue();

            App.getFacade().fjernMedarbejderFraAktivitet(projektId, aktivitet, initialer);

            visInfo("Medarbejder fjernet",
                    initialer + " er fjernet fra aktiviteten.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke fjerne medarbejder", e.getMessage());
        }
    }

    @FXML
    private void handleRegistrerTid() {
        try {
            String projektId = projektIdTidInput.getValue();
            String aktivitetsNavn = aktivitetsNavnTidInput.getValue();
            double timer = Double.parseDouble(timerInput.getText());

            App.getFacade().registrerTid(projektId, aktivitetsNavn, timer);

            timerInput.clear();

            visInfo("Tid registreret", timer + " timer er registreret på " + aktivitetsNavn + ".");
        } catch (NumberFormatException e) {
            visFejl("Inputfejl", "Timer skal være et tal.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke registrere tid", e.getMessage());
        }
    }

    @FXML
    private void handleVisEgneTimer() {
        try {
            double timer = App.getFacade().visEgneTimer();
            egneTimerOutput.setText("Du har registreret " + timer + " timer i alt.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke vise timer", e.getMessage());
        }
    }

    @FXML
    private void handleRegistrerFravaer() {
        try {
            String type = fravaerTypeInput.getText();
            int start = Integer.parseInt(startUgeInput.getText());
            int slut = Integer.parseInt(slutUgeInput.getText());

            App.getFacade().registrerFravaer(type, start, slut);

            fravaerTypeInput.clear();
            startUgeInput.clear();
            slutUgeInput.clear();

            visInfo("Fravær registreret", type + " er registreret i uge " + start + "-" + slut + ".");
        } catch (NumberFormatException e) {
            visFejl("Inputfejl", "Startuge og slutuge skal være tal.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke registrere fravær", e.getMessage());
        }
    }
    @FXML
    private void handleVisLedige() {
        try {
            int start = Integer.parseInt(ledigStartUgeInput.getText());
            int slut = Integer.parseInt(ledigSlutUgeInput.getText());

            String resultat = App.getFacade().visLedigeMedarbejdere(start, slut);

            ledigeOutput.setText(resultat);

        } catch (NumberFormatException e) {
            visFejl("Inputfejl", "Start- og slutuge skal være tal.");
        } catch (OperationNotAllowedException e) {
            visFejl("Fejl", e.getMessage());
        }
    }

    @FXML
    private void handleGenererRapport() {
        try {
            String projektId = rapportProjektIdInput.getValue();

            String rapport = App.getFacade().genererRapport(projektId);
            rapportOutput.setText(rapport);
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke generere rapport", e.getMessage());
        }
    }

    @FXML
    private void handleLogout() throws Exception {
        App.getFacade().removePropertyChangeListener(this);
        App.getFacade().userLogout();
        App.setRoot("primary");
    }

    private void visFejl(String titel, String besked) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titel);
        a.setHeaderText(null);
        a.setContentText(besked);
        a.showAndWait();
    }

    private void visInfo(String titel, String besked) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titel);
        a.setHeaderText(null);
        a.setContentText(besked);
        a.showAndWait();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        String eventName = event.getPropertyName();

        if ("PROJECT_OPRETTET".equals(eventName)
                || "PROJEKT_OMDOEBT".equals(eventName)
                || "AKTIVITET_OPRETTET".equals(eventName)
                || "AKTIVITET_OPDATERET".equals(eventName)
                || "AKTIVITET_SLETTET".equals(eventName)
                || "PROJEKTLEDER_OPDATERET".equals(eventName)
                || "MEDARBEJDER_TILKNYTTET_PROJEKT".equals(eventName)
                || "MEDARBEJDER_FJERNET_PROJEKT".equals(eventName)
                || "MEDARBEJDER_TILKNYTTET_AKTIVITET".equals(eventName)
                || "MEDARBEJDER_FJERNET_AKTIVITET".equals(eventName)
                || "FRAVAER_REGISTRERET".equals(eventName)
                || "HR_LISTE_SYNKRONISERET".equals(eventName)) {

            opdaterMedarbejderLister();
            opdaterProjektLister();
            opdaterAktiviteterTilTilknytning();
            opdaterAktiviteterTilTidsregistrering();
            opdaterAktiviteterTilOpdatering();
            opdaterAktiviteterTilSletning();
            opdaterAktiviteterTilFjernMedarbejder();
        }
    }
}