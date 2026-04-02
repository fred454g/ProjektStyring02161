Feature: Rediger projektnavn
    For at kunne rette fejl eller opdatere projektets formål
    Som en medarbejder
    Vil jeg gerne kunne ændre navnet på et eksisterende projekt
    Background: At der findes et projekt
        Given at medarbejderen er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"

    Scenario: Hovedscenarie - Ændring af projektnavn lykkes
        Given at medarbejderen er logget ind i systemet
        And at projektet "26001" med navnet "Nyt IT System" findes i systemet
        When medarbejderen ændrer navnet på projekt "26001" til "Nyt Navn"
        Then er projektets navn opdateret til "Nyt Navn" i systemet