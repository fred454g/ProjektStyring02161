Feature: Indlæs HR medarbejderliste (File Load)
    For at systemet altid har de korrekte brugere oprettet
    Som systemet
    Vil jeg automatisk indlæse en liste med medarbejderinitialer fra HR

    Scenario: Hovedscenarie - Automatisk indlæsning af initialer
        Given at en ny HR-fil med medarbejderinitialer er tilgængelig
        When systemet udfoerer sin automatiske file load
        Then oprettes nye medarbejdere i systemet baseret på initialerne i filen
        And eksisterende medarbejdere opdateres