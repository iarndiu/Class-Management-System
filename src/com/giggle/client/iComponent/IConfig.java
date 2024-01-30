package com.giggle.client.iComponent;

import java.awt.*;

public class IConfig {

        public static Font font = new Font("Times", Font.PLAIN, 13);

        public static Font createFont(int size) {
            return new Font("Times", Font.PLAIN, size);
        }

}
