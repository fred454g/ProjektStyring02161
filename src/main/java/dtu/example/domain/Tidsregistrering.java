package dtu.example.domain;

import java.time.LocalDate;

public class Tidsregistrering {
    
    private LocalDate dato;
    private double antalArbejdstimer;
    private String initialer;
    
    public Tidsregistrering(LocalDate dato, double antalArbejdstimer, String initialer) {
        this.dato = dato;
        this.antalArbejdstimer = antalArbejdstimer;
        this.initialer = initialer;
    }

    // ======================
    // Get methods
    // ======================
    public LocalDate getDato() {
        return this.dato;
    }

    public double getAntalArbejdstimer() {
        return this.antalArbejdstimer;
    }

    public String getInitialer() {
        return this.initialer;
    }

    public String getDatoForPersistens() {
        return this.dato.toString(); // Returnerer formatet YYYY-MM-DD
    }
}
