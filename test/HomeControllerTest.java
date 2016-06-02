import controllers.HomeController;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import uk.nickbdyer.tictactoe.players.DelayedComputer;
import uk.nickbdyer.tictactoe.players.Human;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.invokeWithContext;

public class HomeControllerTest extends WithApplication{

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
            .configure("play.http.router", "router.Routes")
            .build();
    }

    private HomeController homeController;

    @Before
    public void setUp() {
        homeController = new HomeController();
    }

    @Test
    public void testIndexPage() {
        Result result = homeController.index();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertTrue(contentAsString(result).contains("TicTacToe"));
    }

    @Test
    public void testNewGame() {
        Result result = homeController.newGame();
        assertEquals(SEE_OTHER, result.status());
        assertEquals("/chooseGame", result.header("Location").get());
    }

    @Test
    public void testGameTypes() {
        Result result = homeController.gameTypes();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertTrue(contentAsString(result).contains("Please choose a GameType"));
    }

    @Test
    public void testGameChoiceHvH() {
        Map form = new HashMap<String, String>();
        form.put("gameType", "0");
        Result result = invokeWithContext(Helpers.fakeRequest().bodyForm(form),
                () -> homeController.chooseGame());
        assertEquals(SEE_OTHER, result.status());
        assertEquals("/game", result.header("Location").get());
        assertTrue(homeController.getGame().getCurrentPlayer() instanceof Human);
    }

    @Test
    public void testGameChoiceAivAi() {
        Map form = new HashMap<String, String>();
        form.put("gameType", "3");
        Result result = invokeWithContext(Helpers.fakeRequest().bodyForm(form),
                () -> homeController.chooseGame());
        assertEquals(SEE_OTHER, result.status());
        assertEquals("/game", result.header("Location").get());
        assertTrue(homeController.getGame().getCurrentPlayer() instanceof DelayedComputer);
    }

    @Test
    public void testGameChoicePAivPAi() {
        Map form = new HashMap<String, String>();
        form.put("gameType", "6");
        Result result = invokeWithContext(Helpers.fakeRequest().bodyForm(form),
                () -> homeController.chooseGame());
        assertEquals(SEE_OTHER, result.status());
        assertEquals("/game", result.header("Location").get());
        assertTrue(homeController.getGame().getCurrentPlayer() instanceof DelayedComputer);
    }

    // Shows the board
    // Can make a move
    // Can not make a move if invalid
    // Shows who won

}
