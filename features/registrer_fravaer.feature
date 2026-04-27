Feature: Registrer fravær (Ikke tilstede)
    For at systemet og kollegaer ved, hvornår jeg ikke er tilgængelig
    Som en medarbejder
    Vil jeg gerne kunne registrere fravær (fx ferie eller sygdom)

    Background:
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "huba" tilfoejes til systemet
        And at medarbejderen "huba" er logget ind i systemet

    Scenario: Hovedscenarie - Registrering af ferie
        When medarbejderen registrerer fravær af typen "Ferie" fra uge 28 til uge 30
        Then er medarbejderen markeret som fraværende med typen "Ferie" fra uge 28 til uge 30

    Scenario: Hovedscenarie - Registrering af sygdom
        When medarbejderen registrerer fravær af typen "Sygdom" fra uge 10 til uge 11
        Then er medarbejderen markeret som fraværende med typen "Sygdom" fra uge 10 til uge 11

    Scenario: Fejlscenarie - Ugyldig periode (startuge efter slutuge)
        When medarbejderen registrerer fravær af typen "Ferie" fra uge 30 til uge 28
        Then giver systemet fejlmeddelelsen "Startuge kan ikke være efter slutuge"

    Scenario: Fejlscenarie - Overlappende fraværsperiode
        Given medarbejderen registrerer fravær af typen "Ferie" fra uge 28 til uge 30
        When medarbejderen registrerer fravær af typen "Sygdom" fra uge 29 til uge 31
        Then giver systemet fejlmeddelelsen "Fraværsperioden overlapper med eksisterende fravær"

    Scenario: Fejlscenarie - Registrering uden at være logget ind
        Given at medarbejderen "huba" er logget ud
        When medarbejderen registrerer fravær af typen "Ferie" fra uge 28 til uge 30
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"