package dtu.example.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.io.IOException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Planlaegningsvaerktoej {
    
    private List<Projekt> projekter = new ArrayList<>();
    private List<Medarbejder> medarbejdere = new ArrayList<>();
    private PropertyChangeSupport observers = new PropertyChangeSupport(this);
    private Medarbejder loggedInUser = null;
    private int hoejesteProjektnummer = 1;
    private int hoejesteAktivitetsnummer = 1;
    private long sidsteHrListeOpdatering = -1L;


    // ====================
    // User Methods
    // ====================

    public String getLoggedinUserInitials() {
        return this.loggedInUser.getInitialer();
    }

    public void userLogin(String initialer) throws OperationNotAllowedException {
        if (findMedarbejder(initialer) == null) {
            throw new OperationNotAllowedException("Medarbejder med initialer " + initialer + " findes ikke i systemet.");
        }
        this.loggedInUser = findMedarbejder(initialer);
    }

    public void userLogout() {
        this.loggedInUser = null;
    }

    public void nyMedarbejder(String navn, String initialer) throws OperationNotAllowedException {
        for (Medarbejder m: this.medarbejdere) {
            if (m.getInitialer().equals(initialer)) {
                throw new OperationNotAllowedException("Initialer er allerede i brug");
            }
        }

        Medarbejder nyMedarbejder = new Medarbejder(navn, initialer);
        medarbejdere.add(nyMedarbejder); // !! SKAL DER LAVES TJEK FOR OM DER ER LOGGED IN FOR AT LAVE BRUGER? (KAN ANTAGES AT SYSTEMET f.eks. KUN ER TILGÆNGELIGT PÅ SPECIFIKKE PORT) !!
        observers.firePropertyChange("MEDARBEJDER_OPRETTET", null, nyMedarbejder);
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
        if (this.loggedInUser == null) {
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

    public boolean setProjekt(String projektNummer) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        return false;
    }

    public boolean omdoebProjekt(String projektNummer, String nytNavn) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        
        // Tjek om navnet allerede er i brug
        for (Projekt p: this.projekter) {
            if (p.getProjektNavn().equals(nytNavn)) {
                throw new OperationNotAllowedException("Projektnavn findes allerede");
            }
        }

        // Opdater navn og return true
        Projekt projekt = findProjekt(projektNummer);
        String gammeltNavn = projekt.getProjektNavn();
        boolean opdateret = projekt.opdaterNavn(nytNavn);
        if (opdateret) {
            observers.firePropertyChange("PROJEKT_OMDOEBT", gammeltNavn, projekt);
        }
        return opdateret;
    }

    public boolean opdaterProjektMedProjektleder(String projektNummer, String medarbejderInitialer) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        
        Projekt projekt = findProjekt(projektNummer);
        Medarbejder tidligereProjektleder = projekt.getProjektleder();
        Medarbejder nyProjektleder = findMedarbejder(medarbejderInitialer);
        projekt.opdaterProjektleder(nyProjektleder);
        observers.firePropertyChange("PROJEKTLEDER_OPDATERET", tidligereProjektleder, projekt);
        return true;
    }

    public boolean tilknytMedarbejderTilProjekt(String projektNummer, String medarbejderInfo) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        
        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }
        Medarbejder medarbejder = findMedarbejder(medarbejderInfo);
        if (medarbejder == null) {
            throw new OperationNotAllowedException("Medarbejder med initialer " + medarbejderInfo + " findes ikke i systemet");
        }
        projekt.tilknytMedarbejder(medarbejder);
        observers.firePropertyChange("MEDARBEJDER_TILKNYTTET_PROJEKT", null, projekt);
        return true;
    }

    public boolean fjernMedarbejderFraProjekt(String projektNummer, String initialer) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        return false;
    }

    public boolean registrerTidPaaProjekt(String projektNummer, String aktivitetsNummer, String initialer, float antalArbejdstimer) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        return false;
    }

    // =========================
    // Aktivitet Metoder
    // =========================

    public boolean opretAktivitet(String projektNummer, String aktivitetsNavn, double forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        
        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }
        String nytAktivitetsnr = String.valueOf(26000 + this.hoejesteProjektnummer);
        projekt.opretAktivitet(nytAktivitetsnr, aktivitetsNavn, forventedeAntalArbejdstimer, starttidspunkt, sluttidspunkt);
        this.hoejesteAktivitetsnummer++;
        observers.firePropertyChange("AKTIVITET_OPRETTET", null, projekt.findAktivitet(nytAktivitetsnr));
        return true;
    }

    public boolean opretAktivitet(String projektNummer, String aktivitetsNavn) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        
        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }
        String nytAktivitetsnr = String.valueOf(26000 + this.hoejesteProjektnummer);
        projekt.opretAktivitet(nytAktivitetsnr, aktivitetsNavn);
        this.hoejesteAktivitetsnummer++;
        observers.firePropertyChange("AKTIVITET_OPRETTET", null, projekt.findAktivitet(nytAktivitetsnr));
        return true;
    }

    public boolean sletAktivitet(String projektNummer, String aktivitetsNummer) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        return false;
    }

    public boolean opdaterAktivitet(String projektNummer, String aktivitetsNummer, double forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        Aktivitet aktivitetFoerOpdatering = projekt.findAktivitet(aktivitetsNummer);
        projekt.opdaterAktivitet(aktivitetsNummer, forventedeAntalArbejdstimer, starttidspunkt, sluttidspunkt);
        observers.firePropertyChange("AKTIVITET_OPDATERET", null, aktivitetFoerOpdatering);
        return true;
    }

    public boolean tilknytMedarbejderTilAktivitet(String projektNummer, String aktivitetsNavn, String medarbejderInfo) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        
        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        Medarbejder medarbejder = findMedarbejder(medarbejderInfo);
        if (medarbejder == null) {
            throw new OperationNotAllowedException("Medarbejder med initialer " + medarbejderInfo + " findes ikke i systemet");
        }

        projekt.tilknytMedarbejderTilAktivitet(aktivitetsNavn, medarbejder);
        observers.firePropertyChange("MEDARBEJDER_TILKNYTTET_AKTIVITET", null, projekt.findAktivitet(aktivitetsNavn));
        return true;
    }

    public boolean fjernMedarbejderFraAktivitet(String projektNummer, String aktivitetsNavn, String medarbejderInfo) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        
        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        Medarbejder medarbejder = findMedarbejder(medarbejderInfo);
        if (medarbejder == null) {
            throw new OperationNotAllowedException("Medarbejder med initialer " + medarbejderInfo + " findes ikke i systemet");
        }

        projekt.fjernMedarbejderFraAktivitet(aktivitetsNavn, medarbejder);
        observers.firePropertyChange("MEDARBEJDER_FJERNET_AKTIVITET", medarbejder, projekt.findAktivitet(aktivitetsNavn));
        return true;
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
    public float visMedarbejdersTimerFra(String initialer, LocalDate dato) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        return 0.0f;
    }

    public float visProjektStatus(String projektNummer) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        return 0.0f;
    }

    // =======================
    // Filindløsning
    // =======================
    public void indlaesFil() {
        Path hrListePath = Paths.get("src", "main", "java", "dtu", "example", "hr_liste.txt");

        try {
            FileTime fileTime = Files.getLastModifiedTime(hrListePath);
            long nuvaerendeOpdatering = fileTime.toMillis();

            if (nuvaerendeOpdatering <= this.sidsteHrListeOpdatering) {
                return;
            }

            int antalMedarbejdereFoer = this.medarbejdere.size();
            List<String> linjer = Files.readAllLines(hrListePath);
            List<Medarbejder> opdateredeMedarbejdere = new ArrayList<>();

            for (String linje : linjer) {
                if (linje == null || linje.trim().isEmpty()) {
                    continue;
                }

                String[] dele = linje.split(",", 2);
                if (dele.length < 2) {
                    continue;
                }

                String initialer = dele[0].trim();
                String navn = dele[1].trim();

                boolean findesAllerede = false;
                for (Medarbejder medarbejder : opdateredeMedarbejdere) {
                    if (medarbejder.getInitialer().equals(initialer)) {
                        findesAllerede = true;
                        break;
                    }
                }

                if (!findesAllerede) {
                    opdateredeMedarbejdere.add(new Medarbejder(navn, initialer));
                }
            }

            this.medarbejdere = opdateredeMedarbejdere;
            if (this.loggedInUser != null) {
                this.loggedInUser = findMedarbejder(this.loggedInUser.getInitialer());
            }
            this.sidsteHrListeOpdatering = nuvaerendeOpdatering;
            observers.firePropertyChange("HR_LISTE_SYNKRONISERET", antalMedarbejdereFoer, this.medarbejdere.size());
        } catch (IOException e) {
            throw new RuntimeException("Kunne ikke indlæse HR-liste", e);
        }
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
