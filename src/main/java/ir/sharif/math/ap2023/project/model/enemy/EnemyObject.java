package ir.sharif.math.ap2023.project.model.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.awt.*;
import java.awt.image.BufferedImage;

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
    @JsonIgnore
    int frame = 0;
    @JsonIgnore
    Rectangle solidArea = new Rectangle();

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
        this.y = 10 - y;
    }

    public EnemyType getType() {
        return type;
    }

    public void setType(EnemyType type) {
        this.type = type;
    }

    public void addFrame() {
        frame++;
    }

    @JsonIgnore
    public int getFrame() {
        return frame;
    }

    @JsonIgnore
    public void setFrame(int frame) {
        this.frame = frame;
    }

    @JsonIgnore
    public Rectangle getSolidArea() {
        return solidArea;
    }


    public abstract BufferedImage getImage();
}
