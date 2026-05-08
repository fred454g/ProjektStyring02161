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
    private ProjektRepository projektRepository;
    private MedarbejderRepository medarbejderRepository;
    private FravaerRepository fravaerRepository;

    // ====================
    // Constructor
    // ====================
    /**
     * For App.java
     * @author Jeppe
     */
    public Planlaegningsvaerktoej() {
        this(
            new ProjektRepository(Paths.get("src", "main", "java", "dtu", "example", "projekter.txt")),
            new MedarbejderRepository(Paths.get("src", "main", "java", "dtu", "example", "hr_liste.txt")),
            new FravaerRepository(Paths.get("src", "main", "java", "dtu", "example", "fravaer.txt"))
        );
    }

    /**
     * For tests
     * @param pr projektrepo
     * @param mr medarbejderrepo
     * @param fr fravaerrepo
     */
    public Planlaegningsvaerktoej(ProjektRepository pr, MedarbejderRepository mr, FravaerRepository fr) {
        this.projektRepository = pr;
        this.medarbejderRepository = mr;
        this.fravaerRepository = fr;
    }

    // ====================
    // User Methods
    // ====================
    /**
     * @author Jeppe
     * @return initialer på bruger logged ind
     */
    public String getLoggedinUserInitials() {
        return this.loggedInUser.getInitialer();
    }
    
    /**
     * @author Jeppe
     * @param initialer på bruger
     * @throws OperationNotAllowedException
     */
    public void userLogin(String initialer) throws OperationNotAllowedException {
        if (findMedarbejder(initialer) == null) {
            throw new OperationNotAllowedException("Medarbejder med initialer " + initialer + " findes ikke i systemet.");
        }
        this.loggedInUser = findMedarbejder(initialer);
    }

    /**
     * Null-stiller loggedInUser
     * @author Jeppe
     */
    public void userLogout() {
        this.loggedInUser = null;
    }

    /**
     * Tilføjer ny medarbejder til systemet
     * @author Jeppe
     * @param initialer på ny medarbejder
     * @throws OperationNotAllowedException
     */
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
    * @author Jeppe
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

        // --- DbC PRE-CONDITION ---
        int forventetAntalProjekter = this.projekter.size() + 1;
        int forventetNytNummer = this.hoejesteProjektnummer + 1;

        String nytProjektnr = String.valueOf(26000 + this.hoejesteProjektnummer);
        Projekt nytProjekt = new Projekt(nytProjektnr, projektNavn);
        projekter.add(nytProjekt);
        this.hoejesteProjektnummer++;
        observers.firePropertyChange("PROJECT_OPRETTET", null, nytProjekt);
        gemProjekter();

        // --- DbC POST-CHECK ---
        assert this.projekter.size() == forventetAntalProjekter : "Post-condition: Projekt blev ikke tilføjet til listen";
        assert this.hoejesteProjektnummer == forventetNytNummer : "Post-condition: højesteProjektnummer blev ikke talt op";
        assert findProjekt(nytProjektnr) != null : "Post-condition: Kunne ikke genfinde det oprettede projekt";
    }

    public void sletProjekt(String projektNummer) throws OperationNotAllowedException {
        // Jacob

        tjek_BrugerErLoggedInd(); // Fejlscenarie 1

        tjek_ProjektErValgt(projektNummer); // Fejlscenarie 2

        Projekt projekt = tjek_ProjektetFindes(projektNummer); // Fejlscenarie 3

        projekter.remove(projekt);
        gemProjekter();
    }

    /**
     * @author Jeppe, Frederik
     * @param projektNummer
     * @param nytNavn
     * @return true hvis lykkedes.
     * @throws OperationNotAllowedException
     */
    public boolean omdoebProjekt(String projektNummer, String nytNavn) throws OperationNotAllowedException {
        // Der bliver udført struktureret white-box test på denne metode

        tjek_BrugerErLoggedInd(); // 1

        tjek_ProjektErValgt(projektNummer); // 2 (2a || 2b)


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
        Projekt projekt = tjek_ProjektetFindes(projektNummer);

        // --- DbC PRE-CONDITION ---
        String gammeltNavn = projekt.getProjektNavn();
        assert !gammeltNavn.equals(nytNavn) : "Pre-condition: Projektet har allerede det ønskede navn";

        boolean opdateret = projekt.opdaterNavn(nytNavn);
        
        if (opdateret) {
            observers.firePropertyChange("PROJEKT_OMDOEBT", gammeltNavn, projekt);
        }

        gemProjekter();

        // --- DbC POST-CHECK ---
        assert projekt.getProjektNavn().equals(nytNavn) : "Post-condition: Projektets navn blev ikke opdateret lokalt i objektet";
        assert findProjekt(projektNummer).getProjektNavn().equals(nytNavn) : "Post-condition: Navneændringen blev ikke persisteret overordnet i systemet";

        return opdateret; // 7
    }

    /**
     * @author Jeppe, Frederik
     * @param projektNummer
     * @param medarbejderInitialer
     * @return
     * @throws OperationNotAllowedException
     */
    public boolean opdaterProjektMedProjektleder(String projektNummer, String medarbejderInitialer)
            throws OperationNotAllowedException {

        tjek_BrugerErLoggedInd(); // Fejlscenarie 1

        tjek_ProjektErValgt(projektNummer); // Fejlscenarie 2

        if (medarbejderInitialer == null || medarbejderInitialer.isBlank()) { // Fejlscenarie 3

            throw new OperationNotAllowedException("Projektleder skal vælges");
        }

        Projekt projekt = tjek_ProjektetFindes(projektNummer); // Fejlscenarie 4

        Medarbejder nyProjektleder = findMedarbejder(medarbejderInitialer);
        if (nyProjektleder == null) { // Fejlscenarie 5
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

    /**
     * @author Jeppe
     * @param projektNummer
     * @param initialer
     * @return true hvis lykkedes
     * @throws OperationNotAllowedException
     */
    public boolean tilfoejMedarbejderTilProjekt(String projektNummer, String initialer) throws OperationNotAllowedException {
        // Der bliver udført struktureret white-box test på denne metode

        tjek_BrugerErLoggedInd(); // 1
        
        Projekt projekt = tjek_ProjektetFindes(projektNummer); // 2

        Medarbejder medarbejder = tjek_MedarbejderenFindes(initialer); // 3

        projekt.tilknytMedarbejder(medarbejder);
        observers.firePropertyChange("MEDARBEJDER_TILKNYTTET_PROJEKT", null, projekt);
        gemProjekter();
        return true;
    }

    /**
     * @author Jeppe
     * @param projektNummer
     * @param initialer
     * @return
     * @throws OperationNotAllowedException
     */
    public boolean fjernMedarbejderFraProjekt(String projektNummer, String initialer) throws OperationNotAllowedException {

        tjek_BrugerErLoggedInd(); // Fejlscenarie 1

        Projekt projekt = tjek_ProjektetFindes(projektNummer); // Fejlscenarie 2

        Medarbejder medarbejder = tjek_MedarbejderenFindes(initialer); // Fejlscenarie 3

        projekt.fjernMedarbejderFraProjekt(medarbejder); // Fejlscenarie 4

        observers.firePropertyChange("MEDARBEJDER_FJERNET_PROJEKT", medarbejder, projekt);
        gemProjekter();
        return true;
    }

    public void registrerTid(String projektNr, String aktivitetsNavn, Double timer)
            throws OperationNotAllowedException {

        // Der bliver udført struktureret white-box test på denne metode

        tjek_BrugerErLoggedInd(); // 1

        tjek_ProjektErValgt(projektNr); // 2 (2a || 2b)

        tjek_AktivitetErValgt(aktivitetsNavn); // 3 (3a || 3b)

        if (timer == null || timer <= 0) { // 4 (4a || 4b)
            throw new OperationNotAllowedException("Antal timer skal være større end 0");
        }

        if (timer > 24) { // 5
            throw new OperationNotAllowedException("Antal timer kan ikke overstige 24 timer per dag");
        }

        Projekt projekt = tjek_ProjektetFindes(projektNr); // 6

        // --- DbC PRE-CONDITION ---
        Aktivitet aktivitet = projekt.findAktivitet(aktivitetsNavn);
        // Hent timer hvis aktiviteten findes, ellers sæt til 0 (koden kaster exception om lidt, hvis den er null)
        double registreretTidFoer = (aktivitet != null) ? aktivitet.getTotalRegistreretTid() : 0.0;

        projekt.registrerTid(aktivitetsNavn, loggedInUser, timer);

        observers.firePropertyChange("TID_REGISTRERET", null, loggedInUser);

        gemProjekter();

        // --- DbC POST-CHECK (Nås kun ved et Success-scenarie) ---
        assert aktivitet != null : "Post-condition: Aktiviteten findes pludselig ikke længere";
        double forventetNyTid = registreretTidFoer + timer;
        assert aktivitet.getTotalRegistreretTid() == forventetNyTid : "Post-condition: Timerne blev ikke lagt korrekt til aktivitetens totale tidsforbrug";
    }

    public double visEgneTimer() throws OperationNotAllowedException {
        
        tjek_BrugerErLoggedInd(); // Fejlscenarie 1

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
    /**
     * @author Jeppe
     * @param projektNummer
     * @param aktivitetsNavn
     * @param forventedeAntalArbejdstimer
     * @param starttidspunkt
     * @param sluttidspunkt
     * @return true hvis lykkedes
     * @throws OperationNotAllowedException
     */
    public boolean opretAktivitet(String projektNummer, String aktivitetsNavn, double forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) throws OperationNotAllowedException {

        tjek_BrugerErLoggedInd();

		tjek_ProjektErValgt(projektNummer);

        tjek_AktivitetErValgt(aktivitetsNavn);

        tjek_forventedeAntalArbejdstimer(forventedeAntalArbejdstimer);

        tjek_StartOgSluttidspunkt(starttidspunkt, sluttidspunkt);

        Projekt projekt = tjek_ProjektetFindes(projektNummer);

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

    /**
     * @author Jeppe
     * @param projektNummer
     * @param aktivitetsNummer
     * @param forventedeAntalArbejdstimer
     * @param starttidspunkt
     * @param sluttidspunkt
     * @return true hvis lykkedes
     * @throws OperationNotAllowedException
     */
    public boolean redigerAktivitet(String projektNummer, String aktivitetsNummer, double forventedeAntalArbejdstimer, int starttidspunkt, int sluttidspunkt) throws OperationNotAllowedException {
        
        tjek_BrugerErLoggedInd();

        tjek_ProjektErValgt(projektNummer);

        tjek_forventedeAntalArbejdstimer(forventedeAntalArbejdstimer);

        tjek_StartOgSluttidspunkt(starttidspunkt, sluttidspunkt);

        Projekt projekt = tjek_ProjektetFindes(projektNummer);

        Aktivitet aktivitetFoerOpdatering = tjek_AktivitetFindes(projekt, aktivitetsNummer);


        projekt.opdaterAktivitet(aktivitetsNummer, forventedeAntalArbejdstimer, starttidspunkt, sluttidspunkt);
        observers.firePropertyChange("AKTIVITET_OPDATERET", null, aktivitetFoerOpdatering);
        gemProjekter();
        return true;
    }

    /**
     * @author Jeppe 
     * @param projektNummer
     * @param aktivitetsNummer
     * @return true hvis lykkedes
     * @throws OperationNotAllowedException
     */
    public boolean sletAktivitet(String projektNummer, String aktivitetsNummer) throws OperationNotAllowedException {
        
        tjek_BrugerErLoggedInd();
        
        tjek_ProjektErValgt(projektNummer);
    
		tjek_AktivitetErValgt(aktivitetsNummer);

	    Projekt projekt = tjek_ProjektetFindes(projektNummer);

        Aktivitet aktivitet = tjek_AktivitetFindes(projekt, aktivitetsNummer);

        projekt.sletAktivitet(aktivitet);
        observers.firePropertyChange("AKTIVITET_SLETTET", aktivitet, projekt);
        gemProjekter();
        return true;
    }

    /**
     * @author Jeppe
     * @param projektNummer
     * @param aktivitetsNavn
     * @param initialer
     * @return
     * @throws OperationNotAllowedException
     */
    public boolean tilfoejMedarbejderTilAktivitet(String projektNummer, String aktivitetsNavn, String initialer) throws OperationNotAllowedException {
        // Jacob

        tjek_BrugerErLoggedInd();
        
        Projekt projekt = tjek_ProjektetFindes(projektNummer);

        Medarbejder medarbejder = tjek_MedarbejderenFindes(initialer);

        // --- DbC PRE-CONDITION ---
        Aktivitet aktivitet = projekt.findAktivitet(aktivitetsNavn);
        int forventetAntalMedarbejdere = (aktivitet != null) ? aktivitet.getTilknyttedeMedarbejdere().size() + 1 : 0;

        projekt.tilfoejMedarbejderTilAktivitet(aktivitetsNavn, medarbejder);
        observers.firePropertyChange("MEDARBEJDER_TILKNYTTET_AKTIVITET", null, projekt.findAktivitet(aktivitetsNavn));
        gemProjekter();

        // --- DbC POST-CHECK (Nås kun ved et Success-scenarie) ---
        assert aktivitet != null : "Post-condition: Aktiviteten bør eksistere";
        assert aktivitet.getTilknyttedeMedarbejdere().size() == forventetAntalMedarbejdere : "Post-condition: Medarbejderlisten voksede ikke med 1 som forventet";
        assert aktivitet.getTilknyttedeMedarbejdere().contains(medarbejder) : "Post-condition: Den specifikke medarbejder blev ikke korrekt tilknyttet aktivitetens liste over medarbejdere";

        return true;
    }

    /**
     * @author Jeppe
     */
    public boolean fjernMedarbejderFraAktivitet(String projektNummer, String aktivitetsNavn, String initialer) throws OperationNotAllowedException {
        // Jacob

        tjek_BrugerErLoggedInd();
  
        Projekt projekt = tjek_ProjektetFindes(projektNummer);

        Medarbejder medarbejder = tjek_MedarbejderenFindes(initialer);

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

        Projekt projekt = tjek_ProjektetFindes(projektNummer);

        List<String> navne = new ArrayList<>();
        for (Aktivitet a : projekt.getAktiviteter()) {
            navne.add(a.getAktivitetsNavn());
        }
        return navne;
    }

    /**
     * @author Jeppe
     * @param startUge
     * @param slutUge
     * @return
     * @throws OperationNotAllowedException
     */
    public List<Medarbejder> findLedigeMedarbejdere(int startUge, int slutUge)
            throws OperationNotAllowedException {
        // --- DbC PRE-CONDITIONS ---
        // Metoden fanger ikke null, så det ville være en fatal logisk fejl at kalde denne metode med start > slut
        assert startUge > 0 && startUge <= 52 : "Pre-condition: Ugyldig startUge ("+startUge+")";
        assert slutUge > 0 && slutUge <= 52 : "Pre-condition: Ugyldig slutUge ("+slutUge+")";
        assert startUge <= slutUge : "Pre-condition: Startuge må ikke være efter slutuge";
            
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

        // --- DbC POST-CHECK ---
        assert ledige != null : "Post-condition: Returneret liste må ikke være null (skal være en tom liste i stedet)";
        assert !ledige.contains(null) : "Post-condition: Listen indeholder null-referencer";

        return ledige;
    }
    
    public String visLedigeMedarbejdere(int startUge, int slutUge) throws OperationNotAllowedException {

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
    /**
     * @author Jeppe
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        observers.addPropertyChangeListener(listener);
    }

    /**
     * @author Jeppe
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        observers.removePropertyChangeListener(listener);
    }

    // =======================
    // Filindløsning
    // =======================
    /**
     * @author Jeppe
     */
    public void indlaesFil() {
        int antalMedarbejdereFoer = this.medarbejdere.size();

        List<Medarbejder> indlaesteMedarbejdere = medarbejderRepository.indlaesMedarbejdere();

        if (indlaesteMedarbejdere != null) {
            if (!indlaesteMedarbejdere.isEmpty()) {
                this.medarbejdere = indlaesteMedarbejdere;

                if (this.loggedInUser != null) {
                    this.loggedInUser = findMedarbejder(this.loggedInUser.getInitialer());
                }

                observers.firePropertyChange(
                        "HR_LISTE_SYNKRONISERET",
                        antalMedarbejdereFoer,
                        this.medarbejdere.size()
                );
            } else {
                // If HR-file yields no valid employees (empty list), keep existing employees
                System.out.println("HR-fil var tom eller indeholdt ingen gyldige initialer; behold eksisterende medarbejdere.");
            }
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
    /**
     * @author Jeppe
     * @param projektInfo
     * @return
     */
    public Projekt findProjekt(String projektInfo) {
        for (Projekt p: this.projekter) {
            if (p.getProjektNummer().equals(projektInfo) || p.getProjektNavn().equals(projektInfo)) {
                return p;
            }
        }
        return null;
    }

    /**
     * @author Jeppe
     * @param initialer
     * @return
     */
    public Medarbejder findMedarbejder(String initialer) {
        for (Medarbejder m: this.medarbejdere) {
            if (m.getInitialer().equals(initialer)) {
                return m;
            }
        }
        return null;
    }

    // =====================
    // Tjek hjaepler metoder
    // =====================

    private Projekt tjek_ProjektetFindes(String projektNummer) throws OperationNotAllowedException {
        // Jacob

        Projekt projekt = findProjekt(projektNummer);

        if (projekt == null) {
            throw new OperationNotAllowedException("Projekt findes ikke");
        }

        return projekt;
    }

    private Medarbejder tjek_MedarbejderenFindes(String initialer) throws OperationNotAllowedException {
        // Jacob

        Medarbejder medarbejder = findMedarbejder(initialer);

        if (medarbejder == null) {
            throw new OperationNotAllowedException("Medarbejder med initialer " + initialer + " findes ikke i systemet");
        }

        return medarbejder;
    }

    private Aktivitet tjek_AktivitetFindes(Projekt projekt, String aktivitetsNummer) throws OperationNotAllowedException {
        // Jacob

        Aktivitet aktivitet = projekt.findAktivitet(aktivitetsNummer);
        
         if (aktivitet == null) {
            throw new OperationNotAllowedException("Aktivitet findes ikke");
        }

        return aktivitet;
    }

    private void tjek_BrugerErLoggedInd() throws OperationNotAllowedException {
        // Jacob

        if (this.loggedInUser == null) {
            throw new OperationNotAllowedException("Ingen bruger logged in");
        }
    }

    private void tjek_AktivitetErValgt(String aktivitetsNummer) throws OperationNotAllowedException {
        // Jacob

        if (aktivitetsNummer == null || aktivitetsNummer.isBlank()) {
            throw new OperationNotAllowedException("Aktivitet skal vælges");
        }
    }

    private void tjek_ProjektErValgt(String projektNummer) throws OperationNotAllowedException {
        // Jacob

        if (projektNummer == null || projektNummer.isBlank()) {
            throw new OperationNotAllowedException("Projekt skal vælges");
        }
    }

    private void tjek_forventedeAntalArbejdstimer(double forventedeAntalArbejdstimer) throws OperationNotAllowedException {
        // Jacob

        if (forventedeAntalArbejdstimer < 0) {
            throw new OperationNotAllowedException("Budgetteret tid må ikke være negativ");
        }
    }

    private void tjek_StartOgSluttidspunkt(double starttidspunkt, double sluttidspunkt) throws OperationNotAllowedException {
        // Jacob
        
        if (starttidspunkt > sluttidspunkt) {
            throw new OperationNotAllowedException("Startuge kan ikke være efter slutuge");
        }
    }

}
