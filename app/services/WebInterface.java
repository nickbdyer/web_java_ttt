package services;

import uk.nickbdyer.tictactoe.Board;
import uk.nickbdyer.tictactoe.Game;
import uk.nickbdyer.tictactoe.UserInterface;

public class WebInterface implements UserInterface {

    @Override
    public int getNumber() {
        return -1;
    }

    public String endGame(Board board) {
        if (board.isDraw()) {
            return "It's a draw!!";
        } else {
            String mark = String.valueOf(board.getWinningMark());
            return mark + " is the winner!!";
        }
    }

    public void makeNextMove(Game game, Board board, int position) {
        if (nextMoveIsValid(board, position)) {
            game.takeTurn(board, position);
        }
    }

    private boolean nextMoveIsValid(Board board, int position) {
        return board.availableMoves().contains(position);
    }
}
