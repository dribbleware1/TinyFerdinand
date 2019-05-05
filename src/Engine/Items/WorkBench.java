/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Items;

import Engine.Engine.ESC;
import Engine.Map.WorldObjects;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author DribbleWare
 */
public class WorkBench extends WorldObjects {

//<editor-fold defaultstate="collapsed" desc="Declarations">
    private int scale = 2;
    BufferedImage image;
    public Rectangle bench;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Constructor">
    public WorkBench(int xin, int yin, ESC engine) {
        eng = engine;
        image = eng.assetLdr.workBench;
        w = image.getWidth() * scale;
        h = image.getHeight() * scale;
        x = xin;
        y = yin;
        bench = collisBox();
        size = new Rectangle(x, y, w, h);
        collis = collisBox();
    }
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="update">
    public void update() {
        mouseUpdate();
        if (!dropped) {
            drop();
        }
        if (contains(size, true) && recBuilder(size).intersects(eng.mainChar.box)) {
            //actions
        }

    }
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="render Graphics g">
    public void render(Graphics g) {
        if (eng.mainChar.y >= y + eng.getYOff()) {
            g.drawImage(image, x + eng.getXOff(), y + eng.getYOff(), w, h, null);
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="priorityRender Graphics g">
    public void priorityRender(Graphics g) {
        if (eng.mainChar.y < y + eng.getYOff()) {
            g.drawImage(image, x + eng.getXOff(), y + eng.getYOff(), w, h, null);
        }
        if (blocked) {
            g.setColor(new Color(255, 0, 0, 100));
            g.fillRect(x + eng.getXOff(), y + eng.getYOff(), w, h);
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Contains rectangle boxIn boolean addOffSet ">
    public boolean contains(Rectangle boxin, boolean addOffSet) {
        boolean ret = false;
        Rectangle click = new Rectangle(boxin.x, boxin.y, boxin.width, boxin.height);
        if (addOffSet) {
            click = new Rectangle(boxin.x + eng.getXOff(), boxin.y + eng.getYOff(), boxin.width, boxin.height);
        }
        if (click.contains(new Point(MouseInfo.getPointerInfo().getLocation().x - eng.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - eng.frame.getY() - Math.abs(eng.frame.getLocationOnScreen().y - eng.canvas.getLocationOnScreen().y)))) {
            ret = true;
        }
        return ret;
    }
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="collisBox">
    public Rectangle collisBox() {
        return new Rectangle(x, y + 30, w, h - 55);
    }
//</editor-fold>

}
