/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map.Support;

import Engine.Engine.ESC;
import Engine.FileIO.FileUtils;
import Engine.Items.Support.Item;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author DribbleWare
 */
public class MineMaker {

    //<editor-fold defaultstate="collapsed" desc="declarations">
    static ESC eng;
    //map size
    static int width = 35; //inital number of cells
    static int height = 35; // inital number of cells
    static int cellSize = 32;
    static boolean[][] cellMap;
    //variables to control generation
    static float chanceToStartAlive = 0.32f; //% of map thatll be not a wall
    static int birthLimit = 3;
    static int deathLimit = 2;
    static int steps = 3;
    static boolean flag = false;
    //door
    static int doorx = -cellSize * 3, doory = -cellSize * 2;
    static boolean door = false;
    public static Rectangle exit;
    static BufferedImage DoorImg;
    static boolean noDoor = true;
    //createing the mine image
    static BufferedImage Cave = new BufferedImage(width * cellSize, height * cellSize, BufferedImage.TYPE_INT_RGB);
    public static List<BufferedImage> parts = new ArrayList<>();
    static JFrame frame;
    static Canvas canvas;
    static Graphics g;
    static BufferStrategy bs;
    static Path MASTERPATH = Paths.get(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Game" + File.separator + "Save");
    //collision
    public static Area box = new Area();
    public static List<Area> collis = new ArrayList<>();
    static int caveNum = 0;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="makeMap">
    public static void makeMap() {
        g = Cave.createGraphics();
        g.setColor(new Color(39, 33, 41));
        g.fillRect(0, 0, width * cellSize, height * cellSize);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (cellMap[j][i] == false) {
                    //not a wall
                    g.drawImage(parts.get(6), j * cellSize, i * cellSize, 32, 32, null);
                } else { //wall {
                    g.drawImage(parts.get(picker(j, i)), j * cellSize, i * cellSize, cellSize, cellSize, null);
                }
                //g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                if (flag) {
                    collis.clear();
                    flag = false;
                    door = false;
                    doorx = -cellSize * 3;
                    doory = -cellSize * 2;
                    noDoor = true;
                    i = 0;
                    j = 0;
                }
            }
        }
        if (doorx == (-cellSize * 3) && doory == (-cellSize * 2)) { // in case a cave gets generated without a door
            noDoor = true;
            collis.clear();
            return;
        } else {
            noDoor = false;
        }
        makeBox(collis);
        door(doorx, doory);
        g.dispose();
