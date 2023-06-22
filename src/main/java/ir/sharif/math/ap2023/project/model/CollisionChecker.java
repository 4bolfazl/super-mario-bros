package ir.sharif.math.ap2023.project.model;

import ir.sharif.math.ap2023.project.controller.GameEngine;
import ir.sharif.math.ap2023.project.controller.GameLoader;
import ir.sharif.math.ap2023.project.model.block.BlockObject;
import ir.sharif.math.ap2023.project.model.block.BlockType;
import ir.sharif.math.ap2023.project.model.block.QuestionBlockObject;
import ir.sharif.math.ap2023.project.model.enemy.EnemyObject;
import ir.sharif.math.ap2023.project.model.enemy.Koopa;
import ir.sharif.math.ap2023.project.model.game.SectionObject;
import ir.sharif.math.ap2023.project.model.item.Item;
import ir.sharif.math.ap2023.project.model.item.Star;
import ir.sharif.math.ap2023.project.model.pipe.PipeObject;
import ir.sharif.math.ap2023.project.model.player.Player;
import ir.sharif.math.ap2023.project.model.player.PlayerDirection;
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

        if (player.getSpeedX() != 0) {
            if (player.getSpeedX() >= 0) {
                if (player.getX() + player.getSpeedX() + UIManager.getInstance().getTileSize() >= UIManager.getInstance().getScreenWidth()) {
                    player.setSpeedX(0);
                    player.setX(UIManager.getInstance().getScreenWidth() - UIManager.getInstance().getTileSize());
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
                        player.setSpeedX(0);
                        player.setX((pipe.getX() - 1) * UIManager.getInstance().getTileSize());
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
                    for (BlockObject blockObject : sectionObject.getBlocks()) {
                        if (bounds.intersects(blockObject.getLeftBounds())) {
                            enemy.setToRight(false);
                            enemy.setSpeedX(-2);
                            enemy.getSolidArea().x = ((blockObject.getX() - 1) * UIManager.getInstance().getTileSize());
                        }
                    }
                    for (PipeObject pipe : sectionObject.getPipes()) {
                        if (bounds.intersects(pipe.getLeftBounds())) {
                            enemy.setToRight(false);
                            enemy.setSpeedX(-2);
                            enemy.getSolidArea().x = ((pipe.getX() - 1) * UIManager.getInstance().getTileSize() - 5);
                        }
                    }
                } else {
                    Rectangle bounds = enemy.getLeftBounds();
                    for (BlockObject blockObject : sectionObject.getBlocks()) {
                        if (bounds.intersects(blockObject.getRightBounds())) {
                            enemy.setToRight(true);
                            enemy.setSpeedX(2);
                            enemy.getSolidArea().x = ((blockObject.getX() + 1) * UIManager.getInstance().getTileSize());
                        }
                    }
                    for (PipeObject pipe : sectionObject.getPipes()) {
                        if (bounds.intersects(pipe.getRightBounds())) {
                            enemy.setToRight(true);
                            enemy.setSpeedX(2);
                            enemy.getSolidArea().x = ((pipe.getX() + 2) * UIManager.getInstance().getTileSize() + 5);
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
                }
            }
        }
    }

    private void checkTopCollisions() {
        Player player = GameEngine.getInstance().getPlayer();
        SectionObject sectionObject = GameLoader.getInstance("config.json").getGame().getLevels().get(player.getLevel() - 1).getSections().get(player.getSection() - 1);

        Rectangle topBounds = player.getTopBounds();

        for (int i = 0; i < sectionObject.getBlocks().size(); i++) {
            BlockObject blockObject = sectionObject.getBlocks().get(i);
            if (topBounds.intersects(blockObject.getBottomBounds())) {
                player.setSpeedY(0);
                player.setY((blockObject.getY() + 1) * UIManager.getInstance().getTileSize());
                if (blockObject instanceof QuestionBlockObject && blockObject.getType() == BlockType.QUESTION) {
                    sectionObject.getBlocks().get(i).setType(BlockType.EMPTY);
                    ((QuestionBlockObject) blockObject).revealItem();
                }
            }
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
//                    enemy.setY((blockObject.getY() - 1) * UIManager.getInstance().getTileSize() + 1);
                    enemy.getSolidArea().y = (blockObject.getY() - 1) * UIManager.getInstance().getTileSize() + 1 + ((enemy instanceof Koopa) ? -24 : 0);
                    enemy.setFalling(false);
                    enemy.setSpeedY(0);
                }
            }

            for (PipeObject pipe : sectionObject.getPipes()) {
                if (enemy.getBottomBounds().intersects(pipe.getTopBounds())) {
//                    enemy.setY(pipe.getY() * UIManager.getInstance().getTileSize() - player.getSolidArea().height + 1);
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
            }
        }

        for (PipeObject pipe : sectionObject.getPipes()) {
            if (bottomBounds.intersects(pipe.getTopBounds())) {
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
