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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class World{
    //Housekeeping
    Input input;
    ESC engine;

    
    //no idea
    public int time = 10;
    //Rooms and displays
    public Hub hubRoom;
    public Menu menu;
    //objects and items currently active
    public List<Rectangle> Objects = new ArrayList<>();
    public List<Item> items = new ArrayList<>();

    public World(Input in, ESC engi) {
        input = in;
        engine = engi;
    }

    public void render(Graphics g) {
        switch (engine.Loc) {
            case "hub":
                hubRoom.render(g);
                break;
            case "menu":
                menu.render(g);
                break;
        }
    }

    public void update() {
        switch (engine.Loc) {
            case "hub":
                hubRoom.update();
                if (menu.update == true) {
                    updatelist();
                    menu.update = false;
                }
                break;
            case "menu":
                menu.update();
        }
    }


    public void start() {
        System.out.println("starting the world");
        hubRoom = new Hub(engine);
        menu = new Menu(engine);
        updatelist();
    }

    public void updatelist() {
        switch (engine.Loc) {
            case "hub":
                Objects = hubRoom.objects;
                items = hubRoom.items;
                break;
        }
    }
}
