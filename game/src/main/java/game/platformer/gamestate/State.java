package game.platformer.gamestate;

import game.platformer.Game;
import game.platformer.audio.AudioPlayer;
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

    public void setGameState(GameState state) {
        switch (state) {
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().playSong(AudioPlayer.LEVEL_1);
            default -> throw new IllegalArgumentException("Unexpected value: " + state);
        }
        GameState.state = state;
    }
}
