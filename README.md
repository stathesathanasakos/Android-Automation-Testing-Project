# AppiumFramework – Android Test Automation with Appium, TestNG & Page Object Model

An automated testing framework for Android mobile apps, built with **Appium (java-client)**, **Selenium**, **TestNG**, and **Maven**. It's used as a demo project against the **General Store** app and covering some basic scenarios of this app, like filling out the user form, adding products to the cart, verifying the total amount, and completing a purchase. You can find the app following this link (https://rahulshettyacademy.com/practice) and then searching for 'General Store apk')

---

## Architecture

In this project I follow the **Page Object Model (POM)**, with a clear separation between:

- **Test Cases** (`testCases/android`) – the actual test scenarios (TestNG `@Test` methods)
- **Page Objects** (`pageObjects/android`) – one class per app screen, exposing only business-level actions (e.g. `setNameField`, `addProductToCart`)
- **Utils** (`utils`) – reusable helper classes (gestures, JSON parsing, Appium server management)
- **Test Utils** (`testUtils`) – the test framework's own infrastructure (BaseTest, Listeners, reporting)

```
                 AppiumUtil            AndroidActions
                     |                       |
                     v                       v
   BaseTest  ------------------->  Listeners ···> ExtentReportNG
      |  \
      |   \
      v    v
EcommerceAppFinalCheck   EcommerceSmallTest
      |
      v
FormPage   CartPage   ProductsCatalogue   (extends AndroidActions)
```

**Legend:**
- `——▶` = extends (inheritance)
- `····▶` = uses (composition)

More specifically:
- The **Page Objects** (`FormPage`, `CartPage`, `ProductsCatalogue`) `extend AndroidActions`, giving them direct access to shared gestures (scroll, long-press, swipe).
- The **Test Cases** (`EcommerceSmallTest`, `EcommerceAppFinalCheck`) `extend BaseTest`, inheriting the lifecycle (driver setup/teardown, Appium server, etc.).
- `BaseTest` uses `AppiumUtil` to start the Appium server and read data from JSON.
- `Listeners` (a TestNG `ITestListener`) hooks into `ExtentReportNG` to generate HTML reports after each run.

---

## Tech Stack & Dependencies

The project is Maven-based (`pom.xml`). Key dependencies:

| Library | Version | Purpose |
|---|---|---|
| `io.appium:java-client` | 9.4.0 | Appium client for Android (driver, PageFactory decorators, mobile gestures) |
| `org.seleniumhq.selenium:selenium-java` | 4.28.0 | Pinned Selenium version, compatible with java-client (the version pulled in transitively by appium-java-client is excluded) |
| `org.testng:testng` | 7.7.0 | Test runner, annotations, DataProvider, groups |
| `commons-io:commons-io` | 2.14.0 | File reading (screenshots, JSON) |
| `com.fasterxml.jackson.core:jackson-databind` | 2.18.9 | Parsing JSON test data into `List<HashMap<String,String>>` |
| `com.aventstack:extentreports` | 4.1.3 | HTML test report generation |

> Java version: **11** (`maven.compiler.release=11`)

---

## ⚙️ Setup

### 1. Prerequisites

- **Java JDK 11**
- **Maven** (3.8+)
- **Node.js & npm** (to install the Appium server)
- **Appium server** (install with `npm install -g appium`) + the `uiautomator2` driver:
  ```
  appium driver install uiautomator2
  ```
- **Android SDK / Android Studio** with an emulator set up, or a connected physical device (`adb devices` should list it)
- **Appium Inspector** (optional, for locating elements)


### 2. Configure Appium capabilities

Tests run with the following desired capabilities (via Appium Inspector or your own driver setup):

```json
{
  "platformName": "android",
  "appium:automationName": "UIAutomator2",
  "appium:deviceName": "emulator/device name",
  "appium:app": "<path-to-apk>"
}
```

> In your own `BaseTest`/driver setup, make sure the `app` path and `deviceName` match your device/emulator.

### 3. Starting the Appium Server

The project can start the server programmatically via `AppiumUtil.startAppiumServer(ipAddress, port)` (using `AppiumServiceBuilder`), but you can also just run it manually from the command line:

```bash
appium
```

> ⚠️ `AppiumUtil.java` currently has a hardcoded path to `node_modules/appium/build/lib/main.js` (a Windows path). If you run this on a different machine, you'll need to update it to your own path, or drop `.withAppiumJS(...)` entirely if Appium is already installed globally and available on your `PATH`.

