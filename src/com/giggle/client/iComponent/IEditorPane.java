package com.giggle.client.iComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

public class IEditorPane extends JEditorPane {
    public IEditorPane() {
        setContentType("text");
        setEditable(false);
        setBorder(new EmptyBorder(10, 10, 0, 0));
        putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        setFont(new Font("Times",Font.PLAIN,13));
        addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
