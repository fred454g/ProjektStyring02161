# Jaocb

Feature: Opret aktivitet til projekt

    Background: Tilføj jfk
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"

    Scenario: Fejlscenarie - Projekt er ikke valgt i GUI
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "" med data FAA "0.0" Start "1" Slut "2"
        Then giver systemet fejlmeddelelsen "Projekt skal vælges"

    Scenario: Fejlscenarie - Aktivitet er ikke valgt i GUI
        When medarbejderen opretter aktiviteten "" paa projekt "26001" med data FAA "0.0" Start "1" Slut "2"
        Then giver systemet fejlmeddelelsen "Aktivitet skal vælges"

    Scenario: Fejlscenarie - Forventede antal arbejdstimer er nagativt
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med data FAA "-1.0" Start "1" Slut "2"
        Then giver systemet fejlmeddelelsen "Budgetteret tid må ikke være negativ"

    Scenario: Fejlscenarie - Projektet findes ikke
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "96001" med data FAA "1.0" Start "1" Slut "2"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie - Aktivitetsnavn er allerede i brug
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med data FAA "1.0" Start "1" Slut "2"
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med data FAA "1.0" Start "1" Slut "2"
        Then giver systemet fejlmeddelelsen "Aktivitetsnavn er i brug"

    Scenario: Hovedscenarie - Opret aktivitet til projekt
        Given at projektet "26001" findes i systemet
        When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med data FAA "10.0" Start "1" Slut "2"
        And medarbejderen angiver startuge 10, slutuge 14 og estimeret tid 50.0 timer for aktiviteten "Backend API" på projekt "26001"
        Then er aktiviteten "Backend API" oprettet på projekt "26001"
        And aktiviteten "Backend API" på projekt "26001" har startuge 10, slutuge 14 og estimeret tid 50.0 timer