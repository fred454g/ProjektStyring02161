# Nikolai
Feature: Opret projekt og tildel projektnummer
    For at etablere grundlaget for projektstyring
    Som en medarbejder
    Vil jeg gerne kunne oprette et nyt projekt og få tildelt et automatisk projektnummer

    Background: Grunddata for oprettelse af projekt
        Given at medarbejderen "jfk" tilfoejes til systemet
        And at medarbejderen "jfk" er logget ind i systemet

    Scenario: Fejlscenarie 1 - Brugeren er ikke logget ind
        Given at medarbejderen "jfk" er logget ud
        When medarbejderen forsoeger at oprette et projekt med navnet "Nyt IT System"
        Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

    Scenario: Fejlscenarie 2 - Tomt projektnavn
        When medarbejderen forsoeger at oprette et projekt med navnet ""
        Then giver systemet fejlmeddelelsen "Projektnavnet må ikke være tomt"

    Scenario: Succescenarie 1 - Oprettelse af projekt lykkes
        When medarbejderen forsoeger at oprette et projekt med navnet "Nyt IT System"
        Then giver systenet ingen fejlmeddelelse
        And eksisterer projektet "Nyt IT System" i systemet
        And projektet tildeles automatisk et unikt projektnummer, f.eks. "26001"
        And projektet "Nyt IT System" har nu et projektnummer som starter med det aktuelle aarstal

    Scenario: Succescenarie 2 - Projektnumre er unikke
        When medarbejderen forsoeger at oprette et projekt med navnet "Projekt A"
        Then giver systenet ingen fejlmeddelelse
        When medarbejderen forsoeger at oprette et projekt med navnet "Projekt B"
        Then giver systenet ingen fejlmeddelelse
        Then eksisterer projektet "Projekt A" i systemet
        And eksisterer projektet "Projekt B" i systemet
        And de to projekter har forskellige projektnumre
