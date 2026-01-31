# Setup & Build Instructions

## Changes Made ✅

1. **pom.xml Updated**
   - Changed version from `1.0.0` to `1.0.0-SNAPSHOT`
   - Snapshots will now be created during builds
   - Version reflects development/ongoing state

2. **Client Layer Removed** 
   - Deleted: `src/com/flipfit/client/` folder
   - Reason: REST controllers (`src/com/flipfit/rest/`) have completely replaced client menu functionality
   - No longer needed

3. **REST Layer in Place**
   - LoginController (authentication)
   - AdminController (admin operations)
   - CustomerController (customer operations)
   - GymOwnerController (owner operations)

---

## Project Structure Now

```
JEDI-DELTA-Practice-Project-Dropwizard/
├── src/com/flipfit/
│   ├── api/                          # Dropwizard application bootstrap
│   ├── rest/                         # ✅ REST Controllers (replaces client layer)
│   │   ├── LoginController.java
│   │   ├── AdminController.java
│   │   ├── CustomerController.java
│   │   └── GymOwnerController.java
│   ├── bean/                         # Domain models
│   ├── business/                     # Service layer
│   ├── dao/                          # Data access layer
│   ├── exceptions/
│   ├── constants/
│   ├── util/
│   ├── validation/
│   ├── helper/
│   └── client/ ❌ REMOVED
│
├── pom.xml                           # ✅ Updated with 1.0.0-SNAPSHOT
├── config.yml
└── target/ (created after build)
```

---

## How to Build

### Prerequisites
- Java 11+ installed
- Maven 3.8.1+ installed
- MySQL running with FlipFit database

### Build Command

**First time (clean build):**
```bash
cd d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard
mvn clean package -DskipTests
```

**Subsequent builds (faster):**
```bash
mvn package -DskipTests
```

### What Gets Created

After build completes successfully, you'll see:

```
target/
├── flipfit-dropwizard-1.0.0-SNAPSHOT.jar    ✅ Executable JAR (fat JAR)
├── flipfit-dropwizard-1.0.0-SNAPSHOT-shaded.jar
├── classes/                                  # Compiled .class files
├── maven-status/
├── maven-archiver/
└── ... (other build artifacts)
```

**JAR Location:**
```
d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard\target\flipfit-dropwizard-1.0.0-SNAPSHOT.jar
```

---

## How to Run

### Run the Application

```bash
java -jar target/flipfit-dropwizard-1.0.0-SNAPSHOT.jar server config.yml
```

**Expected Output:**
```
INFO  [2026-01-31 10:00:00,000] com.flipfit.api.FlipFitDropwizardApplication: Running FlipFitDropwizardApplication
INFO  [2026-01-31 10:00:01,234] org.eclipse.jetty.server.Server: Started Server@...
```

### Access Application

- **REST API Endpoints**: http://localhost:8080
- **Admin Console**: http://localhost:8081
- **Health Check**: http://localhost:8081/healthcheck

### Test REST APIs

**Example 1: Admin Login**
```bash
curl -X POST http://localhost:8080/auth/login/admin \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@flipfit.com","password":"admin123"}'
```

**Example 2: Register Customer**
```bash
curl -X POST http://localhost:8080/auth/register/customer \
  -H "Content-Type: application/json" \
  -d '{
    "fullName":"John Doe",
    "email":"john@example.com",
    "password":"pass123",
    "contact":"9999999999"
  }'
```

**Example 3: View Available Gyms**
```bash
curl http://localhost:8080/customers/1/gyms
```

---

## SNAPSHOT Version Explained

### What is SNAPSHOT?
- Development version indicator
- Version string: `1.0.0-SNAPSHOT`
- Tells Maven this is work-in-progress

### When Snapshots are Created
- During `mvn clean package`
- Artifact name: `flipfit-dropwizard-1.0.0-SNAPSHOT.jar`
- Maven fetches latest snapshots of dependencies
- Useful during active development

### Why Use SNAPSHOT?
- ✅ Multiple builds during development
- ✅ No manual version updates for builds
- ✅ Dependencies stay up-to-date
- ✅ Can track development progress

### When to Change to Release
When ready for production:
1. Change `pom.xml` to `<version>1.0.0</version>`
2. Run build
3. Deploy JAR

---

## Complete Workflow

### Step 1: Setup
```bash
cd d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard
```

### Step 2: Verify MySQL Connection
```bash
mysql -u root -p -e "USE FlipFit; SELECT COUNT(*) FROM users;"
```

### Step 3: Clean Build
```bash
mvn clean package -DskipTests
```

### Step 4: Check Build Success
```bash
# On Windows - check if JAR exists
dir target\*.jar

# Should show:
# flipfit-dropwizard-1.0.0-SNAPSHOT.jar
```

### Step 5: Run Application
```bash
java -jar target/flipfit-dropwizard-1.0.0-SNAPSHOT.jar server config.yml
```

### Step 6: Test in Another Terminal
```bash
curl http://localhost:8081/healthcheck
```

### Step 7: Stop Application
```bash
# In the terminal running the app
Ctrl+C
```

---

## Troubleshooting

### Target Folder Not Visible
**Reason**: Hasn't been built yet
```bash
# Build it:
mvn clean package -DskipTests

# Then check:
dir target
```

### JAR File Not Found
**Check**:
1. Build completed successfully
2. No compilation errors
3. Check exact filename matches (SNAPSHOT version now)

```bash
# List JAR files
dir target\*.jar
```

### MySQL Connection Error
**Check**:
1. MySQL service is running
2. Credentials correct in `src/com/flipfit/util/DBUtil.java`
3. FlipFit database exists

```bash
# Test connection
mysql -u root -p Lochan@1999
```

### Port 8080 Already in Use
**Solution**: Change port in `config.yml`
```yaml
server:
  applicationConnectors:
    - type: http
      port: 9090  # Change to different port
```

---

## Summary

✅ **pom.xml**: Updated to `1.0.0-SNAPSHOT`
✅ **Client Layer**: Removed (replaced by REST controllers)
✅ **REST Layer**: Active with 4 controllers (34 endpoints)
✅ **Target Folder**: Creates after `mvn clean package`

**Next Step**: Run build command
```bash
mvn clean package -DskipTests
```

Then run:
```bash
java -jar target/flipfit-dropwizard-1.0.0-SNAPSHOT.jar server config.yml
```

---

**Status**: ✅ Ready to Build & Run
