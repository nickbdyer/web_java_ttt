import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;

public class IntegrationTest {

    @Test
    public void homePageTest() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333");
            assertTrue(browser.pageSource().contains("TicTacToe"));
        });
    }

    @Test
    public void redirectsFromNewGameToChooseGame() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333");
            browser.click("#newgame");
            assertEquals("/chooseGame", browser.url());
        });
    }

    @Test
    public void redirectsFromChooseGameToGame() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/choosePlayers");
            browser.click("#p1H");
            browser.click("#p2H");
            browser.click("#setupGame");
            assertEquals("/play", browser.url());
        });
    }

}

