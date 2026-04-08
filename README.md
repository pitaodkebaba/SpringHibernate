# Music Browser - System Zarządzania Muzyką

## 1. Architektura Systemu

Zgodnie z wymaganiami projektu, poniżej przedstawiono architekturę aplikacji:

* **Frontend**: Aplikacja Single Page Application (SPA) oparta na React + Vite, serwowana przez Nginx (obraz unprivileged).
* **Backend**: Mikroserwis REST API zbudowany w Spring Boot 3, wykorzystujący Java 21 i Maven.
* **Database**: Baza danych MySQL 8.0 do przechowywania informacji o użytkownikach, piosenkach i playlistach.

```mermaid
graph TD
    %% Sekcja Klienta
    subgraph "Urządzenie Użytkownika (Zewnątrz)"
        Client[Przeglądarka Internetowa]
        ReactApp[Uruchomiona Aplikacja React<br/>w pamięci przeglądarki]
        Client --- ReactApp
    end
    
    %% Sekcja Serwera
    subgraph "Docker / Minikube (Wewnętrzna Sieć)"
        Nginx[API Gateway<br/>Nginx]
        ReactContainer[Kontener Frontend<br/>Przechowalnia plików HTML/JS]
        Backend[Backend<br/>Spring Boot]
        Redis[(Redis<br/>Cache)]
        MySQL[(MySQL<br/>Baza Danych)]
    end

    %% PRZEPŁYW 1: POBIERANIE STRONY
    Client -.->|1. Użytkownik wpisuje adres localhost| Nginx
    Nginx -.->|Szuka plików na ścieżce '/'| ReactContainer
    ReactContainer -.->|Zwraca gotowy interfejs do przeglądarki| Client

    %% PRZEPŁYW 2: KOMUNIKACJA Z API (To o co pytałeś)
    ReactApp ==>|2. axios/fetch uderza na '/api/songs'| Nginx
    Nginx ==>|Przekazuje ruch prosto do Javy| Backend
    Backend ==>|Zwraca dane w formacie JSON| Nginx
    Nginx ==>|Przesyła JSON do Reacta| ReactApp

    %% Backend Logika
    Backend -->|Odczyt z Cache| Redis
    Backend -->|Odczyt/Zapis do Bazy| MySQL

    %% Kolory
    style Client fill:#e6f3ff,stroke:#333,stroke-width:2px
    style ReactApp fill:#61dafb,stroke:#333,stroke-width:2px,color:#000
    style Nginx fill:#ffcccb,stroke:#333,stroke-width:2px
    style ReactContainer fill:#f9f9f9,stroke:#333,stroke-dasharray: 5 5
    style Backend fill:#d5e8d4,stroke:#333,stroke-width:2px
    style Redis fill:#ffe6cc,stroke:#333,stroke-width:2px
    style MySQL fill:#fff2cc,stroke:#333,stroke-width:2px
```

> **Zadanie 1.6**: Graficzna reprezentacja architektury wygenerowana przez `compose-viz` znajdzie się w pliku `architektura.png`.

## 2. Repozytoria DockerHub

Obrazy są budowane jako wieloarchitekturowe (`linux/amd64`, `linux/arm64`) i zawierają wbudowane informacje SBOM.

* **Backend**: [Link do DockerHub - pitaodkebaba/backend](https://hub.docker.com/r/pitaodkebaba/backend)
* **Frontend**: *(Link zostanie dodany po przesłaniu obrazu)*
* **Database**: *(Link zostanie dodany po przesłaniu obrazu)*

## 3. Pliki Dockerfile i Dobre Praktyki

Wszystkie obrazy zostały opracowane zgodnie z wytycznymi bezpieczeństwa i optymalizacji:

* **Multi-stage builds**: Oddzielenie etapu budowania od etapu uruchamiania (mniejszy rozmiar obrazu).
* **Lekkie obrazy bazowe**: Wykorzystanie dystrybucji Alpine Linux i Temurin.
* **Bezpieczeństwo (Non-root)**: Uruchamianie procesów jako użytkownicy o niskich uprawnieniach.

## 4. Analiza Podatności (Trivy)

## 5. SBOM (Software Bill of Materials)

Informacje SBOM zostały osadzone w obrazach za pomocą flagi `--sbom=true`. Dowód obecności manifestu można zweryfikować komendą:
`docker buildx imagetools inspect pitaodkebaba/backend:latest`

## 6. Uruchomienie deweloperskie

Aplikację można uruchomić lokalnie za pomocą Docker Compose:

```bash
docker-compose up -d
```

Frontend dostępny jest pod adresem: `http://localhost`  
Backend (API) dostępny jest pod adresem: `http://localhost:8080`
