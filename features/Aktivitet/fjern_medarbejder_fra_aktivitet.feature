# Jacob

Feature: fjern medarbejder fra aktivitet

    Background: Tilføj jfk og huba
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And at medarbejderen "huba" er tilknyttet projekt "26001"
        And at aktiviteten "Backend API" findes på projekt "26001"

    Scenario: Fejlscenarie 1 - Brugeren er ikke logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen fjerner "huba" fra aktiviteten "Backend API" på projekt "26001"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Projektet findes ikke
        When medarbejderen fjerner "huba" fra aktiviteten "Backend API" på projekt "96001"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie 3 - Medarbejederen findes ikke
        When medarbejderen fjerner "Ukendt" fra aktiviteten "Backend API" på projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder med initialer Ukendt findes ikke i systemet"

    Scenario: Fejlscenarie 4- Medarbejederen er ikke tilknyttet projekt
        When medarbejderen fjerner "jfk" fra aktiviteten "Backend API" på projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder ikke tilknyttet projekt"

    Scenario: Fejlscenarie 5 - Medarbejederen er ikke tilknyttet projekt
        When medarbejderen fjerner "huba" fra aktiviteten "Frontend" på projekt "26001"
        Then giver systemet fejlmeddelelsen "Aktivitet findes ikke i projekt"

    Scenario: Fejlscenarie 6 - Aktiviteten findes ikke
        And at medarbejderen "huba" er tilknyttet aktiviteten "Backend API" på projekt "26001"
        And at medarbejderen "jfk" er tilknyttet projekt "26001"
        When medarbejderen fjerner "jfk" fra aktiviteten "Backend API" på projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder er ikke i aktivitet"

    Scenario: Sccesscenarie - Fjern medarbejder fra aktivitet
        And at medarbejderen "huba" er tilknyttet aktiviteten "Backend API" på projekt "26001"
        When medarbejderen fjerner "huba" fra aktiviteten "Backend API" på projekt "26001"
        Then giver systenet ingen fejlmeddelelse
        And er "huba" ikke længere tilknyttet aktiviteten "Backend API" på projekt "26001"

