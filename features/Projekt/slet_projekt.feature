Feature: Slet projekt
Som medarbejder vil jeg gerne kunne slette et projekt fra applikationen når projektet er fuldført

    Background: At der findes et projekt
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Projekt_1"
        And medarbejderen opretter et projekt med navnet "Projekt_2"

    Scenario: Fejlscenarie 1 - Brugeren er ikke logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen forsoeger at slette projektet med nummeret "26001"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Projekt er ikke valgt
        When medarbejderen forsoeger at slette projektet med nummeret ""
        Then giver systemet fejlmeddelelsen "Projekt skal vælges"

    Scenario: Fejlscenarie 3 - Projekt findes ikke
        When medarbejderen forsoeger at slette projektet med nummeret "96001"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Sccesscenarie - Sletning af projekt lykkes
        When medarbejderen forsoeger at slette projektet med nummeret "26001"
        Then giver systenet ingen fejlmeddelelse
        And er projektet med nummeret "26001" slettet fra applikationen
        And at projektet "26002" med navnet "Projekt_2" findes i systemet