package services;

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
}
