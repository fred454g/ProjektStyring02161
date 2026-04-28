Feature: Registrer tid (dagligt)
    For at holde styr på det faktiske tidsforbrug
    Som en medarbejder
    Vil jeg gerne kunne registrere mine brugte timer på en aktivitet dagligt

    Background:
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And at medarbejderen "huba" er tilknyttet projekt "26001"
        And at aktiviteten "Frontend" findes på projekt "26001"
        And at medarbejderen "huba" er tilknyttet aktiviteten "Frontend" på projekt "26001"

    Scenario: Hovedscenarie - Daglig tidsregistrering
        Given at medarbejderen "huba" er logget ind i systemet
        When medarbejderen registrerer 4.5 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then er 4.5 timer tilføjet til det samlede tidsforbrug for "huba" på aktiviteten "Frontend" på projekt "26001"

    Scenario: Fejlscenarie - Tidsregistrering uden at være logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen registrerer 4.5 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Medarbejder kan registrere tid uden direkte aktivitetstilknytning
        When medarbejderen registrerer 2.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then er 2.0 timer tilføjet til det samlede tidsforbrug for "jfk" på aktiviteten "Frontend" på projekt "26001"

    Scenario: Fejlscenarie - Tidsregistrering på ikke-eksisterende aktivitet
        Given at medarbejderen "huba" er logget ind i systemet
        When medarbejderen registrerer 4.5 timer på aktiviteten "Database" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Aktivitet findes ikke i projekt"

    Scenario: Fejlscenarie - Ugyldigt antal timer (nul eller negativ)
        Given at medarbejderen "huba" er logget ind i systemet
        When medarbejderen registrerer -2.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Antal timer skal være større end 0"

    Scenario: Fejlscenarie - For mange timer på en dag
        Given at medarbejderen "huba" er logget ind i systemet
        When medarbejderen registrerer 25.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Antal timer kan ikke overstige 24 timer per dag"