package ir.sharif.math.ap2023.project.model.player;

public enum Difficulty {
    EASY(0),
    MEDIUM(1),
    HARD(2);

    private int code;

    Difficulty(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Difficulty getByCode(int code) {
        for (Difficulty value : Difficulty.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
