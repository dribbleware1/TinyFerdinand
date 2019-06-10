/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Items;

import Engine.Engine.ESC;
import Engine.Map.WorldObjects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.Random;

/**
 *
 * @author DribbleWare
 */
public class Torch extends WorldObjects {

    //<editor-fold defaultstate="collapsed" desc="declarations">
    final int SCALE = 2;
    final int FRAMES = 3;
    final int MAX_ANI = 30;
    final int ADD_TIME = 120;
    private final int MAX_TORCH = 15;
    private final int MAX_LIGHT = 300;
    private final int LIGHT_SIZE_MODIFIER = 1;
    private final int LIGHT_SPEED = 1;

    int lightSizex = new Random().nextInt(5);
    int lightSizey = new Random().nextInt(5) + 10;
    boolean lightx = true, lighty = true;

    int burnTicks = 0;

    int animation = 0;

    public Rectangle light;
    public Rectangle add;
    public Rectangle putOut;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    public Torch(int xi, int yi, int timer, ESC engine) {
        eng = engine;
        x = xi;
        y = yi;

        System.out.println(lightSizex + "      " + lightSizey);
        w = eng.assetLdr.torches.get(0).getWidth() * SCALE;
        h = eng.assetLdr.torches.get(0).getHeight() * SCALE;

        actionTimer = timer;

        size = new Rectangle(x, y, w, h);
        collis = collisBox();

        name = "Torch";

        hasLight = true;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    @Override
    public void update() {
        mouseUpdate();
        if (!dropped) {
            drop();
        }
        if (dropped && actionTimer > 0) {
            burnTicks++;
        }
        if (actionTimer == 0) {
            animated = false;
        }
        if (burnTicks == 60) {
            burnTicks = 0;
            actionTimer--;
        }

        if (animated) {
            if (animation == MAX_ANI - 1) {
                animation = 0;
            }
            animation++;
        }
        lightSizing();

        if (contains(size, true) && eng.left && mouseDelay && recBuilder(size).intersects(eng.mainChar.box)) {
            closeAll(this);
            pop = true;
            mouseDelay = false;
            menuBox = new Rectangle(x + w, y, 420, 175);
            add = addButton(5, 70);
            light = addButton(5, 120);
            putOut = addButton(295, 70);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="render Grpahics g">
    @Override
    public void render(Graphics g) {
        if (eng.mainChar.y >= y + eng.getYOff() && dropped) {
            if (animated) {
                g.drawImage(eng.assetLdr.torches.get((animation / (MAX_ANI / 3)) + 1), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
            } else {
                g.drawImage(eng.assetLdr.torches.get(0), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="priorityRender Grpahics g">
    @Override
    public void priorityRender(Graphics g) {
        if (eng.mainChar.y < collisBox().y + eng.getYOff() && dropped) {
            if (animated) {
                g.drawImage(eng.assetLdr.torches.get((animation / (MAX_ANI / 3)) + 1), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
            } else {
                g.drawImage(eng.assetLdr.torches.get(0), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
            }
        }
        if (!dropped) {
            g.drawImage(eng.assetLdr.torches.get(0), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
            if (blocked) {
                g.setColor(bad);
                g.fillRect(x + eng.getXOff(), y + eng.getYOff(), w, h);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="popUpRender Graphics g">
    @Override
    public void popUpRender(Graphics g) {
        if (pop) {
            menuRender(g);
            menuControl();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="menuRender Graphics g">
    public void menuRender(Graphics g) {
        basicMenu(g);
        pickMenu(g, true);

        boolean lightCost = false, addCost = false;

        for (int i = 0; i < eng.mainChar.inv.inven.size(); i++) {
            if (eng.mainChar.inv.inven.get(i).id == 6) {
                if (eng.mainChar.inv.inven.get(i).qnty >= 1) {
                    addCost = true;
                }
            }
            if (eng.mainChar.inv.inven.get(i).id == 9) {
                if (eng.mainChar.inv.inven.get(i).qnty >= 2) {
                    lightCost = true;
                }
            }
        }

        g.setColor(Color.white);
        g.setFont(text);
        g.drawString("Burn Time: " + String.format("%02d", actionTimer / 60) + " Minutes " + String.format("%02d", actionTimer - (actionTimer / 60) * 60) + " Seconds", menuBox.x + 5 + eng.getXOff(), menuBox.y + eng.getYOff() + (text.getSize() * 2) + 5);

        g.setColor(Color.white);
        g.drawRect(add.x + eng.getXOff(), add.y + eng.getYOff(), add.width, add.height);
        if (contains(add, true)) {
            if (addCost) {
                g.setColor(good);
                if (eng.left && mouseDelay) {
                    eng.mainChar.inv.itemRemove(6, 1);
                    actionTimer += ADD_TIME;
                }
            } else {
                g.setColor(bad);
            }
            g.fillRect(add.x + eng.getXOff() + 2, add.y + eng.getYOff() + 2, add.width - 4, add.height - 4);
        }

        g.setColor(Color.white);
        g.drawRect(light.x + eng.getXOff(), light.y + eng.getYOff(), light.width, light.height);
        if (contains(light, true)) {
            if (lightCost && actionTimer <= 0) {
                g.setColor(good);
                if (eng.left && mouseDelay) {
                    eng.mainChar.inv.itemRemove(6, 1);
                    eng.mainChar.inv.itemRemove(9, 2);
                    actionTimer += ADD_TIME;
                    mouseDelay = false;
                }
            } else {
                g.setColor(bad);
            }
            g.fillRect(light.x + eng.getXOff() + 2, light.y + eng.getYOff() + 2, light.width - 4, light.height - 4);
        }

        g.setColor(Color.white);
        g.drawRect(putOut.x + eng.getXOff(), putOut.y + eng.getYOff(), putOut.width, putOut.height);
        if (contains(putOut, true)) {
            if (actionTimer > 0) {
                g.setColor(good);
                if (eng.left && mouseDelay) {
                    actionTimer = 0;
                }
            } else {
                g.setColor(bad);
            }
            g.fillRect(putOut.x + eng.getXOff() + 2, putOut.y + eng.getYOff() + 2, putOut.width - 4, putOut.height - 4);
        }

        g.setColor(Color.white);
        g.drawString("Add Stick", add.x + 2 + eng.getXOff(), add.y + eng.getYOff() + text.getSize() + 5);
        g.drawString("Cost: 1 Stick", add.x + 10 + eng.getXOff() + add.width, add.y + eng.getYOff() + text.getSize() + 5);
        g.drawString("Relight", light.x + 15 + eng.getXOff(), light.y + eng.getYOff() + text.getSize() + 5);
        g.drawString("Cost: 1 Stick, 2 Leaves", light.x + 10 + eng.getXOff() + light.width, light.y + eng.getYOff() + text.getSize() + 5);
        g.drawString("Putout", putOut.x + 15 + eng.getXOff(), putOut.y + eng.getYOff() + text.getSize() + 5);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="collisBox">
    public Rectangle collisBox() {
        return new Rectangle(x + (w / 2) - 11, y + 30, 20, 12);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pickUp">
    public void pickUp() {
        if (actionTimer <= 0) {
            eng.world.active.obbys.remove(this);
            eng.mainChar.inv.itemAdd(6, 1);
            eng.world.updatelist();
        } else {
            eng.pop("Put out first", y);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="fireSizing">
    void lightSizing() {
        if (animated) {
            if (lightx) {
                lightSizex += LIGHT_SPEED;
            } else {
                lightSizex -= LIGHT_SPEED;
            }

            if (lighty) {
                lightSizey += LIGHT_SPEED;
            } else {
                lightSizey -= LIGHT_SPEED;
            }

            if (lightSizex <= 0) {
                lightx = true;
            }
            if (lightSizex >= MAX_TORCH) {
                lightx = false;
            }
            if (lightSizey <= 0) {
                lighty = true;
            }
            if (lightSizey >= MAX_TORCH) {
                lighty = false;
            }

        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getLight">
    @Override
    public Ellipse2D getLight() {
        return new Ellipse2D.Double(x + eng.getXOff() - ((lightSizex / LIGHT_SIZE_MODIFIER) / 2) - (MAX_LIGHT / 2) + (w / 2),
                y + eng.getYOff() - (MAX_LIGHT / 2) + ((h / 3) * 2) - ((lightSizey / LIGHT_SIZE_MODIFIER) / 2),
                MAX_LIGHT + (lightSizex / LIGHT_SIZE_MODIFIER),
                MAX_LIGHT + (lightSizey / LIGHT_SIZE_MODIFIER));

    }
    //</editor-fold>

}
