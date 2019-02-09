/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map;

import Engine.Engine.ESC;
import Engine.Items.Tree;
import Engine.Items.Item;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DribbleWare
 */
public class Hub {

    public int temp = 12;
    ESC engi;
    private int xOff, yOff;
    public boolean treesTemp = false;

    //Lists to store all objects and items
    public List<Rectangle> objects = new ArrayList<>();
    public List<Item> items = new ArrayList<>();
    public List<Tree> trees = new ArrayList<>();
    public List<Integer> treeNums = new ArrayList<>();

    public Rectangle Tree1, Tree2, Tree3, Tree4, Tree5, Tree6;
    public Rectangle pond1;
    public Rectangle shrub1, shrub2;
    public Rectangle Fence1, Fence2, Fence3, Fence4, Fence5;
    public Rectangle Btree1, Btree2, Btree3;
    public Rectangle Haybales;
    public Rectangle Tomb;
    public Rectangle House1, House2, House3, House4, House5;
    public Rectangle Wheelbarrow1, Wheelbarrow2, Wheelbarrow3;
    public Rectangle Pitchfork;
    public Rectangle Pond2_1, Pond2_2, Pond2_3, Pond2_4, Pond2_5;
    public Rectangle Pond2_6, Pond2_7, Pond2_8, Pond2_9, Pond2_10;
    public Rectangle Pond2_11, Pond2_12;

    //Default locations for items if no save loaded
    public Rectangle item1 = new Rectangle(1, 1, 50, 50);
    public Rectangle item2 = new Rectangle(450, 450, 50, 50);
    public Rectangle item3 = new Rectangle(275, 134, 50, 50);
    public Rectangle item4 = new Rectangle(256, 256, 50, 50);

    public Hub(ESC eng) {
        engi = eng;
        //Tree setup/ collision declaration
        Tree1 = new Rectangle(89 * engi.size, 92 * engi.size, 15 * engi.size, 10 * engi.size);
        trees.add(new Tree(engi, Tree1, 0));
        Tree2 = new Rectangle(185 * engi.size, 54 * engi.size, 15 * engi.size, 10 * engi.size);
        trees.add(new Tree(engi, Tree2, 0));
        Tree3 = new Rectangle(217 * engi.size, 41 * engi.size, 15 * engi.size, 10 * engi.size);
        trees.add(new Tree(engi, Tree3, 0));
        Tree4 = new Rectangle(252 * engi.size, 56 * engi.size, 15 * engi.size, 10 * engi.size);
        trees.add(new Tree(engi, Tree4, 0));
        Tree5 = new Rectangle(289 * engi.size, 56 * engi.size, 15 * engi.size, 10 * engi.size);
        trees.add(new Tree(engi, Tree5, 0));
        Tree6 = new Rectangle(321 * engi.size, 43 * engi.size, 15 * engi.size, 10 * engi.size);
        trees.add(new Tree(engi, Tree6, 0));
        //Ponds
        pond1 = new Rectangle(273 * engi.size, 140 * engi.size, 62 * engi.size, 68 * engi.size);
        //Shrubs
        shrub1 = new Rectangle(457 * engi.size, 214 * engi.size, 10 * engi.size, 10 * engi.size);
        shrub2 = new Rectangle(489 * engi.size, 278 * engi.size, 10 * engi.size, 10 * engi.size);
        //Fences
        Fence1 = new Rectangle(44 * engi.size, 405 * engi.size, 262 * engi.size, 4 * engi.size);
        Fence2 = new Rectangle(44 * engi.size, 405 * engi.size, 7 * engi.size, 227 * engi.size);
        Fence3 = new Rectangle(300 * engi.size, 405 * engi.size, 7 * engi.size, 101 * engi.size);
        Fence4 = new Rectangle(300 * engi.size, 561 * engi.size, 7 * engi.size, 73 * engi.size);
        Fence5 = new Rectangle(44 * engi.size, 630 * engi.size, 262 * engi.size, 4 * engi.size);
        //Big trees
        Btree1 = new Rectangle(610 * engi.size, 371 * engi.size, 66 * engi.size, 45 * engi.size);
        trees.add(new Tree(engi, Btree1, 1));
        Btree2 = new Rectangle(40 * engi.size, 647 * engi.size, 66 * engi.size, 45 * engi.size);
        trees.add(new Tree(engi, Btree2, 1));
        Btree3 = new Rectangle(204 * engi.size, 915 * engi.size, 66 * engi.size, 45 * engi.size);
        trees.add(new Tree(engi, Btree3, 1));
        //Tomb
        Tomb = new Rectangle(144 * engi.size, 410 * engi.size, 62 * engi.size, 70 * engi.size);
        //Haybales
        Haybales = new Rectangle(372 * engi.size, 616 * engi.size, 100 * engi.size, 192 * engi.size);
        //House
        House1 = new Rectangle(706 * engi.size, 80 * engi.size, 360 * engi.size, 140 * engi.size);
        House2 = new Rectangle(706 * engi.size, 220 * engi.size, 92 * engi.size, 36 * engi.size);
        House3 = new Rectangle(830 * engi.size, 220 * engi.size, 92 * engi.size, 36 * engi.size);
        House4 = new Rectangle(922 * engi.size, 80 * engi.size, 144 * engi.size, 186 * engi.size);
        House5 = new Rectangle(928 * engi.size, 266 * engi.size, 128 * engi.size, 30 * engi.size);
        //Wheelbarrow
        Wheelbarrow1 = new Rectangle(1026 * engi.size, 325 * engi.size, 32 * engi.size, 16 * engi.size);
        Wheelbarrow2 = new Rectangle(1057 * engi.size, 325 * engi.size, 31 * engi.size, 3 * engi.size);
        Wheelbarrow3 = new Rectangle(1008 * engi.size, 335 * engi.size, 15 * engi.size, 12 * engi.size);
        //Pitchfork
        Pitchfork = new Rectangle(833 * engi.size, 390 * engi.size, 30 * engi.size, 26 * engi.size);
        //Pon2
        Pond2_1 = new Rectangle(942 * engi.size, 555 * engi.size, 134 * engi.size, 60 * engi.size);
        Pond2_2 = new Rectangle(912 * engi.size, 615 * engi.size, 194 * engi.size, 164 * engi.size);
        Pond2_3 = new Rectangle(1076 * engi.size, 583 * engi.size, 30 * engi.size, 32 * engi.size);
        Pond2_4 = new Rectangle(912 * engi.size, 779 * engi.size, 165 * engi.size, 32 * engi.size);
        Pond2_5 = new Rectangle(1077 * engi.size, 779 * engi.size, 25 * engi.size, 11 * engi.size);
        Pond2_6 = new Rectangle(925 * engi.size, 811 * engi.size, 135 * engi.size, 12 * engi.size);
        Pond2_7 = new Rectangle(943 * engi.size, 823 * engi.size, 100 * engi.size, 25 * engi.size);
        Pond2_8 = new Rectangle(975 * engi.size, 848 * engi.size, 68 * engi.size, 25 * engi.size);
        Pond2_9 = new Rectangle(965 * engi.size, 848 * engi.size, 10 * engi.size, 5 * engi.size);
        Pond2_10 = new Rectangle(975 * engi.size, 873 * engi.size, 60 * engi.size, 10 * engi.size);
        Pond2_11 = new Rectangle(975 * engi.size, 883 * engi.size, 35 * engi.size, 95 * engi.size);
        Pond2_12 = new Rectangle(1010 * engi.size, 938 * engi.size, 110 * engi.size, 40 * engi.size);

        //Adding objects for collision map
        //<editor-fold defaultstate="collapsed" desc="Adding everything to the object list">
        objects.add(Tree1);
        objects.add(Tree2);
        objects.add(Tree3);
        objects.add(Tree4);
        objects.add(Tree5);
        objects.add(Tree6);
        objects.add(pond1);
        objects.add(shrub1);
        objects.add(shrub2);
        objects.add(Fence1);
        objects.add(Fence2);
        objects.add(Fence3);
        objects.add(Fence4);
        objects.add(Fence5);
        objects.add(Btree1);
        objects.add(Btree2);
        objects.add(Btree3);
        objects.add(Tomb);
        objects.add(Haybales);
        objects.add(House1);
        objects.add(House2);
        objects.add(House3);
        objects.add(House4);
        objects.add(House5);
        objects.add(Wheelbarrow1);
        objects.add(Wheelbarrow2);
        objects.add(Wheelbarrow3);
        objects.add(Pitchfork);
        objects.add(Pond2_1);
        objects.add(Pond2_2);
        objects.add(Pond2_3);
        objects.add(Pond2_4);
        objects.add(Pond2_5);
        objects.add(Pond2_6);
        objects.add(Pond2_7);
        objects.add(Pond2_8);
        objects.add(Pond2_9);
        objects.add(Pond2_10);
        objects.add(Pond2_11);
        objects.add(Pond2_12);
        //</editor-fold>
    }

