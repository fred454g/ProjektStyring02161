package dtu.example.persistence;
import dtu.example.domain.Aktivitet;
import dtu.example.domain.Medarbejder;
import dtu.example.domain.OperationNotAllowedException;
import dtu.example.domain.Projekt;
import dtu.example.domain.Tidsregistrering;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProjektRepository {
    private final Path projektSti;

    public ProjektRepository(Path projektSti) {
        this.projektSti = projektSti;
    }

    public void gemProjekter(List<Projekt> projekter) {

        List<String> linjer = new ArrayList<>();
        for (Projekt p : projekter) {
            linjer.add("P," + p.getProjektNummer() + "," + p.getProjektNavn());

            if (p.getProjektleder() != null) {
                linjer.add("L," + p.getProjektleder().getInitialer());
            }
            for (Medarbejder m : p.getTilknyttedeMedarbejdere()) {
                linjer.add("M," + m.getInitialer());
            }
            for (Aktivitet a : p.getAktiviteter()) {
                linjer.add("A," + a.getAktivitetsnummer() + "," +

                        a.getAktivitetsNavn() + "," +
                        a.getForventedeAntalArbejdsTimer() + "," +
                        a.getStartstidspunkt() + "," +
                        a.getSluttidspunkt());
                for (Medarbejder m : a.getTilknyttedeMedarbejdere()) {
                    linjer.add("AM," + a.getAktivitetsnummer() + "," + m.getInitialer());
                }
                for (Tidsregistrering t : a.getTidsregistreringerForPersistens()) {
                    linjer.add("T," + a.getAktivitetsnummer() + "," +

                            t.getAntalArbejdstimer() + "," +
                            t.getDato() + "," +
                            t.getInitialer());
                }
            }
        }
        try {
            Files.write(projektSti, linjer);
        } catch (IOException e) {
            System.err.println("Kunne ikke gemme projekter: " + e.getMessage());
        }
    }

    public List<Projekt> indlaesProjekter(List<Medarbejder> medarbejdere) {

        List<Projekt> projekter = new ArrayList<>();
        if (!Files.exists(projektSti)) {
            return projekter;
        }
        try {
            List<String> projektLinjer = Files.readAllLines(projektSti);
            Projekt nuvaerendeProjekt = null;

            for (String linje : projektLinjer) {
                String[] dele = linje.split(",");

                if (dele.length == 0) {
                    continue;
                }
                if (dele[0].equals("P")) {
                    if (dele.length < 3) {
                        continue;
                    }
                    nuvaerendeProjekt = new Projekt(dele[1], dele[2]);
                    projekter.add(nuvaerendeProjekt);

                } else if (dele[0].equals("L") && nuvaerendeProjekt != null) {

                    if (dele.length < 2) {
                        continue;
                    }
                    Medarbejder leder = findMedarbejder(medarbejdere, dele[1]);
                    if (leder != null) {
                        try {
                            if (!nuvaerendeProjekt.isMedarbejderInProjekt(leder)) {
                                nuvaerendeProjekt.tilknytMedarbejder(leder);
                            }
                        } catch (OperationNotAllowedException ignored) {
                        }
                        nuvaerendeProjekt.opdaterProjektleder(leder);
                    }
                } else if (dele[0].equals("M") && nuvaerendeProjekt != null) {

                    if (dele.length < 2) {
                        continue;
                    }
                    Medarbejder medarbejder = findMedarbejder(medarbejdere, dele[1]);
                    if (medarbejder != null) {
                        try {
                            if (!nuvaerendeProjekt.isMedarbejderInProjekt(medarbejder)) {

                                nuvaerendeProjekt.tilknytMedarbejder(medarbejder);
                            }

                        } catch (OperationNotAllowedException ignored) {

                        }
                    }
                } else if (dele[0].equals("A") && nuvaerendeProjekt != null) {

                    if (dele.length < 6) {
                        continue;
                    }
                    nuvaerendeProjekt.opretAktivitet(
                            dele[1],
                            dele[2],
                            Double.parseDouble(dele[3]),
                            Integer.parseInt(dele[4]),
                            Integer.parseInt(dele[5])
                    );

                } else if (dele[0].equals("AM") && nuvaerendeProjekt != null) {

                    if (dele.length < 3) {
                        continue;
                    }

                    String aktivitetsNummer = dele[1];
                    String initialer = dele[2];
                    Medarbejder medarbejder = findMedarbejder(medarbejdere, initialer);

                    if (medarbejder != null) {
                        try {
                            if (!nuvaerendeProjekt.isMedarbejderInProjekt(medarbejder)) {
                                nuvaerendeProjekt.tilknytMedarbejder(medarbejder);
                            }
                            nuvaerendeProjekt.tilknytMedarbejderTilAktivitet(aktivitetsNummer, medarbejder);

                        } catch (OperationNotAllowedException ignored) {
                        }
                    }
                } else if (dele[0].equals("T") && nuvaerendeProjekt != null) {

                    if (dele.length < 5) {
                        continue;
                    }

                    String aktivitetsNummer = dele[1];
                    double timer = Double.parseDouble(dele[2]);
                    String dato = dele[3];
                    String initialer = dele[4];
                    Aktivitet aktivitet = nuvaerendeProjekt.findAktivitet(aktivitetsNummer);

                    if (aktivitet != null) {
                        aktivitet.tilfoejGenskabtTid(timer, dato, initialer);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Fejl ved indlæsning af projekter: " + e.getMessage());
        }
        return projekter;
    }

    private Medarbejder findMedarbejder(List<Medarbejder> medarbejdere, String initialer) {
        for (Medarbejder m : medarbejdere) {
            if (m.getInitialer().equals(initialer)) {
                return m;
            }
        }
        return null;
    }
}