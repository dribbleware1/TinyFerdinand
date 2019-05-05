/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Engine;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author DribbleWare
 */
public class AssetLoader {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    private final int width = 64, height = 64;//sprite sheet setup

    //<editor-fold defaultstate="collapsed" desc="World">
    public BufferedImage Hub1, overlay;
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
    public BufferedImage smallTree, bigTree;
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
        overlay = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Hub/hub 2.png")));
        Hub1 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Hub/hub - 2.png")));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Main menu">
        title = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/tiny_ferdinand - l.png")));
        title2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/TITLE - L.png")));
        opt = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/options.png")));
        opt2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/options - l.png")));
        cont = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/continue.png")));
        cont2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/continue - l.png")));
        menuBack = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/menu background.png")));
        exit = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/exit.png")));
        exit2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/exit - l.png")));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Options mnenu">
        deb = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/debug.png")));
        on = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/on.png")));
        on2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/on - l.png")));
        off = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/off.png")));
        off2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/off - l.png")));
        back = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/back.png")));
        back2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/back - l.png")));
        framerate = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/framerate.png")));
        sixty = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/60.png")));
        uncapped = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/uncapped.png")));
        reset = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/reset.png")));
        reset2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/reset - l.png")));
        framerate2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Menu/framerate - l.png")));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Pause menu">
        pause = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Pause/pause.png")));
        exitToTitle = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Pause/exittotitle.png")));
        exitToTitle2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Pause/exittotitle - l.png")));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Trees">
        smallTree = toCompatibleImage(crop(ImageIO.read(engine.getClass().getResource("/Hub/Tree Atlas.png")), 0, 0, 32 * 2, 32 * 2));
        bigTree = toCompatibleImage(crop(ImageIO.read(engine.getClass().getResource("/Hub/Tree Atlas.png")), 64, 0, 32 * 4, 32 * 6));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Inventory">
        craft = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/craft.png")));
        craft2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/carftGrey.png")));
        home = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/home.png")));
        home2 = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/homeGrey.png")));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Player and movement">
        walkSheet = ImageIO.read(engine.getClass().getResource("/Player/walkSheet.png"));
        dropShadow = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Player/Drop Shadow.png")));
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
        decoSheet = ImageIO.read(engine.getClass().getResource("/decoSheet.png"));
        logs = toCompatibleImage(crop(decoSheet, (13 * 32), (22 * 32), 32, 32));
        emptyBucket = toCompatibleImage(crop(decoSheet, (2 * 32), (15 * 32), 32, 32));
        fullBucket = toCompatibleImage(crop(decoSheet, (3 * 32), (15 * 32), 32, 32));
        apple = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Items/apple.png")));
        workBench = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/Items/Workbench.png")));
        terrainSheet = ImageIO.read(engine.getClass().getResource("/terrainSheet.png"));
        rocks = toCompatibleImage(crop(terrainSheet, (160), (1728), 32, 32));
        for (int i = 0; i < 5; i++) {
            campFire.add(toCompatibleImage(crop(decoSheet, (8 + i) * 32, 47 * 32, 32, 64)));
        }
        campFire.add(toCompatibleImage(crop(decoSheet, 320, 49 * 32, 32, 32)));
        //</editor-fold>

        greyBox = toCompatibleImage(ImageIO.read(engine.getClass().getResource("/grey box.png")));

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
