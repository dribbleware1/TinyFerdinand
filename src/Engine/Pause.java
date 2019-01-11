/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author DribbleWare
 */
public class Pause {

    ESC eng;
    public Font text = new Font("TimesRoman", Font.PLAIN, 120);
    public int scale = 2;
    Rectangle Title;

    public Pause(ESC engine) {
        eng = engine;
        Title = new Rectangle(eng.sizew / 2 - eng.assetLdr.exitToTitle.getWidth() / 2 / scale, 1300 / scale, eng.assetLdr.exitToTitle.getWidth() / scale, eng.assetLdr.exitToTitle.getHeight() / scale);
    }

    public void update() {
        if (contains(Title) && eng.left) {
            try {
                eng.saver.Save();
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                System.out.println("Saving failed");
            }
            eng.Loc = "menu";
            eng.pause = false;
            eng.pdelay = 0;
        }
    }

    public void render(Graphics g) {
        g.setColor(new Color(0, 190, 211));
        g.drawImage(eng.assetLdr.pause, 50, 50, null);
        g.setColor(Color.white);
        g.setFont(text);
        g.drawString("PAUSED!", 400, 350);
        if (contains(Title)) {
            g.drawImage(eng.assetLdr.exitToTitle2, Title.x, Title.y, Title.width, Title.height, null);
        } else {
            g.drawImage(eng.assetLdr.exitToTitle, Title.x, Title.y, Title.width, Title.height, null);
        }
        
        
        g.setColor(Color.red);
        if(eng.debug){
            g.drawRect(Title.x, Title.y, Title.width, Title.height);
        }
    }

    public boolean contains(Rectangle click) {
        boolean ret = false;
        if (click.contains(new Point(MouseInfo.getPointerInfo().getLocation().x - eng.frame.getX(),
                MouseInfo.getPointerInfo().getLocation().y - eng.frame.getY() - Math.abs(eng.frame.getLocationOnScreen().y - eng.canvas.getLocationOnScreen().y)))) {
            ret = true;
        }
        return ret;
    }

}
