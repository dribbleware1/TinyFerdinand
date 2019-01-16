/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
    boolean dropStart = false;

    Item itemRef = new Item(-100, 0);

    //Key location points for inventory slot text
    public int textY = 780;
    private int textXOff = 78;
    private int offset = 15;
    //Item slot locations on screen
    private final Rectangle slot1 = new Rectangle(422, 690, 100, 100);
    private final Rectangle slot2 = new Rectangle(550, 690, 100, 100);
    private Rectangle[] slots = new Rectangle[]{slot1, slot2};

    public inventory(ESC engine) {
        eng = engine;
        itemRef.artBuild();
    }

    public void update() {
        if (dropStart) {
            drop++;
        }
        if(drop > 10){
            dropStart = false;
        }

        if (drop > 10) {
            for (int i = 0; i < inven.size(); i++) {
                if (contains(slots[i]) && eng.right) {
                    eng.world.hubRoom.items.add(new Item(dropItem().x, dropItem().y, dropItem().width, dropItem().height, inven.get(i).id, inven.get(i).qnty));
                    inven.remove(i);
                    drop = 0;
                    dropStart = true;
                }
            }
        }
    }

    public void render(Graphics g) {
        g.setFont(text);
        g.getFontMetrics(text);

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
}
