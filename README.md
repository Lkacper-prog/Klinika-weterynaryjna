# Klinika Weterynaryjna - System Zarządzania Wizytami

## 📋 Opis Projektu

**Klinika Weterynaryjna** to aplikacja webowa oparta na Spring Boot, zaprojektowana do zarządzania wizytami, zwierzętami i procedurami medycznymi w klinice weterynaryjnej. System umożliwia:

- ✅ Rejestrację i zarządzanie zwierzętami
- ✅ Planowanie wizyt z automatycznym detekcją kolizji czasowych
- ✅ Realizację wizyt z wykonawaniem zabiegów medycznych
- ✅ Generowanie raportów leczenia
- ✅ Zarządzanie rolami użytkowników (Klient, Weterynarz, Administrator)
- ✅ Profesjonalne logowanie zdarzeń
- ✅ Walidację danych wejściowych

## 🚀 Uruchomienie Projektu

### Wymagania
- **Java 25+**
- **Maven 3.8+**
- **Spring Boot 4.0.6**
- **H2 Database** (wbudowana, brak konfiguracji)

### Kroki uruchomienia

1. **Klonowanie repozytorium**
```bash
cd /Users/kacper/IdeaProjects/Klinika-weterynaryjna
```

2. **Kompilacja i budowanie**
```bash
cd klinika-app
mvn clean compile
```

3. **Uruchomienie aplikacji**
```bash
mvn spring-boot:run
```

4. **Dostęp do aplikacji**
```
HTTP: http://localhost:8080
H2 Console: http://localhost:8080/h2-console
```

## 🏗️ Architektura Systemu

### Główne Komponenty

```
pl.klinika
├── Core                          # Wyjątkami i handlery
│   ├── GlobalExceptionHandler    # Centralna obsługa błędów
│   ├── ZwierzeNieZnalezioneException
│   ├── NiedostepnyTerminException
│   └── WeterynarzNieZnalezionyException
│
├── Uzytkownik                    # Zarządzanie użytkownikami
│   ├── Uzytkownik (klasa bazowa)
│   ├── Klient
│   ├── Weterynarz
│   ├── RolaUzytkownika (Enum)
│   ├── UzytkownikRepository
│   ├── KlientRepository
│   └── UzytkownikController
│
├── Zwierze                       # Zarządzanie zwierzętami
│   ├── Zwierze
│   ├── ZwierzeCreateDTO
│   ├── RaportLeczeniaDTO
│   ├── ZwierzeRepository
│   ├── ZwierzeService
│   └── ZwierzeController
│
└── Wizyta                        # Zarządzanie wizytami i zabiegami
    ├── Wizyta
    ├── StatusWizyty (Enum)
    ├── WizytaCreateDTO
    ├── ZabiegMedyczny (abstrakcyjna)
    ├── Konsultacja (konkretna implementacja)
    ├── Operacja (konkretna implementacja)
    ├── Szczepienie (konkretna implementacja)
    ├── WizytaRepository
    ├── WizytaService
    └── WizytaController
```


## 🗂️ Diagram Klas (Architecture Overview)

