## 1. Report Summary

| Target | Type | Vulnerabilities | Secrets |
|:---|:---:|:---:|:---:|
| `pitaodkebaba/database:latest` (oracle 9.7) | `oracle` | 0 | - |
| `usr/lib/mysqlsh/.../migration_plugin/package.json` | `node-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../mrs_plugin/examples/mrs_chat/package.json` | `node-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../mrs_plugin/examples/mrs_notes/package.json` | `node-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../mrs_plugin/examples/mrs_scripts/package.json` | `node-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../mrs_plugin/package.json` | `node-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../mrs_plugin/sdk/typescript/package.json` | `node-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../msm_plugin/package.json` | `node-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../util_plugin/package.json` | `node-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../antlr4_python3_runtime-4.13.2.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../bcrypt-4.3.0.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../certifi-2025.11.12.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../cffi-2.0.0.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../circuitbreaker-2.1.3.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../cryptography-45.0.7.dist-info` | `python-pkg` | **1** | - |
| `usr/lib/mysqlsh/.../invoke-2.2.0.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../oci-2.164.0.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../paramiko-4.0.0.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../pip-25.2.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../pycparser-2.23.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../pynacl-1.5.0.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../pyopenssl-25.1.0.dist-info` | `python-pkg` | **1** | - |
| `usr/lib/mysqlsh/.../python_dateutil-2.9.0.post0.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../pytz-2025.2.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../pyyaml-6.0.2.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../six-1.17.0.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../svg_py-1.6.0.dist-info` | `python-pkg` | 0 | - |
| `usr/lib/mysqlsh/.../typing_extensions-4.12.2.dist-info` | `python-pkg` | 0 | - |
| `usr/local/bin/gosu` | `gobinary` | **6** | - |

> **Legend:** `-`: Not scanned | `0`: Clean

---

## 2. Detailed Findings

### Python Packages (`python-pkg`)
**Total: 2 (HIGH: 2, CRITICAL: 0)**

| Library | Vulnerability | Severity | Status | Installed | Fixed | Title |
|:---|:---|:---:|:---:|:---|:---|:---|
| `cryptography` | CVE-2026-26007 | **HIGH** | fixed | 45.0.7 | 46.0.5 | Subgroup Attack Due to Missing Validation... |
| `pyOpenSSL` | CVE-2026-27459 | **HIGH** | fixed | 25.1.0 | 26.0.0 | DTLS cookie callback buffer overflow |

### Go Binary: `gosu` (`gobinary`)
**Total: 6 (HIGH: 5, CRITICAL: 1)**

| Library | Vulnerability | Severity | Status | Installed | Fixed Version | Title |
|:---|:---|:---:|:---:|:---|:---|:---|
| `stdlib` | CVE-2025-68121 | **CRITICAL** | fixed | v1.24.6 | 1.24.13, 1.25.7 | Incorrect cert validation (TLS resumption) |
| `stdlib` | CVE-2025-58183 | **HIGH** | fixed | v1.24.6 | 1.24.8, 1.25.2 | Unbounded allocation (archive/tar) |
| `stdlib` | CVE-2025-61726 | **HIGH** | fixed | v1.24.6 | 1.24.12, 1.25.6 | Memory exhaustion in net/url |
| `stdlib` | CVE-2025-61728 | **HIGH** | fixed | v1.24.6 | - | CPU consumption in archive/zip |
| `stdlib` | CVE-2025-61729 | **HIGH** | fixed | v1.24.6 | 1.24.11, 1.25.5 | DoS via crafted x509 resources |
| `stdlib` | CVE-2026-25679 | **HIGH** | fixed | v1.24.6 | 1.25.8, 1.26.1 | Incorrect parsing of IPv6 host literals |

---

## 3. Uzasadnienie braku poprawek (Accepted Risk)

Skaner Trivy wykrył podatności w oficjalnym obrazie `mysql:9`. Po analizie stwierdzono, że dotyczą one wyłącznie wewnętrznych narzędzi dostawcy:

1.  **gosu**: Plik binarny jest uruchamiany jedynie przez ułamek sekundy podczas startu kontenera w celu obniżenia uprawnień (z root na mysql). Wektor ataku z zewnątrz na ten komponent nie istnieje.
2.  **Pakiety Python**: Dotyczą modułu *MySQL Shell*, który nie jest wykorzystywany w środowisku produkcyjnym (aplikacja komunikuje się bezpośrednio przez sterownik JDBC).

**Decyzja:** Ryzyko zostaje zaakceptowane do czasu wydania zaktualizowanego obrazu przez Oracle, aby uniknąć ryzyka destabilizacji bazy danych poprzez manualną ingerencję w pliki systemowe obrazu.
