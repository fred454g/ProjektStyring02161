Feature: Tilknyt eller ændr projektleder
    For at sikre ansvar og styring af et projekt
    Som en medarbejder
    Vil jeg gerne kunne tilknytte eller ændre projektlederen for et projekt
    Background: Tilføj medarbejdere
        Given at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "anda" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"

    Scenario: Hovedscenarie - Tilknytning af projektleder lykkes
        Given at projektet "26001" findes i systemet
        And at medarbejderen med initialerne "huba" findes i systemet
        When medarbejderen tilknytter "huba" som projektleder til projekt "26001"
        Then er "huba" registreret som projektleder for projekt "26001"

    Scenario: Ændring af projektleder
        Given at projektet "26001" findes i systemet
        And at medarbejderen med initialerne "huba" findes i systemet
        And medarbejderen tilknytter "huba" som projektleder til projekt "26001"
        When medarbejderen tilknytter "anda" som projektleder til projekt "26001"
        Then er "anda" registreret som projektleder for projekt "26001"
        And er "huba" ikke længere projektleder for projekt "26001"

    Scenario: Fejlscenarie - Projekt findes ikke
        When medarbejderen forsoeger at tilknytte "huba" som projektleder til projekt "99999"
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie - Medarbejder findes ikke
        When medarbejderen forsoeger at tilknytte "xxxx" som projektleder til projekt "26001"
        Then giver systemet fejlmeddelelsen "Medarbejder findes ikke"

    Scenario: Fejlscenarie - Tilknytning uden at være logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen forsoeger at tilknytte "huba" som projektleder til projekt "26001"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"
