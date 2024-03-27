package game.platformer.input;

import game.platformer.Game;
import game.platformer.gamestate.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseHandler implements EventHandler<MouseEvent> {

    private Game game;

    public MouseHandler(Game gamePanel) {
        this.game = gamePanel;
    }

    @Override
    public void handle(MouseEvent event) {
        switch (GameState.state) {
            case MENU:
                game.getMenu().mouseClicked(event);
                break;
            case PLAYING:
                game.getPlaying().mouseClicked(event);
                break;
            default:
                break;
        }
    }
}
