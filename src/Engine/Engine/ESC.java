/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Engine;

//<editor-fold defaultstate="collapsed" desc="Imports">
import Engine.Items.CampFire;
import Engine.Map.Overlay;
import Engine.Map.Pause;
import Engine.Player.Player;
import Engine.Map.World;
import Engine.Map.WorldObjects;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Area;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
//</editor-fold>

/**
 * @author DribbeWare
 */
public class ESC implements Runnable {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    //debug
    public boolean debug = false;
    //stats
    int fps, ticks, fireSize = 1;
    public boolean running = false;
    boolean fire;
    int tp = 0;
    int poptime = 0;
    int popcounter = 0;
    //Input input;
    public String Loc = "hub";

    //Input input;
    public Input input = new Input(this);

    //menu
    public boolean menu = true;
    public boolean pause = false;
    public boolean paused = false;
    public int pdelay = 0;
    public Pause pauseMenu;
    public boolean screenDelay = false;

    //inventory
    public boolean inventory = false;
    public int invTimer = 16;

    //mouse
    public boolean left = false, right = false;

    public int notches = 0;
    public int scrollSens = 10;
    public boolean upNotching = true, downNotching = true;

    public int delay = 0;
    public boolean delaystart = false;
    int mouseDelay = 1;

    //frame
    public JFrame frame;
    public Canvas canvas;
    private Graphics g;
    private BufferStrategy bs;
    public int sizeh = 800, sizew = 1500;
    public Rectangle screenbox = new Rectangle(0, 0, sizew, sizeh);

    public String health = "500", xoff = "100", yoff = "100", name = "Paul";
    public String[] loadVars = {health, name, xoff, yoff};
    //fps control
    public boolean sixty = true;

    //player
    public Player mainChar;
    public boolean move = true;

    //world
    public World world;
    public LoadSave saver = new LoadSave(this);
    public static AssetLoader assetLdr;
    public int size = 4;
    public Overlay overlay;
    public Rectangle TL = new Rectangle(20, 20, 50, 50);

    public boolean dark = false;
    public Color filter = new Color(0, 0, 0, 0);

    public Font text = new Font("TimesRoman", Font.PLAIN, 60);
    public Font text2 = new Font("TimesRoman", Font.PLAIN, 30);

    //time
    private int dayTick = 0;
    private int time;
    private int timeOffset = 1;
    private int days = 0;

    List<scrollingText> popups = new ArrayList<>();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Main">
    public ESC(int w, int h) {
        sizew = w;
        sizeh = h;
        assetLdr = new AssetLoader(this);
        Window();
        init();
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        ESC MainEngine = new ESC(1500, 800);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CreateDisplay">
    public void Window() {
        //Name the game window       
        frame = new JFrame("Tiny Ferdinand");
        //Frame setup
        frame.setSize(sizew, sizeh);
        frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - sizew / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - sizeh / 2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        //Canvas setup for drawing
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(sizew, sizeh));
        canvas.setMaximumSize(new Dimension(sizew, sizeh));
        canvas.setMinimumSize(new Dimension(sizew, sizeh));
        canvas.setFocusable(false);

        //Adding elements to frame for use
        frame.add(canvas);
        frame.addKeyListener(input);
        canvas.addMouseListener(new CustomListener());
        canvas.addMouseWheelListener(new WheelListener());
        frame.pack();
    }
    //</editor-fold>

    //Restart the game with the same save file 
    //<editor-fold defaultstate="collapsed" desc="Restart">
    public void restart() {
        this.frame.dispose();
        ESC MainEngine = new ESC(sizew, sizeh);
    }

