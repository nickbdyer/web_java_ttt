import akka.routing.Router;
import com.google.inject.Inject;
import controllers.HomeController;
import controllers.routes;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import scala.Option;
import uk.nickbdyer.tictactoe.players.DelayedComputer;
import uk.nickbdyer.tictactoe.players.DumbComputer;
import uk.nickbdyer.tictactoe.players.Human;
import uk.nickbdyer.tictactoe.players.PerfectComputer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Controller.session;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;
import static uk.nickbdyer.tictactoe.Mark.*;

public class HomeControllerTest extends WithApplication {

    private HomeController homeController;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
            .configure("play.http.router", "router.Routes")
            .build();
    }

    @Before
    public void setUp() throws Exception {
        homeController = new HomeController();
    }

    @After
    public void tearDown() {
        Context.current.remove();
    }

    @Test
    public void testIndexPage() {
        Result result = new HomeController().index();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertTrue(contentAsString(result).contains("TicTacToe"));
    }

    @Test
    public void testNewGame() {
        Result result = new HomeController().newGame();
        assertEquals(SEE_OTHER, result.status());
        assertEquals("/choosePlayers", result.header("Location").get());
    }

    @Test
    public void testplayerTypes() {
        Result result = new HomeController().playerTypes();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertTrue(contentAsString(result).contains("Please choose players:"));
    }

    @Test
    public void setupGameRedirectsToPlay() {
        Map form = new HashMap<String, String>();
        form.put("player1", "0");
        form.put("player2", "0");

        Result result = route(fakeRequest(routes.HomeController.setupGame()).bodyForm(form));

        assertEquals(SEE_OTHER, result.status());
        assertEquals("/play", result.header("Location").get());
    }

    @Test
    public void testGameChoiceHvsH() {
        Map form = new HashMap<String, String>();
        form.put("player1", "0");
        form.put("player2", "0");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));

        homeController.setupGame();

        assertFalse(Context.current().session().isEmpty());
        String gameId = Context.current().session().get("game_id");
        assertTrue(homeController.getGame(gameId).getCurrentPlayer() instanceof Human);
    }

    @Test
    public void testGameChoiceAivAi() {
        Map form = new HashMap<String, String>();
        form.put("player1", "1");
        form.put("player2", "1");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));

        homeController.setupGame();

        assertFalse(Context.current().session().isEmpty());
        String gameId = Context.current().session().get("game_id");
        assertTrue(homeController.getGame(gameId).getCurrentPlayer() instanceof DumbComputer);
    }

    @Test
    public void testGameChoicePAivPAi() {
        Map form = new HashMap<String, String>();
        form.put("player1", "2");
        form.put("player2", "2");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));

        homeController.setupGame();

        assertFalse(Context.current().session().isEmpty());
        String gameId = Context.current().session().get("game_id");
        assertTrue(homeController.getGame(gameId).getCurrentPlayer() instanceof PerfectComputer);
    }

    @Test
    public void testShowTheBoard() {
        Map form = new HashMap<String, String>();
        form.put("player1", "0");
        form.put("player2", "0");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));

        homeController.setupGame();

        String boardId = Context.current().session().get("board_id");
        assertEquals(Arrays.asList(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY), homeController.getBoard(boardId).getCells());
    }

    @Test
    public void playRouteWithFormReturnsOK() {
        Map form = new HashMap<String, String>();
        form.put("player1", "0");
        form.put("player2", "0");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));
        homeController.setupGame();

        Result result = homeController.play(Option.apply(0));

        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }

    @Test
    public void playRouteWithoutFormReturnsOK() {
        Map form = new HashMap<String, String>();
        form.put("player1", "0");
        form.put("player2", "1");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));
        homeController.setupGame();

        homeController.play(Option.apply(0));

        Result result = homeController.play(Option.empty());
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }

    @Test
    public void testMakeAMove() {
        Map form = new HashMap<String, String>();
        form.put("player1", "0");
        form.put("player2", "0");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));
        homeController.setupGame();

        homeController.play(Option.apply(0));

        String boardId = Context.current().session().get("board_id");
        assertEquals(Arrays.asList(X, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY), homeController.getBoard(boardId).getCells());
    }

    @Test
    public void testInvalidMoveNotMarked() {
        Map form = new HashMap<String, String>();
        form.put("player1", "0");
        form.put("player2", "0");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));
        homeController.setupGame();

        homeController.play(Option.apply(0));
        homeController.play(Option.apply(0));

        String boardId = Context.current().session().get("board_id");
        assertEquals(Arrays.asList(X, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY), homeController.getBoard(boardId).getCells());
    }

    @Test
    public void testNoInputFromUser() {
        Map form = new HashMap<String, String>();
        form.put("player1", "0");
        form.put("player2", "0");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));
        homeController.setupGame();

        homeController.play(Option.empty());

        String boardId = Context.current().session().get("board_id");
        assertEquals(Arrays.asList(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY), homeController.getBoard(boardId).getCells());
    }

    @Test
    public void testHumanCannotTakeComputerMove() {
        Map form = new HashMap<String, String>();
        form.put("player1", "0");
        form.put("player2", "2");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));
        homeController.setupGame();

        homeController.play(Option.apply(0));
        homeController.play(Option.apply(1));
        homeController.play(Option.empty());

        Result result = homeController.play(Option.empty());

        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        String boardId = Context.current().session().get("board_id");
        assertEquals(Arrays.asList(X, EMPTY, EMPTY, EMPTY, O, EMPTY, EMPTY, EMPTY, EMPTY), homeController.getBoard(boardId).getCells());

    }

    @Test
    public void testXWinner() {
        Map form = new HashMap<String, String>();
        form.put("player1", "0");
        form.put("player2", "0");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));
        homeController.setupGame();

        homeController.play(Option.apply(0));
        homeController.play(Option.apply(3));
        homeController.play(Option.apply(1));
        homeController.play(Option.apply(4));
        homeController.play(Option.apply(2));
        Result result = homeController.play(Option.empty());

        assertTrue(contentAsString(result).contains("X is the winner!!"));
        String boardId = Context.current().session().get("board_id");
        assertEquals(Arrays.asList(X, X, X, O, O, EMPTY, EMPTY, EMPTY, EMPTY), homeController.getBoard(boardId).getCells());
    }

    @Test
    public void testOWinner() {
        Map form = new HashMap<String, String>();
        form.put("player1", "0");
        form.put("player2", "0");
        RequestBuilder request = fakeRequest(routes.HomeController.setupGame()).bodyForm(form);
        Context.current.set(new Context(request));
        homeController.setupGame();

        homeController.play(Option.apply(8));
        homeController.play(Option.apply(0));
        homeController.play(Option.apply(3));
        homeController.play(Option.apply(1));
        homeController.play(Option.apply(4));
        homeController.play(Option.apply(2));
        Result result = homeController.play(Option.empty());

        assertTrue(contentAsString(result).contains("O is the winner!!"));
        String boardId = Context.current().session().get("board_id");
        assertEquals(Arrays.asList(O, O, O, X, X, EMPTY, EMPTY, EMPTY, X), homeController.getBoard(boardId).getCells());
    }

}
