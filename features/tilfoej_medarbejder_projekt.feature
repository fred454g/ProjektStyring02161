Feature: Tilføj medarbejder til projekt
    For at samle det rette team til at udføre projektet
    Som en medarbejder
    Vil jeg gerne kunne tilføje andre medarbejdere til et projekt
    Background: Tilføj huba
        Given at medarbejderen "jfk" med navn "John F. Kennedy" tilfoejes til systemet
        And at medarbejderen "huba" med navn "Hubert Baumeister" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"

    Scenario: Hovedscenarie - Tilføjelse af medarbejder til projekt
        Given at projektet "26001" findes i systemet
        And at medarbejderen med initialerne "huba" findes i systemet
        When medarbejderen tilfoejer "huba" til projekt "26001"
        Then fremgår "huba" af listen over tilknyttede medarbejdere på projekt "26001"