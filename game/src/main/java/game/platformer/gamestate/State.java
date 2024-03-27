package game.platformer.gamestate;

import game.platformer.Game;
import game.platformer.ui.MenuButtons;
import javafx.scene.input.MouseEvent;

public class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButtons mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return this.game;
    }
}
