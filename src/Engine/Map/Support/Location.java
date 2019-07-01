/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map.Support;

import Engine.Engine.ESC;
import Engine.Items.Support.Item;
import Engine.Items.Support.Light;
import Engine.Items.Support.WorldObjects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class Location {

    public List<Area> objects = new ArrayList<>();
    public List<Item> items = new ArrayList<>();
    public List<Light> lights = new ArrayList<>();

    public List<WorldObjects> obbys = new ArrayList<>();

    public boolean dark = false;
    public boolean permDark = false;

    public boolean fading = false;

    public ESC eng;

    public Rectangle map;

    public Color FILTER = new Color(0, 0, 0, 0);

    public Color filter = new Color(0, 0, 0, 0);
    public Color backGround = new Color(0, 0, 0, 0);

    public void update() {
    }

    public void itemUpdate() {
    }

    public void render(Graphics g) {
    }

    public void priorityRender(Graphics g) {
    }

    public void overlayRender(Graphics g) {
    }

    public void overlayPriorityRender(Graphics g) {
    }

    public void updateList() {
    }

    //<editor-fold defaultstate="collapsed" desc="toMine">
    /**
     * Sends the player to the mine putting him at the caves entrance
     *
     */
    public void toMine() {
        if (!fading) {
            eng.fadeOut();
            fading = true;
        }
        if (eng.faded) {
            eng.faded = false;
            eng.Loc = "mine";
            eng.world.worldSwitch();
            fading = false;
            eng.fadeIn();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="toHub">
    /**
     * Sends the player to the hub
     *
     */
    public void toHub() {
        if (!fading) {
            eng.fadeOut();
            System.out.println("fading out");
            fading = true;
        }
        if (eng.faded) {
            System.out.println("feded out");
            eng.faded = false;
            eng.Loc = "hub";
            eng.world.worldSwitch();
            fading = false;
            eng.fadeIn();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="toHubFromMine">
    /**
     * Sends the player to the hub at the location of the mine door
     *
     */
    public void toHubFromMine() {
        if (!fading) {
            eng.fadeOut();
            fading = true;
        }
        if (eng.faded) {
            eng.faded = false;
            eng.Loc = "hub";
            eng.setXOff(700 - (eng.world.hubRoom.mineDoor.x + eng.world.hubRoom.mineDoor.width / 2));
            eng.setYOff(350 - (eng.world.hubRoom.mineDoor.y + eng.world.hubRoom.mineDoor.height / 2) + 15);
            eng.world.worldSwitch();
            fading = false;
            eng.fadeIn();
        }
    }
    //</editor-fold>

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
