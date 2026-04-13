Feature: Opret projekt og tildel projektnummer
    For at etablere grundlaget for projektstyring
    Som en medarbejder
    Vil jeg gerne kunne oprette et nyt projekt og få tildelt et automatisk projektnummer
    Background: Tilføj jfk
        Given at medarbejderen "jfk" med navn "John F. Kennedy" tilfoejes til systemet

    Scenario: Hovedscenarie - Oprettelse af projekt lykkes
        Given at medarbejderen "jfk" er logget ind i systemet
        When medarbejderen opretter et projekt med navnet "Nyt IT System"
        Then eksisterer projektet "Nyt IT System" i systemet
        And projektet tildeles automatisk et unikt projektnummer, f.eks. "26001"

    Scenario: Fejlscenarie - Tomt projektnavn
        Given at medarbejderen "jfk" er logget ind i systemet
        When medarbejderen forsoeger at oprette et projekt uden at angive et navn
        Then giver systemet fejlmeddelelsen "Projektnavnet må ikke være tomt"