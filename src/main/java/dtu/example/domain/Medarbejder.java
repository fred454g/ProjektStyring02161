package dtu.example.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Medarbejder {

    private String initialer;
    private List<Fravaer> fravaersliste = new ArrayList<>();

    public Medarbejder(String initialer) {
        if (initialer == null || initialer.isBlank()) {
            throw new IllegalArgumentException("Initialer må ikke være tomme");
        }
        if (initialer.length() > 4) {
            throw new IllegalArgumentException("Initialer må ikke være længere end 4 tegn");
        }
        this.initialer = initialer;
    }

    public void tilfoejFravaer(Fravaer fravaer) {
        this.fravaersliste.add(fravaer);
    }

    public void tilfoejGenskabtFravaer(String type, int startUge, int slutUge) {
        this.fravaersliste.add(new Fravaer(type, startUge, slutUge));
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

    public String getInitialer() {
        return this.initialer;
    }

    public List<Fravaer> getFravaerslisteForPersistens() {
        return this.fravaersliste;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Medarbejder that = (Medarbejder) other;
        return Objects.equals(this.initialer, that.initialer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.initialer);
    }
}
