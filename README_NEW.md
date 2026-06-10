# 🏥 Klinika Weterynaryjna - System Zarządzania Wizytami

## 📋 1. Opis Celu Projektu

### Problem
Tradycyjne metody zarządzania wizytami w klinikach weterynaryjnych są czasochłonne i podatne na błędy. Brak scentralizowanego systemu prowadzi do:
- Kolizji czasowych w grafiku lekarza
- Utraty historii wizyt zwierząt
- Braku przejrzystości kosztów procedur
- Trudności w wyszukiwaniu informacji medycznych

### Rozwiązanie
**Klinika Weterynaryjna** to kompleksowy system webowy oparty na Spring Boot, który:
- ✅ Rejestruje i zarządza danymi zwierząt
- ✅ Planuje wizyty z automatyczną detekcją kolizji czasowych
- ✅ Realizuje wizyty z polimorficznym wykonywaniem zabiegów medycznych
- ✅ Generuje raporty leczenia z podsumowaniem kosztów
- ✅ Zarządza rolami użytkowników (Klient, Weterynarz)
- ✅ Zapewnia centralizowaną obsługę błędów z wiadomościami diagnostycznymi

### Odbiorcy
- 👨‍⚕️ **Lekarze weterynarii** - zarządzanie harmonogramem i procedurami
- 👨‍👩‍👧‍👦 **Właściciele zwierząt (Klienci)** - rejestracja i historia leczenia
- 📊 **Administracja kliniki** - raporty i statystyki

### Główne Funkcjonalności
1. **Zarządzanie Zwierzętami** - rejestracja, dane, historia leczenia
2. **Planowanie Wizyt** - umawianie z detekcją konfliktów
3. **Procedury Medyczne** - konsultacje, operacje, szczepienia (polimorficznie)
4. **Raporty Leczenia** - koszty, zabiegi, liczba wizyt
5. **Obsługa Błędów** - centralizowana, z komunikatami diagnostycznymi

---

## 🏗️ 2. Architektura i Struktura Obiektowa

### 2.1 Architektura warstwowa (3-Layer Architecture)

```
┌─────────────────────────────────────────┐
│      PRESENTATION LAYER                 │
│  (Controllers - REST Endpoints)         │
│  ZwierzeController, WizytaController    │
├─────────────────────────────────────────┤
│      BUSINESS LOGIC LAYER               │
│  (Services - logika biznesowa)          │
│  ZwierzeService, WizytaService          │
├─────────────────────────────────────────┤
│      DATA ACCESS LAYER                  │
│  (Repositories - interfejs do DB)       │
│  ZwierzeRepository, WizytaRepository    │
├─────────────────────────────────────────┤
│      DATABASE                           │
│  (H2 Database - relacyjna)              │
└─────────────────────────────────────────┘
```

**Przepływ danych:** HTTP Request → Controller → Service → Repository → Database

### 2.2 Struktura pakietów

```
pl.klinika/
├── Core/                              # Konfiguracja i obsługa błędów
│   ├── GlobalExceptionHandler         # Centralna obsługa wyjątków
│   ├── CorsConfig                     # Konfiguracja CORS
│   └── Wyjątki niestandardowe
│
├── Uzytkownik/                        # Zarządzanie użytkownikami
│   ├── Uzytkownik                     # Klasa bazowa (supertyp)
│   ├── Klient                         # Właściciel zwierząt
│   ├── Weterynarz                     # Lekarz weterynarii
│   ├── RolaUzytkownika                # Enum (KLIENT, WETERYNARZ, ADMIN)
│   ├── UzytkownikRepository           # Dostęp do BD
│   ├── KlientRepository               # Dostęp do klientów
│   └── UzytkownikController           # Endpointy REST
│
├── Zwierze/                           # Zarządzanie zwierzętami
│   ├── Zwierze                        # Encja - dane zwierzęcia
│   ├── ZwierzeCreateDTO               # DTO dla nowego zwierzęcia
│   ├── RaportLeczeniaDTO              # DTO dla raportu
│   ├── ZwierzeRepository              # Dostęp do BD
│   ├── ZwierzeService                 # Logika biznesowa
│   └── ZwierzeController              # Endpointy REST
│
└── Wizyta/                            # Zarządzanie wizytami i zabiegami
    ├── Wizyta                         # Encja - data, status, uczestniczy
    ├── StatusWizyty                   # Enum (ZAPLANOWANA, ZAKOŃCZONA, ODWOŁANA)
    ├── WizytaCreateDTO                # DTO dla nowej wizyty
    ├── ZabiegMedyczny (abstract)      # Klasa bazowa zabiegów
    │   ├── Konsultacja                # Konkretny zabieg #1
    │   ├── Operacja                   # Konkretny zabieg #2
    │   └── Szczepienie                # Konkretny zabieg #3
    ├── ZabiegCreateDTO                # DTO dla zabiegu
    ├── WizytaRepository               # Dostęp do BD
    ├── WizytaService                  # Logika biznesowa
    └── WizytaController               # Endpointy REST
```

