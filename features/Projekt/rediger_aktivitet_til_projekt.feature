# # Jacob

# Feature: Rediger aktivitet til projekt

#     Background: Tilføj jfk
#         Given at medarbejderen "jfk" tilfoejes til systemet
#         And at medarbejderen "jfk" er logget ind i systemet
#         And medarbejderen opretter et projekt med navnet "Nyt IT System"

#     Scenario: Fejlscenarie - Projekt er ikke valgt i GUI
#         When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med data FAA "123.0" Start "4" Slut "5"
#         And medarbejderen redigerer aktiviteten "Backend API" paa projekt "" til data FAA "67.0" Start "8" Slut "9"
#         Then giver systemet fejlmeddelelsen "Projekt skal vælges"

#     Scenario: Fejlscenarie - Forventede antal arbejdstimer er nagativt
#         When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med data FAA "123.0" Start "4" Slut "5"
#         And medarbejderen redigerer aktiviteten "Backend API" paa projekt "26001" til data FAA "-67.0" Start "8" Slut "9"
#         Then giver systemet fejlmeddelelsen "Budgetteret tid må ikke være negativ"

#     Scenario: Fejlscenarie - Start og slutuge er ikke korrekt angivet
#         When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med data FAA "123.0" Start "4" Slut "5"
#         And medarbejderen redigerer aktiviteten "Backend API" paa projekt "26001" til data FAA "67.0" Start "9" Slut "8"
#         Then giver systemet fejlmeddelelsen "Startuge kan ikke være efter slutuge"

#     Scenario: Fejlscenarie - Projektet findes ikke
#         When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med data FAA "123.0" Start "4" Slut "5"
#         And medarbejderen redigerer aktiviteten "Backend API" paa projekt "96001" til data FAA "67.0" Start "8" Slut "9"
#         Then giver systemet fejlmeddelelsen "Projekt findes ikke"

#     Scenario: Fejlscenarie - Aktivitet findes ikke
#         When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med data FAA "123.0" Start "4" Slut "5"
#         And medarbejderen redigerer aktiviteten "Ukendt Aktivitet" paa projekt "26001" til data FAA "67.0" Start "8" Slut "9"
#         Then giver systemet fejlmeddelelsen "Aktivitet findes ikke"

#     Scenario: Hovedscenarie - Aktiviteten redigeres
#         When medarbejderen opretter aktiviteten "Backend API" paa projekt "26001" med data FAA "123.0" Start "4" Slut "5"
#         And medarbejderen redigerer aktiviteten "Backend API" paa projekt "26001" til data FAA "67.0" Start "8" Slut "9"
#         Then er aktiviteten "Backend API" oprettet på projekt "26001"
#         And aktiviteten "Backend API" på projekt "26001" har startuge 8, slutuge 9 og estimeret tid 67.0 timer