package dtu.example.persistence;

import dtu.example.domain.Medarbejder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

public class MedarbejderRepository {

    private final Path hrListePath;
    private long sidsteHrListeOpdatering = -1L;

    public MedarbejderRepository(Path hrListePath) {
        this.hrListePath = hrListePath;
    }

    public List<Medarbejder> indlaesMedarbejdere() {
        try {
            FileTime fileTime = Files.getLastModifiedTime(hrListePath);
            long nuvaerendeOpdatering = fileTime.toMillis();

            if (nuvaerendeOpdatering <= this.sidsteHrListeOpdatering) {
                return null;
            }

            List<String> linjer = Files.readAllLines(hrListePath);
            List<Medarbejder> medarbejdere = new ArrayList<>();

            for (String linje : linjer) {
                if (linje == null || linje.trim().isEmpty()) {
                    continue;
                }

                String initialer = linje.trim();

                boolean findesAllerede = false;
                for (Medarbejder medarbejder : medarbejdere) {
                    if (medarbejder.getInitialer().equals(initialer)) {
                        findesAllerede = true;
                        break;
                    }
                }

                if (!findesAllerede) {
                    medarbejdere.add(new Medarbejder(initialer));
                }
            }

            this.sidsteHrListeOpdatering = nuvaerendeOpdatering;
            return medarbejdere;

        } catch (IOException e) {
            throw new RuntimeException("Kunne ikke indlæse HR-liste", e);
        }
    }
}