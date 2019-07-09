/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map.Locations;

import Engine.Engine.ESC;
import Engine.Items.Support.Item;
import Engine.Map.Support.Location;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class Hub extends Location {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    public int temp = 12;
    private int xOff, yOff;
    public boolean treesTemp = false;
    public int conTimer = 0;
    public boolean firstTree = true;
    //all objects and items
    public Rectangle mineDoor = new Rectangle(268, 1960, 102, 116);

    public List<Rectangle> ponds = new ArrayList<>();
    public Rectangle pond1;
    public Rectangle Haybales;
    public Rectangle House1, House2, House3, House4, House5;
    public Rectangle Wheelbarrow1, Wheelbarrow2, Wheelbarrow3;
    public Rectangle Pitchfork;
    public Rectangle Pond2_1, Pond2_2, Pond2_3, Pond2_4, Pond2_5;
    public Rectangle Pond2_6, Pond2_7, Pond2_8, Pond2_9, Pond2_10;
    public Rectangle Pond2_11, Pond2_12;
    public Area mines, mines2;
//</editor-fold>

    //gotta fix this wayyyyy too much work
    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public Hub(ESC engine) {
        filter = new Color(0, 0, 0, 200);
        FILTER = new Color(0, 0, 0, 200);
        backGround = new Color(50, 180, 255);
        eng = engine;
        dark = false;
        permDark = false;

        mines = new Area(new Polygon(new int[]{0, 1065, 1070, 816, 1054, 1054, 0}, new int[]{1218, 1218, 1860, 1660, 1660, 1260, 1260}, 7));
        mines2 = new Area(new Polygon(new int[]{1020, 370, 370, 268, 268, 0, 0, 802}, new int[]{2036, 2036, 1970, 1970, 2036, 2036, 1830, 1830}, 8));
        //
        //Ponds
        pond1 = new Rectangle(273 * eng.size, 140 * eng.size, 62 * eng.size, 68 * eng.size);
        ponds.add(pond1);
        //Haybales
        Haybales = new Rectangle(372 * eng.size, 616 * eng.size, 100 * eng.size, 192 * eng.size);
        //House
        House1 = new Rectangle(706 * eng.size, 80 * eng.size, 360 * eng.size, 140 * eng.size);
        House2 = new Rectangle(706 * eng.size, 220 * eng.size, 92 * eng.size, 36 * eng.size);
        House3 = new Rectangle(830 * eng.size, 220 * eng.size, 92 * eng.size, 36 * eng.size);
        House4 = new Rectangle(922 * eng.size, 80 * eng.size, 144 * eng.size, 186 * eng.size);
        House5 = new Rectangle(928 * eng.size, 266 * eng.size, 128 * eng.size, 30 * eng.size);
        //Wheelbarrow
        Wheelbarrow1 = new Rectangle(1026 * eng.size, 325 * eng.size, 32 * eng.size, 16 * eng.size);
        Wheelbarrow2 = new Rectangle(1057 * eng.size, 325 * eng.size, 31 * eng.size, 3 * eng.size);
        Wheelbarrow3 = new Rectangle(1008 * eng.size, 335 * eng.size, 15 * eng.size, 12 * eng.size);
        //Pitchfork
        Pitchfork = new Rectangle(833 * eng.size, 390 * eng.size, 30 * eng.size, 26 * eng.size);
        //Pon2
        Pond2_1 = new Rectangle(942 * eng.size, 555 * eng.size, 134 * eng.size, 60 * eng.size);
        ponds.add(Pond2_1);
        Pond2_2 = new Rectangle(912 * eng.size, 615 * eng.size, 194 * eng.size, 164 * eng.size);
        ponds.add(Pond2_2);
        Pond2_3 = new Rectangle(1076 * eng.size, 583 * eng.size, 30 * eng.size, 32 * eng.size);
        ponds.add(Pond2_3);
        Pond2_4 = new Rectangle(912 * eng.size, 779 * eng.size, 165 * eng.size, 32 * eng.size);
        ponds.add(Pond2_4);
        Pond2_5 = new Rectangle(1077 * eng.size, 779 * eng.size, 25 * eng.size, 11 * eng.size);
        ponds.add(Pond2_5);
        Pond2_6 = new Rectangle(925 * eng.size, 811 * eng.size, 135 * eng.size, 12 * eng.size);
        ponds.add(Pond2_6);
        Pond2_7 = new Rectangle(943 * eng.size, 823 * eng.size, 100 * eng.size, 25 * eng.size);
        ponds.add(Pond2_7);
        Pond2_8 = new Rectangle(975 * eng.size, 848 * eng.size, 68 * eng.size, 25 * eng.size);
        ponds.add(Pond2_8);
        Pond2_9 = new Rectangle(965 * eng.size, 848 * eng.size, 10 * eng.size, 5 * eng.size);
        ponds.add(Pond2_9);
        Pond2_10 = new Rectangle(975 * eng.size, 873 * eng.size, 60 * eng.size, 10 * eng.size);
        ponds.add(Pond2_10);
        Pond2_11 = new Rectangle(975 * eng.size, 883 * eng.size, 35 * eng.size, 95 * eng.size);
        ponds.add(Pond2_11);
        Pond2_12 = new Rectangle(1010 * eng.size, 938 * eng.size, 110 * eng.size, 40 * eng.size);
        ponds.add(Pond2_12);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Render Graphics g">
    @Override
    public void render(Graphics g) {
        //update for the x and y offset for drawing
        int rendercount = 0;
        xOff = eng.getXOff();
        yOff = eng.getYOff();

        //Draw hub
        g.drawImage(ESC.assetLdr.Hub1, xOff, yOff, ESC.assetLdr.Hub1.getWidth() * eng.size, ESC.assetLdr.Hub1.getHeight() * eng.size, null);
        g.setColor(Color.red);

        //Outline for collision box es if debug mode is on
        if (eng.debug == true) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform offset = new AffineTransform();
            offset.translate(eng.getXOff(), eng.getYOff());
            for (int i = 0; i < objects.size(); i++) {
                g2d.draw(objects.get(i).createTransformedArea(offset));
            }
            for (int i = 0; i < items.size(); i++) {
                g.drawRect(items.get(i).x + xOff, items.get(i).y + yOff, items.get(i).width, items.get(i).height);
                g.drawRect(items.get(i).conBox.x + xOff, items.get(i).conBox.y + yOff, items.get(i).conBox.width, items.get(i).conBox.height);
            }

            for (int i = 0; i < obbys.size(); i++) {
                if (obbys.get(i).size != null) {
                    g.drawRect(obbys.get(i).size.x + eng.getXOff(), obbys.get(i).size.y + eng.getYOff(), obbys.get(i).size.width, obbys.get(i).size.height);
                }
            }
        }
        //Drawing all of the items to their world location
        for (int i = 0; i < items.size(); i++) {
            g.drawImage(items.get(i).art[items.get(i).id], items.get(i).x + xOff, items.get(i).y + yOff, items.get(i).width, items.get(i).height, null);
        }
        for (int i = 0; i < obbys.size(); i++) {
            if (new Rectangle(0, 0, eng.sizew, eng.sizeh).intersects(new Rectangle(obbys.get(i).x + eng.getXOff(), obbys.get(i).y + eng.getYOff(), obbys.get(i).w, obbys.get(i).h))) {
                obbys.get(i).render(g);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Priority Render - renders before the player">
    @Override
    public void priorityRender(Graphics g) {
        for (int i = 0; i < obbys.size(); i++) {
            if (new Rectangle(0, 0, eng.sizew, eng.sizeh).intersects(new Rectangle(obbys.get(i).x + eng.getXOff(), obbys.get(i).y + eng.getYOff(), obbys.get(i).w, obbys.get(i).h))) {
                obbys.get(i).priorityRender(g);
            }
        }

        for (int i = 0; i < obbys.size(); i++) {
            if (new Rectangle(0, 0, eng.sizew, eng.sizeh).intersects(new Rectangle(obbys.get(i).x + eng.getXOff(), obbys.get(i).y + eng.getYOff(), obbys.get(i).w, obbys.get(i).h))) {
                obbys.get(i).popUpRender(g);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="overlayRender Graphics g">
    @Override
    public void overlayRender(Graphics g) {
        g.drawImage(eng.assetLdr.overlay, Integer.parseInt(eng.xoff), Integer.parseInt(eng.yoff), eng.assetLdr.overlay.getWidth() * eng.size, eng.assetLdr.overlay.getHeight() * eng.size, null);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="overlayPriorityRender Graohics g">
    @Override
    public void overlayPriorityRender(Graphics g) {
        if (eng.inventory) {
            g.setColor(Color.green);
            for (int i = 0; i < eng.world.items.size(); i++) {
                g.drawRect(eng.world.items.get(i).x + Integer.parseInt(eng.xoff), eng.world.items.get(i).y + Integer.parseInt(eng.yoff), eng.world.items.get(i).width, eng.world.items.get(i).height);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Update">
    public void update() {
        if (new Rectangle(mineDoor.x + eng.getXOff(), mineDoor.y + eng.getYOff(), mineDoor.width, mineDoor.height).contains(eng.mainChar.collbox)) {
            toMine();
        }
        map = new Rectangle(eng.getXOff(), eng.getYOff(), eng.assetLdr.Hub1.getWidth() * eng.size, eng.assetLdr.Hub1.getHeight() * eng.size);
        conTimer++;
        if (conTimer >= 6) {
            condense();
            conTimer = 0;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Item update">
    @Override
    public void itemUpdate() {
        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).update();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addItems">
    public void addItems() {
        items.add(new Item(100, 100, 50, 50, 5, 2));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Contains Rectangle boxIn">
    public boolean contains(Rectangle boxin) {
        boolean ret = false;

        Rectangle click = new Rectangle(boxin.x + eng.getXOff(), boxin.y + eng.getYOff(), boxin.width, boxin.height);
        if (click.contains(new Point(MouseInfo.getPointerInfo().getLocation().x - eng.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - eng.frame.getY() - Math.abs(eng.frame.getLocationOnScreen().y - eng.canvas.getLocationOnScreen().y)))) {
            ret = true;
        }
        return ret;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="condense">
    private void condense() {
        int condensed = 0;
        boolean con = true;
        while (con) {
            for (int i = 0; i < items.size(); i++) {
                for (int j = 0; j < items.size(); j++) {
                    if (i != j) {
                        if (recBuild(items.get(i).conBox, false).intersects(recBuild(items.get(j).conBox, false))) {
                            if (items.get(i).id == items.get(j).id) {
                                condensed++;
                                items.get(i).qnty += items.get(j).qnty;
                                items.remove(j);
                                break;
                            }
                        }
                    }
                }
            }
            con = false;
        }
        if (condensed > 30) {
            try {
                eng.saver.itemSave();
                System.out.println("items saved after condensation");
                System.out.println(condensed);
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                System.out.println("Saving Failed");
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="RecBuild Rectangle boxIn boolean addOff">
    public Rectangle recBuild(Rectangle in, boolean addOff) {
        Rectangle ret;
        if (addOff) {
            ret = new Rectangle(in.x + eng.getXOff(), in.y + eng.getYOff(), in.width, in.height);
        } else {
            ret = new Rectangle(in.x, in.y, in.width, in.height);
        }
        return ret;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="updateList">
    @Override
    public void updateList() {
        objects.clear();
        order();
        for (int i = 0; i < obbys.size(); i++) {
            if (obbys.get(i).dropped == false) {
                //do nothing
            } else {
                objects.add(new Area(obbys.get(i).collisBox()));
            }
        }
        //44
        //23
        objects.add(new Area(pond1));
        objects.add(new Area(Haybales));
        objects.add(new Area(House1));
        objects.add(new Area(House2));
        objects.add(new Area(House3));
        objects.add(new Area(House4));
        objects.add(new Area(House5));
        objects.add(new Area(Wheelbarrow1));
        objects.add(new Area(Wheelbarrow2));
        objects.add(new Area(Wheelbarrow3));
        objects.add(new Area(Pitchfork));
        objects.add(new Area(Pond2_1));
        objects.add(new Area(Pond2_2));
        objects.add(new Area(Pond2_3));
        objects.add(new Area(Pond2_4));
        objects.add(new Area(Pond2_5));
        objects.add(new Area(Pond2_6));
        objects.add(new Area(Pond2_7));
        objects.add(new Area(Pond2_8));
        objects.add(new Area(Pond2_9));
        objects.add(new Area(Pond2_10));
        objects.add(new Area(Pond2_11));
        objects.add(new Area(Pond2_12));
        objects.add(mines);
        objects.add(mines2);
    }
    //</editor-fold>
}
