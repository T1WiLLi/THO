package game.platformer.gamestate;

import game.platformer.Game;
import game.platformer.enities.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Menu extends State implements StateMethods {

    Player player;

    public Menu(Game game, Player player) {
        super(game);
        this.player = player;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext g) {
        g.setFill(Color.BLACK);
        g.setFont(Font.getDefault()); // You can set a specific font if needed
        g.fillText("MENU", Game.getGameWidth() / 2, 200);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            GameState.state = GameState.PLAYING;
            player.resetDirBooleans();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
