package game.platformer.input;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import game.platformer.Game;
import game.platformer.gamestate.GameState;

public class InputHandler implements EventHandler<KeyEvent> {

    private Game game;

    public InputHandler(Game game) {
        this.game = game;
    }

    @Override
    public void handle(KeyEvent event) {
        switch (GameState.state) {
            case MENU:
                game.getMenu().keyPressed(event);
                break;
            case PLAYING:
                game.getPlaying().keyPressed(event);
                break;
            default:
                break;
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        switch (GameState.state) {
            case MENU:
                game.getMenu().keyReleased(event);
                break;
            case PLAYING:
                game.getPlaying().keyReleased(event);
                break;
            default:
                break;
        }
    }
}
