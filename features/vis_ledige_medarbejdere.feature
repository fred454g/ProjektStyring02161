Feature: Vis ledige medarbejdere
    For at kunne allokere ressourcer effektivt
    Som en medarbejder
    Vil jeg gerne kunne se en liste over hvilke medarbejdere der er ledige i en given periode

    Background:
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "anda" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"

    Scenario: Hovedscenarie - Søgning efter ledige medarbejdere
        When medarbejderen søger efter ledige medarbejdere i uge 12
        Then viser systemet en liste over medarbejdere der er ledige i uge 12
        And fremgår "huba" af listen over ledige medarbejdere
        And fremgår "anda" af listen over ledige medarbejdere

    Scenario: Medarbejder med fravær vises ikke som ledig
        Given at medarbejderen "huba" er logget ind i systemet
        And medarbejderen registrerer fravær af typen "Ferie" fra uge 12 til uge 14
        And at medarbejderen "jfk" er logget ind i systemet
        When medarbejderen søger efter ledige medarbejdere i uge 13
        Then fremgår "huba" ikke af listen over ledige medarbejdere
        And fremgår "anda" af listen over ledige medarbejdere

    Scenario: Medarbejder med fuld booking vises ikke som ledig
        Given at medarbejderen "huba" er tilknyttet projekt "26001"
        And at aktiviteten "Backend" findes på projekt "26001"
        And at medarbejderen "huba" er tilknyttet aktiviteten "Backend" på projekt "26001"
        And medarbejderen angiver startuge 10, slutuge 14 og estimeret tid 200.0 timer for aktiviteten "Backend" på projekt "26001"
        When medarbejderen søger efter ledige medarbejdere i uge 12
        Then fremgår "anda" af listen over ledige medarbejdere

    Scenario: Alle medarbejdere ledige når ingen er allokeret
        When medarbejderen søger efter ledige medarbejdere i uge 40
        Then viser systemet en liste over medarbejdere der er ledige i uge 40
        And fremgår "jfk" af listen over ledige medarbejdere
        And fremgår "huba" af listen over ledige medarbejdere
        And fremgår "anda" af listen over ledige medarbejdere

    Scenario: Fejlscenarie - Søgning uden at være logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen søger efter ledige medarbejdere i uge 12
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"
