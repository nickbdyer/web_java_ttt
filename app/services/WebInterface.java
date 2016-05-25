package services;

import uk.nickbdyer.tictactoe.Board;
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
}
