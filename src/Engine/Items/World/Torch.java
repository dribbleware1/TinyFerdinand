/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Items.World;

import Engine.Engine.ESC;
import Engine.Items.Support.Light;
import Engine.Items.Support.WorldObjects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

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

    public boolean first = true;

    int burnTicks = 0;
    int animation = 0;

    public Rectangle light;
    public Rectangle add;
    public Rectangle putOut;
    Light newLight;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    public Torch(int xi, int yi, int timer, ESC engine) {
        eng = engine;
        x = xi;
        y = yi;
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
        if (new Rectangle(0, 0, eng.sizew, eng.sizeh).intersects(x + eng.getXOff(), y + eng.getYOff(), w, h)) {
            mouseUpdate();
            if (animated) {
                if (animation == MAX_ANI - 1) {
                    animation = 0;
                }
                animation++;
            }
        }

        if (!dropped) {
            drop();
        }
        if (dropped && first) {
            for (int i = 0; i < eng.mainChar.inv.inven.size(); i++) {
                if (eng.mainChar.inv.inven.get(i).id == 15 && eng.mainChar.inv.inven.get(i).qnty > 0) {
                    Torch placeHolder = new Torch((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 120, eng);
                    placeHolder.costs = this.costs;
                    eng.world.active.obbys.add(placeHolder);
                    eng.mainChar.inv.itemRemove(15, 1);
                    break;
                }
            }
            first = false;
        }
        if (dropped && !selfTick) {
            newLight = new Light((x / 4) - 70 + (w / 8), (y / 4) - 70 + (h / 8), 70, 1f);
            eng.world.active.lights.add(newLight);
            selfTick = true;
        }

        if (dropped && actionTimer > 0) {
            burnTicks++;
        }
        if (actionTimer == 0) {
            animated = false;
            eng.world.active.lights.remove(newLight);
        }
        if (actionTimer > 0 && !animated) {
            animated = true;
        }
        if (burnTicks == 60) {
            burnTicks = 0;
            actionTimer--;
        }

        if (recBuilder(size).intersects(eng.mainChar.box)) {
            if (contains(size, true) && eng.left && mouseDelay) {
                closeAll(this);
                pop = true;
                mouseDelay = false;
                menuBox = new Rectangle(x + w, y, 420, 175);
                add = addButton(5, 70);
                light = addButton(5, 120);
                putOut = addButton(295, 70);
            }
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
//        g.fillRect(x,y,w,h);
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.draw(new Area(new Ellipse2D.Float((x + (w / 2) - 125) + eng.getXOff(), (y + (h / 2) - 125) + eng.getYOff(), 250, 250
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
            if (addCost && actionTimer > 0) {
                g.setColor(good);
                if (eng.left && mouseDelay) {
                    eng.mainChar.inv.itemRemove(6, 1);
                    actionTimer += ADD_TIME;
                    mouseDelay = false;
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
                    eng.world.active.lights.add(newLight);
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
                    eng.world.active.lights.remove(newLight);
                    mouseDelay = false;
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
}
