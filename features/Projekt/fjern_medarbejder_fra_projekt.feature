Feature: Fjern medarbejder fra projekt
    For at holde projektteamet opdateret
    Som en medarbejder
    Vil jeg gerne kunne fjerne en medarbejder fra et projekt

    Background:
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And medarbejderen tilfoejer "huba" til projekt "26001"

    Scenario: Fejlscenarie 1 - Brugeren er ikke logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen fjerner "huba" fra projekt "26001"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Projektet findes ikke
        When medarbejderen fjerner "huba" fra projekt "96001"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"
    
    Scenario: Fejlscenarie 3 - Medarbejederen findes ikke
        When medarbejderen fjerner "Ukendt" fra projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder med initialer Ukendt findes ikke i systemet"
    
    Scenario: Fejlscenarie 4 - Medarbejederen er ikke tilknyttet projekt
        When medarbejderen fjerner "jfk" fra projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder er ikke tilknyttet projekt"
    
    Scenario: Sccesscenarie - Fjern medarbejder fra projekt
        When medarbejderen fjerner "huba" fra projekt "26001"
        Then giver systenet ingen fejlmeddelelse
        And fremgår "huba" ikke længere af listen over tilknyttede medarbejdere på projekt "26001"
