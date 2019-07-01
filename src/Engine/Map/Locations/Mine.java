/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map.Locations;

import Engine.Engine.ESC;
import Engine.Items.Support.Light;
import Engine.Map.Support.Location;
import Engine.Map.Support.MineMaker;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

/**
 *
 * @author DribbleWare
 */
public class Mine extends Location {

    public BufferedImage cave;
    Area box;
    Rectangle exit = new Rectangle(0, 0, 0, 0);
    boolean newBox = true;

    public Mine(ESC engine) {
        filter = new Color(0, 0, 0, 245);
        FILTER = new Color(0, 0, 0, 245);
        backGround = new Color(20, 17, 21);
        eng = engine;
        permDark = true;
    }

    public void getCave() {
        cave = MineMaker.getMap(eng);
        exit = MineMaker.exit;
        eng.setXOff(750 - ((MineMaker.exit.x + MineMaker.exit.width / 2) * eng.size));
        eng.setYOff(400 - ((MineMaker.exit.y + MineMaker.exit.height + 15)) * eng.size);
        lights.clear();
        lights.add(new Light((exit.x + exit.width / 2) - 70, (exit.y + exit.height) - 70, 70, 1f));
        box = null;
        newBox = true;
    }

    @Override
    public void update() {
        if (new Rectangle((exit.x * 4) + eng.getXOff(), (exit.y * 4) + eng.getYOff(), exit.width * 4, exit.height * 4).contains(eng.mainChar.collbox)) {
            toHubFromMine();
        }
        map = new Rectangle(eng.getXOff(), eng.getYOff(), cave.getWidth() * eng.size, cave.getHeight() * eng.size);
        if (box == null && MineMaker.getBox() != null && newBox) {
            AffineTransform at = new AffineTransform();
            at.scale(4, 4);
            box = new Area(MineMaker.getBox()).createTransformedArea(at);
            newBox = false;
            updateList();
        }
        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(cave, 0 + eng.getXOff(), 0 + eng.getYOff(), cave.getWidth() * eng.size, cave.getHeight() * eng.size, null);
        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).render(g);
        }
        //Drawing all of the items to their world location
        for (int i = 0; i < items.size(); i++) {
            g.drawImage(items.get(i).art[items.get(i).id], items.get(i).x + eng.getXOff(), items.get(i).y + eng.getYOff(), items.get(i).width, items.get(i).height, null);
        }
    }

    @Override
    public void priorityRender(Graphics g) {
        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).priorityRender(g);
        }
        for (int i = 0; i < obbys.size(); i++) {
            obbys.get(i).popUpRender(g);
        }
    }

    @Override
    public void updateList() {
        objects.clear();
        order();
        for (int i = 0; i < obbys.size(); i++) {
            if (obbys.get(i).dropped == false) {
                //do nothing
            } else {
                objects.add(new Area(obbys.get(i).collisBox()));
            }
        }
        if (box != null) {
            objects.add(box);
        }

    }
}
