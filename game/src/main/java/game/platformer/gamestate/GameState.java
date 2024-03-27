package game.platformer.gamestate;

public enum GameState {
    PLAYING,
    MENU,
    OPTIONS,
    QUIT;

    public static GameState state = MENU;
}
