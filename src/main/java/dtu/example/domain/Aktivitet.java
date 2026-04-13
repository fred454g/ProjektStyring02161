package dtu.example.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Aktivitet {
    
    private String aktivitetsnummer;
    private String aktivitetsNavn;
    private double forventedeAntalArbejdstimer;
    private int starttidspunkt;
    private int sluttidspunkt;
    private List<Medarbejder> tilknyttedeMedarbejdere = new ArrayList<>();
    private List<Tidsregistrering> tidsregistreringer = new ArrayList<>();
    
    public Aktivitet(String aktivitetsnummer, String aktivitetsNavn, double forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) {
        this.aktivitetsnummer = aktivitetsnummer;
        this.aktivitetsNavn = aktivitetsNavn;
        this.forventedeAntalArbejdstimer = forventedeAntalArbejdstimer;
        this.starttidspunkt = starttidspunkt;
        this.sluttidspunkt = sluttidspunkt;
    }

    public Aktivitet(String aktivitetsnummer, String aktivitetsNavn) {
        this.aktivitetsnummer = aktivitetsnummer;
        this.aktivitetsNavn = aktivitetsNavn;
    }

    // ==================
    // GET methods
    // ==================
    public String getAktivitetsnummer() {
        return this.aktivitetsnummer;
    }

    public String getAktivitetsNavn() {
        return this.aktivitetsNavn;
    }

    public double getForventedeAntalArbejdsTimer() {
        return this.forventedeAntalArbejdstimer;
    }

    public int getStartstidspunkt() {
        return this.starttidspunkt;
    }

    public int getSluttidspunkt() {
        return this.sluttidspunkt;
    }

    // =======================

    public boolean setStarttidspunkt(int startstidspunkt) {
        this.starttidspunkt = startstidspunkt;
        return true;
    }

    public boolean setSluttidspunkt(int sluttidspunkt) {
        this.sluttidspunkt = sluttidspunkt;
        return true;
    }

    public boolean setForventedeAntalArbejdstimer(double forventedeAntalArbejdstimer) {
        this.forventedeAntalArbejdstimer = forventedeAntalArbejdstimer;
        return true;
    }

    public void tilknytMedarbejder(Medarbejder medarbejder) throws OperationNotAllowedException {
        if (!isMedarbejderInAktivitet(medarbejder)) {
            this.tilknyttedeMedarbejdere.add(medarbejder);
        } else {
            throw new OperationNotAllowedException("Medarbejder er allerede tilknyttet aktivitet");
        }
    }

    public void fjernMedarbejder(Medarbejder medarbejder) throws OperationNotAllowedException {
        if (!isMedarbejderInAktivitet(medarbejder)) {
            throw new OperationNotAllowedException("Medarbejder er ikke i aktivitet");
        }

        this.tilknyttedeMedarbejdere.remove(medarbejder);
    }

    public void registrerTid(String initialer, LocalDate dato, double antalArbejdstimer) {

    }

    public float visMedarbejdersTimer(String initialer) {
        return 0.0f;
    }

    public void overbliksRapport(LocalDate starttidspunkt, LocalDate sluttidspunkt) {

    }

    public void givAktivitetsStatus() {

    }

    // =====================
    // Helpers
    // =====================
    public Boolean isMedarbejderInAktivitet(Medarbejder medarbejder) {
        for (Medarbejder m: this.tilknyttedeMedarbejdere) {
            if (m.equals(medarbejder)) {
                return true;
            }
        }
        return false;
    }
}
