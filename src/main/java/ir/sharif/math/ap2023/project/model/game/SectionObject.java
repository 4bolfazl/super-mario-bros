package ir.sharif.math.ap2023.project.model.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sharif.math.ap2023.project.model.block.BlockObject;
import ir.sharif.math.ap2023.project.model.enemy.EnemyObject;
import ir.sharif.math.ap2023.project.model.enemy.Piranha;
import ir.sharif.math.ap2023.project.model.pipe.PipeObject;

import java.util.ArrayList;
import java.util.List;

public class SectionObject {
    int length;
    int time;
    List<BlockObject> blocks = new ArrayList<>();
    List<EnemyObject> enemies = new ArrayList<>();
    List<PipeObject> pipes = new ArrayList<>();
    PipeObject spawnPipe;

    public SectionObject(int length, int time, List<BlockObject> blocks, List<EnemyObject> enemies, List<PipeObject> pipes, PipeObject spawnPipe) {
        this.length = length;
        this.time = time;
        this.blocks = blocks;
        this.enemies = enemies;
        this.pipes = pipes;
        this.spawnPipe = spawnPipe;
    }

    public SectionObject(int length, int time, List<BlockObject> blocks, List<EnemyObject> enemies, List<PipeObject> pipes) {
        this.length = length;
        this.time = time;
        this.blocks = blocks;
        this.enemies = enemies;
        this.pipes = pipes;
    }

    public SectionObject() {
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<BlockObject> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlockObject> blocks) {
        this.blocks = blocks;
    }

    public List<EnemyObject> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<EnemyObject> enemies) {
        this.enemies = enemies;
    }

    public List<PipeObject> getPipes() {
        return pipes;
    }

    public void setPipes(List<PipeObject> pipes) {
        this.pipes = pipes;
    }

    public PipeObject getSpawnPipe() {
        return spawnPipe;
    }

    public void setSpawnPipe(PipeObject spawnPipe) {
        this.spawnPipe = spawnPipe;
    }
}
