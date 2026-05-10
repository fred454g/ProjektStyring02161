# Jacob
Feature: Opdater projekt med projektleder
    For at sikre ansvar og styring af et projekt
    Som en medarbejder
    Vil jeg gerne kunne tilknytte eller ændre projektlederen for et projekt
    
    Background: Grunddata for tilknytning eller ændring af projektleder
        Given at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "anda" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"       

    Scenario: Fejlscenarie 1 - Tilknytning af projektleder uden at være logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen forsoeger at tilknytte "huba" som projektleder til projekt "26001"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Projekt er ikke valgt
        When medarbejderen forsoeger at tilknytte "huba" som projektleder til projekt ""
        Then giver systemet fejlmeddelelsen "Projekt skal vælges"

    Scenario: Fejlscenarie 3 - Projektleder er ikke valgt
        When medarbejderen forsoeger at tilknytte "" som projektleder til projekt "Nyt IT System"
        Then giver systemet fejlmeddelelsen "Projektleder skal vælges"

    Scenario: Fejlscenarie 4 - Projekt findes ikke
        When medarbejderen forsoeger at tilknytte "huba" som projektleder til projekt "99999"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie 5 - Medarbejder findes ikke
        When medarbejderen forsoeger at tilknytte "xxxx" som projektleder til projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder findes ikke"

    Scenario: Sccesscenarie - Tilknytning af projektleder lykkes
        When medarbejderen forsoeger at tilknytte "huba" som projektleder til projekt "26001"
        Then er "huba" registreret som projektleder for projekt "26001"

    Scenario: Succescenarie - Ændring af projektleder
        Given medarbejderen forsoeger at tilknytte "huba" som projektleder til projekt "26001"
        Then giver systenet ingen fejlmeddelelse
        When medarbejderen forsoeger at tilknytte "anda" som projektleder til projekt "26001"
        Then giver systenet ingen fejlmeddelelse
        And er "anda" registreret som projektleder for projekt "26001"
        And er "huba" ikke længere projektleder for projekt "26001"