Feature: Tilknyt og fjern medarbejder på aktivitet
    For at fordele specifikke opgaver i projektet
    Som en medarbejder
    Vil jeg gerne kunne slå medarbejdere til og fra på en specifik aktivitet
    Background: Tilføj jfk og huba
        Given at medarbejderen "jfk" med navn "John F. Kennedy" tilfoejes til systemet
        And at medarbejderen "huba" med navn "Hubert Baumeister" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And at medarbejderen "huba" er tilknyttet projekt "26001"
        And at aktiviteten "Backend API" findes på projekt "26001"


    Scenario: Hovedscenarie - Tilknyt medarbejder til aktivitet
        When medarbejderen tilknytter "huba" til aktiviteten "Backend API" på projekt "26001"
        Then er "huba" tilknyttet aktiviteten "Backend API" på projekt "26001"

    Scenario: Hovedscenarie - Fjern medarbejder fra aktivitet
        And at medarbejderen "huba" er tilknyttet aktiviteten "Backend API" på projekt "26001"
        When medarbejderen fjerner "huba" fra aktiviteten "Backend API" på projekt "26001"
        Then er "huba" ikke længere tilknyttet aktiviteten "Backend API" på projekt "26001"