package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {
    public Tile[] tiles;
    public int[][] tilesMap;
    public int worldShift = 0;
    GamePanel gp;
    BufferedImage background;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[5];
        tilesMap = new int[2][gp.screenCol * 8 + 8];
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/background/play state.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getImages();
        loadGroundMap(gp.player.level + ".txt");
    }

    public void getImages() {
        tiles[0] = new Tile();
        tiles[1] = new Tile();
        tiles[2] = new Tile();
        try {
            // BLOCK
            tiles[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/blocks/block.png")));
            // BREAK
            tiles[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/blocks/break.png")));
            // STAIR
            tiles[2].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/blocks/stair.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGroundMap(String fileName) {
        InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/map/ground/" + fileName));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            for (int i = 0; i < 2; i++) {
                String line = br.readLine();
                String[] array = line.split(" ");
                for (int j = 0; j < 16 * 8 + 8; j++) {
                    tilesMap[i][j] = Integer.parseInt(array[j]) % 10;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics2D g2D) {
        g2D.drawImage(background, 0, 0, gp.screenWidth, gp.screenHeight, null);
        for (int i = 0; i < 16 * 8 + 8; i++) {
            for (int j = 0; j < 2; j++) {
                int index = tilesMap[j][i];
                if (index != -1) {
                    g2D.drawImage(tiles[tilesMap[j][i]].image, i * gp.tileSize - worldShift, gp.screenHeight - (j + 1) * gp.tileSize, gp.tileSize, gp.tileSize, null);
                }
                if (i % 16 == 0) {
                    g2D.setStroke(new BasicStroke(8));
                    g2D.drawLine(i * gp.tileSize - worldShift, 416, i * gp.tileSize - worldShift, gp.screenHeight - 2 * gp.tileSize);
                    g2D.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 24F));
                    if (i / 16 != 8) {
                        g2D.drawString(String.valueOf((i / 16) + 1), i * gp.tileSize - worldShift - 3, 400);
                    }
                }
            }
        }
    }
}