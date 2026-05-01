package hellocucumber;

import dtu.example.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WhiteBox_tilknytMedarbejderTilProjekt {

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
            planlaegningsvaerktoej.tilknytMedarbejderTilProjekt("26001", "jfk");

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Ingen bruger logged in", exception.getMessage());
        }
    }

    /*
     * Sæt B:
     * 1 false, 2 true
     * loggedInUser != null && findProjekt(projektNummer) == null
     */
    @Test
    public void testSetB_ProjektFindesIkke() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.tilknytMedarbejderTilProjekt("26099", "jfk");

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Projekt findes ikke", exception.getMessage());
        }
    }

    /*
     * Sæt C:
     * 1 false, 2 false, 3 true
     * Projekt findes, men medarbejder findes ikke
     */
    @Test
    public void testSetC_MedarbejderFindesIkke() throws Exception {
        logIndSomJfk();

        planlaegningsvaerktoej.opretProjekt("Projekt A");

        try {
            planlaegningsvaerktoej.tilknytMedarbejderTilProjekt("26001", "xxx");

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals(
                    "Medarbejder med initialer xxx findes ikke i systemet",
                    exception.getMessage()
            );
        }
    }

    /*
     * Sæt D:
     * 1 false, 2 false, 3 false
     * Projekt findes, medarbejder findes, og medarbejderen tilknyttes projektet
     */
    @Test
    public void testSetD_MedarbejderTilknyttesProjekt() throws Exception {
        logIndSomJfk();

        planlaegningsvaerktoej.opretProjekt("Projekt A");

        boolean resultat = false;

        try {
            resultat = planlaegningsvaerktoej.tilknytMedarbejderTilProjekt("26001", "jfk");

        } catch (OperationNotAllowedException exception) {
            fail("Der skulle ikke kastes OperationNotAllowedException");
        }

        assertTrue(resultat);
    }

    private void logIndSomJfk() throws Exception {

        planlaegningsvaerktoej.nyMedarbejder("jfk");
        planlaegningsvaerktoej.userLogin("jfk");
    }
}