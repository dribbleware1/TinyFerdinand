/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Player;

import Engine.Engine.ESC;
import Engine.FileIO.FileUtils;
import Engine.Items.Support.Item;
import Engine.Items.World.CampFire;
import Engine.Items.World.Fence;
import Engine.Items.World.Torch;
import Engine.Items.World.Tree;
import Engine.Items.World.WorkBench;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author DribbleWare shortened by 54 lines
 */
public class Crafting {

    //<editor-fold defaultstate="collapsed" desc="declarations">
    ESC eng;
    Inventory inv;
    boolean mouseDelay = true;
    int del = 0, maxDel = 15;

    HashMap<Integer, CraftObject[]> Recipies = new HashMap<Integer, CraftObject[]>();
    HashMap<Integer, CraftObject[]> BenchRecipies = new HashMap<Integer, CraftObject[]>();

    private List<Rectangle> Crafts = new ArrayList<>();

    private int fontSize = 20;

    int[] index = {0, 2, 3, 5};
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    public Crafting(ESC engine, Inventory invent) {
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
        for (int i = 0; i < eng.world.active.obbys.size(); i++) {
            if (eng.world.active.obbys.get(i) instanceof WorkBench && eng.world.active.obbys.get(i).pop) {
                Crafts = eng.world.active.obbys.get(i).Crafts;
                break;
            }
            if (eng.world.active.obbys.get(i) instanceof CampFire && eng.world.active.obbys.get(i).pop) {
                Crafts = eng.world.active.obbys.get(i).Crafts;
                break;
            }
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Campfire Graphics g">
    public void Campfire(Graphics g) {
        craftBox(0, new CraftObject(eng.assetLdr.crafting.get("Torch"), 15, new Item(6, 1), new Item(9, 3)), g);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Workbench Graphics g int page">
    public void workbench(Graphics g, int page) {
        if (BenchRecipies.get(page) != null) {
            for (int i = 0; i < BenchRecipies.get(page).length; i++) {
                craftBox(i, BenchRecipies.get(page)[i], g);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Inventory int id, Graphic g">
    public void Inventory(int id, Graphics g) {
        int a = 0;
        if (Recipies.get(id) != null) {
            inv.craftSetup(g, Recipies.get(id).length);
            for (int i = 0; i < Recipies.get(id).length; i++) {
                craftBox(i, Recipies.get(id)[i], g);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftBox int number, CraftObject item, graphics">
    public void craftBox(int number, CraftObject item, Graphics g) {
        boolean after = false;
        boolean qty = true;
        boolean maxxed = false;
        int lineOff = fontSize + 10;
        List<Integer> craftSlots = new ArrayList<>();

        for (int i = 0; i < item.cost.size(); i++) {
            for (int j = 0; j < inv.inven.size(); j++) {
                if (inv.inven.get(j).id == item.cost.get(i).id) {
                    craftSlots.add(j);
                }
                if (inv.inven.get(j).id == item.newId && item.hasMax) {
                    if (inv.inven.get(i).qnty >= item.maxCount) {
                        maxxed = true;
                    }
                }
            }
        }

        for (int i = 0; i < craftSlots.size(); i++) {
            if (inv.inven.get(craftSlots.get(i)).qnty < item.cost.get(i).qnty) {
                qty = false;
            }
        }
        if (craftSlots.size() < item.cost.size()) {
            qty = false;
        }
        if (contains(Crafts.get(number))) {
            if (qty && !maxxed) {
                g.setColor(new Color(255, 255, 255, 100));
                if (eng.left && mouseDelay) {
                    if (item.hasSpecial) {
                        switch (item.specialMod) {
                            case "Campfire":
                                CampFire placeHolderFire = new CampFire((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 120, eng);
                                placeHolderFire.costs = item.cost;
                                eng.world.active.obbys.add(placeHolderFire);
                                inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                                eng.left = false;
                                eng.inventory = false;
                                break;
                            case "Torch":
                                Torch placeHolderTorch = new Torch((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 120, eng);
                                placeHolderTorch.costs = item.cost;
                                eng.world.active.obbys.add(placeHolderTorch);
                                inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                                eng.left = false;
                                eng.inventory = false;
                                break;
                            case "Bench":
                                WorkBench placeHolderBench = new WorkBench((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), eng);
                                placeHolderBench.costs = item.cost;
                                eng.world.active.obbys.add(placeHolderBench);
                                inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                                eng.left = false;
                                eng.inventory = false;
                                break;
                            case "Tree":
                                Tree placeHolderTree;
                                if (new Random().nextInt(20) == 7) {
                                    placeHolderTree = new Tree(eng, (int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 1);
                                } else {
                                    placeHolderTree = new Tree(eng, (int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), 0);
                                }
                                placeHolderTree.costs = item.cost;
                                eng.world.active.obbys.add(placeHolderTree);
                                inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                                eng.left = false;
                                eng.inventory = false;
                                break;
                            case "Fence":
                                Fence placeHolderFence = new Fence((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), number, eng, true);
                                placeHolderFence.costs = item.cost;
                                eng.world.active.obbys.add(placeHolderFence);
                                inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                                eng.left = false;
                                eng.inventory = false;
                                break;
                            case "Static-Fence":
                                int fence = 0;
                                switch (number) {
                                    case 2:
                                        fence = 2;
                                        break;
                                    case 3:
                                        fence = 3;
                                        break;
                                    case 4:
                                        fence = 5;
                                        break;
                                }
                                Fence placeHolderStaticFence = new Fence((int) eng.mainChar.x - eng.getXOff(), (int) eng.mainChar.y - eng.getYOff(), fence, eng, false);
                                placeHolderStaticFence.costs = item.cost;
                                eng.world.active.obbys.add(placeHolderStaticFence);
                                inv.r.mouseMove((int) (eng.mainChar.x) + eng.getFrameX(), (int) (eng.mainChar.y) + eng.getFrameY());
                                eng.left = false;
                                eng.inventory = false;
                                break;
                        }
                    } else {
                        if (item.newId == 10 || item.newId == 11 || item.newId == 12) {
                            inv.itemAdd(item.newId, 1);
                        } else {
                            inv.itemAdd(item.newId, 1);
                        }
                    }
                    for (int i = 0; i < craftSlots.size(); i++) {
                        inv.itemRemove(item.cost.get(i).id, item.cost.get(i).qnty);
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

        g.drawImage(item.image, Crafts.get(number).x + 5, Crafts.get(number).y + 5, Crafts.get(number).height - 10, Crafts.get(number).height - 10, null);

        if (item.hasSpecial) {
            g.drawString(item.specialMod, Crafts.get(number).x + Crafts.get(number).height + 5, Crafts.get(number).y + lineOff);
        } else {
            g.drawString(item.cost.get(0).NAMES[item.newId], Crafts.get(number).x + Crafts.get(number).height + 5, Crafts.get(number).y + lineOff);
        }

        for (int i = 0; i < item.cost.size(); i++) {
            g.drawString("Cost: " + item.cost.get(i).NAMES[item.cost.get(i).id] + " " + item.cost.get(i).qnty, Crafts.get(number).x + Crafts.get(number).height + 5, Crafts.get(number).y + lineOff + lineOff * (i + 1));
        }

        if (after) {
            g.setColor(new Color(255, 0, 0, 150));
            g.fillRect(Crafts.get(number).x + 2, Crafts.get(number).y + 2, Crafts.get(number).width - 3, Crafts.get(number).height - 3);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftInit">
    public void craftInit() { //should make a file to load these all in 
        List<CraftObject> objectList = new ArrayList<>(50);
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FileUtils.getFilePathFromRoot("Res", "Crafting", "Crafts.zbd").toFile()));
            line = reader.readLine();
            while (line != null) {
                String[] blocks = line.split("//");
                for (int j = 0; j < blocks.length; j++) {
                    String[] buff = blocks[j].split(" ");
                    CraftObject holder = new CraftObject(eng.assetLdr.crafting.get(buff[0]), Integer.parseInt(buff[1]), Integer.parseInt(buff[2]), buff[3], new Item(Integer.parseInt(buff[4]), Integer.parseInt(buff[5])));

                    if (buff.length > 7) {
                        for (int k = 6; k < buff.length - 1; k += 2) {
                            holder.addCost(new Item(Integer.parseInt(buff[k]), Integer.parseInt(buff[k + 1])));
                        }
                    }
                    objectList.add(j, holder);
                }
                String[] endTag = line.split("ID");
                Recipies.put(Integer.parseInt(endTag[1]), objectList.toArray(new CraftObject[objectList.size()]));
                objectList.clear();
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Reader Crashed");
        }

        objectList.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FileUtils.getFilePathFromRoot("Res", "Crafting", "BenchCrafts.zbd").toFile()));
            line = reader.readLine();
            while (line != null) {
                String[] blocks = line.split("//");
                for (int j = 0; j < blocks.length; j++) {
                    String[] buff = blocks[j].split(" ");
                    CraftObject holder = new CraftObject(eng.assetLdr.crafting.get(buff[0]), Integer.parseInt(buff[1]), Integer.parseInt(buff[2]), buff[3], new Item(Integer.parseInt(buff[4]), Integer.parseInt(buff[5])));
                    if (buff.length > 7) {
                        for (int k = 6; k < buff.length - 1; k += 2) {
                            holder.addCost(new Item(Integer.parseInt(buff[k]), Integer.parseInt(buff[k + 1])));
                        }
                    }
                    objectList.add(holder);
                }
                String[] endTag = line.split("ID");
                BenchRecipies.put(Integer.parseInt(endTag[1]), objectList.toArray(new CraftObject[objectList.size()]));
                objectList.clear();
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Reader Crashed");
        }
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

class CraftObject {

    //<editor-fold defaultstate="collapsed" desc="CraftObject class">
    BufferedImage image;
    int newId;
    List<Item> cost;

    int maxCount;
    String specialMod;

    boolean hasMax = false, hasSpecial = false;

    public CraftObject(BufferedImage img, int ID, int max, String special, Item... costs) {
        image = img;
        cost = new ArrayList<Item>(Arrays.asList(costs));

        if (!special.equalsIgnoreCase("null")) {
            specialMod = special;
            hasSpecial = true;
        } else {
            newId = ID;
        }
        if (max != -1) {
            maxCount = max;
            hasMax = true;
        }

    }

    public CraftObject(BufferedImage img, int ID, Item... costs) {
        image = img;
        newId = ID;
        cost = Arrays.asList(costs);
    }

    public CraftObject(BufferedImage img, int ID, int max, Item... costs) {
        image = img;
        newId = ID;
        cost = Arrays.asList(costs);

        maxCount = max;
        hasMax = true;
    }

    public CraftObject(BufferedImage img, String special, Item... costs) {
        image = img;
        cost = Arrays.asList(costs);

        specialMod = special;
        hasSpecial = true;
    }

    public boolean hasMax() {
        return hasMax;
    }

    public boolean hasSpecial() {
        return hasSpecial;
    }

    public void addCost(Item item) {
        cost.add(item);
    }
    //</editor-fold>

}
