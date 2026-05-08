Feature: Rediger projektnavn
    For at kunne rette fejl eller opdatere projektets formål 
    Som en medarbejder
    Vil jeg gerne kunne ændre navnet på et eksisterende projekt

    Background: At der findes et projekt
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"

    Scenario: Fejlscenarie 1 - Brugeren er ikke at være logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen forsoeger at ændre navnet på projekt "26001" til "Nyt Navn"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Intet projekt er ikke valgt
        When medarbejderen forsoeger at ændre navnet på projekt "" til "Nyt Navn"
        Then giver systemet fejlmeddelelsen "Projekt skal vælges"

    Scenario: Fejlscenarie 3 - Tomt nyt projektnavn
        When medarbejderen forsoeger at ændre navnet på projekt "26001" til ""
        Then giver systemet fejlmeddelelsen "Nyt projektnavn må ikke være tomt"

    Scenario: Fejlscenarie 4 - Det valgte projekt findes ikke
        When medarbejderen forsoeger at ændre navnet på projekt "99999" til "Nyt Navn"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Sccesscenarie - Omdoebning af projekt lykkes
        When medarbejderen forsoeger at ændre navnet på projekt "26001" til "Nyt Navn"
        Then giver systenet ingen fejlmeddelelse
        And er projektets navn opdateret til "Nyt Navn" i systemet
