/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Player;

import Engine.Engine.ESC;
import Engine.Engine.Input;
import Engine.Items.Support.Item;
import Engine.Map.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class Player {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    //location
    public float x, y;
    public int speed = 5;
    public int w = 55, h = 96;
    public int health = 500;
    Input input;
    ESC eng;
    public Inventory inv;
    public final Rectangle box;
    public final Ellipse2D box2;
    private final Rectangle top;
    private final Rectangle lside;
    private final Rectangle rside;
    private final Rectangle bottom;
    public boolean up = false, down = false, left = false, right = false, overIt = false;

    //animations
    public int ani;
    public int aSpeed = 5; //animation speed (high is slow)
    public int direction = 2;//1-left, 2-right, 3-up, 4- down

    private List<BufferedImage> Animation = new ArrayList<>();
    private List<BufferedImage> walkRight;
    private List<BufferedImage> walkLeft;
    private List<BufferedImage> walkUp;
    private List<BufferedImage> walkDown;

    public Rectangle collbox;

    public boolean pass = false;
    public boolean dropped = true;
    public boolean blocked = false;

    boolean mouseDelay = false;
    int mouseTime = 0;
    final int MOUSE_MAX = 15;

    int ticks = 0;

    float[] dist = {0.7f, 1f};
    Color[] colors = {new Color(0, 0, 0, 0), new Color(0, 255, 0, 100)};
    Color[] colors2 = {new Color(0, 0, 0, 0), new Color(255, 0, 0, 100)};
//</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="Initializer">
    public Player(float xi, float yi, Input in, ESC eng) {
        this.x = eng.sizew / 2 - 50;
        this.y = eng.sizeh / 2 - 50;
        input = in;
        this.eng = eng;
        health = Integer.parseInt(this.eng.loadVars[0]);
        inv = new Inventory(this.eng);
        box = new Rectangle((int) (x - 82.5 + 25), (int) y - h / 2, w * 3, h * 2);

        box2 = new Ellipse2D.Float(x - 218, y - 186, 250 * 2, 250 * 2);

        //collision boxes for all 4 sides
        top = new Rectangle((int) x + 10, (int) y - speed / 2, w - 20, speed / 2);
        lside = new Rectangle((int) x - speed / 2, (int) y + 10, speed / 2, h - 20);
        rside = new Rectangle((int) x + w, (int) y + 10, speed / 2, h - 20);
        bottom = new Rectangle((int) x + 10, (int) y + h, w - 20, speed / 2);

        collbox = new Rectangle((int) x, (int) y, w, h);

        walkRight = this.eng.assetLdr.walkRight;
        walkLeft = this.eng.assetLdr.walkLeft;
        walkUp = this.eng.assetLdr.walkUp;
        walkDown = this.eng.assetLdr.walkDown;
        Animation = walkRight;

        collision();
        unstick();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Update">
    public void update() {
        input.update();
        inv.update();
        collect();
        if (mouseDelay) {
            mouseTime++;
            if (mouseTime == MOUSE_MAX) {
                mouseDelay = false;
                mouseTime = 0;
            }
        }
        if (!eng.Loc.equalsIgnoreCase("menu")) {
//            collision();
        }

        if (ani == (aSpeed * 8) - 1) {
            ani = 0;
        } else {
            ani++;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="render Graphics g">
    public void render(Graphics g) {
        g.setColor(Color.red);
        g.drawImage(eng.assetLdr.dropShadow, (int) x - 5, (int) y + 65, eng.assetLdr.dropShadow.getWidth() * 2, eng.assetLdr.dropShadow.getHeight() * 2, null);
        if (eng.move) {
            animate(g);
        } else {
            g.drawImage(walkUp.get(0), (int) x - 36, (int) y - 30, 128, 128, null);
        }
        if (!dropped) {
            Graphics2D g2d = (Graphics2D) g.create();
            RadialGradientPaint p = new RadialGradientPaint(new Point2D.Float(x + 32, y + 64), 250, dist, colors);
            if (blocked) {
                p = new RadialGradientPaint(new Point2D.Float(x + 32, y + 64), 250, dist, colors2);
            }
            g2d.setPaint(p);
            g2d.fillOval((int) x - 218, (int) y - 186, (int) 250 * 2, (int) 250 * 2);
            g2d.dispose();
        }

        //Collision boxes for debug mode
        if (eng.debug) {
            g.setColor(Color.ORANGE);
            g.drawRect(top.x, top.y, top.width, top.height);
            g.drawRect(bottom.x, bottom.y, bottom.width, bottom.height);
            g.drawRect(lside.x, lside.y, lside.width, lside.height);
            g.drawRect(rside.x, rside.y, rside.width, rside.height);
            g.setColor(Color.PINK);
            g.drawRect(box.x, box.y, box.width, box.height);
            g.setColor(Color.CYAN);
            g.drawRect(collbox.x, collbox.y, collbox.width, collbox.height);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="collect">
    public void collect() {
        overIt = false;
        for (int i = 0; i < eng.world.items.size(); i++) {
            if (box.intersects(RecBuilder(eng.world.items.get(i))) && contains(RecBuilder(eng.world.items.get(i)))) {
                overIt = true;
                eng.world.items.get(i).tool = true;
                if (eng.left && !mouseDelay) {
                    if (eng.world.items.get(i).id == 10 || eng.world.items.get(i).id == 11 || eng.world.items.get(i).id == 12) {
                        if (!inv.hasTool(eng.world.items.get(i).id)) {
                            inv.itemAdd(eng.world.items.get(i).id, eng.world.items.get(i).qnty, eng.world.items.get(i).health);
                            eng.world.items.remove(i);
                        } else {
                            eng.pop("Already have one", 0);
                        }
                    } else {
                        inv.itemAdd(eng.world.items.get(i).id, eng.world.items.get(i).qnty);
                        eng.world.items.remove(i);
                    }
                    mouseDelay = true;
                }
            } else {
                eng.world.items.get(i).tool = false;
            }
        }

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collision">
    public void collision() { //still need to finish fixing the crahsing in here

        //booleans to define if there is a collision on that side
        up = false;
        down = false;
        left = false;
        right = false;
        World World = eng.world;
        List<Area> Objects = World.Objects;

        AffineTransform temp = new AffineTransform();
        temp.translate(eng.getXOff(), eng.getYOff());

        for (int i = 0; i < Objects.size(); i++) { // check for collision with world objects
            if (World.active.map != null) {
                if (new Area(Objects.get(i)).createTransformedArea(temp).intersects(top) || !World.active.map.contains(top)) {
                    up = true;
                }
                if (new Area(Objects.get(i)).createTransformedArea(temp).intersects(bottom) || !World.active.map.contains(bottom)) {
                    down = true;
                }
                if (new Area(Objects.get(i)).createTransformedArea(temp).intersects(lside) || !World.active.map.contains(lside)) {
                    left = true;
                }
                if (new Area(Objects.get(i)).createTransformedArea(temp).intersects(rside) || !World.active.map.contains(rside)) {
                    right = true;
                }
            }
        }
        if (left && right && up && down) {
            eng.xoff = Integer.toString(Integer.parseInt(eng.xoff) + 10);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="animate Graphics g">
    public void animate(Graphics g) {
        //<editor-fold defaultstate="collapsed" desc="animation for player movements">\
        if (!eng.pause) {
            if (input.up) {
                direction = 3;
                Animation = walkUp;
            }
            if (input.down) {
                direction = 4;
                Animation = walkDown;
            }
            if (input.left) {
                direction = 1;
                Animation = walkLeft;
            }
            if (input.right) {
                direction = 2;
                Animation = walkRight;
            }
            if (input.right || input.up || input.left || input.down) {
                switch (Math.round(ani / aSpeed)) {
                    case 0:
                        g.drawImage(Animation.get(1), (int) x - 36, (int) y - 30, 128, 128, null);
                        break;
                    case 1:
                        g.drawImage(Animation.get(2), (int) x - 36, (int) y - 30, 128, 128, null);
                        break;
                    case 2:
                        g.drawImage(Animation.get(3), (int) x - 36, (int) y - 30, 128, 128, null);
                        break;
                    case 3:
                        g.drawImage(Animation.get(4), (int) x - 36, (int) y - 30, 128, 128, null);
                        break;
                    case 4:
                        g.drawImage(Animation.get(5), (int) x - 36, (int) y - 30, 128, 128, null);
                        break;
                    case 5:
                        g.drawImage(Animation.get(6), (int) x - 36, (int) y - 30, 128, 128, null);
                        break;
                    case 6:
                        g.drawImage(Animation.get(7), (int) x - 36, (int) y - 30, 128, 128, null);
                        break;
                    case 7:
                        g.drawImage(Animation.get(8), (int) x - 36, (int) y - 30, 128, 128, null);
                        break;
                }
            }
        }
        if (direction == 1 && !input.left || eng.pause) {
            g.drawImage(Animation.get(0), (int) x - 36, (int) y - 30, 128, 128, null);
        } else if (direction == 2 && !input.right || eng.pause) {
            g.drawImage(Animation.get(0), (int) x - 36, (int) y - 30, 128, 128, null);
        } else if (direction == 3 && !input.up || eng.pause) {
            g.drawImage(Animation.get(0), (int) x - 36, (int) y - 30, 128, 128, null);
        } else if (direction == 4 && !input.down || eng.pause) {
            g.drawImage(Animation.get(0), (int) x - 36, (int) y - 30, 128, 128, null);
        }
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loc">
    public Point loc() {
        return new Point(MouseInfo.getPointerInfo().getLocation().x - eng.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - eng.frame.getY() - Math.abs(eng.frame.getLocationOnScreen().y - eng.canvas.getLocationOnScreen().y));

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="contains Rectangle click">
    public boolean contains(Rectangle click) {
        boolean ret = false;
        if (click.contains(new Point(MouseInfo.getPointerInfo().getLocation().x - eng.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - eng.frame.getY() - Math.abs(eng.frame.getLocationOnScreen().y - eng.canvas.getLocationOnScreen().y)))) {
            ret = true;
        }
        return ret;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="RecBuilder Item item">
    public Rectangle RecBuilder(Item item) {
        return new Rectangle(item.x + eng.getXOff(), item.y + eng.getYOff(), item.width, item.height);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Unstick">
    public void unstick() { //not sure
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    public Rectangle getBox() {
        return box;
    }
    //</editor-fold>

}
