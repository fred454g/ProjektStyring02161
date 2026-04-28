package dtu.example.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import dtu.example.domain.Aktivitet;
import dtu.example.domain.Medarbejder;
import dtu.example.domain.Projekt;

public class Observer implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        String eventName = event.getPropertyName();

        if ("PROJECT_OPRETTET".equals(eventName)) {
            Projekt projekt = (Projekt) event.getNewValue();
            System.out.println(">>> SYSTEM BESKED: Nyt projekt oprettet med navn: " + projekt.getProjektNavn());
        } else if ("MEDARBEJDER_OPRETTET".equals(eventName)) {
            Medarbejder medarbejder = (Medarbejder) event.getNewValue();
            System.out.println(">>> SYSTEM BESKED: Ny medarbejder oprettet: " + medarbejder.getInitialer());
        } else if ("PROJEKT_OMDOEBT".equals(eventName)) {
            Projekt projekt = (Projekt) event.getNewValue();
            String gammeltNavn = (String) event.getOldValue();
            System.out.println(">>> SYSTEM BESKED: Projekt omdøbt fra '" + gammeltNavn + "' til '" + projekt.getProjektNavn() + "'");
        } else if ("PROJEKTLEDER_OPDATERET".equals(eventName)) {
            Projekt projekt = (Projekt) event.getNewValue();
            Medarbejder nyLeder = projekt.getProjektleder();
            System.out.println(">>> SYSTEM BESKED: Projektleder opdateret på projekt " + projekt.getProjektNummer()
                    + " til " + (nyLeder == null ? "ingen" : nyLeder.getInitialer()));
        } else if ("MEDARBEJDER_TILKNYTTET_PROJEKT".equals(eventName)) {
            Projekt projekt = (Projekt) event.getNewValue();
            System.out.println(">>> SYSTEM BESKED: Medarbejder tilknyttet projekt " + projekt.getProjektNummer());
        } else if ("MEDARBEJDER_FJERNET_PROJEKT".equals(eventName)) {
            Medarbejder medarbejder = (Medarbejder) event.getOldValue();
            Projekt projekt = (Projekt) event.getNewValue();
            System.out.println(">>> SYSTEM BESKED: Medarbejder " + medarbejder.getInitialer()
                    + " fjernet fra projekt " + projekt.getProjektNummer());
        } else if ("AKTIVITET_OPRETTET".equals(eventName)) {
            Aktivitet aktivitet = (Aktivitet) event.getNewValue();
            System.out.println(">>> SYSTEM BESKED: Ny aktivitet oprettet: " + aktivitet.getAktivitetsNavn());
        } else if ("AKTIVITET_SLETTET".equals(eventName)) {
            Aktivitet aktivitet = (Aktivitet) event.getOldValue();
            Projekt projekt = (Projekt) event.getNewValue();
            System.out.println(">>> SYSTEM BESKED: Aktivitet: " + aktivitet.getAktivitetsNavn() + " slettet fra projekt " + projekt.getProjektNummer());
        } else if ("AKTIVITET_OPDATERET".equals(eventName)) {
            Aktivitet aktivitet = (Aktivitet) event.getNewValue();
            System.out.println(">>> SYSTEM BESKED: Aktivitet opdateret: " + aktivitet.getAktivitetsNavn());
        } else if ("MEDARBEJDER_TILKNYTTET_AKTIVITET".equals(eventName)) {
            Aktivitet aktivitet = (Aktivitet) event.getNewValue();
            System.out.println(">>> SYSTEM BESKED: Medarbejder tilknyttet aktivitet " + aktivitet.getAktivitetsNavn());
        } else if ("MEDARBEJDER_FJERNET_AKTIVITET".equals(eventName)) {
            Medarbejder medarbejder = (Medarbejder) event.getOldValue();
            Aktivitet aktivitet = (Aktivitet) event.getNewValue();
            System.out.println(">>> SYSTEM BESKED: Medarbejder " + medarbejder.getInitialer()
                    + " fjernet fra aktivitet " + aktivitet.getAktivitetsNavn());
        } else if ("HR_LISTE_SYNKRONISERET".equals(eventName)) {
            int foer = (Integer) event.getOldValue();
            int efter = (Integer) event.getNewValue();
            System.out.println(">>> SYSTEM BESKED: HR-liste synkroniseret (" + foer + " -> " + efter + " medarbejdere)");
        } else if ("TID_REGISTRERET".equals(eventName)) {
            System.out.println(">>> SYSTEM BESKED: Tid registreret");
        } else if ("FRAVAER_REGISTRERET".equals(eventName)) {
            System.out.println(">>> SYSTEM BESKED: Fravær registreret");
        }
    }
}
