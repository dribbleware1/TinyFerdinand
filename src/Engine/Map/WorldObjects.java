/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map;

import Engine.Engine.ESC;
import Engine.Items.Item;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class WorldObjects {

    //<editor-fold defaultstate="collapsed" desc="declarations">
    public final int DEFAULT_BUTTON_WIDTH = 120;
    public final int DEFAULT_BUTTON_HEIGHT = 40;

    public boolean hasLight = false;

    public Rectangle size, collis;
    public Rectangle menuBox;
    public Rectangle snapBox;
    public Rectangle buffer = new Rectangle(0, 0, 0, 0);
    public int x, y, ID = 0, count = -1;

    public boolean grown = false;

    public List<Rectangle> Crafts = new ArrayList<>();

    public int h, w;
    public int page = 0;

    public int mouse = 0;
    public int maxMouse = 15;

    public int actionTimer = 0;

    public boolean animated = true;
    public boolean pop = false;

    public String name;

    public boolean dropped = false;
    public boolean mouseDelay = false;
    public boolean blocked = false;
    public boolean mouseBox = false;

    public boolean left = false, right = false, up = false, down = false, changeable = true;
    public boolean cl = true, cr = true, cu = true, cd = true;

    protected boolean snapped = false;

    public Color good = new Color(0, 150, 0, 150);
    public Color bad = new Color(150, 0, 0, 150);

    public ESC eng;

    public Ellipse2D light = new Ellipse2D.Double(0, 0, 0, 0);

    public List<Item> costs = new ArrayList<>();

    public Font text = new Font("Courier", Font.BOLD + Font.ITALIC, 25), text2 = new Font("Helvetica", Font.BOLD, 32), text3 = new Font("Courier", Font.BOLD, 15), text4 = new Font("Courier", Font.BOLD, 20);
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    public void update() {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="mouseUpdate">
    public void mouseUpdate() {
        if (!mouseDelay) {
            mouse++;
        }
        if (mouse >= maxMouse) {
            mouseDelay = true;
            mouse = 0;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="priorityRender Grpahics g">
    public void priorityRender(Graphics g) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="popUpRender Grpahics g">
    public void popUpRender(Graphics g) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="basicMenu Graphic g">
    public void basicMenu(Graphics g) {
        g.setFont(text);
        g.setColor(new Color(10, 10, 10, 150));
        g.fillRect(menuBox.x + eng.getXOff(), menuBox.y + eng.getYOff(), menuBox.width, menuBox.height);
        g.setColor(new Color(200, 200, 200, 200));
        g.fillRect(menuBox.x + eng.getXOff(), menuBox.y + eng.getYOff(), menuBox.width, 30);
        g.drawRect(menuBox.x + eng.getXOff(), menuBox.y + eng.getYOff() + 30, menuBox.width - 1, menuBox.height - 30);

        //still needs some fine tuning
        g.setColor(new Color(255, 255, 255, 200));
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(2);
        g2d.setStroke(bs);
        int set = 8;
        g2d.drawLine(menuBox.x + eng.getXOff() + menuBox.width - text.getSize() - set, menuBox.y + eng.getYOff() + 8,
                menuBox.x + eng.getXOff() + menuBox.width - text.getSize() - set, menuBox.y + 22 + eng.getYOff());
        g.setColor(Color.white);
        g.drawString(name, menuBox.x + eng.getXOff() + 5, menuBox.y + text.getSize() - 2 + eng.getYOff());
        g.setFont(text2);
        Rectangle close = new Rectangle(menuBox.x + eng.getXOff() + menuBox.width - text.getSize() - 4, menuBox.y + eng.getYOff() + 2, 26, 26);
        if (!contains(close, false)) {
            g.setColor(Color.red);
            g.drawString("x", menuBox.x + eng.getXOff() + menuBox.width - text.getSize(), menuBox.y + 24 + eng.getYOff());

        } else {
            g.setColor(Color.red);
            g.fillRect(close.x, close.y, close.width, close.height);
            g.setColor(Color.white);
            g.drawString("x", menuBox.x + eng.getXOff() + menuBox.width - text.getSize(), menuBox.y + 24 + eng.getYOff());
            if (eng.left) {
                pop = false;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="menuBarButtons Graphics g">
    public void menuBarButtons(Graphics g) {
        g.setColor(new Color(255, 255, 255, 200));
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(3);
        g2d.setStroke(bs);
        //seperating lines
        g2d.drawLine(eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 8 - 30, eng.getYOff() + menuBox.y + 8,
                eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 8 - 30, eng.getYOff() + menuBox.y + 22);

        g2d.drawLine(eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 8 - 60, eng.getYOff() + menuBox.y + 8,
                eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 8 - 60, eng.getYOff() + menuBox.y + 22);

        g2d.drawLine(eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 8 - 90, eng.getYOff() + menuBox.y + 8,
                eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 8 - 90, eng.getYOff() + menuBox.y + 22);

        //interactive buttons
        Rectangle home = new Rectangle(menuBox.x + menuBox.width - text.getSize() - 4 - 90, menuBox.y + 2, 26, 26);
        if (contains(home, true) || page == 1) {
            g.drawImage(eng.assetLdr.home, eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 4 - 90, eng.getYOff() + menuBox.y + 4, (eng.assetLdr.home.getWidth() / 8) - 2, eng.assetLdr.home.getHeight() / 8, null);
            if (eng.left) {
                page = 1;
            }
        } else {
            g.drawImage(eng.assetLdr.home2, eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 4 - 90, eng.getYOff() + menuBox.y + 4, (eng.assetLdr.home.getWidth() / 8) - 2, eng.assetLdr.home.getHeight() / 8, null);
        }

        Rectangle craft = new Rectangle(menuBox.x + menuBox.width - text.getSize() - 4 - 60, menuBox.y + 2, 26, 26);
        if (contains(craft, true) || page == 2) {
            g.drawImage(eng.assetLdr.craft, eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 4 - 60, eng.getYOff() + menuBox.y + 4, (eng.assetLdr.craft.getWidth() / 7) + 2, eng.assetLdr.craft.getHeight() / 6, null);
            if (eng.left && page != 2) {
                page = 2;
                eng.notches = 0;
            }
        } else {
            g.drawImage(eng.assetLdr.craft2, eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 4 - 60, eng.getYOff() + menuBox.y + 4, (eng.assetLdr.craft.getWidth() / 7) + 2, eng.assetLdr.craft.getHeight() / 6, null);
        }
        pickMenu(g, true);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pickMenu Grpahics g">
    public void pickMenu(Graphics g, boolean offset) {
        g.setColor(new Color(255, 255, 255, 200));
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(3);
        g2d.setStroke(bs);
        if (offset) {
            g2d.drawLine(eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 8, eng.getYOff() + menuBox.y + 8,
                    eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 8, eng.getYOff() + menuBox.y + 22);

            Rectangle pick = new Rectangle(menuBox.x + menuBox.width - text.getSize() - 4 - 30, menuBox.y + 2, 26, 26);
            if (contains(pick, true)) {
                if (actionTimer <= 0) {
                    g.drawImage(eng.assetLdr.pickUp, eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 4 - 30, eng.getYOff() + menuBox.y + 2, (eng.assetLdr.craft.getWidth() / 7) + 2, (eng.assetLdr.craft.getHeight() / 5) + 3, null);
                } else {
                    g.drawImage(eng.assetLdr.pickUp3, eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 4 - 30, eng.getYOff() + menuBox.y + 2, (eng.assetLdr.craft.getWidth() / 7) + 2, (eng.assetLdr.craft.getHeight() / 5) + 3, null);
                }
                if (eng.left && mouseDelay) {
                    pickUp();
                    mouseDelay = false;
                }

            } else {
                g.drawImage(eng.assetLdr.pickUp2, eng.getXOff() + menuBox.x + menuBox.width - text.getSize() - 4 - 30, eng.getYOff() + menuBox.y + 2, (eng.assetLdr.craft.getWidth() / 7) + 2, (eng.assetLdr.craft.getHeight() / 5) + 3, null);
            }
        } else {
            g2d.drawLine(menuBox.x + menuBox.width - text.getSize() - 8, menuBox.y + 8,
                    menuBox.x + menuBox.width - text.getSize() - 8, menuBox.y + 22);

            Rectangle pick = new Rectangle(menuBox.x + menuBox.width - text.getSize() - 4 - 30, menuBox.y + 2, 26, 26);
            if (contains(pick, false)) {
                if (actionTimer <= 0) {
                    g.drawImage(eng.assetLdr.pickUp, menuBox.x + menuBox.width - text.getSize() - 4 - 30, menuBox.y + 2, (eng.assetLdr.craft.getWidth() / 7) + 2, (eng.assetLdr.craft.getHeight() / 5) + 3, null);
                } else {
                    g.drawImage(eng.assetLdr.pickUp3, menuBox.x + menuBox.width - text.getSize() - 4 - 30, menuBox.y + 2, (eng.assetLdr.craft.getWidth() / 7) + 2, (eng.assetLdr.craft.getHeight() / 5) + 3, null);
                }
                if (eng.left && mouseDelay) {
                    pickUp();
                    mouseDelay = false;
                }

            } else {
                g.drawImage(eng.assetLdr.pickUp2, menuBox.x + menuBox.width - text.getSize() - 4 - 30, menuBox.y + 2, (eng.assetLdr.craft.getWidth() / 7) + 2, (eng.assetLdr.craft.getHeight() / 5) + 3, null);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="basicMenu Graphic g int i">
    public void basicMenu(Graphics g, int i) {
        g.setFont(text);
        g.setColor(new Color(10, 10, 10, 150));
        g.fillRect(menuBox.x, menuBox.y, menuBox.width, menuBox.height);
        g.setColor(new Color(200, 200, 200, 200));
        g.fillRect(menuBox.x, menuBox.y, menuBox.width, 30);
        g.drawRect(menuBox.x, menuBox.y + 30, menuBox.width - 1, menuBox.height - 30);

        //still needs some fine tuning
        g.setColor(new Color(255, 255, 255, 200));
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(3);
        g2d.setStroke(bs);
        int set = 8;
        g2d.drawLine(menuBox.x + menuBox.width - text.getSize() - set, menuBox.y + set,
                menuBox.x + menuBox.width - text.getSize() - set, menuBox.y + 22);
        g.setColor(Color.white);
        g.drawString(name, menuBox.x + 5, menuBox.y + text.getSize() - 2);
        g.setFont(text2);
        Rectangle temps = new Rectangle(menuBox.x + menuBox.width - text.getSize() - 4, menuBox.y + 2, 26, 26);
        if (!contains(temps, false)) {
            g.setColor(Color.red);
            g.drawString("x", menuBox.x + menuBox.width - text.getSize(), menuBox.y + 24);

        } else {
            g.setColor(Color.red);
            g.fillRect(temps.x, temps.y, temps.width, temps.height);
            g.setColor(Color.white);
            g.drawString("x", menuBox.x + menuBox.width - text.getSize(), menuBox.y + 24);
            if (eng.left) {
                pop = false;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="menuControl">
    public void menuControl() {
        if (!eng.frame.contains(menuBox.x + eng.getXOff(), menuBox.y + eng.getYOff()) || !eng.frame.contains(menuBox.x + eng.getXOff() + menuBox.width, menuBox.y + eng.getYOff() + menuBox.height)) {
            pop = false;
        }
        if (!contains(menuBox, true) && eng.left && mouseDelay) {
            pop = false;
            mouseDelay = false;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="menuControl int i">
    public void menuControl(int i) {
        if (!contains(menuBox, true) && eng.left && mouseDelay) {
            pop = false;
            mouseDelay = false;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="render Grpahics g">
    public void render(Graphics g) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addButton int x, int y, int width, int height">
    public Rectangle addButton(int x, int y, int width, int height) {
        return new Rectangle(menuBox.x + x, menuBox.y + y, width, height);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addButton int x, int y *uses default size">
    public Rectangle addButton(int x, int y) {
        return new Rectangle(menuBox.x + x, menuBox.y + y, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT);
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

    //<editor-fold defaultstate="collapsed" desc="drop">
    public void drop() {
        animated = false;
        int Mx = MouseInfo.getPointerInfo().getLocation().x - eng.getFrameX() - eng.getXOff();
        int My = MouseInfo.getPointerInfo().getLocation().y - eng.getFrameY() - eng.getYOff();
        if (eng.mainChar.box2.contains(Mx + eng.getXOff(), My + eng.getYOff())) {
            x = Mx - w / 2;
            y = (My - h / 2) - 32;
            mouseBox = true;
        } else {
            mouseBox = false;
        }

        for (int i = 0; i < eng.world.Objects.size(); i++) {
            if (collisBox().intersects(eng.world.Objects.get(i)) || buffer.intersects(eng.world.Objects.get(i))) {
                if (!snapped) {
                    blocked = true;
                    break;
                }
            } else if (new Rectangle(collisBox().x + eng.getXOff(), collisBox().y + eng.getYOff(), collisBox().width, collisBox().height).intersects(eng.mainChar.collbox)) {
                blocked = true;
                break;
            } else {
                blocked = false;
            }
        }
        if (eng.left && !blocked && mouseDelay) {
            dropped = true;
            animated = true;
            mouseDelay = false;
            size = new Rectangle(x, y, w, h);
            collis = collisBox();
            eng.world.updatelist();
            System.out.println("droppeds");

        }

        if (eng.right) {
            eng.world.active.obbys.remove(this);
            for (int i = 0; i < costs.size(); i++) {
                eng.mainChar.inv.itemAdd(costs.get(i).id, costs.get(i).qnty);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="collisBox()">
    public Rectangle collisBox() {
        return new Rectangle(0, 0, 0, 0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="RecBuilder rectangle in">
    public Rectangle recBuilder(Rectangle in) {
        return new Rectangle(in.x + eng.getXOff(), in.y + eng.getYOff(), in.width, in.height);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getLight">
    public Ellipse2D getLight() {
        return null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="closeAll">
    public void closeAll(WorldObjects holder) {

        for (int i = 0; i < eng.world.active.obbys.size(); i++) {
            if (eng.world.active.obbys.get(i) == holder) {

            } else {
                eng.world.active.obbys.get(i).pop = false;
            }
        }
        if (eng.inventory) {
            eng.inventory = false;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pickUp">
    public void pickUp() {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="part">
    public void part() {

    }
    //</editor-fold>

}
