package game.platformer.utils;

import game.platformer.Game;

public class Constants {

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.getScale());
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.getScale());
        }
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int GROUND = 4;
        public static final int HIT = 5;

        public static int getSpriteAmount(int playerAction) {
            return switch (playerAction) {
                case RUNNING -> 6;
                case IDLE -> 5;
                case HIT -> 4;
                case JUMP -> 3;
                case GROUND -> 2;
                case FALLING -> 1;
                default -> 1;
            };
        }
    }
}
