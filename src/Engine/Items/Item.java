/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Items;

import Engine.Engine.ESC;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author DribbleWare
 */
public class Item {

    public final String[] NAMES = new String[] {"Logs", "Rocks", "Bucket", "Water", "Apple"};
    public BufferedImage[] art = new BufferedImage[]{};

    public int x, y, width, height, id, qnty;
    public boolean tool = false;
    public Font text = new Font("TimesRoman", Font.PLAIN, 25);

    ESC eng;

    public Item(int xi, int yi, int widthi, int heighti, int idi, int q) {
        x = xi;
        y = yi;
        width = widthi;
        height = heighti;
        id = idi;
        qnty = q;
        artBuild();
    }

    public Item(int idi, int q) {
        id = idi;
        qnty = q;
    }

    public void toolTips(Graphics g, ESC engine) {
        eng = engine;
        if (tool && !engine.Loc.equalsIgnoreCase("menu") && !engine.pause) {
            g.drawImage(engine.assetLdr.greyBox, loc(engine).x, loc(engine).y - 75, 100, 75, null);
            g.setColor(Color.white);
            g.drawString(NAMES[id], loc(engine).x + 5, loc(engine).y - 50);
            g.drawString(Integer.toString(qnty), loc(engine).x + 5, loc(engine).y - 25);
        }
    }

    public Point loc(ESC engine) {
        return new Point(MouseInfo.getPointerInfo().getLocation().x - engine.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - engine.frame.getY() - Math.abs(engine.frame.getLocationOnScreen().y - engine.canvas.getLocationOnScreen().y));
    }

    public void artBuild() {
        art = new BufferedImage[]{eng.assetLdr.logs, eng.assetLdr.rocks, eng.assetLdr.emptyBucket, eng.assetLdr.fullBucket, eng.assetLdr.apple};
    }
}
