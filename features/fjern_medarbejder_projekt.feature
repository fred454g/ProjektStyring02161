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

    Scenario: Hovedscenarie - Fjern medarbejder fra projekt
        Given at projektet "26001" findes i systemet
        And at medarbejderen med initialerne "huba" findes i systemet
        And fremgår "huba" af listen over tilknyttede medarbejdere på projekt "26001"
        When medarbejderen fjerner "huba" fra projekt "26001"
        Then fremgår "huba" ikke længere af listen over tilknyttede medarbejdere på projekt "26001"
        And udsendes observer-eventen "MEDARBEJDER_FJERNET_PROJEKT"
