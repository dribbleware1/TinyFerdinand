/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Items;

import Engine.Engine.ESC;
import Engine.Map.WorldObjects;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author DribbleWare
 */
public class Tree extends WorldObjects {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    ESC eng;

    public boolean temp = false;

    final int MAX_TIME = 120;
    final int DEFAULT_COUNT_SMALL = 5;
    final int DEFAULT_COUNT_LARGE = 8;
    final int LONG_TIME = 120;
    final int MENU_BOX_HEIGHT = 30;
    int timer = 0, longtimer = 0;
    int watering = 30;
    boolean wats = false;
    Random rand = new Random();
    private boolean pick = true, trigger = false, longtime = false, opty = false;
    public Rectangle tree, treebox, menuBox;

    private String name = "";

    int tmp = 0;
    boolean tp = false;

    public Rectangle tempBox = new Rectangle(0, 0, 0, 0);
    public Rectangle collectBox = new Rectangle(0, 0, 0, 0);

    public Font text = new Font("Courier", Font.BOLD + Font.ITALIC, 25), text2 = new Font("Helvetica", Font.BOLD, 32);

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public Tree(ESC engine, int xi, int yi, int id) {
        eng = engine;
        ID = id;
        resize(ID, xi, yi);
        x = xi;
        y = yi;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Update">
    public void update() {
        if (timer < MAX_TIME && count > 0) {
            timer++;
            pick = false;
        }
        if (timer == MAX_TIME) {
            pick = true;
        }
        mouseControl();
        if (wats) {
            watering++;
        }
        if (watering > 30) {
            wats = false;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Render Graphics g">
    public void render(Graphics g) {
        g.setFont(text);

        if (eng.mainChar.y >= tree.y + eng.getYOff()) {
            if (ID == 0) {
                g.drawImage(eng.assetLdr.smallTree, tree.x + eng.getXOff() - 101, tree.y + eng.getYOff() - 191, eng.assetLdr.smallTree.getWidth() * 4, eng.assetLdr.smallTree.getHeight() * 4, null);
            }
            if (ID == 1) {
                g.drawImage(eng.assetLdr.bigTree, x + eng.getXOff(), y + eng.getYOff(), eng.assetLdr.bigTree.getWidth() * 4, eng.assetLdr.bigTree.getHeight() * 4, null);
            }
        }

        //g.fillRect(tempBox.x, tempBox.y, tempBox.width, tempBox.height);
        if (eng.debug) {
            g.setColor(Color.red);
            g.drawRect(treebox.x + eng.getXOff(), treebox.y + eng.getYOff(), treebox.width, treebox.height);
            if (opty) {
                g.drawRect(menuBox.x, menuBox.y, menuBox.width, menuBox.height);
            }
        }
    }
//    </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Priority render Graphics g">
    public void priorityRender(Graphics g) {
        g.setFont(text);
        if (eng.mainChar.y < tree.y + eng.getYOff()) {
            if (ID == 0) {
                g.drawImage(eng.assetLdr.smallTree, tree.x + eng.getXOff() - 101, tree.y + eng.getYOff() - 191, eng.assetLdr.smallTree.getWidth() * 4, eng.assetLdr.smallTree.getHeight() * 4, null);
            }
            if (ID == 1) {
                g.drawImage(eng.assetLdr.bigTree, x + eng.getXOff(), y + eng.getYOff(), eng.assetLdr.bigTree.getWidth() * 4, eng.assetLdr.bigTree.getHeight() * 4, null);
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="popUpRender Graphics g">
    public void popUpRender(Graphics g) {
        if (opty) {
            menuRender(g);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Resize int id">
    public void resize(int id, int x, int y) {
        switch (ID) {
            case 0:
                tree = new Rectangle(x + 25, y + 48, 60, 20);
                treebox = new Rectangle(tree.x - 30, tree.y - 100, tree.width + 60, tree.height + 100);
                count = DEFAULT_COUNT_SMALL;
                name = "Small Tree";
                break;
            case 1:
                tree = new Rectangle((x + 32 * 4), (y + 133 * 4), 66 * 4, 100);
                treebox = new Rectangle(tree.x - 30, tree.y - 100, tree.width + 60, tree.height + 150);
                count = DEFAULT_COUNT_LARGE;
                name = "Big Tree";
                break;
        }

        size = new Rectangle(tree.x, tree.y, tree.width, tree.height);
        collis = size;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Contains rectangle boxIn boolean addOffSet ">
    public boolean contains(Rectangle boxin, boolean addOffSet) {
        boolean ret = false;
        Rectangle click = new Rectangle(boxin.x, boxin.y, boxin.width, boxin.height);
        if (addOffSet) {
            click = new Rectangle(boxin.x + eng.getXOff(), boxin.y + eng.getYOff(), boxin.width, boxin.height);
        }
        if (click.contains(new Point(MouseInfo.getPointerInfo().getLocation().x - eng.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - eng.frame.getY() - Math.abs(eng.frame.getLocationOnScreen().y - eng.canvas.getLocationOnScreen().y)))) {
            ret = true;
        }
        return ret;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="RecBuild">
    public Rectangle recBuild() {
        return new Rectangle(treebox.x + eng.getXOff(), treebox.y + eng.getYOff(), treebox.width, treebox.height);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Loc">
    public Point loc(ESC engine) {
        return new Point(MouseInfo.getPointerInfo().getLocation().x - engine.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - engine.frame.getY() - Math.abs(engine.frame.getLocationOnScreen().y - engine.canvas.getLocationOnScreen().y));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Mouse Control">
    private void mouseControl() {
        if (contains(treebox, true) && eng.mainChar.box.intersects(recBuild()) && eng.left) {
            opty = true;
            menuBox = new Rectangle(tree.x + tree.width / 3, tree.y, 400, 250);
        } else if ((!contains(treebox, true) || !eng.mainChar.box.intersects(recBuild())) && opty) {
            if (!contains(menuBox, true) && eng.left) {
                opty = false;
            }
        }

        if (opty) {
            tempBox = new Rectangle(menuBox.x + eng.getXOff(), menuBox.y + eng.getYOff(), menuBox.width, menuBox.height);
            if (tempBox.x < 0 || tempBox.x + tempBox.width > eng.sizew || tempBox.y < 0 || tempBox.y + tempBox.height > eng.sizeh) {
                opty = false;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Random apple">
    private void randomApple() {
        boolean apples = false;
        if (rand.nextInt(25) == 16) {
            for (int j = 0; j < eng.mainChar.inv.inven.size(); j++) {
                if (eng.mainChar.inv.inven.get(j).id == 4) {
                    eng.mainChar.inv.inven.get(j).qnty += 1;
                    apples = true;
                }
            }
            if (!apples) {
                eng.mainChar.inv.inven.add(new Item(4, 1));
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Menu render Graphics g">
    private void menuRender(Graphics g) {
        text = new Font("Courier", Font.BOLD + Font.ITALIC, 25);
        g.setFont(text);
        g.setColor(new Color(10, 10, 10, 150));
        g.fillRect(menuBox.x + eng.getXOff(), menuBox.y + eng.getYOff(), menuBox.width, 130);//menuBox.height);
        g.setColor(new Color(200, 200, 200, 200));
        g.fillRect(menuBox.x + eng.getXOff(), menuBox.y + eng.getYOff(), menuBox.width, 30);
        g.drawRect(menuBox.x + eng.getXOff(), menuBox.y + eng.getYOff() + 30, menuBox.width - 1, 100);//menuBox.height - 30);

        //
        //
        //still needs some fine tuning
        g.setColor(new Color(255, 255, 255, 200));
        Graphics2D g2d = (Graphics2D) g;
        float[] fa = {10, 10, 10};
        BasicStroke bs = new BasicStroke(2);
        g2d.setStroke(bs);
        int set = 8;
        g2d.drawLine(menuBox.x + eng.getXOff() + menuBox.width - text.getSize() - set, menuBox.y + eng.getYOff() + 8,
                menuBox.x + eng.getXOff() + menuBox.width - text.getSize() - set, menuBox.y + 22 + eng.getYOff());
        //
        //
        //
        g.setColor(Color.white);
        g.drawString(name, menuBox.x + eng.getXOff() + 5, menuBox.y + text.getSize() - 2 + eng.getYOff());
        g.setFont(text2);
        Rectangle temps = new Rectangle(menuBox.x + eng.getXOff() + menuBox.width - text.getSize() - 4, menuBox.y + eng.getYOff() + 2, 26, 26);
        //g.drawRect(temps.x, temps.y, temps.width, temps.height);
        if (!contains(temps, false)) {
            g.setColor(Color.red);
            g.drawString("x", menuBox.x + eng.getXOff() + menuBox.width - text.getSize(), menuBox.y + 24 + eng.getYOff());

        } else {
            g.setColor(Color.red);
            g.fillRect(temps.x, temps.y, temps.width, temps.height);
            g.setColor(Color.white);
            g.drawString("x", menuBox.x + eng.getXOff() + menuBox.width - text.getSize(), menuBox.y + 24 + eng.getYOff());
            if (eng.left) {
                opty = false;
            }
        }
        g.setColor(new Color(255, 87, 51));
        g.fillRect(menuBox.x + 140 + eng.getXOff(), menuBox.y + 50 + eng.getYOff(), timer * 2, 20);
        bs = new BasicStroke(1);
        g2d.setStroke(bs);
        g.setColor(Color.white);
        g.drawRect(menuBox.x + 140 + eng.getXOff(), menuBox.y + 50 + eng.getYOff(), MAX_TIME * 2, 20);

        g.drawRect(menuBox.x + 10 + eng.getXOff(), menuBox.y + eng.getYOff() + 40, 120, 40);
        collectBox = new Rectangle(menuBox.x + 10 + eng.getXOff(), menuBox.y + eng.getYOff() + 40, 120, 40);

        if (!contains(collectBox, false)) {
            g.drawString("Collect", menuBox.x + eng.getXOff() + 16, menuBox.y + 70 + eng.getYOff());
        } else {
            if (timer < MAX_TIME) {
                g.setColor(new Color(150,0,0));
            }else{
                g.setColor(new Color(0,150,0));
                if(eng.left){
                    pickup();
                }
            }
            g.fillRect(collectBox.x + 2, collectBox.y + 2, collectBox.width - 3, collectBox.height - 3);
            g.setColor(Color.white);
            g.drawString("Collect", menuBox.x + eng.getXOff() + 16, menuBox.y + 70 + eng.getYOff());
        }
        g.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 23));
        g.drawString("Logs Remaining: " + Integer.toString(count), menuBox.x + eng.getXOff() + 10, menuBox.y + eng.getYOff() + 110);
        
    //<editor-fold defaultstate="collapsed" desc="Old menu (kept for reference)">
//        for (int i = 0; i < eng.mainChar.inv.inven.size(); i++) {
//            if (eng.mainChar.inv.inven.get(i).id == 3 || eng.mainChar.inv.inven.get(i).id == 5) {
//                boxes++;
//            }
//        }
//        menuBox(boxes);
//        for (int i = 0; i < menuBoxes.size(); i++) {
//            g.drawRect(menuBoxes.get(i).x, menuBoxes.get(i).y, menuBoxes.get(i).width, menuBoxes.get(i).height);
//        }
//        menuBox.height = MENU_BOX_HEIGHT * menuBoxes.size();
//
//        g.setColor(Color.white);
//        switch (menuBoxes.size()) {
//            case 3:
//                g.drawString("Wad", menuBoxes.get(2).x + 10, menuBoxes.get(2).y + 25);
//            case 2:
//                if (count > 0) {
//                    g.setColor(new Color(128, 128, 128));
//                } else {
//                    g.setColor(Color.white);
//                }
//                g.drawString("Water", menuBoxes.get(1).x + 10, menuBoxes.get(1).y + 25);
//                if (contains(menuBoxes.get(1), false) && eng.left) {
//                    watering();
//                }
//            case 1:
//                if (count > 0 && pick) {
//                    g.setColor(Color.white);
//                } else {
//                    g.setColor(new Color(128, 128, 128));
//                }
//                g.drawString("Collect", menuBoxes.get(0).x + 10, menuBoxes.get(0).y + 25);
//                if (contains(menuBoxes.get(0), false) && eng.left && pick && count > 0) {
//                    pickup();
        //cut down
//                    eng.world.hubRoom.trees.remove(this);
//                    eng.world.hubRoom.objects.remove(this.tree);
//                    eng.world.updatelist();
//                }
//        }
//</editor-fold>
    
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Pickup">
    private void pickup() {
        pick = false;
        eng.mainChar.inv.itemReplace(0, -1, 1);
        randomApple();
        timer = 0;
        count--;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Watering">
    private void watering() {
        if (watering >= 30 && count == 0) {
            for (int i = 0; i < eng.mainChar.inv.inven.size(); i++) {
                if (eng.mainChar.inv.inven.get(i).id == 3) {
                    eng.mainChar.inv.itemReplace(2, 3, 1);
                    switch (ID) {
                        case 0:
                            count = DEFAULT_COUNT_SMALL;
                            break;
                        case 1:
                            count = DEFAULT_COUNT_LARGE;
                            break;
                    }
                    wats = true;
                    watering = 0;
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Rectangle getBox() {
        return tree;
    }
//</editor-fold>
}
