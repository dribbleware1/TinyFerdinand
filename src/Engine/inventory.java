/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
    //Key location points for inventory slot text
    public int textY = 780;
    private int slot1X = 500;
    private int slot2X = 628;
    private int offset = 15;
    //Item slot locations on screen
    private final Rectangle slot1 = new Rectangle(422, 690, 100, 100);
    private final Rectangle slot2 = new Rectangle(550, 690, 100, 100);

    public inventory(ESC engine) {
        eng = engine;
    }

    public void update() {
    }

    public void render(Graphics g) {
        g.setFont(text);
        g.getFontMetrics(text);
        for (int i = 0; i < inven.size(); i++) {
            switch (i) {
                case 0: //invntory sot 1
                    if (inven.get(i).id == 1) {
                        g.drawImage(eng.assetLdr.logs, slot1.x, slot1.y, slot1.width, slot1.height, null);
                    } else if (inven.get(i).id == 2) {
                        g.drawImage(eng.assetLdr.rocks, slot1.x, slot1.y, slot1.width, slot1.height, null);
                    }
                    g.setColor(Color.white);
                    if (inven.get(i).qnty > 99) { //for 3 digit number alingment
                        g.drawString(Integer.toString(inven.get(i).qnty), slot1X - 2 * offset, textY);
                    } else if (inven.get(i).qnty > 9) { // for 2 digit number alingment
                        g.drawString(Integer.toString(inven.get(i).qnty), slot1X - offset, textY);
                    } else {//single digit number alingment
                        g.drawString(Integer.toString(inven.get(i).qnty), slot1X, textY);
                    }
                    break;

                case 1: //inventory slot 2
                    if (inven.get(i).id == 1) {
                       g.drawImage(eng.assetLdr.logs, slot2.x, slot2.y, slot2.width, slot2.height, null);
                    } else if (inven.get(i).id == 2) {
                        g.drawImage(eng.assetLdr.rocks, slot2.x, slot2.y, slot2.width, slot2.height, null);
                    }
                    g.setColor(Color.white);
                    if (inven.get(i).qnty > 99) {//for 3 digit number alingment
                        g.drawString(Integer.toString(inven.get(i).qnty), slot2X - offset * 2, textY);
                    } else if (inven.get(i).qnty > 9) {// for 2 digit number alingment
                        g.drawString(Integer.toString(inven.get(i).qnty), slot2X - offset, textY);
                    } else {//single digit number alingment
                        g.drawString(Integer.toString(inven.get(i).qnty), slot2X, textY);
                    }
                    break;
            }
        }
    }
}
