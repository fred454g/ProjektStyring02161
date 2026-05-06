package dtu.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Projekt {
    
    private String projektnummer;
    private String projektNavn;
    private Medarbejder projektleder;
    private List<Medarbejder> tilknyttedeMedarbejdere = new ArrayList<>();
    private List<Aktivitet> aktiviteter = new ArrayList<>();
    private int hoejesteAktivitetsnummer = 1;

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

    public List<Medarbejder> getTilknyttedeMedarbejdere() {
        return this.tilknyttedeMedarbejdere;
    }

    public int getHoejesteAktivitetsnummer() {
        return this.hoejesteAktivitetsnummer;
    }


    // ====================
    // Aktivitetsnummer opdater
    // ====================
    public void hoejesteAktivitetsnummerPlusEn() {
        this.hoejesteAktivitetsnummer++;
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

    public void fjernMedarbejderFraProjekt(Medarbejder medarbejder) throws OperationNotAllowedException {
        
        if (isMedarbejderInProjekt(medarbejder)) {
            this.tilknyttedeMedarbejdere.remove(medarbejder);
        } else {

            throw new OperationNotAllowedException("Medarbejder er ikke tilknyttet projekt");
        }
    }

    public void tilknytMedarbejder(Medarbejder medarbejder) throws OperationNotAllowedException {
        // == DbC: PRE-CONDITIONS ==
        assert medarbejder != null : "Pre-condition: Systemet forsøgte at tilføje null som medarbejder til projektet";
        int antalFoer = this.tilknyttedeMedarbejdere.size();
        
        if (!isMedarbejderInProjekt(medarbejder)) {
            this.tilknyttedeMedarbejdere.add(medarbejder);

            // == DbC: POST-CONDITION ==
            assert this.tilknyttedeMedarbejdere.size() == antalFoer + 1 : "Post-condition: Listen med tilknyttede medarbejdere voksede ikke";
            assert isMedarbejderInProjekt(medarbejder) : "Post-condition: Medarbejder blev ikke korrekt gemt i projektet";
        } else {

            throw new OperationNotAllowedException("Medarbejder er allerede tilknyttet projekt");
        }
    }


    // ===================
    // Aktivitet metoder
    // ===================

    public boolean opretAktivitet(String aktivitetsNr, String aktivitetsNavn, double forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) throws OperationNotAllowedException {
        // DbC - PRE-CONDITIONS
        assert aktivitetsNr != null && !aktivitetsNr.isBlank() : "Pre-condition: Mangler internt aktivitetsNr";
        assert starttidspunkt <= sluttidspunkt : "Pre-condition: Facaden tillod en startuge efter slutuge";
        int forventetAntalAktiviteter = this.aktiviteter.size() + 1;

        if (findAktivitet(aktivitetsNavn) != null) {
            throw new OperationNotAllowedException("Aktivitetsnavn er i brug");
        }
        
        Aktivitet nyAktivitet = new Aktivitet(aktivitetsNr, aktivitetsNavn, forventedeAntalArbejdstimer, starttidspunkt, sluttidspunkt);
        
        this.aktiviteter.add(nyAktivitet);

        // DbC - POST-CONDITIONS
        assert this.aktiviteter.size() == forventetAntalAktiviteter : "Post-condition: Aktivitet ikke tilføjet til listen";
        assert findAktivitet(aktivitetsNr) != null : "Post-condition: Fandt ikke oprettet aktivitet";

        return true;
    }

    public boolean opdaterForventedeAntalArbejdstimer(String aktivitetsInfo, float timer, int starttidspunkt, int sluttidspunkt) throws OperationNotAllowedException {
        Aktivitet aktivitet = findAktivitet(aktivitetsInfo);
        if (aktivitet == null) {
            throw new OperationNotAllowedException("Aktivitet findes ikke");
        }

        aktivitet.setForventedeAntalArbejdstimer(timer);
        aktivitet.setStarttidspunkt(starttidspunkt);
        aktivitet.setSluttidspunkt(sluttidspunkt);

        return true;
    }

    public void sletAktivitet(Aktivitet aktivitet) throws OperationNotAllowedException {

        this.aktiviteter.remove(aktivitet);
    }

    public void opdaterAktivitet(String aktivitetsInfo, double forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) throws OperationNotAllowedException {
        
        Aktivitet aktivitet = findAktivitet(aktivitetsInfo);

        aktivitet.setForventedeAntalArbejdstimer(forventedeAntalArbejdstimer);
        aktivitet.setStarttidspunkt(starttidspunkt);
        aktivitet.setSluttidspunkt(sluttidspunkt);
    }

    public void tilfoejMedarbejderTilAktivitet(String aktivitetsNavn, Medarbejder medarbejder) throws OperationNotAllowedException {
        // Jacob

        tjek_MedarbejderenErTilfoejetTilProjektet(medarbejder);

        Aktivitet aktivitet = tjek_AktivotetenFindesIProjektet(aktivitetsNavn);

        aktivitet.tilfoejMedarbejder(medarbejder);
    }

    public void fjernMedarbejderFraAktivitet(String aktivitetsNavn, Medarbejder medarbejder) throws OperationNotAllowedException {
        // Jacob

        tjek_MedarbejderenErTilfoejetTilProjektet(medarbejder);

        Aktivitet aktivitet = tjek_AktivotetenFindesIProjektet(aktivitetsNavn);

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

    // =====================
    // Tjek hjaepler metoder
    // =====================
    
    private void tjek_MedarbejderenErTilfoejetTilProjektet(Medarbejder medarbejder) throws OperationNotAllowedException {
        // Jacob

        if (!isMedarbejderInProjekt(medarbejder)) {
            throw new OperationNotAllowedException("Medarbejder ikke tilknyttet projekt");
        }
    }

    private Aktivitet tjek_AktivotetenFindesIProjektet(String aktivitetsNavn) throws OperationNotAllowedException {
        // Jacob

        Aktivitet aktivitet = findAktivitet(aktivitetsNavn);
        
        if (aktivitet == null) {
            throw new OperationNotAllowedException("Aktivitet findes ikke i projekt");
        }

        return aktivitet;
    } 

}
