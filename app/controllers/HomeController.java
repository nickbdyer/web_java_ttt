package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import services.WebInterface;
import uk.nickbdyer.tictactoe.Board;
import uk.nickbdyer.tictactoe.Game;
import uk.nickbdyer.tictactoe.players.PlayerFactory;
import views.html.game;
import views.html.index;

import java.util.Map;

import static uk.nickbdyer.tictactoe.GameType.HvsH;

public class HomeController extends Controller {

    private Board board;
    private Game currentGame;
    private WebInterface ui;

    public Result index() {
        return ok(index.render("Please click below to start a new game!"));
    }

    public Result game() {
        if (board == null) {
            board = new Board();
        }
        return ok(game.render("Let's Play!", board.getCells(), board.getWinningMark(), board.isDraw()));
    }

    public Result newgame() {
        board = new Board();
        ui = new WebInterface();
        currentGame = new Game(new PlayerFactory(ui).create(HvsH));
        return redirect("/game");
    }

    public Result makeMove() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        ui.setLastInput(Integer.valueOf(request.get("position")[0]));
        currentGame.takeTurn(board, currentGame.getCurrentPlayer().choosePosition(board));
        return ok(game.render("Let's Play!", board.getCells(), board.getWinningMark(), board.isDraw()));
    }

}