//        save(Cave, "Cave" + caveNum);
//        caveNum++;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="picker">
    public static int picker(int x, int y) {
        int id = 16;
        String active = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((x - 1) + j < 0 || (y - 1) + i < 0 || (x - 1) + j > width - 1 || (y - 1) + i > height - 1) {
                    active += "1";
                } else {
                    if (cellMap[(x - 1) + j][(y - 1) + i]) {
                        active += "1";
                    } else {
                        active += "0";
                    }
                }
            }
        }
        switch (active) {
            case "111111110":
                id = 11;
                break;
            case "011111111":
                id = 12;
                break;
            case "110111111":
                id = 13;
                break;
            case "111111011":
                id = 10;
                break;
            case "111111111":
                id = 16;
                break;
        }
        if (active.substring(3, 6).equals("111") && active.charAt(1) == '1' && active.charAt(7) == '0') {
            if (picker(x - 1, y) == 4) {
                cellMap[x][y] = false;
                checkMap();
                flag = true;
                return 6;
            }
            id = 1;
            if (!door) {
                if (picker(x - 1, y) == 1 && picker(x - 2, y) == 1 && y > 0) {
                    if (picker(x - 1, y - 1) == 16 && picker(x - 2, y - 1) == 16 && picker(x, y - 1) == 16) {
                        doorx = x - 2;
                        doory = y - 1;
                        door = true;
                    }
                }
            }

        }

        if (active.substring(3, 6).equals("111") && active.charAt(1) == '0' && active.charAt(7) == '1') {
            if (picker(x - 1, y) == 1) {
                cellMap[x][y] = false;
                checkMap();
                flag = true;
                return 6;
            }
            id = 4;
        }
        if (active.substring(3, 6).equals("011") && active.charAt(1) == '1' && active.charAt(7) == '1') {
            if (picker(x, y - 1) == 15) {
                cellMap[x][y] = false;
                checkMap();
                flag = true;
                return 6;
            }
            id = 14;
        }
        if (active.substring(3, 6).equals("110") && active.charAt(1) == '1' && active.charAt(7) == '1') {
            if (picker(x, y - 1) == 14) {
                cellMap[x][y] = false;
                checkMap();
                flag = true;
                return 6;
            }
            id = 15;
        }

        if (active.substring(3, 6).equals("011") && active.substring(1, 3).equals("11") && active.charAt(7) == '0') {
            id = 0;
        }
        if (active.substring(3, 6).equals("110") && active.substring(0, 2).equals("11") && active.charAt(7) == '0') {
            id = 2;
        }
        if (active.substring(3, 6).equals("110") && active.substring(6, 8).equals("11") && active.charAt(1) == '0') {
            id = 5;
        }
        if (active.substring(3, 6).equals("011") && active.substring(7, 9).equals("11") && active.charAt(1) == '0') {
            id = 3;
        }
        if (active.equals("011111110")) {
            id = 8;
        }
        if (active.equals("110111011")) {
            id = 7;
        }
        collisionMapping(id, x, y);
        return id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="collisionMapping">
    public static void collisionMapping(int id, int x, int y) {
        switch (id) {
            case 0:
                collis.add(new Area(new Rectangle((x * cellSize) + (cellSize - 13), y * cellSize, 13, 14)));
                break;
            case 1:
                collis.add(new Area(new Rectangle((x * cellSize), y * cellSize, cellSize, 14)));
                break;
            case 2:
                collis.add(new Area(new Rectangle((x * cellSize), y * cellSize, 11, 14)));
                break;
            case 3:
                collis.add(new Area(new Rectangle((x * cellSize) + (cellSize - 11), (y * cellSize) + (cellSize - 10), 11, 10)));
                collis.add(new Area(new Rectangle((x * cellSize) + (cellSize - 3), (y * cellSize) + (cellSize - 15), 3, 5)));
                break;
            case 4:
                collis.add(new Area(new Rectangle((x * cellSize), (y * cellSize) + (cellSize - 15), cellSize, 15)));
                break;
            case 5:
                collis.add(new Area(new Rectangle((x * cellSize), (y * cellSize) + (cellSize - 7), 11, 7)));
                collis.add(new Area(new Rectangle((x * cellSize), (y * cellSize) + (cellSize - 15), 6, 8)));
                break;
            case 7:
                collis.add(new Area(new Polygon(new int[]{(x * cellSize), (x * cellSize) + 12, (x * cellSize) + cellSize, (x * cellSize) + cellSize, (x * cellSize) + (cellSize - 13), (x * cellSize)},
                        new int[]{(y * cellSize), (y * cellSize), (y * cellSize) + 17, (y * cellSize) + cellSize, (y * cellSize) + cellSize, (y * cellSize) + 14},
                        6)));
                break;
            case 8:
                collis.add(new Area(new Polygon(new int[]{(x * cellSize), (x * cellSize) + (cellSize - 11), (x * cellSize) + (cellSize), (x * cellSize) + (cellSize), (x * cellSize) + 12, (x * cellSize)},
                        new int[]{(y * cellSize) + (cellSize - 15), (y * cellSize), (y * cellSize), (y * cellSize) + 14, (y * cellSize) + (cellSize), (y * cellSize) + (cellSize)},
                        6)));
                break;
            case 10:
                collis.add(new Area(new Polygon(new int[]{(x * cellSize), (x * cellSize), (x * cellSize) + cellSize, (x * cellSize) + cellSize, (x * cellSize) + (cellSize - 13), (x * cellSize) + 10},
                        new int[]{(y * cellSize) + 14, (y * cellSize), (y * cellSize), (y * cellSize) + cellSize, (y * cellSize) + cellSize, (y * cellSize) + 14},
                        6)));
                break;
            case 11:
                collis.add(new Area(new Polygon(new int[]{(x * cellSize) + cellSize, (x * cellSize) + cellSize, (x * cellSize), (x * cellSize), (x * cellSize) + (13), (x * cellSize) + (cellSize - 10)},
                        new int[]{(y * cellSize) + 14, (y * cellSize), (y * cellSize), (y * cellSize) + cellSize, (y * cellSize) + cellSize, (y * cellSize) + 14},
                        6)));
                break;
            case 12:
                collis.add(new Area(new Polygon(new int[]{(x * cellSize), (x * cellSize), (x * cellSize) + cellSize, (x * cellSize) + cellSize, (x * cellSize) + (cellSize - 13), (x * cellSize) + 10},
                        new int[]{(y * cellSize) + (cellSize - 13), (y * cellSize) + cellSize, (y * cellSize) + cellSize, (y * cellSize), (y * cellSize), (y * cellSize) + (cellSize - 13)},
                        6)));
                break;
            case 13:
                collis.add(new Area(new Polygon(new int[]{(x * cellSize) + cellSize, (x * cellSize) + cellSize, (x * cellSize), (x * cellSize), (x * cellSize) + 13, (x * cellSize) + (cellSize - 5)},
                        new int[]{(y * cellSize) + (cellSize - 15), (y * cellSize) + cellSize, (y * cellSize) + cellSize, (y * cellSize), (y * cellSize), (y * cellSize) + (cellSize - 15)},
                        6)));
                break;
            case 14:
                collis.add(new Area(new Rectangle((x * cellSize) + (cellSize - 12), (y * cellSize), 12, cellSize)));
                break;
            case 15:
                collis.add(new Area(new Rectangle((x * cellSize), (y * cellSize), 13, cellSize)));
                break;
            case 16:
                collis.add(new Area(new Rectangle((x * cellSize), (y * cellSize), cellSize, cellSize)));
                break;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="makeBox">
    private static void makeBox(List<Area> in) {
        box.reset();
        for (int i = 0; i < in.size(); i++) {
            box.add(in.get(i));
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="door">
    private static void door(int x, int y) {
        g.drawImage(DoorImg, x * cellSize, y * cellSize, null);
        exit = new Rectangle((x * cellSize) + (cellSize + 4), (y * cellSize) + (cellSize - 7), 24, 39);
        box.subtract(new Area(exit));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getMap">
    public static BufferedImage getMap(ESC engine) {
        eng = engine;
        parts = eng.assetLdr.caveParts;
        DoorImg = eng.assetLdr.caveDoor;
        box.reset();
        collis.clear();
        eng.world.mine.objects.clear();
        eng.world.mine.obbys.clear();
        eng.world.mine.items.clear();
        while (noDoor) {
            cellMap = new boolean[width][height];
            cellMap = initialiseMap(cellMap);
            for (int i = 0; i < steps; i++) {
                cellMap = simulation(cellMap);
            }
            checkMap();
            makeMap();
        }
        populate();
        noDoor = true;
        door = false;
        return Cave;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="populate">
    public static void populate() {
        int density = new Random().nextInt(30) + 15;
        for (int i = 0; i < density; i++) {
            Item placeHolder = new Item(new Random().nextInt(cellSize * width), new Random().nextInt(cellSize * height), 50, 50, 1, 1);
            if (box.intersects(new Rectangle(placeHolder.x, placeHolder.y, placeHolder.width, placeHolder.height)) || box.contains(new Rectangle(placeHolder.x, placeHolder.y, placeHolder.width, placeHolder.height))) {
                i -= 1;
            } else {
                eng.world.mine.items.add(new Item(placeHolder.x * 4, placeHolder.y * 4, placeHolder.width, placeHolder.height, placeHolder.id, placeHolder.qnty));
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkMap">
    public static void checkMap() { //check through some rules to make sure that all of the wall placements are valid
        int holder = 0;
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                boolean left = false, right = false, up = false, down = false;
                if (cellMap[j][i] == true) {
                    if (cellMap[j - 1][i]) {
                        left = true;
                    }
                    if (cellMap[j + 1][i]) {
                        right = true;
                    }
                    if (cellMap[j][i - 1]) {
                        up = true;
                    }
                    if (cellMap[j][i + 1]) {
                        down = true;
                    }
                    boolean[] sides = new boolean[]{left, right, up, down};
                    int counter = 0;
                    for (int k = 0; k < 4; k++) {
                        if (sides[k]) {
                            counter++;
                        }
                    }

                    if ((left ^ right) && (up ^ down)) {
                        if (left) {
                            if (up) {
                                if (!cellMap[j - 1][i - 1]) {
                                    cellMap[j][i] = false;
                                    holder++;
                                    i = 1;
                                    j = 1;
                                }
                            } else if (!cellMap[j - 1][i + 1]) {
                                cellMap[j][i] = false;
                                holder++;
                                i = 1;
                                j = 1;
                            }
                        }
                        if (right) {
                            if (up) {
                                if (!cellMap[j + 1][i - 1]) {
                                    cellMap[j][i] = false;
                                    holder++;
                                    i = 1;
                                    j = 1;
                                }
                            } else if (!cellMap[j + 1][i + 1]) {
                                cellMap[j][i] = false;
                                holder++;
                                i = 1;
                                j = 1;
                            }
                        }
                    }
                    if (counter == 1 || counter == 0) {
                        cellMap[j][i] = false;
                        holder++;
                        i = 1;
                        j = 1;
                    }
                    if (!left && !right && up && down) {
                        cellMap[j][i] = false;
                        holder++;
                        i = 1;
                        j = 1;
                    }
                    if (left && right && !up && !down) {
                        cellMap[j][i] = false;
                        holder++;
                        i = 1;
                        j = 1;
                    }
                }
            }
        }
        System.out.println("checker modified " + holder + " tiles");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="initialize map">
    public static boolean[][] initialiseMap(boolean[][] map) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (new Random().nextFloat() < chanceToStartAlive) {
                    map[x][y] = true;
                }
            }
        }
        return map;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="simulation">
    public static boolean[][] simulation(boolean[][] oldMap) {
        boolean[][] newMap = new boolean[width][height];
        for (int x = 0; x < oldMap.length; x++) {
            for (int y = 0; y < oldMap[0].length; y++) {
                int nbs = countAliveNeighbours(oldMap, x, y);
                if (oldMap[x][y]) {
                    if (nbs < deathLimit) {
                        newMap[x][y] = false;
                    } else {
                        newMap[x][y] = true;
                    }
                } else {
                    if (nbs > birthLimit) {
                        newMap[x][y] = true;
                    } else {
                        newMap[x][y] = false;
                    }
                }
            }
        }
        return newMap;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="count alive neightbors">
    public static int countAliveNeighbours(boolean[][] map, int x, int y) {
        int count = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int neighbour_x = x + i;
                int neighbour_y = y + j;
                if (i == 0 && j == 0) {
                } else if (neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= map.length || neighbour_y >= map[0].length) {
                    count = count + 1;
                } else if (map[neighbour_x][neighbour_y]) {
                    count = count + 1;
                }
            }
        }
        return count;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getBox">
    public static Area getBox() {
        return box;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="save">
    public static void save(BufferedImage img, String name) {
        try {
            ImageIO.write(img, "png", FileUtils.getFilePath(MASTERPATH.toString(), name + ".png").toFile());
        } catch (IOException ex) {
        }
    }
    //</editor-fold>
}
