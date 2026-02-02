package com.FracProPlus.utils;  // Package where utility class lives

// Java AWT classes used for Robot (keyboard/mouse) operations
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

// File handling
import java.io.File;
import java.io.IOException;

// Time & utility classes
import java.time.Duration;
import java.util.*;

// Logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Selenium core
import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.*;

// Test validation
import org.testng.Assert;

// Cucumber scenario for screenshot embedding
import io.cucumber.java.Scenario;

public class CommonUtils {

    // ===== Framework-level wait timings =====
    public static final int IMPLICIT_WAIT_TIME = 10;      // Default implicit wait
    public static final int PAGE_LOAD_TIME = 15;          // Page load wait time
    public static final int EXPLICIT_WAIT_BASIC_TIME = 20;// Explicit wait max time

    // Logger instance for logging messages
    private static Logger logger = LogManager.getLogger(CommonUtils.class);


    // ================= EXISTING METHODS =================

    // Generates a unique email using current timestamp
    public static String getEmailWithTimeStamp() {
        Date date = new Date();  // Get current system date/time
        String newEmail = "newemail" + date.toString().replace(" ", "").replace(":", "") + "@gmail.com";
        logger.info("Generated email: " + newEmail);  // Log generated email
        return newEmail;
    }

    // Capture screenshot in Cucumber and return as byte array
    public static byte[] takeScreenShot(Scenario scenario, WebDriver driver, String scenarioName) {
        logger.info("Taking screenshot for scenario: " + scenarioName);
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES); // Capture screenshot as bytes
    }

    // Scroll page gradually until bottom (handles lazy loading pages)
    public static void scrollToBottom(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;  // JS executor
        long lastHeight = (long) js.executeScript("return document.body.scrollHeight"); // Initial height

        while (true) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);"); // Scroll to bottom
            sleep(2); // Wait for content load
            long newHeight = (long) js.executeScript("return document.body.scrollHeight");
            if (newHeight == lastHeight) break; // Stop if no new content
            lastHeight = newHeight;
        }
    }

    // Scroll back to top smoothly
    public static void scrollToTop(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while ((long) js.executeScript("return window.pageYOffset") > 0) {
            js.executeScript("window.scrollBy(0, -1000);"); // Scroll upward
            sleep(1);
        }
    }

    // Verify selected dropdown option index
    public static void DropdownSelectedOptionVerification(WebElement dropdownElement, int index, String msg) {
        Select dropdown = new Select(dropdownElement);  // Create Select object
        WebElement selectedOption = dropdown.getFirstSelectedOption(); // Get selected option
        Assert.assertEquals(dropdown.getOptions().indexOf(selectedOption), index, msg); // Assert index
    }


    // ================= NEW ENTERPRISE UTILITIES =================

    // 📸 Capture screenshot and save file to /screenshots folder
    public static String captureScreenshot(WebDriver driver, String name) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE); // Capture screenshot
        String path = System.getProperty("user.dir") + "/screenshots/" + name + ".png"; // Build file path
        try { FileHandler.copy(src, new File(path)); } // Save screenshot
        catch (IOException e) { e.printStackTrace(); }
        return path; // Return saved file path
    }


    // 🪟 Switch to newly opened browser window
    public static void switchToNewWindow(WebDriver driver) {
        String current = driver.getWindowHandle(); // Current window ID
        for (String win : driver.getWindowHandles())
            if (!win.equals(current)) driver.switchTo().window(win);
    }

    // Switch back to parent (first) window
    public static void switchToParentWindow(WebDriver driver) {
        driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(0));
    }


    // 📂 Upload file directly to input[type=file]
    public static void uploadFile(WebElement element, String path) {
        element.sendKeys(path); // Selenium sends file path
    }

    // Upload file using Robot for native OS dialog
    public static void uploadFileUsingRobot(String filePath) {
        try {
            setClipboardData(filePath); // Copy path to clipboard
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL); robot.keyPress(KeyEvent.VK_V); // Paste
            robot.keyRelease(KeyEvent.VK_V); robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER); robot.keyRelease(KeyEvent.VK_ENTER); // Press Enter
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Helper method to copy text to system clipboard
    private static void setClipboardData(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(text), null);
    }


    // 🎲 Random string generator
    public static String getRandomString(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }

    // Random numeric string
    public static String getRandomNumber(int digits) {
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < digits; i++) sb.append(r.nextInt(10));
        return sb.toString();
    }

    // Random email using timestamp
    public static String getRandomEmail() {
        return "user" + System.currentTimeMillis() + "@test.com";
    }


    // 🚨 Check if alert is present safely
    public static boolean isAlertPresent(WebDriver driver) {
        try { driver.switchTo().alert(); return true; }
        catch (NoAlertPresentException e) { return false; }
    }

    public static void acceptAlert(WebDriver driver) { driver.switchTo().alert().accept(); }
    public static void dismissAlert(WebDriver driver) { driver.switchTo().alert().dismiss(); }


    // ⏳ Wait until element is visible
    public static void waitForElementVisible(WebDriver driver, WebElement element) {
        new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_BASIC_TIME))
                .until(ExpectedConditions.visibilityOf(element));
    }

    // Wait until element is clickable
    public static void waitForElementClickable(WebDriver driver, WebElement element) {
        new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_BASIC_TIME))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    // Wait until page completely loads
    public static void waitForPageLoad(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(PAGE_LOAD_TIME)).until(
                wd -> ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }


    // 🔄 Browser utilities
    public static void refreshPage(WebDriver driver) { driver.navigate().refresh(); }
    public static void maximizeWindow(WebDriver driver) { driver.manage().window().maximize(); }


    // Common sleep helper
    public static void sleep(int seconds) {
        try { Thread.sleep(seconds * 1000L); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}