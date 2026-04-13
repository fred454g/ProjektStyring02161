package dtu.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Medarbejder {
    
    private String navn;
    private String initialer;
    private List<Fravaer> fravaersliste = new ArrayList<>();

    public Medarbejder(String navn, String initialer) {
        this.navn = navn;
        this.initialer = initialer;
    }

    public void tilfoejFravaer(Fravaer fravaer) {
        this.fravaersliste.add(fravaer);
    }

    public boolean harFravaer(String type, int startUge, int slutUge) {
        for (Fravaer f : this.fravaersliste) {
            if (f.getType().equals(type) && f.getStartUge() == startUge && f.getSlutUge() == slutUge) {
                return true;
            }
        }
        return false;
    }

    public boolean harOverlappendeFravaer(int startUge, int slutUge) {
        for (Fravaer f : this.fravaersliste) {
            if (f.overlapper(startUge, slutUge)) {
                return true;
            }
        }
        return false;
    }

    // =================
    // Get Methods
    // =================
    public String getNavn() {
        return this.navn;
    }

    public String getInitialer() {
        return this.initialer;
    }
}
