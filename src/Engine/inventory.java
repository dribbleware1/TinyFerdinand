/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class inventory {

    ESC eng;
    //Text font
    public List<Item> inven = new ArrayList<>();
    public Font text = new Font("TimesRoman", Font.PLAIN, 25);

    int drop = 11;
    boolean dropStart = false, refresh = true;

    Item itemRef = new Item(-100, 0);

    //Key location points for inventory slot text
    public int textY = 180;
    private int textXOff = 78;
    private int offset = 15;

    public int slotsx = 13, slotsy = 6;

    Rectangle slotmenu = new Rectangle(0, 0, 0, 0);

    private boolean pop = false;
    int pops = 0;

    Rectangle[] slots;

    int pickUpY = 675;

    private Rectangle dropBox;

    public inventory(ESC engine) {
        eng = engine;
        itemRef.artBuild();
        slots = new Rectangle[slotsx * slotsy];

        slotsAdd();

    }

    public void update() {
        if (dropStart) {
            drop++;
        }
        if (drop > 10) {
            dropStart = false;
        }
        if (!eng.inventory) {
            pop = false;
        }
        if (drop > 10 && false) {
            for (int i = 0; i < inven.size(); i++) {
                if (contains(slots[i]) && eng.right) {
                    eng.world.hubRoom.items.add(new Item(dropItem().x, dropItem().y, dropItem().width, dropItem().height, inven.get(i).id, inven.get(i).qnty));
                    inven.remove(i);
                    drop = 0;
                    dropStart = true;
                    pop = false;
                }
            }
        }
    }

    public void render(Graphics g) {

        g.setFont(text);
        g.getFontMetrics(text);
        g.setColor(Color.BLACK);

        if (eng.inventory) {
            g.setColor(new Color(10, 10, 10, 150));
            g.fillRoundRect(50, 50, eng.sizew - 100, eng.sizeh - 100, 50, 50);

//            for (int i = 0; i < slots.length; i++) {
//                g.setColor(Color.red);
//                g.drawRoundRect(slots[i].x, slots[i].y, slots[i].width, slots[i].height, 50, 50);
//            }
            for (int i = 0; i < inven.size(); i++) {
                g.drawImage(itemRef.art[inven.get(i).id], slots[i].x, slots[i].y, slots[i].width, slots[i].height, null);
                g.setColor(Color.white);
                if (inven.get(i).qnty > 99) {//for 3 digit number alingment
                    g.drawString(Integer.toString(inven.get(i).qnty), slots[i].x - offset * 2 + textXOff, textY);
                } else if (inven.get(i).qnty > 9) {// for 2 digit number alingment
                    g.drawString(Integer.toString(inven.get(i).qnty), slots[i].x - offset + textXOff, textY);
                } else {//single digit number alingment
                    g.drawString(Integer.toString(inven.get(i).qnty), slots[i].x + textXOff, textY);
                }
            }

            if (inven.size() > 0) {
                for (int i = 0; i < inven.size(); i++) {
                    if (contains(slots[i]) && eng.left && !pop) {
                        pop = true;
                        pops = i;
                        slotmenu = new Rectangle(slots[i].x + slots[i].width - 20, slots[i].y, 150, 200);
                    }
                }
                if (pop) {
                    g.setColor(new Color(10, 10, 10, 100));
                    g.fillRect(slotmenu.x, slotmenu.y, slotmenu.width, slotmenu.height);
                    g.setColor(Color.WHITE);
                    dropBox = new Rectangle(slots[pops].x + slots[pops].width - 20, slots[pops].y + 28, slotmenu.width, 25);
                    menuPop(g);

                }
                if (!contains(slots[pops]) && !contains(slotmenu)) {
                    pop = false;
                }
            }

            //Drawing the pick up prompt when overtop of an item
            g.setColor(Color.white);
            if (eng.mainChar.overIt == true && !eng.pause) {
                g.drawString("Press Left Mouse To Pick Up", 555, eng.sizeh - 30);
            }
            refresh = false;
        }
    }

    public void menuPop(Graphics g) {
        g.drawString(inven.get(pops).NAMES[inven.get(pops).id] + "  " + inven.get(pops).qnty, slots[pops].x + slots[pops].width - 10, slots[pops].y + 20);
        g.drawString("Drop", slots[pops].x + slots[pops].width - 10, slots[pops].y + 50);

        if (eng.debug) {
            g.setColor(Color.red);
            g.drawRect(dropBox.x, dropBox.y, dropBox.width, dropBox.height);
        }

        if (contains(dropBox) && drop > 10 && eng.left) {
            eng.world.hubRoom.items.add(new Item(dropItem().x, dropItem().y, dropItem().width, dropItem().height, inven.get(pops).id, inven.get(pops).qnty));
            inven.remove(pops);
            drop = 0;
            dropStart = true;
            pop = false;
        }

    }

    public Rectangle dropItem() {

        return new Rectangle((int) eng.mainChar.x - Integer.parseInt(eng.xoff), (int) eng.mainChar.y - Integer.parseInt(eng.yoff) + 30, 50, 50);

    }

    public boolean contains(Rectangle click) {
        boolean ret = false;
        if (click.contains(new Point(MouseInfo.getPointerInfo().getLocation().x - eng.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - eng.frame.getY() - Math.abs(eng.frame.getLocationOnScreen().y - eng.canvas.getLocationOnScreen().y)))) {
            ret = true;
        }
        return ret;
    }

    public void slotsAdd() {
        int t = 0;
        System.out.println("yip?");
        for (int j = 0; j < slotsy; j++) {
            for (int i = 0; i < slotsx; i++) {
                slots[t] = (new Rectangle(60 + 105 * i, 90 + 105 * j, 100, 100));
                t++;
            }
        }
    }

    public Point loc(ESC engine) {
        return new Point(MouseInfo.getPointerInfo().getLocation().x - engine.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - engine.frame.getY() - Math.abs(engine.frame.getLocationOnScreen().y - engine.canvas.getLocationOnScreen().y));
    }

}
