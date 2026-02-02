package com.FracProPlus.utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

public class ElementUtils {

    private WebDriver driver;
    private final long EXPLICIT_WAIT = 20;

    public ElementUtils(WebDriver driver) {
        this.driver = driver;
    }

    // =========================================================
    // WAIT UTILITIES
    // =========================================================

    public WebElement waitForVisibility(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForAlert() {
        new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.alertIsPresent());
    }

    public void waitForLoaderDisappear(By loaderLocator) {
        new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));
    }

    public void waitForUrlContains(String fraction) {
        new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.urlContains(fraction));
    }

    public void waitForAjaxToComplete() {
        new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT)).until(
                wd -> ((JavascriptExecutor) wd)
                        .executeScript("return window.jQuery != null && jQuery.active === 0").equals(true));
    }

    // =========================================================
    // SAFE ACTIONS
    // =========================================================

    public void clickElement(By locator) {
        try {
            waitForClickable(locator).click();
        } catch (Exception e) {
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    public void retryClick(By locator, int attempts) {
        int count = 0;
        while (count < attempts) {
            try {
                driver.findElement(locator).click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                count++;
            }
        }
        throw new RuntimeException("Unable to click after retries: " + locator);
    }

    public void clearAndSendKeys(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void retryType(By locator, String text, int attempts) {
        int count = 0;
        while (count < attempts) {
            try {
                clearAndSendKeys(locator, text);
                return;
            } catch (StaleElementReferenceException e) {
                count++;
            }
        }
        throw new RuntimeException("Unable to type after retries: " + locator);
    }

    // =========================================================
    // JAVASCRIPT HELPERS
    // =========================================================

    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0);");
    }

    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public void highlight(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", element);
    }

    // =========================================================
    // VALIDATIONS
    // =========================================================

    public boolean isElementDisplayed(By locator) {
        return waitForVisibility(locator).isDisplayed();
    }

    public boolean isElementClickable(By locator) {
        return waitForClickable(locator).isEnabled();  // FIXED LOGIC
    }

    public String getElementText(By locator) {
        return waitForVisibility(locator).getText().trim();
    }

    public void verifyText(By locator, String expected) {
        Assert.assertEquals(getElementText(locator), expected);
    }

    public boolean isElementPresent(By locator) {
        return driver.findElements(locator).size() > 0;
    }

    // =========================================================
    // DROPDOWN
    // =========================================================

    public void selectByVisibleText(By locator, String text) {
        new Select(waitForVisibility(locator)).selectByVisibleText(text);
    }

    public void selectByValue(By locator, String value) {
        new Select(waitForVisibility(locator)).selectByValue(value);
    }

    public String getSelectedOption(By locator) {
        return new Select(waitForVisibility(locator)).getFirstSelectedOption().getText();
    }

    // =========================================================
    // FRAME HANDLING
    // =========================================================

    public void switchToFrame(By locator) {
        driver.switchTo().frame(waitForVisibility(locator));
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // =========================================================
    // ADVANCED USER ACTIONS
    // =========================================================

    public void hoverAndClick(By locator) {
        Actions actions = new Actions(driver);
        WebElement element = waitForVisibility(locator);
        actions.moveToElement(element).click().perform();
    }

    public void doubleClick(By locator) {
        Actions actions = new Actions(driver);
        actions.doubleClick(waitForVisibility(locator)).perform();
    }

    // =========================================================
    // ALERT HANDLING
    // =========================================================

    public void acceptAlert() {
        waitForAlert();
        driver.switchTo().alert().accept();
    }

    public void dismissAlert() {
        waitForAlert();
        driver.switchTo().alert().dismiss();
    }
}