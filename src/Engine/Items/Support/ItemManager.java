/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Items.Support;

import Engine.Engine.ESC;

/**
 *
 * @author DribbleWare
 */
public class ItemManager implements Runnable {

    ESC eng;
    boolean running = false;

    public ItemManager(ESC engine) {
        System.out.println("Item manager started");
        eng = engine;
        running = true;
    }

    public void update() {
        eng.world.active.itemUpdate();
        if (!eng.Loc.equalsIgnoreCase("menu")) {
            eng.mainChar.collision();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0; // Number of ticks for update and render cycle(in one second)
        double ns = 1000000000 / amountOfTicks; //Ticks per second
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;

        while (running) { //Running loop
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                //If fps locked to 60
                update();
                updates++;
                delta--;

            }
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("item manager ups: " + updates);
                updates = 0;
            }
        }
    }
}
