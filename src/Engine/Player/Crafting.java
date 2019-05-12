/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Player;

import Engine.Engine.ESC;
import Engine.Items.CampFire;
import Engine.Items.Item;
import Engine.Items.Tree;
import Engine.Items.TreeV2;
import Engine.Items.WorkBench;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author DribbleWare
 */
public class Crafting {

    ESC eng;
    inventory inv;
    boolean mouseDelay = true;
    int del = 0, maxDel = 15;

    private List<Item> Bucket = new ArrayList<>();
    private List<Item> Fire = new ArrayList<>();
    private List<Item> Bench = new ArrayList<>();
    private List<Item> Tree = new ArrayList<>();

    public Crafting(ESC engine, inventory invent) {
        eng = engine;
        inv = invent;
        craftInit();
    }

    public void update() {
        if (!mouseDelay) {
            del++;
        }
        if (del >= maxDel) {
            mouseDelay = true;
            del = 0;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="logCraft Graphics g">
    public void logCraft(Graphics g) {
        craftBox(0, eng.assetLdr.emptyBucket, Bucket, 2, g);
        craftBox(1, eng.assetLdr.campFire.get(eng.assetLdr.campFire.size() - 1), Fire, g, "Campfire");
        craftBox(2, eng.assetLdr.workBench, Bench, g, "Bench");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sapling Craft Graphics g">
    public void saplingCraft(Graphics g) {
        craftBox(0, eng.assetLdr.sapling, Tree, g, "Tree");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftBox int number, image, cost, int newId, graphics">
    public void craftBox(int number, BufferedImage image, List<Item> cost, int newId, Graphics g) {
        boolean after = false;
        boolean qty = true;
        int lineOff = 25;
        List<Integer> craftSlots = new ArrayList<>();
        int newIDSlot = -1;

        for (int i = 0; i < cost.size(); i++) {
            for (int j = 0; j < inv.inven.size(); j++) {
                if (inv.inven.get(j).id == cost.get(i).id) {
                    craftSlots.add(j);
                }
                if (newId == inv.inven.get(j).id) {
                    newIDSlot = j;
                }
            }
        }
        for (int i = 0; i < craftSlots.size(); i++) {
            if (inv.inven.get(craftSlots.get(i)).qnty < cost.get(i).qnty) {
                qty = false;
            }
        }
        if (contains(inv.Crafts.get(number))) {
            if (qty) {
                g.setColor(new Color(255, 255, 255, 100));
                if (eng.left && mouseDelay) {
                    if (newIDSlot > -1) {
                        inv.inven.get(newIDSlot).qnty += 1;
                    } else {
                        inv.inven.add(new Item(newId, 1));

                    }
                    for (int i = 0; i < craftSlots.size(); i++) {
                        int slot = craftSlots.get(i);
                        inv.inven.get(slot).qnty -= cost.get(i).qnty;
                        if (inv.inven.get(slot).qnty == 0) {
                            inv.inven.remove(slot);
                        }
                    }
                    mouseDelay = false;
                }
            } else {
                after = true;
            }

        } else {
            g.setColor(new Color(100, 100, 100, 150));
        }

        if (!after) {
            g.fillRect(inv.Crafts.get(number).x, inv.Crafts.get(number).y + eng.notches, inv.Crafts.get(number).width, inv.Crafts.get(number).height);
        }
        g.setColor(Color.white);
        g.drawRect(inv.Crafts.get(number).x, inv.Crafts.get(number).y, inv.Crafts.get(number).width, inv.Crafts.get(number).height);

        g.drawImage(image, inv.Crafts.get(number).x + 5, inv.Crafts.get(number).y + 5 + eng.notches, inv.Crafts.get(number).height - 10, inv.Crafts.get(number).height - 10, null);

        g.drawString(cost.get(0).NAMES[newId], inv.Crafts.get(number).x + inv.Crafts.get(number).height + 5, inv.Crafts.get(number).y + 50 + eng.notches);

        for (int i = 0; i < cost.size(); i++) {
            g.drawString("Cost: " + cost.get(i).NAMES[cost.get(i).id] + " " + cost.get(i).qnty, inv.Crafts.get(number).x + inv.Crafts.get(number).height + 5, inv.Crafts.get(number).y + 75 + eng.notches + lineOff * i);
        }

        if (after) {
            g.setColor(new Color(255, 0, 0, 150));
            g.fillRect(inv.Crafts.get(number).x + 2, inv.Crafts.get(number).y + eng.notches + 2, inv.Crafts.get(number).width - 3, inv.Crafts.get(number).height - 3);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftBox int number, image, cost, int newId, graphics, specialMod">
    public void craftBox(int number, BufferedImage image, List<Item> cost, Graphics g, String specialMod) {
        boolean after = false;
        boolean qty = true;
        int lineOff = 25;
        List<Integer> craftSlots = new ArrayList<>();

        for (int i = 0; i < cost.size(); i++) {
            for (int j = 0; j < inv.inven.size(); j++) {
                if (inv.inven.get(j).id == cost.get(i).id) {
                    craftSlots.add(j);
                }
            }
        }
        for (int i = 0; i < craftSlots.size(); i++) {
            if (inv.inven.get(craftSlots.get(i)).qnty < cost.get(i).qnty) {
                qty = false;
            }
        }
        if (contains(inv.Crafts.get(number))) {
            if (qty) {
                g.setColor(new Color(255, 255, 255, 100));
                if (eng.left && mouseDelay) {
                    switch (specialMod) {
                        case "Campfire":
                            eng.world.hubRoom.obbys.add(new CampFire((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 5, eng));
                            inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                            eng.inventory = false;
                            break;
                        case "Bench":
                            eng.world.hubRoom.obbys.add(new WorkBench((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), eng));
                            inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                            eng.inventory = false;
                            break;
                        case "Tree":
                            if (new Random().nextInt(20) == 7) {
                                eng.world.hubRoom.obbys.add(new Tree(eng, (int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 1));
                            } else {
                                eng.world.hubRoom.obbys.add(new Tree(eng, (int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 0));
                            }
                            inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                            eng.inventory = false;
                            break;
                    }
                    for (int i = 0; i < craftSlots.size(); i++) {
                        int slot = craftSlots.get(i);
                        inv.inven.get(slot).qnty -= cost.get(i).qnty;
                        if (inv.inven.get(slot).qnty == 0) {
                            inv.inven.remove(slot);
                        }
                    }
                    mouseDelay = false;
                }
            } else {
                after = true;
            }

        } else {
            g.setColor(new Color(100, 100, 100, 150));
        }

        if (!after) {
            g.fillRect(inv.Crafts.get(number).x, inv.Crafts.get(number).y + eng.notches, inv.Crafts.get(number).width, inv.Crafts.get(number).height);
        }
        g.setColor(Color.white);
        g.drawRect(inv.Crafts.get(number).x, inv.Crafts.get(number).y, inv.Crafts.get(number).width, inv.Crafts.get(number).height);

        g.drawImage(image, inv.Crafts.get(number).x + 5, inv.Crafts.get(number).y + 5 + eng.notches, inv.Crafts.get(number).height - 10, inv.Crafts.get(number).height - 10, null);

        g.drawString(specialMod, inv.Crafts.get(number).x + inv.Crafts.get(number).height + 5, inv.Crafts.get(number).y + 50 + eng.notches);

        for (int i = 0; i < cost.size(); i++) {
            g.drawString("Cost: " + cost.get(i).NAMES[cost.get(i).id] + " " + cost.get(i).qnty, inv.Crafts.get(number).x + inv.Crafts.get(number).height + 5, inv.Crafts.get(number).y + 75 + eng.notches + lineOff * i);
        }

        if (after) {
            g.setColor(new Color(255, 0, 0, 150));
            g.fillRect(inv.Crafts.get(number).x + 2, inv.Crafts.get(number).y + eng.notches + 2, inv.Crafts.get(number).width - 3, inv.Crafts.get(number).height - 3);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftInit">
    public void craftInit() { //should make a file to load these all in 

        Item[] bucketList = {new Item(0, 3)};
        Item[] fireList = {new Item(0, 7)};
        Item[] benchList = {new Item(0, 15)};
        Item[] treeList = {new Item(5, 1)};

        Bucket.addAll(Arrays.asList(bucketList));
        Fire.addAll(Arrays.asList(fireList));
        Bench.addAll(Arrays.asList(benchList));
        Tree.addAll(Arrays.asList(treeList));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Contains Rectangle click">
    public boolean contains(Rectangle click) {
        boolean ret = false;
        if (click.contains(new Point(MouseInfo.getPointerInfo().getLocation().x - eng.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - eng.frame.getY() - Math.abs(eng.frame.getLocationOnScreen().y - eng.canvas.getLocationOnScreen().y)))) {
            ret = true;
        }
        return ret;
    }
    //</editor-fold>

}
