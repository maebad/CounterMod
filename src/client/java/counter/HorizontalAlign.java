package counter;

public enum HorizontalAlign {
    LEFT, CENTER, RIGHT;

    public int calculateX(int screenWidth, int textWidth) {
        return switch (this) {
            case LEFT -> 10;
            case CENTER -> (screenWidth - textWidth) / 2;
            case RIGHT -> screenWidth - textWidth - 10;
        };
    }
}
