/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author DribbleWare
 */
public class backDrop {

    private Dimension size;
    ESC engi;
    BufferedImage hotBar;
    int offset = 8;
    int hotX = 396;
    int pickUpY = 675;
    //boolean hotbar = false;
    player mainChar;

    public backDrop(int w, int h, ESC eng, player py) {
        this.size = new Dimension(w, h);
        this.engi = eng;
        hotBar = engi.assetLdr.hotBar;
        mainChar = py;
    }

    public void render(Graphics g) {
        //Drawing the hotbar
        g.drawImage(hotBar, hotX, engi.sizeh - 120, hotBar.getWidth() * offset, hotBar.getHeight() * offset, null);

        //Drawing the pick up prompt when overtop of an item
        if (mainChar.overIt == true) {
            g.drawString("Press Left Mouse To Pick Up", pickUpY - 120, engi.sizeh - 130);
        }
    }
}
