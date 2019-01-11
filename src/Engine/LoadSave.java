/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author gjudge9427
 */
//add player inventory save;
public class LoadSave {

    public String Loc, Loc2;
    static List<String> inputs = new ArrayList<>();
    String[] saves;
    ESC engine;
    boolean ItemClear = false, invenClear = false;
    int size = 2, its = 0, ivn = 0;

    Path MASTERPATH = Paths.get(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Game" + File.separator + "Save");

    public LoadSave(ESC eng) {
        engine = eng;
    }

    public void init() {
        String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Game" + File.separator + "Save" + File.separator + "Player";
        String path2 = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Game" + File.separator + "Save" + File.separator + "Items";
        File customDir = new File(path);
        File itemDir = new File(path2);
        File customDir2 = new File(path + File.separator + "/Save.zbd");
        File itemDir2 = new File(path2 + File.separator + "/Hub.zbd");
        File invenDir = new File(path + File.separator + "/Inventory.zbd");

//stats saving init
        if (customDir.exists() && customDir2.isFile()) {
            System.out.println(customDir2 + " already exists");
            try {
                load(path, 0);
            } catch (IOException ex) {
                System.out.println("Failed to load");
            }
        } else if (customDir.mkdirs()) {
            System.out.println(customDir + " was created");
            try {
                newSave(path, 0);
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                System.out.println("file not found");
            }
        } else {
            System.out.println(customDir + " save file added");
        }

//inventory saving
        if (customDir.exists() && invenDir.isFile()) {
            System.out.println(invenDir + " already exists");
            try {
                load(path, 2);
            } catch (IOException ex) {
                System.out.println("Failed to load");
            }
        } else if (customDir.mkdirs()) {
            System.out.println(customDir + " was created");
            try {
                newSave(path, 2);
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                System.out.println("file not found");
            }
        } else {
            System.out.println(invenDir + " save file added");
        }

//hub items init        
        if (itemDir.exists() && itemDir2.isFile()) {
            System.out.println(itemDir2 + " already exists");
            try {
                load(path2, 1);
            } catch (IOException ex) {
                System.out.println("Failed to load");
            }
        } else if (itemDir.mkdirs()) {
            System.out.println(itemDir + " was created");
            engine.world.hubRoom.addItems();
            try {
                newSave(path2, 1);
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                System.out.println("file not found");
            }
        } else {
            System.out.println(itemDir + " save file added");
            engine.world.hubRoom.addItems();
        }

        Loc = path;
        Loc2 = path2;

    }

    public void newSave(String path, int id) throws FileNotFoundException, UnsupportedEncodingException {
        if (id == 0) {
            PrintWriter writer = new PrintWriter(path + "/Save.zbd", "UTF-8");
            writer.close();
        }
        if (id == 1) {
            PrintWriter writer = new PrintWriter(path + "/Hub.zbd", "UTF-8");
            writer.close();
        }
        if (id == 2) {
            PrintWriter writer = new PrintWriter(path + "/Inventory.zbd", "UTF-8");
            writer.close();
        }

    }

    public void Save() throws FileNotFoundException, UnsupportedEncodingException {

        playerSave();
        inventorySave();
        itemSave();

        System.out.println("saved");
    }

    public void load(String path, int id) throws IOException {
        if (id == 0) { //player load
            Scanner scanner = new Scanner(new File(path + "/Save.zbd"));
            while (scanner.hasNext()) {
                if (scanner.hasNextLine()) {
                    inputs.add(scanner.nextLine());
                } else {
                    scanner.nextLine();
                }
            }
            fill();
        }
        if (id == 1) { //hub load
            int x = 0, y = 0, idd = 0, qnt = 0;
            Scanner scanner = new Scanner(new File(path + "/Hub.zbd"));
            while (scanner.hasNext()) {
                if (scanner.hasNextLine()) {
                    if (ItemClear == false) {
                        engine.world.hubRoom.items.clear();
                        ItemClear = true;
                    }
                    switch (its) {
                        case 0:
                            x = scanner.nextInt();
                            its++;
                            break;
                        case 1:
                            y = scanner.nextInt();
                            its++;
                            break;
                        case 2:
                            idd = scanner.nextInt();
                            its++;
                            break;
                        case 3:
                            qnt = scanner.nextInt();
                            its = 0;
                            engine.world.hubRoom.items.add(new Item(x, y, 50, 50, idd, qnt));
                            engine.world.items = engine.world.hubRoom.items;
                            break;
                    }
                } else {
                    scanner.nextLine();
                }
            }
            if (!scanner.hasNext()) {
                if (engine.mainChar.inv.inven.size() > 0) {
                    System.out.println("no new items");
                } else {
                    System.out.println("adding items");
                    engine.world.hubRoom.addItems();
                }
            }
        }

        if (id == 2) { //inventory load
            int i = 0, qty = 0;
            Scanner scanner = new Scanner(new File(path + "/Inventory.zbd"));
            while (scanner.hasNext()) {
                if (scanner.hasNextLine()) {
                    if (invenClear == false) {
                        engine.mainChar.inv.inven.clear();
                        invenClear = true;
                    }
                    switch (ivn) {
                        case 0:
                            i = scanner.nextInt();
                            ivn++;
                            break;
                        case 1:
                            qty = scanner.nextInt();
                            engine.mainChar.inv.inven.add(new Item(0, 0, 0, 0, i, qty));
                            ivn = 0;
                            break;
                    }
                } else {
                    scanner.nextLine();
                }
            }
        }
    }

    public void fill() {
        for (int i = 0; i < inputs.size(); i++) {
            engine.loadVars[i] = inputs.get(i);
        }
        if (inputs.size() != engine.loadVars.length) {
            for (int i = 0; i < engine.loadVars.length; i++) {
                if (engine.loadVars[i].equals(" ")) {
                    engine.loadVars[i] = "oops";
                }
            }
        }
        System.out.println("Loaded values");
        for (int i = 0; i < engine.loadVars.length; i++) {
            System.out.println(engine.loadVars[i]);
        }
        System.out.println("\n \n");
    }

    private void itemSave() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(Loc2 + "/Hub.zbd", "UTF-8");
        for (int i = 0; i < engine.world.items.size(); i++) {
            writer.println(engine.world.items.get(i).x + " " + engine.world.items.get(i).y + " " + engine.world.items.get(i).id + " " + engine.world.items.get(i).qnty);
        }
        writer.close();
    }

