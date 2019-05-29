/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Player;

import Engine.Engine.ESC;
import Engine.Items.CampFire;
import Engine.Items.Fence;
import Engine.Items.Item;
import Engine.Items.Tree;
import Engine.Items.WorkBench;
import java.awt.Color;
import java.awt.Font;
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

    //<editor-fold defaultstate="collapsed" desc="declarations">
    ESC eng;
    inventory inv;
    boolean mouseDelay = true;
    int del = 0, maxDel = 15;

    private List<Item> Bucket = new ArrayList<>();
    private List<Item> Fire = new ArrayList<>();
    private List<Item> Bench = new ArrayList<>();
    private List<Item> Tree = new ArrayList<>();
    private List<Item> Stick = new ArrayList<>();
    private List<Item> Paper = new ArrayList<>();
    private List<Item> Plank = new ArrayList<>();
    private List<Item> Leaf = new ArrayList<>();

    private List<Item> Axe1 = new ArrayList<>();
    private List<Item> Shovel1 = new ArrayList<>();
    private List<Item> Pick1 = new ArrayList<>();

    private List<Item> Sign = new ArrayList<>();
    private List<Item> Fence = new ArrayList<>();

    private List<Item> FenceParts = new ArrayList<>();

    private List<Rectangle> Crafts = new ArrayList<>();

    private int fontSize = 20;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    public Crafting(ESC engine, inventory invent) {
        eng = engine;
        inv = invent;
        craftInit();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    public void update() {
        if (!mouseDelay) {
            del++;
        }
        if (del >= maxDel) {
            mouseDelay = true;
            del = 0;
        }
        if (eng.inventory) {
            Crafts = inv.Crafts;
        }
        for (int i = 0; i < eng.world.hubRoom.obbys.size(); i++) {
            if (eng.world.hubRoom.obbys.get(i) instanceof WorkBench && eng.world.hubRoom.obbys.get(i).pop) {
                Crafts = eng.world.hubRoom.obbys.get(i).Crafts;
                break;
            }
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Workbench Graphics g int page">
    public void workbench(Graphics g, int page) {
        switch (page) {
            case 1: //tools
                craftBox(0, eng.assetLdr.emptyBucket, Bucket, 2, g);
                craftBox(1, eng.assetLdr.axe1, Axe1, 10, g, 1);
                craftBox(2, eng.assetLdr.shovel1, Shovel1, 11, g, 1);
                craftBox(3, eng.assetLdr.pick1, Pick1, 12, g, 1);
                break;
            case 2: //objects
                craftBox(0, eng.assetLdr.sign, Sign, 13, g);
                craftBox(1, eng.assetLdr.fence, Fence, 14, g);
                //fence
                break;
            case 3: //resources
                craftBox(0, eng.assetLdr.stick, Stick, 6, g);
                craftBox(1, eng.assetLdr.paper, Paper, 7, g);
                craftBox(2, eng.assetLdr.plank, Plank, 8, g);
//                craftBox(3, eng.assetLdr.leaf, Leaf, 9, g);
                break;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="logCraft Graphics g">
    public void logCraft(Graphics g) {
        craftBox(0, eng.assetLdr.campFire.get(eng.assetLdr.campFire.size() - 1), Fire, g, "Campfire");
        craftBox(1, eng.assetLdr.workBench, Bench, g, "Bench");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sapling Craft Graphics g">
    public void saplingCraft(Graphics g) {
        craftBox(0, eng.assetLdr.sapling, Tree, g, "Tree");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="fenceCraft Graphics g">
    public void fenceCraft(Graphics g) {
        for (int i = 0; i < eng.assetLdr.fenceParts.size(); i++) {
            craftBox(i, eng.assetLdr.fenceParts.get(i), FenceParts, g, "Fences");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftBox int number, image, cost, int newId, graphics">
    public void craftBox(int number, BufferedImage image, List<Item> cost, int newId, Graphics g) {
        boolean after = false;
        boolean qty = true;
        int lineOff = fontSize + 10;
        List<Integer> craftSlots = new ArrayList<>();
        int newIDSlot = -1;

//        g.setFont(eng.assetLdr.testing.deriveFont((float) fontSize));
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
        if (contains(Crafts.get(number))) {
            if (qty && craftSlots.size() == cost.size()) {
                g.setColor(new Color(255, 255, 255, 100));
                if (eng.left && mouseDelay) {
                    inv.itemAdd(newId, 1);
                    for (int i = 0; i < craftSlots.size(); i++) {
                        inv.itemRemove(cost.get(i).id, cost.get(i).qnty);
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
            g.fillRect(Crafts.get(number).x, Crafts.get(number).y, Crafts.get(number).width, Crafts.get(number).height);
        }
        g.setColor(Color.white);
        g.drawRect(Crafts.get(number).x, Crafts.get(number).y, Crafts.get(number).width, Crafts.get(number).height);

        g.drawImage(image, Crafts.get(number).x + 5, Crafts.get(number).y + 5, Crafts.get(number).height - 10, Crafts.get(number).height - 10, null);

        g.drawString(cost.get(0).NAMES[newId], Crafts.get(number).x + Crafts.get(number).height + 5, Crafts.get(number).y + lineOff);

        for (int i = 0; i < cost.size(); i++) {
            g.drawString("Cost: " + cost.get(i).NAMES[cost.get(i).id] + " " + cost.get(i).qnty, Crafts.get(number).x + Crafts.get(number).height + 5, Crafts.get(number).y + lineOff + lineOff * (i + 1));
        }

        if (after) {
            g.setColor(new Color(255, 0, 0, 150));
            g.fillRect(Crafts.get(number).x + 2, Crafts.get(number).y + 2, Crafts.get(number).width - 3, Crafts.get(number).height - 3);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftBox int number, image, cost, int newId, graphics in maxCount">
    public void craftBox(int number, BufferedImage image, List<Item> cost, int newId, Graphics g, int maxCount) {
        boolean after = false;
        boolean qty = true;
        boolean maxxed = false;
        int lineOff = fontSize + 10;
        List<Integer> craftSlots = new ArrayList<>();
        int newIDSlot = -1;

//        g.setFont(eng.assetLdr.testing.deriveFont((float) fontSize));
        for (int i = 0; i < inv.inven.size(); i++) {
            if (inv.inven.get(i).id == newId) {
                if (inv.inven.get(i).qnty >= maxCount) {
                    maxxed = true;
                }
            }
        }

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
        if (contains(Crafts.get(number))) {
            if (qty && craftSlots.size() == cost.size() && !maxxed) {
                g.setColor(new Color(255, 255, 255, 100));
                if (eng.left && mouseDelay) {
                    inv.itemAdd(newId, 1);
                    for (int i = 0; i < craftSlots.size(); i++) {
                        inv.itemRemove(cost.get(i).id, cost.get(i).qnty);
                    }
                    mouseDelay = false;
                }
            } else {
                after = true;
                if (eng.left && mouseDelay) {
                    eng.pop("Already have one", 0);
                    mouseDelay = false;
                }
            }

        } else {
            g.setColor(new Color(100, 100, 100, 150));
        }

        if (!after) {
            g.fillRect(Crafts.get(number).x, Crafts.get(number).y, Crafts.get(number).width, Crafts.get(number).height);
        }
        g.setColor(Color.white);
        g.drawRect(Crafts.get(number).x, Crafts.get(number).y, Crafts.get(number).width, Crafts.get(number).height);

        g.drawImage(image, Crafts.get(number).x + 5, Crafts.get(number).y + 5, Crafts.get(number).height - 10, Crafts.get(number).height - 10, null);

        g.drawString(cost.get(0).NAMES[newId], Crafts.get(number).x + Crafts.get(number).height + 5, Crafts.get(number).y + lineOff);

        for (int i = 0; i < cost.size(); i++) {
            g.drawString("Cost: " + cost.get(i).NAMES[cost.get(i).id] + " " + cost.get(i).qnty, Crafts.get(number).x + Crafts.get(number).height + 5, Crafts.get(number).y + lineOff + lineOff * (i + 1));
        }

        if (after) {
            g.setColor(new Color(255, 0, 0, 150));
            g.fillRect(Crafts.get(number).x + 2, Crafts.get(number).y + 2, Crafts.get(number).width - 3, Crafts.get(number).height - 3);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftBox int number, image, cost, int newId, graphics, specialMod">
    public void craftBox(int number, BufferedImage image, List<Item> cost, Graphics g, String specialMod) {
        boolean after = false;
        boolean qty = true;
        int lineOff = fontSize + 10;
        List<Integer> craftSlots = new ArrayList<>();

//        g.setFont(eng.assetLdr.testing.deriveFont((float) fontSize));
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
        if (contains(Crafts.get(number))) {
            if (qty) {
                g.setColor(new Color(255, 255, 255, 100));
                if (eng.left && mouseDelay) {
                    switch (specialMod) {
                        case "Campfire":
                            eng.world.hubRoom.obbys.add(new CampFire((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 5, eng));
                            inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                            eng.left = false;
                            eng.inventory = false;
                            break;
                        case "Bench":
                            eng.world.hubRoom.obbys.add(new WorkBench((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), eng));
                            inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                            eng.left = false;
                            eng.inventory = false;
                            break;
                        case "Tree":
                            if (new Random().nextInt(20) == 7) {
                                eng.world.hubRoom.obbys.add(new Tree(eng, (int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 1));
                            } else {
                                eng.world.hubRoom.obbys.add(new Tree(eng, (int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 0));
                            }
                            inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                            eng.left = false;
                            eng.inventory = false;
                            break;
                        case "Fences":
                            eng.world.hubRoom.obbys.add(new Fence((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), number, eng));
                            inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                            eng.left = false;
                            eng.inventory = false;
                            break;
                    }
                    for (int i = 0; i < craftSlots.size(); i++) {
                        inv.itemRemove(cost.get(i).id, cost.get(i).qnty);
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
            g.fillRect(Crafts.get(number).x, Crafts.get(number).y, Crafts.get(number).width, Crafts.get(number).height);
        }
        g.setColor(Color.white);
        g.drawRect(Crafts.get(number).x, Crafts.get(number).y, Crafts.get(number).width, Crafts.get(number).height);

        g.drawImage(image, Crafts.get(number).x + 5, Crafts.get(number).y + 5, Crafts.get(number).height - 10, Crafts.get(number).height - 10, null);

        g.drawString(specialMod, Crafts.get(number).x + Crafts.get(number).height + 5, Crafts.get(number).y + lineOff);

        for (int i = 0; i < cost.size(); i++) {
            g.drawString("Cost: " + cost.get(i).NAMES[cost.get(i).id] + " " + cost.get(i).qnty, Crafts.get(number).x + Crafts.get(number).height + 5, Crafts.get(number).y + lineOff + lineOff * (i + 1));
        }

        if (after) {
            g.setColor(new Color(255, 0, 0, 150));
            g.fillRect(Crafts.get(number).x + 2, Crafts.get(number).y + 2, Crafts.get(number).width - 3, Crafts.get(number).height - 3);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftInit">
    public void craftInit() { //should make a file to load these all in 

        Item[] bucketList = {new Item(0, 3)};
        Item[] fireList = {new Item(0, 7)};
        Item[] benchList = {new Item(0, 15)};
        Item[] treeList = {new Item(5, 1)};
        Item[] stickList = {new Item(0, 1)};

        Item[] paperList = {new Item(0, 1)};
        Item[] plankList = {new Item(0, 2)};
        Item[] leafList = {new Item(0, 1)};

        Item[] axe1List = {new Item(6, 2), new Item(1, 5)};
        Item[] shovel1List = {new Item(6, 2), new Item(1, 3)};
        Item[] pick1List = {new Item(6, 2), new Item(1, 5)};

        Item[] signList = {new Item(6, 2), new Item(8, 4)};
        Item[] fenceList = {new Item(0, 2), new Item(6, 4)};

        Item[] fencePartsList = {new Item(14, 1)};

        Bucket.addAll(Arrays.asList(bucketList));
        Fire.addAll(Arrays.asList(fireList));
        Bench.addAll(Arrays.asList(benchList));
        Tree.addAll(Arrays.asList(treeList));
        Stick.addAll(Arrays.asList(stickList));
        Paper.addAll(Arrays.asList(paperList));
        Plank.addAll(Arrays.asList(plankList));
        Leaf.addAll(Arrays.asList(leafList));

        Axe1.addAll(Arrays.asList(axe1List));
        Shovel1.addAll(Arrays.asList(shovel1List));
        Pick1.addAll(Arrays.asList(pick1List));

        Sign.addAll(Arrays.asList(signList));
        Fence.addAll(Arrays.asList(fenceList));
        FenceParts.addAll(Arrays.asList(fencePartsList));

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
