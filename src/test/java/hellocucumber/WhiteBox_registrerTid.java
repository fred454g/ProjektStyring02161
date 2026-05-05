package hellocucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dtu.example.domain.OperationNotAllowedException;
import dtu.example.domain.Planlaegningsvaerktoej;
import dtu.example.domain.Projekt;
import dtu.example.domain.Aktivitet;

public class WhiteBox_registrerTid {
    

    private Planlaegningsvaerktoej planlaegningsvaerktoej;

    @BeforeEach
    public void setUp() throws Exception {
        planlaegningsvaerktoej = new Planlaegningsvaerktoej();
    }

        /*
     * Sæt A:
     * 1 true
     * loggedInUser == null
     */
    @Test
    public void testSetA_IngenBrugerLoggedIn() throws Exception {

        try {
            planlaegningsvaerktoej.registrerTid("26001", "Analyse", 2.0);

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Ingen bruger logged in", exception.getMessage());
        }
    }

        /*
     * Sæt B:
     * 1 false, 2 true (2a true)
     * projektNr == null
     */
    @Test
    public void testSetB_ProjektNrErNull() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.registrerTid(null, "Analyse", 2.0);

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Projekt skal vælges", exception.getMessage());
        }
    }

        /*
     * Sæt C:
     * 1 false, 2 true (2a false, 2b true)
     * projektNr != null && projektNr.isBlank()
     */
    @Test
    public void testSetC_ProjektNrErBlank() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.registrerTid("   ", "Analyse", 2.0);

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Projekt skal vælges", exception.getMessage());
        }
    }

        /*
     * Sæt D:
     * 1 false, 2 false, 3 true (3a true)
     * aktivitetsNavn == null
     */
    @Test
    public void testSetD_AktivitetsNavnErNull() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.registrerTid("26001", null, 2.0);

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Aktivitet skal vælges", exception.getMessage());
        }
    }

        /*
     * Sæt E:
     * 1 false, 2 false, 3 true (3a false, 3b true)
     * aktivitetsNavn != null && aktivitetsNavn.isBlank()
     */
    @Test
    public void testSetE_AktivitetsNavnErBlank() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.registrerTid("26001", "   ", 2.0);

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Aktivitet skal vælges", exception.getMessage());
        }
    }

        /*
     * Sæt F:
     * 1 false, 2 false, 3 false, 4 true (4a true)
     * timer == null
     */
    @Test
    public void testSetF_TimerErNull() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.registrerTid("26001", "Analyse", null);

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Antal timer skal være større end 0", exception.getMessage());
        }
    }

        /*
     * Sæt G:
     * 1 false, 2 false, 3 false, 4 true (4a false, 4b true)
     * timer != null && timer <= 0
     */
    @Test
    public void testSetG_TimerErNul() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.registrerTid("26001", "Analyse", 0.0);

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Antal timer skal være større end 0", exception.getMessage());
        }
    }

        /*
     * Sæt H:
     * 1 false, 2 false, 3 false, 4 false, 5 true
     * timer > 24
     */
    @Test
    public void testSetH_TimerOver24() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.registrerTid("26001", "Analyse", 25.0);

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Antal timer kan ikke overstige 24 timer per dag", exception.getMessage());
        }
    }

    /*
     * Sæt I:
     * 1 false, 2 false, 3 false, 4 false, 5 false, 6 true
     * Projekt findes ikke
     */
    @Test
    public void testSetI_ProjektFindesIkke() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.registrerTid("26099", "Analyse", 2.0);

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Projekt findes ikke", exception.getMessage());
        }
    }

        /*
     * Sæt J:
     * 1 false, 2 false, 3 false, 4 false, 5 false, 6 false
     * Projekt findes, aktivitet findes, og tid registreres.
     */
    @Test
    public void testSetJ_TidRegistreresKorrekt() throws Exception {
        logIndSomJfk();
        opretProjektMedAktivitet();

        try {
            planlaegningsvaerktoej.registrerTid("26001", "Analyse", 2.0);

        } catch (OperationNotAllowedException exception) {
            fail("Der skulle ikke kastes OperationNotAllowedException");
        }

        Projekt projekt = planlaegningsvaerktoej.findProjekt("26001");

        assertNotNull(projekt);

        Aktivitet aktivitet = projekt.findAktivitet("Analyse");

        assertNotNull(aktivitet);

        assertEquals(2.0, aktivitet.getRegistreretTidForMedarbejder("jfk"));
    }


    private void logIndSomJfk() throws Exception {

        planlaegningsvaerktoej.nyMedarbejder("jfk");
        planlaegningsvaerktoej.userLogin("jfk");
    }

    private void opretProjektMedAktivitet() throws Exception {

        planlaegningsvaerktoej.opretProjekt("Projekt A");
        planlaegningsvaerktoej.opretAktivitet("26001", "Analyse", 10.0, 1, 50);
    }
    
}
