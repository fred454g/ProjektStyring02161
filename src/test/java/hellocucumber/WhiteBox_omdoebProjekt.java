package hellocucumber;

import dtu.example.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WhiteBox_omdoebProjekt {

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
            planlaegningsvaerktoej.omdoebProjekt("26001", "Nyt navn");

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Ingen bruger logged in", exception.getMessage());
        }
    }

    /*
     * Sæt B:
     * 1 false, 2 true (2a true)
     * loggedInUser != null && projektNummer == null
     */
    @Test
    public void testSetB_ProjektNummerErNull() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.omdoebProjekt(null, "Nyt navn");

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Projekt skal vælges", exception.getMessage());
        }
    }

    /*
     * Sæt C:
     * 1 false, 2 true (2a false, 2b true)
     * loggedInUser != null && projektNummer != null && projektNummer.isBlank()
     */
    @Test
    public void testSetC_ProjektNummerErBlank() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.omdoebProjekt("   ", "Nyt navn");

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Projekt skal vælges", exception.getMessage());
        }
    }

    /*
     * Sæt D:
     * 1 false, 2 false, 3 true (3a true)
     * nytNavn == null
     */
    @Test
    public void testSetD_NytNavnErNull() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.omdoebProjekt("26001", null);

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Nyt projektnavn må ikke være tomt", exception.getMessage());
        }
    }

    /*
     * Sæt E:
     * 1 false, 2 false, 3 true (3a false, 3b true)
     * nytNavn != null && nytNavn.isBlank()
     */
    @Test
    public void testSetE_NytNavnErBlank() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.omdoebProjekt("26001", "   ");

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Nyt projektnavn må ikke være tomt", exception.getMessage());
        }
    }

    /*
     * Sæt F:
     * 1 false, 2 false, 3 false, 4(>1), 5 false, 5 true
     * Projektnavnet findes allerede.
     */
    @Test
    public void testSetF_ProjektnavnFindesAllerede() throws Exception {
        logIndSomJfk();

        planlaegningsvaerktoej.opretProjekt("Projekt A"); // forventet projektnr. 26001
        planlaegningsvaerktoej.opretProjekt("Projekt B"); // forventet projektnr. 26002

        try {
            planlaegningsvaerktoej.omdoebProjekt("26001", "Projekt B");

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Projektnavn findes allerede", exception.getMessage());
        }
    }

    /*
     * Sæt G:
     * 1 false, 2 false, 3 false, 4(0), 6 true
     * Ingen projekter findes, og findProjekt(projektNummer) == null.
     */
    @Test
    public void testSetG_ProjektFindesIkke() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.omdoebProjekt("26099", "Nyt navn");

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Projekt findes ikke", exception.getMessage());
        }
    }

    /*
     * Sæt H:
     * 1 false, 2 false, 3 false, 4(1), 5 false, 6 false, 7 true
     * Ét projekt findes, nyt navn er unikt, og projektet omdøbes korrekt.
     */
    @Test
    public void testSetH_EtProjektOmdobesKorrekt() throws Exception {
        logIndSomJfk();

        planlaegningsvaerktoej.opretProjekt("Projekt A");

        boolean resultat = false;

        try {
            resultat = planlaegningsvaerktoej.omdoebProjekt("26001", "Projekt A omdøbt");

        } catch (OperationNotAllowedException exception) {
            fail("Der skulle ikke kastes OperationNotAllowedException");
        }

        assertTrue(resultat);
    }

    /*
     * Sæt I:
     * 1 false, 2 false, 3 false, 4(>1), 5 false for alle projekter, 6 false, 7 true
     * Flere projekter findes, nyt navn er unikt, og projektet omdøbes korrekt.
     */
    @Test
    public void testSetI_FlereProjekterOgUniktNytNavn() throws Exception {
        logIndSomJfk();

        planlaegningsvaerktoej.opretProjekt("Projekt A"); // forventet projektnr. 26001
        planlaegningsvaerktoej.opretProjekt("Projekt B"); // forventet projektnr. 26002

        boolean resultat = false;

        try {
            resultat = planlaegningsvaerktoej.omdoebProjekt("26002", "Projekt B omdøbt");

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