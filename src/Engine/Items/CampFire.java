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
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class CampFire extends WorldObjects {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    ESC eng;

    public int  sX = 0;
    public int  sY = 0;

    int placeDis = 150;

    int aSpeed = 15;
    int aFrames;
    int ani = 0;
    boolean animated = true;

    public boolean dropped = false;

    boolean blocked = false;

    boolean pop = false;

    int sizew, sizeh;

    int scale = 2;

    Rectangle box;

    private List<BufferedImage> campFire = new ArrayList<>();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor int x, int y, ESC engine">
    public CampFire(int xi, int yi, ESC engi) {
        eng = engi;
        campFire = eng.assetLdr.campFire;
        x = xi;
        y = yi;
        sX = xi;
        sY = yi;
        sizew = campFire.get(0).getWidth() * scale;
        sizeh = campFire.get(0).getHeight() * scale;
        aFrames = campFire.size() - 1;
        size = collisBox();
        collis = size;
        box = new Rectangle(x, y, sizew, sizeh);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Update">
    public void update() {
        if (!dropped) {
            int Mx = MouseInfo.getPointerInfo().getLocation().x - eng.getFrameX() - eng.getXOff();
            int My = MouseInfo.getPointerInfo().getLocation().y - eng.getFrameY() - eng.getYOff();
            if (eng.mainChar.box2.contains(Mx + eng.getXOff(), My + eng.getYOff())) {
                x = Mx - sizew / 2;
                y = My - sizeh / 2;
                box = new Rectangle(x, y, sizew, sizeh);
            }
            place();
        }

        if (ani == (aSpeed * aFrames) - 1) {
            ani = 0;
        } else {
            ani++;
        }
        if (eng.input.reset) {
            animated = false;
        }
        if (eng.input.set) {
            animated = true;
        }
        if (contains(box, true) && recBuilder(box).intersects(eng.mainChar.box)) {
            pop = true;
        } else {
            pop = false;
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
                        g.drawImage(campFire.get(0), x + eng.getXOff(), y + eng.getYOff(), sizew, sizeh, null);
                        break;
                    case 1:
                        g.drawImage(campFire.get(1), x + eng.getXOff(), y + eng.getYOff(), sizew, sizeh, null);
                        break;
                    case 2:
                        g.drawImage(campFire.get(2), x + eng.getXOff(), y + eng.getYOff(), sizew, sizeh, null);
                        break;
                    case 3:
                        g.drawImage(campFire.get(3), x + eng.getXOff(), y + eng.getYOff(), sizew, sizeh, null);
                        break;
                    case 4:
                        g.drawImage(campFire.get(4), x + eng.getXOff(), y + eng.getYOff(), sizew, sizeh, null);
                        break;
                }
            } else {
                g.drawImage(campFire.get(campFire.size() - 1), x + eng.getXOff(), y + eng.getYOff() + 32 * scale, 32 * scale, 32 * scale, null);
            }
        }

        if (eng.debug) {
            g.setColor(Color.red);
            g.drawRect(box.x + eng.getXOff(), box.y + eng.getYOff(), box.width, box.height);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="priorityRender Grpahics g">
    @Override
    public void priorityRender(Graphics g) {
        if (eng.mainChar.y < collisBox().y + eng.getYOff() && dropped) {
            if (animated) {
                switch (Math.round(ani / aSpeed)) {
                    case 0:
                        g.drawImage(campFire.get(0), x + eng.getXOff(), y + eng.getYOff(), sizew, sizeh, null);
                        break;
                    case 1:
                        g.drawImage(campFire.get(1), x + eng.getXOff(), y + eng.getYOff(), sizew, sizeh, null);
                        break;
                    case 2:
                        g.drawImage(campFire.get(2), x + eng.getXOff(), y + eng.getYOff(), sizew, sizeh, null);
                        break;
                    case 3:
                        g.drawImage(campFire.get(3), x + eng.getXOff(), y + eng.getYOff(), sizew, sizeh, null);
                        break;
                    case 4:
                        g.drawImage(campFire.get(4), x + eng.getXOff(), y + eng.getYOff(), sizew, sizeh, null);
                        break;
                }
            } else {
                g.drawImage(campFire.get(campFire.size() - 1), x + eng.getXOff(), y + eng.getYOff() + 32 * scale, 32 * scale, 32 * scale, null);
            }
        } else if (!dropped) {
            g.drawImage(campFire.get(campFire.size() - 1), x + eng.getXOff(), y + eng.getYOff() + 32 * scale, 32 * scale, 32 * scale, null);
        }
        if (blocked) {
            g.setColor(new Color(255, 0, 0, 100));
            g.fillRect(x + eng.getXOff(), y + eng.getYOff() + sizeh / 2 + 10, sizew, sizeh / 2 - 10);
            g.setColor(Color.red);
        }

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collision builder">
    public Rectangle collisBox() {
        return new Rectangle(x, y + 95, 60, 10);
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

    //<editor-fold defaultstate="collapsed" desc="RecBuilder rectangle in">
    public Rectangle recBuilder(Rectangle in) {
        return new Rectangle(in.x + eng.getXOff(), in.y + eng.getYOff(), in.width, in.height);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="place">
    public void place() {
        boolean in = false;
        if (!dropped) {
            for (int i = 0; i < eng.world.Objects.size(); i++) {
                if (new Rectangle(x, y, sizew, sizeh).intersects(eng.world.Objects.get(i))) {
                    in = true;
                    break;
                } else {
                    if (new Rectangle(collisBox().x + eng.getXOff(), collisBox().y + eng.getYOff(), 60, 35).intersects(eng.mainChar.collbox)) {
                        in = true;
                        break;
                    } else {
                        in = false;
                    }
                }
            }
            if (in) {
                blocked = true;
            } else {
                blocked = false;
                if (eng.left) {
                    dropped = true;
                    eng.world.Objects.add(collisBox());
                    eng.world.hubRoom.obbys.add(this);
                    eng.world.hubRoom.fires.clear();
                    box = new Rectangle(x, y, sizew, sizeh);
                    size = new Rectangle(x, y + sizeh / 2, sizew, sizeh / 2);
                    eng.world.hubRoom.stuff();
                }
            }
        }
    }
    //</editor-fold>
}
