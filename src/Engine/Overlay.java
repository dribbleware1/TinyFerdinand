/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.Graphics;

/**
 *
 * @author DribbleWare
 */
public class Overlay {

    ESC engi;
    
    public Overlay(ESC engine){
        engi = engine;
    }
    
    public void render(Graphics g) {

        g.drawImage(engi.assetLdr.overlay, Integer.parseInt(engi.xoff), Integer.parseInt(engi.yoff), engi.assetLdr.overlay.getWidth() * engi.size, engi.assetLdr.overlay.getHeight() * engi.size, null);

    }

}
