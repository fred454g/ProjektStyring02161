Feature: Opret aktivitet og angiv estimater
    For at kunne nedbryde et projekt og planlægge arbejdet
    Som en medarbejder
    Vil jeg gerne kunne oprette en aktivitet og angive start/slut uge samt estimeret tid
    Background: Tilføj jfk
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"

    Scenario: Hovedscenarie - Oprettelse og estimering af aktivitet
        Given at projektet "26001" findes i systemet
        When medarbejderen opretter aktiviteten "Backend API" på projekt "26001"
        And medarbejderen angiver startuge 10, slutuge 14 og estimeret tid 50.0 timer for aktiviteten "Backend API" på projekt "26001"
        Then er aktiviteten "Backend API" oprettet på projekt "26001"
        And aktiviteten "Backend API" på projekt "26001" har startuge 10, slutuge 14 og estimeret tid 50.0 timer
    
    Scenario: Fjernelse af aktivitet
        Given at projektet "26001" findes i systemet
        And at aktiviteten "Backend API" er oprettet på projekt "26001"
        When medarbejderen fjerner aktiviteten "Backend API" på projekt "26001"
        Then er aktiviteten "Backend API" ikke tilknyttet projekt "26001"
        And udsendes observer-eventen "AKTIVITET_SLETTET"