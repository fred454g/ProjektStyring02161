package dtu.example.domain;

import java.time.LocalDate;

public class Tidsregistrering {
    
    private LocalDate dato;
    private float antalArbejdstimer;
    private String initialer;
    
    public Tidsregistrering(LocalDate dato, float antalArbejdstimer, String initialer) {
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

    public float getAntalArbejdstimer() {
        return this.antalArbejdstimer;
    }

    public String getInitialer() {
        return this.initialer;
    }
}
