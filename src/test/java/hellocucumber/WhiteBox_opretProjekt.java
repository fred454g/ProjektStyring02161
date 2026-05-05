package hellocucumber;

import dtu.example.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WhiteBox_opretProjekt {

    Planlaegningsvaerktoej planlaegningsvaerktoej;


    @BeforeEach
	public void setUp() throws Exception {

        planlaegningsvaerktoej = new Planlaegningsvaerktoej();
        

	}

    
    @Test
    public void testSetA_IngenBrugerLoggedIn() throws Exception {

        try {
            planlaegningsvaerktoej.opretProjekt("Nyt IT System");

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Ingen bruger logged in", exception.getMessage());
        }   
    }

    @Test
    public void testSetB_IntetProjektnavn() throws Exception {

        logIndSomJfk();

        try {
            planlaegningsvaerktoej.opretProjekt(null);

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Projektnavnet må ikke være tomt", exception.getMessage());
        }
    }

    @Test
    public void testSetC_ProjektNavnErTomString() throws Exception {
        
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.opretProjekt("");

            fail("Forventede at OperationNotAllowedException blev kastet");

        } catch (OperationNotAllowedException exception) {
            assertEquals("Projektnavnet må ikke være tomt", exception.getMessage());
        }
    }

    @Test
    public void testSetD_ProjektOprettesKorrekt() throws Exception {
        logIndSomJfk();

        try {
            planlaegningsvaerktoej.opretProjekt("Nyt IT System");

        } catch (OperationNotAllowedException exception) {
            fail("Der skulle ikke kastes en OperationNotAllowedException ved gyldigt projektnavn");
        }

        assertNotNull(planlaegningsvaerktoej.findProjekt("Nyt IT System"));
          
    }


    private void logIndSomJfk() throws Exception {

        planlaegningsvaerktoej.nyMedarbejder("jfk");
        planlaegningsvaerktoej.userLogin("jfk");
    }


}