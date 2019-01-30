/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author DribbleWare
 */
public class Overlay {

    ESC engi;

    public Overlay(ESC engine) {
        engi = engine;
    }

    public void render(Graphics g) {
        if (!engi.inventory || true) {
            g.drawImage(engi.assetLdr.overlay, Integer.parseInt(engi.xoff), Integer.parseInt(engi.yoff), engi.assetLdr.overlay.getWidth() * engi.size, engi.assetLdr.overlay.getHeight() * engi.size, null);
        }

        if (engi.inventory) {
            g.setColor(Color.green);
            for (int i = 0; i < engi.world.items.size(); i++) {
                g.drawRect(engi.world.items.get(i).x + Integer.parseInt(engi.xoff), engi.world.items.get(i).y + Integer.parseInt(engi.yoff), engi.world.items.get(i).width, engi.world.items.get(i).height);
            }
        }

    }

}
