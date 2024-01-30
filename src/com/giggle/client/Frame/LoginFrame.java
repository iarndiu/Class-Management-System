package com.giggle.client.Frame;

import com.giggle.client.base.PostClient;
import com.giggle.client.iComponent.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame implements MouseListener {
    public JFrame frame = new JFrame();
    public static final int w_height = 330;
    public static final int w_width = 420;
    public static final String w_title = "LOGIN";
    public static final int LOCATION_X = 497;
    public static final int LOCATION_Y = 242;
    private int xOld,yOld;
    JPanel panelN, panelS, panelW, panelE, panelC,panelContainer;
    public static String usr;
    public static String pwd;
    JButton btnL, btnR,closeButton;
    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();//JPasswordField.LEFT
    JLabel regist, regetpwd;
    JCheckBox rember_pwd, login_auto;
    private String text1;
    private String text2;
    private int distinguish;//用来记录鼠标悬停在哪个位置

    public LoginFrame(){
        panelN = new JPanel();
        panelN.setLayout(null);
        panelN.setPreferredSize(new Dimension(0, 140));

        ImageIcon imageN = new ImageIcon("images/icons/Untitled 4.png");
        JLabel backgroundN = new JLabel(imageN);
        backgroundN.setBounds(0, 0, 420, 180);
        panelN.add(backgroundN);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setSize(w_width,w_height);
        frame.getContentPane().add(layeredPane);
        //背景图层Panel,充当容器---最底层

        //主界面，也就是背景上面的一层Panel，可用于添加控件

        //关闭按钮
        closeButton = new JButton();
        ImageIcon iexit=new ImageIcon("images/icons/close-circle(1).png");
        Image img = iexit.getImage() ;
        Image newimg = img.getScaledInstance( 10, 10,  java.awt.Image.SCALE_SMOOTH ) ;
        iexit = new ImageIcon( newimg );
        closeButton.setIcon(iexit);
        closeButton.setSize(10, 10);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panelW = new JPanel();
        panelW.setLayout(null);
        panelW.setPreferredSize(new Dimension(130, 0));

        ImageIcon imageW = new ImageIcon("images/icons/IMG_0340.JPG");
        Image avatar = imageW.getImage();
        imageW = IRoundImageIcon.getRoundImageIcon(avatar);
        JLabel backgroundW = new JLabel(imageW);
        backgroundW.setBounds(0, 0, 120, 120);

        panelW.add(backgroundW);

        //创建东部面板
        panelE = new JPanel();
        panelE.setLayout(null);
        panelE.setPreferredSize(new Dimension(100, 0));

        regist = new JLabel("找回账号");
        regist.setForeground(new Color(100, 149, 238));
        regist.setBounds(0, 13, 60, 30);
        regist.setFont(new Font("Times", 0, 12));
        regist.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        regist.setForeground(Color.GRAY);

        regetpwd = new JLabel("找回密码");
        regetpwd.setForeground(new Color(100, 149, 238));
        regetpwd.setBounds(0, 43, 60, 30);
        regetpwd.setFont(new Font("Times", 0, 12));
        regetpwd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        regetpwd.setForeground(Color.GRAY);
        panelE.add(regetpwd);
        panelE.add(regist);

        panelS = new JPanel();
        panelS.setLayout(null);
        panelS.setPreferredSize(new Dimension(0, 50));
        ILineBorder LineBorderS = new ILineBorder(new Color(192, 192, 192), 1, true);

        //登录按钮 注册按钮
        ImageIcon imageL = new ImageIcon("images/icons/login.png");
        ImageIcon imageR = new ImageIcon("images/icons/register.png");
        IRoundImageIcon.scaleImage(imageL,50);
        IRoundImageIcon.scaleImage(imageR,50);

        btnL = new JButton(imageL);
        btnR = new JButton(imageR);
        btnL.setBounds(160, 0, imageL.getIconWidth() - 10, imageL.getIconHeight() - 10);
        btnR.setBounds(240, 0, imageR.getIconWidth() - 10, imageR.getIconHeight() - 10);
        btnL.setBorder(LineBorderS);
        btnR.setBorder(LineBorderS);
        panelS.add(btnL);
        panelS.add(btnR);


        //创建中部面板
        panelC = new JPanel();
        panelC.setLayout(null);
        panelC.setPreferredSize(new Dimension(0, 180));
        ILineBorder LineBorderC = new ILineBorder(new Color(192, 192, 192), 1, true);
        //用户名框
        username.setBounds(0, 15, 175, 30);
        username.setBorder(LineBorderC);
        usr = username.getText();
        //密码名框
        password.setBounds(0, 44, 175, 30);
        password.setBorder(LineBorderC);
        pwd = String.valueOf(password.getPassword());
        username.setOpaque(false);//设置文本框透明
        password.setOpaque(false);

        rember_pwd = new JCheckBox("记住密码");
        rember_pwd.setBounds(0, 80, 100, 20);

        login_auto = new JCheckBox("自动登录");
        login_auto.setBounds(100, 80, 100, 20);

        panelC.add(rember_pwd);
        panelC.add(username);
        panelC.add(password);
        panelC.add(login_auto);

        frame.setLocation(LOCATION_X, LOCATION_Y);
        frame.setTitle(w_title);
        frame.setSize(w_width, w_height);
        frame.setResizable(false);
//        login.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setUndecorated(true);//setVisible之前调用

        panelContainer=new JPanel();
        BorderLayout borderLayout = new BorderLayout();
        panelContainer.setLayout(borderLayout);
        panelContainer.setSize(w_width,w_height);
        panelContainer.add(panelN, BorderLayout.NORTH);
        panelContainer.add(panelC, BorderLayout.CENTER);
        panelContainer.add(panelW, BorderLayout.WEST);
        panelContainer.add(panelS, BorderLayout.SOUTH);
        panelContainer.add(panelE, BorderLayout.EAST);
        panelContainer.setVisible(true);

        layeredPane.add(panelContainer);
        layeredPane.add(closeButton);
        frame.setVisible(true);

        btnL.addMouseListener(this);
        btnR.addMouseListener(this);
        /**
         * 实现拖拽窗口的功能
         */
        frame.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e) {
                xOld = e.getX();   //记录鼠标按下时的坐标
                yOld = e.getY();
            }

            public void mouseClicked(MouseEvent e){
                frame.setExtendedState(JFrame.ICONIFIED);
            }
        });

        frame.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int xOnScreen = e.getXOnScreen();
                int yOnScreen = e.getYOnScreen();
                int xNew = xOnScreen - xOld;
                int yNew = yOnScreen - yOld;
                frame.setLocation(xNew, yNew);  //设置拖拽后，窗口的位置
            }
        });

    }

    public void mouseClicked(MouseEvent arg0) {
        System.out.println("已开始向服务器发送登录数据...");
        System.out.println("等待服务器返回登录结果...");
            if (distinguish == 1) {
                text1 = username.getText();//获取用户输入数据
                text2 = String.valueOf(password.getPassword());
                if (text1==null||text2==null) {//id为空
                    JOptionPane.showMessageDialog(null, "please enter your id", "hint", 2);
                } else {//登录判断
                    var code = PostClient.login(text1, text2);
                    System.out.println("登录结果" + (code >= 0));
                    if((code >= 0)) {
                        JOptionPane.showMessageDialog(null, "success", "hint", 2);
                        username.setText("");
                        password.setText("");
                        distinguish = 4;
                        System.out.println("success");
//                        var type = PostClient.userType(text1);
                        new FrameMain(text1);
                        frame.dispose();//登录成功则关闭界面
                    }else
                        JOptionPane.showMessageDialog(null, "check your input", "hint", 2);
                }
            }
            if (distinguish == 2) {//注册
                String account = (String) JOptionPane.showInputDialog(null, "account：\n", "register", JOptionPane.PLAIN_MESSAGE, null, null,"");
                String name = (String) JOptionPane.showInputDialog(null, "name: \n", "register", JOptionPane.PLAIN_MESSAGE, null, null, "");
                String password = (String) JOptionPane.showInputDialog(null, "password：\n", "register", JOptionPane.PLAIN_MESSAGE, null, null, "");
                var code = PostClient.register(account,name,password);
                JOptionPane.showMessageDialog(null, "success!", "hint", 2);
                System.out.println("注册结果" + (code >= 0));
                }
            }



    public void mouseEntered(MouseEvent arg0) {
        if (arg0.getSource() == btnL) {
            distinguish = 1;//鼠标悬停在btnL处则把distinguish置1
            btnL.setForeground(Color.red);//改变颜色
            btnL.setBorder(BorderFactory.createLoweredBevelBorder());//组件凹陷
            btnR.setForeground(Color.gray);
            btnR.setBorder(BorderFactory.createRaisedBevelBorder());
//            btn3.setForeground(Color.gray);
//            btn3.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        if (arg0.getSource() == btnR) {
            distinguish = 2;
            btnL.setForeground(Color.gray);
            btnL.setBorder(BorderFactory.createRaisedBevelBorder());
            btnR.setForeground(Color.red);
            btnR.setBorder(BorderFactory.createLoweredBevelBorder());
//            btn3.setForeground(Color.gray);
//            btn3.setBorder(BorderFactory.createRaisedBevelBorder());
        }
    }

    //鼠标退出三个button组件后恢复
    public void mouseExited(MouseEvent arg0) {
        distinguish = 0;
//        label1.setForeground(Color.gray);
//        label2.setForeground(Color.gray);
        username.setOpaque(false);
        password.setOpaque(false);
        btnL.setContentAreaFilled(false);
        btnL.setForeground(Color.gray);
        btnL.setBorder(BorderFactory.createRaisedBevelBorder());
        btnR.setContentAreaFilled(false);
        btnR.setBorder(BorderFactory.createRaisedBevelBorder());
        btnR.setForeground(Color.gray);
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }
}

