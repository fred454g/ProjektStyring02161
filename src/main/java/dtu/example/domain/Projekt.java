package dtu.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Projekt {
    
    private String projektnummer;
    private String projektNavn;
    private Medarbejder projektleder;
    private List<Medarbejder> tilknyttedeMedarbejdere = new ArrayList<>();
    private List<Aktivitet> aktiviteter = new ArrayList<>();

    public Projekt(String projektnummer, String projektNavn) {
        this.projektnummer = projektnummer;
        this.projektNavn = projektNavn;
    }

    // ===================
    // Get metoder
    // ===================
    public String getProjektNummer() {
        return this.projektnummer;
    }

    public String getProjektNavn() {
        return this.projektNavn;
    }

    public Medarbejder getProjektleder() {
        return this.projektleder;
    }

    // ====================

    public boolean opdaterNavn(String nytNavn) {
        this.projektNavn = nytNavn;
        return true;
    }

    public boolean opdaterProjektleder(Medarbejder nyProjektleder) {
        this.projektleder = nyProjektleder;
        return true;
    }

    public boolean opdaterForventedeAntalArbejdstimer(float timer, int starttidspunkt, int sluttidspunkt) {
        return false;
    }

    public void sletAktivitet(String aktivitetsNummer) {

    }

    public void opdaterAktivitet(String aktivitetsNummer, float forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) {

    }

    public void tilknytMedarbejder(String initialer) {

    }

    public void fjernMedarbejder(String initialer) {

    }

    public boolean registrerTid(String aktivitetsNummer, String initialer, float antalArbejdstimer) {
        return false;
    }

    public float visMedarbejdersTimer(String initialer) {
        return 0.0f;
    }

    public void overbliksRapport(int starttidspunkt, int sluttidspunkt) {

    }

    public void givProjektStatus() {

    }
}
