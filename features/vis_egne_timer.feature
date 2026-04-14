Feature: Vis medarbejders egne timer
    For at bevare overblikket over min egen arbejdsindsats
    Som en medarbejder
    Vil jeg gerne kunne se en oversigt over mine egne registrerede timer

    Background:
        Given at medarbejderen "jfk" med navn "John F. Kennedy" tilfoejes til systemet
        And at medarbejderen "huba" med navn "Hubert Baumeister" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And at medarbejderen "huba" er tilknyttet projekt "26001"
        And at medarbejderen "jfk" er tilknyttet projekt "26001"
        And at aktiviteten "Frontend" findes på projekt "26001"
        And at aktiviteten "Backend" findes på projekt "26001"
        And at medarbejderen "huba" er tilknyttet aktiviteten "Frontend" på projekt "26001"
        And at medarbejderen "huba" er tilknyttet aktiviteten "Backend" på projekt "26001"
        And at medarbejderen "jfk" er tilknyttet aktiviteten "Frontend" på projekt "26001"

    Scenario: Hovedscenarie - Visning af egne timer på tværs af aktiviteter
        Given at medarbejderen "huba" er logget ind i systemet
        And medarbejderen registrerer 4.5 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        And medarbejderen registrerer 3.0 timer på aktiviteten "Backend" på projekt "26001" for dags dato
        When medarbejderen anmoder om at se sine egne tidsregistreringer
        Then viser systemet 7.5 timer totalt for medarbejderen
        And systemet viser 4.5 timer på aktiviteten "Frontend" på projekt "26001"
        And systemet viser 3.0 timer på aktiviteten "Backend" på projekt "26001"

    Scenario: Visning af egne timer når ingen timer er registreret
        Given at medarbejderen "huba" er logget ind i systemet
        When medarbejderen anmoder om at se sine egne tidsregistreringer
        Then viser systemet 0.0 timer totalt for medarbejderen

    Scenario: Kun egne timer vises, ikke andres
        Given at medarbejderen "huba" er logget ind i systemet
        And medarbejderen registrerer 4.5 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen registrerer 2.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        When medarbejderen anmoder om at se sine egne tidsregistreringer
        Then viser systemet 2.0 timer totalt for medarbejderen

    Scenario: Fejlscenarie - Vis timer uden at være logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen anmoder om at se sine egne tidsregistreringer
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"