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

    public List<Aktivitet> getAktiviteter() {
        return this.aktiviteter;
    }

    // ====================
    // Projekt Metoder
    // ====================
    public boolean opdaterNavn(String nytNavn) {
        this.projektNavn = nytNavn;
        return true;
    }

    public boolean opdaterProjektleder(Medarbejder nyProjektleder) {
        this.projektleder = nyProjektleder;
        return true;
    }

    public void fjernMedarbejder(String initialer) {

    }

    public void tilknytMedarbejder(Medarbejder medarbejder) throws OperationNotAllowedException {
        if (!isMedarbejderInProjekt(medarbejder)) {
            this.tilknyttedeMedarbejdere.add(medarbejder);
        } else {
            throw new OperationNotAllowedException("Medarbejder er allerede tilknyttet projekt");
        }
    }

    public void overbliksRapport(int starttidspunkt, int sluttidspunkt) {

    }

    public void givProjektStatus() {

    }

    // ===================
    // Aktivitet metoder
    // ===================

    public boolean opretAktivitet(String aktivitetsNr, String aktivitetsNavn, double forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) throws OperationNotAllowedException {
        if (findAktivitet(aktivitetsNavn) != null) {
            throw new OperationNotAllowedException("Aktivitetsnavn er i brug");
        }
        Aktivitet nyAktivitet = new Aktivitet(aktivitetsNr, aktivitetsNavn, forventedeAntalArbejdstimer, starttidspunkt, sluttidspunkt);
        this.aktiviteter.add(nyAktivitet);
        return true;
    }

    public boolean opretAktivitet(String aktivitetsNr, String aktivitetsNavn) throws OperationNotAllowedException {
        if (findAktivitet(aktivitetsNavn) != null) {
            throw new OperationNotAllowedException("Aktivitetsnavn er i brug");
        }
        Aktivitet nyAktivitet = new Aktivitet(aktivitetsNr, aktivitetsNavn);
        this.aktiviteter.add(nyAktivitet);
        return true;
    }

    public boolean opdaterForventedeAntalArbejdstimer(float timer, int starttidspunkt, int sluttidspunkt) {
        return false;
    }

    public void sletAktivitet(String aktivitetsNummer) {

    }

    public void opdaterAktivitet(String aktivitetsInfo, double forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) throws OperationNotAllowedException {
        Aktivitet aktivitet = findAktivitet(aktivitetsInfo);
        if (aktivitet == null) {
            throw new OperationNotAllowedException("Aktivitet findes ikke");
        }

        aktivitet.setForventedeAntalArbejdstimer(forventedeAntalArbejdstimer);
        aktivitet.setStarttidspunkt(starttidspunkt);
        aktivitet.setSluttidspunkt(sluttidspunkt);
    }

    public void tilknytMedarbejderTilAktivitet(String aktivitetsNavn, Medarbejder medarbejder) throws OperationNotAllowedException {
        if (!isMedarbejderInProjekt(medarbejder)) {
            throw new OperationNotAllowedException("Medarbejder ikke tilknyttet projekt");
        }

        Aktivitet aktivitet = findAktivitet(aktivitetsNavn);
        if (aktivitet == null) {
            throw new OperationNotAllowedException("Aktivitet findes ikke i projekt");
        }

        aktivitet.tilknytMedarbejder(medarbejder);
    }

    public void fjernMedarbejderFraAktivitet(String aktivitetsNavn, Medarbejder medarbejder) throws OperationNotAllowedException {
        if (!isMedarbejderInProjekt(medarbejder)) {
            throw new OperationNotAllowedException("Medarbejder ikke tilknyttet projekt");
        }

        Aktivitet aktivitet = findAktivitet(aktivitetsNavn);
        if (aktivitet == null) {
            throw new OperationNotAllowedException("Aktivitet findes ikke i projekt");
        }

        aktivitet.fjernMedarbejder(medarbejder);
    }

    public void registrerTid(String aktivitetsNavn, Medarbejder medarbejder, Double timer) throws OperationNotAllowedException {
        // 1. find gældende aktivitet
        Aktivitet aktivitet = findAktivitet(aktivitetsNavn);

        // Har vi fundet et match?
        if (aktivitet == null) {
            throw new OperationNotAllowedException("Aktivitet findes ikke i projekt");
        }
        aktivitet.registrerTid(medarbejder, timer);
    }

    public double getRegistreretTidForMedarbejder(String initialer) {
        double total = 0;
        
        for (Aktivitet a : this.aktiviteter) {
            total += a.getRegistreretTidForMedarbejder(initialer);
        }

        return total;
    }

    // =====================
    // Helpers
    // =====================
    public Boolean isMedarbejderInProjekt(Medarbejder medarbejder) {
        for (Medarbejder m: this.tilknyttedeMedarbejdere) {
            if (m.equals(medarbejder)) {
                return true;
            }
        }
        return false;
    }

    public Aktivitet findAktivitet(String aktivitetsInfo) {
        for (Aktivitet a: this.aktiviteter) {
            if (a.getAktivitetsnummer().equals(aktivitetsInfo) || a.getAktivitetsNavn().equals(aktivitetsInfo)) {
                return a;
            }
        }
        return null;
    }

    
}
