import org.junit.Test;
import play.twirl.api.Content;
import uk.nickbdyer.tictactoe.Board;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.nickbdyer.tictactoe.Mark.O;
import static uk.nickbdyer.tictactoe.Mark.X;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("TicTacToe");
        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("TicTacToe"));
    }

    @Test
    public void renderGamePage() {
        Content html = views.html.game.render("Let's Play!", new Board().getCells(), null, false);
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("Let&#x27;s Play!"));
    }

    @Test
    public void announcesXWinner() {
        Content html = views.html.game.render("Let's Play!", new Board().getCells(), "X is the winner!!", true);
        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("X is the winner!!"));
    }

    @Test
    public void announcesOWinner() {
        Content html = views.html.game.render("Let's Play!", new Board().getCells(), "O is the winner!!", true);
        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("O is the winner!!"));
    }

    @Test
    public void announcesDraw() {
        Content html = views.html.game.render("Let's Play!", Arrays.asList(X, O, X, O, O, X, X, X, O), "It's a draw!!", true);
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("It&#x27;s a draw!!"));
    }

}
