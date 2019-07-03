/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Player;

import Engine.Engine.ESC;
import Engine.Items.Support.Item;
import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare shortend by 270 lines
 */
public class Inventory {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    ESC eng;

    //Text font
    public List<Item> inven = new ArrayList<>();
    public List<Rectangle> Crafts = new ArrayList<>();

    public Font text = new Font("TimesRoman", Font.PLAIN, 25), text2 = new Font("Helvetica", Font.BOLD, 32);

    int drop = 0;
    int dropMax = 15;
    int fireTime = 0;
    boolean dropStart = false, refresh = true;

    public Item itemRef = new Item(-100, 0);

    public String logLine = "Wooden logs, most likely taken form a tree or other \ntall leafy thing perhaps...";
    public String rockLine = "Its a pile of rocks, not sure what else you want me \nto say about it...";
    public String bucketLine = "Empty bucket, you could fill it with water, you \ncould put it on youre head... I dont know man, \nfigure it out";
    public String waterLine = "A splish splash i was taking a bath...";
    public String appleLine = "Apples, hopefully theres no worms in them...";
    public String saplingLine = "Yep its just a small tree... some leaves\nsome twigs you know, nothing special";

    public String stickLine = "Yep its just a small log...\nbasically just an oversized twig";
    public String paperLine = "Its very thin and doesnt do well with \nthe water so id definatly avoid that";
    public String plankLine = "Its like a log but its flat... thats all \nthere is to say i think";
    public String leafLine = "Fell off a tree no idea what you could do with it";

    public String axe1Line = "First tier axe. Useful for cutting wood\nCan only carry one at a time\nIt will give 2x wood until its broken";
    public String shovel1Line = "First tier shovel. Useful for digging \nponds and making path\nCan only carry one at a time";
    public String pick1Line = "First tier pickaxe. Useful for digging and removing \nstone based items\nCan only carry one at a time";

    public String signLine = "A little sign you could leave a message on";
    public String fenceLine = "Small wooden fence, used to keep animals\n and people out";

    public String torchLine = "Basically just a little campfire.\nWill be helpFul to provide light";

    public String helpLine = "Hello and welcome to the inventory help page\nyou can use the navigation buttons on the top\n"
            + "right of this window to switch between pages on this\nitem and close the window. You can close the \ninventory anytime by pressing E";

    public String[] Lines = {logLine, rockLine, bucketLine, waterLine, appleLine, saplingLine, stickLine, paperLine, plankLine, leafLine, axe1Line, shovel1Line, pick1Line, signLine, fenceLine, torchLine};

    //Key location points for inventory slot text
    public int textY = 180;
    private int textXOff = 78;
    private int offset = 15;

    public int slotsx = 6, slotsy = 6;

    Rectangle slotmenu = new Rectangle(750, 50, 650, 350 + 30);

    public boolean pop = false;

    public int pops = 0;
    public int page = 1;

    private boolean Visible = false;

    Rectangle[] slots;

    int notchHold;

    int pickUpY = 675;

    int slotMax = slotsx * slotsy;

    private Rectangle dropBox;
    private Rectangle dropOneBox;

    Color button = new Color(255, 115, 5);
    int y = 75;

    public Crafting crafter;

