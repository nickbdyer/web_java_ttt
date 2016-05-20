package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import uk.nickbdyer.tictactoe.Board;
import uk.nickbdyer.tictactoe.Game;
import uk.nickbdyer.tictactoe.Mark;
import uk.nickbdyer.tictactoe.players.PlayerFactory;
import views.html.game;
import views.html.index;

import java.util.Map;

import static uk.nickbdyer.tictactoe.GameType.HvsH;

public class HomeController extends Controller {

    private Board board;
    private Game GAME;

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result game() {
        if (board == null) {
            board = new Board();
        }
        return ok(game.render("Let's Play!", board.getCells()));
    }

    public Result newgame() {
        board = new Board();
        GAME = new Game(new PlayerFactory().create(HvsH));
        return redirect("/game");
    }

    public Result makeMove() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        board.mark(Integer.valueOf(request.get("position")[0]), Mark.X);
        return redirect("/game");
    }

}
