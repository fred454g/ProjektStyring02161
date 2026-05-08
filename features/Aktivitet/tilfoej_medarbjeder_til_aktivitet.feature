# Jacob

Feature: Tilknyt medarbejder på aktivitet

    Background: Tilføj jfk og huba
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And at medarbejderen "huba" er tilknyttet projekt "26001"
        And at aktiviteten "Backend API" findes på projekt "26001"

    Scenario: Fejlscenarie - Projektet findes ikke
        When medarbejderen tilfoejer "huba" til aktiviteten "Backend API" paa projekt "96001"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie - Medarbejederen findes ikke
        When medarbejderen tilfoejer "Ukendt" til aktiviteten "Backend API" paa projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder med initialer Ukendt findes ikke i systemet"

    Scenario: Fejlscenarie - Medarbejederen er ikke tilknyttet projekt
        When medarbejderen tilfoejer "jfk" til aktiviteten "Backend API" paa projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder ikke tilknyttet projekt"

    Scenario: Fejlscenarie - Medarbejederen er ikke tilknyttet projekt
        When medarbejderen tilfoejer "huba" til aktiviteten "Frontend" paa projekt "26001"
        Then giver systemet fejlmeddelelsen "Aktivitet findes ikke i projekt"

    # Scenario: Fejlscenarie - Aktiviteten findes ikke
    #     And at medarbejderen "huba" er tilknyttet aktiviteten "Backend API" på projekt "26001"
    #     And at medarbejderen "jfk" er tilknyttet projekt "26001"
    #     When medarbejderen tilfoejer "jfk" til aktiviteten "ukendt" paa projekt "26001"
    #     Then giver systemet fejlmeddelelsen "Medarbejder er ikke i aktivitet"

    Scenario: Hovedscenarie - Tilknyt medarbejder til aktivitet
        When medarbejderen tilfoejer "huba" til aktiviteten "Backend API" paa projekt "26001"
        Then er "huba" tilknyttet aktiviteten "Backend API" på projekt "26001"
