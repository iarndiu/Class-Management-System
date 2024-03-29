package com.giggle.client.iComponent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import java.util.ArrayList;
import java.util.List;

public class ITextPane extends JTextPane {
    public ITextPane() {
        initView();
    }

    private void initView() {
        setFont(IConfig.font);
    }

    private List<ImageIcon> images = new ArrayList<>();
    private void parse() {
        images.clear();
        var root = getStyledDocument().getRootElements()[0];
        var n = root.getElementCount();
        for (var i = 0; i < n; i++) {
            var e = root.getElement(i);
            for (var j = 0; j < e.getElementCount(); j++) {
                var icon = (ImageIcon) StyleConstants.getIcon(e.getElement(j).getAttributes());
                if (icon != null) {
                    images.add(icon);
                }
            }
        }
    }

    public String getMessage() {
        parse();
        var msg = new StringBuilder();
        var k = 0;
        for (var i = 0; i < getStyledDocument().getLength(); i++) {
            var ele = getStyledDocument().getCharacterElement(i);
            if (ele.getName().equals("icon")) {
                msg.append("<").append(images.get(k++)).append(">");
            } else {
                try {
                    var ch = getStyledDocument().getText(i, 1);
                    // 转义
                    if (ch.equals("<") || ch.equals(">") || ch.equals("\\")) {
                        msg.append("\\");
                    }
                    msg.append(ch);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return msg.toString();
    }
}
