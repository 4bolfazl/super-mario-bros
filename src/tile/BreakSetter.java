package tile;

import main.GamePanel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class BreakSetter {
    public java.util.List<Tile> breaks = new ArrayList<>();
    GamePanel gp;

    public BreakSetter(GamePanel gp) {
        this.gp = gp;

        setBreaks(gp.player.level + ".txt");
    }

    public void setBreaks(String fileName) {
        InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/map/sky/" + fileName));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                String[] array = line.split(" - ");
                Tile breakTile = new Tile();
                breakTile.x = Integer.parseInt(array[0]) * gp.tileSize;
                breakTile.y = Integer.parseInt(array[1]) * gp.tileSize;
                breakTile.image = gp.tileManager.tiles[1].image;
                breaks.add(breakTile);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics2D g2D) {
        for (Tile tile : breaks) {
            g2D.drawImage(tile.image, tile.x - gp.tileManager.worldShift, tile.y, gp.tileSize, gp.tileSize / 2, null);
        }
    }
}
