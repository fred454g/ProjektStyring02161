# Jaocb
Feature: Slet aktivitet fra projekt

    Background: Tilføj jfk
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And at aktiviteten "Backend API" findes på projekt "Nyt IT System"
    
    Scenario: Fejlscenarie 1 - Brugeren er ikke logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen fjerner aktiviteten "Backend API" på projekt "26001"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Projekt er ikke valgt
        When medarbejderen fjerner aktiviteten "Backend API" på projekt ""
        Then giver systemet fejlmeddelelsen "Projekt skal vælges"

    Scenario: Fejlscenarie 3 - Aktivitet er ikke valgt i GUI
        When medarbejderen fjerner aktiviteten "" på projekt "26001"
        Then giver systemet fejlmeddelelsen "Aktivitet skal vælges"

    Scenario: Fejlscenarie 4 - Projektet findes ikke
        When medarbejderen fjerner aktiviteten "Backend API" på projekt "96001"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie 5 - Aktivitet findes ikke
        When medarbejderen fjerner aktiviteten "Ukendt Aktivitet" på projekt "26001"
        Then giver systemet fejlmeddelelsen "Aktivitet findes ikke"

    Scenario: Sccesscenarie - Slet aktivitet fra projekt
        When medarbejderen fjerner aktiviteten "Backend API" på projekt "26001"
        Then giver systenet ingen fejlmeddelelse
        And er aktiviteten "Backend API" ikke tilknyttet projekt "26001"