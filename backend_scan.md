# Raport ze skanowania podatności (Trivy / Security Scan)

## Podsumowanie (Report Summary)

| Cel (Target) | Typ | Podatności | Sekrety |
| :--- | :---: | :---: | :---: |
| `pitaodkebaba/backend:latest` (ubuntu 24.04) | ubuntu | **0** | - |
| `app/MusicBrowser.jar` | jar | **13** | - |

**Legenda:**

> * `-`: Nie skanowano
> * `0`: Czysto (nie wykryto żadnych problemów bezpieczeństwa)

---

## Java (jar) - Znalezione Podatności

**Łącznie:** 13 (Krytyczne: 2, Wysokie: 11)

| Biblioteka (Zainstalowana wersja) | CVE / Podatność | Ważność | Status | Naprawiono w wersji | Tytuł / Opis |
| :--- | :--- | :---: | :---: | :--- | :--- |
| **`org.apache.tomcat.embed:tomcat-embed-core`** <br/> *(10.1.31)* | [CVE-2025-24813](https://avd.aquasec.com/nvd/cve-2025-24813) | **CRITICAL** | fixed | `11.0.3`, `10.1.35`, `9.0.99` | tomcat: Potential RCE and/or information disclosure and/or information corruption with partial PUT |
| **`org.apache.tomcat.embed:tomcat-embed-core`** <br/> *(10.1.31)* | [CVE-2024-50379](https://avd.aquasec.com/nvd/cve-2024-50379) | HIGH | fixed | `11.0.2`, `10.1.34`, `9.0.98` | tomcat: RCE due to TOCTOU issue in JSP compilation |
| **`org.apache.tomcat.embed:tomcat-embed-core`** <br/> *(10.1.31)* | [CVE-2024-56337](https://avd.aquasec.com/nvd/cve-2024-56337) | HIGH | fixed | `11.0.2`, `10.1.34`, `9.0.98` | tomcat: Incomplete fix for CVE-2024-50379 - RCE due to TOCTOU issue |
| **`org.apache.tomcat.embed:tomcat-embed-core`** <br/> *(10.1.31)* | [CVE-2025-48988](https://avd.aquasec.com/nvd/cve-2025-48988) | HIGH | fixed | `11.0.8`, `10.1.42`, `9.0.106` | tomcat: Apache Tomcat DoS in multipart upload |
| **`org.apache.tomcat.embed:tomcat-embed-core`** <br/> *(10.1.31)* | [CVE-2025-48989](https://avd.aquasec.com/nvd/cve-2025-48989) | HIGH | fixed | `11.0.10`, `10.1.44`, `9.0.108` | tomcat: http/2 "MadeYouReset" DoS attack through HTTP/2 control frames |
| **`org.apache.tomcat.embed:tomcat-embed-core`** <br/> *(10.1.31)* | [CVE-2025-52520](https://avd.aquasec.com/nvd/cve-2025-52520) | HIGH | fixed | `11.0.9`, `10.1.43`, `9.0.107` | tomcat: Apache Tomcat denial of service |
| **`org.apache.tomcat.embed:tomcat-embed-core`** <br/> *(10.1.31)* | [CVE-2025-53506](https://avd.aquasec.com/nvd/cve-2025-53506) | HIGH | fixed | `11.0.9`, `10.1.43`, `9.0.107` | tomcat: Apache Tomcat denial of service |
| **`org.apache.tomcat.embed:tomcat-embed-core`** <br/> *(10.1.31)* | [CVE-2025-55752](https://avd.aquasec.com/nvd/cve-2025-55752) | HIGH | fixed | `11.0.11`, `10.1.45`, `9.0.109` | tomcat: Directory traversal via rewrite with possible RCE |
| **`org.apache.tomcat.embed:tomcat-embed-core`** <br/> *(10.1.31)* | [CVE-2026-24734](https://avd.aquasec.com/nvd/cve-2026-24734) | HIGH | fixed | `11.0.18`, `10.1.52`, `9.0.115` | tomcat: Certificate revocation bypass due to improper OCSP response validation |
| **`org.springframework.boot:spring-boot`** <br/> *(3.3.5)* | [CVE-2025-22235](https://avd.aquasec.com/nvd/cve-2025-22235) | HIGH | fixed | `3.3.11`, `3.4.5` | spring-boot: EndpointRequest.to() creates wrong matcher if actuator endpoint is not... |
| **`org.springframework.security:spring-security-crypto`** <br/> *(6.3.4)* | [CVE-2025-22228](https://avd.aquasec.com/nvd/cve-2025-22228) | HIGH | fixed | `6.3.8`, `6.4.4`, `6.2.10`, `6.1.14`, `6.0.16`, `5.8.18`, `5.7.16` | spring-security-core: BCryptPasswordEncoder does not enforce maximum password length |
| **`org.springframework.security:spring-security-web`** <br/> *(brak/6.3.x)* | [CVE-2026-22732](https://avd.aquasec.com/nvd/cve-2026-22732) | **CRITICAL** | fixed | `6.5.9`, `7.0.4` | Spring Security: Security policy bypass and information disclosure |
| **`org.springframework:spring-core`** <br/> *(6.1.14)* | [CVE-2025-41249](https://avd.aquasec.com/nvd/cve-2025-41249) | HIGH | fixed | `6.2.11` | spring-core: Spring Framework Annotation Detection Vulnerability |

## Uzasadnienie braku wpływu wykrytych podatności na bezpieczeństwo projektu:

Wykryte podatności dotyczą bibliotek bazowych Spring Boot oraz wbudowanego serwera Apache Tomcat. Ich obecność w obrazie deweloperskim nie stwarza jednak realnego zagrożenia dla bezpieczeństwa i działania niniejszego projektu z następujących powodów:

1. Aplikacja jest uruchamiana w izolowanym środowisku kontenerowym (Docker Compose) o charakterze deweloperskim/testowym. Nie jest wystawiona na publiczny dostęp z poziomu sieci Internet, co drastycznie ogranicza wektor potencjalnych ataków (w tym ataków DoS wymienionych w raporcie).

2. Większość podatności serwera Tomcat (np. CVE-2024-50379 dotyczące TOCTOU) odnosi się do kompilacji widoków JSP (JavaServer Pages) lub specyficznych reguł rewrite. Architektura mojego projektu opiera się wyłącznie na udostępnianiu REST API (zwracanie formatu JSON) i w ogóle nie korzysta z mechanizmów renderowania stron po stronie serwera ani z protokołu HTTP/2.

3. Raport wskazuje podatność CVE-2025-22228 dotyczącą braku weryfikacji długości hasła przez BCryptPasswordEncoder, co może prowadzić do ataków DoS. Podatność ta jest skutecznie mitygowana na poziomie logiki biznesowej mojej aplikacji – klasa RegisterUserDto posiada walidację `@Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")`. Hasła przekraczające dozwoloną długość są odrzucane przez kontroler, zanim w ogóle trafią do mechanizmu szyfrującego.

4. Podatność CVE-2025-22235 dotyczy tworzenia reguł bezpieczeństwa dla modułu monitorującego Spring Actuator, który nie został zaimplementowany w architekturze tego projektu.
