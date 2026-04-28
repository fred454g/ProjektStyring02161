package dtu.example.persistence;

import dtu.example.domain.Fravaer;
import dtu.example.domain.Medarbejder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FravaerRepository {

    private final Path path;

    public FravaerRepository(Path path) {
        this.path = path;
    }

    public void gemFravaer(List<Medarbejder> medarbejdere) {
        List<String> linjer = new ArrayList<>();

        for (Medarbejder m : medarbejdere) {
            for (Fravaer f : m.getFravaerslisteForPersistens()) {
                linjer.add(
                        m.getInitialer() + "," +
                                f.getType() + "," +
                                f.getStartUge() + "," +
                                f.getSlutUge()
                );
            }
        }

        try {
            Files.write(path, linjer);
        } catch (IOException e) {
            System.err.println("Kunne ikke gemme fravær: " + e.getMessage());
        }
    }

    public void indlaesFravaer(List<Medarbejder> medarbejdere) {
        if (!Files.exists(path)) {
            return;
        }

        try {
            List<String> linjer = Files.readAllLines(path);

            for (String linje : linjer) {
                String[] dele = linje.split(",");

                if (dele.length != 4) {
                    continue;
                }

                String initialer = dele[0];
                String type = dele[1];
                int startUge = Integer.parseInt(dele[2]);
                int slutUge = Integer.parseInt(dele[3]);

                Medarbejder medarbejder = findMedarbejder(medarbejdere, initialer);

                if (medarbejder != null) {
                    medarbejder.tilfoejGenskabtFravaer(type, startUge, slutUge);
                }
            }
        } catch (Exception e) {
            System.err.println("Kunne ikke indlæse fravær: " + e.getMessage());
        }
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