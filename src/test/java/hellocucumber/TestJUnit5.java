package hellocucumber;

import dtu.example.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestJUnit5 {

	private Aktivitet aktivitet;
	private Medarbejder medarbejder;

	@BeforeEach
	public void setUp() throws Exception {
		// Klargør data inden hver test
		aktivitet = new Aktivitet("A1", "Frontend", 50.0, 10, 12);
		medarbejder = new Medarbejder("Hubert Baumeister", "huba");
		aktivitet.tilknytMedarbejder(medarbejder); // Sørg for at huba er på aktiviteten
	}

	@Test
	public void testRegistrerTid_Succes_PositivTid() throws Exception {
		// A (Arrange - gjort i setUp)

		// A (Act)
		aktivitet.registrerTid(medarbejder, 4.5);

		// A (Assert)
		assertEquals(4.5, aktivitet.getTotalRegistreretTid(), 0.01);
		assertEquals(4.5, aktivitet.getRegistreretTidForMedarbejder("huba"), 0.01);
	}

	@Test
	public void testRegistrerTid_Fejl_NegativTid() {
		// Vi forventer en AssertionError, fordi vores Pre-condition
		// fanger det ulovlige input, før forretningslogic
		AssertionError error = assertThrows(AssertionError.class, () -> {
			aktivitet.registrerTid(medarbejder, -2.0);
		});

		assertTrue(error.getMessage().contains("Timer skal være større end 0"));

		// Sikr at den totale tid stadig er 0 (tilstanden forblev intakt)
		assertEquals(0.0, aktivitet.getTotalRegistreretTid(), 0.01);
	}

	@Test
	public void testRegistrerTid_Fejl_IkkeTilknyttetAktivitet() {
		// A (Arrange)
		Medarbejder wilo = new Medarbejder("William Lopez", "wilo");
		// BEMÆRK: Vi tilknytter IKKE wilo til aktiviteten

		// A (Act & Assert)
		Exception exception = assertThrows(OperationNotAllowedException.class, () -> {
			aktivitet.registrerTid(wilo, 5.0);
		});

		assertEquals("Medarbejder er ikke tilknyttet aktiviteten", exception.getMessage());
	}
}