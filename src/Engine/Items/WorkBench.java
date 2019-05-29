/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Items;

import Engine.Engine.ESC;
import Engine.Map.WorldObjects;
import Engine.Player.Crafting;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author DribbleWare
 */
public class WorkBench extends WorldObjects {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    private int scale = 2;
    BufferedImage image;

    int page = 1;

    Rectangle page1 = new Rectangle(0, 0, 0, 0);
    Rectangle page2 = new Rectangle(0, 0, 0, 0);
    Rectangle page3 = new Rectangle(0, 0, 0, 0);

    Crafting crafter;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public WorkBench(int xin, int yin, ESC engine) {
        eng = engine;
        image = eng.assetLdr.workBench;
        w = image.getWidth() * scale;
        h = image.getHeight() * scale;
        x = xin;
        y = yin;
        size = new Rectangle(x, y, w, h);
        collis = collisBox();
        name = "WorkBench";
        crafter = eng.mainChar.inv.crafter;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    @Override
    public void update() {
        mouseUpdate();
        if (!dropped) {
            drop();
        }
        if (contains(size, true) && eng.left && mouseDelay) {
            if (recBuilder(size).intersects(eng.mainChar.box)) {
                closeAll(this);
                pop = true;
                mouseDelay = false;
                page = 1;
                menuBox = new Rectangle(75, 50, 1375, 700);
                page1 = addButton(- 25, 40, 25, 150);
                page2 = addButton(- 25, 190, 25, 150);
                page3 = addButton(- 25, 340, 25, 150);

            } else {
                eng.pop("Too far away!", 0);
                mouseDelay = false;
            }
        }
        if (!recBuilder(size).intersects(eng.mainChar.box)) {
            pop = false;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="render Graphics g">
    public void render(Graphics g) {
        if (eng.mainChar.y >= y + eng.getYOff()) {
            g.drawImage(image, x + eng.getXOff(), y + eng.getYOff(), w, h, null);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="priorityRender Graphics g">
    public void priorityRender(Graphics g) {
        if (eng.mainChar.y < y + eng.getYOff()) {
            g.drawImage(image, x + eng.getXOff(), y + eng.getYOff(), w, h, null);
        }
        if (blocked) {
            g.setColor(new Color(255, 0, 0, 100));
            g.fillRect(x + eng.getXOff(), y + eng.getYOff(), w, h);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="popUpRender Graphics g">
    @Override
    public void popUpRender(Graphics g) {
        if (pop) {
            crafter.update();
            menuRender(g);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="menuRender Grpahics g">
    public void menuRender(Graphics g) {
        basicMenu(g, 0);

        //page 1
        if (page == 1 || contains(page1, false)) {
            g.setColor(new Color(120, 120, 120, 150));
            if (eng.left && mouseDelay) {
                page = 1;
            }
        } else {
            g.setColor(new Color(10, 10, 10, 150));
        }
        g.fillRect(page1.x, page1.y, page1.width, page1.height);
        g.setColor(new Color(200, 200, 200, 200));
        g.drawRect(page1.x, page1.y, page1.width, page1.height);
        sideText(g, page1, "Tools");

        //page 2
        if (page == 2 || contains(page2, false)) {
            g.setColor(new Color(120, 120, 120, 150));
            if (eng.left && mouseDelay) {
                page = 2;
            }
        } else {
            g.setColor(new Color(10, 10, 10, 150));
        }
        g.fillRect(page2.x, page2.y, page2.width, page2.height);
        g.setColor(new Color(200, 200, 200, 200));
        g.drawRect(page2.x, page2.y, page2.width, page2.height);
        sideText(g, page2, "Objects");

        //page 3
        if (page == 3 || contains(page3, false)) {
            g.setColor(new Color(120, 120, 120, 150));
            if (eng.left && mouseDelay) {
                page = 3;
            }
        } else {
            g.setColor(new Color(10, 10, 10, 150));
        }
        g.fillRect(page3.x, page3.y, page3.width, page3.height);
        g.setColor(new Color(200, 200, 200, 200));
        g.drawRect(page3.x, page3.y, page3.width, page3.height);
        sideText(g, page3, "Resources");

        g.setFont(text);
        switch (page) {
            case 1:
                craftSetup(g, 4);
                break;
            case 2:
                craftSetup(g, 2);
                break;
            case 3:
                craftSetup(g, 3);
                break;
        }
        crafter.workbench(g, page);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setClip(0, 0, eng.sizew, eng.sizeh);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="sideText Graphics g, Rectangle button, String input">
    public void sideText(Graphics g, Rectangle button, String input) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(-90 * 0.0174533, button.x + (button.width / 2), button.y + (button.height / 2));
        g.setFont(text4);
        g.setColor(Color.white);
        g.drawString(input, 5 + button.x - (int) (((double) input.length() / 2) * 13) + button.width / 2, button.y + (button.height / 2) + 7);
        g2d.rotate(90 * 0.0174533, button.x + (button.width / 2), button.y + (button.height / 2));
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

    //<editor-fold defaultstate="collapsed" desc="collisBox">
    public Rectangle collisBox() {
        return new Rectangle(x, y + 30, w, h - 55);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftSetup Graphics g int num">
    void craftSetup(Graphics g, int num) {
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(3);
        g2d.setStroke(bs);
        g2d.setColor(Color.red);
        g2d.setClip(new Rectangle(menuBox.x, menuBox.y + 30, menuBox.width, menuBox.height - 30)); // stops from rendering outside of box

        Crafts.clear();
        int tick = 0;
        int layer = 0;
        for (int i = 0; i < num; i++) {
            Crafts.add(new Rectangle(menuBox.x + 9 + (349 * (i - (4 * layer))), (menuBox.y + eng.notches + 35 + (120 * layer)), 310, 110));
            tick++;
            if (tick == 4) {
                layer++;
                tick = 0;
            }
        }

        if (Crafts.get(Crafts.size() - 1).y + Crafts.get(Crafts.size() - 1).height + eng.notches + 10 < menuBox.y + menuBox.height + eng.scrollSens) {//stops from scrolling up past the last element
            eng.downNotching = false;
        } else {
            eng.downNotching = true;
        }

        if (Crafts.get(0).y + eng.notches + 10 > menuBox.y + 30 + eng.scrollSens) {
            eng.upNotching = false;
        } else {
            eng.upNotching = true;
        }
    }
//</editor-fold>

}
