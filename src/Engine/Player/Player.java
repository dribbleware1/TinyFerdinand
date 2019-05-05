/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Player;

import Engine.Items.Item;
import Engine.Map.World;
import Engine.Engine.ESC;
import Engine.Engine.Input;
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
public class Player {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    //location
    public float x, y;
    public int speed = 5;
    public int w = 55, h = 96;
    public int health = 500;
    Input input;
    ESC engine;
    public inventory inv;
    World World;
    public final Rectangle box, box2;
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

    public Rectangle map, collbox;

    public boolean pass = false;

    int ticks = 0;
//</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="Initializer">
    public Player(float xi, float yi, Input in, ESC eng, World world) {
        this.x = eng.sizew / 2 - 50;
        this.y = eng.sizeh / 2 - 50;
        input = in;
        engine = eng;
        health = Integer.parseInt(engine.loadVars[0]);
        inv = new inventory(engine);
        box = new Rectangle((int) (x - 82.5 + 25), (int) y - h / 2, w * 3, h * 2);
        box2 = new Rectangle(box.x - 75, box.y - 75, box.width + 140, box.height + 140);

        //collision boxes for all 4 sides
        top = new Rectangle((int) x + 10, (int) y - speed / 2, w - 20, speed / 2);
        lside = new Rectangle((int) x - speed / 2, (int) y + 10, speed / 2, h - 20);
        rside = new Rectangle((int) x + w, (int) y + 10, speed / 2, h - 20);
        bottom = new Rectangle((int) x + 10, (int) y + h, w - 20, speed / 2);

        collbox = new Rectangle((int) x, (int) y, w, h);

        World = world;

        walkRight = engine.assetLdr.walkRight;
        walkLeft = engine.assetLdr.walkLeft;
        walkUp = engine.assetLdr.walkUp;
        walkDown = engine.assetLdr.walkDown;
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
        collision();

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
        g.drawImage(engine.assetLdr.dropShadow, (int) x - 5, (int) y + 65, engine.assetLdr.dropShadow.getWidth() * 2, engine.assetLdr.dropShadow.getHeight() * 2, null);
        animate(g);
        //g.drawRect(collbox.x, collbox.y, collbox.width, collbox.height);
//Collision boxes for debug mode
        if (engine.debug) {
            g.setColor(Color.ORANGE);
            g.drawRect(top.x, top.y, top.width, top.height);
            g.drawRect(bottom.x, bottom.y, bottom.width, bottom.height);
            g.drawRect(lside.x, lside.y, lside.width, lside.height);
            g.drawRect(rside.x, rside.y, rside.width, rside.height);
            g.setColor(Color.PINK);
            g.drawRect(box.x, box.y, box.width, box.height);
            g.drawRect(collbox.x, collbox.y, collbox.width, collbox.height);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="collect">
    public void collect() {
        overIt = false;
        for (int i = 0; i < World.items.size(); i++) {
            if (box.intersects(RecBuilder(World.items.get(i))) && contains(RecBuilder(World.items.get(i)))) {
                overIt = true;
                World.items.get(i).tool = true;
                if (engine.left) {
                    for (int j = 0; j < inv.inven.size(); j++) {
                        if (World.items.get(i).id == inv.inven.get(j).id) {
                            inv.inven.get(j).qnty += World.items.get(i).qnty;
                            World.items.remove(i);
                            return;
                        }
                    }
                    inv.inven.add(new Item(World.items.get(i).id, World.items.get(i).qnty));
                    World.items.remove(i);
                }
            } else {
                World.items.get(i).tool = false;
            }
        }

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Collision">
    private void collision() {
        //booleans to define if there is a collision on that side
        up = false;
        down = false;
        left = false;
        right = false;
        for (int i = 0; i < World.Objects.size(); i++) { // check for collision with world objects
            if (new Rectangle((World.Objects.get(i).x + engine.getXOff()), (World.Objects.get(i).y + engine.getYOff()), World.Objects.get(i).width, World.Objects.get(i).height).intersects(top) || !map.contains(top)) {
                up = true;
            }
            if (new Rectangle((World.Objects.get(i).x + engine.getXOff()), (World.Objects.get(i).y + engine.getYOff()), World.Objects.get(i).width, World.Objects.get(i).height).intersects(lside) || !map.contains(lside)) {
                left = true;
            }
            if (new Rectangle((World.Objects.get(i).x + engine.getXOff()), (World.Objects.get(i).y + engine.getYOff()), World.Objects.get(i).width, World.Objects.get(i).height).intersects(rside) || !map.contains(rside)) {
                right = true;
            }
            if (new Rectangle((World.Objects.get(i).x + engine.getXOff()), (World.Objects.get(i).y + engine.getYOff()), World.Objects.get(i).width, World.Objects.get(i).height).intersects(bottom) || !map.contains(bottom)) {
                down = true;
            }
        }
        if (left && right && up && down) {
            engine.xoff = Integer.toString(Integer.parseInt(engine.xoff) + 10);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="animate Graphics g">
    public void animate(Graphics g) {
        //<editor-fold defaultstate="collapsed" desc="animation for player movements">\
        if (!engine.pause) {
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
        if (direction == 1 && !input.left || engine.pause) {
            g.drawImage(Animation.get(0), (int) x - 36, (int) y - 30, 128, 128, null);
        } else if (direction == 2 && !input.right || engine.pause) {
            g.drawImage(Animation.get(0), (int) x - 36, (int) y - 30, 128, 128, null);
        } else if (direction == 3 && !input.up || engine.pause) {
            g.drawImage(Animation.get(0), (int) x - 36, (int) y - 30, 128, 128, null);
        } else if (direction == 4 && !input.down || engine.pause) {
            g.drawImage(Animation.get(0), (int) x - 36, (int) y - 30, 128, 128, null);
        }
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loc">
    public Point loc() {
        return new Point(MouseInfo.getPointerInfo().getLocation().x - engine.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - engine.frame.getY() - Math.abs(engine.frame.getLocationOnScreen().y - engine.canvas.getLocationOnScreen().y));

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="contains Rectangle click">
    public boolean contains(Rectangle click) {
        boolean ret = false;
        if (click.contains(new Point(MouseInfo.getPointerInfo().getLocation().x - engine.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - engine.frame.getY() - Math.abs(engine.frame.getLocationOnScreen().y - engine.canvas.getLocationOnScreen().y)))) {
            ret = true;
        }
        return ret;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="RecBuilder Item item">
    public Rectangle RecBuilder(Item item) {

        return new Rectangle(item.x + engine.getXOff(), item.y + engine.getYOff(), item.width, item.height);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Unstick">
    public void unstick() {

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    public Rectangle getBox() {
        return box;
    }
    //</editor-fold>

}
