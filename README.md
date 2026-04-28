# Projektstyringsværktøj – Softwarehuset A/S

## 1. Dette kan appen

Appen understøtter centrale arbejdsgange i Softwarehuset A/S’ projektstyring:

### Projekter
- Oprette projekter med automatisk projektnummer
- Omdøbe projekter
- Tilknytte og ændre projektleder
- Tilføje medarbejdere til projekter
- Fjerne medarbejdere fra projekter

### Aktiviteter
- Oprette aktiviteter på projekter
- Angive budgetteret tid
- Angive startuge og slutuge
- Opdatere aktivitetens budget og periode
- Slette aktiviteter
- Tilknytte medarbejdere til aktiviteter
- Fjerne medarbejdere fra aktiviteter

### Tidsregistrering
- Registrere daglige timer på aktiviteter
- Vise egne registrerede timer
- Sikre at man ikke kan registrere 0, negative timer eller mere end 24 timer pr. dag
- Tillade tidsregistrering på en aktivitet, selvom medarbejderen ikke nødvendigvis er direkte tilknyttet aktiviteten

### Fravær
- Registrere fravær som fx ferie eller sygdom
- Validere at startuge ikke ligger efter slutuge
- Forhindre overlappende fravær

### Ressourceoverblik
- Vise ledige medarbejdere i en given ugeperiode
- En medarbejder vurderes som ledig, hvis personen:
    - ikke har fravær i perioden
    - ikke er tilknyttet en aktivitet, der overlapper perioden

### Statusrapport
- Generere rapport for et projekt
- Rapporten viser:
    - projektnavn
    - projektnummer
    - projektleder
    - tilknyttede medarbejdere
    - aktiviteter
    - budgetteret tid
    - registreret tid
    - restarbejde
    - aktiviteter over budget

---

## 2. Sådan starter du appen

### Krav
- Java 17 eller nyere
- Maven
- JavaFX
- IntelliJ IDEA anbefales

### Start i IntelliJ

1. Åbn projektet i IntelliJ.
2. Sørg for at Maven dependencies er indlæst.
3. Kør klassen:

```text
dtu.example.ui.App
```

Login

Appen læser medarbejdere fra:
```
src/main/java/dtu/example/hr_liste.txt
```
eks: 
```
jfk
huba
abc
```
Du kan logge ind med et af disse brugernavne. Det er ikke nødvendigt at indtaste en adgangskode.

###Database
Appen bruger simple tekstfiler til persistens:
```
projekter.txt

fravaer.txt

hr_liste.txt
```
* hr_liste.txt indeholder medarbejderinitialer.
* projekter.txt gemmer projekter, aktiviteter, projektleder, medarbejderrelationer og tidsregistreringer.
* fravaer.txt gemmer medarbejderfravær.

##Arkitektur og ansvarsområder
Systemet er bygget efter en lagdelt MVC-inspireret arkitektur:
JavaFX UI
    ↓
Controllers
    ↓
Planlaegningsvaerktoej
    ↓
Domain
    ↓
Repositories

UI-lag

FXML-filerne beskriver brugergrænsefladen.

Eksempler:

* primary.fxml
* secondary.fxml

UI-laget har ansvar for:

* visning
* inputfelter
* knapper
* dropdowns
* brugerfeedback

FXML indeholder ingen domænelogik.

⸻

Controller-lag

Centrale controllers:

* PrimaryController
* SecondaryController

Controllerens ansvar:

* læse input fra UI
* parse tal fra tekstfelter
* kalde metoder på facaden
* vise fejl- og succesbeskeder
* opdatere dropdowns ved observer-events

Controlleren kalder ikke direkte på Projekt, Aktivitet eller repositories. Den går gennem:
Planlaegningsvaerktoej

Denne klasse fungerer som systemets facade.

Ansvar:

* håndtere use cases
* validere input
* sikre at bruger er logget ind
* kalde domæneobjekter
* kalde repositories
* udsende observer-events

Eksempler på use cases:
``
opretProjekt(...)
opretAktivitet(...)
registrerTid(...)
registrerFravaer(...)
genererRapport(...)
visLedigeMedarbejdere(...)
``

Domain-lag

Centrale klasser:

* Projekt
* Aktivitet
* Medarbejder
* Fravaer
* Tidsregistrering
* RapportGenerator

Ansvar:

* holde systemets centrale data
* udføre domæneregler
* beregne registreret tid
* håndtere fraværsoverlap
* generere rapportindhold

Eksempel:
``
Fravaer.overlapper(...)
``
er domænelogik og ligger derfor i domain-laget.

⸻

Persistence-lag

Centrale klasser:

* ProjektRepository
* MedarbejderRepository
* FravaerRepository

Ansvar:

* læse fra filer
* skrive til filer
* genskabe objekter fra tekstdata

Repositories holder filhåndtering ude af domæneklasserne. Det følger Single Responsibility Principle.

⸻

Observer pattern

Systemet bruger Java’s PropertyChangeSupport.

Planlaegningsvaerktoej fungerer som subject.

Observers:

* SecondaryController
* Observer.java

Når en use case ændrer systemets tilstand, udsendes events som:
``
PROJECT_OPRETTET
AKTIVITET_OPRETTET
AKTIVITET_SLETTET
MEDARBEJDER_TILKNYTTET_PROJEKT
FRAVAER_REGISTRERET
``

SecondaryController bruger events til at opdatere UI-lister og dropdowns.

Observer.java bruges som systemlogger.

⸻

Afgrænsninger og kendte begrænsninger

Systemet er en prototype og ikke en produktionsklar løsning.

Begrænsninger

* Persistens sker i tekstfiler, ikke database.
* Der er ingen brugerroller eller adgangskontrol ud over login med initialer.
* Ledighed beregnes binært: enten ledig eller optaget.
* Systemet tager ikke højde for delvis kapacitet, fx 10 timer ledig ud af 37 timer.
* Der er ikke avanceret konfliktløsning mellem projekter.
* Controlleren kunne opdeles i mindre controllers for bedre skalering.

Bevidste designvalg

* Fravær er modelleret som separat Fravaer i stedet for som en aktivitet.
* Medarbejdere identificeres kun ved initialer.
* Facaden samler use cases for at holde controlleren simpel.
* Repositories bruges for at holde filadgang ude af domænemodellen.

⸻

Testdækning

Systemets funktioner er beskrevet med Cucumber feature-filer.

De dækker blandt andet:

* opret projekt
* rediger projektnavn
* opret aktivitet
* slet aktivitet
* tilknyt/fjern medarbejder fra projekt
* tilknyt/fjern medarbejder fra aktivitet
* registrer tid
* vis egne timer
* registrer fravær
* generer rapport
* indlæs HR-liste

Feature-filerne dokumenterer både hovedscenarier og fejlscenarier.

