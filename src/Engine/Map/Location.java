/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map;

import Engine.Items.Item;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class Location {

    public List<Rectangle> objects = new ArrayList<>();
    public List<Item> items = new ArrayList<>();

    public List<WorldObjects> obbys = new ArrayList<>();

    public boolean dark = false;

    public void update() {

    }

    public void render(Graphics g) {

    }

    public void priorityRender(Graphics g) {

    }

    public void updateList() {

    }

    //<editor-fold defaultstate="collapsed" desc="order">
    //reorganizes, adds and sorts the objects to the obbys list to create the render order
    public void order() {
        boolean order = true;
        WorldObjects holder;
        if (obbys.size() >= 2) {
            for (int i = 1; i < obbys.size(); i++) {
                if (obbys.get(i).collis.y < obbys.get(i - 1).collis.y) {
                    holder = obbys.get(i);
                    obbys.set(i, obbys.get(i - 1));
                    obbys.set(i - 1, holder);
                    i = 1;
                }
            }
        }
    }
//</editor-fold>

}
