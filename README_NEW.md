#  Klinika Weterynaryjna - System Zarządzania Wizytami

##  1. Opis Celu Projektu

**Klinika Weterynaryjna** to kompleksowy system webowy oparty na Spring Boot, który:
- ✅ Rejestruje i zarządza danymi zwierząt
- ✅ Planuje wizyty z automatyczną detekcją kolizji czasowych
- ✅ Realizuje wizyty z polimorficznym wykonywaniem zabiegów medycznych
- ✅ Generuje raporty leczenia z podsumowaniem kosztów
- ✅ Zarządza rolami użytkowników (Klient, Weterynarz)
- ✅ Zapewnia centralizowaną obsługę błędów z wiadomościami diagnostycznymi

### Główne Funkcjonalności
1. **Zarządzanie Zwierzętami** - rejestracja, dane, historia leczenia
2. **Planowanie Wizyt** - umawianie z detekcją konfliktów, realizacja zabiegów
3. **Procedury Medyczne** - konsultacje, operacje, szczepienia (polimorficznie)
4. **Raporty Leczenia** - koszty, zabiegi, liczba wizyt
5. **Obsługa Błędów** - centralizowana, z komunikatami diagnostycznymi

---

## 🏗️ 2. Architektura i Struktura Obiektowa

### Struktura pakietów ###

```text
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

## 👥 4. Skład zespołu 

| **Kacper** | 
| **Damian** |
| **Konrad** | 

