/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Graeme Judge
 */
public class Tree {

    ESC eng;
    
    int timer = 0;
    final int ID;
    private Rectangle tree, treebox;
    private boolean popup = false;

    public Tree(ESC engine, Rectangle bounds, int id) {
        eng = engine;
        tree = bounds;
        resize(id);
        ID = id;
    }

    public void update() {
        if (contains(treebox)) {
            popup = true;
        }else{
            popup = false;
        }
    }

    public void render(Graphics g) {
        if(popup){
            switch(ID){
                case 0: 
                    //g.fillRect(treebox.x + Integer.parseInt(eng.xoff), treebox.y - 110 + Integer.parseInt(eng.yoff), 120, 25);
            }          
        }
        if (eng.debug) {
            g.setColor(Color.red);
            g.drawRect(treebox.x + Integer.parseInt(eng.xoff), treebox.y + Integer.parseInt(eng.yoff), treebox.width, treebox.height);
        }
    }

    public void resize(int id) {
        switch (id) {
            case 0:
                treebox = new Rectangle(tree.x - 30, tree.y - 100, tree.width + 60, tree.height + 100);
                break;
            case 1:
                System.out.println("big tree");
                break;
        }

    }

    public boolean contains(Rectangle boxin) {
        boolean ret = false;
        
        Rectangle click = new Rectangle(boxin.x + Integer.parseInt(eng.xoff),  boxin.y+ Integer.parseInt(eng.yoff), boxin.width, boxin.height);
        
        if (click.contains(new Point(MouseInfo.getPointerInfo().getLocation().x - eng.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - eng.frame.getY() - Math.abs(eng.frame.getLocationOnScreen().y - eng.canvas.getLocationOnScreen().y)))) {
            ret = true;
        }
        return ret;
    }

}
