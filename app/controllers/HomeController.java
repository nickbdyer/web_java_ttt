package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import uk.nickbdyer.tictactoe.Board;
import uk.nickbdyer.tictactoe.Game;
import uk.nickbdyer.tictactoe.WebInterface;
import uk.nickbdyer.tictactoe.players.PlayerFactory;
import views.html.game;
import views.html.index;

import java.util.Map;

import static uk.nickbdyer.tictactoe.GameType.HvsH;

public class HomeController extends Controller {

    private Board board;
    private Game currentGame;

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result game() {
        if (board == null) {
            board = new Board();
        }
        return ok(game.render("Let's Play!", board.getCells(), board.getWinningMark(), board.isDraw()));
    }

    public Result newgame() {
        board = new Board();
        currentGame = new Game(new PlayerFactory().create(HvsH, new WebInterface()));
        return redirect("/game");
    }

    public Result makeMove() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        board.mark(Integer.valueOf(request.get("position")[0]), currentGame.getCurrentPlayer().getMark());
        currentGame.swapPlayers();
        return ok(game.render("Let's Play!", board.getCells(), board.getWinningMark(), board.isDraw()));
    }

}
