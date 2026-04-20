package dtu.example.domain;

public class RapportGenerator {

    // Modtager et projekt og returnerer en formateret streng
    public static String genererProjektRapport(Projekt p) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== STATUSRAPPORT FOR PROJEKT: ").append(p.getProjektNavn()).append(" ===\n");
        sb.append("Projektnummer: ").append(p.getProjektNummer()).append("\n");

        // Tjekker om der er en projektleder tilknyttet
        Medarbejder leder = p.getProjektleder();
        if (leder != null) {
            sb.append("Projektleder: ").append(leder.getNavn())
                    .append(" (").append(leder.getInitialer()).append(")\n");
        } else {
            sb.append("Projektleder: Ikke tildelt endnu\n");
        }

        sb.append("--------------------------------------------------\n");

        double totalBudget = 0;
        double totalBrugt = 0;

        // Trækker data op fra Aktivitet-laget
        for (Aktivitet a : p.getAktiviteter()) {
            double budget = a.getForventedeAntalArbejdsTimer();
            double brugt = a.getTotalRegistreretTid();

            sb.append("Aktivitet: ").append(a.getAktivitetsNavn()).append("\n");
            sb.append(" - Budget: ").append(budget).append(" timer\n");
            sb.append(" - Brugt:  ").append(brugt).append(" timer\n");

            totalBudget += budget;
            totalBrugt += brugt;
        }

        sb.append("--------------------------------------------------\n");
        sb.append("Total Budget: ").append(totalBudget).append(" timer\n");
        sb.append("Total Brugt:  ").append(totalBrugt).append(" timer\n");
        sb.append("Restarbejde:  ").append(totalBudget - totalBrugt).append(" timer\n");

        return sb.toString();
    }
}