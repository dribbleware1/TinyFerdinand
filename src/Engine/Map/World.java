/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map;

import Engine.Engine.ESC;
import Engine.Engine.Input;
import Engine.Items.Item;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class World {

    //Housekeeping
    Input input;
    ESC eng;
    String prevLoc = "null";

    //no idea
    public int time = 10;
    //Rooms and displays
    public Hub hubRoom;
    public Menu menu;
    public Mine mine;
    //objects and items currently active
    public List<Rectangle> Objects = new ArrayList<>();
    public List<Item> items = new ArrayList<>();

    public Location active;

    public World(Input in, ESC engi) {
        input = in;
        eng = engi;
        hubRoom = new Hub(eng);
        menu = new Menu(eng);
        mine = new Mine(eng);
        worldSwitch();
        start();
    }

    public void render(Graphics g) {
        active.render(g);
    }

    public void priorityRrender(Graphics g) {
        active.priorityRender(g);
    }

    public void update() {
        worldSwitch();
        active.update();
    }

    public void start() {
        System.out.println("starting the world");
        worldSwitch();
        updatelist();
    }

    public void worldSwitch() {
        if (eng.Loc != prevLoc) {
            switch (eng.Loc) {
                case "menu":
                    active = menu;
                    break;
                case "hub":
                    active = hubRoom;
                    break;
                case "mine":
                    active = mine;
                    break;
            }
            updatelist();
            prevLoc = eng.Loc;
        }
    }

    public void updatelist() {
        active.updateList();
        Objects = active.objects;
        items = active.items;
    }
}
