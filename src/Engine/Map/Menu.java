/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Map;

import Engine.Engine.ESC;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author DribbleWare
 */
public class Menu {

    ESC eng;
    Rectangle Title;
    Rectangle exit;
    Rectangle reset;
    Rectangle framerate;
    Rectangle options;
    Rectangle optClose;
    Rectangle debon;
    Rectangle deboff;
    Rectangle cont;

    public boolean update = false;
    public boolean opty = false;
    public int scale = 2;

    public Menu(ESC engine) {
        eng = engine;
        //title screen
        Title = new Rectangle(eng.sizew / 2 - eng.assetLdr.title.getWidth() / 2 / scale, 200 / scale, eng.assetLdr.title.getWidth() / scale, eng.assetLdr.title.getHeight() / scale);
        options = new Rectangle(eng.sizew / 2 - eng.assetLdr.opt.getWidth() / 2 / scale, 700 / scale, eng.assetLdr.opt.getWidth() / scale, eng.assetLdr.opt.getHeight() / scale);
        cont = new Rectangle(eng.sizew / 2 - eng.assetLdr.cont.getWidth() / 2 / scale, 500 / scale, eng.assetLdr.cont.getWidth() / scale, eng.assetLdr.cont.getHeight() / scale);
        //options menu
        optClose = new Rectangle(110, eng.sizeh - 168, eng.assetLdr.back.getWidth() / 3, eng.assetLdr.back.getHeight() / 3);
        debon = new Rectangle(eng.sizew / 2 - eng.assetLdr.on2.getWidth() / 2 + 25, 400 / 3, eng.assetLdr.on2.getWidth() / 3, eng.assetLdr.on2.getHeight() / 3);
        deboff = new Rectangle(eng.sizew / 2 - eng.assetLdr.off.getWidth() / 2 + 75, 400 / 3, eng.assetLdr.off.getWidth() / 3, eng.assetLdr.off.getHeight() / 3);
        exit = new Rectangle(eng.sizew / 2 - eng.assetLdr.exit.getWidth() / 2 / scale, 1300 / scale, eng.assetLdr.exit.getWidth() / scale, eng.assetLdr.exit.getHeight() / scale);
        framerate = new Rectangle(eng.sizew / 2 - eng.assetLdr.deb.getWidth() / 2 + 100, 450 / scale, eng.assetLdr.framerate.getWidth() / 3, eng.assetLdr.framerate.getHeight() / 3);
        reset = new Rectangle(eng.sizew / 2 - eng.assetLdr.reset.getWidth() / 2 / scale, 1100 / scale, eng.assetLdr.reset.getWidth() / scale, eng.assetLdr.reset.getHeight() / scale);

    }

