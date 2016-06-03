package services;

import uk.nickbdyer.tictactoe.Board;
import uk.nickbdyer.tictactoe.Game;
import uk.nickbdyer.tictactoe.UserInterface;

public class WebInterface implements UserInterface {

    private int lastInput;

    public void setLastInput(int position) {
       this.lastInput = position;
    }

    @Override
    public int getNumber() {
        return lastInput;
    }

    public String endGame(Board board) {
        if (board.isDraw()) {
            return "It's a draw!!";
        } else {
            String mark = String.valueOf(board.getWinningMark());
            return mark + " is the winner!!";
        }
    }

    public void assignUserChoice(int position) {
        setLastInput(position);
    }

    public void makeNextMove(Game game, Board board) {
        if (nextMoveIsValid(game, board)) {
            game.takeTurn(board, game.getCurrentPlayer().choosePosition(board));
        }
    }

    private boolean nextMoveIsValid(Game game, Board board) {
        return board.availableMoves().contains(game.getCurrentPlayer().choosePosition(board));
    }
}
