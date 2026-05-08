# Feature: Opret aktivitet og angiv estimater
#     For at kunne nedbryde et projekt og planlægge arbejdet
#     Som en medarbejder
#     Vil jeg gerne kunne oprette en aktivitet og angive start/slut uge samt estimeret tid
#     Background: Tilføj jfk
#         Given at medarbejderen "jfk" tilfoejes til systemet
#         And at medarbejderen "jfk" er logget ind i systemet
#         And medarbejderen opretter et projekt med navnet "Nyt IT System"

#     Scenario: Hovedscenarie - Oprettelse og estimering af aktivitet
#         Given at projektet "26001" findes i systemet
#         When medarbejderen opretter aktiviteten "Backend API" på projekt "26001"
#         And medarbejderen angiver startuge 10, slutuge 14 og estimeret tid 50.0 timer for aktiviteten "Backend API" på projekt "26001"
#         Then er aktiviteten "Backend API" oprettet på projekt "26001"
#         And aktiviteten "Backend API" på projekt "26001" har startuge 10, slutuge 14 og estimeret tid 50.0 timer

#     Scenario: Fjernelse af aktivitet
#         Given at projektet "26001" findes i systemet
#         And at aktiviteten "Backend API" er oprettet på projekt "26001"
#         When medarbejderen fjerner aktiviteten "Backend API" på projekt "26001"
#         Then er aktiviteten "Backend API" ikke tilknyttet projekt "26001"
#         And udsendes observer-eventen "AKTIVITET_SLETTET"

#     Scenario: Fejlscenarie - Oprettelse af aktivitet på ikke-eksisterende projekt
#         When medarbejderen forsoeger at oprette aktiviteten "Backend API" på projekt "99999"
#         Then giver systemet fejlmeddelelsen "Projekt findes ikke"

#     Scenario: Fejlscenarie - Startuge efter slutuge
#         Given at projektet "26001" findes i systemet
#         And medarbejderen opretter aktiviteten "Test" på projekt "26001"
#         When medarbejderen angiver startuge 14, slutuge 10 og estimeret tid 50.0 timer for aktiviteten "Test" på projekt "26001"
#         Then giver systemet fejlmeddelelsen "Startuge kan ikke være efter slutuge"

#     Scenario: Fejlscenarie - Negativ estimeret tid
#         Given at projektet "26001" findes i systemet
#         And medarbejderen opretter aktiviteten "Test" på projekt "26001"
#         When medarbejderen angiver startuge 10, slutuge 14 og estimeret tid -5.0 timer for aktiviteten "Test" på projekt "26001"
#         Then giver systemet fejlmeddelelsen "Budgetteret tid må ikke være negativ"

#     Scenario: Redigering af aktivitetens estimater
#         Given at projektet "26001" findes i systemet
#         And at aktiviteten "Backend API" er oprettet på projekt "26001"
#         When medarbejderen angiver startuge 10, slutuge 14 og estimeret tid 50.0 timer for aktiviteten "Backend API" på projekt "26001"
#         Then aktiviteten "Backend API" på projekt "26001" har startuge 10, slutuge 14 og estimeret tid 50.0 timer
#         When medarbejderen angiver startuge 12, slutuge 16 og estimeret tid 80.0 timer for aktiviteten "Backend API" på projekt "26001"
#         Then aktiviteten "Backend API" på projekt "26001" har startuge 12, slutuge 16 og estimeret tid 80.0 timer

#     Scenario: Fejlscenarie - Fjernelse af ikke-eksisterende aktivitet
#         Given at projektet "26001" findes i systemet
#         When medarbejderen forsoeger at fjerne aktiviteten "Ukendt" på projekt "26001"
#         Then giver systemet fejlmeddelelsen "Aktivitet findes ikke"

#     Scenario: Fejlscenarie - Oprettelse uden at være logget ind
#         Given at medarbejderen "jfk" er logget ud
#         When medarbejderen forsoeger at oprette aktiviteten "Backend API" på projekt "26001"
#         Then giver systemet fejlmeddelelsen "Ingen bruger logged in"

#     Scenario: Oprettelse af flere aktiviteter på samme projekt
#         Given at projektet "26001" findes i systemet
#         When medarbejderen opretter aktiviteten "Frontend" på projekt "26001"
#         And medarbejderen opretter aktiviteten "Backend" på projekt "26001"
#         And medarbejderen opretter aktiviteten "Test" på projekt "26001"
#         Then er aktiviteten "Frontend" oprettet på projekt "26001"
#         And er aktiviteten "Backend" oprettet på projekt "26001"
#         And er aktiviteten "Test" oprettet på projekt "26001"
