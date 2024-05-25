## CuzzApp

### Tabel de conținut:
1. [Informații generale](#informații-generale)
2. [Ghid de instalare și utilizare](#ghid-de-instalare-și-utilizare)
3. [Arhitectură aplicației](#arhitectură-aplicației)
4. [Justificarea tehnologiilor alese](#justificarea-tehnologiilor-alese)

### 1. Informații generale

#### 1.1 Scopul
Scopul aplicației este de a crea un sistem activ de învățare destinat copiilor, indiferent de posibilitățile lor financiare. Aplicația se bazează pe premisa nevoii umane de socializare și competiție. Astfel, am creat o secțiune de învățare de unde copiii pot colecta informații educaționale de înaltă calitate gratuit. Aceste informații le permit să obțină puncte, care ulterior le oferă acces la lecții mai complexe și materiale costisitoare. În plus, aplicația utilizează sistemul de rating pentru a stimula o competiție sănătoasă, permițând fiecărei persoane să-și testeze cunoștințele pe baza punctelor acumulate.

#### 1.2 Audiență și rezultate
Audiența principală este formată din orice persoană dornică să învețe sau să-și depășească limitele. Deși audiența noastră este diversă, am reușit să ne atingem scopul de a educa oameni și de a le oferi oportunități egale. Acest lucru a fost dovedit printr-un experiment desfășurat în clasa mea, unde jumătate dintre colegi au folosit aplicația timp de trei săptămâni pentru a-și îmbunătăți cunoștințele în diverse domenii școlare. Rezultatele au fost vizibile în scurt timp, confirmând utilitatea și direcția aplicației.

### 2. Ghid de instalare și utilizare

#### 2.1 Instalarea pe dispozitive
Pentru instalarea aplicației, este necesar să folosiți un distribuitor precum Google Play sau site-ul nostru dedicat, ai-cuza.ro, de unde puteți descărca APK-ul pentru platforma dorită. Tehnologia aleasă permite utilizarea aplicației pe orice platformă, dar vom detalia acest aspect în secțiunea 4.

#### 2.2 Crearea contului
După instalarea aplicației, va fi necesar să creați un cont care necesită verificarea identității printr-o fotografie a feței, procesată cu un model TensorFlow antrenat de noi. După înregistrare, vă puteți loga și începe utilizarea aplicației fără probleme.

### 3. Arhitectură aplicației

#### 3.1 Stack
Aplicația este construită utilizând următorul stack tehnologic:
- Kotlin Multiplatform
- Python
- Java
- Firebase
- Gradle pentru construirea aplicației

Fiecare element din stack este integrat eficient datorită specificului fiecărui limbaj.

#### 3.2 Tehnici de programare
Am folosit mai mulți algoritmi pentru a optimiza aplicația, inclusiv:
- Programarea sincronă pentru sarcini precum sortarea listelor și crearea claselor.
- Programarea asincronă pentru interacțiunea cu baza de date, permițând codului să aștepte răspunsuri de la baza de date.
- Programarea pe grafuri cu o eficiență de 87%. Am creat un DNN cu 5 milioane de noduri/neuroni.

#### 3.3 UI/UX
Aplicația a avut inițial o versiune v1.0, care, deși funcțională, avea deficiențe de UX raportate de utilizatori. Am folosit feedback-ul pentru a îmbunătăți paleta de culori și experiența utilizatorilor, adăugând caracteristici precum bottom bar și drawer, utilizabile prin widget-uri intuitive.

### 4. Justificarea tehnologiilor alese

Am ales acest stack datorită scalabilității oferite de Kotlin Multiplatform, care permite soluții native pentru orice platformă. În plus, Gradle și Python oferă o soluție stabilă pe termen lung datorită comunității de susținere.

Din punct de vedere tehnologic, proiectul oferă o soluție logică și clară pentru ecranele aplicației:
- **Login și Register:** Ecrane simple, dar securizate, cu algoritmi AI pentru verificarea identității.
- **Home Screen:** Oferă posibilitatea de a învăța și colecta puncte prin videoclipuri educaționale integrate cu ExoPlayer și API-uri de video.
- **Ranking Screen:** Afișează datele din baza de date în ordine crescătoare.
- **Shop:** Utilizatorii pot folosi punctele pentru a achiziționa resurse educaționale.
- **Asistență:** Oferă suport 24/24 pentru orice problemă întâmpinată de utilizatori.
