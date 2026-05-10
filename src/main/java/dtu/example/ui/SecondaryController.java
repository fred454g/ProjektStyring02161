package dtu.example.ui;

import dtu.example.domain.Medarbejder;
import dtu.example.domain.OperationNotAllowedException;
import dtu.example.domain.Projekt;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * @author Frederik, Jeppe, Nikolai, Jacob
 */
public class SecondaryController implements PropertyChangeListener {

    // --- Projekter ---
    @FXML private TextField nytProjektNavnInput;

    // --- Projektleder ---
    @FXML private ComboBox<Projekt> projektIdForLederInput;
    @FXML private ComboBox<String> projektlederInitialerVaelger;

    // --- Projektmedarbejdere ---
    @FXML private ComboBox<Projekt> projektIdForProjektMedarbejderInput;
    @FXML private ComboBox<String> medarbejderTilProjektVaelger;
    @FXML private ComboBox<Projekt> projektIdForFjernProjektMedarbejderInput;
    @FXML private ComboBox<String> medarbejderFraProjektVaelger;

    // --- Omdøb projekt ---
    @FXML private ComboBox<Projekt> projektIdForOmdobInput;
    @FXML private TextField nytProjektNavnOmdobInput;

    // --- Slet projekt ---
    @FXML private ComboBox<Projekt> projektIdForSletProjektInput;

    // --- Opret aktivitet ---
    @FXML private ComboBox<Projekt> projektIdForAktivitetInput;
    @FXML private TextField aktivitetsNavnInput;
    @FXML private TextField budgetInput;
    @FXML private TextField startUgeAktivitetInput;
    @FXML private TextField slutUgeAktivitetInput;

    // --- Opdater aktivitet ---
    @FXML private ComboBox<Projekt> projektIdForOpdaterAktivitetInput;
    @FXML private ComboBox<String> aktivitetForOpdateringInput;
    @FXML private TextField nytBudgetInput;
    @FXML private TextField nyStartUgeInput;
    @FXML private TextField nySlutUgeInput;

    // --- Slet aktivitet ---
    @FXML private ComboBox<Projekt> projektIdForSletAktivitetInput;
    @FXML private ComboBox<String> aktivitetForSletningInput;

    // --- Tilknyt medarbejder til aktivitet ---
    @FXML private ComboBox<Projekt> projektIdForTilknytningInput;
    @FXML private ComboBox<String> aktivitetForTilknytningInput;
    @FXML private ComboBox<String> medarbejderInitialerVaelger;

    //--- ledighed ---
    @FXML private TextField ledigStartUgeInput;
    @FXML private TextField ledigSlutUgeInput;
    @FXML private TextArea ledigeOutput;

    // --- Fjern medarbejder fra aktivitet ---
    @FXML private ComboBox<Projekt> projektIdForFjernMedarbejderInput;
    @FXML private ComboBox<String> aktivitetForFjernMedarbejderInput;
    @FXML private ComboBox<String> medarbejderForFjernelseVaelger;

    // --- Tidsregistrering ---
    @FXML private ComboBox<Projekt> projektIdTidInput;
    @FXML private ComboBox<String> aktivitetsNavnTidInput;
    @FXML private TextField timerInput;
    @FXML private TextArea egneTimerOutput;

    // --- Fravær ---
    @FXML private TextField fravaerTypeInput;
    @FXML private TextField startUgeInput;
    @FXML private TextField slutUgeInput;

    // --- Rapport ---
    @FXML private ComboBox<Projekt> rapportProjektIdInput;
    @FXML private TextArea rapportOutput;

    @FXML
    public void initialize() {
        App.getFacade().addPropertyChangeListener(this);

        konfigurerProjektVaelgere();
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
        List<Projekt> projekter = App.getFacade().getProjekter();

        projektIdForLederInput.getItems().setAll(projekter);
        projektIdForAktivitetInput.getItems().setAll(projekter);
        projektIdForTilknytningInput.getItems().setAll(projekter);
        projektIdTidInput.getItems().setAll(projekter);
        rapportProjektIdInput.getItems().setAll(projekter);

        projektIdForProjektMedarbejderInput.getItems().setAll(projekter);
        projektIdForFjernProjektMedarbejderInput.getItems().setAll(projekter);
        projektIdForOmdobInput.getItems().setAll(projekter);
        projektIdForSletProjektInput.getItems().setAll(projekter);
        projektIdForOpdaterAktivitetInput.getItems().setAll(projekter);
        projektIdForSletAktivitetInput.getItems().setAll(projekter);
        projektIdForFjernMedarbejderInput.getItems().setAll(projekter);
    }

    private void konfigurerProjektVaelgere() {
        konfigurerProjektVaelger(projektIdForLederInput);
        konfigurerProjektVaelger(projektIdForAktivitetInput);
        konfigurerProjektVaelger(projektIdForTilknytningInput);
        konfigurerProjektVaelger(projektIdTidInput);
        konfigurerProjektVaelger(rapportProjektIdInput);
        konfigurerProjektVaelger(projektIdForProjektMedarbejderInput);
        konfigurerProjektVaelger(projektIdForFjernProjektMedarbejderInput);
        konfigurerProjektVaelger(projektIdForOmdobInput);
        konfigurerProjektVaelger(projektIdForSletProjektInput);
        konfigurerProjektVaelger(projektIdForOpdaterAktivitetInput);
        konfigurerProjektVaelger(projektIdForSletAktivitetInput);
        konfigurerProjektVaelger(projektIdForFjernMedarbejderInput);
    }

