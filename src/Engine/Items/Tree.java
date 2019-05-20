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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author DribbleWare
 */
public class Tree extends WorldObjects {

    //<editor-fold defaultstate="collapsed" desc="delcarations">
    //final variables 
    final int MAX_TIME = 120;
    final int DEFAULT_COUNT_SMALL = 5;
    final int DEFAULT_COUNT_LARGE = 8;
    final int SCALE_FACTOR = 4;
    int GROW_TIME = 15; //in seconds not ticks

    Random rand = new Random(); //used for apples

    //growing variables
    int scaleX = 0, scaleY = 0;
    int maxScaleX = 0, maxScaleY = 0;
    //boolean grown = false;

    int textY = 0;

    int timer = 0; // for pick ups
    boolean initialized = false, finialized = false; //to set some values after being placed and after being fully grown;

    String warning = "*WARNING: This will only give you half of the \nremaining logs";

    public Rectangle collectBox = new Rectangle(0, 0, 0, 0); //holder for the collect box
    public Rectangle chopBox = new Rectangle(0, 0, 0, 0); //holder for the chop down box

    BufferedImage image;//image holder for simplifed render loops
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public Tree(ESC engine, int xi, int yi, int idi) {
        eng = engine;
        x = xi;
        y = yi;
        ID = idi;
        image = getImage();
        collis = collisBox();
        maxScaleX = image.getWidth() * SCALE_FACTOR;
        maxScaleY = image.getHeight() * SCALE_FACTOR;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    @Override
    public void update() {
        mouseUpdate();
        if (dropped && !grown) {
            grow();
        }
        if (timer < MAX_TIME && grown) {
            timer++;
        }
        if (!dropped) {
            drop();
            buffer = new Rectangle(x - 50, y - 50, 100, 100);
            size = buffer;
        }
        if (dropped && !initialized) {
            size.y -= 150;
            size.height += 100;
            eng.world.updatelist();
            if (count == -1) {
                if (ID == 0) {
                    count = DEFAULT_COUNT_SMALL;
                }
                if (ID == 1) {
                    count = DEFAULT_COUNT_LARGE;
                }
            }
            initialized = true;
        }
        if (grown && !finialized) {
            eng.world.updatelist();
            finialized = true;
        }

        if (contains(size, true) && dropped && mouseDelay && eng.left) {
            if (recBuilder(size).intersects(eng.mainChar.box)) {
                closeAll(this);
                pop = true;
                mouseDelay = false;
                menuBox = new Rectangle(x + (maxScaleX / 3), y - (maxScaleX / 2), 600, 200);
                collectBox = addButton(10, 40);
                chopBox = addButton(10, 90);
            } else {
                eng.pop("Too far away!");
                mouseDelay = false;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="render Graphics g">
    @Override
    public void render(Graphics g) {
        if (dropped) {
            if (eng.mainChar.y >= collis.y + eng.getYOff()) {
                g.drawImage(image, treeLoc(image).x + eng.getXOff(), treeLoc(image).y + eng.getYOff(), scaleX, scaleY, null);
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="priorityRender Grpahics g">
    @Override
    public void priorityRender(Graphics g) {
        if (dropped) {
            if (eng.mainChar.y < collis.y + eng.getYOff()) {
                g.drawImage(image, treeLoc(image).x + eng.getXOff(), treeLoc(image).y + eng.getYOff(), scaleX, scaleY, null);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="popUpRender Graphics g">
    @Override
    public void popUpRender(Graphics g) {
        if (!dropped) {
            g.drawImage(image, treeLoc(image).x + eng.getXOff(), treeLoc(image).y + eng.getYOff(), image.getWidth() * SCALE_FACTOR, image.getHeight() * SCALE_FACTOR, null);
            g.drawRect(buffer.x + eng.getXOff(), buffer.y + eng.getYOff(), buffer.width, buffer.height);
            if (blocked) {
                g.setColor(new Color(255, 0, 0, 100));
                g.fillRect(treeLoc(image).x + eng.getXOff(), treeLoc(image).y + eng.getYOff(), image.getWidth() * SCALE_FACTOR, image.getHeight() * SCALE_FACTOR);
            }
        }
        if (pop) {
            menuRender(g);
            menuControl();
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="menuRender Grpahics g">
    public void menuRender(Graphics g) {
        basicMenu(g);
        if (grown) {
            Graphics2D g2d = (Graphics2D) g;
            BasicStroke bs = new BasicStroke(2);
            g2d.setStroke(bs);
            //collecting bar
            g.setColor(new Color(255, 87, 51));
            g.fillRect(menuBox.x + 140 + eng.getXOff(), menuBox.y + 50 + eng.getYOff(), timer * 2, 20);
            bs = new BasicStroke(1);
            g2d.setStroke(bs);
            g.setColor(Color.white);
            g.drawRect(menuBox.x + 140 + eng.getXOff(), menuBox.y + 50 + eng.getYOff(), MAX_TIME * 2, 20);
            g.setFont(text);

            //collect button
            g.drawRect(collectBox.x + eng.getXOff(), collectBox.y + eng.getYOff(), collectBox.width, collectBox.height);
            if (contains(collectBox, true)) {
                if (timer < MAX_TIME) {
                    g.setColor(bad);
                } else {
                    g.setColor(good);
                    if (eng.left) {
                        pickup();
                    }
                }
                g.fillRect(collectBox.x + 2 + eng.getXOff(), collectBox.y + 2 + eng.getYOff(), collectBox.width - 3, collectBox.height - 3);
            }
            //chop down button
            g.setColor(Color.white);
            g.drawRect(chopBox.x + eng.getXOff(), chopBox.y + eng.getYOff(), chopBox.width, chopBox.height);
            if (contains(chopBox, true)) {
                if (timer < MAX_TIME) {
                    g.setColor(bad);
                } else {
                    g.setColor(good);
                    if (eng.left) {
                        chopDown(count / 2);
                    }
                }
                g.fillRect(chopBox.x + 2 + eng.getXOff(), chopBox.y + 2 + eng.getYOff(), chopBox.width - 3, chopBox.height - 3);
            }

            //drawing of the text
            g.setColor(Color.white);
            g.drawString("Collect", collectBox.x + eng.getXOff() + 10, collectBox.y + +eng.getYOff() + text.getSize() + 5);
            g.drawString("Logs Remaining: " + Integer.toString(count), collectBox.x + eng.getXOff() + 25 + collectBox.width, menuBox.y + eng.getYOff() + 100);
            g.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 20));
            g.drawString("Chop Down", chopBox.x + eng.getXOff() + 5, chopBox.y + text.getSize() + 5 + eng.getYOff());
            g.setFont(text3);
            textY = 0;
            g.setColor(Color.red);
            for (String warning : warning.split("\n")) {
                g.drawString(warning, chopBox.x + eng.getXOff() + 5, chopBox.y + eng.getYOff() + chopBox.height + (textY += 20));
            }
        } else {
            g.setColor(Color.white);
            g.setFont(text);
            g.drawString("Growing", menuBox.x + eng.getXOff() + 25, menuBox.y + eng.getYOff() + 80);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grow">
    public void grow() {
        actionTimer++;
        if (actionTimer % 10 == 0) {
            if (scaleY < maxScaleY) {
                scaleY += (maxScaleY / GROW_TIME) / 6;
            }
            if (scaleX < maxScaleX) {
                scaleX += (maxScaleX / GROW_TIME) / 6;
            }
            if (scaleX >= maxScaleX && scaleY >= maxScaleY) {
                grown = true;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="collisBox">
    @Override
    public Rectangle collisBox() {
        if (ID == 0) {
            collis = new Rectangle(x - 30, y - 55, 60, 20);
            name = "Small Tree";
        }
        if (ID == 1) {
            collis = new Rectangle(x - 155, y - 150, 312, 100);
            name = "BigTree";
        }
        return collis;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getImage">
    public BufferedImage getImage() {
        BufferedImage ret = null;
        if (ID == 0) {
            ret = eng.assetLdr.smallT;
            GROW_TIME = 15;
        }
        if (ID == 1) {
            ret = eng.assetLdr.bigT;
            GROW_TIME = 25;
        }
        return ret;
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

    //<editor-fold defaultstate="collapsed" desc="Pickup">
    private void pickup() {
        eng.mainChar.inv.itemReplace(0, -1, 1);
        randomApple();
        timer = 0;
        count--;
        if (count == 0) {
            eng.mainChar.inv.itemReplace(5, -1, new Random().nextInt(2) + 1);
            eng.world.hubRoom.obbys.remove(this);
            eng.world.updatelist();

        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="chopDown">
    private void chopDown(int qnt) {
        eng.mainChar.inv.itemReplace(0, -1, qnt);
        randomApple();
        eng.mainChar.inv.itemReplace(5, -1, new Random().nextInt(2) + 1);
        eng.world.hubRoom.obbys.remove(this);
        eng.world.updatelist();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loading">
    public void loading() {
        scaleY += (maxScaleY / GROW_TIME) * (actionTimer / 60);
        scaleX += (maxScaleX / GROW_TIME) * (actionTimer / 60);
        if (actionTimer == 120) {
            grown = true;
        }
        size = new Rectangle(x - 50, y - 50, 100, 100);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tree location to keep them centered around the bottom so growing is less trippy">
    public Point treeLoc(BufferedImage img) {
        Point ret = new Point(0, 0);
        if (!dropped) {
            ret = new Point(x - ((img.getWidth() * SCALE_FACTOR) / 2), y - (img.getHeight() * SCALE_FACTOR));
        } else {
            ret = new Point(x - (scaleX / 2), y - scaleY);
        }
        return ret;
    }
    //</editor-fold>
}
