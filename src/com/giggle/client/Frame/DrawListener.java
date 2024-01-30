package com.giggle.client.Frame;

//import com.giggle.client.Frame.DrawMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class DrawListener extends MouseAdapter implements ActionListener {
    private int x1, y1, x2, y2;
    private int newx1, newy1, newx2, newy2;
    private Graphics2D g;
    private JFrame df;
    private boolean flag = false;
    String shape = "直线";
    Color color;
    private int[] arrx = new int[4];
    private int[] arry = new int[4];
    private int temp = 0;
//    public DrawListener(DrawMain d) {
//        df = d;
//    }

    public DrawListener(JFrame jFrame){
        df = jFrame;
    }
    // 获取形状和颜色
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("")) {
            JButton button = (JButton) e.getSource();
            color = button.getBackground();
            System.out.println("color = " + color);
        } else {
            JButton button = (JButton) e.getSource();
            shape = button.getActionCommand();
            System.out.println("String = " + shape);
        }
    }
    // 实现画笔
    public void mousePressed(MouseEvent e) {
        g = (Graphics2D) df.getGraphics();
        g.setColor(color);
        x1 = e.getX();
        y1 = e.getY();
    }
    public void mouseReleased(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
        if (shape.equals("直线")) {
            g.drawLine(x1, y1, x2, y2);
        } else if (shape.equals("弧线")) {
            g.drawArc(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1), 0, 180);
        } else if (shape.equals("圆")) {
            g.drawOval(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
        } else if (shape.equals("矩形")) {
            g.drawRect(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
        } else if (shape.equals("圆角矩形")) {
            g.drawRoundRect(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1), 2, 10);
        } else if (shape.equals("椭圆")) {
            g.drawOval(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
        }
    }





    public void mouseDragged(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
        if (shape.equals("曲线")) {
            // g.setStroke(new BasicStroke(10));
            // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            // RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        } else if (shape.equals("橡皮擦")) {
            g.setStroke(new BasicStroke(80));
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE);
            g.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        } else if (shape.equals("喷枪")) {
            // g.setStroke(new BasicStroke(2)); //不用加粗
            // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            // RenderingHints.VALUE_ANTIALIAS_ON);
            for (int k = 0; k < 20; k++) {
                Random i = new Random();
                int a = i.nextInt(8);
                int b = i.nextInt(10);
                g.drawLine(x2 + a, y2 + b, x2 + a, y2 + b);
            }
        }
    }
}
