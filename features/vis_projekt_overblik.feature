Feature: Vis restarbejde, flaskehalse og timeforbrug
    For at kunne identificere problemer og se fremdrift
    Som en medarbejder
    Vil jeg gerne kunne se et overblik over timeforbrug, restarbejde og potentielle flaskehalse

    Background:
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet
        And medarbejderen opretter et projekt med navnet "Nyt IT System"
        And at medarbejderen "huba" er tilknyttet projekt "26001"

    Scenario: Hovedscenarie - Visning af projektstatus
        Given at aktiviteten "Frontend" findes på projekt "26001"
        And medarbejderen angiver startuge 10, slutuge 14 og estimeret tid 50.0 timer for aktiviteten "Frontend" på projekt "26001"
        And at medarbejderen "huba" er tilknyttet aktiviteten "Frontend" på projekt "26001"
        And at medarbejderen "huba" er logget ind i systemet
        And medarbejderen registrerer 20.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        And at medarbejderen "jfk" er logget ind i systemet
        When medarbejderen anmoder om overblik for projekt "26001"
        Then viser systemet det samlede budget på 50.0 timer
        And viser systemet det samlede tidsforbrug på 20.0 timer
        And viser systemet det forventede restarbejde på 30.0 timer

    Scenario: Visning af projektstatus med flere aktiviteter
        Given at aktiviteten "Frontend" findes på projekt "26001"
        And at aktiviteten "Backend" findes på projekt "26001"
        And medarbejderen angiver startuge 10, slutuge 14 og estimeret tid 50.0 timer for aktiviteten "Frontend" på projekt "26001"
        And medarbejderen angiver startuge 12, slutuge 16 og estimeret tid 80.0 timer for aktiviteten "Backend" på projekt "26001"
        When medarbejderen anmoder om overblik for projekt "26001"
        Then viser systemet det samlede budget på 130.0 timer
        And viser systemet det forventede restarbejde på 130.0 timer

    Scenario: Aktivitet der overskrider budget fremhæves
        Given at aktiviteten "Frontend" findes på projekt "26001"
        And medarbejderen angiver startuge 10, slutuge 14 og estimeret tid 10.0 timer for aktiviteten "Frontend" på projekt "26001"
        And at medarbejderen "huba" er tilknyttet aktiviteten "Frontend" på projekt "26001"
        And at medarbejderen "huba" er logget ind i systemet
        And medarbejderen registrerer 20.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
        And at medarbejderen "jfk" er logget ind i systemet
        When medarbejderen anmoder om overblik for projekt "26001"
        Then fremhæver systemet aktiviteten "Frontend" som overskredet

    Scenario: Fejlscenarie - Overblik for ikke-eksisterende projekt
        When medarbejderen anmoder om overblik for projekt "99999"
        Then giver systemet fejlmeddelelsen "Projektet findes ikke"

    Scenario: Fejlscenarie - Overblik uden at være logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen anmoder om overblik for projekt "26001"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Overblik for projekt uden aktiviteter
        When medarbejderen anmoder om overblik for projekt "26001"
        Then viser systemet det samlede budget på 0.0 timer
        And viser systemet det samlede tidsforbrug på 0.0 timer
        And viser systemet det forventede restarbejde på 0.0 timer
