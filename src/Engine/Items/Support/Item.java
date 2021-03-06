/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Items.Support;

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

    //<editor-fold defaultstate="collapsed" desc="Decalrations">
//                                                0      1       2           3         4      5          6         7       8         9     10      11         12        13      14      15
    public final String[] NAMES = new String[]{"Logs", "Rocks", "Bucket", "Water", "Apple", "Sapling", "Stick", "Paper", "Plank", "Leaf", "Axe", "Shovel", "Pickaxe", "Sign", "Fence", "Torch"}; //leaf -> leaves ?
    public BufferedImage[] art = new BufferedImage[]{};

    public int x, y, width, height, id, qnty;
    public boolean tool = false;
    public Font text = new Font("TimesRoman", Font.PLAIN, 25);
    public int health = -1;
    public Rectangle conBox;

    ESC eng;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Item Constructors">
    /**
     * Item constructor for items in the world not needing a health number
     *
     * @param x integer for the x position
     * @param y integer for the y position
     * @param width integer for the width of the item
     * @param height integer for the height of the item
     * @param id integer for the id of the item
     * @param q integer for the quantity of the item
     */
    public Item(int x, int y, int width, int height, int id, int q) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = id;
        qnty = q;
        artBuild();
        conBox = new Rectangle(x - 10, y - 10, width + 20, height + 20);
    }

    /**
     * Constructor for items without a location and size only needing an id and
     * quantity
     *
     * @param id integer for the id of the item
     * @param q integer for the quantity of the item
     */
    public Item(int id, int q) {
        this.id = id;
        qnty = q;
        artBuild();
    }

    /**
     * Constructor for items without a location and size only needing an id and
     * quantity
     *
     * @param id integer for the id of the item
     * @param q integer for the quantity of the item
     * @param health integer for the health of the item for tools and such
     */
    public Item(int id, int q, int health) {
        this.id = id;
        qnty = q;
        this.health = health;
        artBuild();
    }

    /**
     * Item constructor for items in the world not needing a health number
     *
     * @param x integer for the x position
     * @param y integer for the y position
     * @param width integer for the width of the item
     * @param height integer for the height of the item
     * @param id integer for the id of the item
     * @param q integer for the quantity of the item
     * @param health integer for the health of the item (not needed for most
     * items except tools
     */
    public Item(int x, int y, int width, int height, int id, int q, int health) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = id;
        this.health = health;
        qnty = q;
        artBuild();
        conBox = new Rectangle(x - 10, y - 10, width + 20, height + 20);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="toolTips">
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loc">
    public Point loc(ESC engine) {
        return new Point(MouseInfo.getPointerInfo().getLocation().x - engine.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - engine.frame.getY() - Math.abs(engine.frame.getLocationOnScreen().y - engine.canvas.getLocationOnScreen().y));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="comment">
    public void artBuild() {
        art = new BufferedImage[]{eng.assetLdr.logs, eng.assetLdr.rocks, eng.assetLdr.emptyBucket, eng.assetLdr.fullBucket, eng.assetLdr.apple, eng.assetLdr.sapling, eng.assetLdr.stick,
            eng.assetLdr.paper, eng.assetLdr.plank, eng.assetLdr.leaf, eng.assetLdr.axe1, eng.assetLdr.shovel1, eng.assetLdr.pick1, eng.assetLdr.sign, eng.assetLdr.fence, eng.assetLdr.torches.get(0)};
    }
    //</editor-fold>
}