### 4. Running the tests

Tests run via TestNG suite XML files. Example (`testng_DemoGroup.xml`):

```bash
mvn test -DsuiteXmlFile=testng_DemoGroup.xml
```

or directly from your IDE (right-click the `.xml` file → Run as TestNG Suite).

---

## Design Patterns Used

### Page Object Model (POM) + PageFactory
Each app screen is represented by its own class (`FormPage`, `CartPage`, `ProductsCatalogue`). Web elements are declared with `@AndroidFindBy` annotations and initialized via:

```java
PageFactory.initElements(new AppiumFieldDecorator(driver), this);
```

This gives **lazy initialization** of elements and keeps test code free of locators — tests just call methods like `formPage.setCountrySelection(country)`.

### Shared Driver via Inheritance
All page objects `extend AndroidActions`, and the driver is passed once through the constructor:

```java
public FormPage(AndroidDriver driver) {
    super(driver);              // the same driver is passed into AndroidActions
    this.driver = driver;       // local reference for use inside this class
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
}
```

This avoids creating multiple driver instances — all classes share the same session.

### Utility / Helper Layer
- **`AndroidActions`** – base class with shared, reusable gestures (long press, scroll to text, scroll to end, swipe), implemented via `mobile:` JavascriptExecutor commands. Inherited by all page objects.
- **`AppiumUtil`** – responsible for: (a) starting the Appium server, (b) loading test data from JSON files, (c) capturing a screenshot when a test fails.

### BaseTest / Listener / Reporting layer
- **`BaseTest`** – contains the shared setup/teardown lifecycle (driver capabilities, session management) inherited by all test classes.
- **`Listeners`** (`ITestListener`) – listens to TestNG events (onTestSuccess, onTestFailure, etc.) and feeds `ExtentReportNG`, which automatically generates an HTML report with the outcome of each test.

### Data-Driven Testing
Input data (e.g. country/name/gender) isn't hardcoded in the tests — it's read from a JSON file:

```java
@DataProvider
public Object[][] getData() throws IOException {
    List<HashMap<String, String>> data = getJsonData(
        System.getProperty("user.dir") + "\\src\\test\\java\\org\\mellonStathis\\testData\\EcommerceFullData.json");
    return new Object[][] {{data.get(0)}, {data.get(1)}};
}
```

`AppiumUtil.getJsonData(...)` uses:
1. **commons-io** to read the file into a String
2. **Jackson (`ObjectMapper`)** to convert it into a `List<HashMap<String,String>>`

Each `HashMap` corresponds to one dataset (one test run), and TestNG automatically runs the `@Test` once per data row via the `dataProvider` attribute:

```java
@Test(dataProvider = "getData", groups = {"DemoGroup"})
public void fillTheFirstPageTest(HashMap<String, String> input) throws InterruptedException {
    formPage.setCountrySelection(input.get("country"));
    formPage.setNameField(input.get("name"));
    formPage.setGender(input.get("gender"));
}
```

This makes it easy to extend test cases — a new dataset just means a new row in the JSON file, no code changes needed.

### TestNG Groups
`testng_DemoGroup.xml` lets you run only specific groups of tests selectively (e.g. `DemoGroup`) — useful for separating smoke tests from full regression suites.

---

## Locator Strategy

- Where a stable **resource-id** exists (`com.androidsample.generalstore:id/...`), it's used first, since it's more resilient to UI changes.
- Where no id is available (e.g. dynamic radio buttons, an id-less checkbox), **XPath** is used based on the view hierarchy, identified via **Appium Inspector**.
- For scrollable lists (e.g. the country list, the products list), `UiScrollable`/`UiSelector` is used via `AppiumBy.androidUIAutomator(...)`.

---



## Possible Future Improvements

- Replace `Thread.sleep(...)` calls with explicit `WebDriverWait` / `ExpectedConditions` (there's already an example of this in `CheckAndSumManyItemsTest`).
- Externalize capabilities (device name, app path, server IP/port) into a `config.properties`/`config.json` file instead of hardcoding them.
- Cross-platform support (iOS) via a shared abstract base class on top of `AppiumUtil`.
- CI integration (e.g. GitHub Actions) with an emulator matrix.

---

## References

- [Appium UiAutomator2 Driver – Mobile Gestures](https://github.com/appium/appium-uiautomator2-driver/blob/master/docs/android-mobile-gestures.md)
- [TestNG on Maven Central](https://mvnrepository.com/artifact/org.testng/testng/7.7.0)
