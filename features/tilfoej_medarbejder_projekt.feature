Feature: Tilføj medarbejder til projekt
    For at samle det rette team til at udføre projektet
    Som en medarbejder
    Vil jeg gerne kunne tilføje andre medarbejdere til et projekt
    Background: Tilføj medarbejdere
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"

    Scenario: Hovedscenarie - Tilføjelse af medarbejder til projekt
        Given at projektet "26001" findes i systemet
        And at medarbejderen med initialerne "huba" findes i systemet
        When medarbejderen tilfoejer "huba" til projekt "26001"
        Then fremgår "huba" af listen over tilknyttede medarbejdere på projekt "26001"

    Scenario: Fejlscenarie - Medarbejder allerede tilknyttet projekt
        Given medarbejderen tilfoejer "huba" til projekt "26001"
        When medarbejderen forsoeger at tilfoejer "huba" til projekt "26001" igen
        Then giver systemet fejlmeddelelsen "Medarbejder er allerede tilknyttet projekt"

    Scenario: Fejlscenarie - Projekt findes ikke
        When medarbejderen forsoeger at tilfoejer "huba" til projekt "99999"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie - Medarbejder findes ikke
        When medarbejderen forsoeger at tilfoejer "xxxx" til projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder med initialer xxxx findes ikke i systemet"

    Scenario: Fejlscenarie - Tilføjelse uden at være logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen forsoeger at tilfoejer "huba" til projekt "26001"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Tilføjelse af flere medarbejdere til projekt
        Given at medarbejderen "anda" tilfoejes til systemet
        When medarbejderen tilfoejer "huba" til projekt "26001"
        And medarbejderen tilfoejer "anda" til projekt "26001"
        Then fremgår "huba" af listen over tilknyttede medarbejdere på projekt "26001"
        And fremgår "anda" af listen over tilknyttede medarbejdere på projekt "26001"
