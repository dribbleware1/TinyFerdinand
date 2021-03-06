/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Engine;

//<editor-fold defaultstate="collapsed" desc="Imports">
import Engine.FileIO.FileUtils;
import Engine.Items.Support.ItemManager;
import Engine.Map.Locations.Pause;
import Engine.Map.World;
import Engine.Player.Player;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    int fps, ticks;
    public boolean running = false;
    int tp = 0;
    int poptime = 0;
    int popcounter = 0;
    public String Loc = "menu";
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
    public String health = "500", xoff = "100", yoff = "100", place = "hub", name = "Paul";
    public String[] loadVars = {health, place, xoff, yoff};
    private String windowName = "";
    //fps control
    public boolean sixty = false;

    //player
    public Player mainChar;
    public boolean move = true;

    //world
    public World world;
    public LoadSave saver = new LoadSave(this);
    public static AssetLoader assetLdr;
    public int size = 4;
    public boolean fadeout = false, fadein = false, faded = false;
    Color fadeFilter = new Color(0, 0, 0, 0);
    int fadeTimer = 0;
    int fadeAmount = 3;

    public Thread lightManager;
    public Thread itemManager;

    public ItemManager im;

    public Font text = new Font("TimesRoman", Font.PLAIN, 60);
    public Font text2 = new Font("TimesRoman", Font.PLAIN, 30);
    public BufferedImage FILTER = new BufferedImage(1120, 1120, 3);
    float alpha = 0;
    public boolean doLight = false;

    //time
    private int TimeTick = 0;
    private int time;
    private int timeOffset = 1;
    private int days = 0;

    //Scrolling text
    List<scrollingText> popups = new ArrayList<>();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Main">
    public ESC(int w, int h, String winName) {
        windowName = winName;
        sizew = w;
        sizeh = h;
        assetLdr = new AssetLoader(this);
        Window();
        init();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CreateDisplay">
    public void Window() {
        //Name the game window       
        frame = new JFrame(windowName);
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
//        ESC MainEngine = new ESC(sizew, sizeh);
    }

    public void restart(int w, int h) {
        this.frame.dispose();
//        ESC MainEngine = new ESC(w, h);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Update">
    public void update() {
        //Movement checks
        move();
        //World
        fade();
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
            canvas.createBufferStrategy(2);
            return;
        }
        g = bs.getDrawGraphics();

        g.setFont(text);
        g.getFontMetrics(text);

        g.clearRect(0, 0, sizew, sizeh);

        g.setColor(world.active.backGround);
        g.fillRect(0, 0, sizew, sizeh);

        world.render(g);

        if (!Loc.equalsIgnoreCase("menu")) {
            mainChar.render(g);

            world.overlayRender(g);

            world.priorityRender(g);

            world.overlayPriorityRender(g);

            if (world.active.dark || world.active.permDark) {
                g.drawImage(FILTER, 0 + getXOff(), 0 + getYOff(), FILTER.getWidth() * size, FILTER.getHeight() * size, null);
            }
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

        if (fadeout || fadein) {
            g.setColor(fadeFilter);
            g.fillRect(0, 0, sizew, sizeh);
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

    //<editor-fold defaultstate="collapsed" desc="save (just for testing)">
    public static void save(BufferedImage img, String name) {
        try {
            ImageIO.write(img, "png", FileUtils.getFilePathFromRoot("Res", "Gameplay", "Mine", name + ".png").toFile());
        } catch (IOException ex) {
        }
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
                System.out.println("FPS: " + frames + " TICKS: " + updates);
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
            System.out.println(ex);
        }
        world = new World(input, this);
        lightManager = new Thread(new LightManager(this));
        lightManager.start();
        itemManager = new Thread(new ItemManager(this));
        itemManager.start();
        mainChar = new Player(10, 10, input, this);
        saver.init();
        xoff = loadVars[2];
        yoff = loadVars[3];
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
    public void dayCycle() { //needs to be redone
        int hour = 0, minute = 0;
        if (!Loc.equalsIgnoreCase("menu")) {
            TimeTick++;
            if (TimeTick == 60) {
                time += 1 * timeOffset;
                TimeTick = 0;
                hour = time / 60;
                minute = time - (hour * 60);
                //daycycle math
                if (time > (7 * 60) && time < ((12 + 8.5) * 60)) {
                    //7am
                    world.active.dark = false;
                }
                if (time > ((12 + 8) * 60) || time < (7 * 60)) {
                    //8:30pm
                    world.active.dark = true;
                }
                //
            }
            if (time > (6.5 * 60) && time < (7 * 60) && !world.active.permDark) { //getting brigther
                float amountB = (float) world.active.FILTER.getAlpha() / (30 * 60);
                alpha = world.active.FILTER.getAlpha() - amountB * (((time - (float) (6.5 * 60)) * 60) + TimeTick);
                world.active.filter = new Color(world.active.filter.getRed(), world.active.filter.getGreen(), world.active.filter.getBlue(), (int) alpha);
                doLight = true;
            }
            if ((time > ((12 + 8) * 60)) && time < ((12.5 + 8) * 60) && !world.active.permDark) {//getting darker
                float amountD = (float) world.active.FILTER.getAlpha() / (30 * 60);
                alpha = amountD * (((time - (float) ((12 + 8) * 60)) * 60) + TimeTick);
                world.active.filter = new Color(world.active.filter.getRed(), world.active.filter.getGreen(), world.active.filter.getBlue(), (int) alpha);
                doLight = true;
            }
            if (time >= 60 * 24) {
                time = 0;
                days++;
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="all fade stuff">
    public void fade() {
        if (fadein || fadeout) {
            if (fadeout && fadeFilter.getAlpha() + fadeAmount < 255) {
                fadeFilter = new Color(fadeFilter.getRed(), fadeFilter.getGreen(), fadeFilter.getBlue(), fadeFilter.getAlpha() + fadeAmount);
            }
            if (fadein && fadeFilter.getAlpha() - fadeAmount > 0) {
                fadeFilter = new Color(fadeFilter.getRed(), fadeFilter.getGreen(), fadeFilter.getBlue(), fadeFilter.getAlpha() - fadeAmount);
            }
            if (fadeFilter.getAlpha() >= 252 && fadeout) {
                fadeout = false;
                faded = true;
            }
            if (fadeFilter.getAlpha() <= 3 && fadein) {
                fadein = false;
                faded = true;
                move = true;
            }
        }
    }

    public void fadeOut() {
        fadeout = true;
        faded = false;
        move = false;
    }

    public void fadeIn() {
        fadein = true;
        faded = false;
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
