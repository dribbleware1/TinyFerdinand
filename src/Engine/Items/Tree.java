/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Items;

import Engine.Engine.ESC;
import Engine.Items.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author DribbleWare
 */
public class Tree {

    ESC eng;

    public boolean temp = false;

    final int MAX_TIME = 120;
    final int DEFAULT_COUNT_SMALL = 5;
    final int DEFAULT_COUNT_LARGE = 8;
    final int LONG_TIME = 120;
    int timer = 0, longtimer = 0;
    public int count;
    final int ID;
    Random rand = new Random();
    private boolean pick = true, trigger = false, longtime = false, opty = false;
    private Rectangle tree, treebox, menuBox;
    private boolean popup = false;

    public Font text = new Font("TimesRoman", Font.PLAIN, 25);

    public Tree(ESC engine, Rectangle bounds, int id) {
        eng = engine;
        tree = bounds;
        ID = id;
        resize(ID);
    }

    public void update() {

        if (timer < MAX_TIME && count > 0) {
            timer++;
            pick = false;
        }
        if (timer == MAX_TIME) {
            pick = true;
        }

        if (contains(treebox) && eng.mainChar.box.intersects(recBuild())) {
            popup = true;
            if (eng.left && pick && count > 0) {
                for (int j = 0; j < eng.mainChar.inv.inven.size(); j++) {
                    if (eng.mainChar.inv.inven.get(j).id == 0) {
                        eng.mainChar.inv.inven.get(j).qnty += 1;
                        randomApple();
                        timer = 0;
                        trigger = true; 
                    }
                }
                if (!trigger) {
                    eng.mainChar.inv.inven.add(new Item(0, 1));
                    randomApple();
                    timer = 0;
                }
                if (eng.mainChar.inv.inven.isEmpty()) {
                    eng.mainChar.inv.inven.add(new Item(0, 1));
                    randomApple();
                    timer = 0;
                }
                count--;
            }
            if (eng.right && count == 0) {
                opty = true;
                menuBox = new Rectangle(tree.x + tree.width + eng.getXOff(), tree.y + eng.getYOff() - 100, 100, 150);
            }

        } else {
            popup = false;
            if (opty == true) {
                if (!menuBox.contains(loc(eng))) {
                    opty = false;
                }
            }
        }
        if (longtime && longtimer < LONG_TIME) {
            longtimer++;
        }
        if (longtimer == LONG_TIME) {
            switch (ID) {
                case 0:
                    count = DEFAULT_COUNT_SMALL;
                    break;
                case 1:
                    count = DEFAULT_COUNT_LARGE;
                    break;
            }
            longtime = false;
            longtimer = 0;
        }
    }

    public void render(Graphics g) {
        g.setFont(text);

        if (opty) {
            g.setColor(new Color(10, 10, 10, 100));
            g.fillRect(menuBox.x, menuBox.y, menuBox.width, menuBox.height);

            for (int i = 0; i < eng.mainChar.inv.inven.size(); i++) {
                if (eng.mainChar.inv.inven.get(i).id == 3) {
                    longtime = true;
                    eng.mainChar.inv.inven.get(i).id = 2;
                }
            }
        }

        if (popup) {
            if (pick) {
                g.setColor(Color.white);
                g.drawString("Press Left Mouse To Collect", 555, eng.sizeh - 60);
                g.drawString("Press Right Mouse For Options", 555, eng.sizeh - 30);
            }
            switch (ID) {
                case 0:
                    g.setColor(Color.red);
                    g.fillRect(treebox.x + eng.getXOff(), treebox.y - 100 + eng.getYOff(), timer / (MAX_TIME / 120), 15);
                    g.setColor(Color.orange);
                    g.drawRect(treebox.x + eng.getXOff(), treebox.y - 100 + eng.getYOff(), 120, 15);
                    g.setColor(Color.white);
                    g.drawString(Integer.toString(count), treebox.x + eng.getXOff() + 130, treebox.y - 83 + eng.getYOff());
                    break;
                case 1:
                    g.setColor(Color.red);
                    g.fillRect(treebox.x +eng.getXOff() + 75, treebox.y - 100 + eng.getYOff(), timer / (MAX_TIME / 120), 15);
                    g.setColor(Color.orange);
                    g.drawRect(treebox.x + eng.getXOff() + 75, treebox.y - 100 + eng.getYOff(), 120, 15);
                    g.setColor(Color.white);
                    g.drawString(Integer.toString(count), treebox.x + eng.getXOff() + 205, treebox.y - 83 +eng.getYOff());
                    break;
            }
        }
        if (eng.debug) {
            g.setColor(Color.red);
            g.drawRect(treebox.x + eng.getXOff(), treebox.y + eng.getYOff(), treebox.width, treebox.height);
            if (opty) {
                g.drawRect(menuBox.x, menuBox.y, menuBox.width, menuBox.height);
            }
        }
    }

    public void resize(int id) {
        switch (ID) {
            case 0:
                treebox = new Rectangle(tree.x - 30, tree.y - 100, tree.width + 60, tree.height + 100);
                count = DEFAULT_COUNT_SMALL;
                break;
            case 1:
                treebox = new Rectangle(tree.x - 30, tree.y - 100, tree.width + 60, tree.height + 150);
                count = DEFAULT_COUNT_LARGE;
                break;
        }
    }

    public boolean contains(Rectangle boxin) {
        boolean ret = false;

        Rectangle click = new Rectangle(boxin.x + eng.getXOff(), boxin.y + eng.getYOff(), boxin.width, boxin.height);
        if (click.contains(new Point(MouseInfo.getPointerInfo().getLocation().x - eng.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - eng.frame.getY() - Math.abs(eng.frame.getLocationOnScreen().y - eng.canvas.getLocationOnScreen().y)))) {
            ret = true;
        }
        return ret;
    }

    public Rectangle recBuild() {
        return new Rectangle(treebox.x + eng.getXOff(), treebox.y + eng.getYOff(), treebox.width, treebox.height);
    }

    public Point loc(ESC engine) {
        return new Point(MouseInfo.getPointerInfo().getLocation().x - engine.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - engine.frame.getY() - Math.abs(engine.frame.getLocationOnScreen().y - engine.canvas.getLocationOnScreen().y));
    }

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

}
