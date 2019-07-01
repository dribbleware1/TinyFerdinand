/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Items.World;

import Engine.Engine.ESC;
import Engine.Items.Support.Light;
import Engine.Items.Support.WorldObjects;
import Engine.Player.Crafting;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DribbleWare
 */
public class CampFire extends WorldObjects {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    private final int MAX_TIME = 60 * 99; //60 seconds in a minute * 99 minuts to keep it as a 2 digit minute second display
    private final int ADD = 120; //seconds to add per log (seems too short at 2 mins but we'll see
    private final int LIGHT_COST = 3; //cost to relight a fire thats burnt out

    int burnTicks = 0;
    int index = -1;

    public int sX = 0;
    public int sY = 0;

    int placeDis = 150;
    boolean first = true;

    int aSpeed = 8;
    int aFrames;
    int ani = 0;

    Crafting crafter;

    int scale = 3;
    private List<BufferedImage> campFire = new ArrayList<>();
    private Rectangle addBox;
    private Rectangle lightBox;
    private Rectangle putOut;
    Light newLight;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor int x, int y, ESC engine">
    public CampFire(int xi, int yi, int time, ESC engi) {
        eng = engi;
        campFire = eng.assetLdr.campFire;
        x = xi;
        y = yi;
        sX = xi;
        sY = yi;
        actionTimer = time;
        w = campFire.get(0).getWidth() * scale;
        h = campFire.get(0).getHeight() * scale;
        aFrames = campFire.size() - 1;
        size = new Rectangle(x, y, w, h);
        collis = collisBox();
        mouseDelay = false;
        name = "CampFire";
        crafter = eng.mainChar.inv.crafter;
        hasLight = true;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Update">
    @Override
    public void update() {
        mouseUpdate();
        if (dropped && actionTimer > 0) {
            burnTicks++;
        }
        if (burnTicks == 60) {
            burnTicks = 0;
            actionTimer--;
        }
        if (actionTimer == 0) {
            animated = false;
            eng.world.active.lights.remove(newLight);
        }
        if (actionTimer > 0 && !animated) {
            animated = true;
        }

        if (!dropped) {
            drop();
        }

        if (dropped && first) {
            newLight = new Light((x / 4) - 150 + (w / 8), (y / 4) - 150 + (h / 8), 150, 1f);
            eng.world.active.lights.add(newLight);
            first = false;
        }

        if (ani == (aSpeed * aFrames) - 1) {
            ani = 0;
        } else {
            ani++;
        }

        if (contains(size, true) && recBuilder(size).intersects(eng.mainChar.box) && eng.left && mouseDelay) {
            closeAll(this);
            pop = true;
            page = 1;
            mouseDelay = false;
            menuBox = new Rectangle(x + w, y, 450, 250);
            addBox = addButton(5, 70);
            lightBox = addButton(5, 125);
            putOut = addButton(5, 180);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Render">
    @Override
    public void render(Graphics g) {
        if (eng.mainChar.y >= y + eng.getYOff() && dropped) {
            if (animated) {
                switch (Math.round(ani / aSpeed)) {
                    case 0:
                        g.drawImage(campFire.get(0), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
                        break;
                    case 1:
                        g.drawImage(campFire.get(1), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
                        break;
                    case 2:
                        g.drawImage(campFire.get(2), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
                        break;
                    case 3:
                        g.drawImage(campFire.get(3), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
                        break;
                    case 4:
                        g.drawImage(campFire.get(4), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
                        break;
                }
            } else {
                g.drawImage(campFire.get(campFire.size() - 1), x + eng.getXOff(), y + eng.getYOff() + 32 * scale, 32 * scale, 32 * scale, null);
            }
        }

        if (eng.debug) {
            g.setColor(Color.red);
            g.drawRect(size.x + eng.getXOff(), size.y + eng.getYOff(), size.width, size.height);
            g.drawRect(collis.x + eng.getXOff(), collis.y + eng.getYOff(), collis.width, collis.height);
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="priorityRender Grpahics g">
    @Override
    public void priorityRender(Graphics g) {
        if (eng.mainChar.y < collisBox().y + eng.getYOff() && dropped) {
            if (animated && actionTimer > 0) {
                switch (Math.round(ani / aSpeed)) {
                    case 0:
                        g.drawImage(campFire.get(0), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
                        break;
                    case 1:
                        g.drawImage(campFire.get(1), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
                        break;
                    case 2:
                        g.drawImage(campFire.get(2), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
                        break;
                    case 3:
                        g.drawImage(campFire.get(3), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
                        break;
                    case 4:
                        g.drawImage(campFire.get(4), x + eng.getXOff(), y + eng.getYOff(), w, h, null);
                        break;
                }
            } else {
                g.drawImage(campFire.get(campFire.size() - 1), x + eng.getXOff(), y + eng.getYOff() + 32 * scale, 32 * scale, 32 * scale, null);
            }
        } else if (!dropped) {
            g.drawImage(campFire.get(campFire.size() - 1), x + eng.getXOff(), y + eng.getYOff() + 32 * scale, 32 * scale, 32 * scale, null);
            g.drawRect(collisBox().x + eng.getXOff(), collisBox().y + eng.getYOff(), collisBox().width, collisBox().height);
        }
        if (blocked) {
            g.setColor(bad);
            g.fillRect(x + eng.getXOff(), y + eng.getYOff() + h / 2 + 10, w, h / 2 - 10);
            g.setColor(Color.red);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="popUpRender Graphics g">
    @Override
    public void popUpRender(Graphics g) {
        if (pop) {
            crafter.update();
            menuRender(g);
            menuControl();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="menuRender Graphics g">
    public void menuRender(Graphics g) {
        basicMenu(g);
        menuBarButtons(g);

        for (int i = 0; i < eng.mainChar.inv.inven.size(); i++) { //get index of logs in inventory will make this a one time call everytime the menu opens
            if (eng.mainChar.inv.inven.get(i).id == 0) {
                index = i;
                break;
            }
        }
        if (page == 1) {
            g.setColor(Color.white);
            g.setFont(text);
            g.drawString("Burn Time: " + String.format("%02d", actionTimer / 60) + " Minutes " + String.format("%02d", actionTimer - (actionTimer / 60) * 60) + " Seconds", menuBox.x + 5 + eng.getXOff(), menuBox.y + eng.getYOff() + (text.getSize() * 2) + 5);
            g.drawRect(addBox.x + eng.getXOff(), addBox.y + eng.getYOff(), addBox.width, addBox.height);
            g.drawRect(lightBox.x + eng.getXOff(), lightBox.y + eng.getYOff(), lightBox.width, lightBox.height);
            g.drawRect(putOut.x + eng.getXOff(), putOut.y + eng.getYOff(), putOut.width, putOut.height);
            //add log button
            if (contains(addBox, true)) {
                if (index < 0) {
                    g.setColor(bad);
                } else if ((actionTimer + ADD) < MAX_TIME && eng.mainChar.inv.inven.get(index).qnty >= 1 && actionTimer > 0) {
                    if (eng.left && mouseDelay) {
                        actionTimer += ADD;
                        eng.mainChar.inv.itemRemove(0, 1);
                        mouseDelay = false;
                    }
                    g.setColor(good);
                } else {
                    g.setColor(bad);
                }
                g.fillRect(addBox.x + 2 + eng.getXOff(), addBox.y + 2 + eng.getYOff(), addBox.width - 4, addBox.height - 4);
            }
            //light fire button
            if (contains(lightBox, true)) {
                if (index < 0) {
                    g.setColor(bad);
                } else if (actionTimer == 0 && eng.mainChar.inv.inven.get(index).qnty >= LIGHT_COST) {
                    if (eng.left && mouseDelay) {
                        actionTimer += ADD;
                        eng.mainChar.inv.itemRemove(0, LIGHT_COST);
                        mouseDelay = false;
                        eng.world.active.lights.add(newLight);
                    }
                    g.setColor(good);
                } else {
                    g.setColor(bad);
                }
                g.fillRect(lightBox.x + 2 + eng.getXOff(), lightBox.y + 2 + eng.getYOff(), lightBox.width - 4, lightBox.height - 4);
            }
            //put out fire button
            if (contains(putOut, true)) {
                g.setColor(good);
                g.fillRect(putOut.x + eng.getXOff() + 2, putOut.y + eng.getYOff() + 2, putOut.width - 4, putOut.height - 4);
                if (eng.left && mouseDelay) {
                    actionTimer = 0;
                    mouseDelay = false;
                    eng.world.active.lights.remove(newLight);
                }
            }

            g.setColor(Color.white);
            //draw add log info
            g.drawString("Add Log", addBox.x + 7 + eng.getXOff(), addBox.y + eng.getYOff() + text.getSize() + 5);
            g.drawString("Cost: 1 Log", addBox.x + 10 + eng.getXOff() + addBox.width, addBox.y + eng.getYOff() + text.getSize() + 5);
            //draw light fire info
            g.drawString("Light Fire", lightBox.x + 3 + eng.getXOff(), lightBox.y + eng.getYOff() + text.getSize() + 5);
            g.drawString("Cost: " + LIGHT_COST + " Logs", lightBox.x + 10 + eng.getXOff() + lightBox.width, lightBox.y + eng.getYOff() + text.getSize() + 5);
            //draw put out info
            g.drawString("Put Out", putOut.x + 13 + eng.getXOff(), putOut.y + eng.getYOff() + text.getSize() + 5);
            //draw warnings
            g.setColor(Color.red);
            g.setFont(text3);
            g.drawString("** WARNING: This will get rid of all remaining burn time **", putOut.x + eng.getXOff(), putOut.y + eng.getYOff() + text3.getSize() + 5 + putOut.height);
        }
        if (page == 2) {//crafting
            craftSetup(g, 1);
            eng.mainChar.inv.crafter.Campfire(g);
            g.setClip(0, 0, eng.sizew, eng.sizeh);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collision builder">
    public Rectangle collisBox() {
        return new Rectangle(x + 10, y + 130, 75, 15);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pickUp">
    @Override
    public void pickUp() {
        if (actionTimer <= 0) {
            eng.world.active.obbys.remove(this);
            eng.mainChar.inv.itemAdd(0, 2);
            eng.world.updatelist();
        } else {
            eng.pop("Put out first", y);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftSetup Graphics g int num">
    void craftSetup(Graphics g, int num) {
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(3);
        g2d.setStroke(bs);
        g2d.setColor(Color.red);
        g2d.setClip(new Rectangle(menuBox.x + eng.getXOff(), menuBox.y + 30 + eng.getYOff(), menuBox.width, menuBox.height - 30)); // stops from rendering outside of box

        Crafts.clear();
        for (int i = 0; i < num; i++) {
            Crafts.add(new Rectangle(menuBox.x + 70 + eng.getXOff(), (menuBox.y + eng.notches + 35 + (120 * i)) + eng.getYOff(), 310, 110));
        }

        if (Crafts.get(Crafts.size() - 1).y + Crafts.get(Crafts.size() - 1).height + eng.notches + 10 < menuBox.y + menuBox.height + eng.scrollSens + eng.getYOff()) {//stops from scrolling up past the last element
            eng.downNotching = false;
        } else {
            eng.downNotching = true;
        }

        if (Crafts.get(0).y + eng.notches + 10 > menuBox.y + 30 + eng.scrollSens + eng.getYOff()) {
            eng.upNotching = false;
        } else {
            eng.upNotching = true;
        }

    }
//</editor-fold>
}
