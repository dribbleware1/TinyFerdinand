/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map;

import Engine.Engine.ESC;
import java.awt.Graphics;

/**
 *
 * @author DribbleWare
 */
public class Mine extends Location {

    ESC eng;

    public Mine(ESC engine) {
        eng = engine;
dark = true;
    }

    public void update() {
        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).update();
        }
    }

    public void render(Graphics g) {
        g.drawImage(eng.assetLdr.terrainSheet, eng.getXOff(), eng.getYOff(), null);
        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).render(g);
        }
    }

    public void priorityRender(Graphics g) {
        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).priorityRender(g);
        }
        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).popUpRender(g);
        }
    }

    public void updateList() {
        objects.clear();
        order();
        for (int i = 0; i < obbys.size(); i++) {
            if (obbys.get(i).dropped == false) {
                //do nothing
            } else {
                objects.add(obbys.get(i).collisBox());
            }
        }
    }
}
