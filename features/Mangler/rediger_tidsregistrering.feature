Feature: Rediger tidsregistrering
    For at kunne rette fejl i allerede registrerede timer
    Som en medarbejder
    Vil jeg gerne kunne rette i mine tidsregistreringer

    Background: Grunddata for redigering af tidsregistrering
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And at medarbejderen "huba" er tilknyttet projekt "26001"
        And at aktiviteten "Frontend" findes på projekt "26001"
        And at medarbejderen "huba" er tilknyttet aktiviteten "Frontend" på projekt "26001"
        And at medarbejderen "huba" er logget ind i systemet
        And medarbejderen registrerer 4.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato

    Scenario: Hovedscenarie - Ret registreret tid
        When medarbejderen retter tidsregistrering til 6.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then er det samlede tidsforbrug for "huba" på aktiviteten "Frontend" på projekt "26001" nu 10.0 timer

    Scenario: Ret registreret tid ned
        When medarbejderen retter tidsregistrering til 2.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then er det samlede tidsforbrug for "huba" på aktiviteten "Frontend" på projekt "26001" nu 6.0 timer

    Scenario: Fejlscenarie - Ret til negativt antal timer
        When medarbejderen retter tidsregistrering til -1.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Antal timer skal være større end 0"

    Scenario: Fejlscenarie - Redigering uden at være logget ind
        Given at medarbejderen "huba" er logget ud
        When medarbejderen retter tidsregistrering til 6.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie - Redigering på ikke-eksisterende aktivitet
        When medarbejderen retter tidsregistrering til 6.0 timer på aktiviteten "Database" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Aktivitet findes ikke i projekt"

    Scenario: Ret registreret tid til nul (slet registrering)
        When medarbejderen retter tidsregistrering til 0.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then er det samlede tidsforbrug for "huba" på aktiviteten "Frontend" på projekt "26001" nu 4.0 timer
