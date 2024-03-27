package game.platformer.gamestate;

import game.platformer.Game;

public class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return this.game;
    }
}
