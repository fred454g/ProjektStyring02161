package hellocucumber;

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
    private static final Path HR_LISTE_PATH = Paths.get("src", "main", "java", "dtu", "example", "hr_liste.txt");
    private String hrListeOriginalIndhold;
    private boolean hrListeBackupTaget;

    /*
     * The only purpose of this constructor is to test
     * if Cucumber Dependency Injection using Picocontainer works.
     */
    public StepDefinitions(ErrorMessageHolder errorMessageHolder, Planlaegningsvaerktoej planlaegningsvaerktoej) {
        this.errorMessageHolder = errorMessageHolder;
        this.planlaegningsvaerktoej = planlaegningsvaerktoej;
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
    @Given("at medarbejderen {string} med navn {string} tilfoejes til systemet")
    public void atMedarbejderenTilfoejesTilSystemet(String initialer, String navn) throws OperationNotAllowedException {
        planlaegningsvaerktoej.nyMedarbejder(navn, initialer);
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
        Projekt projekt = planlaegningsvaerktoej.findProjekt(projektNr);
        try {
            projekt.opretAktivitet(projektNr, aktivitetsNavn);
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
    public void aktivitetenPåProjektHarStartugeSlutugeOgEstimeretTidTimer(String aktivitetsNavn, String projektNr,
            Integer startuge, Integer slutuge, Double forventetTid) {
        Projekt projekt = planlaegningsvaerktoej.findProjekt(projektNr);
        Aktivitet aktivitet = projekt.findAktivitet(aktivitetsNavn);
        assertEquals(startuge, aktivitet.getStartstidspunkt());
        assertEquals(slutuge, aktivitet.getSluttidspunkt());
        assertEquals(forventetTid, aktivitet.getForventedeAntalArbejdsTimer());
    }

    // =============================
    // administrer_medarbejder_aktivitet
    // =============================
    @Given("at medarbejderen {string} er tilknyttet aktiviteten {string} på projekt {string}")
    public void atMedarbejderenErTilknyttetAktivitetenPåProjekt(String medarbejderInfo, String aktivitetsNavn,
            String projektNr) throws OperationNotAllowedException {
        planlaegningsvaerktoej.tilknytMedarbejderTilAktivitet(projektNr, aktivitetsNavn, medarbejderInfo);
    }

    @When("medarbejderen fjerner {string} fra aktiviteten {string} på projekt {string}")
    public void medarbejderenFjernerFraAktivitetenPåProjekt(String medarbejderInfo, String aktivitetsNavn,
            String projektNr) {
        try {
            planlaegningsvaerktoej.fjernMedarbejderFraAktivitet(projektNr, aktivitetsNavn, medarbejderInfo);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("er {string} ikke længere tilknyttet aktiviteten {string} på projekt {string}")
    public void erIkkeLængereTilknyttetAktivitetenPåProjekt(String medarbejderInfo, String aktivitetsNavn,
            String projektNr) {
        Medarbejder medarbejder = planlaegningsvaerktoej.findMedarbejder(medarbejderInfo);
        assertFalse(planlaegningsvaerktoej.findProjekt(projektNr).findAktivitet(aktivitetsNavn)
                .isMedarbejderInAktivitet(medarbejder));
    }

    @Given("at aktiviteten {string} findes på projekt {string}")
    public void atAktivitetenFindesPåProjekt(String aktivitetsNavn, String projektNr)
            throws OperationNotAllowedException {
        planlaegningsvaerktoej.opretAktivitet(projektNr, aktivitetsNavn);
    }

    @Given("at medarbejderen {string} er tilknyttet projekt {string}")
    public void atMedarbejderenErTilknyttetProjekt(String medarbejderInfo, String projektNr)
            throws OperationNotAllowedException {
        planlaegningsvaerktoej.tilknytMedarbejderTilProjekt(projektNr, medarbejderInfo);
    }

    @When("medarbejderen tilknytter {string} til aktiviteten {string} på projekt {string}")
    public void medarbejderenTilknytterTilAktivitetenPåProjekt(String medarbejderInfo, String aktivitetsNavn,
            String projektNr) {
        try {
            planlaegningsvaerktoej.tilknytMedarbejderTilAktivitet(projektNr, aktivitetsNavn, medarbejderInfo);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("er {string} tilknyttet aktiviteten {string} på projekt {string}")
    public void erTilknyttetAktivitetenPåProjekt(String medarbejderInfo, String aktivitetsNavn, String projektNr) {
        Medarbejder medarbejder = planlaegningsvaerktoej.findMedarbejder(medarbejderInfo);
        assertTrue(planlaegningsvaerktoej.findProjekt(projektNr).findAktivitet(aktivitetsNavn)
                .isMedarbejderInAktivitet(medarbejder));
    }

    // =============================
    // indlaes_hr_liste
    // =============================
    @Given("at en ny HR-fil med medarbejderinitialer er tilgængelig")
    public void atEnNyHRFilMedMedarbejderinitialerErTilgængelig() {
        try {
            planlaegningsvaerktoej.nyMedarbejder("Outdated JFK", "jfk");
            planlaegningsvaerktoej.nyMedarbejder("Skal Fjernes", "temp");
        } catch (OperationNotAllowedException e) {
            fail("Kunne ikke oprette testdata: " + e.getMessage());
        }

        String hrIndhold = "jfk, John F. Kennedy\n" +
                "huba, Hubert Baumeister\n";

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
        assertEquals("John F. Kennedy", jfk.getNavn());
        assertEquals("Hubert Baumeister", huba.getNavn());

        // Bekræfter at sync-logik fjerner medarbejdere, der ikke længere er i filen.
        assertNull(planlaegningsvaerktoej.findMedarbejder("temp"), "temp skulle fjernes ved HR-sync");
    }

    // =============================
    // ??
    // =============================

}
