Feature: Tilfoej medarbejder til projekt
    For at samle det rette team til at udføre projektet
    Som en medarbejder
    Vil jeg gerne kunne tilføje andre medarbejdere til et projekt

    Background: Grunddata for tilføjelse af medarbejder til projekt
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"

    Scenario: Fejlscenarie 1 - Brugeren er ikke logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen tilfoejer "huba" til projekt "26001"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Projekt findes ikke
        When medarbejderen tilfoejer "huba" til projekt "26002"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie 3 - Medarbejederen findes ikke
        When medarbejderen tilfoejer "Ukendt" til projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder med initialer Ukendt findes ikke i systemet"

    Scenario: Fejlscenarie 4 - Medarbejderen er allerede tilknyttet projektet
        Given at medarbejderen "huba" er tilknyttet projekt "26001"
        When medarbejderen tilfoejer "huba" til projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder er allerede tilknyttet projekt"

    Scenario: Sccesscenarie - Tilføjelse af medarbejder til projekt
        When medarbejderen tilfoejer "huba" til projekt "26001"
        Then giver systenet ingen fejlmeddelelse
        And fremgår "huba" af listen over tilknyttede medarbejdere på projekt "26001"
