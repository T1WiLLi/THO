package game.platformer.input;

import game.platformer.Game;
import game.platformer.gamestate.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseHandler implements EventHandler<MouseEvent> {
    private Game game;

    public MouseHandler(Game game) {
        this.game = game;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            handleMousePressed(event);
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            handleMouseReleased(event);
        } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            handleMouseMoved(event);
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            handleMouseDragged(event);
        }
    }

    private void handleMousePressed(MouseEvent event) {
        switch (GameState.state) {
            case MENU -> game.getMenu().mousePressed(event);
            case PLAYING -> game.getPlaying().mousePressed(event);
            case OPTIONS -> game.getGameOptions().mousePressed(event);
            case QUIT -> game.getMenu().mousePressed(event);
            default -> throw new IllegalArgumentException("Unexpected value: " + GameState.state);
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        switch (GameState.state) {
            case MENU -> game.getMenu().mouseReleased(event);
            case PLAYING -> game.getPlaying().mouseReleased(event);
            case OPTIONS -> game.getGameOptions().mouseReleased(event);
            case QUIT -> game.getMenu().mouseReleased(event);
            default -> throw new IllegalArgumentException("Unexpected value: " + GameState.state);
        }
    }

    private void handleMouseMoved(MouseEvent event) {
        switch (GameState.state) {
            case MENU -> game.getMenu().mouseMoved(event);
            case PLAYING -> game.getPlaying().mouseMoved(event);
            case OPTIONS -> game.getGameOptions().mouseMoved(event);
            case QUIT -> game.getMenu().mouseMoved(event);
            default -> throw new IllegalArgumentException("Unexpected value: " + GameState.state);
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        switch (GameState.state) {
            case PLAYING -> game.getPlaying().mouseDragged(event);
            case MENU -> game.getMenu().mouseDragged(event);
            case OPTIONS -> game.getGameOptions().mouseDragged(event);
            default -> throw new IllegalArgumentException("Unexpected value: " + GameState.state);
        }
    }
}