    public void restart(int w, int h) {
        this.frame.dispose();
        ESC MainEngine = new ESC(w, h);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Update">
    public void update() {
        //Movement checks
        move();

//        System.out.println(getXOff() + "      " + getYOff());
        //World
        for (int i = 0; i < popups.size(); i++) {
            if (popups.get(i).tick == 240) {
                popups.remove(i);
                i = 0;
            } else {
                popups.get(i).update();
            }
        }
        if (poptime < 50) {
            poptime++;
        }
        if (poptime == 15) {
            popcounter = 0;
        }
        world.update();
        //Character update
        mainChar.update();
        if (fire) {
            fireSize++;
        } else {
            fireSize--;
        }
        if (fireSize == 0) {
            fire = true;
        }
        if (fireSize == 25) {
            fire = false;
        }
        //pause 
        if (input.close && !pause && pdelay == 0 && !Loc.equalsIgnoreCase("menu")) {
            pause = true;
            inventory = false;
            invTimer = 0;
        }
        if (input.close && pause && pdelay > 15) {
            pause = false;
            pdelay = 0;
            paused = true;
        }
        if (input.reset) {
            try {
                saver.Save();
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(ESC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (input.action && !inventory && invTimer > 15) {
            if (world.hubRoom.obbys.size() > 0) {
                world.hubRoom.obbys.get(0).closeAll(null);
            }
            inventory = true;
            invTimer = 0;
        }
        if (input.action && inventory && invTimer > 15) {
            inventory = false;
            invTimer = 0;
        }
        if (input.temp) {
            time += 60;
        }
        if (pause) {
            pauseMenu.update();
        }
        if (delay == mouseDelay) {
            delaystart = false;
            right = false;
            left = false;
            delay = 0;
        }
        if (screenDelay) {
            tp++;
        }
        if (tp == 20) {
            screenDelay = false;
            tp = 0;
        }
        if (delaystart == true) {
            delay++;
        }
        if (pause && pdelay < 20) {
            pdelay++;
        }
        if (paused) {
            pdelay++;
            if (pdelay > 15) {
                pdelay = 0;
                paused = false;
            }
        }
        if (invTimer < 16) {
            invTimer++;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Render">
    public void render() {
        bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();

        g.setFont(text);
        g.getFontMetrics(text);

        g.clearRect(0, 0, sizew, sizeh);

        g.setColor(new Color(50, 180, 255));
        g.fillRect(0, 0, sizew, sizeh);

        world.render(g);

        if (!Loc.equalsIgnoreCase("menu")) {
            mainChar.render(g);

            overlay.render(g);

            world.priorityRrender(g);

            overlay.priorityRender(g);

            makeDark(g);

            g.setFont(text2);
            g.getFontMetrics(text2);
            g.setColor(Color.white);
            drawClock(g);

            mainChar.inv.render(g);
        }

        for (int i = 0; i < world.items.size(); i++) {
            (world.items.get(i)).toolTips(g, this);
        }

        g.setFont(text2);
        for (int i = 0; i < popups.size(); i++) {
            popups.get(i).render(g);
        }

        if (pause) {
            pauseMenu.render(g);
        }

        g.setFont(text);
        g.getFontMetrics(text);
        if (debug == true) {
            g.setColor(Color.blue);
            g.drawString(Integer.toString(fps), 100, 100);
            g.setColor(Color.red);
            g.drawString(Integer.toString(ticks), 250, 100);
        }

        g.dispose();
        bs.show();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="drawClock Graphics g">
    void drawClock(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Helvetica", Font.BOLD, 25));
        int center = 85;
        if (time < 60 * 12) {
            if (time / 60 == 0) {
                g.drawString("Day " + days + " " + 12 + ":" + String.format("%02d", (time - ((int) time / 60) * 60)) + " am", sizew / 2 - center, 25);
            } else {
                g.drawString("Day " + days + " " + String.format("%02d", (int) time / 60) + ":" + String.format("%02d", (time - ((int) time / 60) * 60)) + " am", sizew / 2 - center, 25);
            }
        } else {
            if (time / 60 > 12) {
                g.drawString("Day " + days + " " + String.format("%02d", (((int) time / 60) - 12)) + ":" + String.format("%02d", (time - ((int) time / 60) * 60)) + " pm", sizew / 2 - center, 25);
            } else {
                g.drawString("Day " + days + " " + String.format("%02d", ((int) time / 60)) + ":" + String.format("%02d", (time - ((int) time / 60) * 60)) + " pm", sizew / 2 - center, 25);
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="makeDark Graphics g">
    void makeDark(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(filter);
        Area outer = new Area(new Rectangle(0, 0, sizew, sizeh));

        List<WorldObjects> obbys = world.active.obbys;
        if (dark || world.active.dark) {
            for (int i = 0; i < obbys.size(); i++) {
                if ((obbys.get(i).hasLight)) {
                    if (obbys.get(i).actionTimer > 0 && obbys.get(i).dropped) {
                        outer.subtract(new Area(((WorldObjects) obbys.get(i)).getLight()));
                    }
                }
            }
        }
        g2d.fill(outer);
        g2d.dispose();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Move">
    private void move() {
        if (!"menu".equals(Loc) && !pause && move) {
            if (input.up && !mainChar.up) { //Moving up
                yoff = Integer.toString((Integer.parseInt(yoff)) + mainChar.speed);
            }
            if (input.down && !mainChar.down) { //Moving down
                yoff = Integer.toString((Integer.parseInt(yoff)) - mainChar.speed);
            }
            if ((input.left && !mainChar.left)) { //Moving left
                xoff = Integer.toString((Integer.parseInt(xoff)) + mainChar.speed);
            }
            if (input.right && !mainChar.right) { //Moving right
                xoff = Integer.toString((Integer.parseInt(xoff)) - mainChar.speed);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pop String text int y">
    public void pop(String text, int y) {
        popcounter++;
        popups.add(new scrollingText(text, y - (popcounter * 30)));
        poptime = 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Run">
    //@Override
    public void run() {

        long timediff = 0, startTime = 0;
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0; // Number of ticks for update and render cycle(in one second)
        double ns = 1000000000 / amountOfTicks; //Ticks per second
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        while (running) { //Running loop
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                input.update();
                //If fps locked to 60
                if (sixty == true) {
                    render();
                    frames++;
                }
                update();
                dayCycle();
                updates++;
                delta--;

            }
            //uncapped area
            if (sixty == false) {
                render();
                frames++;
            }
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                //System.out.println("FPS: " + frames + " TICKS: " + updates);
                fps = frames;
                ticks = updates;
                frames = 0;
                updates = 0;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="init">
    public void init() {
        try {
            assetLdr.init();
        } catch (IOException ex) {
            System.out.println("oops cant start loader");
        }
        world = new World(input, this);
//        world.start();
        mainChar = new Player(10, 10, input, this, world);
        saver.init();
        xoff = loadVars[2];
        yoff = loadVars[3];
        overlay = new Overlay(this);
        running = true;
        pauseMenu = new Pause(this);
        try {
            saver.Save();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println("inital save failed");
        }

        run();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dayCycle">
    public void dayCycle() {
        int hour, minute;
        if (!Loc.equalsIgnoreCase("menu")) {
            dayTick++;
            if (time >= 12 * 60) {
                dark = true;
                filter = new Color(0, 0, 0, 150);
            }
            if (dayTick == 60) {
                time += 1 * timeOffset;
                if (time >= 24 * 60) {
                    dark = false;
                    time = 0;
                    filter = new Color(0, 0, 0, 0);
                    days++;
                    System.out.println(days);
                }
                dayTick = 0;
                hour = time / 60;
                minute = time - (hour * 60);
                //System.out.println(String.format("%02d", hour) + ":" + String.format("%02d", minute));
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getXOff() {
        return Integer.parseInt(xoff);
    }

    public int getYOff() {
        return Integer.parseInt(yoff);
    }

    public void setXOff(int in) {
        xoff = Integer.toString(in);
    }

    public void setYOff(int in) {
        yoff = Integer.toString(in);
    }

    public int getFrameX() {
        return frame.getX();
    }

    public int getFrameY() {
        return frame.getY();
    }

    public int getTime() {
        return time;
    }

    public void setDay(int d) {
        days = d;
    }

    public int getDay() {
        return days;
    }

    public void setTime(int t) {
        time = t;
    }

    //</editor-fold>
    //Mouse listener with delay for noise reduction
    //<editor-fold defaultstate="collapsed" desc="Mouse listener class">
    public class CustomListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            if (mouseEvent.getButton() == MouseEvent.BUTTON1 && !screenDelay) {
                if (!delaystart) {
                    left = true;
                    delaystart = true;
                }
            } else if (mouseEvent.getButton() == MouseEvent.BUTTON3 && !screenDelay) {
                if (!delaystart) {
                    right = true;
                    delaystart = true;
                    //System.out.println("right press");
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Mouse Wheel Listener classs">
    public class WheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (upNotching) {
                if (e.getWheelRotation() < 0) {
                    notches += scrollSens;
                }
            }
            if (downNotching) {
                if (e.getWheelRotation() > 0) {
                    notches -= scrollSens;
                }
            }

        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Scrolling Text class">
    public class scrollingText {

        int x = (sizew / 2), y = (sizeh / 2) - 50;
        int scale = 2;
        int tick = 0;
        int alpha = 255;
        String text;

        Color textColor = new Color(255, 255, 255);

        public scrollingText(String textin, int offSet) {
            text = textin;
            x -= (text.length() / 3) * text2.getSize();
            y = ((sizeh / 2) - 50) - offSet;
        }

        public void update() {
            y -= scale;
            tick += 5;
            alpha -= 5;
        }

        public void render(Graphics g) {
            g.setColor(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), alpha));
            g.drawString(text, x, y);
        }

    }
    //</editor-fold>

}
