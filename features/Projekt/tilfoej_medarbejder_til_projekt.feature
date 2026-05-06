Feature: Tilføj medarbejder til projekt
    For at samle det rette team til at udføre projektet
    Som en medarbejder
    Vil jeg gerne kunne tilføje andre medarbejdere til et projekt

    Background: Tilføj huba
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"

    # Jacob
    Scenario: Fejlscenarie - Projektet findes ikke
        When medarbejderen tilfoejer "huba" til projekt "26002"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    # Jacob
    Scenario: Fejlscenarie - Medarbejederen findes ikke
        When medarbejderen tilfoejer "Ukendt" til projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder med initialer Ukendt findes ikke i systemet"

    # Jacob
    Scenario: Fejlscenarie - Medarbejederen er ikke tilknyttet projekt
        And medarbejderen tilfoejer "huba" til projekt "26001"
        When medarbejderen tilfoejer "huba" til projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder er allerede tilknyttet projekt"

    Scenario: Hovedscenarie - Tilføjelse af medarbejder til projekt
        Given at projektet "26001" findes i systemet
        And at medarbejderen med initialerne "huba" findes i systemet
        When medarbejderen tilfoejer "huba" til projekt "26001"
        Then fremgår "huba" af listen over tilknyttede medarbejdere på projekt "26001"