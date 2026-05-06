Feature: Generer rapport
  For at kunne dele projektets status med andre
  Som en medarbejder
  Vil jeg gerne kunne generere en statusrapport

  Background:
    Given at medarbejderen "jfk" tilfoejes til systemet
    And at medarbejderen "huba" tilfoejes til systemet
    And at medarbejderen "jfk" er logget ind i systemet
    And medarbejderen opretter et projekt med navnet "Nyt IT System"

  Scenario: Hovedscenarie - Generering af rapport for et projekt
    Given at projekt "26001" med navnet "Nyt IT System" eksisterer
    And at projektet har en aktivitet "Frontend" med budget på 50 timer
    When medarbejderen anmoder om en rapport for projekt "26001"
    Then modtager systemet en rapport, der indeholder projektnavnet "Nyt IT System"
    And rapporten viser at totalt budget er 50 timer

  Scenario: Rapport med flere aktiviteter og tidsforbrug
    Given at projekt "26001" med navnet "Nyt IT System" eksisterer
    And at projektet har en aktivitet "Frontend" med budget på 50 timer
    And at projektet har en aktivitet "Backend" med budget på 80 timer
    And at medarbejderen "huba" er tilknyttet projekt "26001"
    And at medarbejderen "huba" er tilknyttet aktiviteten "Frontend" på projekt "26001"
    And at medarbejderen "huba" er logget ind i systemet
    And medarbejderen registrerer 10.0 timer på aktiviteten "Frontend" på projekt "26001" for dags dato
    And at medarbejderen "jfk" er logget ind i systemet
    When medarbejderen anmoder om en rapport for projekt "26001"
    Then modtager systemet en rapport, der indeholder projektnavnet "Nyt IT System"
    And rapporten viser at totalt budget er 130 timer
    And rapporten viser at totalt registreret tid er 10.0 timer

  Scenario: Rapport for projekt uden aktiviteter
    Given at projekt "26001" med navnet "Nyt IT System" eksisterer
    When medarbejderen anmoder om en rapport for projekt "26001"
    Then modtager systemet en rapport, der indeholder projektnavnet "Nyt IT System"
    And rapporten viser at totalt budget er 0 timer

  Scenario: Fejlscenarie - Rapport for ikke-eksisterende projekt
    When medarbejderen anmoder om en rapport for projekt "99999"
    Then giver systemet fejlmeddelelsen "Projektet findes ikke"

  Scenario: Fejlscenarie - Rapport uden at være logget ind
    Given at medarbejderen "jfk" er logget ud
    When medarbejderen anmoder om en rapport for projekt "26001"
    Then giver systemet fejlmeddelelsen "Ingen bruger logged in"
