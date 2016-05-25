package services;

import uk.nickbdyer.tictactoe.Board;
import uk.nickbdyer.tictactoe.Game;

import java.util.Map;

public class MoveHelper {

    public static void assignUserChoice(WebInterface ui, Map<String, String[]> request) {
        if(request.get("position") != null) {
            ui.setLastInput(Integer.valueOf(request.get("position")[0]));
        }
    }

    public static void makeNextMove(WebInterface ui, Game game, Board board) {
        if (game.getCurrentPlayer().choosePosition(board) != -1) {
            game.takeTurn(board, game.getCurrentPlayer().choosePosition(board));
            ui.setLastInput(-1);
        }
    }

}
