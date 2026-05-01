package dtu.example.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Paths;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import dtu.example.persistence.ProjektRepository;
import dtu.example.persistence.MedarbejderRepository;
import dtu.example.persistence.FravaerRepository;

public class Planlaegningsvaerktoej {
    
    private List<Projekt> projekter = new ArrayList<>();
    private List<Medarbejder> medarbejdere = new ArrayList<>();
    private PropertyChangeSupport observers = new PropertyChangeSupport(this);
    private Medarbejder loggedInUser = null;
    private int hoejesteProjektnummer = 1;
    private ProjektRepository projektRepository =
            new ProjektRepository(Paths.get("src", "main", "java", "dtu", "example", "projekter.txt"));
    private MedarbejderRepository medarbejderRepository =
            new MedarbejderRepository(Paths.get("src", "main", "java", "dtu", "example", "hr_liste.txt"));
    private FravaerRepository fravaerRepository =
            new FravaerRepository(Paths.get("src", "main", "java", "dtu", "example", "fravaer.txt"));


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

    public void nyMedarbejder(String initialer) throws OperationNotAllowedException {
        for (Medarbejder m: this.medarbejdere) {
            if (m.getInitialer().equals(initialer)) {
                throw new OperationNotAllowedException("Initialer er allerede i brug");
            }
        }

        Medarbejder nyMedarbejder = new Medarbejder(initialer);
        medarbejdere.add(nyMedarbejder); // !! SKAL DER LAVES TJEK FOR OM DER ER LOGGED IN FOR AT LAVE BRUGER? (KAN ANTAGES AT SYSTEMET f.eks. KUN ER TILGÆNGELIGT PÅ SPECIFIKKE PORT) !!
        observers.firePropertyChange("MEDARBEJDER_OPRETTET", null, nyMedarbejder);
    }

