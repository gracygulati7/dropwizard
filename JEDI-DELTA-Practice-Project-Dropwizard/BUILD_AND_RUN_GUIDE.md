# Dropwizard Build & Run Guide - FlipFit Project

## Target Folder & Build Output

### Where Target Folder Is Created
```
JEDI-DELTA-Practice-Project-Dropwizard/
├── pom.xml
├── config.yml
├── src/                    # Source code
├── target/                 # ✅ CREATED HERE after maven build
│   ├── flipfit-dropwizard-1.0.0.jar              # Final executable JAR (fat JAR)
│   ├── flipfit-dropwizard-1.0.0-shaded.jar      # Intermediate shaded JAR
│   ├── classes/                                  # Compiled .class files
│   ├── maven-status/
│   ├── maven-archiver/
│   └── ... (other build artifacts)
└── bin/                    # (Original project files, not affected)
```

### Target Folder Location (Full Path)
```
d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard\target\
```

---

## SNAPSHOT vs Release Versions

### Current pom.xml Configuration

```xml
<version>1.0.0</version>  <!-- ✅ RELEASE VERSION (no -SNAPSHOT) -->
```

### When Snapshots Are Created
- **SNAPSHOT versions**: Used during development (e.g., `1.0.0-SNAPSHOT`)
- **Release versions**: Stable, final builds (e.g., `1.0.0`)

### Why NO SNAPSHOT in This Project

Your pom.xml specifies:
```xml
<version>1.0.0</version>
```

This means:
- ✅ **Snapshots will NOT be created** - Only stable release artifacts
- ✅ **Version is final** - Ready for production
- ✅ **Dependencies are stable** - No -SNAPSHOT suffixes

### When Snapshots WOULD Be Used

If your version was:
```xml
<version>1.0.0-SNAPSHOT</version>
```

Then:
- ⚠️ Maven would fetch latest snapshots of dependencies
- ⚠️ Built JAR would be: `flipfit-dropwizard-1.0.0-SNAPSHOT.jar`
- ⚠️ Useful during active development
- ⚠️ NOT recommended for production

---

## Build Commands

### 1. Clean Build (Recommended First Time)
```bash
cd d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard
mvn clean package -DskipTests
```

**What this does:**
- `clean` - Deletes old target folder
- `package` - Compiles, tests, packages JAR
- `-DskipTests` - Skips unit tests (no tests exist yet)
- **Output**: `target/flipfit-dropwizard-1.0.0.jar` (fat JAR with all dependencies)

### 2. Fast Build (Without Clean)
```bash
mvn package -DskipTests
```

**What this does:**
- Reuses existing compiled classes
- Only recompiles changed files
- Faster than clean build

### 3. Build With Tests (If tests exist)
```bash
mvn clean package
```

**What this does:**
- Runs all unit tests
- Fails if tests fail
- Slower but more reliable

### 4. Only Compile (Without Packaging)
```bash
mvn compile
```

**What this does:**
- Only compiles .java files to .class files
- No JAR created
- Useful for checking for compilation errors

---

## Run Commands

### 1. Run From JAR File (Recommended)
```bash
cd d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard
java -jar target/flipfit-dropwizard-1.0.0.jar server config.yml
```

**What this does:**
- Executes the fat JAR file
- Starts the Dropwizard application
- Loads `config.yml` for server configuration
- Application runs on `http://localhost:8080`
- Admin console on `http://localhost:8081`

### 2. Run With Custom Configuration
```bash
java -jar target/flipfit-dropwizard-1.0.0.jar server config.yml
```

### 3. Run With Additional JVM Arguments
```bash
java -Xmx512m -Xms256m -jar target/flipfit-dropwizard-1.0.0.jar server config.yml
```

**Flags:**
- `-Xmx512m` - Maximum heap memory: 512 MB
- `-Xms256m` - Minimum heap memory: 256 MB

### 4. Run With Verbose Logging
```bash
java -jar target/flipfit-dropwizard-1.0.0.jar server config.yml --debug
```

---

## Complete Build & Run Workflow

### Step 1: Navigate to Project
```bash
cd d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard
```

### Step 2: Clean Build
```bash
mvn clean package -DskipTests
```

**Output:**
```
[INFO] Building jar: d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard\target\flipfit-dropwizard-1.0.0.jar
[INFO] BUILD SUCCESS
```

### Step 3: Run Application
```bash
java -jar target/flipfit-dropwizard-1.0.0.jar server config.yml
```

**Output:**
```
INFO  [2026-01-31 10:00:00,000] com.flipfit.api.FlipFitDropwizardApplication: Running FlipFitDropwizardApplication
INFO  [2026-01-31 10:00:01,234] org.eclipse.jetty.server.Server: Started Server@...
```

### Step 4: Verify Application is Running

**In another terminal:**
```bash
curl http://localhost:8081/healthcheck
```

**Expected Response:**
```json
{"deadline":"2226-01-31T10:00:00.000Z","isHealthy":true}
```

