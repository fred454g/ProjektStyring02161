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

    public double getTotalRegistreretTid() {
        double total = 0;
        for (Tidsregistrering t : this.tidsregistreringer) {
            total += t.getAntalArbejdstimer();
        }
        return total;
    }
    public List<Tidsregistrering> getTidsregistreringerForPersistens() {
        return this.tidsregistreringer;
    }

    public void tilfoejGenskabtTid(double timer, String datoStreng, String initialer) {
        LocalDate dato = LocalDate.parse(datoStreng);
        Tidsregistrering registrering = new Tidsregistrering(dato, timer, initialer);
        this.tidsregistreringer.add(registrering);
    }

    public List<Medarbejder> getTilknyttedeMedarbejdere() {
        return this.tilknyttedeMedarbejdere;
    }

     // ==================

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

    public void registrerTid(Medarbejder medarbejder, Double timer) throws OperationNotAllowedException {
        // PRE-CONDITIONS (Design by Contract)
        // Tjekker at de logiske grundbetingelser for metoden er opfyldt
        assert medarbejder != null : "Pre-condition fejlede: Medarbejder må ikke være null";
        assert timer > 0 : "Pre-condition fejlede: Timer skal være større end 0";

        // Gemmer den gamle tilstand til brug i post-condition
        double gammelTotalTid = getTotalRegistreretTid();

        // FORRETNINGSLOGIK (niko)

        if (timer <= 0) {
            throw new OperationNotAllowedException("Tid skal være positiv"); // Kaster exception over for brugeren
        }

        // 1. Opretter ny tidsregistrering
        Tidsregistrering registrering = new Tidsregistrering(LocalDate.now(), timer, medarbejder.getInitialer());

        // 2. Tilføjer til liste af tidsregistreringer inden for aktivitet
        this.tidsregistreringer.add(registrering);

        // POST-CONDITIONS (Design by Contract)
        // Beviser at tilstanden faktisk ændrede sig korrekt
        assert getTotalRegistreretTid() == gammelTotalTid + timer : "Post-condition fejlede: Total tid blev ikke opdateret";
        assert this.tidsregistreringer.contains(registrering) : "Post-condition fejlede: Registreringen blev ikke gemt";
    }

    public double getRegistreretTidForMedarbejder(String initialer) {
        double total = 0;

        // løb liste igennem af registreringer og akkumeler baseret på match af initialer
        for (Tidsregistrering t : this.tidsregistreringer) {
            if (t.getInitialer().equals(initialer)) { // match!
                total += t.getAntalArbejdstimer();
            }
        }

        return total;
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
