package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import services.MoveHelper;
import services.WebInterface;
import uk.nickbdyer.tictactoe.Board;
import uk.nickbdyer.tictactoe.Game;
import uk.nickbdyer.tictactoe.GameType;
import uk.nickbdyer.tictactoe.players.PlayerFactory;
import views.html.game;
import views.html.gameTypes;
import views.html.index;

import java.util.Map;

public class HomeController extends Controller {

    private Board board;
    private Game currentGame;
    private WebInterface ui;

    public Result index() {
        return ok(index.render("Please click below to start a new game!"));
    }

    public Result game() {
        return ok(game.render("Let's Play!", board.getCells(), ui.endGame(board), currentGame.isOver(board)));
    }

    public Result gameTypes() {
        return ok(gameTypes.render("Please choose a GameType", GameType.values()));
    }

    public Result newGame() {
        board = new Board();
        ui = new WebInterface();
        return redirect("/chooseGame");
    }

    public Result chooseGame() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        currentGame = new Game(new PlayerFactory(ui).create(GameType.values()[Integer.valueOf(request.get("gameType")[0])]));
        return redirect("/game");
    }

    public Result play() {
        MoveHelper.assignUserChoice(ui, request().queryString());
        MoveHelper.makeNextMove(ui, currentGame, board);
        return ok(game.render("Let's Play!", board.getCells(), ui.endGame(board), currentGame.isOver(board)));
    }

}