### Step 5: Test API Endpoints

**Example: Admin Login**
```bash
curl -X POST http://localhost:8080/auth/login/admin \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@flipfit.com","password":"admin123"}'
```

---

## Key Maven Concepts from pom.xml

### Properties
```xml
<properties>
  <maven.compiler.source>11</maven.compiler.source>    <!-- Java 11 source -->
  <maven.compiler.target>11</maven.compiler.target>    <!-- Java 11 bytecode -->
  <dropwizard.version>2.1.12</dropwizard.version>      <!-- Dropwizard version -->
</properties>
```

### Dependencies
```xml
<!-- Dropwizard Core Framework -->
<dependency>
  <groupId>io.dropwizard</groupId>
  <artifactId>dropwizard-core</artifactId>
  <version>2.1.12</version>
</dependency>

<!-- MySQL Database Driver -->
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.33</version>
</dependency>
```

### Build Plugin: Maven Shade
```xml
<plugin>
  <artifactId>maven-shade-plugin</artifactId>
  <goal>shade</goal>
  <mainClass>com.flipfit.api.FlipFitDropwizardApplication</mainClass>
</plugin>
```

**What it does:**
- Packages all dependencies into a single "fat JAR"
- Sets entry point to `FlipFitDropwizardApplication.main()`
- Results in `flipfit-dropwizard-1.0.0.jar` (executable standalone JAR)
- No need to worry about classpath or missing dependencies

---

## Directory Structure After Build

```
JEDI-DELTA-Practice-Project-Dropwizard/
├── src/
│   └── com/flipfit/
│       ├── api/
│       ├── rest/
│       ├── business/
│       ├── dao/
│       └── ...
│
├── target/                           # ✅ CREATED BY MVN BUILD
│   ├── flipfit-dropwizard-1.0.0.jar  # ✅ EXECUTABLE FAT JAR
│   ├── flipfit-dropwizard-1.0.0-shaded.jar
│   ├── classes/
│   │   └── com/flipfit/...           # Compiled .class files
│   ├── maven-status/
│   └── maven-archiver/
│
├── pom.xml
├── config.yml
├── README.md
└── ... (documentation files)
```

---

## Maven Build Lifecycle

When you run `mvn clean package`:

1. **clean** phase
   - Deletes `target/` directory
   - Cleans old artifacts

2. **validate** phase
   - Validates project structure
   - Checks dependencies

3. **compile** phase
   - Compiles `src/` to `target/classes/`
   - Java source → .class files

4. **test** phase
   - Runs unit tests (skipped with `-DskipTests`)
   - Tests in `src/test/`

5. **package** phase
   - Creates JAR file
   - Maven Shade plugin creates fat JAR
   - Adds all dependencies into single JAR

6. **verify** phase
   - Verifies package integrity

7. **install** phase
   - Installs JAR to local Maven repository (optional)

8. **deploy** phase
   - Deploys to remote repository (optional)

---

## Common Issues & Solutions

### Issue: JAR not found after build
```bash
# Check if build was successful
mvn clean package -DskipTests

# If it failed, check for compilation errors
mvn compile
```

### Issue: Port 8080 already in use
```bash
# Change port in config.yml
server:
  applicationConnectors:
    - type: http
      port: 9090  # Change to different port
```

### Issue: Database connection fails
```bash
# Verify MySQL is running
mysql -u root -p -e "SELECT 1"

# Check credentials in util/DBUtil.java
# Default: root / Lochan@1999
```

### Issue: Java version mismatch
```bash
# Check Java version
java -version

# Should be Java 11 or higher
# If not, update pom.xml compiler properties

<maven.compiler.source>11</maven.compiler.source>
<maven.compiler.target>11</maven.compiler.target>
```

---

## Quick Reference

| Task | Command |
|------|---------|
| Clean build | `mvn clean package -DskipTests` |
| Run application | `java -jar target/flipfit-dropwizard-1.0.0.jar server config.yml` |
| Check health | `curl http://localhost:8081/healthcheck` |
| Test admin login | `curl -X POST http://localhost:8080/auth/login/admin -H "Content-Type: application/json" -d '{"email":"admin@flipfit.com","password":"admin123"}'` |
| View logs | Check console output |
| Stop application | `Ctrl+C` in terminal |

---

## Summary

✅ **Target Folder**: Created in `JEDI-DELTA-Practice-Project-Dropwizard/target/` after `mvn package`

✅ **Snapshots**: NOT created (version is `1.0.0`, not `1.0.0-SNAPSHOT`)

✅ **Build Command**: 
```bash
mvn clean package -DskipTests
```

✅ **Run Command**: 
```bash
java -jar target/flipfit-dropwizard-1.0.0.jar server config.yml
```

✅ **Access Application**: 
- REST API: http://localhost:8080
- Admin Console: http://localhost:8081
- Health Check: http://localhost:8081/healthcheck

---

**Ready to build and run!** 🚀