    private void playerSave() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(Loc + "/Save.zbd", "UTF-8");
        writer.println(engine.health);
        writer.println(engine.name);
        writer.println(engine.xoff);
        writer.println(engine.yoff);
        writer.close();
    }

    private void inventorySave() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(Loc + "/Inventory.zbd", "UTF-8");
        for (int i = 0; i < engine.mainChar.inv.inven.size(); i++) {
            writer.println(engine.mainChar.inv.inven.get(i).id + " " + engine.mainChar.inv.inven.get(i).qnty);
        }
        writer.close();

    }

    public void reset() {
        String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Game" + File.separator + "Save" + File.separator + "Player";
        String path2 = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Game" + File.separator + "Save" + File.separator + "Items";
        File customDir = new File(path);
        File itemDir = new File(path2);
        File customDir2 = new File(path + File.separator + "/Save.zbd");
        File itemDir2 = new File(path2 + File.separator + "/Hub.zbd");
        File invenDir = new File(path + File.separator + "/Inventory.zbd");
        System.out.println("resetting");
        try {
            System.out.println("here");
            Files.delete(Paths.get(customDir2.toString()));
            System.out.println("hey");
            Files.delete(Paths.get(itemDir2.toString()));
            System.out.println("yup");
            Files.delete(Paths.get(invenDir.toString()));

            System.out.println("reset successful!");

            engine.restart();
            System.exit(0);

        } catch (IOException ex) {
            System.out.println("Reset failed");
        }
    }
}
