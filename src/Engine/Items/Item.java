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
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author DribbleWare
 */
public class Item {
//                                                0      1       2           3         4      5          6         7       8         9     10      11         12        13      14

    public final String[] NAMES = new String[]{"Logs", "Rocks", "Bucket", "Water", "Apple", "Sapling", "Stick", "Paper", "Plank", "Leaf", "Axe", "Shovel", "Pickaxe", "Sign", "Fence"}; //leaf -> leaves ?
    public BufferedImage[] art = new BufferedImage[]{};

    public int x, y, width, height, id, qnty;
    public boolean tool = false;
    public Font text = new Font("TimesRoman", Font.PLAIN, 25);

    public Rectangle conBox;

    ESC eng;

    public Item(int xi, int yi, int widthi, int heighti, int idi, int q) {
        x = xi;
        y = yi;
        width = widthi;
        height = heighti;
        id = idi;
        qnty = q;
        artBuild();
        conBox = new Rectangle(x - 10, y - 10, width + 20, height + 20);
    }

    public Item(int idi, int q) {
        id = idi;
        qnty = q;
        artBuild();
    }

    public void toolTips(Graphics g, ESC engine) {
        eng = engine;
        g.setColor(Color.red);
        if (tool && !engine.Loc.equalsIgnoreCase("menu") && !engine.pause && !eng.inventory) {
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
        art = new BufferedImage[]{eng.assetLdr.logs, eng.assetLdr.rocks, eng.assetLdr.emptyBucket, eng.assetLdr.fullBucket, eng.assetLdr.apple, eng.assetLdr.sapling, eng.assetLdr.stick,
            eng.assetLdr.paper, eng.assetLdr.plank, eng.assetLdr.leaf, eng.assetLdr.axe1, eng.assetLdr.shovel1, eng.assetLdr.pick1, eng.assetLdr.sign, eng.assetLdr.fence};
    }
}
