# AGENTS.md

## Cursor Cloud specific instructions

### Overview

Milk WEB is a monolithic Spring Boot 3.1.2 (Java 17) community forum with Thymeleaf server-rendered UI, JPA/QueryDSL, MySQL 8.0, and Spring Security (form + OAuth2 login). See `README.md` for the Korean-language project description.

### Services

| Service | How to Start | Notes |
|---|---|---|
| MySQL 8.0 | `sudo docker start mysql` (if container exists) or `sudo docker run -d --name mysql -e MYSQL_DATABASE=milkweb -e MYSQL_ROOT_PASSWORD=milkweb123 -p 3306:3306 mysql:8.0` | Must be running before tests or app startup. Docker daemon must be started first: `sudo dockerd &>/tmp/dockerd.log &` |
| Spring Boot App | `./gradlew bootRun --args='--server.port=8080'` | Port 80 (default in `application.properties`) requires root; use 8080 for dev. DevTools hot-reload is enabled. |

### Required environment variables

Set in `~/.bashrc` (already configured in the snapshot):

- `JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64`
- `MY_SQL_HOST=127.0.0.1`
- `MY_SQL_NAME=root`
- `MY_SQL_PWD=milkweb123`
- `FILE_LOCATION=/tmp/milkweb`

### Key commands

- **Build:** `./gradlew clean build`
- **Tests:** `./gradlew test` (106 tests; repository tests hit real MySQL via `@AutoConfigureTestDatabase(replace = NONE)`)
- **Run:** `./gradlew bootRun --args='--server.port=8080'`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`

### Gotchas

- `application-oauth.properties` is `.gitignored`. It must exist under `src/main/resources/` with at least stub Naver OAuth2 client registration and `spring.mail.*` properties, or the Spring context fails to load. The CI workflow injects this from a GitHub secret.
- The `MilkWebApplicationTests.contextLoads()` test requires `spring.mail.host` to be configured (for `JavaMailSender` bean creation). A stub `spring.mail.host=localhost` is sufficient.
- `mkdir -p /tmp/milkweb/board` is needed for image upload functionality (the `FILE_LOCATION` directory).
- Java 17 must be selected as the default JVM (`update-alternatives --set java ...`); the VM may have Java 21 as default.
