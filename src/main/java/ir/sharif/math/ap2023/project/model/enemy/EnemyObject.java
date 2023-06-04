package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "subclass")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Bowser.class, name = "bowserEnemy"),
        @JsonSubTypes.Type(value = Goompa.class, name = "goompaEnemy"),
        @JsonSubTypes.Type(value = Koopa.class, name = "koopaEnemy"),
        @JsonSubTypes.Type(value = NukeBird.class, name = "nukebirdEnemy"),
        @JsonSubTypes.Type(value = Spiny.class, name = "spinyEnemy"),
})
public abstract class EnemyObject {
    int x, y;
    EnemyType type;

    public EnemyObject(int x, int y, EnemyType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public EnemyObject() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public EnemyType getType() {
        return type;
    }

    public void setType(EnemyType type) {
        this.type = type;
    }
}
