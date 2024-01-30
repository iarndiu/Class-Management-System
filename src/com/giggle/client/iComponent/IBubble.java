package com.giggle.client.iComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import javax.swing.text.StyleConstants;

public class IBubble extends JPanel {
    private ITextPane textPane;
    private boolean isLeft;

    public boolean isLeft() {
        return isLeft;
    }
    /**
     * 用于计算气泡应该有的实际高度。
     * @param  width 期望的宽度
     * @param document 当前文档内容
     * @return 高度
     */
    private int getContentHeight(int width, StyledDocument document) {
        var testPane = new ITextPane();
        testPane.setSize(width, Short.MAX_VALUE);
        testPane.setStyledDocument(document);
        return testPane.getPreferredSize().height;
    }

    public IBubble(boolean isLeft) {
        //setLayout(new BorderLayout());
        this.isLeft = isLeft;
        // 在绘制气泡时左偏移了10个像素，然后期望和边框距离6像素
        if (isLeft) {
            setBorder(new EmptyBorder(6, 16, 6, 6));
        } else {
            setBorder(new EmptyBorder(6, 6, 6, 16));
        }
        textPane = new ITextPane();
        textPane.setFont(IConfig.font);
        textPane.setEditable(false);
        textPane.setOpaque(false);
        add(textPane);
        // 默认使用白色做背景
        setBackground(Color.WHITE);
    }

    // 高级操作通过这个进行
    public ITextPane getTextPane() {
        return textPane;
    }

    public void insertIcon(Icon icon) {
        getTextPane().setCaretPosition(getTextPane().getStyledDocument().getLength());
        getTextPane().insertIcon(icon);
    }

    /**
     * 设置气泡的最佳宽度，此方法消耗双倍运算能力，因此建议只调用一次以获得最小的损失
     * @param width
     */
    public void setPreferredSize(int width) {
        var h = getContentHeight(width, getTextPane().getStyledDocument());
        getTextPane().setPreferredSize(new Dimension(width, h));
    }

    public void insertText(String text) {
        var doc = getTextPane().getStyledDocument();
        StyleContext context = new StyleContext();
        Style style = context.addStyle("font", null);
        StyleConstants.setForeground(style, getForeground());
        getTextPane().setCaretPosition(doc.getLength());
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RendererUtil.applyQualityRenderingHints(g2d);
        g2d.setPaint(getBackground());
        if (isLeft) {
            paintLeftBubble(g2d);
        } else {
            paintRightBubble(g2d);
        }
    }

    private void paintRightBubble(Graphics2D g2d) {
        var width = getWidth();
        var height = getHeight();
        var path = new GeneralPath();
        path.moveTo(width - 10, 18);
        path.lineTo(width, 13);
        path.lineTo(width - 10, 8);
        path.curveTo(width - 10, 8, width - 10, 0, width - 15, 0);
        path.lineTo(5, 0);
        path.curveTo(5, 0, 0, 0, 0, 5);
        path.lineTo(0, height - 5);
        path.curveTo(0, height - 5, 0, height, 5, height);
        path.lineTo(width - 15, height);
        path.curveTo(width - 15, height, width - 10, height, width - 10, height - 5);
        path.lineTo(width - 10, 18);
        path.closePath();
        g2d.fill(path);
    }

    private void paintLeftBubble(Graphics2D g2d) {
        var width = getWidth();
        var height = getHeight();
        var path = new GeneralPath();
        path.moveTo(10, 18);
        path.lineTo(0, 13);
        path.lineTo(10, 8);
        path.curveTo(10, 8, 10, 0, 15, 0);
        path.lineTo(width - 5, 0);
        path.curveTo(width - 5, 0, width, 0, width, 5);
        path.lineTo(width, height - 5);
        path.curveTo(width, height - 5, width, height, width - 5, height);
        path.lineTo(15, height);
        path.curveTo(15, height, 10, height, 10, height - 5);
        path.lineTo(10, 18);
        path.closePath();
        g2d.fill(path);
    }
}


class RendererUtil {
    public static void applyQualityRenderingHints(Graphics2D g2d, boolean text) {
        if (text) g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        applyQualityRenderingHints(g2d);
    }
    public static void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        //
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    public static GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }
}