//package com.giggle.client.Frame;
//
//import com.giggle.client.Frame.DrawListener;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
//
//public class DrawMain extends JPanel implements ActionListener {
//    private JButton save;
//    private ImageIcon imageIcon;
//    private int x = 600, y = 300;
//    private int width = 950, height = 600;//800
//    private JFrame jf;
//    private BufferedImage img;
//    private JPanel chatPane;
//    String path;
//
//
//    public DrawMain(JPanel chatPane) {
//
//        this.chatPane = chatPane;
//        jf = new JFrame();
//        jf.setLocation(x, y);
//        jf.setSize(width, height);
//        jf.setTitle("drawPad");
//        jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        jf.setLocationRelativeTo(null);
//        jf.setLayout(new BorderLayout());
//        // 实例化事件监听类
//        DrawListener dl = new DrawListener(this);
//        // 实现中间面板
//        this.setBackground(Color.WHITE);
//        jf.add(this, BorderLayout.CENTER);
//        // 实现性状面板
//        JPanel ShapePanel = new JPanel();
//        ShapePanel.setBackground(Color.black);
//        ShapePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//        ShapePanel.setBackground(Color.gray);
//
//        String[] Shape = {"直线", "曲线", "圆", "喷枪", "橡皮擦", "矩形", "椭圆", "圆角矩形",
//                "弧线",};
//        for (int i = 0; i < Shape.length; i++) {
//            JButton button = new JButton(Shape[i]);
//            button.setBackground(Color.WHITE);
//            button.addActionListener(dl); // 添加事件监听机制
//            ShapePanel.add(button);
//        }
//        jf.add(ShapePanel, BorderLayout.NORTH);
//        // 实现颜色面板
//        JPanel ColorPanel = new JPanel();
//        ColorPanel.setBackground(Color.black);
//        ColorPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//        ColorPanel.setBackground(Color.gray);
//
//        Color[] colors= {Color.BLACK, Color.BLUE, Color.WHITE,
//                Color.red, Color.CYAN, Color.green, Color.darkGray, Color.pink};
//        for (int i = 0; i < colors.length; i++) {
//            JButton button = new JButton();
//            button.addActionListener(dl); // 添加事件监听机制
//            button.setPreferredSize(new Dimension(30, 30));
//            button.setBackground(colors[i]);
//            button.setOpaque(true); //foreground设置透明
//            button.setBorderPainted(false); //最后显示颜色
//            ColorPanel.add(button);
//        }
//
//        save = new JButton("保存");
//        save.addActionListener(this);
//        ShapePanel.add(save);
//
//        jf.add(ColorPanel, BorderLayout.SOUTH);
//        jf.setVisible(true);
//        this.addMouseListener(dl);
//        this.addMouseMotionListener(dl);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == save) {
//
//            //[表情]
////             veImage=image.getSubimage(x, y, width, height);
//            //得到窗口内容面板
//            Container content = jf.getContentPane();
//            //创建缓冲图片对象
////            img = new BufferedImage(
////                    this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
////            //得到图形对象
////            Graphics2D g2d = img.createGraphics();
////
////            //将窗口内容面板输出到图形对象中
////            content.printAll(g2d);
//            try {
//                img = new Robot().createScreenCapture(new Rectangle(x+10,y+10,width-260,height-160));
//            } catch (AWTException e1) {
//                e1.printStackTrace();
//            }
////            saveToFile();
////            imageIcon.setImage(imageIcon.getImage().
////                    getScaledInstance(imageIcon.getIconWidth() / 4, imageIcon.getIconHeight() / 4, Image.SCALE_FAST));
////            chatPane.insertIcon(imageIcon);
////            jf.dispose();
//        }
//    }
//
//
//    public String saveToFile() {//保存图片
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyHHmmss");
//        String name = sdf.format(new Date());
//        String format = "jpg";
//        path =  "images/drawing/" + name + "." + format;
//
//
//        System.out.println(path + name + "." + format);
//        File f = new File(path);
//        try {
//            ImageIO.write(img, format, new FileOutputStream(path));
//            imageIcon = new ImageIcon(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return path;
//    }
//
////    public String getPath(){
////
////    }
//
//}
