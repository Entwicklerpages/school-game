package de.entwicklerpages.java.schoolgame.tools.dialog;


import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JButton;

/***
 * Adopted from http://stackoverflow.com/a/29202938
 *
 * Only fit to the metal L&F
 */
public class MetalGradientButton extends JButton
{
    public MetalGradientButton(String label)
    {
        super(label);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setPaint(new GradientPaint(new Point(0, 0), Color.WHITE, new Point(0, getHeight()), isEnabled() ? getBackground() : Color.LIGHT_GRAY));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();

        super.paintComponent(g);
    }
}

