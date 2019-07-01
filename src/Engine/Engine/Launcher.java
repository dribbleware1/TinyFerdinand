/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Engine;

/**
 *
 * @author DribbleWare
 */
public class Launcher {

    static Thread game;
    static String version = " - No version number";

    public static void main(String[] args) {
        ESC GAME = new ESC(1500, 800, "Tiny Ferdinand" + version);
        game = new Thread(GAME);
        
    }

}
