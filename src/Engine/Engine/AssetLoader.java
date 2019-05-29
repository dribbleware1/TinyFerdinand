/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Engine;

import Engine.FileIO.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DribbleWare
 */
public class AssetLoader {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    private final int width = 64, height = 64;//sprite sheet setup

    //<editor-fold defaultstate="collapsed" desc="World">
    public BufferedImage Hub1, overlay;
    public List<BufferedImage> fenceParts = new ArrayList<>();

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Main Menu">
    public BufferedImage menuBack, title, title2, cont, cont2, opt, opt2, exit, exit2;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Options menu">
    public BufferedImage deb, on, on2, off, off2, back, back2, framerate, sixty, uncapped, reset, framerate2, reset2;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Pause Menu">
    public BufferedImage pause, exitToTitle, exitToTitle2;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Trees">
    public BufferedImage sapling;
    public BufferedImage smallT, bigT;
    public List<BufferedImage> bigTree = new ArrayList<>();
    public List<BufferedImage> smallTree = new ArrayList<>();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Inventory">
    public BufferedImage craft, craft2, home, home2;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Player">
    public BufferedImage walkSheet, dropShadow;
    //arraylists for the walking animations
    public List<BufferedImage> walkUp = new ArrayList<>();
    public List<BufferedImage> walkLeft = new ArrayList<>();
    public List<BufferedImage> walkDown = new ArrayList<>();
    public List<BufferedImage> walkRight = new ArrayList<>();
    public List<BufferedImage> campFire = new ArrayList<>();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Items">
    public BufferedImage decoSheet, logs, emptyBucket, fullBucket;//decoration sprite sheet
    public BufferedImage terrainSheet, rocks;
    public BufferedImage apple, workBench;
    //newest stuff
    public BufferedImage stick, leaf, plank, paper;
    public BufferedImage axe1, shovel1, pick1;
    public BufferedImage sign, fence;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Fonts">
    public Font testing;
    //</editor-fold>

    public BufferedImage greyBox;

