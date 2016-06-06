package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import scala.Option;
import services.WebInterface;
import uk.nickbdyer.tictactoe.Board;
import uk.nickbdyer.tictactoe.Game;
import uk.nickbdyer.tictactoe.GameType;
import uk.nickbdyer.tictactoe.players.PlayerFactory;
import views.html.game;
import views.html.gameTypes;
import views.html.index;

import java.util.HashMap;
import java.util.Map;

public class HomeController extends Controller {

    private WebInterface ui = new WebInterface();
    private Map<String, Board> boardMap = new HashMap<>();
    private Map<String, Game> gameMap = new HashMap<>();

    public Board getBoard() {
        return boardMap.get(session("board_id"));
    }

    public Game getGame() {
        return gameMap.get(session("game_id"));
    }

    public Result index() {
        return ok(index.render("Please click below to start a new game!"));
    }

    public Result gameTypes() {
        return ok(gameTypes.render("Please choose a GameType", GameType.values()));
    }

    public Result newGame() {
        session().clear();
        return redirect("/chooseGame");
    }

    public Result chooseGame() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        Board board = new Board();
        Game game = new Game(new PlayerFactory(ui).create(GameType.values()[Integer.valueOf(request.get("gameType")[0])]));
        boardMap.put(Integer.toString(board.hashCode()), board);
        gameMap.put(Integer.toString(game.hashCode()), game);
        session("game_id", Integer.toString(game.hashCode()));
        session("board_id", Integer.toString(board.hashCode()));
        return redirect("/play");
    }

    public Result play(Option<Integer> position) {
        Board board = boardMap.get(session("board_id"));
        Game currentGame = gameMap.get(session("game_id"));
        if (position.isDefined() && !ui.nextMoveIsValid(board, currentGame.getCurrentPlayer().choosePosition(board))) {
            ui.makeNextMove(currentGame, board, position.get());
            return ok(game.render("Let's Play!", board.getCells(), ui.endGame(board), currentGame.isOver(board), Integer.toString(board.hashCode())));
        }
        ui.makeNextMove(currentGame, board, currentGame.getCurrentPlayer().choosePosition(board));
        return ok(game.render("Let's Play!", board.getCells(), ui.endGame(board), currentGame.isOver(board), Integer.toString(board.hashCode())));
    }

}
