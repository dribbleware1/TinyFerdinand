/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map;

import Engine.Engine.ESC;
import Engine.Items.CampFire;
import Engine.Items.Fence;
import Engine.Items.Tree;
import Engine.Items.Item;
import Engine.Items.WorkBench;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class Hub {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    public int temp = 12;
    ESC engi;
    private int xOff, yOff;
    public boolean treesTemp = false;
    public boolean water = false;
    public int conTimer = 0;
    public boolean firstTree = true;

    //Lists to store all objects and items
    public List<Rectangle> objects = new ArrayList<>();
    public List<Item> items = new ArrayList<>();
    public List<Tree> trees = new ArrayList<>();
    public List<Rectangle> ponds = new ArrayList<>();
    public List<Integer> treeNums = new ArrayList<>();
    public List<CampFire> fires = new ArrayList<>();
    public List<WorkBench> benches = new ArrayList<>();
    public List<WorldObjects> obbys = new ArrayList<>();

    public Rectangle Tree1, Tree2, Tree3, Tree4, Tree5, Tree6;
    public Rectangle pond1;
    public Rectangle shrub1, shrub2;
    public Rectangle Fence1, Fence2, Fence3, Fence4, Fence5;
    public Rectangle Btree1, Btree2, Btree3;
    public Rectangle Haybales;
    public Rectangle Tomb;
    public Rectangle House1, House2, House3, House4, House5;
    public Rectangle Wheelbarrow1, Wheelbarrow2, Wheelbarrow3;
    public Rectangle Pitchfork;
    public Rectangle Pond2_1, Pond2_2, Pond2_3, Pond2_4, Pond2_5;
    public Rectangle Pond2_6, Pond2_7, Pond2_8, Pond2_9, Pond2_10;
    public Rectangle Pond2_11, Pond2_12;
//</editor-fold>

    //gotta fix this wayyyyy too much work
    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public Hub(ESC eng) {
        engi = eng;

        //Ponds
        pond1 = new Rectangle(273 * engi.size, 140 * engi.size, 62 * engi.size, 68 * engi.size);
        ponds.add(pond1);
        //Shrubs
        shrub1 = new Rectangle(457 * engi.size, 214 * engi.size, 10 * engi.size, 10 * engi.size);
        shrub2 = new Rectangle(489 * engi.size, 278 * engi.size, 10 * engi.size, 10 * engi.size);
        //Fences
        Fence1 = new Rectangle(44 * engi.size, 405 * engi.size, 262 * engi.size, 4 * engi.size);
        Fence2 = new Rectangle(44 * engi.size, 405 * engi.size, 7 * engi.size, 227 * engi.size);
        Fence3 = new Rectangle(300 * engi.size, 405 * engi.size, 7 * engi.size, 101 * engi.size);
        Fence4 = new Rectangle(300 * engi.size, 561 * engi.size, 7 * engi.size, 73 * engi.size);
        Fence5 = new Rectangle(44 * engi.size, 630 * engi.size, 262 * engi.size, 4 * engi.size);
        //Big trees
        //Tomb
        Tomb = new Rectangle(144 * engi.size, 410 * engi.size, 62 * engi.size, 70 * engi.size);
        //Haybales
        Haybales = new Rectangle(372 * engi.size, 616 * engi.size, 100 * engi.size, 192 * engi.size);
        //House
        House1 = new Rectangle(706 * engi.size, 80 * engi.size, 360 * engi.size, 140 * engi.size);
        House2 = new Rectangle(706 * engi.size, 220 * engi.size, 92 * engi.size, 36 * engi.size);
        House3 = new Rectangle(830 * engi.size, 220 * engi.size, 92 * engi.size, 36 * engi.size);
        House4 = new Rectangle(922 * engi.size, 80 * engi.size, 144 * engi.size, 186 * engi.size);
        House5 = new Rectangle(928 * engi.size, 266 * engi.size, 128 * engi.size, 30 * engi.size);
        //Wheelbarrow
        Wheelbarrow1 = new Rectangle(1026 * engi.size, 325 * engi.size, 32 * engi.size, 16 * engi.size);
        Wheelbarrow2 = new Rectangle(1057 * engi.size, 325 * engi.size, 31 * engi.size, 3 * engi.size);
        Wheelbarrow3 = new Rectangle(1008 * engi.size, 335 * engi.size, 15 * engi.size, 12 * engi.size);
        //Pitchfork
        Pitchfork = new Rectangle(833 * engi.size, 390 * engi.size, 30 * engi.size, 26 * engi.size);
        //Pon2
        Pond2_1 = new Rectangle(942 * engi.size, 555 * engi.size, 134 * engi.size, 60 * engi.size);
        ponds.add(Pond2_1);
        Pond2_2 = new Rectangle(912 * engi.size, 615 * engi.size, 194 * engi.size, 164 * engi.size);
        ponds.add(Pond2_2);
        Pond2_3 = new Rectangle(1076 * engi.size, 583 * engi.size, 30 * engi.size, 32 * engi.size);
        ponds.add(Pond2_3);
        Pond2_4 = new Rectangle(912 * engi.size, 779 * engi.size, 165 * engi.size, 32 * engi.size);
        ponds.add(Pond2_4);
        Pond2_5 = new Rectangle(1077 * engi.size, 779 * engi.size, 25 * engi.size, 11 * engi.size);
        ponds.add(Pond2_5);
        Pond2_6 = new Rectangle(925 * engi.size, 811 * engi.size, 135 * engi.size, 12 * engi.size);
        ponds.add(Pond2_6);
        Pond2_7 = new Rectangle(943 * engi.size, 823 * engi.size, 100 * engi.size, 25 * engi.size);
        ponds.add(Pond2_7);
        Pond2_8 = new Rectangle(975 * engi.size, 848 * engi.size, 68 * engi.size, 25 * engi.size);
        ponds.add(Pond2_8);
        Pond2_9 = new Rectangle(965 * engi.size, 848 * engi.size, 10 * engi.size, 5 * engi.size);
        ponds.add(Pond2_9);
        Pond2_10 = new Rectangle(975 * engi.size, 873 * engi.size, 60 * engi.size, 10 * engi.size);
        ponds.add(Pond2_10);
        Pond2_11 = new Rectangle(975 * engi.size, 883 * engi.size, 35 * engi.size, 95 * engi.size);
        ponds.add(Pond2_11);
        Pond2_12 = new Rectangle(1010 * engi.size, 938 * engi.size, 110 * engi.size, 40 * engi.size);
        ponds.add(Pond2_12);

        //Adding objects for collision map
        //<editor-fold defaultstate="collapsed" desc="Adding everything to the object list">
        //objects.add(trees.get(0).getBox());
        //objects.add(trees.get(1).tree);
        objects.add(pond1);
        objects.add(shrub1);
        objects.add(shrub2);
        objects.add(Fence1);
        objects.add(Fence2);
        objects.add(Fence3);
        objects.add(Fence4);
        objects.add(Fence5);
        objects.add(Tomb);
        objects.add(Haybales);
        objects.add(House1);
        objects.add(House2);
        objects.add(House3);
        objects.add(House4);
        objects.add(House5);
        objects.add(Wheelbarrow1);
        objects.add(Wheelbarrow2);
        objects.add(Wheelbarrow3);
        objects.add(Pitchfork);
        objects.add(Pond2_1);
        objects.add(Pond2_2);
        objects.add(Pond2_3);
        objects.add(Pond2_4);
        objects.add(Pond2_5);
        objects.add(Pond2_6);
        objects.add(Pond2_7);
        objects.add(Pond2_8);
        objects.add(Pond2_9);
        objects.add(Pond2_10);
        objects.add(Pond2_11);
        objects.add(Pond2_12);
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Stuff">
    //reorganizes, adds and sorts the objects to the obbys list to create the render order
    public void stuff() {
        boolean order = true;
        WorldObjects holder;
        if (obbys.size() >= 2) {
            for (int i = 1; i < obbys.size(); i++) {
                if (obbys.get(i).collis.y < obbys.get(i - 1).collis.y) {
                    holder = obbys.get(i);
                    obbys.set(i, obbys.get(i - 1));
                    obbys.set(i - 1, holder);
                    i = 1;
                }
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Render Graphics g">
    public void render(Graphics g) {
        //update for the x and y offset for drawing
        xOff = engi.getXOff();
        yOff = engi.getYOff();

        //Draw hub
        g.drawImage(engi.assetLdr.Hub1, xOff, yOff, engi.assetLdr.Hub1.getWidth() * engi.size, engi.assetLdr.Hub1.getHeight() * engi.size, null);
        g.setColor(Color.red);

        //Outline for collision box es if debug mode is on
        if (engi.debug == true) {
            for (int i = 0; i < objects.size(); i++) {
                g.drawRect(objects.get(i).x + xOff, objects.get(i).y + yOff, objects.get(i).width, objects.get(i).height);
            }
            for (int i = 0; i < items.size(); i++) {
                g.drawRect(items.get(i).x + xOff, items.get(i).y + yOff, items.get(i).width, items.get(i).height);
                //g.drawRect(items.get(i).conBox.x + xOff, items.get(i).conBox.y + yOff, items.get(i).conBox.width, items.get(i).conBox.height);
            }

            for (int i = 0; i < obbys.size(); i++) {
                if (obbys.get(i).size != null) {
                    g.drawRect(obbys.get(i).size.x + engi.getXOff(), obbys.get(i).size.y + engi.getYOff(), obbys.get(i).size.width, obbys.get(i).size.height);
                }
            }

        }

        //Drawing all of the items to their world location
        for (int i = 0; i < items.size(); i++) {
            g.drawImage(items.get(i).art[items.get(i).id], items.get(i).x + xOff, items.get(i).y + yOff, items.get(i).width, items.get(i).height, null);
        }
        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).render(g);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Priority Render - renders before the player">
    public void priorityRender(Graphics g) {
        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).priorityRender(g);
        }

        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).popUpRender(g);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Update">
    public void update() {
        engi.mainChar.map = new Rectangle(Integer.parseInt(engi.xoff), Integer.parseInt(engi.yoff), engi.assetLdr.Hub1.getWidth() * engi.size, engi.assetLdr.Hub1.getHeight() * engi.size);
        conTimer++;
        if (conTimer >= 6) {
            condense();
            conTimer = 0;
        }

        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).update();
        }

        waterCheck();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addItems">
    public void addItems() {
        items.add(new Item(100, 100, 50, 50, 5, 2));

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Water Check">
    public void waterCheck() {
        for (int i = 0; i < ponds.size(); i++) {
            if (contains(ponds.get(i))) {
                water = true;
                return;
            } else {
                water = false;
                return;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Contains Rectangle boxIn">
    public boolean contains(Rectangle boxin) {
        boolean ret = false;

        Rectangle click = new Rectangle(boxin.x + engi.getXOff(), boxin.y + engi.getYOff(), boxin.width, boxin.height);
        if (click.contains(new Point(MouseInfo.getPointerInfo().getLocation().x - engi.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - engi.frame.getY() - Math.abs(engi.frame.getLocationOnScreen().y - engi.canvas.getLocationOnScreen().y)))) {
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
                engi.saver.itemSave();
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
            ret = new Rectangle(in.x + engi.getXOff(), in.y + engi.getYOff(), in.width, in.height);
        } else {
            ret = new Rectangle(in.x, in.y, in.width, in.height);
        }
        return ret;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="updateList">
    public void updateList() {
        objects.clear();

        stuff();

        for (int i = 0; i < obbys.size(); i++) {
            if (obbys.get(i).dropped == false) {
                //do nothing
            } else {
                objects.add(obbys.get(i).collisBox());
            }
            //updateTrees();
            objects.add(pond1);
            objects.add(shrub1);
            objects.add(shrub2);
            objects.add(Fence1);
            objects.add(Fence2);
            objects.add(Fence3);
            objects.add(Fence4);
            objects.add(Fence5);
            objects.add(Tomb);
            objects.add(Haybales);
            objects.add(House1);
            objects.add(House2);
            objects.add(House3);
            objects.add(House4);
            objects.add(House5);
            objects.add(Wheelbarrow1);
            objects.add(Wheelbarrow2);
            objects.add(Wheelbarrow3);
            objects.add(Pitchfork);
            objects.add(Pond2_1);
            objects.add(Pond2_2);
            objects.add(Pond2_3);
            objects.add(Pond2_4);
            objects.add(Pond2_5);
            objects.add(Pond2_6);
            objects.add(Pond2_7);
            objects.add(Pond2_8);
            objects.add(Pond2_9);
            objects.add(Pond2_10);
            objects.add(Pond2_11);
            objects.add(Pond2_12);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getWater">
    public boolean getWater() {
        return water;
    }
    //</editor-fold>
}