### 2.3 Omówienie Najważniejszych Klas

#### 🔹 Hierarchia Użytkowników (Inheritance Strategy: JOINED)

**Struktura:**
```
┌────────────────────────────────┐
│      Uzytkownik (supertyp)     │
├────────────────────────────────┤
│ - id: Integer (PK)             │
│ - email: String (UNIQUE)       │
│ - password: String             │
│ - rola: RolaUzytkownika (Enum) │
└────────┬───────────────────────┘
         │
         ├─────────┬─────────┐
         │         │         │
    ┌────▼──┐ ┌────▼──┐ ┌────▼───┐
    │Klient │ │Weetryn│ │(future) │
    │       │ │arz    │ │ ADMIN   │
    └────┬──┘ └────┬──┘ └─────────┘
         │         │
         │ 1:N     │ 1:N
         ▼         ▼
     Zwierze    Wizyta
```

**Odpowiedzialność:**
- **Uzytkownik**: Wspólne pole dla każdego użytkownika (email, rola)
- **Klient**: Przechowuje zwierzęta, umawiania wizyty
- **Weterynarz**: Prowadzi wizyty, wykonuje zabiegi

**Relacje:**
- Klient **1:N** Zwierze (jeden klient ma wiele zwierząt)
- Weterynarz **1:N** Wizyta (jeden weterynarz prowadzi wiele wizyt)

**Uzasadnienie JOINED Strategy:**
- ✅ **Normalizacja bazy** - brak NULL dla `nrTelefonu` w Weterynarzu
- ✅ **Polimorfizm** - `SELECT u FROM Uzytkownik u` zwraca Klientów i Weterynarzy
- ✅ **Type Safety** - Java kompilator weryfikuje typy

---

#### 🔹 Hierarchia Zabiegów Medycznych (Inheritance: JOINED + Strategy Pattern)

**Struktura:**
```
┌──────────────────────────────────┐
│   ZabiegMedyczny (abstract)      │
├──────────────────────────────────┤
│ - id: Integer (PK)               │
│ - nazwa: String                  │
│ - cenaBazowa: double             │
│ - wizyta: Wizyta (FK)            │
├──────────────────────────────────┤
│ + wykonajZabieg(Wizyta): void    │ ← ABSTRAKCYJNA
│   (każda klasa implementuje inaczej)
└────────────────┬─────────────────┘
                 │
         ┌───────┼────────┐
         │       │        │
    ┌────▼──┐┌──▼───┐┌───▼────┐
    │Konsul-││Opera-││Szczepi-│
    │tacja  ││cja   ││enie    │
    ├──────┤├──────┤├────────┤
    │-wywiųd││-czy  ││-preparat
    │       ││Wymaga││-ważnośćW
    │       ││Szpita││Mies
    │       ││la    ││-dataWaż
    │       ││      ││nosci
    └──────┘└──────┘└────────┘
```

**Odpowiedzialność każdej klasy:**
- **ZabiegMedyczny**: Definiuje kontrakt `wykonajZabieg(Wizyta)` - każdy zabieg MUSI implementować
- **Konsultacja**: Loguje wywiąd i pobraną informację, liczy koszt
- **Operacja**: Loguje ostrzeżenie o hospitalizacji jeśli wymagana, rekomendacje
- **Szczepienie**: Oblicza datę ważności szczepienia (data + waznoscWMiesiacach)

**Design Pattern:** **Strategy Pattern**
- `wykonajZabieg()` zmienia zachowanie w zależności od typu zabiegu
- Polimorfizm runtime: `zabieg.wykonajZabieg(wizyta)` wywoła właściwą metodę

**Uzasadnienie:**
- ✅ **Elastyczność** - łatwo dodać nowy typ zabiegu (np. `Diagnostyka`)
- ✅ **Bez switch/if-else** - polimorfizm zastępuje warunki
- ✅ **Type Safety** - kompilator weryfikuje implementację

---

#### 🔹 Encja Wizyta (Główna encja)

