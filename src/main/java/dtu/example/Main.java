package dtu.example;

import dtu.example.domain.Planlaegningsvaerktoej;
import dtu.example.ui.Observer;

public class Main {
    public static void main(String[] args) {
        
        // 1. Create model (The backend - /domain)
        Planlaegningsvaerktoej model = new Planlaegningsvaerktoej();

        // 2. Create view (The frontend - /ui)
        Observer view = new Observer();

        // 3. Subscribe the view to the model
        model.addPropertyChangeListener(view);

        // 4. Controller action (Simulating user input)
        try {
            model.userLogin("JFK");
            System.out.println("Bruger taster: Opret projekt 'Nyt IT System'");
            model.opretProjekt("Nyt IT System"); // Automatically triggers
        } catch (Exception e) {
            System.out.println("Fejl: " + e.getMessage());
        }
    }
}
