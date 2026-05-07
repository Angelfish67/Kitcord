# Kitcord, Projektkonfiguration

## 1. Allgemeine Informationen

* Projektname: Kitcord

* Technologie: Spring Boot (Java 26)

* Build Tool: Maven

* Authentifizierung: Keycloak (OAuth2 / JWT)

* Datenbank: PostgreSQL

* API Base URL:

  ```
  http://localhost:9090
  ```

---

## 2. Datenbankkonfiguration

### Verbindung

* Host: localhost
* Port: 5432
* Datenbank: kitcord
* Benutzer: postgres
* Passwort: 123456

### application.yml

```yaml
server:
  port: 9090

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/kitcord
    username: postgres
    password: 123456
    driverClassName: org.postgresql.Driver

  jpa:
    show-sql: true
    generate-ddl: true
    hibernate:
      ddl-auto: update
```

---

## 3. Keycloak Konfiguration

### Server

* URL: [http://localhost:8080](http://localhost:8080)
* Admin Console: [http://localhost:8080/admin](http://localhost:8080/admin)

### Realm

* Name: kitcord

### Client

* Client ID: kitcord
* Typ: bearer-only (Backend Resource Server)

### Rollen (Client Roles)

* ROLE_admin
* ROLE_read
* ROLE_update

### JWT Konfiguration (Spring Boot)

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/kitcord

app:
  name: kitcord
```

Wichtig:
Der Wert von app.name muss exakt dem Keycloak Client entsprechen.

---

## 4. Ports

| Service         | Port |
| --------------- | ---- |
| Spring Boot API | 9090 |
| Keycloak        | 8080 |
| PostgreSQL      | 5432 |

---

## 5. Projekt starten

### 1. PostgreSQL starten

Die Datenbank kitcord muss existieren wenn bei 5.3 sich für PostgreSQL entscheidet.

---

### 2. Keycloak starten

```bash
cd C:\Pfad\keycloak-26.6.0\bin
.\kc.bat start-dev --http-port=8080 --bootstrap-admin-username=admin --bootstrap-admin-password=admin
```

Login:

* Username: admin
* Passwort: admin

Danach in Keycloak:

1. Realm kitcord erstellen
2. Client kitcord erstellen
3. Rollen hinzufügen (ROLE_admin, ROLE_update, ROLE_read)
4. Benutzer erstellen

ODER

kitcord-realm.json unter Create Realm importieren.

---

### 3. Backend starten

```bash
mvn spring-boot:run
```

#### Backend starten mit PostgreSQL
mvn spring-boot:run "-Dspring-boot.run.profiles=postgres"


#### Backend starten mit H2-Console
mvn spring-boot:run "-Dspring-boot.run.profiles=h2"
http://localhost:9090/h2-console

---

## 6. API testen

### Token holen

POST

```
http://localhost:8080/realms/kitcord/protocol/openid-connect/token
```

Body:

```
grant_type=password
client_id=kitcord
username=admin
password=admin
```

---

### Beispiel Request

```
GET http://localhost:9090/admin/users
Authorization: Bearer <ACCESS_TOKEN>
```

---

## 7. Wichtige Hinweise

* Rollen müssen im Keycloak Client "kitcord" definiert werden
* Rollen müssen exakt so heißen wie im Backend (z. B. ROLE_admin)
* Ohne gültigen JWT Token sind alle Endpoints gesperrt
* Admin-Endpunkte erfordern die Rolle ROLE_admin
* Falsche Rollen-Konfiguration führt zu einem 403 Fehler

---

## 8. Voraussetzungen

* Java 26
* Maven
* PostgreSQL
* Keycloak

---

## 9. Swagger UI

```
http://localhost:9090/swagger-ui.html
```