**Struktura:**
```
┌──────────────────────────────────┐
│          Wizyta                  │
├──────────────────────────────────┤
│ - id: Integer (PK)               │
│ - dataczas: LocalDateTime        │
│ - status: StatusWizyty (Enum)    │
│ - zabiegi: List<ZabiegMedyczny>  │ (1:N)
│ - zwierze: Zwierze (FK)          │ (N:1)
│ - weterynarz: Weterynarz (FK)    │ (N:1)
├──────────────────────────────────┤
│ + podsumujCalkowityKoszt()       │ ← Agregacja
│   : double                       │
└──────────────────────────────────┘
```

**Odpowiedzialność:**
- Przechowuje informacje o wizycie (czas, status)
- Liczy całkowity koszt zabiegu (suma `cenaBazowa` ze wszystkich zabiegów)
- Zawiera listę zabiegów do wykonania

**Logika biznesowa:**
```java
double total = 0;
for (ZabiegMedyczny zabieg : zabiegi) {
    total += zabieg.getCenaBazowa();
}
return total;
```

---

#### 🔹 Serwisy (Business Logic Layer)

**ZwierzeService:**
```
+ zarejestrujZwierze(Zwierze, wlascicielId)
  ├─ Pobiera Klienta z bazy
  ├─ Waliduje istnienie Klienta
  ├─ Przypisuje Klienta do Zwierzęcia
  └─ Zapisuje do bazy

+ pobierzHistorieLeczenia(zwierzeId)
  ├─ Sprawdza czy Zwierzę istnieje
  ├─ Pobiera wszystkie Wizyty dla Zwierzęcia
  └─ Zwraca listę wizyt
```

**WizytaService:**
```
+ sprawdzKonfliktTerminow(dataczas, vetId)
  ├─ Szuka nałożonych wizyt weterynarza
  └─ Zwraca true (wolny) / false (zajęty)

+ umowWizyte(dataczas, zwierzeId, vetId)
  ├─ Sprawdza konflikt terminów
  ├─ Waliduje zwierzę i weterynarza
  ├─ Tworzy nową Wizytę (status: ZAPLANOWANA)
  └─ Zapisuje i zwraca

+ zrealizujWizyte(wizytaId)
  ├─ Pobiera Wizytę
  ├─ Dla każdego zabiegu: wykonajZabieg()
  ├─ Zmienia status na ZAKOŃCZONA
  └─ Zapisuje zmiany
```

---

### 2.4 Diagram Relacji Bazy Danych (ERD)

```
┌─────────────────┐
│   UZYTKOWNIK    │ (supertyp)
├─────────────────┤
│ id (PK)         │
│ email (UNIQUE)  │
│ haslo           │
│ rola (ENUM)     │
└────────┬────────┘
         │
    ┌────┴────┐
    │          │
┌───▼──┐  ┌───▼──┐
│KLIENT│  │WETERYN│
│      │  │ARZ    │
├──────┤  ├──────┤
│id(FK)│  │id(FK)│
│nr_tel│  │nr_pwz│
│      │  │spec  │
└──┬───┘  └──┬───┘
   │ 1:N     │ 1:N
   │         │
   ▼         ▼
┌───────┐  ┌──────┐
│ZWIERZE│  │WIZYTA│
├───────┤  ├──────┤
│id(PK) │  │id(PK)│
│imie   │  │dataczas
│gatunek│  │status│
│data_ur│  │zwierz_id(FK)
│wlasci │  │weterynarz_id(FK)
│_id(FK)│  │
└───┬───┘  └───┬──┘
    │ 1:N      │ 1:N
    └────┬─────┘
         │
    ┌────▼──────────┐
    │ZABIEG_MEDYCZN│
    │Y (supertyp)   │
    ├────────────────┤
    │id (PK)         │
    │nazwa           │
    │cena_bazowa     │
    │wizyta_id(FK)   │
    │dtype (JOINED)  │
    └────┬───────────┘
         │
    ┌────┼────┐
    │    │    │
┌───▼──┐│┌──▼───┐┌────▼────┐
│KONSUL││OPERACY││SZCZEPIEN│
│TACJA ││JA     ││IE        │
```

---

### 2.5 Uzasadnienie Decyzji Projektowych

#### 🎯 **1. Dlaczego JOINED Strategy dla dziedziczenia?**

| Strategia | Problem | Rozwiązanie |
|-----------|---------|-----------|
| **SINGLE_TABLE** | Wiele NULL kolumn | Szybkie but redundancja |
| **TABLE_PER_CLASS** | Duplikacja kolumn | Szybkie ale normalizacja |
| **JOINED** (✅ wybrana) | Więcej JOINów | Normalizacja + polimorfizm |

