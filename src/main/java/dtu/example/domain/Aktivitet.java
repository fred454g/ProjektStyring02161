package dtu.example.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Aktivitet {
    
    private String aktivitetsnummer;
    private float forventedeAntalArbejdstimer;
    private LocalDate starttidspunkt;
    private LocalDate sluttidspunkt;
    private List<Tidsregistrering> tidsregistreringer = new ArrayList<>();
    
    public Aktivitet(String aktivitetsnummer, float forventedeAntalArbejdstimer, LocalDate starttidspunkt, LocalDate sluttidspunkt) {
        this.aktivitetsnummer = aktivitetsnummer;
        this.forventedeAntalArbejdstimer = forventedeAntalArbejdstimer;
        this.starttidspunkt = starttidspunkt;
        this.sluttidspunkt = sluttidspunkt;
    }

    public boolean setStarttidspunkt(LocalDate startstidspunkt) {
        this.starttidspunkt = startstidspunkt;
        return true;
    }

    public boolean setSluttidspunkt(LocalDate sluttidspunkt) {
        this.sluttidspunkt = sluttidspunkt;
        return true;
    }

    public void registrerTid(String initialer, LocalDate dato, float antalArbejdstimer) {

    }

    public float visMedarbejdersTimer(String initialer) {
        return 0.0f;
    }

    public void overbliksRapport(LocalDate starttidspunkt, LocalDate sluttidspunkt) {

    }

    public void givAktivitetsStatus() {

    }
}
