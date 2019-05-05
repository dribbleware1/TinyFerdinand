package Engine.Engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DribbleWare
 */
public class Input implements KeyListener {

    private boolean[] button;
    public boolean up, down, left, right, reset, start, close, pause, action, set, temp;
    ESC engine;

    public Input(ESC cargoEngine) {
        button = new boolean[265];
        this.engine = cargoEngine;
    }

    public void update() {
        up = button[KeyEvent.VK_W];
        down = button[KeyEvent.VK_S];
        left = button[KeyEvent.VK_A];
        right = button[KeyEvent.VK_D];
        reset = button[KeyEvent.VK_DOWN];
        start = button[KeyEvent.VK_SPACE];
        close = button[KeyEvent.VK_ESCAPE];
        action = button[KeyEvent.VK_E];
        set = button[KeyEvent.VK_UP];
        temp = button[KeyEvent.VK_B];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() <= button.length) {
            button[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() <= button.length) {
            button[e.getKeyCode()] = false;
        }
//        if(engine.menu==true && engine.name.length() < 10){
//            if(e.getKeyCode() == 65){
//                engine.name+="a";
//            }
//            if(e.getKeyCode() == 66){
//                engine.name+="b";
//            }
//            if(e.getKeyCode() == 67){
//                engine.name+="c";
//            }
//            if(e.getKeyCode() == 68){
//                engine.name+="d";
//            }
//            if(e.getKeyCode() == 69){
//                engine.name+="e";
//            }
//            if(e.getKeyCode() == 70){
//                engine.name+="f";
//            }
//            if(e.getKeyCode() == 71){
//                engine.name+="g";
//            }
//            if(e.getKeyCode() == 72){
//                engine.name+="h";
//            }
//            if(e.getKeyCode() == 73){
//                engine.name+="i";
//            }
//            if(e.getKeyCode() == 74){
//                engine.name+="j";
//            }
//            if(e.getKeyCode() == 75){
//                engine.name+="k";
//            }
//            if(e.getKeyCode() == 76){
//                engine.name+="l";
//            }
//            if(e.getKeyCode() == 77){
//                engine.name+="m";
//            }
//            if(e.getKeyCode() == 78){
//                engine.name+="n";
//            }
//            if(e.getKeyCode() == 79){
//                engine.name+="o";
//            }
//            if(e.getKeyCode() == 80){
//                engine.name+="p";
//            }
//            if(e.getKeyCode() == 81){
//                engine.name+="q";
//            }
//            if(e.getKeyCode() == 82){
//                engine.name+="r";
//            }
//            if(e.getKeyCode() == 83){
//                engine.name+="s";
//            }
//            if(e.getKeyCode() == 84){
//                engine.name+="t";
//            }
//            if(e.getKeyCode() == 85){
//                engine.name+="u";
//            }
//            if(e.getKeyCode() == 86){
//                engine.name+="v";
//            }
//            if(e.getKeyCode() == 87){
//                engine.name+="w";
//            }
//            if(e.getKeyCode() == 88){
//                engine.name+="x";
//            }
//            if(e.getKeyCode() == 89){
//                engine.name+="y";
//            }
//            if(e.getKeyCode() == 90){
//                engine.name+="z";
//            }
//        }if(engine.menu == true){
//            if(e.getKeyCode() == 8){
//                if(engine.name.length() > 0){
//                engine.name = engine.name.substring(0,engine.name.length()-1);
//                }
//            }
//            if(e.getKeyCode() == 10){
//                engine.menu = false;
//            }
//        }        
    }

}
