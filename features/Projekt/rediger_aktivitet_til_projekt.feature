Feature: Rediger aktivitet til projekt

    Background: Tilføj jfk
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And at aktiviteten "Backend API" findes på projekt "Nyt IT System"

    Scenario: Fejlscenarie 1 - Brugeren er ikke logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen redigerer aktiviteten "Backend API" paa projekt "26001" til budgetteret tid 67.0 starttidspunkt 8 sluttidspunkt 9
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Projekt er ikke valgt
        When medarbejderen redigerer aktiviteten "Backend API" paa projekt "" til budgetteret tid 67.0 starttidspunkt 8 sluttidspunkt 9
        Then giver systemet fejlmeddelelsen "Projekt skal vælges"

    Scenario: Fejlscenarie 3 - Forventede antal arbejdstimer er nagativt
        When medarbejderen redigerer aktiviteten "Backend API" paa projekt "26001" til budgetteret tid -67.0 starttidspunkt 8 sluttidspunkt 9
        Then giver systemet fejlmeddelelsen "Budgetteret tid må ikke være negativ"

    Scenario: Fejlscenarie 4 - Start og slutuge er ikke korrekt angivet
        When medarbejderen redigerer aktiviteten "Backend API" paa projekt "26001" til budgetteret tid 67.0 starttidspunkt 18 sluttidspunkt 9
        Then giver systemet fejlmeddelelsen "Startuge kan ikke være efter slutuge"

    Scenario: Fejlscenarie 5 - Projektet findes ikke
        When medarbejderen redigerer aktiviteten "Backend API" paa projekt "96001" til budgetteret tid 67.0 starttidspunkt 8 sluttidspunkt 9
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie 6 - Aktivitet findes ikke
        When medarbejderen redigerer aktiviteten "Ukendt Aktivitet" paa projekt "26001" til budgetteret tid 67.0 starttidspunkt 8 sluttidspunkt 9
        Then giver systemet fejlmeddelelsen "Aktivitet findes ikke"

    Scenario: Sccesscenarie - Aktiviteten redigeres
        When medarbejderen redigerer aktiviteten "Backend API" paa projekt "26001" til budgetteret tid 67.0 starttidspunkt 8 sluttidspunkt 9
        Then giver systenet ingen fejlmeddelelse
        And er aktiviteten "Backend API" oprettet på projekt "26001"
        And aktiviteten "Backend API" på projekt "26001" har startuge 8, slutuge 9 og estimeret tid 67.0 timer