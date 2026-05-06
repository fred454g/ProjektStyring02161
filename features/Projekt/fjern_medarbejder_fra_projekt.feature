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

    # Jacob
    Scenario: Fejlscenarie - Projektet findes ikke
        When medarbejderen fjerner "huba" fra projekt "96001"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"
    
    # Jacob
    Scenario: Fejlscenarie - Medarbejederen findes ikke
        When medarbejderen fjerner "Ukendt" fra projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder med initialer Ukendt findes ikke i systemet"
    
    # Jacob
    Scenario: Fejlscenarie - Medarbejederen er ikke tilknyttet projekt
        When medarbejderen fjerner "jfk" fra projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder er ikke tilknyttet projekt"
    
    Scenario: Hovedscenarie - Fjern medarbejder fra projekt
        Given at projektet "26001" findes i systemet
        And at medarbejderen med initialerne "huba" findes i systemet
        And fremgår "huba" af listen over tilknyttede medarbejdere på projekt "26001"
        When medarbejderen fjerner "huba" fra projekt "26001"
        Then fremgår "huba" ikke længere af listen over tilknyttede medarbejdere på projekt "26001"
        And udsendes observer-eventen "MEDARBEJDER_FJERNET_PROJEKT"
