package dtu.example.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Planlaegningsvaerktoej {
    
    private List<Projekt> projekter = new ArrayList<>();
    private List<Medarbejder> medarbejdere = new ArrayList<>();
    private PropertyChangeSupport observers = new PropertyChangeSupport(this);
    private String loggedInUser = "";
    private int hoejesteProjektnummer = 1;


    // ====================
    // User Methods
    // ====================

    public String getLoggedinUser() {
        return loggedInUser;
    }

    public void userLogin(String initialer) {
        this.loggedInUser = initialer;
    }

    public void userLogout() {
        this.loggedInUser = "";
    }

    public void nyMedarbejder(String navn, String initialer) throws OperationNotAllowedException {
        for (Medarbejder m: this.medarbejdere) {
            if (m.getInitialer().equals(initialer)) {
                throw new OperationNotAllowedException("Initialer er allerede i brug");
            }
        }
        
        medarbejdere.add(new Medarbejder(navn, initialer)); // !! SKAL DER LAVES TJEK FOR OM DER ER LOGGED IN FOR AT LAVE BRUGER? (KAN ANTAGES AT SYSTEMET f.eks. KUN ER TILGÆNGELIGT PÅ SPECIFIKKE PORT) !!
    }

    // ====================
    // Projekt Metoder
    // ====================

    /**
    * Logik til at oprette projekt og tildele automatisk nummer
    * 
    * @param projektNavn Navn på nyoprettede projekt
    * @param OperationNotAllowedException Indikere at systemets krav ikke opfyldes
    */
    public void opretProjekt(String projektNavn) throws OperationNotAllowedException {
        if (this.loggedInUser == null ||this.loggedInUser.isEmpty()) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        if (projektNavn == null || projektNavn.isEmpty()) {
            throw new OperationNotAllowedException("Projektnavnet må ikke være tomt");
        }

        String nytProjektnr = String.valueOf(26000 + this.hoejesteProjektnummer);
        Projekt nytProjekt = new Projekt(nytProjektnr, projektNavn);
        projekter.add(nytProjekt);
        this.hoejesteProjektnummer++;
        observers.firePropertyChange("PROJECT_OPRETTET", null, nytProjekt);
    }

    public boolean setProjekt(String projektNummer) {
        return false;
    }

    public boolean omdoebProjekt(String projektNummer, String nytNavn) throws OperationNotAllowedException {
        // Tjek om navnet allerede er i brug
        for (Projekt p: this.projekter) {
            if (p.getProjektNavn().equals(nytNavn)) {
                throw new OperationNotAllowedException("Projektnavn findes allerede");
            }
        }

        // Opdater navn og return true
        Projekt projekt = findProjekt(projektNummer);
        return projekt.opdaterNavn(nytNavn);
    }

    public boolean opdaterProjektMedProjektleder(String projektNummer, String medarbejderInitialer) {
        Projekt projekt = findProjekt(projektNummer);
        Medarbejder nyProjektleder = findMedarbejder(medarbejderInitialer);
        projekt.opdaterProjektleder(nyProjektleder);
        return true;
    }

    public boolean tilknytMedarbejderTilProjekt(String projektNummer, Medarbejder medarbejder) {
        return false;
    }

    public boolean fjernMedarbejderFraProjekt(String projektNummer, String initialer) {
        return false;
    }

    public boolean registrerTidPaaProjekt(String projektNummer, String aktivitetsNummer, String initialer, float antalArbejdstimer) {
        return false;
    }

    // =========================
    // Aktivitet Metoder
    // =========================

    public boolean opretAktivitet(String projektNummer, float forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) {
        return false;
    }

    public boolean sletAktivitet(String projektNummer, String aktivitetsNummer) {
        return false;
    }

    public boolean opdaterAktivitet(String projektNummer, String aktivitetsNummer, float forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) {
        return false;
    }

    // =====================
    // Observer Metoder
    // =====================
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        observers.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        observers.removePropertyChangeListener(listener);
    }

    // ========================
    // Status Metoder
    // ========================
    public float visMedarbejdersTimerFra(String initialer, LocalDate dato) {
        return 0.0f;
    }

    public float visProjektStatus(String projektNummer) {
        return 0.0f;
    }

    // =======================
    // Filindløsning
    // =======================
    public void indlaesFil() {
        // Logik til at indlæse HR fil
    }

    // =====================
    // Helpers
    // =====================
    public Projekt findProjekt(String projektInfo) {
        for (Projekt p: this.projekter) {
            if (p.getProjektNummer().equals(projektInfo) || p.getProjektNavn().equals(projektInfo)) {
                return p;
            }
        }
        return null;
    }

    public Medarbejder findMedarbejder(String medarbejderInfo) {
        for (Medarbejder m: this.medarbejdere) {
            if (m.getInitialer().equals(medarbejderInfo) || m.getNavn().equals(medarbejderInfo)) {
                return m;
            }
        }
        return null;
    }
}
