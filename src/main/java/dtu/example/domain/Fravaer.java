package dtu.example.domain;

public class Fravaer {

    private String type;
    private int startUge;
    private int slutUge;

    public Fravaer(String type, int startUge, int slutUge) {
        this.type = type;
        this.startUge = startUge;
        this.slutUge = slutUge;
    }

    public String getType() {
        return this.type;
    }

    public int getStartUge() {
        return this.startUge;
    }

    public int getSlutUge() {
        return this.slutUge;
    }

    public boolean overlapper(int andenStart, int andenSlut) {
        return andenStart <= this.slutUge && andenSlut >= this.startUge;
    }
}