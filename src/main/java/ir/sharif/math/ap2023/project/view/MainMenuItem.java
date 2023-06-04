package ir.sharif.math.ap2023.project.view;

public enum MainMenuItem {
    NEW_GAME(1),
    CONTINUE(2),
    HIGHEST_SCORES(3),
    SHOP(4),
    PROFILE(5);

    private final int code;

    MainMenuItem(int code) {
        this.code = code;
    }

    public static MainMenuItem getByCode(int code) {
        for (MainMenuItem mainMenuItem : MainMenuItem.values()) {
            if (mainMenuItem.getCode() == code) {
                return mainMenuItem;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }
}
