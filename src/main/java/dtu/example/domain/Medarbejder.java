package dtu.example.domain;

import java.util.Objects;

public class Medarbejder {
    
    private String navn;
    private String initialer;

    public Medarbejder(String navn, String initialer) {
        this.navn = navn;
        this.initialer = initialer;
    }

    // Git test 1

    // Git test 2


    // =================
    // Get Methods
    // =================
    public String getNavn() {
        return this.navn;
    }

    public String getInitialer() {
        return this.initialer;
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
