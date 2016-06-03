package services;

import uk.nickbdyer.tictactoe.Board;
import uk.nickbdyer.tictactoe.Game;
import uk.nickbdyer.tictactoe.UserInterface;

public class WebInterface implements UserInterface {

    private int lastInput = -1;

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

    public void assignUserChoice(String position) {
        if(position != null) {
            setLastInput(Integer.valueOf(position));
        }
    }

    public void makeNextMove(Game game, Board board) {
        if (game.getCurrentPlayer().choosePosition(board) != -1) {
            game.takeTurn(board, game.getCurrentPlayer().choosePosition(board));
            setLastInput(-1);
        }
    }
}
