Feature: Tilknyt eller ændr projektleder
    For at sikre ansvar og styring af et projekt
    Som en medarbejder
    Vil jeg gerne kunne tilknytte eller ændre projektlederen for et projekt
    Background: Tilføj huba
    Given at medarbejderen "huba" med navn "Hubert Baumeister" tilfoejes til systemet
    And at medarbejderen er logget ind i systemet
    And medarbejderen opretter et projekt med navnet "Nyt IT System"

    Scenario: Hovedscenarie - Tilknytning af projektleder lykkes
        Given at medarbejderen er logget ind i systemet
        And at projektet "26001" findes i systemet
        And at medarbejderen med initialerne "huba" findes i systemet
        When medarbejderen tilknytter "huba" som projektleder til projekt "26001"
        Then er "huba" registreret som projektleder for projekt "26001"