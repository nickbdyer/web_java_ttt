package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import scala.Option;
import services.WebInterface;
import uk.nickbdyer.tictactoe.Board;
import uk.nickbdyer.tictactoe.Game;
import uk.nickbdyer.tictactoe.PlayerType;
import uk.nickbdyer.tictactoe.players.PlayerFactory;
import views.html.game;
import views.html.index;
import views.html.playerTypes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static uk.nickbdyer.tictactoe.Mark.O;
import static uk.nickbdyer.tictactoe.Mark.X;

public class HomeController extends Controller {

    private WebInterface ui = new WebInterface();
    private Map<String, Board> boardMap = new HashMap<>();
    private Map<String, Game> gameMap = new HashMap<>();

    public Board getBoard(String boardId) {
        return boardMap.get(boardId);
    }

    public Game getGame(String gameId) {
        return gameMap.get(gameId);
    }

    public Result index() {
        return ok(index.render("Please click below to start a new game!"));
    }

    public Result playerTypes() {
        return ok(playerTypes.render("Please choose a GameType", PlayerType.values()));
    }

    public Result newGame() {
        return redirect("/choosePlayers");
    }

    public Result setupGame() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        PlayerType player1 = PlayerType.values()[Integer.valueOf(request.get("player1")[0])];
        PlayerType player2 = PlayerType.values()[Integer.valueOf(request.get("player2")[0])];
        PlayerFactory pf = new PlayerFactory(ui);
        Board board = new Board();
        board.getCells();
        Game game = new Game(Arrays.asList(pf.create(player1, X), pf.create(player2, O)));

        boardMap.put(Integer.toString(board.hashCode()), board);
        gameMap.put(Integer.toString(game.hashCode()), game);
        session("game_id", Integer.toString(game.hashCode()));
        session("board_id", Integer.toString(board.hashCode()));

        return redirect("/play");
    }

    public Result play(Option<Integer> position) {
        Board board = getBoard(session("board_id"));
        Game currentGame = getGame(session("game_id"));
        if (position.isDefined() && !ui.nextMoveIsValid(board, currentGame.getCurrentPlayer().choosePosition(board))) {
            ui.makeNextMove(currentGame, board, position.get());
            return ok(game.render("Let's Play!", board.getCells(), ui.endGame(board), currentGame.isOver(board), Integer.toString(board.hashCode())));
        }
        ui.makeNextMove(currentGame, board, currentGame.getCurrentPlayer().choosePosition(board));
        return ok(game.render("Let's Play!", board.getCells(), ui.endGame(board), currentGame.isOver(board), Integer.toString(board.hashCode())));
    }

}
