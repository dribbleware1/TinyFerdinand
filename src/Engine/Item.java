/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

/**
 *
 * @author DribbleWare
 */
public class Item {

    int x, y, width, height, id, qnty;

    public Item(int xi, int yi, int widthi, int heighti, int idi, int q) {
        x = xi;
        y = yi;
        width = widthi;
        height = heighti;
        id = idi;
        qnty = q;
    }

    public Item(int idi, int q) {
        id = idi;
        qnty = q;
    }

}
