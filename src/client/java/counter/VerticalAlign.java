package counter;

public enum VerticalAlign {
    TOP, MIDDLE, BOTTOM;

    public int calculateY(int screenHeight, int textHeight) {
        return switch (this) {
            case TOP -> 10;
            case MIDDLE -> (screenHeight - textHeight) / 2;
            case BOTTOM -> screenHeight - textHeight - 10;
        };
    }
}
