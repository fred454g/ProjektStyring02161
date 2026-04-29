package dtu.example.domain;

import java.util.Locale;

public class RapportGenerator {

    public static String genererProjektRapport(Projekt p) {
        StringBuilder sb = new StringBuilder();

        double totalBudget = beregnTotalBudget(p);
        double totalBrugt = beregnTotalBrugt(p);
        double totalRest = totalBudget - totalBrugt;

        sb.append("==================================================\n");
        sb.append(" STATUSRAPPORT FOR PROJEKT\n");
        sb.append("==================================================\n");
        sb.append("Projektnavn:   ").append(p.getProjektNavn()).append("\n");
        sb.append("Projektnummer: ").append(p.getProjektNummer()).append("\n");

        Medarbejder leder = p.getProjektleder();
        if (leder != null) {
            sb.append("Projektleder:  ").append(leder.getInitialer()).append("\n");
        } else {
            sb.append("Projektleder:  Ikke tildelt\n");
        }

        sb.append("Medarbejdere:  ");

        if (p.getTilknyttedeMedarbejdere().isEmpty()) {
            sb.append("Ingen tilknyttede medarbejdere\n");
        } else {
            for (int i = 0; i < p.getTilknyttedeMedarbejdere().size(); i++) {
                Medarbejder m = p.getTilknyttedeMedarbejdere().get(i);
                sb.append(m.getInitialer());

                if (i < p.getTilknyttedeMedarbejdere().size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }

        sb.append("\n");
        sb.append("SAMLET STATUS\n");
        sb.append("--------------------------------------------------\n");
        sb.append("Budgetteret tid: ").append(formatTimer(totalBudget)).append(" timer\n");
        sb.append("Registreret tid: ").append(formatTimer(totalBrugt)).append(" timer\n");
        sb.append("Restarbejde:     ").append(formatTimer(totalRest)).append(" timer\n");
        sb.append("Forbrugsgrad:    ").append(formatProcent(totalBrugt, totalBudget)).append("\n");
        sb.append("Status:          ").append(vurderProjektStatus(totalBrugt, totalBudget)).append("\n");

        sb.append("\n");
        sb.append("AKTIVITETER\n");
        sb.append("--------------------------------------------------\n");

        if (p.getAktiviteter().isEmpty()) {
            sb.append("Der er endnu ikke oprettet aktiviteter på projektet.\n");
        }

        for (Aktivitet a : p.getAktiviteter()) {
            double budget = a.getForventedeAntalArbejdsTimer();
            double brugt = a.getTotalRegistreretTid();
            double rest = budget - brugt;

            sb.append("\n");
            sb.append("Aktivitet: ").append(a.getAktivitetsNavn()).append("\n");
            sb.append("Aktivitetsnr.: ").append(a.getAktivitetsnummer()).append("\n");
            sb.append("Periode: uge ").append(a.getStartstidspunkt())
                    .append(" - ").append(a.getSluttidspunkt()).append("\n");
            sb.append("Budget: ").append(formatTimer(budget)).append(" timer\n");
            sb.append("Brugt:  ").append(formatTimer(brugt)).append(" timer\n");
            sb.append("Rest:   ").append(formatTimer(rest)).append(" timer\n");
            sb.append("Forbrug: ").append(formatProcent(brugt, budget)).append("\n");
            sb.append("Vurdering: ").append(vurderAktivitetsStatus(brugt, budget)).append("\n");

            sb.append("Medarbejdere: ");

            if (a.getTilknyttedeMedarbejdere().isEmpty()) {
                sb.append("Ingen tilknyttet\n");
            } else {
                for (int i = 0; i < a.getTilknyttedeMedarbejdere().size(); i++) {
                    Medarbejder m = a.getTilknyttedeMedarbejdere().get(i);
                    sb.append(m.getInitialer());

                    if (i < a.getTilknyttedeMedarbejdere().size() - 1) {
                        sb.append(", ");
                    }
                }
                sb.append("\n");
            }
        }

        sb.append("\n");
        sb.append("LEDELSESOVERBLIK\n");
        sb.append("--------------------------------------------------\n");
        sb.append("Antal aktiviteter: ").append(p.getAktiviteter().size()).append("\n");
        sb.append("Aktiviteter over budget: ").append(antalAktiviteterOverBudget(p)).append("\n");
        sb.append("Aktiviteter uden registreret tid: ").append(antalAktiviteterUdenTid(p)).append("\n");

        sb.append("\n");
        sb.append("ANBEFALING\n");
        sb.append("--------------------------------------------------\n");
        sb.append(genererAnbefaling(p, totalBudget, totalBrugt));

        return sb.toString();
    }

    private static double beregnTotalBudget(Projekt p) {
        double total = 0;

        for (Aktivitet a : p.getAktiviteter()) {
            total += a.getForventedeAntalArbejdsTimer();
        }

        return total;
    }

    private static double beregnTotalBrugt(Projekt p) {
        double total = 0;

        for (Aktivitet a : p.getAktiviteter()) {
            total += a.getTotalRegistreretTid();
        }

        return total;
    }

    private static int antalAktiviteterOverBudget(Projekt p) {
        int antal = 0;

        for (Aktivitet a : p.getAktiviteter()) {
            if (a.getTotalRegistreretTid() > a.getForventedeAntalArbejdsTimer()) {
                antal++;
            }
        }

        return antal;
    }

    private static int antalAktiviteterUdenTid(Projekt p) {
        int antal = 0;

        for (Aktivitet a : p.getAktiviteter()) {
            if (a.getTotalRegistreretTid() == 0) {
                antal++;
            }
        }

        return antal;
    }

    private static String vurderProjektStatus(double brugt, double budget) {
        if (budget == 0) {
            return "Ingen budgetteret tid";
        }

        double forbrugsgrad = brugt / budget;

        if (forbrugsgrad > 1.0) {
            return "KRITISK - projektet er over budget";
        }

        if (forbrugsgrad >= 0.8) {
            return "OBS - projektet nærmer sig budgetgrænsen";
        }

        return "OK - projektet er inden for budget";
    }

    private static String vurderAktivitetsStatus(double brugt, double budget) {
        if (budget == 0) {
            return "Ingen budgetteret tid";
        }

        if (brugt > budget) {
            return "Over budget";
        }

        if (brugt == 0) {
            return "Ingen tid registreret endnu";
        }

        if (brugt >= budget * 0.8) {
            return "Nærmer sig budget";
        }

        return "Inden for budget";
    }

    private static String genererAnbefaling(Projekt p, double totalBudget, double totalBrugt) {
        if (p.getAktiviteter().isEmpty()) {
            return "Projektet bør opdeles i aktiviteter, før der kan følges op på fremdrift.\n";
        }

        if (totalBudget == 0) {
            return "Projektet mangler budgetteret tid på aktiviteterne.\n";
        }

        if (totalBrugt > totalBudget) {
            return "Projektlederen bør gennemgå aktiviteter over budget og revurdere restarbejdet.\n";
        }

        if (antalAktiviteterUdenTid(p) > 0) {
            return "Der findes aktiviteter uden registreret tid. Projektlederen bør sikre, at arbejdet registreres løbende.\n";
        }

        return "Projektet ser umiddelbart kontrolleret ud. Følg fortsat op på aktiviteter tæt på budgetgrænsen.\n";
    }

    private static String formatTimer(double timer) {
        return String.format(Locale.US, "%.1f", timer);
    }

    private static String formatProcent(double brugt, double budget) {
        if (budget == 0) {
            return "N/A";
        }

        return String.format(Locale.US, "%.1f%%", (brugt / budget) * 100);
    }
}