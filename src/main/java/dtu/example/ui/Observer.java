package dtu.example.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import dtu.example.domain.Projekt;

public class Observer implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("PROJECT_OPRETTET")) {
            Projekt p = (Projekt) event.getNewValue();
            System.out.println(">>> SYSTEM BESKED: Nyt projekt oprettet med navn: " + p.getProjektNavn());
        } else if (event.getPropertyName().equals("TID_REGISTRERET")) {
            System.out.println(">>> SYSTEM BESKED: Tid registreret");
        } else if (event.getPropertyName().equals("FRAVAER_REGISTRERET")) {
            System.out.println(">>> SYSTEM BESKED: Fravær registreret");
        }
    }
}
