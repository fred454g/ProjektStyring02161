package dtu.example.domain;

public class Medarbejder {
    
    private String navn;
    private String initialer;

    public Medarbejder(String navn, String initialer) {
        this.navn = navn;
        this.initialer = initialer;
    }

    // Git test 1


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
