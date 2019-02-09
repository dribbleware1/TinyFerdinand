/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Engine;

import Engine.Map.Overlay;
import Engine.Map.Pause;
import Engine.Player.Player;
import Engine.Map.World;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author DribbeWare
 */
public class ESC {

    //debug
    public boolean debug = false;

    //stats
    int fps, ticks;
    public boolean running = false;

    //Input input;
    public String Loc = "menu";

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
    public int delay = 0;
    public boolean delaystart = false;

    //frame
    public JFrame frame;
    public Canvas canvas;
    private Graphics g;
    private BufferStrategy bs;
    public int sizeh = 800, sizew = 1500;

    public String health = "500", xoff = "100", yoff = "100", name = "Paul";
    public String[] loadVars = {health, name, xoff, yoff};
    //fps control
    public boolean sixty = true;

    //player
    public Player mainChar;

    //world
    public World world;
    public LoadSave saver = new LoadSave(this);
    public static AssetLoader assetLdr;
    public int size = 4;
    public Overlay overlay;
    public Rectangle TL = new Rectangle(20, 20, 50, 50);

    public Font text = new Font("TimesRoman", Font.PLAIN, 60);
    public Font text2 = new Font("TimesRoman", Font.PLAIN, 30);

    public static void main(String[] args) {
        ESC CargoEngine = new ESC();
        assetLdr = new AssetLoader(CargoEngine);
        CargoEngine.createDisplay();
        CargoEngine.start();
    }

    private void createDisplay() {
        //Name the game window
        frame = new JFrame("GameTime");
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
        frame.pack();
    }

    //Restart the game with the same save file
    public void restart() {
        this.frame.dispose();
        ESC CargoEngine = new ESC();
        assetLdr = new AssetLoader(CargoEngine);
        CargoEngine.createDisplay();
        CargoEngine.start();
    }

    //Update controlls
    public void update() {
        //Movement checks
        move();
        //World
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
            inventory = true;
            invTimer = 0;
        }
        if (input.action && inventory && invTimer > 15) {
            inventory = false;
            invTimer = 0;
        }
        if (pause) {
            pauseMenu.update();
        }
    }

    //Movement controls
    private void move() {
        if (!"menu".equals(Loc) && !pause) {
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

    //Render to control whats seen
    public void render() {
        //Canvas setup with buffer strategy
        bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        //Set up font
        g.setFont(text);
        g.getFontMetrics(text);

        //Clear sceen before redrawing
        g.clearRect(0, 0, sizew, sizeh);

        //Draw genaric coloured background
        g.setColor(new Color(50, 180, 255));
        g.fillRect(0, 0, sizew, sizeh);

        //render the world
        world.render(g);

        //needs to be delt with likely can be moved to world control
        if (!Loc.equalsIgnoreCase("menu")) {
            mainChar.render(g);
            overlay.render(g);
            if (Loc.equalsIgnoreCase("hub")) {
                for (int i = 0; i < world.hubRoom.trees.size(); i++) {
                    world.hubRoom.trees.get(i).render(g);
                }
            }
            mainChar.inv.render(g);
            g.setFont(text2);
            g.getFontMetrics(text2);
            g.setColor(Color.white);
        }
        for (int i = 0; i < world.items.size(); i++) {
            world.items.get(i).toolTips(g, this);
        }

        if (pause) {
            pauseMenu.render(g);
        }

        //Controls fps and update readout if debug mode is on
        g.setFont(text);
        g.getFontMetrics(text);
        if (debug == true) {
            g.setColor(Color.blue);
            g.drawString(Integer.toString(fps), 100, 100);
            g.setColor(Color.red);
            g.drawString(Integer.toString(ticks), 250, 100);
        }

        //end draw
        bs.show();
        g.dispose();
    }

    //Main game loop
    public void run() {
        int tp = 0;
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
                updates++;
                delta--;
                if (delay == 1) {
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

    //Start worlds and player
    public void start() {
        try {
            assetLdr.init();
        } catch (IOException ex) {
            System.out.println("oops cant start loader");
        }
        render();
        world = new World(input, this);
        world.start();
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

    public int getXOff() {
        return Integer.parseInt(xoff);
    }

    public int getYOff() {
        return Integer.parseInt(yoff);
    }

    //Mouse listener with delay for noise reduction
    public class CustomListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            if (mouseEvent.getButton() == MouseEvent.BUTTON1 && !screenDelay) {
                if (!delaystart) {
                    left = true;
                    delaystart = true;
                }
            } else if (mouseEvent.getButton() == MouseEvent.BUTTON3 && !screenDelay) {
                if (!delaystart) {
                    right = true;
                    delaystart = true;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
        }
    }

}
