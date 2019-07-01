/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Items.Support;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

/**
 *
 * @author DribbleWare
 */
public class Light {

    float intensity;
    float x, y;
    float size;
    float[] dist = {0.0f, 1f};
    Color[] colors = {new Color(0, 0, 0, 0), new Color(0, 0, 0, 0)};
    Point2D center, focus;

    public Light(float x, float y, float size, float intensity) {
        this.x = x;
        this.y = y;
        this.size = size;
        center = new Point2D.Float(x + size, y + size);
        colors = new Color[]{new Color(0f, 0f, 0f, 0f), new Color(0f, 0f, 0f, intensity)};
    }

    public Light(float x, float y, float size, float intensity, Point2D focus) { // for directional lighting
        this.x = x;
        this.y = y;
        this.size = size;
        center = new Point2D.Float(x + size, y + size);
        this.focus = focus;
        colors = new Color[]{new Color(0f, 0f, 0f, 0f), new Color(0f, 0f, 0f, intensity)};
    }

    public void render(Graphics2D g) {
        RadialGradientPaint p;
        if (focus == null) {
            p = new RadialGradientPaint(center, size, dist, colors);
        } else {
            p = new RadialGradientPaint(center, size, focus, dist, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1f));
        g.setPaint(p);
        g.fillOval((int) x, (int) y, (int) size * 2, (int) size * 2);
    }
}