```mermaid
graph TD
    subgraph Core["Core Module"]
        GEH["GlobalExceptionHandler"]
        ZNE["ZwierzeNieZnalezioneException"]
        NTE["NiedostepnyTerminException"]
        WNE["WeterynarzNieZnalezionyException"]
        GEH -->|handles| ZNE
        GEH -->|handles| NTE
        GEH -->|handles| WNE
    end

    subgraph Enums["Enums"]
        SW["StatusWizyty<br/>ZAPLANOWANA<br/>ZAKONCZONA<br/>ODWOLANA"]
        RU["RolaUzytkownika<br/>KLIENT<br/>WETERYNARZ<br/>ADMIN"]
    end

    subgraph UserHierarchy["User Hierarchy JOINED"]
        UZ["Uzytkownik<br/>-id: Integer<br/>-email: String<br/>-password: String<br/>-rola: RolaUzytkownika"]
        KL["Klient<br/>-nrTelefonu: String<br/>-zwierzaki: List"]
        WET["Weterynarz<br/>-numerPWZ: String<br/>-specjalizacja: String"]
        UZ -->|extends| KL
        UZ -->|extends| WET
    end

    subgraph AnimalModule["Animal Module"]
        ZW["Zwierze<br/>-id: Integer<br/>-imie: String<br/>-gatunek: String<br/>-dataurodzenia: LocalDate<br/>-wlasciciel: Klient"]
        ZWR["ZwierzeRepository"]
        ZWS["ZwierzeService"]
        ZWC["ZwierzeController"]
        ZWDTO["ZwierzeCreateDTO<br/>-imie<br/>-gatunek<br/>-dataurodzenia"]
        RAPORT["RaportLeczeniaDTO<br/>-imieZwierzecia<br/>-liczbaWizyt<br/>-zabiegi<br/>-lacznyKoszt"]
        
        ZWR -->|manages| ZW
        ZWS -->|uses| ZWR
        ZWC -->|calls| ZWS
        ZWC -->|accepts| ZWDTO
        ZWS -->|returns| RAPORT
        KL -->|1..n| ZW
    end

    subgraph ProcedureHierarchy["Procedure Hierarchy JOINED"]
        ZM["ZabiegMedyczny abstract<br/>-id: Integer<br/>-nazwa: String<br/>-cenaBazowa: double<br/>-wizyta: Wizyta<br/>+wykonajZabieg(): void"]
        KONSL["Konsultacja<br/>-wywiad: String"]
        OP["Operacja<br/>-czyWymagaSzpitala: Boolean"]
        SZC["Szczepienie<br/>-preparat: String<br/>-waznoscWMiesiacach: int<br/>-dataWaznosci: LocalDate"]
        
        ZM -->|extends| KONSL
        ZM -->|extends| OP
        ZM -->|extends| SZC
    end

    subgraph VisitModule["Visit Module"]
        WIZ["Wizyta<br/>-id: Integer<br/>-dataczas: LocalDateTime<br/>-status: StatusWizyty<br/>-zabiegi: List<br/>-zwierze: Zwierze<br/>-weterynarz: Weterynarz<br/>+podsumujCalkowityKoszt(): double"]
        WIZDTO["WizytaCreateDTO<br/>-data: LocalDateTime<br/>-zwierzeId: Integer<br/>-vetId: Integer"]
        WIR["WizytaRepository"]
        WIS["WizytaService<br/>+umowWizyte()<br/>+zrealizujWizyte()<br/>+sprawdzKonflikt()"]
        WIC["WizytaController"]
        
        WIR -->|manages| WIZ
        WIS -->|uses| WIR
        WIC -->|calls| WIS
        WIC -->|accepts| WIZDTO
        WIZ -->|1..n| ZM
        WIZ -->|many-to-1| ZW
        WIZ -->|many-to-1| WET
    end

    subgraph UserModule["User Management Module"]
        UZR["UzytkownikRepository"]
        KLR["KlientRepository"]
        UZC["UzytkownikController"]
        
        UZR -->|manages| UZ
        KLR -->|manages| KL
        UZC -->|uses| UZR
    end

    %% Relationships between modules
    Core -->|throws| UserModule
    Core -->|throws| VisitModule
    Core -->|throws| AnimalModule
    UserModule -->|contains| UserHierarchy
    VisitModule -->|contains| ProcedureHierarchy
    VisitModule -->|uses| AnimalModule
    VisitModule -->|uses| UserHierarchy
    AnimalModule -->|uses| UserHierarchy
    
    %% Enums usage
    UserHierarchy -->|uses| RU
    VisitModule -->|uses| SW
    
    style UZ fill:#e1f5ff
    style ZM fill:#fff3e0
    style ZW fill:#e8f5e9
    style WIZ fill:#f3e5f5
```

## 🔌 Endpoints API

### Zwierzęta
```
POST   /api/zwierzeta?wlascicielId=1          - Rejestracja nowego zwierzęcia
GET    /api/zwierzeta/{zwierzeId}             - Pobranie danych zwierzęcia
GET    /api/zwierzeta/{zwierzeId}/historia    - Historia wizyt zwierzęcia
GET    /api/zwierzeta/{zwierzeId}/raport      - Raport leczenia zwierzęcia
```

### Wizyty
```
POST   /api/wizyty/umow                       - Umówienie nowej wizyty
PUT    /api/wizyty/{wizytaId}/zrealizuj       - Realizacja wizyty
```

### Użytkownicy
```
POST   /api/uzytkownicy/klient               - Rejestracja klienta
POST   /api/uzytkownicy/weterynarz           - Rejestracja weterynarza
GET    /api/uzytkownicy                       - Lista wszystkich użytkowników
```

## 🧪 Testowanie

### Przykładowe żądanie - Umówienie wizyty
```bash
curl -X POST http://localhost:8080/api/wizyty/umow \
  -H "Content-Type: application/json" \
  -d '{
    "data": "2026-06-15T14:00:00",
    "zwierzeId": 1,
    "vetId": 1
  }'
```

### Przykładowa odpowiedź - Raport leczenia
```json
{
  "imieZwierzecia": "Mruczek",
  "liczbaWizyt": 3,
  "zabiegi": [
    { "nazwa": "Szczepienie", "cena": 150.00 },
    { "nazwa": "Czyszczenie zębów", "cena": 200.00 }
  ],
  "lacznyKoszt": 950.00
}
```

## 🛡️ Obsługa Błędów

Wszystkie błędy są obsługiwane przez `GlobalExceptionHandler`:

```
404 NOT_FOUND     - Zwierzę/Weterynarz/Wizyta nie znaleziony(a)
409 CONFLICT      - Termin wizyty jest zajęty (kolizja)
400 BAD_REQUEST   - Błąd walidacji danych wejściowych
500 SERVER_ERROR  - Błąd serwera
```




## 👨‍💻 Autorzy

Kacper 
Damian
Konrad
