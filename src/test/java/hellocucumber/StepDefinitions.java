package hellocucumber;

import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

import dtu.example.domain.*;

public class StepDefinitions {
	
    private ErrorMessageHolder errorMessageHolder;
    private Planlaegningsvaerktoej planlaegningsvaerktoej;

	/* The only purpose of this constructor is to test
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
    public void medarbejderenÆndrerNavnetPåProjektTil(String string, String string2) throws OperationNotAllowedException {
        planlaegningsvaerktoej.omdoebProjekt(string, string2);
    }

    @Then("er projektets navn opdateret til {string} i systemet")
    public void erProjektetsNavnOpdateretTilISystemet(String string) {
        Projekt projekt = planlaegningsvaerktoej.findProjekt(string);
        assertNotNull(projekt, "Projektet med det nye navn findes ikke i systemet"); // Tjekker at der findes et projekt med navnet
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
    public void medarbejderenTilknytterSomProjektlederTilProjekt(String initialer, String projektnummer) throws OperationNotAllowedException {
        planlaegningsvaerktoej.opdaterProjektMedProjektleder(projektnummer, initialer);
    }

    @Then("er {string} registreret som projektleder for projekt {string}")
    public void erRegistreretSomProjektlederForProjekt(String initialer, String projektnummer) {
        assertEquals(planlaegningsvaerktoej.findMedarbejder(initialer), planlaegningsvaerktoej.findProjekt(projektnummer).getProjektleder());
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
    public void medarbejderenAngiverStartugeSlutugeOgEstimeretTidTimerForAktivitetenPåProjekt(Integer startuge, Integer slutuge, Double forventetTid, String aktivitetsNavn, String projektNr) {
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

    // =============================
    // ??
    // =============================

}
