/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map;

import Engine.Engine.ESC;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author DribbleWare
 */
public class WorldObjects {

    public final int DEFAULT_BUTTON_WIDTH = 120;
    public final int DEFAULT_BUTTON_HEIGHT = 40;

    public Rectangle size, collis;
    public Rectangle menuBox;
    public int x, y, ID = 0, count = 0;

    public int h, w;

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

    public Color good = new Color(0, 150, 0, 150);
    public Color bad = new Color(150, 0, 0, 150);

    public ESC eng;

    public Ellipse2D light = new Ellipse2D.Double(0, 0, 0, 0);

    public Font text = new Font("Courier", Font.BOLD + Font.ITALIC, 25), text2 = new Font("Helvetica", Font.BOLD, 32), text3 = new Font("Courier", Font.BOLD, 15);

    public void update() {
    }

    public void mouseUpdate() {
        if (!mouseDelay) {
            mouse++;
        }
        if (mouse >= maxMouse) {
            mouseDelay = true;
            mouse = 0;
        }
    }

    public void priorityRender(Graphics g) {
    }

    public void popUpRender(Graphics g) {
    }

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
        Rectangle temps = new Rectangle(menuBox.x + eng.getXOff() + menuBox.width - text.getSize() - 4, menuBox.y + eng.getYOff() + 2, 26, 26);
        if (!contains(temps, false)) {
            g.setColor(Color.red);
            g.drawString("x", menuBox.x + eng.getXOff() + menuBox.width - text.getSize(), menuBox.y + 24 + eng.getYOff());

        } else {
            g.setColor(Color.red);
            g.fillRect(temps.x, temps.y, temps.width, temps.height);
            g.setColor(Color.white);
            g.drawString("x", menuBox.x + eng.getXOff() + menuBox.width - text.getSize(), menuBox.y + 24 + eng.getYOff());
            if (eng.left) {
                pop = false;
            }
        }
    }

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

    public void render(Graphics g) {
    }

    public Rectangle addButton(int x, int y, int width, int height) {
        return new Rectangle(menuBox.x + x, menuBox.y + y, width, height);
    }

    public Rectangle addButton(int x, int y) {
        return new Rectangle(menuBox.x + x, menuBox.y + y, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT);
    }

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
            if (collisBox().intersects(eng.world.Objects.get(i))) {
                blocked = true;
                break;
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

    public Ellipse2D getLight() {
        return null;
    }
}
