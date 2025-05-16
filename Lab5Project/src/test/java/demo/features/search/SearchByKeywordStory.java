package demo.features.search;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Issue;
import net.thucydides.core.annotations.Managed;
import static org.junit.Assert.*;
import net.thucydides.core.annotations.Pending;
//import net.thucydides.core.annotations.UseTestDataFrom;
import net.thucydides.junit.annotations.UseTestDataFrom;

import net.thucydides.core.annotations.Steps;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import demo.steps.serenity.EndUserSteps;

import java.util.Objects;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("src/test/resources/testdata/login-data.csv")
public class SearchByKeywordStory {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    @Steps
    public EndUserSteps anna;


    private String username;
    private String password;
    private String valid;

    public void login_only(){
        anna.is_the_home_page();
        anna.login("standard_user", "secret_sauce");
    }

    @Test
    public void login() {
        anna.is_the_home_page();
        anna.login(username, password);

        if (Objects.equals(valid, "true")) {
            assertTrue(anna.assertShoppingCartExists());
        } else {
            boolean result = anna.assertShoppingCartExists();
            System.out.println(result);
            assertTrue(!result);
            assertFalse(result);
        }
    }

    @Test
    public void addToCard(){
        login_only();
        anna.addToCartStep();
        assert anna.assertButtonChangedRemove();
    }

    @Test
    public void logout(){
        login_only();
        anna.pressLogoutButton();
        assert anna.assertLoginButtonAfterLogout();
    }

    @Test
    public void removeFromCart(){
        login_only();
        addToCard();
        anna.removeFromCart();
        assert anna.assertButtonAddToCart();
    }

}