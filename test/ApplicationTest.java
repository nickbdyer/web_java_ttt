import org.junit.Test;
import play.twirl.api.Content;
import uk.nickbdyer.tictactoe.Board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.nickbdyer.tictactoe.Mark.EMPTY;
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
        Content html = views.html.game.render("Let's Play!", new Board().getCells(), EMPTY, false, false);
        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("Play!"));
        assertTrue(html.body().contains("<td>\n" +
                "                            <input type=\"hidden\" name=\"position\" value=\"0\">\n" +
                "                            <input type=\"submit\" value=\"\">\n" +
                "                        </td>"));
    }

    @Test
    public void announcesXWinner() {
        Content html = views.html.game.render("Let's Play!", new Board().getCells(), X, false, true);
        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("X is the winner!!"));
    }

    @Test
    public void announcesOWinner() {
        Content html = views.html.game.render("Let's Play!", new Board().getCells(), O, false, true);
        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("O is the winner!!"));
    }

    @Test
    public void announcesDraw() {
        Content html = views.html.game.render("Let's Play!", new Board().getCells(), EMPTY, true, true);
        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("It's a draw!!"));
    }

}
