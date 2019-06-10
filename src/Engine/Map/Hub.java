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
public class Hub extends Location {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    public int temp = 12;
    ESC eng;
    private int xOff, yOff;
    public boolean treesTemp = false;
    public boolean water = false;
    public int conTimer = 0;
    public boolean firstTree = true;

    //Lists to store all objects and items
    public List<Tree> trees = new ArrayList<>();
    public List<Rectangle> ponds = new ArrayList<>();
    public List<Integer> treeNums = new ArrayList<>();
    public List<CampFire> fires = new ArrayList<>();
    public List<WorkBench> benches = new ArrayList<>();
    public Rectangle pond1;
    public Rectangle Haybales;
    public Rectangle House1, House2, House3, House4, House5;
    public Rectangle Wheelbarrow1, Wheelbarrow2, Wheelbarrow3;
    public Rectangle Pitchfork;
    public Rectangle Pond2_1, Pond2_2, Pond2_3, Pond2_4, Pond2_5;
    public Rectangle Pond2_6, Pond2_7, Pond2_8, Pond2_9, Pond2_10;
    public Rectangle Pond2_11, Pond2_12;
//</editor-fold>

    //gotta fix this wayyyyy too much work
    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public Hub(ESC engine) {
        eng = engine;

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
    public void render(Graphics g) {
        //update for the x and y offset for drawing
        xOff = eng.getXOff();
        yOff = eng.getYOff();

        //Draw hub
        g.drawImage(eng.assetLdr.Hub1, xOff, yOff, eng.assetLdr.Hub1.getWidth() * eng.size, eng.assetLdr.Hub1.getHeight() * eng.size, null);
        g.setColor(Color.red);

        //Outline for collision box es if debug mode is on
        if (eng.debug == true) {
            for (int i = 0; i < objects.size(); i++) {
                g.drawRect(objects.get(i).x + xOff, objects.get(i).y + yOff, objects.get(i).width, objects.get(i).height);
            }
            for (int i = 0; i < items.size(); i++) {
                g.drawRect(items.get(i).x + xOff, items.get(i).y + yOff, items.get(i).width, items.get(i).height);
                //g.drawRect(items.get(i).conBox.x + xOff, items.get(i).conBox.y + yOff, items.get(i).conBox.width, items.get(i).conBox.height);
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
        if (eng.getXOff() < -2485 && eng.getXOff() > -2570 && eng.getYOff() > -610) {
            eng.Loc = "mine";
            eng.world.worldSwitch();
            eng.setXOff(100);
            eng.setYOff(100);
        }

        eng.mainChar.map = new Rectangle(Integer.parseInt(eng.xoff), Integer.parseInt(eng.yoff), eng.assetLdr.Hub1.getWidth() * eng.size, eng.assetLdr.Hub1.getHeight() * eng.size);
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
    public void updateList() {
        objects.clear();

        order();

        for (int i = 0; i < obbys.size(); i++) {
            if (obbys.get(i).dropped == false) {
                //do nothing
            } else {
                objects.add(obbys.get(i).collisBox());
            }
            objects.add(pond1);
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
