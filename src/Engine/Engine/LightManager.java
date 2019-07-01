/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Engine;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

/**
 *
 * @author DribbleWare
 */
public class LightManager implements Runnable {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    ESC eng;
    boolean running = true;
    int lastLight = 0;
    int width = 1120, height = 1120;
    boolean flagged = false;

    GraphicsConfiguration gfxConfig = GraphicsEnvironment.
            getLocalGraphicsEnvironment().getDefaultScreenDevice().
            getDefaultConfiguration();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public LightManager(ESC engine) {
        System.out.println("light manager started");
        eng = engine;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    public void update() {
        if (lastLight != eng.world.active.lights.size()) {
            makeLight();
            lastLight = eng.world.active.lights.size();
            if (lastLight == 0) {
                flagged = false;
            }
        }
        if (eng.world.active.lights.isEmpty() && !eng.Loc.equals("menu") && !flagged) {
            if (eng.world.active.dark || eng.world.active.permDark) {
                makeLight();
                flagged = true;
            }
        }
        if (eng.doLight) {
            makeLight();
            eng.doLight = false;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="make light">
    public void makeLight() {
        BufferedImage FILTER = gfxConfig.createCompatibleImage(width, height, 3);
        Graphics2D LG = FILTER.createGraphics();
        LG.clearRect(0, 0, FILTER.getWidth(), FILTER.getHeight());
        LG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1f));
        LG.setColor(eng.world.active.filter);
        LG.fillRect(0, 0, FILTER.getWidth(), FILTER.getHeight());
        for (int i = 0; i < eng.world.active.lights.size(); i++) {
            eng.world.active.lights.get(i).render(LG);
        }
        LG.dispose();
        FILTER.flush();
        eng.FILTER = FILTER;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="run">
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0; // Number of ticks for update and render cycle(in one second)
        double ns = 1000000000 / amountOfTicks; //Ticks per second
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;

        while (running && true) { //Running loop;
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                //If fps locked to 60
                update();
                updates++;
                delta--;
            }
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("light manager ups: " + updates);
                updates = 0;
            }
        }
    }
    //</editor-fold>

}
