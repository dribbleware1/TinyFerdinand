/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

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

    public BufferedImage hotBar, Hub1, overlay; //Worlds
    public BufferedImage menuBack, title, title2, cont, cont2, opt, opt2; //Main menu
    public BufferedImage deb, on, on2, off, off2, back, back2, exit, exit2, framerate, sixty, uncapped, reset, framerate2, reset2; //Options menu
    public BufferedImage pause, exitToTitle, exitToTitle2;//Pause menu
    public BufferedImage walkSheet, dropShadow;//player walk sprite sheet
    private final int width = 64, height = 64;//sprite sheet setup
    public BufferedImage decoSheet, logs;//decoration sprite sheet
    public BufferedImage terrainSheet, rocks;//decoration sprite sheet
    
    public BufferedImage greyBox;
    
    //array for the walking animations
    public List<BufferedImage> walkUp = new ArrayList<>();
    public List<BufferedImage> walkLeft = new ArrayList<>();
    public List<BufferedImage> walkDown = new ArrayList<>();
    public List<BufferedImage> walkRight = new ArrayList<>();
    ESC engine;

    public AssetLoader(ESC eng) {
        engine = eng;
    }

    public void init() throws IOException {// add the things to be loaded in here will be called in beginnig

        //Worlds
        overlay = ImageIO.read(engine.getClass().getResource("/Hub/hub 2.png"));
        hotBar = ImageIO.read(engine.getClass().getResource("/hotbar.png"));
        Hub1 = ImageIO.read(engine.getClass().getResource("/Hub/hub.png"));
        exit = ImageIO.read(engine.getClass().getResource("/Menu/exit.png"));
        exit2 = ImageIO.read(engine.getClass().getResource("/Menu/exit - l.png"));

        //main menu
        title = ImageIO.read(engine.getClass().getResource("/Menu/TITLE.png"));
        title2 = ImageIO.read(engine.getClass().getResource("/Menu/TITLE - L.png"));
        opt = ImageIO.read(engine.getClass().getResource("/Menu/options.png"));
        opt2 = ImageIO.read(engine.getClass().getResource("/Menu/options - l.png"));
        cont = ImageIO.read(engine.getClass().getResource("/Menu/continue.png"));
        cont2 = ImageIO.read(engine.getClass().getResource("/Menu/continue - l.png"));
        menuBack = ImageIO.read(engine.getClass().getResource("/Menu/menu background.png"));

        //Options menu
        deb = ImageIO.read(engine.getClass().getResource("/Menu/debug.png"));
        on = ImageIO.read(engine.getClass().getResource("/Menu/on.png"));
        on2 = ImageIO.read(engine.getClass().getResource("/Menu/on - l.png"));
        off = ImageIO.read(engine.getClass().getResource("/Menu/off.png"));
        off2 = ImageIO.read(engine.getClass().getResource("/Menu/off - l.png"));
        back = ImageIO.read(engine.getClass().getResource("/Menu/back.png"));
        back2 = ImageIO.read(engine.getClass().getResource("/Menu/back - l.png"));
        framerate = ImageIO.read(engine.getClass().getResource("/Menu/framerate.png"));
        sixty = ImageIO.read(engine.getClass().getResource("/Menu/60.png"));
        uncapped = ImageIO.read(engine.getClass().getResource("/Menu/uncapped.png"));
        reset = ImageIO.read(engine.getClass().getResource("/Menu/reset.png"));
        reset2 = ImageIO.read(engine.getClass().getResource("/Menu/reset - l.png"));
        framerate2 = ImageIO.read(engine.getClass().getResource("/Menu/framerate - l.png"));

        //pause menu
        pause = ImageIO.read(engine.getClass().getResource("/Pause/pause.png"));
        exitToTitle = ImageIO.read(engine.getClass().getResource("/Pause/exittotitle.png"));
        exitToTitle2 = ImageIO.read(engine.getClass().getResource("/Pause/exittotitle - l.png"));
        //Player
        walkSheet = ImageIO.read(engine.getClass().getResource("/Player/walkSheet.png"));
        dropShadow = ImageIO.read(engine.getClass().getResource("/Player/Drop Shadow.png"));
        
        
        for (int i = 0; i < 9; i++) {
            walkUp.add(Crop(walkSheet, 0 + (width * i), 0, width, height));
        }
        for (int i = 0; i < 9; i++) {
            walkLeft.add(Crop(walkSheet, 0 + (width * i), 64, width, height));
        }
        for (int i = 0; i < 9; i++) {
            walkDown.add(Crop(walkSheet, 0 + (width * i), 128, width, height));
        }
        for (int i = 0; i < 9; i++) {
            walkRight.add(Crop(walkSheet, 0 + (width * i), 192, width, height));
        }
        //decorations
        decoSheet = ImageIO.read(engine.getClass().getResource("/decoSheet.png"));
        logs = Crop(decoSheet, (416), (704), 32, 32);
        
        terrainSheet = ImageIO.read(engine.getClass().getResource("/terrainSheet.png"));
        rocks = Crop(terrainSheet, (160), (1728), 32, 32);
        
        greyBox = ImageIO.read(engine.getClass().getResource("/grey box.png"));
        
    }

    //to crop sprite sheets for animations
    public BufferedImage Crop(BufferedImage sheet, int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }

}
