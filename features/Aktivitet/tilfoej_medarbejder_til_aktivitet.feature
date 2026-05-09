Feature: Tilkfoej medarbejder til aktivitet

    Background: Grunddata for tilknytning af medarbejder til aktivitet
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And at medarbejderen "huba" er tilknyttet projekt "26001"
        And at aktiviteten "Backend API" findes på projekt "26001"

    Scenario: Fejlscenarie 1 - Brugeren er ikke logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen tilfoejer "huba" til aktiviteten "Backend API" paa projekt "26001"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Projektet findes ikke
        When medarbejderen tilfoejer "huba" til aktiviteten "Backend API" paa projekt "96001"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie 3 - Medarbejederen findes ikke
        When medarbejderen tilfoejer "Ukendt" til aktiviteten "Backend API" paa projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder med initialer Ukendt findes ikke i systemet"

    Scenario: Fejlscenarie 4 - Medarbejederen er ikke tilknyttet projekt
        When medarbejderen tilfoejer "jfk" til aktiviteten "Backend API" paa projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder ikke tilknyttet projekt"

    Scenario: Fejlscenarie 5 - Aktiviteten findes ikke i projektet
        When medarbejderen tilfoejer "huba" til aktiviteten "Frontend" paa projekt "26001"
        Then giver systemet fejlmeddelelsen "Aktivitet findes ikke i projekt"

    Scenario: Fejlscenarie 6 - Medarbejder er allerede tilknyttet aktivitet
        When medarbejderen tilfoejer "huba" til aktiviteten "Backend API" paa projekt "26001"
        Then giver systenet ingen fejlmeddelelse
        When medarbejderen tilfoejer "huba" til aktiviteten "Backend API" paa projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder er allerede tilknyttet aktivitet"

    Scenario: Succescenarie - Tilknyt medarbejder til aktivitet
        When medarbejderen tilfoejer "huba" til aktiviteten "Backend API" paa projekt "26001"
        Then giver systenet ingen fejlmeddelelse
        And er "huba" tilknyttet aktiviteten "Backend API" på projekt "26001"