    ESC engine;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public AssetLoader(ESC eng) {
        engine = eng;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Init">
    public void init() throws IOException {// add the things to be loaded in here will be called in beginnig

        //<editor-fold defaultstate="collapsed" desc="World">
        overlay = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Hub", "hub 2.png").toFile()));
        Hub1 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Hub", "hub - 2.png").toFile()));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                fenceParts.add(toCompatibleImage(crop(ImageIO.read(FileUtils.getFilePath("Res", "fenceSheet.png").toFile()), 0 + (32 * j), 0 + (32 * i), 32, 32)));
            }
        }

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Main menu">
        title = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "tiny_ferdinand - l.png").toFile()));
        title2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "TITLE - L.png").toFile()));
        opt = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "options.png").toFile()));
        opt2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "options - l.png").toFile()));
        cont = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "continue.png").toFile()));
        cont2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "continue - l.png").toFile()));
        menuBack = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "menu background.png").toFile()));
        exit = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "exit.png").toFile()));
        exit2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "exit - l.png").toFile()));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Options mnenu">
        deb = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "debug.png").toFile()));
        on = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "on.png").toFile()));
        on2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "on - l.png").toFile()));
        off = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "off.png").toFile()));
        off2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "off - l.png").toFile()));
        back = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "back.png").toFile()));
        back2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "back - l.png").toFile()));
        framerate = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "framerate.png").toFile()));
        sixty = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "60.png").toFile()));
        uncapped = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "uncapped.png").toFile()));
        reset = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "reset.png").toFile()));
        reset2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu", "reset - l.png").toFile()));
        framerate2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Menu","framerate - l.png").toFile()));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Pause menu">
        pause = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Pause", "pause.png").toFile()));
        exitToTitle = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Pause", "exittotitle.png").toFile()));
        exitToTitle2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "UX", "Pause", "exittotitle - l.png").toFile()));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Trees">
        //bigTree = toCompatibleImage(crop(ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Hub", "Tree Atlas.png").toFile()), 64, 0, 32 * 4, 32 * 6));
        final BufferedImage treeImage = ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Hub", "Trees.png").toFile());
        smallTree.add(toCompatibleImage(crop(treeImage, 0, 0, 32, 64)));
        smallTree.add(toCompatibleImage(crop(treeImage, 32, 0, 64, 64)));
        smallTree.add(toCompatibleImage(crop(treeImage, 96, 0, 64, 64)));

        bigTree.add(toCompatibleImage(crop(treeImage, 0, 64, 32, 64)));
        bigTree.add(toCompatibleImage(crop(treeImage, 32, 64, 64, 96)));
        bigTree.add(toCompatibleImage(crop(treeImage, 96, 64, 128, 192)));

        sapling = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Items", "sapling.png").toFile()));

        final BufferedImage atlas2Image = ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Hub", "Tree Atlas2.png").toFile());
        smallT = toCompatibleImage(crop(atlas2Image, 0, 0, 32 * 2, 32 * 2));
        bigT = toCompatibleImage(crop(atlas2Image, 64, 0, 32 * 4, 32 * 6));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Inventory">
        craft = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "craft.png").toFile()));
        craft2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "carftGrey.png").toFile()));
        home = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "home.png").toFile()));
        home2 = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "homeGrey.png").toFile()));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Player and movement">
        walkSheet = ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Player", "walkSheet.png").toFile());
        dropShadow = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Player", "Drop Shadow.png").toFile()));
        for (int i = 0; i < 9; i++) {
            walkUp.add(toCompatibleImage(crop(walkSheet, 0 + (width * i), 0, width, height)));
        }
        for (int i = 0; i < 9; i++) {
            walkLeft.add(toCompatibleImage(crop(walkSheet, 0 + (width * i), 64, width, height)));
        }
        for (int i = 0; i < 9; i++) {
            walkDown.add(toCompatibleImage(crop(walkSheet, 0 + (width * i), 128, width, height)));
        }
        for (int i = 0; i < 9; i++) {
            walkRight.add(toCompatibleImage(crop(walkSheet, 0 + (width * i), 192, width, height)));
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Items">
        decoSheet = ImageIO.read(FileUtils.getFilePath("Res", "decoSheet.png").toFile());
        logs = toCompatibleImage(crop(decoSheet, (13 * 32), (22 * 32), 32, 32));
        emptyBucket = toCompatibleImage(crop(decoSheet, (2 * 32), (15 * 32), 32, 32));
        fullBucket = toCompatibleImage(crop(decoSheet, (3 * 32), (15 * 32), 32, 32));
        apple = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Items", "apple.png").toFile()));
        workBench = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Items", "Workbench.png").toFile()));
        terrainSheet = ImageIO.read(FileUtils.getFilePath("Res", "terrainSheet.png").toFile());
        rocks = toCompatibleImage(crop(terrainSheet, (160), (1728), 32, 32));

        //new additions being worked on
        final BufferedImage itemsImage = ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Items", "items.png").toFile());
        stick = toCompatibleImage(crop(itemsImage, 0, 16, 16, 16));
        plank = toCompatibleImage(crop(itemsImage, 0, 32, 16, 16));
        leaf = toCompatibleImage(crop(itemsImage, 16, 96, 16, 16));
        paper = toCompatibleImage(crop(itemsImage, 16, 32, 16, 16));

        final BufferedImage rpgItemsImage = ImageIO.read(FileUtils.getFilePath("Res", "Gameplay", "Items", "rpgItems.png").toFile());
        shovel1 = toCompatibleImage(crop(rpgItemsImage, 64, 48, 16, 16));
        axe1 = toCompatibleImage(crop(rpgItemsImage, 64, 80, 16, 16));
        pick1 = toCompatibleImage(crop(rpgItemsImage, 80, 48, 16, 16));

        sign = toCompatibleImage(crop(decoSheet, 224, 128, 32, 32));
        fence = toCompatibleImage(crop(ImageIO.read(FileUtils.getFilePath("Res", "fenceSheet.png").toFile()), 32, 0, 32, 32));

        for (int i = 0; i < 5; i++) {
            campFire.add(toCompatibleImage(crop(decoSheet, (8 + i) * 32, 47 * 32, 32, 64)));
        }
        campFire.add(toCompatibleImage(crop(decoSheet, 320, 49 * 32, 32, 32)));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Fonts (to come)">
        final InputStream is = new FileInputStream(FileUtils.getFilePath("Res", "UX", "Fonts", "Useable", "Unbroken.ttf").toFile());
        try {
            testing = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException ex) {
            Logger.getLogger(AssetLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

        greyBox = toCompatibleImage(ImageIO.read(FileUtils.getFilePath("Res", "grey box.png").toFile()));
    }
//</editor-fold>

    //to crop sprite sheets for animations
    //<editor-fold defaultstate="collapsed" desc="Crop BufferedImage shee int x int y int width int height">
    public BufferedImage crop(BufferedImage sheet, int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }
    //</editor-fold>

    //to improve rendering speeds and perfromance
    //<editor-fold defaultstate="collapsed" desc="toCompatibleImage BufferedImage image">
    private BufferedImage toCompatibleImage(BufferedImage image) {
        // obtain the current system graphical settings
        GraphicsConfiguration gfxConfig = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();

        /*
     * if image is already compatible and optimized for current system 
     * settings, simply return it
         */
        if (image.getColorModel().equals(gfxConfig.getColorModel())) {
            return image;
        }

        // image is not optimized, so create a new image that is
        BufferedImage newImage = gfxConfig.createCompatibleImage(
                image.getWidth(), image.getHeight(), image.getTransparency());

        // get the graphics context of the new image to draw the old image on
        Graphics2D g2d = newImage.createGraphics();

        // actually draw the image and dispose of context no longer needed
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // return the new optimized image
        return newImage;
    }
    //</editor-fold>
}
