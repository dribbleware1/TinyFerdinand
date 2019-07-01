/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map;

import Engine.Engine.ESC;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author DribbleWare
 */
public class Overlay {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    ESC eng;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public Overlay(ESC engine) {
        eng = engine;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Render Graphics g">
    public void render(Graphics g) {
        if (!eng.inventory || true) {
            g.drawImage(eng.assetLdr.overlay, Integer.parseInt(eng.xoff), Integer.parseInt(eng.yoff), eng.assetLdr.overlay.getWidth() * eng.size, eng.assetLdr.overlay.getHeight() * eng.size, null);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="priorityRender Graphics g">
    public void priorityRender(Graphics g) {
        if (eng.inventory) {
            g.setColor(Color.green);
            for (int i = 0; i < eng.world.items.size(); i++) {
                g.drawRect(eng.world.items.get(i).x + Integer.parseInt(eng.xoff), eng.world.items.get(i).y + Integer.parseInt(eng.yoff), eng.world.items.get(i).width, eng.world.items.get(i).height);
            }
            g.setColor(Color.red);
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform offset = new AffineTransform();
            offset.translate(eng.getXOff(), eng.getYOff());
            for (int i = 0; i < eng.world.Objects.size(); i++) {
                g2d.draw(eng.world.Objects.get(i).createTransformedArea(offset));
            }
        }
    }
    //</editor-fold>

}
