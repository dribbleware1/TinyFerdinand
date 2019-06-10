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
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class Fence extends WorldObjects {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    private int scale = 4;
    private int snapSize = 9;
    public boolean first = true;

    private Rectangle pickUp = new Rectangle(0, 0, 0, 0);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Fence int xi yi part ESC enging">
    public Fence(int xi, int yi, int part, ESC engine, boolean ch) {
        eng = engine;
        x = xi;
        y = yi;
        ID = part;
        changeable = ch;
        change();
        w = eng.assetLdr.fenceParts.get(ID).getWidth() * scale;
        h = eng.assetLdr.fenceParts.get(ID).getHeight() * scale;
        size = new Rectangle(x, y, w, h);
        snapBox = new Rectangle(x - snapSize, y - snapSize, w + (snapSize * 2), h + (snapSize * 2));
        collis = collisBox();
        name = "Fence";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    public void update() {
        mouseUpdate();
        rotate();

        if (!dropped) {
            drop();
            snap();
        }
        if (dropped && first) {
            size = new Rectangle(x, y, w, h);
            collis = collisBox();
            snapBox = new Rectangle(x - snapSize, y - snapSize, w + (snapSize * 2), h + (snapSize * 2));
            eng.world.updatelist();
            boolean doit = false;
            for (int i = 0; i < eng.mainChar.inv.inven.size(); i++) {
                if (eng.mainChar.inv.inven.get(i).id == 14 && !changeable) {
                    eng.mainChar.inv.pops = i;
                    doit = true;
                    break;
                } else if (eng.mainChar.inv.inven.get(i).id == 14 && changeable) {
                    Fence placeHolderFence = new Fence((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 0, eng, true);
                    placeHolderFence.costs = this.costs;
                    eng.world.active.obbys.add(placeHolderFence);
                    eng.mainChar.inv.itemRemove(14, 1);
                    break;
                }
            }
            if (doit) {
                eng.inventory = true;
                eng.mainChar.inv.pop = true;
                eng.mainChar.inv.page = 2;
            }
            first = false;
        }
        if (contains(size, true) && eng.left && mouseDelay && !eng.inventory) {
            if (recBuilder(size).intersects(eng.mainChar.box)) {
                closeAll(this);
                pop = true;
                mouseDelay = false;
                menuBox = new Rectangle(x + 75, y + 50, 140, 90);
                pickUp = addButton(10, 40);
            }
        }
        if (!recBuilder(size).intersects(eng.mainChar.box)) {
            pop = false;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="render Graphics g">
    public void render(Graphics g) {
        if (eng.mainChar.y >= collis.y + eng.getYOff() && dropped) {
            g.drawImage(eng.assetLdr.fenceParts.get(ID), x + eng.getXOff(), y + eng.getYOff(), eng.assetLdr.fenceParts.get(ID).getWidth() * scale, eng.assetLdr.fenceParts.get(ID).getHeight() * scale, null);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="priorityRender Graphics g">
    public void priorityRender(Graphics g) {
        if (eng.mainChar.y < collis.y + eng.getYOff() && dropped) {
            g.drawImage(eng.assetLdr.fenceParts.get(ID), x + eng.getXOff(), y + eng.getYOff(), eng.assetLdr.fenceParts.get(ID).getWidth() * scale, eng.assetLdr.fenceParts.get(ID).getHeight() * scale, null);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="popUpRender Graphics g">
    public void popUpRender(Graphics g) {
        if (!dropped) {
            g.drawImage(eng.assetLdr.fenceParts.get(ID), x + eng.getXOff(), y + eng.getYOff(), eng.assetLdr.fenceParts.get(ID).getWidth() * scale, eng.assetLdr.fenceParts.get(ID).getHeight() * scale, null);
        }
        if (blocked) {
            g.setColor(new Color(255, 0, 0, 100));
            g.fillRect(x + eng.getXOff(), y + eng.getYOff(), w, h);
        }
        if (pop) {
            menuRender(g);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="menuRender Grphics g">    
    public void menuRender(Graphics g) {
        basicMenu(g);

        g.setColor(Color.white);
        g.drawRect(pickUp.x + eng.getXOff(), pickUp.y + eng.getYOff(), pickUp.width, pickUp.height);
        if (contains(pickUp, true)) {
            if (eng.left && mouseDelay) {
                mouseDelay = false;
                eng.world.active.obbys.remove(this);
                eng.world.updatelist();
                eng.mainChar.inv.itemAdd(14, 1);
            }
            g.setColor(good);
            g.fillRect(pickUp.x + eng.getXOff(), pickUp.y + eng.getYOff(), pickUp.width, pickUp.height);
        } else {
            //do nothing
        }
        g.setColor(Color.white);
        g.drawString("Pickup", pickUp.x + 7 + eng.getXOff(), pickUp.y + 32 + eng.getYOff());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="collisBox">
    public Rectangle collisBox() {
        Rectangle ret = new Rectangle(0, 0, 0, 0);
        switch (ID) {
            case 0:
                ret = new Rectangle(x + (w / 2) - 17, y + (h / 8) * 3, (w / 2) + 17, h / 8);
                break;
            case 1:
                ret = new Rectangle(x, y + (h / 8) * 3, w, h / 8);
                break;
            case 2:
                ret = new Rectangle(x, y + (h / 8) * 3, (w / 2) + 13, h / 8);
                break;
            case 3:
                ret = new Rectangle(x + (w / 2) - (w / 12) - 3, y, (w / 6), h - 75);
                break;
            case 4:
                ret = new Rectangle(x + (w / 2) - (w / 12) - 3, y, (w / 6), h);
                break;
            case 5:
                ret = new Rectangle(x + (w / 2) - (w / 12) - 3, y + 75, (w / 6), h - 75);
                break;
            case 6:
                ret = new Rectangle(x + (w / 2) - (w / 12) - 3, y + 65, (w / 6), h - 65);
                if (dropped) {
                    eng.world.active.objects.add(new Rectangle(x + (w / 2) - 13, y + (h / 8) * 3, (w / 2) + 13, h / 8));
                }
                break;
            case 7:
                ret = new Rectangle(x + (w / 2) - (w / 12) - 3, y + 65, (w / 6), h - 65);
                if (dropped) {
                    eng.world.active.objects.add(new Rectangle(x, y + (h / 8) * 3, w, h / 8));

                }
                break;
            case 8:
                ret = new Rectangle(x + (w / 2) - (w / 12) - 3, y + 65, (w / 6), h - 65);
                if (dropped) {
                    eng.world.active.objects.add(new Rectangle(x, y + (h / 8) * 3, (w / 2) + 8, (h / 8)));
                }
                break;
            case 9:
                ret = new Rectangle(x + (w / 2) - 13, y + (h / 8) * 3, (w / 2) + 13, h / 8);
                if (dropped) {
                    eng.world.active.objects.add(new Rectangle(x + (w / 2) - (w / 12) - 3, y, (w / 6), h));
                }
                break;
            case 10:
                ret = new Rectangle(x, y + (h / 8) * 3, w, h / 8);
                if (dropped) {
                    eng.world.active.objects.add(new Rectangle(x + (w / 2) - (w / 12) - 3, y, (w / 6), h));
                }
                break;
            case 11:
                ret = new Rectangle(x, y + (h / 8) * 3, (w / 2) + 8, (h / 8));
                if (dropped) {
                    eng.world.active.objects.add(new Rectangle(x + (w / 2) - (w / 12) - 3, y, (w / 6), h));
                }
                break;
            case 12:
                ret = new Rectangle(x + (w / 2) - 13, y + (h / 8) * 3, (w / 2) + 13, h / 8);
                if (dropped) {
                    eng.world.active.objects.add(new Rectangle(x + (w / 2) - (w / 12) - 3, y, (w / 6), h - 80));
                }
                break;
            case 13:
                ret = new Rectangle(x, y + (h / 8) * 3, w, h / 8);
                if (dropped) {
                    eng.world.active.objects.add(new Rectangle(x + (w / 2) - (w / 12) - 3, y, (w / 6), h - 80));
                }
                break;
            case 14:
                ret = new Rectangle(x, y + (h / 8) * 3, (w / 2) + 8, h / 8);
                if (dropped) {
                    eng.world.active.objects.add(new Rectangle(x + (w / 2) - (w / 12) - 3, y, (w / 6), h - 80));
                }
                break;
            case 15:
                ret = new Rectangle(x + (w / 2) - (w / 12) - 3, y + ((h / 8) * 3), (w / 6), h / 8);
                break;

        }
        return ret;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="snap">
    public void snap() {
        snapBox = new Rectangle(x - snapSize, y - snapSize, w + (snapSize * 2), h + (snapSize * 2));
        List<WorldObjects> obbys = eng.world.active.obbys;
        for (int i = 0; i < obbys.size(); i++) {
            if (obbys.get(i) instanceof Fence && obbys.get(i) != this) {
                if (new Rectangle(obbys.get(i).snapBox.x + eng.getXOff(), obbys.get(i).snapBox.y + eng.getYOff(), obbys.get(i).snapBox.width, obbys.get(i).snapBox.height).intersects(
                        new Rectangle(snapBox.x + eng.getXOff(), snapBox.y + eng.getYOff(), snapBox.width, snapBox.height))) {
                    if (snapBox.y > obbys.get(i).snapBox.y - 10 && snapBox.y + snapBox.height < obbys.get(i).snapBox.height + obbys.get(i).snapBox.y + 10) {
                        if (obbys.get(i).x < x) { // left
                            x = obbys.get(i).x + obbys.get(i).w;
                            y = obbys.get(i).y;
                            snapped = true;
                            break;
                        } else {//right
                            x = obbys.get(i).x - w;
                            y = obbys.get(i).y;
                            snapped = true;
                            break;
                        }
                    } else if (snapBox.x > obbys.get(i).snapBox.x - 10 && snapBox.x + snapBox.width < obbys.get(i).snapBox.width + obbys.get(i).snapBox.x + 10) {
                        if (obbys.get(i).y > y) {//above
                            x = obbys.get(i).x;
                            y = obbys.get(i).y - obbys.get(i).h;
                            snapped = true;
                            break;
                        } else {//below
                            x = obbys.get(i).x;
                            y = obbys.get(i).y + obbys.get(i).h;
                            snapped = true;
                            break;
                        }
                    }
                }
            }
            snapped = false;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="rotate">
    public void rotate() {
        left = false;
        right = false;
        up = false;
        down = false;

        snapBox = new Rectangle(x - snapSize, y - snapSize, w + (snapSize * 2), h + (snapSize * 2));
        List<WorldObjects> obbys = eng.world.active.obbys;
        for (int i = 0; i < obbys.size(); i++) {
            if (obbys.get(i) instanceof Fence && obbys.get(i) != this) {
                if (new Rectangle(obbys.get(i).snapBox.x + eng.getXOff(), obbys.get(i).snapBox.y + eng.getYOff(), obbys.get(i).snapBox.width, obbys.get(i).snapBox.height).intersects(
                        new Rectangle(snapBox.x + eng.getXOff(), snapBox.y + eng.getYOff(), snapBox.width, snapBox.height))) {
                    if (snapBox.y > obbys.get(i).snapBox.y - 10 && snapBox.y + snapBox.height < obbys.get(i).snapBox.height + obbys.get(i).snapBox.y + 10) {
                        if (obbys.get(i).x < x) { // left
                            if (cr && obbys.get(i).cl) {
                                left = true;
                                obbys.get(i).right = true;
                            }
                        } else {//right
                            if (cl && obbys.get(i).cr) {
                                right = true;
                                obbys.get(i).left = true;
                            }
                        }
                    } else if (snapBox.x > obbys.get(i).snapBox.x - 10 && snapBox.x + snapBox.width < obbys.get(i).snapBox.width + obbys.get(i).snapBox.x + 10) {
                        if (obbys.get(i).y > y) {//above
                            if (cu && obbys.get(i).cd) {
                                obbys.get(i).up = true;
                                down = true;
                            }
                        } else {//below
                            if (cd && obbys.get(i).cu) {
                                obbys.get(i).down = true;
                                up = true;
                            }
                        }
                    }
                    obbys.get(i).part();
                    part();
                }
            }
        }
        part();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="part">
    @Override
    public void part() {
        int idHold = ID;
        if (changeable) {
            if (!left && right && !up && !down) {
                ID = 0;
            }
            if (left && right && !up && !down) {
                ID = 1;
            }
            if (left && !right && !up && !down) {
                ID = 2;
            }
            if (!left && !right && up && !down) {
                ID = 3;
            }
            if (!left && !right && up && down) {
                ID = 4;
            }
            if (!left && !right && !up && down) {
                ID = 5;
            }
            if (!left && right && !up && down) {
                ID = 6;
            }
            if (left && right && !up && down) {
                ID = 7;
            }
            if (left && !right && !up && down) {
                ID = 8;
            }
            if (!left && right && up && down) {
                ID = 9;
            }
            if (left && right && up && down) {
                ID = 10;
            }
            if (left && !right && up && down) {
                ID = 11;
            }
            if (!left && right && up && !down) {
                ID = 12;
            }
            if (left && right && up && !down) {
                ID = 13;
            }
            if (left && !right && up && !down) {
                ID = 14;
            }
            if (!left && !right && !up && !down) {
                ID = 15;
            }
        }
        if (ID != idHold) {
            eng.world.updatelist();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="change">
    public void change() {
        if (!changeable) {
            if (ID == 0) {
                cl = true;
                cr = false;
                cu = false;
                cd = false;
            }
            if (ID == 2) {
                cl = false;
                cr = true;
                cu = false;
                cd = false;
            }
            if (ID == 3) {
                cl = false;
                cr = false;
                cu = false;
                cd = true;
            }
            if (ID == 5) {
                cl = false;
                cr = false;
                cu = true;
                cd = false;
            }
        }
    }
    //</editor-fold>
}
