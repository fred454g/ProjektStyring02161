Feature: Opret projekt og tildel projektnummer
    For at etablere grundlaget for projektstyring
    Som en medarbejder
    Vil jeg gerne kunne oprette et nyt projekt og få tildelt et automatisk projektnummer
    Background: Tilføj jfk
        Given at medarbejderen "jfk" tilfoejes til systemet

    Scenario: Hovedscenarie - Oprettelse af projekt lykkes
        Given at medarbejderen "jfk" er logget ind i systemet
        When medarbejderen opretter et projekt med navnet "Nyt IT System"
        Then eksisterer projektet "Nyt IT System" i systemet
        And projektet tildeles automatisk et unikt projektnummer, f.eks. "26001"

    Scenario: Fejlscenarie - Tomt projektnavn
        Given at medarbejderen "jfk" er logget ind i systemet
        When medarbejderen forsoeger at oprette et projekt uden at angive et navn
        Then giver systemet fejlmeddelelsen "Projektnavnet må ikke være tomt"

    Scenario: Projektnummer tildeles sekventielt
        Given at medarbejderen "jfk" er logget ind i systemet
        When medarbejderen opretter et projekt med navnet "Projekt A"
        And medarbejderen opretter et projekt med navnet "Projekt B"
        Then eksisterer projektet "Projekt A" i systemet
        And eksisterer projektet "Projekt B" i systemet
        And de to projekter har forskellige projektnumre

    Scenario: Fejlscenarie - Oprettelse uden at være logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen forsoeger at oprette et projekt med navnet "Nyt IT System"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Projektnummer har korrekt format med årstal
        Given at medarbejderen "jfk" er logget ind i systemet
        When medarbejderen opretter et projekt med navnet "Format Test"
        Then giver systenet ingen fejlmeddelelse
        And projektet tildeles automatisk et unikt projektnummer, f.eks. "26001"
        And projektnummeret starter med det aktuelle årstal