**Decyzja:** JOINED, bo:
- 🗄️ Normalizacja bazy danych
- 🔍 Przejrzystość - jasne kolumny dla każdej klasy
- 🎯 Polimorfizm JPA - `SELECT u FROM Uzytkownik u` zwraca wszystkich

#### 🛡️ **2. Dlaczego DTO (Data Transfer Object)?**

```java
❌ Niebezpieczeństwo (bez DTO):
@PostMapping
public Zwierze create(@RequestBody Zwierze z) {
  // Hacker wysyła dodatkowe pola (np. isAdmin=true)
  return service.save(z);
}

✅ Bezpieczne (z DTO):
@PostMapping
public Zwierze create(@Valid @RequestBody ZwierzeCreateDTO dto) {
  // Tylko zdefiniowane pola + walidacja @Valid
}
```

**Zalety:**
- 🔐 **Bezpieczeństwo** - nie expose całej encji
- ✔️ **Walidacja** - `@NotBlank`, `@Future` na DTO poziomie
- 🔄 **Mapowanie** - kontrolujemy jakie pola idą do frontendu

#### 📊 **3. Dlaczego Enum zamiast String?**

```java
❌ Problem (String):
if (status.equals("ZAPLANOWANA")) {...}
if (status.equals("ZAPLANNOWANA")) {...}  // TYPO!

✅ Enum (type-safe):
switch (status) {
  case ZAPLANOWANA: ...
  case ZAKONCZONA: ...
  case ODWOLANA: ...
}
```

#### ⏰ **4. Detekcja kolizji czasowych - logika**

Każda wizyta trwa 30 minut. Sprawdzamy czy nowa wizyta nachodzi na istniejące:

```sql
SELECT COUNT(w) FROM wizyta w
WHERE weterynarz_id = :vetId
AND dataczas < :dataKoniec              ← Koniec nowej wizyty
AND DATE_ADD(dataczas, 30 MIN) > :data  ← Start nowej wizyty
```

**Przykład:**
```
Istniejąca: 14:00-14:30
Nowa:       14:20-14:50  ← ❌ KOLIZJA!
```

#### 🔐 **5. GlobalExceptionHandler - centralizacja błędów**

**Zaleta:** Jeden handler dla wszystkich kontrolerów
```java
@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ZwierzeNieZnalezioneException.class)
  public ResponseEntity handle(ZwierzeNieZnalezioneException e) {
    return ResponseEntity.status(404).body(...);
  }
}
```

---

## 🚀 3. Instrukcja Uruchomienia i Użycia

### 3.1 Wymagania systemowe

```
✅ Java 25 LTS
✅ Maven 3.8+
✅ Spring Boot 3.3+
✅ H2 Database (wbudowana - brak dodatkowych instalacji)
```

### 3.2 Kroki uruchomienia

**Krok 1:** Przejdź do folderu projektu
```bash
cd C:\Users\toxic\Desktop\Klinika-weterynaryjna\klinika-app
```

**Krok 2:** Kompilacja
```bash
mvn clean compile
```

**Krok 3:** Uruchomienie
```bash
mvn spring-boot:run
```

**Krok 4:** Dostęp
```
🔌 Backend REST API:    http://localhost:8080
📊 H2 Console:          http://localhost:8080/h2-console
```

### 3.3 Przykładowe Use Cases (cURLem)

**1️⃣ Rejestracja Zwierzęcia:**
```bash
curl -X POST http://localhost:8080/api/zwierzeta?wlascicielId=1 \
  -H "Content-Type: application/json" \
  -d '{
    "imie": "Mruczek",
    "gatunek": "Kot",
    "dataurodzenia": "2020-05-15"
  }'
```

**2️⃣ Umówienie Wizyty:**
```bash
curl -X POST http://localhost:8080/api/wizyty/umow \
  -H "Content-Type: application/json" \
  -d '{
    "data": "2026-06-15T14:00:00",
    "zwierzeId": 1,
    "vetId": 1
  }'
```

**3️⃣ Historia Wizyty Zwierzęcia:**
```bash
curl http://localhost:8080/api/zwierzeta/1/historia
```

**4️⃣ Raport Leczenia:**
```bash
curl http://localhost:8080/api/zwierzeta/1/raport
```

---

## 👥 4. Opis Podziału Pracy w Zespole

### Skład zespołu 

| Imię | 
|------|------|
| **Kacper** | 
| **Damian** |
| **Konrad** | 

