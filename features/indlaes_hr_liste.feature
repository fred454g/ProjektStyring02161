Feature: Indlæs HR medarbejderliste (File Load)
    For at systemet altid har de korrekte brugere oprettet
    Som systemet
    Vil jeg automatisk indlæse en liste med medarbejderinitialer fra HR

    Scenario: Hovedscenarie - Automatisk indlæsning af initialer
        Given at en ny HR-fil med medarbejderinitialer er tilgængelig
        When systemet udfoerer sin automatiske file load
        Then oprettes nye medarbejdere i systemet baseret på initialerne i filen
        And eksisterende medarbejdere opdateres

    Scenario: Systemet indeholder altid medarbejderen huba
        Given at en ny HR-fil med medarbejderinitialer er tilgængelig
        When systemet udfoerer sin automatiske file load
        Then findes medarbejderen "huba" i systemet

    Scenario: Indlæsning af fil med duplikerede initialer
        Given at en HR-fil med duplikerede initialer er tilgængelig
        When systemet udfoerer sin automatiske file load
        Then oprettes hver medarbejder kun én gang i systemet

    Scenario: Indlæsning af tom HR-fil
        Given at en tom HR-fil er tilgængelig
        When systemet udfoerer sin automatiske file load
        Then forbliver det eksisterende antal medarbejdere uændret

    Scenario: Initialer har maks fire bogstaver
        Given at en HR-fil med initialer af varierende længde er tilgængelig
        When systemet udfoerer sin automatiske file load
        Then accepteres initialer med op til 4 bogstaver
        And afvises initialer med mere end 4 bogstaver
