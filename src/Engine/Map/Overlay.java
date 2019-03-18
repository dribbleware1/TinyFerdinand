/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map;

import Engine.Engine.ESC;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author DribbleWare
 */
public class Overlay {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    ESC engi;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public Overlay(ESC engine) {
        engi = engine;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Render Graphics g">
    public void render(Graphics g) {
        if (!engi.inventory || true) {
            g.drawImage(engi.assetLdr.overlay, Integer.parseInt(engi.xoff), Integer.parseInt(engi.yoff), engi.assetLdr.overlay.getWidth() * engi.size, engi.assetLdr.overlay.getHeight() * engi.size, null);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="priorityRender Graphics g">
    public void priorityRender(Graphics g) {
        if (engi.inventory) {
            g.setColor(Color.green);
            for (int i = 0; i < engi.world.items.size(); i++) {
                g.drawRect(engi.world.items.get(i).x + Integer.parseInt(engi.xoff), engi.world.items.get(i).y + Integer.parseInt(engi.yoff), engi.world.items.get(i).width, engi.world.items.get(i).height);
            }
            g.setColor(Color.red);
            for (int i = 0; i < engi.world.Objects.size(); i++) {
                g.drawRect(engi.world.Objects.get(i).x + engi.getXOff(), engi.world.Objects.get(i).y + engi.getYOff(), engi.world.Objects.get(i).width, engi.world.Objects.get(i).height);
            }

        }
    }
    //</editor-fold>

}