    //Drawing everything to screen
    public void render(Graphics g) {
        //update for the x and y offset for drawing
        xOff = Integer.parseInt(engi.xoff);
        yOff = Integer.parseInt(engi.yoff);

        //Draw hub
        g.drawImage(engi.assetLdr.Hub1, xOff, yOff, engi.assetLdr.Hub1.getWidth() * engi.size, engi.assetLdr.Hub1.getHeight() * engi.size, null);
        g.setColor(Color.red);

        //Outline for collision boxes if debug mode is on
        if (engi.debug == true) {
            for (int i = 0; i < objects.size(); i++) {
                g.drawRect(objects.get(i).x + xOff, objects.get(i).y + yOff, objects.get(i).width, objects.get(i).height);
            }
        }

        //Drawing all of the items to their world location
        for (int i = 0; i < items.size(); i++) {

            g.drawImage(items.get(i).art[items.get(i).id], items.get(i).x + xOff, items.get(i).y + yOff, items.get(i).width, items.get(i).height, null);
            
        }

        for (int i = 0; i < trees.size(); i++) {
            trees.get(i).render(g);
        }

    }

    //Update series
    public void update() {
        engi.mainChar.map = new Rectangle(Integer.parseInt(engi.xoff), Integer.parseInt(engi.yoff), engi.assetLdr.Hub1.getWidth() * engi.size, engi.assetLdr.Hub1.getHeight() * engi.size);
        for (int i = 0; i < trees.size(); i++) {
            trees.get(i).update();
        }
    }

    public void addItems() {
        //Item set up for defaults if nothing loaded
        items.add(new Item(item1.x, item1.y, item1.width, item1.height, 0, 3));
        items.add(new Item(item2.x, item2.y, item2.width, item2.height, 0, 3));
        items.add(new Item(item3.x, item3.y, item3.width, item3.height, 1, 1));
        items.add(new Item(item4.x, item4.y, item4.width, item4.height, 1, 1));
    }

    public void updateTrees() {
        for (int i = 0; i < trees.size(); i++) {
            trees.get(i).count = treeNums.get(i);
        }
    }
}
