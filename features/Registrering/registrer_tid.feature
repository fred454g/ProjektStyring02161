Feature: Registrer tid (dagligt)
    For at holde styr på det faktiske tidsforbrug
    Som en medarbejder
    Vil jeg gerne kunne registrere mine brugte timer på en aktivitet dagligt

    Background:
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "huba" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And at aktiviteten "Frontend" findes på projekt "26001"

    Scenario: Fejlscenarie 1 - Tidsregistrering uden at være logget ind
        Given at medarbejderen "huba" er logget ud
        When medarbejderen registrerer 4.5 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Projekt er ikke valgt
        When medarbejderen registrerer 4.5 timer på aktiviteten "Frontend" på projekt "" for dags dato
        Then giver systemet fejlmeddelelsen "Projekt skal vælges"

    Scenario: Fejlscenarie 3 - Aktivitet findes ikke
        When medarbejderen registrerer 4.5 timer på aktiviteten "" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Aktivitet skal vælges"

    Scenario: Fejlscenarie 4 - Ugyldigt antal timer (nul eller negativ)
        When medarbejderen registrerer -2.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Antal timer skal være større end 0"

    Scenario: Fejlscenarie 5 - For mange timer på en dag
        When medarbejderen registrerer 25.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Antal timer kan ikke overstige 24 timer per dag"

    Scenario: Fejlscenarie 6 - Tidsregistrering på ikke-eksisterende projekt
        When medarbejderen registrerer 4.5 timer på aktiviteten "Frontend" på projekt "99999" for dags dato
        Then giver systemet fejlmeddelelsen "Projekt findes ikke"

    Scenario: Fejlscenarie 7 - Tidsregistrering på ikke-eksisterende aktivitet
        When medarbejderen registrerer 4.5 timer på aktiviteten "Database" på projekt "26001" for dags dato
        Then giver systemet fejlmeddelelsen "Aktivitet findes ikke i projekt"

    Scenario: Sccesscenarie - Medarbejder kan registrere tid uden direkte aktivitetstilknytning
        When medarbejderen registrerer 2.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systenet ingen fejlmeddelelse
        And er 2.0 timer tilføjet til det samlede tidsforbrug for "huba" på aktiviteten "Frontend" på projekt "26001"

    Scenario: Sccesscenarie - Tidsregistrering med halvtimes nøjagtighed
        When medarbejderen registrerer 0.5 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systenet ingen fejlmeddelelse
        And er 0.5 timer tilføjet til det samlede tidsforbrug for "huba" på aktiviteten "Frontend" på projekt "26001"

    Scenario: Sccesscenarie - Flere tidsregistreringer på samme aktivitet akkumuleres
        When medarbejderen registrerer 2.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systenet ingen fejlmeddelelse
        And medarbejderen registrerer 3.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systenet ingen fejlmeddelelse
        And er 5.0 timer tilføjet til det samlede tidsforbrug for "huba" på aktiviteten "Frontend" på projekt "26001"

    Scenario: Sccesscenarie - Daglig tidsregistrering
        When medarbejderen registrerer 4.5 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        Then giver systenet ingen fejlmeddelelse
        And er 4.5 timer tilføjet til det samlede tidsforbrug for "huba" på aktiviteten "Frontend" på projekt "26001"
