# Spring Tools Suite (STS) Setup Guide for FlipFit Dropwizard

## Prerequisites

- ✅ Spring Tools Suite 4 installed (download from https://spring.io/tools)
- ✅ Java 11+ installed
- ✅ MySQL running with FlipFit database
- ✅ Maven 3.8.1+ (usually bundled with STS)

---

## Step 1: Open Spring Tools Suite

1. Launch Spring Tools Suite application
2. Select workspace (create new or use existing)
3. Click "Launch" to start

---

## Step 2: Import Maven Project into STS

### Method 1: Import Existing Maven Project (Recommended)

1. **In STS Menu**: Go to `File` → `Import...`

2. **In Import Dialog**:
   - Select: `Maven` → `Existing Maven Projects`
   - Click `Next`

3. **Select Root Directory**:
   - Browse to: `d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard`
   - Click `Select Folder`
   - Click `Next`

4. **Select Projects**:
   - Check: `flipfit-dropwizard [pom]`
   - Click `Next`
   - Click `Finish`

5. **Wait for Maven Build**:
   - STS will download dependencies automatically
   - Check bottom-right for progress
   - May take 2-5 minutes first time

### Method 2: Command Line Import

```bash
# In STS Terminal (or external terminal):
cd d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard
mvn clean install -DskipTests
```

---

## Step 3: Configure Build Path

### Verify Java Compiler Level

1. Right-click project → `Properties`
2. Go to `Java Compiler`
3. Verify: `Compiler compliance level: 11`
4. If not, change it to 11
5. Click `Apply and Close`

### Verify Project Facets (if needed)

1. Right-click project → `Properties`
2. Look for `Project Facets`
3. Make sure Java version is set to 1.11
4. Click `Apply and Close`

---

## Step 4: Configure Maven Settings

### Update Maven in STS

1. `Window` → `Preferences` (or `Eclipse` → `Preferences` on Mac)
2. Go to `Maven` → `Installations`
3. Click `Add...`
4. Browse to Maven installation path (or use bundled Maven)
5. Click `Finish`
6. Select it and click `Apply and Close`

### Update Eclipse Maven Integration

1. Right-click project
2. `Maven` → `Update Project`
3. Select force updates
4. Click `OK`
5. Wait for update to complete

---

## Step 5: Download Dependencies

### Automatic (Recommended)

Once project is imported, STS automatically downloads all Maven dependencies:
- Check `M2_REPO` in Eclipse
- All JARs appear there

### Manual Download

If needed:
1. Right-click project → `Maven` → `Update Project`
2. Or: `Project` → `Clean...` → `Build`

---

## Step 6: Verify Project Structure in STS

In Package Explorer, you should see:

```
flipfit-dropwizard
├── src/
│   └── com/flipfit/
│       ├── api/
│       │   ├── FlipFitDropwizardApplication.java
│       │   ├── FlipFitConfiguration.java
│       │   └── health/
│       ├── rest/
│       │   ├── LoginController.java
│       │   ├── AdminController.java
│       │   ├── CustomerController.java
│       │   └── GymOwnerController.java
│       ├── business/
│       ├── dao/
│       ├── bean/
│       └── ...
├── pom.xml
├── config.yml
└── target/ (created after build)
```

---

## Step 7: Build Project in STS

### Method 1: STS Menu

1. `Project` → `Clean...`
   - Select "Clean all projects"
   - Click `OK`
   - Wait for clean to complete

2. `Project` → `Build All`
   - Or: Right-click project → `Maven` → `Build All`
   - Select Goals: `clean package`
   - Click `Run`

### Method 2: Quick Build

1. Right-click project
2. `Run As` → `Maven build...`
3. Enter Goals: `clean package -DskipTests`
4. Click `Run`

### Method 3: Maven Command in Terminal

```bash
# In STS integrated terminal or external:
cd d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard
mvn clean package -DskipTests
```

---

## Step 8: Verify Build Success

### In STS Console

You should see:
```
[INFO] Building jar: d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard\target\flipfit-dropwizard-1.0.0-SNAPSHOT.jar
[INFO] BUILD SUCCESS
```

### In Package Explorer

Expand `target` folder:
```
target/
├── flipfit-dropwizard-1.0.0-SNAPSHOT.jar  ✅ Your executable JAR
├── classes/
├── maven-status/
└── ...
```

---

## Step 9: Run Application from STS

### Method 1: Create Run Configuration

1. `Run` → `Run Configurations...`

2. Click `New Launch Configuration` (or `Java Application`)

3. In **Main** tab:
   - **Name**: `FlipFit Dropwizard`
   - **Project**: `flipfit-dropwizard`
   - **Main class**: `com.flipfit.api.FlipFitDropwizardApplication`

4. In **Arguments** tab:
   - **Program arguments**: `server config.yml`

5. In **JRE** tab:
   - **Execution environment**: Java 11

6. Click `Apply` → `Run`

### Method 2: Run JAR Directly

```bash
# In STS Terminal or command line:
cd d:\JEDI-DELTA-DEVELOPMENT-FLIPKART\JEDI-DELTA-Practice-Project-Dropwizard
java -jar target/flipfit-dropwizard-1.0.0-SNAPSHOT.jar server config.yml
```

### Expected Output

```
INFO  [2026-01-31 10:00:00,000] com.flipfit.api.FlipFitDropwizardApplication: Running FlipFitDropwizardApplication
INFO  [2026-01-31 10:00:01,234] org.eclipse.jetty.server.Server: Started Server@...
INFO  [2026-01-31 10:00:02,567] org.eclipse.jetty.server.handler.ContextHandler: Started ContextHandler@...
```

---

## Step 10: Test APIs from STS

### Using STS REST Client (if available)

1. `Window` → `Show View` → `Other...`
2. Search for `REST Client`
3. Click `OK`

Or use external tools:

### Using Postman

1. Download Postman (https://www.postman.com/downloads/)
2. Import endpoints or create manually

### Using cURL in STS Terminal

1. Open Terminal in STS: `Window` → `Show View` → `Terminal`

2. Run test command:
```bash
curl -X POST http://localhost:8080/auth/login/admin ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"admin@flipfit.com\",\"password\":\"admin123\"}"
```

---

## Step 11: Debug Mode in STS

### Run in Debug Mode

1. Right-click on `FlipFitDropwizardApplication.java`
2. `Debug As` → `Java Application`

Or:

1. `Run` → `Debug Configurations...`
2. Select your configuration
3. Click `Debug`

### Set Breakpoints

1. Click in left margin of code (line number area)
2. Breakpoint appears as red dot
3. Code pauses at breakpoint during debug

### Debug Controls

- **F5**: Step Into
- **F6**: Step Over
- **F8**: Resume
- **F9**: Skip Breakpoints

---

## Step 12: View Logs in STS

### Console View

1. Window → Show View → Console
2. All application logs appear here

### Filter Logs

1. In Console, use search/filter options
2. Common filters:
   - `ERROR` - Show only errors
   - `com.flipfit` - Show only project logs
   - `javax.ws.rs` - REST framework logs

---

## Common Issues in STS

### Issue 1: Dependencies Not Downloaded

**Solution**:
1. Right-click project → `Maven` → `Update Project`
2. Or: Delete `.m2/repository` folder and rebuild

```bash
# Windows:
del %USERPROFILE%\.m2\repository

# Then rebuild:
mvn clean install -DskipTests
```

### Issue 2: Java Version Mismatch

**Solution**:
1. Project → Properties → Java Compiler
2. Set to Java 11
3. Apply and rebuild

### Issue 3: Build Errors

**Solution**:
1. `Project` → `Clean...` → `Clean all projects`
2. `Project` → `Build All`

### Issue 4: Port Already in Use

**Solution**:
1. Open `config.yml`
2. Change port:
```yaml
server:
  applicationConnectors:
    - type: http
      port: 9090
```

### Issue 5: MySQL Connection Failed

**Solution**:
1. Verify MySQL is running:
```bash
mysql -u root -p -e "SELECT 1"
```

2. Check credentials in `src/com/flipfit/util/DBUtil.java`
3. Default: `root / Lochan@1999`

---

## Quick Commands in STS Terminal

### Build Project
```bash
mvn clean package -DskipTests
```

### Run Application
```bash
java -jar target/flipfit-dropwizard-1.0.0-SNAPSHOT.jar server config.yml
```

### Test Health Check
```bash
curl http://localhost:8081/healthcheck
```

### Test Admin Login
```bash
curl -X POST http://localhost:8080/auth/login/admin ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"admin@flipfit.com\",\"password\":\"admin123\"}"
```

### View Project Structure
```bash
tree /A src
```

### Stop Running Application
```
Ctrl+C
```

---

## STS Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| `Ctrl+Shift+O` | Organize Imports |
| `Ctrl+Shift+F` | Format Code |
| `Ctrl+1` | Quick Fix |
| `Ctrl+Space` | Autocomplete |
| `Ctrl+H` | Find/Replace |
| `Ctrl+Alt+H` | Open Call Hierarchy |
| `F11` | Debug |
| `Ctrl+F11` | Run |
| `F5` | Step Into |
| `F6` | Step Over |
| `F8` | Resume |

---

## Useful STS Views/Panels

Open via: `Window` → `Show View` → `Other...`

| View | Purpose |
|------|---------|
| Problems | Shows build errors/warnings |
| Console | Application output and logs |
| Terminal | Integrated command terminal |
| Outline | Code structure view |
| Package Explorer | Project structure |
| Properties | Object properties |
| Git | Git integration |

---

## Final Checklist

- ✅ STS installed with Java 11+
- ✅ Project imported as Maven project
- ✅ Maven dependencies downloaded
- ✅ Project builds successfully (`mvn clean package`)
- ✅ Target folder created with JAR
- ✅ MySQL running with FlipFit database
- ✅ Application runs without errors
- ✅ REST endpoints accessible at http://localhost:8080
- ✅ Health check works at http://localhost:8081/healthcheck

---

## Next Steps

1. **Build Project**: `mvn clean package -DskipTests`
2. **Run Application**: `java -jar target/flipfit-dropwizard-1.0.0-SNAPSHOT.jar server config.yml`
3. **Test Endpoints**: Use Postman or cURL
4. **Debug**: Set breakpoints and run in debug mode
5. **Deploy**: Package and deploy the JAR file

---

## Resources

- STS Documentation: https://spring.io/tools
- Dropwizard Docs: https://www.dropwizard.io/
- Maven Guide: https://maven.apache.org/guides/
- REST Best Practices: https://restfulapi.net/

---

**Status**: ✅ Ready to Import and Build in STS