    public Robot r;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructer">
    public Inventory(ESC engine) {
        eng = engine;
        itemRef.artBuild();
        slots = new Rectangle[slotMax];
        slotsAdd();

        try {
            r = new Robot();
        } catch (AWTException ex) {
            System.out.println("uh oh robot died");
        }

        crafter = new Crafting(eng, this);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Update">
    public void update() {
        if (eng.inventory) {
            Visible = true;
            crafter.update();
        } else {
            Visible = false;
        }
        if (dropStart) {
            drop++;
        }
        if (drop > dropMax) {
            dropStart = false;
        }
        if (!eng.inventory) {
            pop = false;
        }
        if (fireTime < 11) {
            fireTime++;
        }
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="Render Graphics g">
    public void render(Graphics g) {

        g.setFont(text);
        g.getFontMetrics(text);

        if (Visible) {
            //draw background box
            g.setColor(new Color(10, 10, 10, 150));
            g.fillRect(0, 50, eng.sizew - (100 + eng.sizew / 2), eng.sizeh - 100);
            g.setColor(new Color(200, 200, 200, 200));
            Graphics2D g2d = (Graphics2D) g;
            BasicStroke bs = new BasicStroke(3);
            g2d.setStroke(bs);
            g2d.drawRect(0, 50, eng.sizew - (100 + eng.sizew / 2), eng.sizeh - 100);

            itemRender(g);

            if (inven.size() > 0 && pops < inven.size()) {
                if (pop) {
                    menuPop(g, inven.get(pops).id);
                }
            }
        }
        //Drawing the pick up prompt when overtop of an item might get moved to a better location.  
        g.setColor(new Color(255, 255, 255));
        if (eng.mainChar.overIt == true && !eng.pause) {
            g.drawString("Press Left Mouse To Pick Up", 555, eng.sizeh - 30);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="itemRender Graphics g">
    public void itemRender(Graphics g) {

        if (inven.size() > 0) {
            for (int i = 0; i < inven.size(); i++) {
                drawItemBox(slots[i].x, slots[i].y, g);
                if (contains(slots[i])) {
                    g.drawImage(itemRef.art[inven.get(i).id], slots[i].x, slots[i].y - 5, slots[i].width, slots[i].width, null);
                } else {
                    g.drawImage(itemRef.art[inven.get(i).id], slots[i].x, slots[i].y, slots[i].width, slots[i].width, null);
                }
                if (inven.get(i).health >= 0) {
                    g.setColor(Color.green);
                    g.fillRect(slots[i].x + 5, slots[i].y + 5, (int) (inven.get(i).health * 0.9), 10);
                }
                numberAlingment(g, i);
                if (contains(slots[i]) && eng.left) {
                    pop = true;
                    pops = i;
                    page = 1;
                }
            }
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Menu pop Graphics g">
    public void menuPop(Graphics g, int id) {

        g.setColor(new Color(10, 10, 10, 150));
        g.fillRect(slotmenu.x, slotmenu.y, slotmenu.width, slotmenu.height);
        g.setColor(new Color(200, 200, 200, 200));
        g.fillRect(slotmenu.x, slotmenu.y, slotmenu.width, 30);
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(3);
        g2d.setStroke(bs);
        g2d.drawRect(slotmenu.x, slotmenu.y, slotmenu.width, slotmenu.height);

        menuBarButtons(g, g2d);

        if (page != 2) {
            drawItemBox(slotmenu.x + 20, slotmenu.y + 50, g);
            g.drawImage(itemRef.art[inven.get(pops).id], slotmenu.x + 20, slotmenu.y + 50, 100, 100, null);
        }

        g.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 25));
        g.setColor(Color.white);
        g.drawString(itemRef.NAMES[inven.get(pops).id], slotmenu.x + 5, slotmenu.y + 25);

        y = 75;

        switch (page) {
            case 1:
                basicHome(g);
                g.setColor(Color.white);
                g.setFont(new Font("Courier", Font.BOLD, 20));
                for (String text : Lines[id].split("\n")) {
                    g.drawString(text, slotmenu.x + 130, slotmenu.y + (y += 20));
                }
                break;
            case 2:
                crafter.Inventory(id, g);
                break;
            case 3:
                helpPage(g);
                break;
        }
        g2d.setClip(0, 0, eng.sizew, eng.sizeh);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="menuBarButtons Graphics g">
    void menuBarButtons(Graphics g, Graphics2D g2d) {
        g.setColor(new Color(255, 255, 255, 200));

        //seperating lines
        g2d.drawLine(slotmenu.x + slotmenu.width - text.getSize() - 8, slotmenu.y + 8,
                slotmenu.x + slotmenu.width - text.getSize() - 8, slotmenu.y + 22);

        g2d.drawLine(slotmenu.x + slotmenu.width - text.getSize() - 8 - 30, slotmenu.y + 8,
                slotmenu.x + slotmenu.width - text.getSize() - 8 - 30, slotmenu.y + 22);

        g2d.drawLine(slotmenu.x + slotmenu.width - text.getSize() - 8 - 60, slotmenu.y + 8,
                slotmenu.x + slotmenu.width - text.getSize() - 8 - 60, slotmenu.y + 22);

        g2d.drawLine(slotmenu.x + slotmenu.width - text.getSize() - 8 - 90, slotmenu.y + 8,
                slotmenu.x + slotmenu.width - text.getSize() - 8 - 90, slotmenu.y + 22);

        //interactive buttons
        Rectangle home = new Rectangle(slotmenu.x + slotmenu.width - text.getSize() - 4 - 90, slotmenu.y + 2, 26, 26);
        if (contains(home) || page == 1) {
            g.drawImage(eng.assetLdr.home, slotmenu.x + slotmenu.width - text.getSize() - 4 - 90, slotmenu.y + 4, (eng.assetLdr.home.getWidth() / 8) - 2, eng.assetLdr.home.getHeight() / 8, null);
            if (eng.left) {
                page = 1;
            }
        } else {
            g.drawImage(eng.assetLdr.home2, slotmenu.x + slotmenu.width - text.getSize() - 4 - 90, slotmenu.y + 4, (eng.assetLdr.home.getWidth() / 8) - 2, eng.assetLdr.home.getHeight() / 8, null);
        }

        Rectangle craft = new Rectangle(slotmenu.x + slotmenu.width - text.getSize() - 4 - 60, slotmenu.y + 2, 26, 26);
        if (contains(craft) || page == 2) {
            g.drawImage(eng.assetLdr.craft, slotmenu.x + slotmenu.width - text.getSize() - 4 - 60, slotmenu.y + 4, (eng.assetLdr.craft.getWidth() / 7) + 2, eng.assetLdr.craft.getHeight() / 6, null);
            if (eng.left && page != 2) {
                page = 2;
                eng.notches = 0;
            }
        } else {
            g.drawImage(eng.assetLdr.craft2, slotmenu.x + slotmenu.width - text.getSize() - 4 - 60, slotmenu.y + 4, (eng.assetLdr.craft.getWidth() / 7) + 2, eng.assetLdr.craft.getHeight() / 6, null);
        }

        Font text3 = new Font("Helvetica", Font.BOLD, 28);
        g.setFont(text3);
        Rectangle question = new Rectangle(slotmenu.x + slotmenu.width - text.getSize() - 4 - 30, slotmenu.y + 2, 26, 26);
        if (contains(question) || page == 3) {
            g.setColor(Color.white);
            g.drawString("?", slotmenu.x + slotmenu.width - text.getSize() - 30, slotmenu.y + 24);
            if (eng.left) {
                page = 3;
            }

        } else {
            g.setColor(new Color(200, 200, 200, 200));
            g.drawString("?", slotmenu.x + slotmenu.width - text.getSize() - 30, slotmenu.y + 24);
        }

        g.setFont(text2);
        Rectangle close = new Rectangle(slotmenu.x + slotmenu.width - text.getSize() - 4, slotmenu.y + 2, 26, 26);
        if (!contains(close)) {
            g.setColor(Color.red);
            g.drawString("x", slotmenu.x + slotmenu.width - text.getSize(), slotmenu.y + 24);

        } else {
            g.setColor(Color.red);
            g.fillRect(close.x, close.y, close.width, close.height);
            g.setColor(Color.white);
            g.drawString("x", slotmenu.x + slotmenu.width - text.getSize(), slotmenu.y + 24);
            if (eng.left) {
                pop = false;
                page = 1;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="basicHome Graphics g">
    void basicHome(Graphics g
    ) {
        g.setFont(new Font("Courier", Font.BOLD, 25));
        g.setColor(Color.white);
        g.drawString("Amount: " + Integer.toString(inven.get(pops).qnty), slotmenu.x + 130, slotmenu.y + 70);

        Rectangle Dropx1Box = new Rectangle(slotmenu.x + 20, slotmenu.y + 170, 120, 40);
        g.setFont(new Font("Courier", Font.BOLD, 25));
        if (!contains(Dropx1Box)) {
            g.setColor(new Color(200, 200, 200, 200));
            g.drawString("Drop x 1", Dropx1Box.x + 5, Dropx1Box.y + 30);
        } else {
            g.setColor(button);
            g.fillRect(Dropx1Box.x + 2, Dropx1Box.y + 2, Dropx1Box.width - 3, Dropx1Box.height - 3);
            g.setColor(Color.white);
            g.drawString("Drop x 1", Dropx1Box.x + 5, Dropx1Box.y + 30);
            if (eng.left) {
                dropItem(1, pops);
            }
        }
        g.drawRect(Dropx1Box.x, Dropx1Box.y, Dropx1Box.width, Dropx1Box.height);

        Rectangle DropallBox = new Rectangle(slotmenu.x + 20, slotmenu.y + 220, 120, 40);
        g.setFont(new Font("Courier", Font.BOLD, 25));
        if (!contains(DropallBox)) {
            g.setColor(new Color(200, 200, 200, 200));
            g.drawString("Drop all", DropallBox.x + 5, DropallBox.y + 30);
        } else {
            g.setColor(button);
            g.fillRect(DropallBox.x + 2, DropallBox.y + 2, DropallBox.width - 3, DropallBox.height - 3);
            g.setColor(Color.white);
            g.drawString("Drop all", DropallBox.x + 5, DropallBox.y + 30);
            if (eng.left) {
                dropItem(inven.get(pops).qnty, pops);
            }
        }
        g.drawRect(DropallBox.x, DropallBox.y, DropallBox.width, DropallBox.height);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="helpPage Graphics g">
    void helpPage(Graphics g
    ) {

        g.setColor(Color.white);
        g.setFont(new Font("Courier", Font.BOLD, 20));
        for (String text : helpLine.split("\n")) {
            g.drawString(text, slotmenu.x + 130, slotmenu.y + (y += 20));
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="craftSetup Graphics g int num">
    void craftSetup(Graphics g, int num) {
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(3);
        g2d.setStroke(bs);
        g2d.setColor(Color.red);
        g2d.setClip(new Rectangle(slotmenu.x, slotmenu.y + 30, slotmenu.width, slotmenu.height - 30)); // stops from rendering outside of box

        Crafts.clear();
        boolean side = true;
        int layer = 0;
        for (int i = 0; i < num; i++) {
            if (side) {
                Crafts.add(new Rectangle(slotmenu.x + 5, (slotmenu.y + eng.notches + 35 + (120 * layer)), 310, 110));
                side = false;
            } else {
                Crafts.add(new Rectangle(slotmenu.x + 5 + 310 + 20, (slotmenu.y + 35 + (120 * layer)) + eng.notches, 310, 110));
                side = true;
                layer++;
            }
        }

        if ((Crafts.get(num - 1).y + Crafts.get(num - 1).height) + 10 < (slotmenu.y + slotmenu.height + eng.scrollSens)) {//stops from scrolling up past the last element
            eng.downNotching = false;
        } else {
            eng.downNotching = true;
        }

        if (Crafts.get(0).y + eng.notches + 10 > slotmenu.y + 30 + eng.scrollSens) {
            eng.upNotching = false;
        } else {
            eng.upNotching = true;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawItemBox Graphics g">
    void drawItemBox(int x, int y, Graphics g) {
        g.setColor(new Color(100, 100, 100, 200));
        g.fillRect(x, y, slots[0].width, 100);
        g.setColor(new Color(200, 200, 200, 200));
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(3);
        g2d.setStroke(bs);
        g2d.drawRect(x, y, slots[0].width, slots[0].height);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="numberAlingment Graphics g">
    public void numberAlingment(Graphics g, int index) {
        g.setColor(Color.white);
        if (inven.get(index).qnty > 99) {//for 3 digit number alingment
            g.drawString(Integer.toString(inven.get(index).qnty), slots[index].x - offset * 2 + textXOff, slots[index].y + 90);
        } else if (inven.get(index).qnty > 9) {// for 2 digit number alingment
            g.drawString(Integer.toString(inven.get(index).qnty), slots[index].x - offset + textXOff, slots[index].y + 90);
        } else {//single digit number alingment
            g.drawString(Integer.toString(inven.get(index).qnty), slots[index].x + textXOff, slots[index].y + 90);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dropItem">
    public void dropItem(int q, int id) {
        Item ref = new Item(0, 0);
        if (!dropStart) {
            eng.world.active.items.add(new Item((int) eng.mainChar.x - Integer.parseInt(eng.xoff), (int) eng.mainChar.y - Integer.parseInt(eng.yoff) + 30, 50, 50, inven.get(id).id, q));
            dropStart = true;
            drop = 0;
            inven.get(pops).qnty -= q;
            if (ref.NAMES[inven.get(id).id].endsWith("s") || q == 1) {
                eng.pop("-" + q + " " + ref.NAMES[inven.get(id).id], 0);
            } else {
                eng.pop("-" + q + " " + ref.NAMES[inven.get(id).id] + "s", 0);
            }
        }
        if (inven.get(id).qnty <= 0) {
            inven.remove(id);
            pop = false;
            page = 1;
        }

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hasAxe">
    public boolean hasAxe() {
        boolean ret = false;
        for (int i = 0; i < inven.size(); i++) {
            if (inven.get(i).id == 10) {
                ret = true;
                break;
            } else {
                ret = false;
            }
        }
        return ret;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="damageTool String name">
    public void damageTool(String name, int amount) {
        int id = -1;

        if (name.equalsIgnoreCase("axe")) {
            id = 10;
        } else if (name.equalsIgnoreCase("shovel")) {
            id = 11;
        } else if (name.equalsIgnoreCase("pick")) {
            id = 12;
        }
        for (int i = 0; i < inven.size(); i++) {
            if (inven.get(i).id == id) {
                inven.get(i).health -= amount;
                if (inven.get(i).health <= 0) {
                    inven.remove(i);
                }
            }
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

    //<editor-fold defaultstate="collapsed" desc="Loc ESC engine">
    public Point loc(ESC engine) {
        return new Point(MouseInfo.getPointerInfo().getLocation().x - engine.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - engine.frame.getY() - Math.abs(engine.frame.getLocationOnScreen().y - engine.canvas.getLocationOnScreen().y));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ItemReplace int idAdd int idRemove int qnt">
    public void itemReplace(int idAdd, int idRemove, int qnt) {
        boolean add = false;
        int addid = -1;
        boolean remove = false;
        int removeid = -1;
        boolean destroy = false;
        int destroyid = -1;

        for (int i = 0; i < inven.size(); i++) {
            if (inven.get(i).id == idAdd) {
                add = true;
                addid = i;
            }
            if (inven.get(i).id == idRemove && inven.get(i).qnty > 1) {
                remove = true;
                removeid = i;
            }
            if (inven.get(i).id == idRemove && inven.get(i).qnty <= 1) {
                destroy = true;
                destroyid = i;
            }
        }
        if (add && addid > -1) {
            inven.get(addid).qnty += qnt;
        }
        if (remove && removeid > -1) {
            inven.get(removeid).qnty -= qnt;
        }
        if (destroy && destroyid > -1) {
            inven.remove(destroyid);
        }
        if (!add) {
            inven.add(new Item(idAdd, qnt));
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="itemAdd int id int qnty">
    public void itemAdd(int id, int qnty) {
        int index = -1;
        Item ref = new Item(0, 0);
        for (int i = 0; i < inven.size(); i++) {
            if (inven.get(i).id == id) {
                index = i;
            }
        }
        if (index != -1) {
            inven.get(index).qnty += qnty;
        } else {
            inven.add(new Item(id, qnty));
        }
        if (ref.NAMES[id].endsWith("s") || qnty == 1) {
            eng.pop("+" + qnty + " " + ref.NAMES[id], 0);
        } else {
            eng.pop("+" + qnty + " " + ref.NAMES[id] + "s", 0);
        }

    }

    public void itemAdd(int id, int qnty, int health) {
        int index = -1;
        Item ref = new Item(0, 0);
        for (int i = 0; i < inven.size(); i++) {
            if (inven.get(i).id == id) {
                index = i;
            }
        }
        if (index != -1) {
            inven.get(index).qnty += qnty;
        } else {
            inven.add(new Item(id, qnty, health));
        }
        if (ref.NAMES[id].endsWith("s") || qnty == 1) {
            eng.pop("+" + qnty + " " + ref.NAMES[id], 0);
        } else {
            eng.pop("+" + qnty + " " + ref.NAMES[id] + "s", 0);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="itemRemove int id int qnty">
    public void itemRemove(int id, int qt) {
        int index = -1;
        Item ref = new Item(0, 0);

        for (int i = 0; i < inven.size(); i++) {
            if (inven.get(i).id == id) {
                index = i;
            }
        }
        if (index != -1) {
            inven.get(index).qnty -= qt;
        }
        if (inven.get(index).qnty <= 0) {
            inven.remove(index);
        }
        if (ref.NAMES[id].endsWith("s") || qt == 1) {
            eng.pop("-" + qt + " " + ref.NAMES[id], 0);
        } else {
            eng.pop("-" + qt + " " + ref.NAMES[id] + "s", 0);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="slotsAdd">
    public void slotsAdd() {
        int t = 0;
        for (int j = 0; j < slotsy; j++) {
            for (int i = 0; i < slotsx; i++) {
                slots[t] = (new Rectangle(10 + (105 * i), 90 + (105 * j), 100, 100));
                t++;
            }
        }
    }
    //</editor-fold>
}
