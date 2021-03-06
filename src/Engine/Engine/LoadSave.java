/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Engine;

import Engine.FileIO.FileUtils;
import Engine.Items.Support.Item;
import Engine.Items.Support.WorldObjects;
import Engine.Items.World.CampFire;
import Engine.Items.World.Fence;
import Engine.Items.World.Torch;
import Engine.Items.World.Tree;
import Engine.Items.World.WorkBench;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author DribbleWare
 */
public class LoadSave {

    public String Loc, Loc2;
    static List<String> inputs = new ArrayList<>();
    String[] saves;
    ESC engine;
    boolean ItemClear = false, invenClear = false, check = true;
    int size = 2, its = 0, ivn = 0;

    Path MASTERPATH = Paths.get(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Game" + File.separator + "Save");

    public LoadSave(ESC eng) {
        engine = eng;
    }

    public void init() {
        final String userHomeDirectory = System.getProperty("user.home");
        final Path playerSavePath = FileUtils.getFilePath(userHomeDirectory, "Documents", "Game", "Save", "Player");
        final Path playerItemsSavePath = FileUtils.getFilePath(userHomeDirectory, "Documents", "Game", "Save", "Items");
        File customDir = playerSavePath.toFile();
        File itemDir = playerItemsSavePath.toFile();

        File mainDir = FileUtils.getFilePath(playerSavePath.toString(), "Save.zbd").toFile();
        File hubDir = FileUtils.getFilePath(playerItemsSavePath.toString(), "hub_Items.zbd").toFile();
        File hub_Objects = FileUtils.getFilePath(playerItemsSavePath.toString(), "hub_Objects.zbd").toFile();
        File invenDir = FileUtils.getFilePath(playerSavePath.toString(), "Inventory.zbd").toFile();

//stats saving init
        if (customDir.exists() && mainDir.isFile()) {
            System.out.println(mainDir + " already exists");
            try {
                load(playerSavePath.toAbsolutePath().toString(), 0);
            } catch (IOException ex) {
                System.out.println("Failed to load");
            }
        } else if (customDir.mkdirs()) {
            System.out.println(customDir + " was created");
        } else {
            System.out.println(customDir + " save file added");
        }

//inventory saving
        if (customDir.exists() && invenDir.isFile()) {
            System.out.println(invenDir + " already exists");
            try {
                load(playerSavePath.toAbsolutePath().toString(), 2);
            } catch (IOException ex) {
                System.out.println("Failed to load");
            }
        } else if (customDir.mkdirs()) {
            System.out.println(customDir + " was created");
        } else {
            System.out.println(invenDir + " save file added");
        }

//hub items init        
        if (itemDir.exists() && hubDir.isFile()) {
            System.out.println(hubDir + " already exists");
            try {
                load(playerItemsSavePath.toAbsolutePath().toString(), 1);
            } catch (IOException ex) {
                System.out.println("Failed to load");
            }
        } else if (itemDir.mkdirs()) {
            System.out.println(itemDir + " was created");
            engine.world.hubRoom.addItems();
        } else {
            System.out.println(itemDir + " save file added");
            engine.world.hubRoom.addItems();
        }

        //tree savings
        if (itemDir.exists() && hub_Objects.isFile()) {
            System.out.println(hub_Objects + " already exists");
            try {
                load(playerItemsSavePath.toAbsolutePath().toString(), 3);
            } catch (IOException ex) {
                System.out.println("Failed to load");
            }
        } else if (itemDir.mkdirs()) {
            System.out.println(hub_Objects + " was created");
            engine.world.hubRoom.addItems();
        } else {
            System.out.println(hub_Objects + " save file added");
        }
        Loc = playerSavePath.toAbsolutePath().toString();
        Loc2 = playerItemsSavePath.toAbsolutePath().toString();

    }

    public void Save() throws FileNotFoundException, UnsupportedEncodingException {

        playerSave();
        inventorySave();
        itemSave();
        objectSave();

        System.out.println("saved");
    }

    public void load(String path, int id) throws IOException {

        //<editor-fold defaultstate="collapsed" desc="Player load">
        if (id == 0) { //player load
            Scanner scanner = new Scanner(new File(path + "/Save.zbd"));
            while (scanner.hasNext()) {
                if (scanner.hasNextLine()) {
                    inputs.add(scanner.nextLine());
                } else {
                    scanner.nextLine();
                }
            }

            engine.loadVars[0] = inputs.get(0);
            engine.place = inputs.get(1);
            engine.loadVars[1] = inputs.get(1);
            engine.loadVars[2] = inputs.get(2);
            engine.loadVars[3] = inputs.get(3);
            engine.setTime(Integer.parseInt(inputs.get(4)));
            engine.setDay(Integer.parseInt(inputs.get(5)));

            scanner.close();

        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="hub_Items load">
        if (id == 1) { //hub load
            int x = 0, y = 0, idd = 0, qnt = 0, health = 0;
            Scanner scanner = new Scanner(new File(path + "/hub_Items.zbd"));
            while (scanner.hasNext()) {
                if (scanner.hasNextLine()) {
                    if (ItemClear == false) {
                        engine.world.hubRoom.items.clear();
                        ItemClear = true;
                    }

                    String hold = scanner.nextLine();
                    String temp[] = hold.split("\\s+");
                    x = Integer.parseInt(temp[0]);
                    y = Integer.parseInt(temp[1]);
                    idd = Integer.parseInt(temp[2]);
                    qnt = Integer.parseInt(temp[3]);

                    if (temp.length > 4) {
                        health = Integer.parseInt(temp[4]);
                        engine.world.hubRoom.items.add(new Item(x, y, 50, 50, idd, qnt, health));
                    } else {
                        engine.world.hubRoom.items.add(new Item(x, y, 50, 50, idd, qnt));
                    }
                    //<editor-fold defaultstate="collapsed" desc="old">
//                    switch (its) {
//                        case 0:
//                            x = scanner.nextInt();
//                            its++;
//                            break;
//                        case 1:
//                            y = scanner.nextInt();
//                            its++;
//                            break;
//                        case 2:
//                            idd = scanner.nextInt();
//                            its++;
//                            break;
//                        case 3:
//                            qnt = scanner.nextInt();
//                            its = 0;
//                            engine.world.hubRoom.items.add(new Item(x, y, 50, 50, idd, qnt));
//                            check = false;
//                            break;
//                    }
                    //</editor-fold>
                } else {
                    scanner.nextLine();
                }
            }
            if (!scanner.hasNext()) {
                if (engine.mainChar.inv.inven.size() > 0) {
                    System.out.println("no new items");
                } else if (check) {
                    System.out.println("adding items");
                    engine.world.hubRoom.addItems();
                }
            }
            scanner.close();
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="INV load">
        if (id == 2) { //inventory load
            int i = 0, qty = 0, health = -1;
            Scanner scanner = new Scanner(new File(path + "/Inventory.zbd"));
            while (scanner.hasNext()) {
                if (scanner.hasNextLine()) {
                    if (invenClear == false) {
                        engine.mainChar.inv.inven.clear();
                        invenClear = true;
                    }

                    String hold = scanner.nextLine();
                    String temp[] = hold.split("\\s+");

                    i = Integer.parseInt(temp[0]);
                    qty = Integer.parseInt(temp[1]);
                    if (temp.length > 2) {
                        health = Integer.parseInt(temp[2]);
                        engine.mainChar.inv.inven.add(new Item(i, qty, health));
                    } else {
                        engine.mainChar.inv.inven.add(new Item(i, qty));
                    }
                } else {
                    scanner.nextLine();
                }
            }
            scanner.close();
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Objects load">
        if (id == 3) { //objects load
            int treeCount = 0, fireCount = 0, benchCount = 0, fenceCount = 0, torchCount = 0;
            int tX = 0, tY = 0, tID = 0, tC = 0, tic = 0; // trees
            int fX = 0, fY = 0, fT = 0; //fires
            int bX = 0, bY = 0; //workbenches
            int feX = 0, feY = 0, feId; //fences
            int toX = 0, toY = 0;
            boolean feC = true;
            String section = "BLANK", hold;
            Scanner scanner = new Scanner(new File(path + "/hub_Objects.zbd"));
            while (scanner.hasNext()) {
                if (scanner.hasNextLine()) {

                    hold = scanner.nextLine();
                    if (hold.equalsIgnoreCase("TREES")) {
                        section = "TREES";
                        hold = scanner.nextLine();
                    }
                    if (hold.equalsIgnoreCase("FIRES")) {
                        section = "FIRES";
                        hold = scanner.nextLine();
                    }
                    if (hold.equalsIgnoreCase("BENCHES")) {
                        section = "BENCHES";
                        hold = scanner.nextLine();
                    }
                    if (hold.equalsIgnoreCase("FENCES")) {
                        section = "FENCES";
                        hold = scanner.nextLine();
                    }
                    if (hold.equalsIgnoreCase("TORCHES")) {
                        section = "TORCHES";
                        hold = scanner.nextLine();
                    }

                    String temp[] = hold.split("\\s+");

//<editor-fold defaultstate="collapsed" desc="TREES">
                    if (section.equalsIgnoreCase("TREES")) {
                        tX = Integer.parseInt(temp[0]);
                        tY = Integer.parseInt(temp[1]);
                        tID = Integer.parseInt(temp[2]);
                        tC = Integer.parseInt(temp[3]);
                        tic = Integer.parseInt(temp[4]);

                        Tree tmp = new Tree(engine, tX, tY, tID);
                        tmp.count = tC;
                        tmp.actionTimer = tic;
                        tmp.dropped = true;
                        tmp.loading();
                        engine.world.hubRoom.obbys.add(tmp);
                        engine.world.updatelist();
                        treeCount++;
                    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FIRES">
                    if (section.equalsIgnoreCase("FIRES")) {
                        fX = Integer.parseInt(temp[0]);
                        fY = Integer.parseInt(temp[1]);
                        fT = Integer.parseInt(temp[2]);
                        CampFire placeHolder = new CampFire(fX, fY, fT, engine);
                        engine.world.hubRoom.obbys.add(placeHolder);
                        engine.world.updatelist();
                        placeHolder.dropped = true;
                        fireCount++;
                    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="BENCHES">
                    if (section.equalsIgnoreCase("BENCHES")) {
                        bX = Integer.parseInt(temp[0]);
                        bY = Integer.parseInt(temp[1]);
                        WorkBench placeHolder = new WorkBench(bX, bY, engine);
                        engine.world.hubRoom.obbys.add(placeHolder);
                        placeHolder.dropped = true;
                        benchCount++;
                    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Fences">
                    if (section.equalsIgnoreCase("Fences")) {
                        feX = Integer.parseInt(temp[0]);
                        feY = Integer.parseInt(temp[1]);
                        feId = Integer.parseInt(temp[2]);
                        feC = Boolean.parseBoolean(temp[3]);
                        Fence placeHolder = new Fence(feX, feY, feId, engine, feC);
                        engine.world.hubRoom.obbys.add(placeHolder);
                        placeHolder.dropped = true;
                        placeHolder.first = false;
                        fenceCount++;
                    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="TORCHES">
                    if (section.equalsIgnoreCase("TORCHES")) {
                        toX = Integer.parseInt(temp[0]);
                        toY = Integer.parseInt(temp[1]);
                        Torch placeHolder = new Torch(toX, toY, 100, engine);
                        placeHolder.dropped = true;
                        placeHolder.first = false;
                        engine.world.hubRoom.obbys.add(placeHolder);
                        torchCount++;
                    }
//</editor-fold>

                } else {
                    scanner.nextLine();
                }
            }
            System.out.println("Loaded " + treeCount + " trees");
            System.out.println("Loaded " + fireCount + " fires");
            System.out.println("Loaded " + benchCount + " benches");
            System.out.println("Loaded " + fenceCount + " fences");
            System.out.println("Loaded " + torchCount + " torches");

            engine.world.hubRoom.order();
            engine.world.updatelist();

            scanner.close();

        }
        //</editor-fold>

    }

    public void itemSave() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(Loc2 + "/hub_Items.zbd", "UTF-8");
        for (int i = 0; i < engine.world.hubRoom.items.size(); i++) {
            writer.println(engine.world.hubRoom.items.get(i).x + " " + engine.world.hubRoom.items.get(i).y + " " + engine.world.hubRoom.items.get(i).id + " " + engine.world.hubRoom.items.get(i).qnty + " "
                    + engine.world.hubRoom.items.get(i).health);
        }
        writer.close();
    }

    private void playerSave() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(Loc + "/Save.zbd", "UTF-8");
        writer.println(engine.health);
        if (!engine.Loc.equalsIgnoreCase("menu")) {
            writer.println(engine.Loc);
        } else {
            writer.println(engine.loadVars[1]);
        }
        writer.println(engine.xoff);
        writer.println(engine.yoff);
        writer.println(engine.getTime());
        writer.println(engine.getDay());
        writer.close();
    }

    private void objectSave() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(Loc2 + "/hub_Objects.zbd", "UTF-8");

        List<WorldObjects> obbys = engine.world.hubRoom.obbys;

        boolean trees = false, fires = false, benches = false, fences = false, torches = false;
        for (int i = 0; i < obbys.size(); i++) {
            if (obbys.get(i) instanceof Tree) {
                trees = true;
            }
            if (obbys.get(i) instanceof WorkBench) {
                benches = true;
            }
            if (obbys.get(i) instanceof CampFire) {
                fires = true;
            }
            if (obbys.get(i) instanceof Fence) {
                fences = true;
            }
            if (obbys.get(i) instanceof Torch) {
                torches = true;
            }
        }

        for (int j = 0; j < 5; j++) {
            boolean first = true;
            for (int i = 0; i < engine.world.hubRoom.obbys.size(); i++) {
                switch (j) {
                    case 0:
                        if (first && trees) {
                            writer.println("TREES");
                            first = false;
                        }
                        if (obbys.get(i) instanceof Tree) {
                            writer.println(obbys.get(i).x + " " + obbys.get(i).y + " " + obbys.get(i).ID + " " + obbys.get(i).count + " " + obbys.get(i).actionTimer);
                        }
                        break;
                    case 1:
                        if (first && fires) {
                            writer.println("FIRES");
                            first = false;
                        }
                        if (obbys.get(i) instanceof CampFire) {
                            writer.println(obbys.get(i).x + " " + obbys.get(i).y + " " + obbys.get(i).actionTimer);
                        }
                        break;
                    case 2:
                        if (first && benches) {
                            writer.println("BENCHES");
                            first = false;
                        }
                        if (obbys.get(i) instanceof WorkBench) {
                            writer.println(obbys.get(i).x + " " + obbys.get(i).y);
                        }
                        break;
                    case 3:
                        if (first && fences) {
                            writer.println("Fences");
                            first = false;
                        }
                        if (obbys.get(i) instanceof Fence) {
                            writer.println(obbys.get(i).x + " " + obbys.get(i).y + " " + obbys.get(i).ID + " " + obbys.get(i).changeable);
                        }
                        break;
                    case 4:
                        if (first && torches) {
                            writer.println("Torches");
                            first = false;
                        }
                        if (obbys.get(i) instanceof Torch) {
                            writer.println(obbys.get(i).x + " " + obbys.get(i).y);
                        }
                        break;
                }
            }
        }

        writer.close();
    }

    private void inventorySave() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(Loc + "/Inventory.zbd", "UTF-8");
        for (int i = 0; i < engine.mainChar.inv.inven.size(); i++) {
            writer.println(engine.mainChar.inv.inven.get(i).id + " " + engine.mainChar.inv.inven.get(i).qnty + " " + engine.mainChar.inv.inven.get(i).health);
        }
        writer.close();

    }

    public void reset() {
        String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Game" + File.separator + "Save" + File.separator + "Player";
        String path2 = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Game" + File.separator + "Save" + File.separator + "Items";

        File customDir = new File(path + File.separator + "/Save.zbd");
        File itemDir = new File(path2 + File.separator + "/hub_Items.zbd");
        File hub_Objects = new File(path2 + File.separator + "/hub_Objects.zbd");
        File invenDir = new File(path + File.separator + "/Inventory.zbd");
        System.out.println("resetting");
        try {

            System.out.println("1");
            Files.delete(customDir.toPath());
            System.out.println("2");
            Files.delete(itemDir.toPath());
            System.out.println("3");
            Files.delete(invenDir.toPath());
            System.out.println("4");
            Files.delete(hub_Objects.toPath());

            engine.restart();
            System.exit(0);

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
