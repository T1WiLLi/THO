package game.platformer.database;

import game.platformer.Game;
import game.platformer.gamestate.GameState;
import game.platformer.gamestate.Menu;
import game.platformer.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Connexion {

    private Menu menu;

    private Pane textFieldPane;
    private StackPane root;

    private Image backgroundImage;
    private int connexionX, connexionY, connexionWidth, connexionHeight;

    public Connexion(Menu menu, StackPane root) {
        this.menu = menu;
        loadBackground();
        loadTextFields(root);
    }

    private void loadBackground() {
        this.backgroundImage = LoadSave.getSprite(LoadSave.CONNEXION_BACKGROUND);
        this.connexionWidth = (int) (this.backgroundImage.getWidth() * Game.getScale());
        this.connexionHeight = (int) (this.backgroundImage.getHeight() * Game.getScale());
        this.connexionX = Game.getScreenWidth() / 6 - this.connexionWidth / 2;
        this.connexionY = (int) (Game.getScreenHeight() / 2 - this.connexionHeight / 2);
    }

    private void loadTextFields(StackPane root) {
        this.root = root;
        this.textFieldPane = new Pane();

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button connectButton = new Button("Connect");

        usernameField.setPrefWidth(200);
        passwordField.setPrefWidth(200);

        int fieldOffsetY = 50;

        usernameField.setLayoutX(connexionX + 175);
        usernameField.setLayoutY(connexionY + 80);

        passwordField.setLayoutX(connexionX + 175);
        passwordField.setLayoutY(connexionY + 60 + fieldOffsetY);

        connectButton.setPrefWidth(200);
        connectButton.setLayoutX(connexionX + 175);
        connectButton.setLayoutY(connexionY + 60 + 2 * fieldOffsetY);

        connectButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (User.logging(username, password)) {
                System.out.println("Logging!");
                this.menu.getGame().getUser().setUsername(username);
            } else if (User.register(username, password)) {
                System.out.println("Register!");
                this.menu.getGame().getUser().setUsername(username);
            } else {
                System.out.println("Error in connexion");
            }
        });

        textFieldPane.getChildren().addAll(usernameField, passwordField, connectButton);
        root.getChildren().add(textFieldPane);
    }

    private void loadLabels(GraphicsContext gc) {
        gc.setFont(new Font("Arial", 25));
        gc.setFill(Color.WHITE);

        int labelOffsetX = 50;
        int labelOffsetY = 100;

        gc.fillText("Username:", connexionX + labelOffsetX, connexionY + labelOffsetY);
        gc.fillText("Password:", connexionX + labelOffsetX, connexionY + labelOffsetY + 30);
    }

    public void update() {
        if (GameState.state == GameState.MENU) {
            this.textFieldPane.setVisible(true);
        }

    }

    public void render(GraphicsContext gc) {
        gc.drawImage(this.backgroundImage, connexionX, connexionY, connexionWidth, connexionHeight);
        loadLabels(gc);
    }

    public void hidePane() {
        this.textFieldPane.setVisible(false);
        this.root.requestFocus();
    }
}
