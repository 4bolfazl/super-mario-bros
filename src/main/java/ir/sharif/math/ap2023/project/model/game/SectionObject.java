package ir.sharif.math.ap2023.project.model.game;

import ir.sharif.math.ap2023.project.model.block.BlockObject;
import ir.sharif.math.ap2023.project.model.block.Flag;
import ir.sharif.math.ap2023.project.model.block.NothingBlockObject;
import ir.sharif.math.ap2023.project.model.checkpoint.Checkpoint;
import ir.sharif.math.ap2023.project.model.enemy.Bowser;
import ir.sharif.math.ap2023.project.model.enemy.EnemyObject;
import ir.sharif.math.ap2023.project.model.pipe.PipeObject;
import ir.sharif.math.ap2023.project.model.pipe.PipeType;
import ir.sharif.math.ap2023.project.model.pipe.TrunkPipe;

import java.util.ArrayList;
import java.util.List;

public class SectionObject {
    public List<NothingBlockObject> nothingBlockObjects = new ArrayList<>();
    public List<TrunkPipe> trunkPipes = new ArrayList<>();
    int length;
    int time;
    List<BlockObject> blocks = new ArrayList<>();
    List<EnemyObject> enemies = new ArrayList<>();
    List<PipeObject> pipes = new ArrayList<>();
    PipeObject spawnPipe;
    boolean bossSection = false;
    Flag flag;
    Checkpoint checkpoint;

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

    public boolean isBossSection() {
        return bossSection;
    }

    public void setBossSection(boolean bossSection) {
        this.bossSection = bossSection;
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
        for (BlockObject block : blocks) {
            boolean leftest = true;
            boolean rightest = true;
            for (BlockObject blockObject : blocks) {
                if (blockObject.getY() == block.getY() && blockObject.getX() + 1 == block.getX()) {
                    leftest = false;
                    break;
                }
            }
            for (BlockObject blockObject : blocks) {
                if (blockObject.getY() == block.getY() && blockObject.getX() - 1 == block.getX()) {
                    rightest = false;
                    break;
                }
            }
            if (leftest) {
                NothingBlockObject nothing = new NothingBlockObject(block.getX() - 1, block.getY() - 1);
                nothingBlockObjects.add(nothing);
            }
            if (rightest) {
                NothingBlockObject nothing = new NothingBlockObject(block.getX() + 1, block.getY() - 1);
                nothingBlockObjects.add(nothing);
            }
        }
    }

    public List<EnemyObject> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<EnemyObject> enemies) {
        this.enemies = enemies;
        for (EnemyObject enemy : enemies) {
            if (enemy instanceof Bowser) {
                setBossSection(true);
                nothingBlockObjects.clear();
            }
        }
    }

    public List<PipeObject> getPipes() {
        return pipes;
    }

    public void setPipes(List<PipeObject> pipes) {
        this.pipes = pipes;
        for (PipeObject pipe : pipes) {
            if (pipe.getY() < 8) {
                for (int i = pipe.getY() + 2; i < 10; i += 2) {
                    trunkPipes.add(new TrunkPipe(pipe.getX(), i, PipeType.TRUNK));
                }
            }
        }
    }

    public PipeObject getSpawnPipe() {
        return spawnPipe;
    }

    public void setSpawnPipe(PipeObject spawnPipe) {
        this.spawnPipe = spawnPipe;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public List<NothingBlockObject> getNothingBlockObjects() {
        return nothingBlockObjects;
    }

    public void setNothingBlockObjects(List<NothingBlockObject> nothingBlockObjects) {
        this.nothingBlockObjects = nothingBlockObjects;
    }

    public List<TrunkPipe> getTrunkPipes() {
        return trunkPipes;
    }

    public void setTrunkPipes(List<TrunkPipe> trunkPipes) {
        this.trunkPipes = trunkPipes;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(Checkpoint checkpoint) {
        this.checkpoint = checkpoint;
    }
}
