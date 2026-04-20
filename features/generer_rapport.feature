Feature: Generer rapport
  For at kunne dele projektets status med andre
  Som en medarbejder
  Vil jeg gerne kunne generere en statusrapport

  Scenario: Hovedscenarie - Generering af rapport for et projekt
    Given at medarbejderen er logget ind i systemet
    And at projekt "26001" med navnet "Nyt IT System" eksisterer
    And at projektet har en aktivitet "Frontend" med budget på 50 timer
    When medarbejderen anmoder om en rapport for projekt "26001"
    Then modtager systemet en rapport, der indeholder projektnavnet "Nyt IT System"
    And rapporten viser at totalt budget er 50 timer
