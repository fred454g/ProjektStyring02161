# Feature: Rediger projektnavn
#     For at kunne rette fejl eller opdatere projektets formål
#     Som en medarbejder
#     Vil jeg gerne kunne ændre navnet på et eksisterende projekt

#     Background: At der findes et projekt
#         Given at medarbejderen "jfk" tilfoejes til systemet
#         And at medarbejderen "jfk" er logget ind i systemet
#         And medarbejderen opretter et projekt med navnet "Nyt IT System"

#     Scenario: Hovedscenarie - Ændring af projektnavn lykkes
#         Given at projektet "26001" med navnet "Nyt IT System" findes i systemet
#         When medarbejderen ændrer navnet på projekt "26001" til "Nyt Navn"
#         Then er projektets navn opdateret til "Nyt Navn" i systemet

#     Scenario: Fejlscenarie - Tomt nyt projektnavn
#         Given at projektet "26001" med navnet "Nyt IT System" findes i systemet
#         When medarbejderen forsoeger at ændre navnet på projekt "26001" til ""
#         Then giver systemet fejlmeddelelsen "Nyt projektnavn må ikke være tomt"

#     Scenario: Fejlscenarie - Projekt findes ikke
#         When medarbejderen forsoeger at ændre navnet på projekt "99999" til "Nyt Navn"
#         Then giver systemet fejlmeddelelsen "Projekt findes ikke"

#     Scenario: Fejlscenarie - Ændring uden at være logget ind
#         Given at medarbejderen "jfk" er logget ud
#         When medarbejderen forsoeger at ændre navnet på projekt "26001" til "Nyt Navn"
#         Then giver systemet fejlmeddelelsen "Ingen bruger logged in"