    private void konfigurerProjektVaelger(ComboBox<Projekt> projektBox) {
        projektBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Projekt projekt, boolean empty) {
                super.updateItem(projekt, empty);
                setText(empty ? null : formatProjekt(projekt));
            }
        });

        projektBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Projekt projekt, boolean empty) {
                super.updateItem(projekt, empty);
                setText(empty ? null : formatProjekt(projekt));
            }
        });
    }

    private String formatProjekt(Projekt projekt) {
        if (projekt == null) {
            return null;
        }

        return projekt.getProjektNummer() + " - " + projekt.getProjektNavn();
    }

    private String getProjektNummer(ComboBox<Projekt> projektBox) {
        Projekt projekt = projektBox.getValue();
        return projekt == null ? null : projekt.getProjektNummer();
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

    private void opdaterAktivitetsDropdown(ComboBox<Projekt> projektBox, ComboBox<String> aktivitetBox) {
        try {
            String projektId = getProjektNummer(projektBox);
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
            String projektId = getProjektNummer(projektIdForOmdobInput);
            String nytNavn = nytProjektNavnOmdobInput.getText();

            App.getFacade().omdoebProjekt(projektId, nytNavn);

            nytProjektNavnOmdobInput.clear();

            visInfo("Projekt omdøbt", "Projektet er omdøbt.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke omdøbe projekt", e.getMessage());
        }
    }

    @FXML
    private void handleSletProjekt() {
        try {
            String projektId = getProjektNummer(projektIdForSletProjektInput);

            App.getFacade().sletProjekt(projektId);

            visInfo("Projekt slettet", "Projektet er slettet.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke slette projekt", e.getMessage());
        }
    }

    @FXML
    private void handleTilknytProjektleder() {
        try {
            String projektId = getProjektNummer(projektIdForLederInput);
            String projektVisning = formatProjekt(projektIdForLederInput.getValue());
            String initialer = projektlederInitialerVaelger.getValue();

            App.getFacade().opdaterProjektMedProjektleder(projektId, initialer);

            visInfo("Projektleder tilknyttet",
                    initialer + " er nu projektleder for projekt " + projektVisning + ".");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke tilknytte projektleder", e.getMessage());
        }
    }

    @FXML
    private void handleTilknytMedarbejderTilProjekt() {
        try {
            String projektId = getProjektNummer(projektIdForProjektMedarbejderInput);
            String projektVisning = formatProjekt(projektIdForProjektMedarbejderInput.getValue());
            String initialer = medarbejderTilProjektVaelger.getValue();

            App.getFacade().tilfoejMedarbejderTilProjekt(projektId, initialer);

            visInfo("Medarbejder tilknyttet",
                    initialer + " er tilknyttet projekt " + projektVisning + ".");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke tilknytte medarbejder", e.getMessage());
        }
    }

    @FXML
    private void handleFjernMedarbejderFraProjekt() {
        try {
            String projektId = getProjektNummer(projektIdForFjernProjektMedarbejderInput);
            String projektVisning = formatProjekt(projektIdForFjernProjektMedarbejderInput.getValue());
            String initialer = medarbejderFraProjektVaelger.getValue();

            App.getFacade().fjernMedarbejderFraProjekt(projektId, initialer);

            visInfo("Medarbejder fjernet",
                    initialer + " er fjernet fra projekt " + projektVisning + ".");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke fjerne medarbejder", e.getMessage());
        }
    }

    @FXML
    private void handleOpretAktivitet() {
        try {
            String projektId = getProjektNummer(projektIdForAktivitetInput);
            String projektVisning = formatProjekt(projektIdForAktivitetInput.getValue());
            String navn = aktivitetsNavnInput.getText();
            double budget = Double.parseDouble(budgetInput.getText());
            int startUge = Integer.parseInt(startUgeAktivitetInput.getText());
            int slutUge = Integer.parseInt(slutUgeAktivitetInput.getText());

            App.getFacade().opretAktivitet(projektId, navn, budget, startUge, slutUge);

            aktivitetsNavnInput.clear();
            budgetInput.clear();
            startUgeAktivitetInput.clear();
            slutUgeAktivitetInput.clear();

            visInfo("Aktivitet oprettet", "Aktiviteten er oprettet på projekt " + projektVisning + ".");
        } catch (NumberFormatException e) {
            visFejl("Inputfejl", "Budget, startuge og slutuge skal være tal.");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke oprette aktivitet", e.getMessage());
        }
    }

    @FXML
    private void handleOpdaterAktivitet() {
        try {
            String projektId = getProjektNummer(projektIdForOpdaterAktivitetInput);
            String aktivitet = aktivitetForOpdateringInput.getValue();
            double budget = Double.parseDouble(nytBudgetInput.getText());
            int start = Integer.parseInt(nyStartUgeInput.getText());
            int slut = Integer.parseInt(nySlutUgeInput.getText());

            App.getFacade().redigerAktivitet(projektId, aktivitet, budget, start, slut);

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
            String projektId = getProjektNummer(projektIdForSletAktivitetInput);
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
            String projektId = getProjektNummer(projektIdForTilknytningInput);
            String aktivitetsNavn = aktivitetForTilknytningInput.getValue();
            String initialer = medarbejderInitialerVaelger.getValue();

            App.getFacade().tilfoejMedarbejderTilAktivitet(projektId, aktivitetsNavn, initialer);

            visInfo("Medarbejder tilknyttet",
                    initialer + " er tilknyttet aktiviteten " + aktivitetsNavn + ".");
        } catch (OperationNotAllowedException e) {
            visFejl("Kunne ikke tilknytte medarbejder", e.getMessage());
        }
    }

    @FXML
    private void handleFjernMedarbejderFraAktivitet() {
        try {
            String projektId = getProjektNummer(projektIdForFjernMedarbejderInput);
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
            String projektId = getProjektNummer(projektIdTidInput);
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
            String projektId = getProjektNummer(rapportProjektIdInput);

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
                || "PROJEKT_SLETTET".equals(eventName)
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
