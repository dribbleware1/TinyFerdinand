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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DribbleWare
 */
public class CampFire extends WorldObjects {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    private final int MAX_FIRE = 15;
    private final int FIRE_SIZE_MODIFIER = 1;
    private final int MAX_LIGHT = 500;
    private final int MAX_TIME = 60 * 99; //60 seconds in a minute * 99 minuts to keep it as a 2 digit minute second display
    private final int ADD = 120; //seconds to add per log (seems too short at 2 mins but we'll see
    private final int LIGHT_COST = 3; //cost to relight a fire thats burnt out

    int burnTicks = 0;
    int index = -1;

    public int sX = 0;
    public int sY = 0;

    int fireSizex = MAX_FIRE / 3;
    int fireSizey = 0;
    boolean firex = true, firey = true;

    int placeDis = 150;

    int aSpeed = 8;
    int aFrames;
    int ani = 0;

    int scale = 2;

    private List<BufferedImage> campFire = new ArrayList<>();
    private Rectangle addBox;
    private Rectangle lightBox;
    private Rectangle putOut;
    private Rectangle pickUp;
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
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Update">
    @Override
    public void update() {
        mouseUpdate();
        if (!animated) {
            firex = false;
            firey = false;
        }
        if (dropped && actionTimer > 0) {
            burnTicks++;
        }
        if (burnTicks == 60) {
            burnTicks = 0;
            actionTimer--;
        }
        fireSizing();

        if (!dropped) {
            drop();
        }

        if (ani == (aSpeed * aFrames) - 1) {
            ani = 0;
        } else {
            ani++;
        }

        if (contains(size, true) && recBuilder(size).intersects(eng.mainChar.box) && eng.left && mouseDelay) {
            closeAll(this);
            pop = true;
            mouseDelay = false;
            menuBox = new Rectangle(x + w, y, 450, 250);
            addBox = addButton(5, 70);
            lightBox = addButton(5, 125);
            putOut = addButton(5, 180);
            pickUp = addButton(165, 180);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Render">
    @Override
    public void render(Graphics g) {
        if (eng.mainChar.y >= y + eng.getYOff() && dropped) {
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
            g.setColor(new Color(255, 0, 0, 100));
            g.fillRect(x + eng.getXOff(), y + eng.getYOff() + h / 2 + 10, w, h / 2 - 10);
            g.setColor(Color.red);
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
        index = -1;

        for (int i = 0; i < eng.world.hubRoom.obbys.size(); i++) {
            if (eng.world.hubRoom.obbys.get(i) != this && eng.world.hubRoom.obbys.get(i).pop) {
                eng.world.hubRoom.obbys.get(i).pop = false;
            }
        }

        for (int i = 0; i < eng.mainChar.inv.inven.size(); i++) { //get index of logs in inventory will make this a one time call everytime the menu opens
            if (eng.mainChar.inv.inven.get(i).id == 0) {
                index = i;
                break;
            }
        }

        g.setColor(Color.white);
        g.setFont(text);
        g.drawString("Burn Time: " + String.format("%02d", actionTimer / 60) + " Minutes " + String.format("%02d", actionTimer - (actionTimer / 60) * 60) + " Seconds", menuBox.x + 5 + eng.getXOff(), menuBox.y + eng.getYOff() + (text.getSize() * 2) + 5);
        g.drawRect(addBox.x + eng.getXOff(), addBox.y + eng.getYOff(), addBox.width, addBox.height);
        g.drawRect(lightBox.x + eng.getXOff(), lightBox.y + eng.getYOff(), lightBox.width, lightBox.height);
        g.drawRect(putOut.x + eng.getXOff(), putOut.y + eng.getYOff(), putOut.width, putOut.height);
        g.drawRect(pickUp.x + eng.getXOff(), pickUp.y + eng.getYOff(), pickUp.width, pickUp.height);

        //
        //
        //add log button
        if (contains(addBox, true)) {
            if (index < 0) {
                g.setColor(bad);
            } else if ((actionTimer + ADD) < MAX_TIME && eng.mainChar.inv.inven.get(index).qnty >= 1 && actionTimer > 0) {
                if (eng.left && mouseDelay) {
                    actionTimer += ADD;
                    eng.mainChar.inv.inven.get(index).qnty -= 1;
                    if (eng.mainChar.inv.inven.get(index).qnty == 0) {
                        eng.mainChar.inv.inven.remove(index);
                        return;
                    }
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
                    eng.mainChar.inv.inven.get(index).qnty -= LIGHT_COST;
                    if (eng.mainChar.inv.inven.get(index).qnty == 0) {
                        eng.mainChar.inv.inven.remove(index);
                        return;
                    }
                    mouseDelay = false;
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
            }
        }
        //pick up button
        if (contains(pickUp, true)) {
            if (actionTimer == 0) {
                if (eng.left && mouseDelay) {
                    if (index == -1) {
                        eng.mainChar.inv.inven.add(new Item(0, 2));
                    } else {
                        eng.mainChar.inv.inven.get(index).qnty += 2;
                    }
                    mouseDelay = false;
                    eng.world.hubRoom.obbys.remove(this);
                    eng.world.updatelist();
                }
                g.setColor(good);
            } else {
                g.setColor(bad);
            }
            g.fillRect(pickUp.x + 2 + eng.getXOff(), pickUp.y + 2 + eng.getYOff(), pickUp.width - 4, pickUp.height - 4);
        }

        g.setColor(Color.white);
        //draw add log info
        g.drawString("Add Log", addBox.x + 7 + eng.getXOff(), addBox.y + eng.getYOff() + text.getSize() + 5);
        g.drawString("Cost: 1 Log", addBox.x + 10 + eng.getXOff() + addBox.width, addBox.y + eng.getYOff() + text.getSize() + 5);
        //draw light fire info
        g.drawString("Light Fire", lightBox.x + 3 + eng.getXOff(), lightBox.y + eng.getYOff() + text.getSize() + 5);
        g.drawString("Cost: 3 Logs", lightBox.x + 10 + eng.getXOff() + lightBox.width, lightBox.y + eng.getYOff() + text.getSize() + 5);
        //draw put out info
        g.drawString("Put Out", putOut.x + 13 + eng.getXOff(), putOut.y + eng.getYOff() + text.getSize() + 5);
        //draw pick up info
        g.drawString("Pick Up", pickUp.x + 13 + eng.getXOff(), pickUp.y + eng.getYOff() + text.getSize() + 5);
        g.drawString("+ 2 Logs", pickUp.x + 10 + eng.getXOff() + pickUp.width, pickUp.y + eng.getYOff() + text.getSize() + 5);
        //draw warnings
        g.setColor(Color.red);
        g.setFont(text3);
        g.drawString("** WARNING: This will get rid of all remaining burn time **", putOut.x + eng.getXOff(), putOut.y + eng.getYOff() + text3.getSize() + 5 + putOut.height);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collision builder">
    public Rectangle collisBox() {
        return new Rectangle(x, y + 95, 60, 10);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="fireSizing">
    void fireSizing() {
        if (animated) {
            if (firex) {
                fireSizex++;
            } else {
                fireSizex--;
            }

            if (firey) {
                fireSizey++;
            } else {
                fireSizey--;
            }

            if (fireSizex <= 0) {
                firex = true;
            }
            if (fireSizex >= MAX_FIRE) {
                firex = false;
            }
            if (fireSizey <= 0) {
                firey = true;
            }
            if (fireSizey >= MAX_FIRE) {
                firey = false;
            }

        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getLight">
    @Override
    public Ellipse2D getLight() {

        return new Ellipse2D.Double(x + eng.getXOff() - ((fireSizex / FIRE_SIZE_MODIFIER) / 2) - (MAX_LIGHT / 2) + (w / 2),
                y + eng.getYOff() - (MAX_LIGHT / 2) + ((h / 3) * 2) - ((fireSizey / FIRE_SIZE_MODIFIER) / 2),
                MAX_LIGHT + (fireSizex / FIRE_SIZE_MODIFIER),
                MAX_LIGHT + (fireSizey / FIRE_SIZE_MODIFIER));

    }
    //</editor-fold>

}
