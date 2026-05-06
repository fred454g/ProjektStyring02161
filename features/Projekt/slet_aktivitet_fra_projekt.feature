# Jaocb
Feature: Slet aktivitet fra projekt

    Background: Tilføj jfk
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
    
    Scenario: Fejlscenarie - Projektet findes ikke
        When medarbejderen fjerner aktiviteten "Backend API" på projekt "96001"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie - Aktivitet findes ikke
        When medarbejderen fjerner aktiviteten "Ukendt Aktivitet" på projekt "26001"
        Then giver systemet fejlmeddelelsen "Aktivitet findes ikke"

    Scenario: Fejlscenarie - Aktivitet er ikke valgt i GUI
        When medarbejderen fjerner aktiviteten "" på projekt "26001"
        Then giver systemet fejlmeddelelsen "Aktivitet skal vælges"

    Scenario: Fejlscenarie - Projekt er ikke valgt i GUI
        When medarbejderen fjerner aktiviteten "Backend API" på projekt ""
        Then giver systemet fejlmeddelelsen "Projekt skal vælges"

    Scenario: Hovedscenarie - Slet aktivitet fra projekt
        Given at projektet "26001" findes i systemet
        And at aktiviteten "Backend API" er oprettet på projekt "26001"
        When medarbejderen fjerner aktiviteten "Backend API" på projekt "26001"
        Then er aktiviteten "Backend API" ikke tilknyttet projekt "26001"
        And udsendes observer-eventen "AKTIVITET_SLETTET"