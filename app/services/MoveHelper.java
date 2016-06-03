package services;

import uk.nickbdyer.tictactoe.Board;
import uk.nickbdyer.tictactoe.Game;

public class MoveHelper {

    public static void assignUserChoice(WebInterface ui, String position) {
        if(position != null) {
            ui.setLastInput(Integer.valueOf(position));
        }
    }

    public static void makeNextMove(WebInterface ui, Game game, Board board) {
        if (game.getCurrentPlayer().choosePosition(board) != -1) {
            game.takeTurn(board, game.getCurrentPlayer().choosePosition(board));
            ui.setLastInput(-1);
        }
    }

}
