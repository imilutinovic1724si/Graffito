# Graffito

Desktop aplikacija za kreiranje i uređivanje slajd prezentacija, razvijena u okviru predmeta Dizajn softvera na Računarskom fakultetu (RAF).

Autori: Iva Milutinović i Elena Mijajlović

## O projektu

Graffito je editor prezentacija sa grafičkim interfejsom (Swing) koji omogućava kreiranje projekata sa više prezentacija i slajdova, dodavanje i uređivanje elemenata (tekst, slike, logo), kao i čuvanje i učitavanje sadržaja u JSON formatu.

Cilj projekta bio je prepoznavanje situacija u kojima se mogu primeniti klasični dizajn obrasci i njihova samostalna implementacija u okviru realnog softverskog rešenja.

## Funkcionalnosti

- Kreiranje i organizacija projekata, prezentacija i slajdova (stablo elemenata)
- Dodavanje, pomeranje, rotacija i promena veličine elemenata na slajdu
- Undo/redo za sve akcije
- Čuvanje i učitavanje projekata i šablona u JSON formatu
- Prikaz u više režima prozora (normalan, ceo ekran, mali)
- Provera dostupnog prostora na slajdu pri dodavanju elemenata
- Logovanje aktivnosti (konzola i fajl)

## Primenjeni dizajn obrasci

- **Composite** - hijerarhijska struktura projekata, prezentacija, slajdova i elemenata
- **Decorator** - dodatno bojenje/stilizovanje elemenata
- **Proxy** - odloženo (lazy) učitavanje slika
- **Bridge** - režimi prikaza prozora i provera prostora na slajdu
- **Mediator** - komunikacija između komponenti pri proveri prostora i promeni režima prozora
- **State** - alati za rad sa elementima (dodavanje, brisanje, pomeranje, promena veličine, selekcija)
- **Observer** - sistem logovanja i obaveštavanja o promenama
- **Command** - akcije korisnika sa podrškom za undo/redo, uključujući složene (composite) komande
- **Factory** - kreiranje logger objekata

## Tehnologije

- Java 22
- Swing (grafički interfejs)
- Maven
- Jackson (JSON serijalizacija/deserijalizacija)

## Pokretanje

1. Otvoriti projekat u IntelliJ IDEA (ili drugom IDE-u sa Maven podrškom)
2. Maven će automatski povući zavisnosti iz pom.xml
3. Pokrenuti AppCore.java
