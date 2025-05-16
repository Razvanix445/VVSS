package demo.pages;

import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import net.serenitybdd.core.pages.WebElementFacade;

import java.util.Objects;
import java.util.stream.Collectors;

import net.serenitybdd.core.annotations.findby.FindBy;

import net.thucydides.core.pages.PageObject;

import java.util.List;

@DefaultUrl("https://www.saucedemo.com/")
public class DictionaryPage extends PageObject {

    @FindBy(name="user-name")
    private WebElementFacade usernameHolder;

    @FindBy(name="password")
    private WebElementFacade passwordHolder;

    @FindBy(name="login-button")
    private WebElementFacade loginButton;

    @FindBy(name="add-to-cart-sauce-labs-onesie")
    private WebElementFacade cart;

    @FindBy(name="add-to-cart-sauce-labs-backpack")
    private WebElementFacade addToCartButton;

    @FindBy(name="remove-sauce-labs-backpack")
    private WebElementFacade removeCartButton;

    @FindBy(id="react-burger-menu-btn")
    private WebElementFacade buttonThreePoints;

    @FindBy(id="logout_sidebar_link")
    private WebElementFacade logoutButton;

    public void pressLoginButton() {
        loginButton.waitUntilVisible().waitUntilEnabled().waitUntilClickable();
        waitABit(200); // optional but helps React finish state updates
        loginButton.sendKeys(Keys.ENTER);
    }

    public void removeFromCart(){
        removeCartButton.waitUntilVisible().waitUntilEnabled().waitUntilClickable();
        waitABit(200);
        removeCartButton.sendKeys(Keys.ENTER);
    }

    public boolean checkAddToCartVisibleButton(){
        return addToCartButton.waitUntilVisible().isVisible();
    }

    public boolean checkLoginButtonAfterLogout(){
        return loginButton.waitUntilVisible().isVisible();
    }

    public void pressLogoutButton() {
        logoutButton.waitUntilVisible().waitUntilEnabled().waitUntilClickable();
        waitABit(1000); // optional but helps React finish state updates
        logoutButton.sendKeys(Keys.ENTER);
    }

    public void pressButtonThreePoints(){
        buttonThreePoints.waitUntilVisible().waitUntilEnabled().waitUntilClickable();
        waitABit(200); // optional but helps React finish state updates
        buttonThreePoints.sendKeys(Keys.ENTER);
    }

    public void addToCart(){
        addToCartButton.waitUntilVisible().waitUntilEnabled();
        addToCartButton.sendKeys(Keys.ENTER);
    }

    public void enterUsername(String username){
        usernameHolder.waitUntilVisible().waitUntilEnabled();
        usernameHolder.sendKeys(username);
    }

    public boolean checkButtonChangedValue(){
        return removeCartButton.waitUntilVisible().isVisible();
    }

    public void enterPassword(String password){
        passwordHolder.waitUntilVisible().waitUntilEnabled();
        passwordHolder.sendKeys(password);
    }

    public boolean checkCart(){
        try {
            return cart.waitUntilVisible().isVisible();
        } catch (Exception e){
            return false;
        }
    }
}