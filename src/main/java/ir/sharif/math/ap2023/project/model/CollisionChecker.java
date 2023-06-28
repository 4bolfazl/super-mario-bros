package ir.sharif.math.ap2023.project.model;

import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameLoader;
import ir.sharif.math.ap2023.project.controller.GameState;
import ir.sharif.math.ap2023.project.controller.sound.BackgroundMusicType;
import ir.sharif.math.ap2023.project.controller.sound.SoundEffectType;
import ir.sharif.math.ap2023.project.controller.sound.SoundManager;
import ir.sharif.math.ap2023.project.model.block.*;
import ir.sharif.math.ap2023.project.model.checkpoint.Checkpoint;
import ir.sharif.math.ap2023.project.model.enemy.*;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.model.item.Bomb;
import ir.sharif.math.ap2023.project.model.item.Item;
import ir.sharif.math.ap2023.project.model.item.Star;
import ir.sharif.math.ap2023.project.model.pipe.*;
import ir.sharif.math.ap2023.project.model.player.Fireball;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.model.player.PlayerDirection;
import ir.sharif.math.ap2023.project.model.player.Sword;
import ir.sharif.math.ap2023.project.view.UIManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class CollisionChecker {
    private static CollisionChecker instance;

    private CollisionChecker() {
    }

    public static CollisionChecker getInstance() {
        if (instance == null) {
            instance = new CollisionChecker();
        }
        return instance;
    }

    public void checkCollisions() {
        if (GameEngine.getInstance().getPlayer().getDirection() != PlayerDirection.DEAD) {
            checkBottomCollisions();
            checkTopCollisions();
            checkHorizontalCollisions();
            checkPlayerItemsCollision();
        }
        checkItemsBottomCollisions();
        checkItemsHorizontalCollision();
        checkEnemiesBottomCollisions();
        checkEnemiesHorizontalCollisions();
        checkFireballCollisions();
        if (GameEngine.getInstance().getPlayer().hasSword) {
            checkSwordCollisions();
        }
        if (GameEngine.getInstance().boss != null) {
            checkBossAttackCollisions();
            if (GameEngine.getInstance().boss.bomb != null) {
                checkBombCollision();
            }
        }
        checkFlagCollisions();
        checkCheckpointCollisions();
    }

    private void checkCheckpointCollisions() {
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);
        Checkpoint checkpoint = sectionObject.getCheckpoint();
        if (checkpoint != null && !checkpoint.isTriggered()) {
            if (player.getFullBounds().intersects(checkpoint.getBounds())) {
                GameEngine.getInstance().setGameState(GameState.CHECKPOINT);
                if (player.getSpeedX() >= 0) {
                    player.setDirection(PlayerDirection.IDLE_RIGHT);
                } else {
                    player.setDirection(PlayerDirection.IDLE_LEFT);
                }
                player.setJumping(false);
                player.setSpeedX(0);
                player.setSpeedY(0);
            }
        }
    }

    private void checkFlagCollisions() {
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);
        Flag flag = sectionObject.getFlag();
        if (flag != null) {
            Rectangle bounds = flag.getFlagRod().getBounds();
            if (bounds.intersects(player.getRightBounds())) {
                player.setSpeedX(0);
                flag.setTriggered(true);
                SoundManager.getInstance().pauseMusic();
                SoundManager.getInstance().playBackgroundMusic(BackgroundMusicType.FLAG);
                GameEngine.getInstance().setGameState(GameState.SCENE);
            }
        }
    }

    private void checkBossAttackCollisions() {
        Bowser boss = GameEngine.getInstance().boss;
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);
        for (BowserFireball fireball : boss.getFireballs()) {
            Rectangle bounds = fireball.getLeftBounds();
            if (fireball.speedX > 0) {
                if (fireball.getBoundsForPlayer().intersects(player.getLeftBounds())) {
                    player.decreaseHeartHit(false);
                    fireball.setDestroyed(true);
                }
                for (BlockObject block : sectionObject.getBlocks()) {
                    if (block.getLeftBounds().intersects(bounds)) {
                        fireball.setDestroyed(true);
                    }
                }
            } else {
                if (fireball.getBoundsForPlayer().intersects(player.getRightBounds())) {
                    player.decreaseHeartHit(false);
                    fireball.setDestroyed(true);
                }
                for (BlockObject block : sectionObject.getBlocks()) {
                    if (block.getLeftBounds().intersects(bounds)) {
                        fireball.setDestroyed(true);
                    }
                }
            }
        }
    }

    private void checkSwordCollisions() {
        Sword sword = GameEngine.getInstance().getPlayer().sword;
        if (sword.released) {
            Player player = GameEngine.getInstance().getPlayer();
            SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);
            Rectangle bounds = sword.getBounds(player);
            if (sword.speedX > 0) {
                for (BlockObject block : sectionObject.getBlocks()) {
                    if (block.getLeftBounds().intersects(bounds)) {
                        sword.setDestroyed(true);
                    }
                }
                for (PipeObject pipe : sectionObject.getPipes()) {
                    if (pipe.getLeftBounds().intersects(bounds)) {
                        sword.setDestroyed(true);
                    }
                }
                for (EnemyObject enemy : sectionObject.getEnemies()) {
                    if (enemy.getLeftBounds().intersects(bounds)) {
                        if (enemy instanceof Koopa)
                            ((Koopa) enemy).setFreeze(true);
                        if (enemy instanceof Bowser)
                            ((Bowser) enemy).kill(1);
                        else
                            enemy.kill();
                        player.sword.setDestroyed(true);
                    }
                }
            } else {
                for (BlockObject block : sectionObject.getBlocks()) {
                    if (block.getRightBounds().intersects(bounds)) {
                        sword.setDestroyed(true);
                    }
                }
                for (PipeObject pipe : sectionObject.getPipes()) {
                    if (pipe.getRightBounds().intersects(bounds)) {
                        sword.setDestroyed(true);
                    }
                }
                for (EnemyObject enemy : sectionObject.getEnemies()) {
                    if (enemy.getRightBounds().intersects(bounds)) {
                        if (enemy instanceof Koopa)
                            ((Koopa) enemy).setFreeze(true);
                        if (enemy instanceof Bowser)
                            ((Bowser) enemy).kill(1);
                        else
                            enemy.kill();
                        player.sword.setDestroyed(true);
                    }
                }
            }
        }
    }

    private void checkBombCollision() {
        Player player = GameEngine.getInstance().getPlayer();
        Bomb bomb = GameEngine.getInstance().boss.bomb;
        double distance = Math.abs(bomb.getX() - player.getX()) / UIManager.getInstance().getTileSize();
        if (bomb.getY() >= 9 * UIManager.getInstance().getTileSize()) {
            if (distance <= 2) {
                player.decreaseHeartHit(false);
            }
            GameEngine.getInstance().boss.bomb = null;
            GameEngine.getInstance().boss.nukeAttacking = false;
            GameEngine.getInstance().boss.nukeCoolDownStart = true;
        }
    }

    private void checkPlayerItemsCollision() {
        Player player = GameEngine.getInstance().getPlayer();
        List<Item> items = GameEngine.getInstance().getItems();

        List<Item> toBeRemoved = new ArrayList<>();

        for (Item item : items) {
            Rectangle itemsSolidArea = new Rectangle((int) item.getX(), (int) item.getY(), UIManager.getInstance().getTileSize(), UIManager.getInstance().getTileSize());
            player.updateSolidArea();
            if (player.getSolidArea().intersects(itemsSolidArea)) {
                item.acquired(player);
                toBeRemoved.add(item);
            }
        }

        for (Item item : toBeRemoved) {
            items.remove(item);
        }
    }

    private void checkHorizontalCollisions() {
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);

        for (EnemyObject enemy : sectionObject.getEnemies()) {
            if (enemy.isDead())
                continue;
            if (enemy.getRightBounds().intersects(player.getLeftBounds()) || enemy.getLeftBounds().intersects(player.getRightBounds())) {
                if (player.isInvincible()) {
                    if (enemy instanceof Koopa)
                        ((Koopa) enemy).setFreeze(true);
                    enemy.kill();
                } else if (!player.isEnemyInvincible()) {
                    player.decreaseHeartHit(false);
                }
            }
        }

        if (player.getSpeedX() != 0) {
            if (player.getSpeedX() >= 0) {
                if (player.getX() + player.getSpeedX() + UIManager.getInstance().getTileSize() / 2d >= UIManager.getInstance().getScreenWidth()) {
                    player.nextSection();
                    player.setX(UIManager.getInstance().getTileSize() / 2d + 2);
                }
                Rectangle bounds = player.getRightBounds();
                for (BlockObject blockObject : sectionObject.getBlocks()) {
                    if (bounds.intersects(blockObject.getLeftBounds())) {
                        player.setSpeedX(0);
                        player.setX((blockObject.getX() - 1) * UIManager.getInstance().getTileSize());
                    }
                }
                for (PipeObject pipe : sectionObject.getPipes()) {
                    if (bounds.intersects(pipe.getLeftBounds())) {
                        if (pipe instanceof ExitPipe) {
                            player.exitSecretPipe();
                        } else {
                            player.setSpeedX(0);
                            player.setX((pipe.getX() - 1) * UIManager.getInstance().getTileSize());
                        }
                    }
                }
                for (TrunkPipe trunkPipe : sectionObject.trunkPipes) {
                    if (bounds.intersects(trunkPipe.getLeftBounds())) {
                        player.setSpeedX(0);
                        player.setX((trunkPipe.getX() - 1) * UIManager.getInstance().getTileSize());
                    }
                }
            } else {
                if (player.getX() - player.getSpeedX() <= 0) {
                    player.setSpeedX(0);
                    player.setX(0);
                }
                Rectangle bounds = player.getLeftBounds();
                for (BlockObject blockObject : sectionObject.getBlocks()) {
                    if (bounds.intersects(blockObject.getRightBounds())) {
                        player.setSpeedX(0);
                        player.setX((blockObject.getX() + 1) * UIManager.getInstance().getTileSize());
                    }
                }
                for (PipeObject pipe : sectionObject.getPipes()) {
                    if (bounds.intersects(pipe.getRightBounds())) {
                        player.setSpeedX(0);
                        player.setX((pipe.getX() + 2) * UIManager.getInstance().getTileSize());
                    }
                }
                for (TrunkPipe trunkPipe : sectionObject.trunkPipes) {
                    if (bounds.intersects(trunkPipe.getRightBounds())) {
                        player.setSpeedX(0);
                        player.setX((trunkPipe.getX() + 2) * UIManager.getInstance().getTileSize());
                    }
                }
            }
        }
    }

    private void checkFireballCollisions() {
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);
        OuterLoop:
        for (Fireball fireball : player.getFireballs()) {
            if (fireball.isToRight()) {
                Rectangle bounds = fireball.getRightBounds();
                for (EnemyObject enemy : sectionObject.getEnemies()) {
                    if (enemy.getLeftBounds().intersects(bounds)) {
                        if (enemy instanceof Koopa)
                            ((Koopa) enemy).setFreeze(true);
                        if (enemy instanceof Bowser)
                            ((Bowser) enemy).kill(1);
                        else
                            enemy.kill();
                        fireball.setDestroyed(true);
                        continue OuterLoop;
                    }
                }
                for (BlockObject block : sectionObject.getBlocks()) {
                    if (block.getLeftBounds().intersects(bounds)) {
                        fireball.setDestroyed(true);
                        continue OuterLoop;
                    }
                }
            } else {
                Rectangle bounds = fireball.getLeftBounds();
                for (EnemyObject enemy : sectionObject.getEnemies()) {
                    if (enemy.getRightBounds().intersects(bounds)) {
                        if (enemy instanceof Koopa)
                            ((Koopa) enemy).setFreeze(true);
                        if (enemy instanceof Bowser)
                            ((Bowser) enemy).kill(1);
                        else
                            enemy.kill();
                        fireball.setDestroyed(true);
                        continue OuterLoop;
                    }
                }
                for (BlockObject block : sectionObject.getBlocks()) {
                    if (block.getRightBounds().intersects(bounds)) {
                        fireball.setDestroyed(true);
                        continue OuterLoop;
                    }
                }
            }
        }
    }

    private void checkEnemiesHorizontalCollisions() {
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);
        for (EnemyObject enemy : sectionObject.getEnemies()) {
            if (enemy.getSpeedX() != 0) {
                if (enemy.getSpeedX() > 0) {
                    Rectangle bounds = enemy.getRightBounds();
                    List<BlockObject> toBeRemoved = new ArrayList<>();
                    if (enemy instanceof Bowser) {
                        if (enemy.getSolidArea().x + 2 >= 18 * UIManager.getInstance().getTileSize()) {
                            enemy.setToRight(false);
                            enemy.setSpeedX(-enemy.defaultSpeed);
                        }
                    }
                    for (BlockObject blockObject : sectionObject.getBlocks()) {
                        if (bounds.intersects(blockObject.getLeftBounds())) {
                            if (enemy instanceof Bowser && blockObject.getX() != 0 && blockObject.getX() != 25) {
                                toBeRemoved.add(blockObject);
                            } else {
                                enemy.setToRight(false);
                                enemy.setSpeedX(-enemy.defaultSpeed);
                                enemy.getSolidArea().x = ((blockObject.getX() - 1) * UIManager.getInstance().getTileSize()) + ((enemy instanceof Bowser) ? -144 : 0);
                            }
                        }
                    }
                    for (BlockObject blockObject : toBeRemoved) {
                        sectionObject.getBlocks().remove(blockObject);
                    }
                    OuterLoop:
                    for (NothingBlockObject nothingBlockObject : sectionObject.nothingBlockObjects) {
                        if (bounds.intersects(nothingBlockObject.getLeftBounds())) {
                            for (NothingBlockObject blockObject : sectionObject.nothingBlockObjects) {
                                if (blockObject.getY() == nothingBlockObject.getY() && blockObject.getX() + 2 == nothingBlockObject.getX()) {
                                    enemy.setSpeedX(0);
                                    break OuterLoop;
                                }
                            }
                            enemy.setToRight(false);
                            enemy.setSpeedX(-enemy.defaultSpeed);
                            enemy.getSolidArea().x = ((nothingBlockObject.getX() - 1) * UIManager.getInstance().getTileSize()) + ((enemy instanceof Bowser) ? -144 : 0);
                        }
                    }
                    for (PipeObject pipe : sectionObject.getPipes()) {
                        if (bounds.intersects(pipe.getLeftBounds())) {
                            enemy.setToRight(false);
                            enemy.setSpeedX(-enemy.defaultSpeed);
                            enemy.getSolidArea().x = ((pipe.getX() - 1) * UIManager.getInstance().getTileSize() - 5) + ((enemy instanceof Bowser) ? -144 : 0);
                        }
                    }
                    for (TrunkPipe trunkPipe : sectionObject.trunkPipes) {
                        if (bounds.intersects(trunkPipe.getLeftBounds())) {
                            enemy.setToRight(false);
                            enemy.setSpeedX(-enemy.defaultSpeed);
                            enemy.getSolidArea().x = ((trunkPipe.getX() - 1) * UIManager.getInstance().getTileSize() - 5) + ((enemy instanceof Bowser) ? -144 : 0);
                        }
                    }
                } else {
                    Rectangle bounds = enemy.getLeftBounds();
                    List<BlockObject> toBeRemoved = new ArrayList<>();
                    if (enemy instanceof Bowser) {
                        if (enemy.getSolidArea().x - 2 <= 3 * UIManager.getInstance().getTileSize()) {
                            enemy.setToRight(true);
                            enemy.setSpeedX(enemy.defaultSpeed);
                        }
                    }
                    for (BlockObject blockObject : sectionObject.getBlocks()) {
                        if (bounds.intersects(blockObject.getRightBounds())) {
                            if (enemy instanceof Bowser && blockObject.getX() != 0 && blockObject.getX() != 25) {
                                toBeRemoved.add(blockObject);
                            } else {
                                enemy.setToRight(true);
                                enemy.setSpeedX(enemy.defaultSpeed);
                                enemy.getSolidArea().x = ((blockObject.getX() + 1) * UIManager.getInstance().getTileSize());
                            }
                        }
                    }
                    for (BlockObject blockObject : toBeRemoved) {
                        sectionObject.getBlocks().remove(blockObject);
                    }
                    for (NothingBlockObject nothingBlockObject : sectionObject.nothingBlockObjects) {
                        if (bounds.intersects(nothingBlockObject.getRightBounds())) {
                            enemy.setToRight(true);
                            enemy.setSpeedX(enemy.defaultSpeed);
                            enemy.getSolidArea().x = ((nothingBlockObject.getX() + 1) * UIManager.getInstance().getTileSize());
                        }
                    }
                    for (PipeObject pipe : sectionObject.getPipes()) {
                        if (bounds.intersects(pipe.getRightBounds())) {
                            enemy.setToRight(true);
                            enemy.setSpeedX(enemy.defaultSpeed);
                            enemy.getSolidArea().x = ((pipe.getX() + 2) * UIManager.getInstance().getTileSize() + 5);
                        }
                    }
                    for (TrunkPipe trunkPipe : sectionObject.trunkPipes) {
                        if (bounds.intersects(trunkPipe.getRightBounds())) {
                            enemy.setToRight(true);
                            enemy.setSpeedX(enemy.defaultSpeed);
                            enemy.getSolidArea().x = ((trunkPipe.getX() + 2) * UIManager.getInstance().getTileSize() + 5);
                        }
                    }
                }
            }
        }
    }

    private void checkItemsHorizontalCollision() {
        Player player = GameEngine.getInstance().getPlayer();
        List<Item> items = GameEngine.getInstance().getItems();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);

        for (Item item : items) {
            if (!item.isShouldBeCollisionChecked())
                continue;
            if (item.getSpeedX() != 0) {
                if (item.getSpeedX() > 0) {
                    Rectangle bounds = item.getRightBounds();
                    for (BlockObject blockObject : sectionObject.getBlocks()) {
                        if (bounds.intersects(blockObject.getLeftBounds())) {
                            item.setToRight(false);
                            item.setSpeedX(-2);
                            item.setX((blockObject.getX() - 1) * UIManager.getInstance().getTileSize());
                        }
                    }
                    for (PipeObject pipe : sectionObject.getPipes()) {
                        if (bounds.intersects(pipe.getLeftBounds())) {
                            item.setToRight(false);
                            item.setSpeedX(-2);
                            item.setX((pipe.getX() - 1) * UIManager.getInstance().getTileSize() - 5);
                        }
                    }
                    for (TrunkPipe pipe : sectionObject.trunkPipes) {
                        if (bounds.intersects(pipe.getLeftBounds())) {
                            item.setToRight(false);
                            item.setSpeedX(-2);
                            item.setX((pipe.getX() - 1) * UIManager.getInstance().getTileSize() - 5);
                        }
                    }
                } else {
                    Rectangle bounds = item.getLeftBounds();
                    for (BlockObject blockObject : sectionObject.getBlocks()) {
                        if (bounds.intersects(blockObject.getRightBounds())) {
                            item.setToRight(true);
                            item.setSpeedX(2);
                            item.setX((blockObject.getX() + 1) * UIManager.getInstance().getTileSize());
                        }
                    }
                    for (PipeObject pipe : sectionObject.getPipes()) {
                        if (bounds.intersects(pipe.getRightBounds())) {
                            item.setToRight(true);
                            item.setSpeedX(2);
                            item.setX((pipe.getX() + 2) * UIManager.getInstance().getTileSize() + 5);
                        }
                    }
                    for (TrunkPipe pipe : sectionObject.trunkPipes) {
                        if (bounds.intersects(pipe.getRightBounds())) {
                            item.setToRight(true);
                            item.setSpeedX(2);
                            item.setX((pipe.getX() + 2) * UIManager.getInstance().getTileSize() + 5);
                        }
                    }
                }
            }
        }
    }

    private synchronized void checkTopCollisions() {
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);

        Rectangle topBounds = player.getTopBounds();

        List<BlockObject> toBeRemoved = new ArrayList<>();

        for (BlockObject blockObject : sectionObject.getBlocks()) {
            if (topBounds.intersects(blockObject.getBottomBounds())) {
                blockObject.gotHit();
                if (player.getCharacterState() > 0 && (blockObject instanceof SimpleBlockObject || blockObject.getType() == BlockType.SIMPLE)) {
                    toBeRemoved.add(blockObject);
                }
                player.setSpeedY(0);
                player.setY((blockObject.getY() + 1) * UIManager.getInstance().getTileSize());
                if (blockObject instanceof QuestionBlockObject && blockObject.getType() == BlockType.QUESTION) {
                    blockObject.setType(BlockType.EMPTY);
                    ((QuestionBlockObject) blockObject).revealItem();
                }
                if (blockObject instanceof CoinBlockObject && blockObject.getType() == BlockType.COIN) {
                    blockObject.setType(BlockType.SIMPLE);
                    ((CoinBlockObject) blockObject).revealItem();
                }
                if (blockObject instanceof CoinsBlockObject && blockObject.getType() == BlockType.COINS) {
                    ((CoinsBlockObject) blockObject).hitTimes++;
                    if (((CoinsBlockObject) blockObject).hitTimes <= 5) {
                        player.addCoins(1);
                        ((CoinsBlockObject) blockObject).revealItem();
                    }
                }
            }
        }

        for (BlockObject blockObject : toBeRemoved) {
            sectionObject.getBlocks().remove(blockObject);
            if (!sectionObject.isBossSection())
                sectionObject.nothingBlockObjects.add(new NothingBlockObject(blockObject.getX(), blockObject.getY() - 1));
        }
    }

    private void checkEnemiesBottomCollisions() {
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);

        for (EnemyObject enemy : sectionObject.getEnemies()) {
            if (!enemy.isJumping())
                enemy.setFalling(true);
            for (BlockObject blockObject : sectionObject.getBlocks()) {
                if (enemy.getBottomBounds().intersects(blockObject.getTopBounds())) {
                    blockObject.setEnemyOnIt(enemy);
                    enemy.getSolidArea().y = (blockObject.getY() - 1) * UIManager.getInstance().getTileSize() + 1 + ((enemy instanceof Bowser) ? -192 : 0) + ((enemy instanceof Koopa) ? -24 : 0);
                    enemy.setFalling(false);
                    enemy.setSpeedY(0);
                    if (enemy instanceof Bowser && ((Bowser) enemy).jumpAttackStarted) {
                        ((Bowser) enemy).jumpAttackStarted = false;
                        ((Bowser) enemy).jumpAttacking = true;
                    }
                } else {
                    if (blockObject.getEnemyOnIt() == null || blockObject.getEnemyOnIt() == enemy)
                        blockObject.setEnemyOnIt(null);
                }
            }

            for (PipeObject pipe : sectionObject.getPipes()) {
                if (enemy.getBottomBounds().intersects(pipe.getTopBounds())) {
                    enemy.getSolidArea().y = ((pipe.getY() - 1) * UIManager.getInstance().getTileSize() + 1);
                    enemy.setFalling(false);
                    enemy.setSpeedY(0);
                }
            }
        }
    }

    private void checkBottomCollisions() {
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);

        if (!player.isJumping()) {
            player.setFalling(true);
        }

        Rectangle bottomBounds = player.getBottomBounds();
        List<BlockObject> toBeRemoved = new ArrayList<>();

        for (BlockObject blockObject : sectionObject.getBlocks()) {
            if (bottomBounds.intersects(blockObject.getTopBounds())) {
                player.setY(blockObject.getY() * UIManager.getInstance().getTileSize() - player.getSolidArea().height + 1);
                player.setFalling(false);
                player.setSpeedY(0);
                switch (player.getDirection()) {
                    case JUMP_RIGHT, JUMP_IDLE_RIGHT -> {
                        player.setDirection(PlayerDirection.IDLE_RIGHT);
                        if (player.getSpeedX() != 0)
                            player.setDirection(PlayerDirection.RIGHT);
                    }
                    case JUMP_LEFT, JUMP_IDLE_LEFT -> {
                        player.setDirection(PlayerDirection.IDLE_LEFT);
                        if (player.getSpeedX() != 0)
                            player.setDirection(PlayerDirection.LEFT);
                    }
                }
                if (blockObject instanceof SlimeBlockObject) {
                    player.setJumping(true);
                    player.setSpeedY(15);
                }

                if (sectionObject.isBossSection() && !(blockObject instanceof GroundBlockObject)) {
                    blockObject.addToDestroyTime();
                    if (blockObject.getToDestroyTimer() >= 60) {
                        toBeRemoved.add(blockObject);
                    }
                }
            } else {
                if (sectionObject.isBossSection()) {
                    blockObject.setToDestroyTimer(0);
                }
            }
        }

        for (BlockObject blockObject : toBeRemoved) {
            sectionObject.getBlocks().remove(blockObject);
        }

        for (PipeObject pipe : sectionObject.getPipes()) {
            if (bottomBounds.intersects(pipe.getTopBounds())) {
                if (pipe instanceof TeleSimplePipe || pipe instanceof TelePiranhaPipe) {
                    player.setPipeUnder(pipe);
                }
                player.setY(pipe.getY() * UIManager.getInstance().getTileSize() - player.getSolidArea().height + 1);
                player.setFalling(false);
                player.setSpeedY(0);
                switch (player.getDirection()) {
                    case JUMP_RIGHT, JUMP_IDLE_RIGHT -> {
                        player.setDirection(PlayerDirection.IDLE_RIGHT);
                        if (player.getSpeedX() != 0)
                            player.setDirection(PlayerDirection.RIGHT);
                    }
                    case JUMP_LEFT, JUMP_IDLE_LEFT -> {
                        player.setDirection(PlayerDirection.IDLE_LEFT);
                        if (player.getSpeedX() != 0)
                            player.setDirection(PlayerDirection.LEFT);
                    }
                }
            } else {
                player.setPipeUnder(null);
            }
        }

        if (!player.isEnemyInvincible()) {
            for (EnemyObject enemy : sectionObject.getEnemies()) {
                if (bottomBounds.intersects(enemy.getTopBounds())) {
                    if (enemy.isDead())
                        continue;
                    if (!player.isInvincible()) {
                        if (enemy instanceof Spiny) {
                            player.decreaseHeartHit(false);
                        } else {
                            player.setJumping(true);
                            player.setFalling(false);
                            player.setSpeedX(0);
                            player.setSpeedY(7);
                        }
                    } else {
                        if (enemy instanceof Koopa)
                            ((Koopa) enemy).setFreeze(true);
                        if (enemy instanceof Spiny)
                            enemy.kill();
                    }
                    if (!(enemy instanceof Spiny)) {
                        if (enemy instanceof Bowser)
                            ((Bowser) enemy).kill(3);
                        else
                            enemy.kill();
                    }
                }
            }
        }
    }

    private void checkItemsBottomCollisions() {
        Player player = GameEngine.getInstance().getPlayer();
        List<Item> items = GameEngine.getInstance().getItems();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);

        for (Item item : items) {
            if (!item.isJumping())
                item.setFalling(true);
            if (!item.isShouldBeCollisionChecked())
                continue;
            for (BlockObject blockObject : sectionObject.getBlocks()) {
                if (item.getBottomBounds().intersects(blockObject.getTopBounds())) {
                    item.setY((blockObject.getY() - 1) * UIManager.getInstance().getTileSize() + 1);
                    item.setFalling(false);
                    item.setSpeedY(0);
                    if (item.getWaitTime() < 100)
                        item.addWaitTime();
                    else {
                        item.setSpeedX(2 * (item.isToRight() ? 1 : -1));
                        if (item instanceof Star) {
                            ((Star) item).addJumpTime();
                            if (((Star) item).getJumpTime() > 60) {
                                item.setJumping(true);
                                item.setSpeedY(6.5);
                                ((Star) item).setJumpTime(0);
                            }
                        }
                    }
                }
            }
        }
    }
}
