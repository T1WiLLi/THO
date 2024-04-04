package game.platformer.utils;

import game.platformer.Game;

public class Constants {

    public static final float GRAVITY = 0.04f * Game.getScale();
    public static final float ANIMATION_SPEED = 20;

    public static class ObjectConstants {
        public static final int GRASS = 0;
        public static final int SPIKE = 1;
        public static final int SPIKE_BALL = 2;
        public static final int ATTACH_SPIKE_POINT = 3;
        public static final int RUNE = 4;

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (Game.getScale() * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int) (Game.getScale() * SPIKE_HEIGHT_DEFAULT);
        public static final int SPIKED_BALL_WIDTH_DEFAULT = 28;
        public static final int SPIKED_BALL_HEIGHT_DEFAULT = 28;
        public static final int SPIKE_BALL_WIDTH = (int) (Game.getScale() * SPIKED_BALL_WIDTH_DEFAULT);
        public static final int SPIKE_BALL_HEIGHT = (int) (Game.getScale() * SPIKED_BALL_HEIGHT_DEFAULT);
        public static final int CHAIN_WIDTH_DEFAULT = 8;
        public static final int CHAIN_HEIGHT_DEFAULT = 8;
        public static final int CHAIN_WIDTH = (int) (Game.getScale() * CHAIN_WIDTH_DEFAULT);
        public static final int CHAIN_HEIGHT = (int) (Game.getScale() * CHAIN_HEIGHT_DEFAULT);
        public static final int RUNE_WIDTH_DEFAULT = 32;
        public static final int RUNE_HEIGHT_DEFAULT = 36;
        public static final int RUNE_WIDTH = (int) (Game.getScale() * RUNE_WIDTH_DEFAULT);
        public static final int RUNE_HEIGHT = (int) (Game.getScale() * RUNE_HEIGHT_DEFAULT);

        public static int getSpriteAmount(int object_type) {
            return switch (object_type) {
                case GRASS, SPIKE, SPIKE_BALL -> 1;
                case RUNE -> 11;
                default -> 1;
            };
        }
    }

    public static final class UI {
        public static final class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.getScale());
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.getScale());
        }

        public static final class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.getScale());
        }

        public static final class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.getScale());
        }

        public static final class VolumeButton {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.getScale());
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.getScale());
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.getScale());
        }
    }

    public static final class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int GROUND = 4;
        public static final int DEAD = 5;

        public static final int getSpriteAmount(int playerAction) {
            return switch (playerAction) {
                case DEAD -> 8;
                case RUNNING -> 6;
                case IDLE -> 5;
                case JUMP -> 3;
                case GROUND -> 2;
                case FALLING -> 1;
                default -> 1;
            };
        }
    }
}
