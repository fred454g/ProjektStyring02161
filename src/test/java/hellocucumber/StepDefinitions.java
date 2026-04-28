package hellocucumber;

import dtu.example.domain.Planlaegningsvaerktoej;
import dtu.example.ui.App;
import io.cucumber.java.After;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

import dtu.example.domain.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class StepDefinitions {

    private ErrorMessageHolder errorMessageHolder;
    private Planlaegningsvaerktoej planlaegningsvaerktoej;
    private String genereretRapport = "";
    private double totalTimer;
    private static final Path HR_LISTE_PATH = Paths.get("src", "main", "java", "dtu", "example", "hr_liste.txt");
    private String hrListeOriginalIndhold;
    private boolean hrListeBackupTaget;
    private String sidsteObserverEvent;
    private Planlaegningsvaerktoej app = new Planlaegningsvaerktoej();

    /*
     * The only purpose of this constructor is to test
     * if Cucumber Dependency Injection using Picocontainer works.
     */
    public StepDefinitions(ErrorMessageHolder errorMessageHolder, Planlaegningsvaerktoej planlaegningsvaerktoej) {
        this.errorMessageHolder = errorMessageHolder;
        this.planlaegningsvaerktoej = planlaegningsvaerktoej;
        this.planlaegningsvaerktoej.addPropertyChangeListener(event -> this.sidsteObserverEvent = event.getPropertyName());
    }

    // =============================
    // opret_projekt.feature
    // =============================

    @Given("at medarbejderen {string} er logget ind i systemet")
    public void atMedarbejderenErLoggetIndISystemet(String medarbejderInitialer) {
        try {
            planlaegningsvaerktoej.userLogin(medarbejderInitialer);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("medarbejderen forsoeger at oprette et projekt uden at angive et navn")
    public void medarbejderenForsoegerAtOpretteEtProjektUdenAtAngiveEtNavn() {
        try {
            planlaegningsvaerktoej.opretProjekt(null);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("medarbejderen opretter et projekt med navnet {string}")
    public void medarbejderenOpretterEtProjektMedNavnet(String projektNavn) throws OperationNotAllowedException {
        planlaegningsvaerktoej.opretProjekt(projektNavn);
    }

    @Then("eksisterer projektet {string} i systemet")
    public void eksistererProjektetISystemet(String projektNavn) {
        Projekt fundneProjekt = planlaegningsvaerktoej.findProjekt(projektNavn);
        assertNotNull(fundneProjekt, "Projektet skulle eksistere i systemet");
        assertEquals(projektNavn, fundneProjekt.getProjektNavn());
    }

    @Then("projektet tildeles automatisk et unikt projektnummer, f.eks. {string}")
    public void projektetTildelesAutomatiskEtUniktProjektnummerFEks(String projektnummer) {
        Projekt fundneProjekt = planlaegningsvaerktoej.findProjekt(projektnummer);
        assertEquals(projektnummer, fundneProjekt.getProjektNummer());
    }

    @Then("giver systemet fejlmeddelelsen {string}")
    public void giverSystemetFejlmeddelelsen(String fejlMeddelelse) throws Exception {
        assertEquals(fejlMeddelelse, this.errorMessageHolder.getErrorMessage());
    }

    // =============================
    // rediger_projektnavn.feature
    // =============================
    @Given("at projektet {string} med navnet {string} findes i systemet")
    public void atProjektetMedNavnetFindesISystemet(String projektnummer, String projektNavn) {
        assertEquals(projektnummer, planlaegningsvaerktoej.findProjekt(projektnummer).getProjektNummer());
        assertEquals(projektNavn, planlaegningsvaerktoej.findProjekt(projektNavn).getProjektNavn());
    }

    @When("medarbejderen ændrer navnet på projekt {string} til {string}")
    public void medarbejderenÆndrerNavnetPåProjektTil(String string, String string2)
            throws OperationNotAllowedException {
        planlaegningsvaerktoej.omdoebProjekt(string, string2);
    }

    @Then("er projektets navn opdateret til {string} i systemet")
    public void erProjektetsNavnOpdateretTilISystemet(String string) {
        Projekt projekt = planlaegningsvaerktoej.findProjekt(string);
        assertNotNull(projekt, "Projektet med det nye navn findes ikke i systemet"); // Tjekker at der findes et projekt
                                                                                     // med navnet
        assertEquals(string, projekt.getProjektNavn()); // Passser navnene
    }

    // =============================
    // tilknyt_projektleder.feature
    // =============================
    @Given("at medarbejderen {string} tilfoejes til systemet")
    public void atMedarbejderenTilfoejesTilSystemet(String initialer) throws OperationNotAllowedException {
        planlaegningsvaerktoej.nyMedarbejder(initialer);
    }

    @Given("at projektet {string} findes i systemet")
    public void atProjektetFindesISystemet(String projektnummer) {
        assertEquals(projektnummer, planlaegningsvaerktoej.findProjekt(projektnummer).getProjektNummer());
    }

    @Given("at medarbejderen med initialerne {string} findes i systemet")
    public void atMedarbejderenMedInitialerneFindesISystemet(String initialer) {
        assertEquals(initialer, planlaegningsvaerktoej.findMedarbejder(initialer).getInitialer());
    }

    @When("medarbejderen tilknytter {string} som projektleder til projekt {string}")
    public void medarbejderenTilknytterSomProjektlederTilProjekt(String initialer, String projektnummer)
            throws OperationNotAllowedException {
        planlaegningsvaerktoej.opdaterProjektMedProjektleder(projektnummer, initialer);
    }

    @Then("er {string} registreret som projektleder for projekt {string}")
    public void erRegistreretSomProjektlederForProjekt(String initialer, String projektnummer) {
        assertEquals(planlaegningsvaerktoej.findMedarbejder(initialer),
                planlaegningsvaerktoej.findProjekt(projektnummer).getProjektleder());
    }

    // =============================
    // tilfoej_medarbejder_projekt
    // =============================
    @When("medarbejderen tilfoejer {string} til projekt {string}")
    public void medarbejderenTilfoejerTilProjekt(String medarbejderInitialer, String projektNr) {
        try {
            planlaegningsvaerktoej.tilknytMedarbejderTilProjekt(projektNr, medarbejderInitialer);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("fremgår {string} af listen over tilknyttede medarbejdere på projekt {string}")
    public void fremgårAfListenOverTilknyttedeMedarbejderePåProjekt(String medarbejderInitialer, String projektNummer) {
        Medarbejder medarbejder = planlaegningsvaerktoej.findMedarbejder(medarbejderInitialer);
        assertTrue(planlaegningsvaerktoej.findProjekt(projektNummer).isMedarbejderInProjekt(medarbejder));
    }

    // =============================
    // opret_og_rediger_aktivitet
    // =============================
    @When("medarbejderen opretter aktiviteten {string} på projekt {string}")
    public void medarbejderenOpretterAktivitetenPåProjekt(String aktivitetsNavn, String projektNr) {
        try {
            planlaegningsvaerktoej.opretAktivitet(projektNr, aktivitetsNavn, 0.0, 1, 1);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("medarbejderen angiver startuge {int}, slutuge {int} og estimeret tid {double} timer for aktiviteten {string} på projekt {string}")
    public void medarbejderenAngiverStartugeSlutugeOgEstimeretTidTimerForAktivitetenPåProjekt(Integer startuge,
            Integer slutuge, Double forventetTid, String aktivitetsNavn, String projektNr) {
        try {
            planlaegningsvaerktoej.opdaterAktivitet(projektNr, aktivitetsNavn, forventetTid, startuge, slutuge);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }

    }

    @Then("er aktiviteten {string} oprettet på projekt {string}")
    public void erAktivitetenOprettetPåProjekt(String aktivitetsNavn, String projektNr) {
        Projekt projekt = planlaegningsvaerktoej.findProjekt(projektNr);
        assertNotNull(projekt.findAktivitet(aktivitetsNavn), "Aktivitet ikke oprettet");
    }

    @Then("aktiviteten {string} på projekt {string} har startuge {int}, slutuge {int} og estimeret tid {double} timer")
    public void aktivitetenPåProjektHarStartugeSlutugeOgEstimeretTidTimer(String aktivitetsNavn, String projektNr, Integer startuge, Integer slutuge, Double forventetTid) {
        Projekt projekt = planlaegningsvaerktoej.findProjekt(projektNr);
        Aktivitet aktivitet = projekt.findAktivitet(aktivitetsNavn);
        assertEquals(startuge, aktivitet.getStartstidspunkt());
        assertEquals(slutuge, aktivitet.getSluttidspunkt());
        assertEquals(forventetTid, aktivitet.getForventedeAntalArbejdsTimer());
    }

    @Given("at aktiviteten {string} er oprettet på projekt {string}")
    public void atAktivitetenErOprettetPåProjekt(String aktivitetsnavn, String projektnummer) {
        try {
            planlaegningsvaerktoej.opretAktivitet(projektnummer, aktivitetsnavn, 0.0, 1, 1);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @When("medarbejderen fjerner aktiviteten {string} på projekt {string}")
    public void medarbejderenFjernerAktivitetenPåProjekt(String aktivitetsnavn, String projektnummer) {
        try {
            planlaegningsvaerktoej.sletAktivitet(projektnummer, aktivitetsnavn);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("er aktiviteten {string} ikke tilknyttet projekt {string}")
    public void erAktivitetenIkkeTilknyttetProjekt(String aktivitetsnavn, String projektnummer) {
        Projekt projekt = planlaegningsvaerktoej.findProjekt(projektnummer);
        assertNull(projekt.findAktivitet(aktivitetsnavn), "FEJL: aktivitet er ikke fjernet");
    }

    // =============================
    // administrer_medarbejder_aktivitet
    // =============================
    @Given("at medarbejderen {string} er tilknyttet aktiviteten {string} på projekt {string}")
    public void atMedarbejderenErTilknyttetAktivitetenPåProjekt(String initialer, String aktivitetsNavn,
            String projektNr) throws OperationNotAllowedException {
        planlaegningsvaerktoej.tilknytMedarbejderTilAktivitet(projektNr, aktivitetsNavn, initialer);
    }

    @When("medarbejderen fjerner {string} fra aktiviteten {string} på projekt {string}")
    public void medarbejderenFjernerFraAktivitetenPåProjekt(String initialer, String aktivitetsNavn,
            String projektNr) {
        try {
            planlaegningsvaerktoej.fjernMedarbejderFraAktivitet(projektNr, aktivitetsNavn, initialer);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("er {string} ikke længere tilknyttet aktiviteten {string} på projekt {string}")
    public void erIkkeLængereTilknyttetAktivitetenPåProjekt(String initialer, String aktivitetsNavn,
            String projektNr) {
        Medarbejder medarbejder = planlaegningsvaerktoej.findMedarbejder(initialer);
        assertFalse(planlaegningsvaerktoej.findProjekt(projektNr).findAktivitet(aktivitetsNavn)
                .isMedarbejderInAktivitet(medarbejder));
    }

    @Given("at aktiviteten {string} findes på projekt {string}")
    public void atAktivitetenFindesPåProjekt(String aktivitetsNavn, String projektNr)
            throws OperationNotAllowedException {
        planlaegningsvaerktoej.opretAktivitet(projektNr, aktivitetsNavn, 0.0, 1, 1);
    }

    @Given("at medarbejderen {string} er tilknyttet projekt {string}")
    public void atMedarbejderenErTilknyttetProjekt(String initialer, String projektNr)
            throws OperationNotAllowedException {
        planlaegningsvaerktoej.tilknytMedarbejderTilProjekt(projektNr, initialer);
    }

    @When("medarbejderen tilknytter {string} til aktiviteten {string} på projekt {string}")
    public void medarbejderenTilknytterTilAktivitetenPåProjekt(String initialer, String aktivitetsNavn,
            String projektNr) {
        try {
            planlaegningsvaerktoej.tilknytMedarbejderTilAktivitet(projektNr, aktivitetsNavn, initialer);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("er {string} tilknyttet aktiviteten {string} på projekt {string}")
    public void erTilknyttetAktivitetenPåProjekt(String initialer, String aktivitetsNavn, String projektNr) {
        Medarbejder medarbejder = planlaegningsvaerktoej.findMedarbejder(initialer);
        assertTrue(planlaegningsvaerktoej.findProjekt(projektNr).findAktivitet(aktivitetsNavn)
                .isMedarbejderInAktivitet(medarbejder));
    }

    // =============================
    // indlaes_hr_liste
    // =============================
    @Given("at en ny HR-fil med medarbejderinitialer er tilgængelig")
    public void atEnNyHRFilMedMedarbejderinitialerErTilgængelig() {
        try {
            planlaegningsvaerktoej.nyMedarbejder("jfk");
            planlaegningsvaerktoej.nyMedarbejder("temp");
        } catch (OperationNotAllowedException e) {
            fail("Kunne ikke oprette testdata: " + e.getMessage());
        }

        String hrIndhold = "jfk\n" +
                "huba\n";

        try {
            if (!hrListeBackupTaget) {
                hrListeOriginalIndhold = Files.exists(HR_LISTE_PATH) ? Files.readString(HR_LISTE_PATH) : null;
                hrListeBackupTaget = true;
            }

            Files.writeString(
                    HR_LISTE_PATH,
                    hrIndhold,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            fail("Kunne ikke skrive HR-fil: " + e.getMessage());
        }
    }

    @After
    public void gendanHrListeEfterScenario() {
        if (!hrListeBackupTaget) {
            return;
        }

        try {
            if (hrListeOriginalIndhold == null) {
                Files.deleteIfExists(HR_LISTE_PATH);
            } else {
                Files.writeString(
                        HR_LISTE_PATH,
                        hrListeOriginalIndhold,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE);
            }
        } catch (IOException e) {
            fail("Kunne ikke gendanne HR-fil: " + e.getMessage());
        } finally {
            hrListeBackupTaget = false;
            hrListeOriginalIndhold = null;
        }
    }

    @When("systemet udfoerer sin automatiske file load")
    public void systemetUdfoererSinAutomatiskeFileLoad() {
        planlaegningsvaerktoej.indlaesFil();
    }

    @Then("oprettes nye medarbejdere i systemet baseret på initialerne i filen")
    public void oprettesNyeMedarbejdereISystemetBaseretPåInitialerneIFilen() {
        assertNotNull(planlaegningsvaerktoej.findMedarbejder("jfk"), "jfk skulle være oprettet via HR-fil");
        assertNotNull(planlaegningsvaerktoej.findMedarbejder("huba"), "huba skulle være oprettet via HR-fil");
    }

    @Then("eksisterende medarbejdere opdateres")
    public void eksisterendeMedarbejdereOpdateres() {
        Medarbejder jfk = planlaegningsvaerktoej.findMedarbejder("jfk");
        Medarbejder huba = planlaegningsvaerktoej.findMedarbejder("huba");

        assertNotNull(jfk, "jfk skulle eksistere efter HR-fil load");
        assertNotNull(huba, "huba skulle eksistere efter HR-fil load");

        // Bekræfter at sync-logik fjerner medarbejdere, der ikke længere er i filen.
        assertNull(planlaegningsvaerktoej.findMedarbejder("temp"), "temp skulle fjernes ved HR-sync");
    }

    // =============================
    // registrer_tid.feature
    // =============================

    @Given("at medarbejderen {string} er logget ud")
    public void atMedarbejderenErLoggetUd(String initialer) {
        planlaegningsvaerktoej.userLogout();
    }

    @When("medarbejderen registrerer {double} timer på aktiviteten {string} på projekt {string} for dags dato")
    public void medarbejderenRegistrererTimerPåAktivitetenPåProjektForDagsDato(Double timer, String aktivitetsNavn, String projektNr) {
        try {
            planlaegningsvaerktoej.registrerTid(projektNr, aktivitetsNavn, timer);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("er {double} timer tilføjet til det samlede tidsforbrug for {string} på aktiviteten {string} på projekt {string}")
    public void erTimerTilføjetTilDetSamledeTidsforbrugForPåAktivitetenPåProjekt(Double timer, String initialer, String aktivitetsNavn, String projektNr) {
        Projekt projekt = planlaegningsvaerktoej.findProjekt(projektNr);
        Aktivitet aktivitet = projekt.findAktivitet(aktivitetsNavn);
        double registreret = aktivitet.getRegistreretTidForMedarbejder(initialer);
        assertEquals(timer, registreret);
    }

    // =============================
    // vis_egne_timer.feature
    // =============================
    @When("medarbejderen anmoder om at se sine egne tidsregistreringer")
    public void medarbejderenAnmoderOmAtSeSineEgneTidsregistreringer() {
        try {
            totalTimer = planlaegningsvaerktoej.visEgneTimer();
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("viser systemet {double} timer totalt for medarbejderen")
    public void viserSystemetTimerTotaltForMedarbejderen(Double timer) {
        assertEquals(timer, totalTimer);
    }

    @Then("systemet viser {double} timer på aktiviteten {string} på projekt {string}")
    public void systemetViserTimerPåAktivitetenPåProjekt(Double timer, String aktivitetsNavn, String projektNr) {
        String initialer = planlaegningsvaerktoej.getLoggedinUserInitials();
        Projekt projekt = planlaegningsvaerktoej.findProjekt(projektNr);
        Aktivitet aktivitet = projekt.findAktivitet(aktivitetsNavn);
        double registreret = aktivitet.getRegistreretTidForMedarbejder(initialer);

        assertEquals(timer, registreret);
    }

    // =============================
    // registrer_fravaer.feature
    // =============================

    @When("medarbejderen registrerer fravær af typen {string} fra uge {int} til uge {int}")
    public void medarbejderenRegistrererFraværAfTypenFraUgeTilUge(String type, Integer startUge, Integer slutUge) {
        try {
            planlaegningsvaerktoej.registrerFravaer(type, startUge, slutUge);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("er medarbejderen markeret som fraværende med typen {string} fra uge {int} til uge {int}")
    public void erMedarbejderenMarkeretSomFraværendeMedTypenFraUgeTilUge(String type, Integer startUge, Integer slutUge) {
        String initialer = planlaegningsvaerktoej.getLoggedinUserInitials();
        assertTrue(planlaegningsvaerktoej.harFravaer(initialer, type, startUge, slutUge));
    }

    // =============================
    // fjern_medarbejder_projekt.feature
    // =============================
    @When("medarbejderen fjerner {string} fra projekt {string}")
    public void medarbejderenFjernerFraProjekt(String medarbejderInitialer, String projektNummer) {
        try {
            planlaegningsvaerktoej.fjernMedarbejderFraProjekt(projektNummer, medarbejderInitialer);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("fremgår {string} ikke længere af listen over tilknyttede medarbejdere på projekt {string}")
    public void fremgårIkkeLængereAfListenOverTilknyttedeMedarbejderePåProjekt(String medarbejderInitialer, String projektNummer) {
        Medarbejder medarbejder = planlaegningsvaerktoej.findMedarbejder(medarbejderInitialer);
        assertFalse(planlaegningsvaerktoej.findProjekt(projektNummer).isMedarbejderInProjekt(medarbejder));
    }

    @Then("udsendes observer-eventen {string}")
    public void udsendesObserverEventen(String eventNavn) {
        assertEquals(eventNavn, this.sidsteObserverEvent);
    }

    // =============================
    // ??
    // =============================


    // ==========================================
    // STEPS TIL RAPPORTGENERERING
    // ==========================================

    @Given("at medarbejderen er logget ind i systemet")
    public void at_medarbejderen_er_logget_ind_i_systemet() throws Exception {
        // Sikrer at systemet har data og logger ind
        app.indlaesFil();
        app.userLogin("huba"); // Sørg for at "huba" er i HR-listen, ellers skal du oprette ham først
    }

    @Given("at projekt {string} med navnet {string} eksisterer")
    public void at_projekt_med_navnet_eksisterer(String projektNummer, String projektNavn) throws Exception {
        // Vi simulerer at brugeren har oprettet projektet gennem systemet
        // Ret eventuelt metodens parametre, så de matcher jeres faktiske app.opretProjekt()
        app.opretProjekt(projektNavn);
    }

    @Given("at projektet har en aktivitet {string} med budget på {int} timer")
    public void at_projektet_har_en_aktivitet_med_budget_paa_timer(String aktivitetsNavn, Integer budget) throws Exception {
        // Simulerer oprettelse af aktivitet i det netop oprettede projekt "26001"
        // Bemærk: '10' og '12' er bare dummy start/slut uger for at få metoden til at køre
        app.opretAktivitet("26001", aktivitetsNavn, (double) budget, 10, 12);
    }

    @When("medarbejderen anmoder om en rapport for projekt {string}")
    public void medarbejderen_anmoder_om_en_rapport_for_projekt(String projektNummer) throws Exception {
        // Her trækker vi data ud af facaden, præcis som UI'en vil gøre det!
        genereretRapport = app.genererRapport(projektNummer);
    }

    @Then("modtager systemet en rapport, der indeholder projektnavnet {string}")
    public void modtager_systemet_en_rapport_der_indeholder_projektnavnet(String forventetNavn) {
        // Vi bruger JUnit's assertTrue til at bevise, at strengen indeholder det rigtige
        assertTrue(genereretRapport.contains(forventetNavn), "Rapporten mangler projektnavnet");
    }

    @Then("rapporten viser at totalt budget er {int} timer")
    public void rapporten_viser_at_totalt_budget_er_timer(Integer timer) {
        assertTrue(
                genereretRapport.contains("Budgetteret tid: " + timer + ".0 timer")
                        || genereretRapport.contains("Total Budget: " + timer + ".0 timer")
                        || genereretRapport.contains("Total Budget: " + timer + " timer"),
                "Rapporten har forkert budget"
        );
    }
}