    public void render(Graphics g) {
        //mainmenu
        g.drawImage(eng.assetLdr.menuBack, 0, 0, eng.sizew, eng.sizeh, null);
        //debug boxes
        if (eng.debug) {
            g.setColor(Color.red);
            g.drawRect(Title.x, Title.y, Title.width, Title.height);
            g.drawRect(options.x, options.y, options.width, options.height);
            g.drawRect(cont.x, cont.y, cont.width, cont.height);
            g.drawRect(exit.x, exit.y, exit.width, exit.height);
        }
        //Title
        g.drawImage(eng.assetLdr.title, eng.sizew / 2 - eng.assetLdr.title.getWidth() / 2 / scale, 200 / scale, eng.assetLdr.title.getWidth() / scale, eng.assetLdr.title.getHeight() / scale, null);

        //options
        if (contains(options) && !opty) {
            g.drawImage(eng.assetLdr.opt2, options.x, options.y, options.width, options.height, null);
        } else {
            g.drawImage(eng.assetLdr.opt, options.x, options.y, options.width, options.height, null);
        }
        //continue
        if (contains(cont) && !opty) {
            g.drawImage(eng.assetLdr.cont2, cont.x, cont.y, cont.width, cont.height, null);
        } else {
            g.drawImage(eng.assetLdr.cont, cont.x, cont.y, cont.width, cont.height, null);
        }
        //exit
        if (contains(exit) && !opty) {
            g.drawImage(eng.assetLdr.exit2, exit.x, exit.y, exit.width, exit.height, null);
        } else {
            g.drawImage(eng.assetLdr.exit, exit.x, exit.y, exit.width, exit.height, null);
        }

//options panel        
        if (opty) {
            g.setColor(new Color(0, 190, 211));
            g.fillRect(100, 100, eng.sizew - 200, eng.sizeh - 200);
            g.setColor(Color.red);
            g.drawImage(eng.assetLdr.deb, eng.sizew / 2 - eng.assetLdr.deb.getWidth() / 2 + 100, 400 / 3, eng.assetLdr.deb.getWidth() / 3, eng.assetLdr.deb.getHeight() / 3, null);
            //options close
            if (contains(optClose) && opty) {
                g.drawImage(eng.assetLdr.back2, optClose.x, optClose.y, optClose.width, optClose.height, null);
            } else {
                g.drawImage(eng.assetLdr.back, optClose.x, optClose.y, optClose.width, optClose.height, null);
            }
            //framerate
            if (contains(framerate) && opty) {
                g.drawImage(eng.assetLdr.framerate2, framerate.x, framerate.y, framerate.width, framerate.height, null);
            } else {
                g.drawImage(eng.assetLdr.framerate, framerate.x, framerate.y, framerate.width, framerate.height, null);
            }

            if (eng.sixty) {
                g.drawImage(eng.assetLdr.sixty, framerate.x + framerate.width + 15, framerate.y, eng.assetLdr.sixty.getWidth() / 3, eng.assetLdr.sixty.getHeight() / 3, null);
            } else {
                g.drawImage(eng.assetLdr.uncapped, framerate.x + framerate.width + 15, framerate.y, eng.assetLdr.uncapped.getWidth() / 3, eng.assetLdr.uncapped.getHeight() / 3, null);
            }

            //reset
            if (contains(reset) && opty) {
                g.drawImage(eng.assetLdr.reset2, reset.x, reset.y, reset.width, reset.height, null);
            } else {
                g.drawImage(eng.assetLdr.reset, reset.x, reset.y, reset.width, reset.height, null);
            }

            //debug button
            if (eng.debug) {
                g.drawRect(optClose.x, optClose.y, optClose.width, optClose.height);
                g.drawRect(debon.x, debon.y, debon.width, debon.height);
                g.drawRect(framerate.x, framerate.y, framerate.width, framerate.height);
                g.drawRect(reset.x, reset.y, reset.width, reset.height);
                if (contains(debon) && opty) {
                    g.drawImage(eng.assetLdr.on2, debon.x, debon.y, debon.width, debon.height, null);
                } else {
                    g.drawImage(eng.assetLdr.on, debon.x, debon.y, debon.width, debon.height, null);
                }
            } else {
                if (contains(deboff) && opty) {
                    g.drawImage(eng.assetLdr.off2, deboff.x, deboff.y, deboff.width, deboff.height, null);
                } else {
                    g.drawImage(eng.assetLdr.off, deboff.x, deboff.y, deboff.width, deboff.height, null);
                }
            }
        }

    }

    public void update() {
        //continue button
        if (contains(cont) && eng.left && !opty) {
            eng.Loc = "hub";
            update = true;
            eng.left = false;
        }
        //options button
        if (contains(options) && eng.left && !opty) {
            opty = true;
            eng.left = false;
        }
        //options close
        if (contains(optClose) && eng.left && opty) {
            opty = false;
            eng.left = false;
        }
        //framerate change
        if (contains(framerate) && eng.left && opty && eng.sixty) {
            eng.sixty = false;
            eng.left = false;
        }
        if (contains(framerate) && eng.left && opty && !eng.sixty) {
            eng.sixty = true;
            eng.left = false;
        }
        //debug on
        if (contains(debon) && eng.left && opty && eng.debug) {
            eng.debug = false;
            eng.left = false;
        }
        //debug off
        if (contains(deboff) && eng.left && opty && !eng.debug) {
            eng.debug = true;
            eng.left = false;
        }
        //exit button
        if (contains(exit) && eng.left && !opty && !eng.screenDelay) {
            System.exit(0);
        }
        //reset button
        if (contains(reset) && eng.left && opty) {
            eng.saver.reset();
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
