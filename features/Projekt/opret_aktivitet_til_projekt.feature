# Jeppe
Feature: Opret aktivitet til projekt
    For at kunne planlægge arbejdet på et projekt
    Som en medarbejder
    Vil jeg gerne kunne oprette aktiviteter på et projekt

    Background: Grunddata for oprettelse af aktivitet til projekt
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"

    Scenario: Fejlscenarie 1 - Brugeren er ikke logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med budgetteret tid 10.0 starttidspunkt 1 sluttidspunkt 2
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Projekt er ikke valgt
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "" med budgetteret tid 0.0 starttidspunkt 1 sluttidspunkt 2
        Then giver systemet fejlmeddelelsen "Projekt skal vælges"

    Scenario: Fejlscenarie 3 - Aktivitet er ikke valgt
        When medarbejderen opretter aktiviteten "" paa projekt "26001" med budgetteret tid 0.0 starttidspunkt 1 sluttidspunkt 2
        Then giver systemet fejlmeddelelsen "Aktivitet skal vælges"

    Scenario: Fejlscenarie 4- Budgetteret tid er negativ (Forventede antal arbejdstimer)
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med budgetteret tid -1.0 starttidspunkt 1 sluttidspunkt 2
        Then giver systemet fejlmeddelelsen "Budgetteret tid må ikke være negativ"

    Scenario: Fejlscenarie 5 - Startuge er efter slutuge
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med budgetteret tid 0.0 starttidspunkt 50 sluttidspunkt 2
        Then giver systemet fejlmeddelelsen "Startuge kan ikke være efter slutuge"

    Scenario: Fejlscenarie 6 - Projektet findes ikke
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "96001" med budgetteret tid 0.0 starttidspunkt 1 sluttidspunkt 2
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie 7 - Aktivitetsnavn er allerede i brug
        Given at aktiviteten "Backend API" findes på projekt "26001"
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med budgetteret tid 0.0 starttidspunkt 1 sluttidspunkt 2
        Then giver systemet fejlmeddelelsen "Aktivitetsnavn er i brug"

    Scenario: Succescenarie - Opret aktivitet til projekt
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med budgetteret tid 50.0 starttidspunkt 10 sluttidspunkt 14
        Then giver systenet ingen fejlmeddelelse
        And er aktiviteten "Backend API" oprettet på projekt "26001"
        And aktiviteten "Backend API" på projekt "26001" har startuge 10, slutuge 14 og estimeret tid 50.0 timer