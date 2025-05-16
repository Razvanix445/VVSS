package demo.steps.serenity;

import demo.pages.DictionaryPage;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;

public class EndUserSteps {

    DictionaryPage dictionaryPage;

    @Step
    public void entersUsername(String username) {
        dictionaryPage.enterUsername(username);
    }
    @Step
    public void entersPassword(String password) {
        dictionaryPage.enterPassword(password);
    }

    @Step
    public void pressLoginButton() {
        dictionaryPage.pressLoginButton();
    }

    @Step
    public void is_the_home_page() {
        dictionaryPage.open();
    }

    @Step
    public boolean assertShoppingCartExists(){
        return dictionaryPage.checkCart();
    }

    @Step
    public void addToCart(){
        dictionaryPage.addToCart();
    }

    @Step
    public void login(String username, String password) {
        entersUsername(username);
        entersPassword(password);
        pressLoginButton();
    }

    @Step
    public void addToCartStep(){
        addToCart();
    }

    @Step
    public void pressLogoutButton(){
        dictionaryPage.pressButtonThreePoints();
        dictionaryPage.pressLogoutButton();
    }

    @Step
    public boolean assertLoginButtonAfterLogout(){
        return dictionaryPage.checkLoginButtonAfterLogout();
    }

    @Step
    public void removeFromCart(){
        dictionaryPage.removeFromCart();
    }

    @Step
    public boolean assertButtonAddToCart(){
        return dictionaryPage.checkAddToCartVisibleButton();
    }

    @Step
    public boolean assertButtonChangedRemove(){
        return dictionaryPage.checkButtonChangedValue();
    }
}