    public List<Medarbejder> getMedarbejdere() {
        return this.medarbejdere;
    }
    // ====================
    // Projekt Metoder
    // ====================
    public void gemProjekter() {
        projektRepository.gemProjekter(this.projekter);
    }
    /**
    * Logik til at oprette projekt og tildele automatisk nummer
    * 
    * @param projektNavn Navn på nyoprettede projekt
    * @param \OperationNotAllowedException Indikere at systemets krav ikke opfyldes
    */
    public void opretProjekt(String projektNavn) throws OperationNotAllowedException {
        // Der bliver udført struktureret white-box test på denne metode
        
        if (this.loggedInUser == null) { // 1
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        if (projektNavn == null || projektNavn.isEmpty()) { // 2 (2a || 2b)
            throw new OperationNotAllowedException("Projektnavnet må ikke være tomt");
        }

        String nytProjektnr = String.valueOf(26000 + this.hoejesteProjektnummer);
        Projekt nytProjekt = new Projekt(nytProjektnr, projektNavn);
        projekter.add(nytProjekt);
        this.hoejesteProjektnummer++;
        observers.firePropertyChange("PROJECT_OPRETTET", null, nytProjekt);
        gemProjekter();
    }


    public boolean omdoebProjekt(String projektNummer, String nytNavn) throws OperationNotAllowedException {
        // Der bliver udført struktureret white-box test på denne metode

        if (this.loggedInUser == null) { // 1
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        if (projektNummer == null || projektNummer.isBlank()) { // 2 (2a || 2b)
            throw new OperationNotAllowedException("Projekt skal vælges");
        }

        if (nytNavn == null || nytNavn.isBlank()) { // 3 (3a || 3b)
            throw new OperationNotAllowedException("Nyt projektnavn må ikke være tomt");
        }

        // Tjek om navnet allerede er i brug
        for (Projekt p: this.projekter) { // 4

            if (p.getProjektNavn().equals(nytNavn)) { // 5
                throw new OperationNotAllowedException("Projektnavn findes allerede");
            }
        }

        // Opdater navn og return true
        Projekt projekt = findProjekt(projektNummer);

        if (projekt == null) { // 6
            throw new OperationNotAllowedException("Projekt findes ikke");
        
        }

        String gammeltNavn = projekt.getProjektNavn();
        boolean opdateret = projekt.opdaterNavn(nytNavn);
        
        if (opdateret) {
            observers.firePropertyChange("PROJEKT_OMDOEBT", gammeltNavn, projekt);
        }

        gemProjekter();
        return opdateret; // 7
    }

    public boolean opdaterProjektMedProjektleder(String projektNummer, String medarbejderInitialer)
            throws OperationNotAllowedException {

        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        if (projektNummer == null || projektNummer.isBlank()) {
            throw new OperationNotAllowedException("Projekt skal vælges");
        }

        if (medarbejderInitialer == null || medarbejderInitialer.isBlank()) {
            throw new OperationNotAllowedException("Projektleder skal vælges");
        }

        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        Medarbejder nyProjektleder = findMedarbejder(medarbejderInitialer);
        if (nyProjektleder == null) {
            throw new OperationNotAllowedException("Medarbejder findes ikke");
        }

        Medarbejder tidligereProjektleder = projekt.getProjektleder();

        try {
            projekt.tilknytMedarbejder(nyProjektleder);
        } catch (OperationNotAllowedException e) {
            if (!"Medarbejder er allerede tilknyttet projekt".equals(e.getMessage())) {
                throw e;
            }
        }

        projekt.opdaterProjektleder(nyProjektleder);

        observers.firePropertyChange("PROJEKTLEDER_OPDATERET", tidligereProjektleder, projekt);

        gemProjekter();
        return true;
    }

    public boolean tilknytMedarbejderTilProjekt(String projektNummer, String initialer) throws OperationNotAllowedException {
        // Der bliver udført struktureret white-box test på denne metode

        if (this.loggedInUser == null) { // 1
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        
        Projekt projekt = findProjekt(projektNummer);

        if (projekt == null) { // 2
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        Medarbejder medarbejder = findMedarbejder(initialer);

        if (medarbejder == null) { // 3
            throw new OperationNotAllowedException("Medarbejder med initialer " + initialer + " findes ikke i systemet");
        }
        projekt.tilknytMedarbejder(medarbejder);
        observers.firePropertyChange("MEDARBEJDER_TILKNYTTET_PROJEKT", null, projekt);
        gemProjekter();
        return true;
    }

    public boolean fjernMedarbejderFraProjekt(String projektNummer, String initialer) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        Medarbejder medarbejder = findMedarbejder(initialer);
        if (medarbejder == null) {
            throw new OperationNotAllowedException("Medarbejder med initialer " + initialer + " findes ikke i systemet");
        }

        projekt.fjernMedarbejder(medarbejder);
        observers.firePropertyChange("MEDARBEJDER_FJERNET_PROJEKT", medarbejder, projekt);

        gemProjekter();
        return true;
    }

    public void registrerTid(String projektNr, String aktivitetsNavn, Double timer)
            throws OperationNotAllowedException {

        // Der bliver udført struktureret white-box test på denne metode

        if (this.loggedInUser == null) { // 1
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        if (projektNr == null || projektNr.isBlank()) { // 2 (2a || 2b)
            throw new OperationNotAllowedException("Projekt skal vælges");
        }

        if (aktivitetsNavn == null || aktivitetsNavn.isBlank()) { // 3 (3a || 3b)
            throw new OperationNotAllowedException("Aktivitet skal vælges");
        }

        if (timer == null || timer <= 0) { // 4 (4a || 4b)
            throw new OperationNotAllowedException("Antal timer skal være større end 0");
        }

        if (timer > 24) { // 5
            throw new OperationNotAllowedException("Antal timer kan ikke overstige 24 timer per dag");
        }

        Projekt projekt = findProjekt(projektNr);

        if (projekt == null) { // 6
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        projekt.registrerTid(aktivitetsNavn, loggedInUser, timer);

        observers.firePropertyChange("TID_REGISTRERET", null, loggedInUser);

        gemProjekter();
    }

    public double visEgneTimer() throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        String initialer = loggedInUser.getInitialer();
        double total = 0;

        for (Projekt p : this.projekter) {
            total += p.getRegistreretTidForMedarbejder(initialer);
        }

        return total;
    }

    // =========================
    // Aktivitet Metoder
    // =========================

    public boolean opretAktivitet(String projektNummer, String aktivitetsNavn,
                                  double forventedeAntalArbejdstimer,
                                  int starttidspunkt, int sluttidspunkt)
            throws OperationNotAllowedException {

        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        if (projektNummer == null || projektNummer.isBlank()) {
            throw new OperationNotAllowedException("Projekt skal vælges");
        }

        if (aktivitetsNavn == null || aktivitetsNavn.isBlank()) {
            throw new OperationNotAllowedException("Aktivitetsnavn må ikke være tomt");
        }

        if (forventedeAntalArbejdstimer < 0) {
            throw new OperationNotAllowedException("Budgetteret tid må ikke være negativ");
        }

        if (starttidspunkt > sluttidspunkt) {
            throw new OperationNotAllowedException("Startuge kan ikke være efter slutuge");
        }

        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        String nytAktivitetsnr =
                projekt.getProjektNummer() + "-" + projekt.getHoejesteAktivitetsnummer();

        projekt.opretAktivitet(
                nytAktivitetsnr,
                aktivitetsNavn,
                forventedeAntalArbejdstimer,
                starttidspunkt,
                sluttidspunkt
        );

        projekt.hoejesteAktivitetsnummerPlusEn();

        Aktivitet nyAktivitet = projekt.findAktivitet(nytAktivitetsnr);

        observers.firePropertyChange("AKTIVITET_OPRETTET", null, nyAktivitet);

        gemProjekter();
        return true;
    }

    public boolean opdaterAktivitet(String projektNummer, String aktivitetsNummer, double forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        if (projektNummer == null || projektNummer.isBlank()) {
            throw new OperationNotAllowedException("Projekt skal vælges");
        }
        if (forventedeAntalArbejdstimer < 0) {
            throw new OperationNotAllowedException("Budgetteret tid må ikke være negativ");
        }
        if (starttidspunkt > sluttidspunkt) {
            throw new OperationNotAllowedException("Startuge kan ikke være efter slutuge");
        }

        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        Aktivitet aktivitetFoerOpdatering = projekt.findAktivitet(aktivitetsNummer);
        if (aktivitetFoerOpdatering == null) {
            throw new OperationNotAllowedException("Aktivitet findes ikke");
        }

        projekt.opdaterAktivitet(aktivitetsNummer, forventedeAntalArbejdstimer, starttidspunkt, sluttidspunkt);
        observers.firePropertyChange("AKTIVITET_OPDATERET", null, aktivitetFoerOpdatering);
        gemProjekter();
        return true;
    }

    public boolean sletAktivitet(String projektNummer, String aktivitetsNummer)

            throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        if (projektNummer == null || projektNummer.isBlank()) {
            throw new OperationNotAllowedException("Projekt skal vælges");
        }
        if (aktivitetsNummer == null || aktivitetsNummer.isBlank()) {
            throw new OperationNotAllowedException("Aktivitet skal vælges");
        }

        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        Aktivitet aktivitet = projekt.findAktivitet(aktivitetsNummer);
        if (aktivitet == null) {
            throw new OperationNotAllowedException("Aktivitet findes ikke");
        }

        projekt.sletAktivitet(aktivitetsNummer);
        observers.firePropertyChange("AKTIVITET_SLETTET", aktivitet, projekt);
        gemProjekter();
        return true;
    }

    public boolean tilknytMedarbejderTilAktivitet(String projektNummer, String aktivitetsNavn, String initialer) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        
        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        Medarbejder medarbejder = findMedarbejder(initialer);
        if (medarbejder == null) {
            throw new OperationNotAllowedException("Medarbejder med initialer " + initialer + " findes ikke i systemet");
        }

        projekt.tilknytMedarbejderTilAktivitet(aktivitetsNavn, medarbejder);
        observers.firePropertyChange("MEDARBEJDER_TILKNYTTET_AKTIVITET", null, projekt.findAktivitet(aktivitetsNavn));
        gemProjekter();
        return true;
    }

    public boolean fjernMedarbejderFraAktivitet(String projektNummer, String aktivitetsNavn, String initialer) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
        
        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        Medarbejder medarbejder = findMedarbejder(initialer);
        if (medarbejder == null) {
            throw new OperationNotAllowedException("Medarbejder med initialer " + initialer + " findes ikke i systemet");
        }

        projekt.fjernMedarbejderFraAktivitet(aktivitetsNavn, medarbejder);
        observers.firePropertyChange("MEDARBEJDER_FJERNET_AKTIVITET", medarbejder, projekt.findAktivitet(aktivitetsNavn));
        gemProjekter();
        return true;
    }

    public List<String> getAlleProjektIds() {
        List<String> ids = new ArrayList<>();
        for (Projekt p : this.projekter) {
            ids.add(p.getProjektNummer());
        }
        return ids;
    }

    public List<String> getAktivitetsNavneForProjekt(String projektNummer) throws OperationNotAllowedException {
        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        List<String> navne = new ArrayList<>();
        for (Aktivitet a : projekt.getAktiviteter()) {
            navne.add(a.getAktivitetsNavn());
        }
        return navne;
    }

    public List<Medarbejder> findLedigeMedarbejdere(int startUge, int slutUge)
            throws OperationNotAllowedException {

        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        List<Medarbejder> ledige = new ArrayList<>();

        for (Medarbejder m : this.medarbejdere) {

            // 1. Tjek fravær
            if (m.harOverlappendeFravaer(startUge, slutUge)) {
                continue;
            }

            // 2. Tjek aktiviteter
            boolean optaget = false;

            for (Projekt p : this.projekter) {
                for (Aktivitet a : p.getAktiviteter()) {

                    boolean overlapper =
                            startUge <= a.getSluttidspunkt()
                                    && slutUge >= a.getStartstidspunkt();

                    if (overlapper && a.isMedarbejderInAktivitet(m)) {
                        optaget = true;
                        break;
                    }
                }
                if (optaget) break;
            }

            if (!optaget) {
                ledige.add(m);
            }
        }

        return ledige;
    }
    public String visLedigeMedarbejdere(int startUge, int slutUge)
            throws OperationNotAllowedException {

        List<Medarbejder> ledige = findLedigeMedarbejdere(startUge, slutUge);

        if (ledige.isEmpty()) {
            return "Ingen ledige medarbejdere i perioden.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Ledige medarbejdere i uge ")
                .append(startUge)
                .append("-")
                .append(slutUge)
                .append(":\n");

        for (Medarbejder m : ledige) {
            sb.append("- ").append(m.getInitialer()).append("\n");
        }

        return sb.toString();
    }

    // =====================
    // Fravaer Metoder
    // =====================

    public void registrerFravaer(String type, Integer startUge, Integer slutUge)
            throws OperationNotAllowedException {

        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        if (type == null || type.isBlank()) {
            throw new OperationNotAllowedException("Fraværstype må ikke være tom");
        }

        if (startUge == null || slutUge == null) {
            throw new OperationNotAllowedException("Startuge og slutuge skal angives");
        }

        if (startUge > slutUge) {
            throw new OperationNotAllowedException("Startuge kan ikke være efter slutuge");
        }

        if (loggedInUser.harOverlappendeFravaer(startUge, slutUge)) {
            throw new OperationNotAllowedException("Fraværsperioden overlapper med eksisterende fravær");
        }

        Fravaer fravaer = new Fravaer(type, startUge, slutUge);
        loggedInUser.tilfoejFravaer(fravaer);

        observers.firePropertyChange("FRAVAER_REGISTRERET", null, loggedInUser);
        fravaerRepository.gemFravaer(this.medarbejdere);
    }

    public boolean harFravaer(String initialer, String type, Integer startUge, Integer slutUge) {
        Medarbejder medarbejder = findMedarbejder(initialer);
        return medarbejder.harFravaer(type, startUge, slutUge);
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

        if (initialer == null || initialer.isBlank()) {
            throw new OperationNotAllowedException("Medarbejder skal vælges");
        }

        if (dato == null) {
            throw new OperationNotAllowedException("Dato skal angives");
        }

        Medarbejder medarbejder = findMedarbejder(initialer);
        if (medarbejder == null) {
            throw new OperationNotAllowedException("Medarbejder findes ikke");
        }

        double total = 0.0;

        for (Projekt p : this.projekter) {
            for (Aktivitet a : p.getAktiviteter()) {
                for (Tidsregistrering t : a.getTidsregistreringerForPersistens()) {
                    if (t.getInitialer().equals(initialer)
                            && !t.getDato().isBefore(dato)) {
                        total += t.getAntalArbejdstimer();
                    }
                }
            }
        }

        return (float) total;
    }
    public float visProjektStatus(String projektNummer) throws OperationNotAllowedException {
        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }

        if (projektNummer == null || projektNummer.isBlank()) {
            throw new OperationNotAllowedException("Projekt skal vælges");
        }

        Projekt projekt = findProjekt(projektNummer);
        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        double totalRegistreretTid = 0.0;

        for (Aktivitet a : projekt.getAktiviteter()) {
            totalRegistreretTid += a.getTotalRegistreretTid();
        }

        return (float) totalRegistreretTid;
    }
    // =======================
    // Filindløsning
    // =======================
    public void indlaesFil() {
        int antalMedarbejdereFoer = this.medarbejdere.size();

        List<Medarbejder> indlaesteMedarbejdere = medarbejderRepository.indlaesMedarbejdere();

        if (indlaesteMedarbejdere != null) {
            this.medarbejdere = indlaesteMedarbejdere;

            if (this.loggedInUser != null) {
                this.loggedInUser = findMedarbejder(this.loggedInUser.getInitialer());
            }

            observers.firePropertyChange(
                    "HR_LISTE_SYNKRONISERET",
                    antalMedarbejdereFoer,
                    this.medarbejdere.size()
            );
        }
        fravaerRepository.indlaesFravaer(this.medarbejdere);

        this.projekter = projektRepository.indlaesProjekter(this.medarbejdere);

        for (Projekt p : this.projekter) {
            int nummer = Integer.parseInt(p.getProjektNummer());
            this.hoejesteProjektnummer =
                    Math.max(this.hoejesteProjektnummer, nummer - 26000 + 1);
        }
    }

    // ====================
    // Rapport Generering
    // ====================
    public String genererRapport(String projektInfo) throws OperationNotAllowedException {
        // 1. Find projektet (helper)
        Projekt p = findProjekt(projektInfo);

        // 2. Validering
        if (p == null) {
            throw new OperationNotAllowedException("Projektet findes ikke i systemet.");
        }

        // 3. formatering og returnering af rapport
        return RapportGenerator.genererProjektRapport(p);
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

    public Medarbejder findMedarbejder(String initialer) {
        for (Medarbejder m: this.medarbejdere) {
            if (m.getInitialer().equals(initialer)) {
                return m;
            }
        }
        return null;
    }

}
