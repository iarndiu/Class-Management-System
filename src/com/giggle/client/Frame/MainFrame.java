package com.giggle.client.Frame;

import com.giggle.bean.Message;
import com.giggle.client.base.*;
import com.giggle.client.iComponent.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MainFrame implements NewMessageListener {
    private String chatTargetAccount;

    JFrame frame;
    String account,name,password,avatarPath;
    int user_type;
    private static final int w_height = 800,w_width = 1200,c_height=650,c_width=800,l_width=320,l_height=c_height,LOCATION_X = 297,LOCATION_Y = 72;
    private int xOld,yOld;
    private static final String w_title = "mainframe",chatTitle="CHAT",AnnTitle="ANNOUNCE",fileTItle="FILE",voteTitle="VOTE";
    JTabbedPane tabbedPane,tabbedList;
    JPanel panelContainer,panelContainerB,panelContainerT,pFriendList,pclass,rightPanel,leftPanel;
    JButton bAddF,bEmoji,bDrawpad;
    DateFormat simpleDateFormat;
    ILineBorder LineBorder = new ILineBorder(new Color(192, 192, 192), 1, true);
    JScrollPane scrollPane;

    private int width = 1152;
    private int height = 200;
    private int titleBarHeight = 65;
    private int paneHeight = height - titleBarHeight;
    private int groupWidth = 300;
    private int paneWidth = width - groupWidth;
    private int chatTitleBarHeight = 50;
    private int chatMessageHeight = 160;
    private int chatHeight = paneHeight - chatMessageHeight - chatTitleBarHeight;
    private int msgToolBarHeight = 30;
    private int msgSendHeight = 35;
    private int msgTextAreaHeight = chatHeight - msgSendHeight - msgToolBarHeight;
    private int chatBarHeight = 60;

    private int noticeWidth = 600;
    private int noticeHeight = 180;


    public MainFrame(String account){
        this.account=account;
        user_type = PostClient.userType(account);
        password = PostClient.password(account);
        avatarPath = "images/avatars/"+account+".JPG";//

        System.out.println("frame");
        frame = new JFrame();
        frame.setUndecorated(true);

        pclass=new JPanel();
        pclass.setSize(320,w_height);
        tabbedList = new JTabbedPane();
        tabbedList.setSize(320,w_height);

        pFriendList=new JPanel();

        JPanel addF = new JPanel();
        bAddF=new JButton("add");
        addF.setPreferredSize(new Dimension(10,10));
        addF.add(bAddF);

        JPanel setAdmin = new JPanel();
        JButton bset=new JButton("setAdmin");
        setAdmin.setPreferredSize(new Dimension(10,10));
        setAdmin.add(bset);

        JPanel pdiary = new JPanel();
        JButton bdiary=new JButton("manager diary");
        pdiary.setPreferredSize(new Dimension(10,10));
        pdiary.add(bdiary);

        pFriendList.setLayout(new BoxLayout(pFriendList, BoxLayout.Y_AXIS));
        pFriendList.setSize(320,w_height);//////////////////////////////

        refreshFriend();
        refreshClass();
        pFriendList.add(addF);
        pFriendList.add(setAdmin);
        pFriendList.add(pdiary);
        tabbedList.addTab("friend",pFriendList);
        tabbedList.addTab("class",pclass);
        leftPanel = new JPanel();
        leftPanel.setSize(l_width,w_height);
        leftPanel.add(tabbedList);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setSize(c_width,100);
        tabbedPane=new JTabbedPane();

        createChatCard();
        createFileCard();
        createNoticeCard();
        createVoteCard();
        rightPanel.add(tabbedPane, BorderLayout.CENTER);

        ImageIcon head=new ImageIcon(avatarPath);
        ImageIcon iexit=new ImageIcon("images/icons/Exit.png");

        Image avatar = head.getImage();
        head = IRoundImageIcon.getRoundImageIcon(avatar);
        IRoundImageIcon.scaleImage(head, 70);

        IRoundImageIcon.scaleImage(iexit, 20);


        JLabel bInfo = new JLabel(head);
        bInfo.setBounds(0,0,120,120);

        JButton exit = new JButton(iexit);
        panelContainerT=new JPanel();
        panelContainerT.setLayout(new BorderLayout());
        panelContainerT.setBackground(Color.gray);
        panelContainerT.add(exit,BorderLayout.WEST);
        panelContainerT.add(bInfo,BorderLayout.EAST);
        panelContainerT.setSize(w_width,200);

        panelContainerB = new JPanel();
        panelContainerB.setLayout(new BoxLayout(panelContainerB,BoxLayout.X_AXIS));
        panelContainerB.add(leftPanel);
        panelContainerB.add(rightPanel);
        panelContainerB.setOpaque(true);

        panelContainer=new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer,BoxLayout.Y_AXIS));
        panelContainer.add(panelContainerT);
        panelContainer.add(panelContainerB);

        frame.setSize(new Dimension(w_width, w_height));
        frame.setLocation(LOCATION_X, LOCATION_Y);
        frame.setTitle(w_title);
        frame.setContentPane(panelContainer);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);

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

        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                frame.dispose();
                System.exit(0);
            }
        });
        bInfo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String message="account : "+account+"\nname : "+PostClient.getUserName(account)+"\nuser_type : "+PostClient.userType(account)+"\nuser_count : "+PostClient.userCount();
                JOptionPane.showMessageDialog(null, message, "Info", 1);
            }
        });
        bset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String account = (String) JOptionPane.showInputDialog(null, "account：\n", "setAdmin", JOptionPane.PLAIN_MESSAGE, null, null,"");
                String type =  (String)JOptionPane.showInputDialog(null, "type：\n", "setAdmin", JOptionPane.PLAIN_MESSAGE, null, null,"");
                int itype= Integer.parseInt(type);
                var code = PostClient.setAdmin(account,itype);
                if(code>=0){
                    System.out.println("修改"+account+"权限" + (code >= 0));
                    JOptionPane.showMessageDialog(null, "success!", "hint", 1);
                }else
                    JOptionPane.showMessageDialog(null, "failed", "hint", 1);
            }
        });
        bdiary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog textEditor = new JDialog();
                Panel tempPane = new Panel();
                tempPane.setLayout(new BoxLayout(tempPane,BoxLayout.Y_AXIS));
                JTextArea tfDiary = new JTextArea(20,40);
                tempPane.add(tfDiary);
//                String diary = PostClient.getDiary(account);
//                tfDiary.append(diary);
                tfDiary.setEditable(false);
                textEditor.setLocation(LOCATION_X,LOCATION_Y);
                textEditor.add(tempPane);
                textEditor.pack();
                textEditor.setVisible(true);
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        bAddF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String account = (String) JOptionPane.showInputDialog(null, "account：\n", "add friend", JOptionPane.PLAIN_MESSAGE, null, null,"");
                if (account == null) return;
                var code=PostClient.addFriend(account);
                if(code >= 0) {
                    System.out.println("添加好友" + account + (code >= 0));
                    JOptionPane.showMessageDialog(null, "success!", "hint", 1);
                    refreshFriend();
                    pFriendList.add(bAddF);
                }else if(code==-1)
                    JOptionPane.showMessageDialog(null, "account doesn't exist", "hint", 1);
                else if(code==-2)
                    JOptionPane.showMessageDialog(null, "account has already been added", "hint", 2);
            }
        });
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        initChat();
    }

    private JScrollPane friendListPane;
    private void refreshFriend(){
        var pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        friendListPane = new JScrollPane(pane);
        friendListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        friendListPane.setPreferredSize(new Dimension(320, l_height-150));
        friendListPane.setMaximumSize(new Dimension(320, l_height-150));
        var friends=PostClient.getFriendList();
        pFriendList.removeAll();
        for(var i : friends) {
            System.out.println("c" + i);
            pane.add(createFriendPanel(i));
        }
        pFriendList.add(friendListPane);
        pFriendList.revalidate();
        pFriendList.repaint();
        //没了？？？？！！！！！！！！！！！！！！
    }
    private JPanel createFriendPanel(String friendAccount) {
        var friendName = PostClient.getUserName(friendAccount);
        var pane = new JPanel(new BorderLayout());
        pane.setPreferredSize(new Dimension(320, 80));//90
        pane.setMaximumSize(new Dimension(320, 80));//90
        var infoPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ImageIcon head = new ImageIcon(PostClient.getUserAvatar(friendAccount));
        Image avatar = head.getImage();
        head = IRoundImageIcon.getRoundImageIcon(avatar);
        IRoundImageIcon.scaleImage(head,60);
        JLabel lhead = new JLabel(head);
        JLabel lname = new JLabel("    "+friendName);
        lhead.setPreferredSize(new Dimension(160, 160));//320 90
        lname.setPreferredSize(new Dimension(160, 160));//320 90
        infoPane.setPreferredSize(new Dimension(320,80));
        infoPane.setLayout(new BorderLayout());
        infoPane.add((lname),BorderLayout.EAST);
        infoPane.add(lhead,BorderLayout.WEST);

        infoPane.setBackground(new Color(0xF0F3F4));
        infoPane.setBorder(new EmptyBorder(0, 120, 0, 120));
        pane.add(infoPane);//, BorderLayout.SOUTH);
        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                receiver = friendAccount;
                removeChatBubble();
                messageType = 0;
                getNewMessage(receiver, messageType);
                System.out.println(receiver);
                pane.setForeground(new Color(0xFDFEFE));
                pane.setForeground(Color.red);//改变颜色
                pane.setBorder(BorderFactory.createLoweredBevelBorder());//组件凹陷
                pane.setForeground(Color.gray);
//                pane.setBorder(BorderFactory.createRaisedBevelBorder());
            }
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                pane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                pane.setForeground(new Color(0xFDFEFE));
                pane.setForeground(Color.red);//改变颜色
                pane.setBorder(BorderFactory.createLoweredBevelBorder());//组件凹陷
//                pane.setForeground(Color.gray);
//                pane.setBorder(BorderFactory.createRaisedBevelBorder());
            }
            public void mouseExited(MouseEvent arg0) {
//                pane.setContentAreaFilled(false);
                pane.setForeground(Color.gray);
                pane.setBorder(BorderFactory.createRaisedBevelBorder());
            }

        });
        return pane;
    }

    private JScrollPane classListPane;
    private void refreshClass(){
        var pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        classListPane = new JScrollPane(pane);
        classListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        classListPane.setPreferredSize(new Dimension(320, l_height));
        classListPane.setMaximumSize(new Dimension(320, l_height));
        pclass.removeAll();
        pane.add(createClassPanel());
        pclass.add(classListPane);
        pclass.revalidate();
        pclass.repaint();
    }
    private JPanel createClassPanel(){
        var pane = new JPanel(new BorderLayout());
        pane.setPreferredSize(new Dimension(320, 80));
        pane.setMaximumSize(new Dimension(320, 80));
        var infoPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ImageIcon head = new ImageIcon("images/icons/IMG_0340.JPG");
        Image avatar = head.getImage();
        head = IRoundImageIcon.getRoundImageIcon(avatar);
        IRoundImageIcon.scaleImage(head,60);
        JLabel lhead = new JLabel(head);
        JLabel lclass = new JLabel("class");
        lhead.setPreferredSize(new Dimension(160,160));
        lclass.setPreferredSize(new Dimension(160,160));
        infoPane.setPreferredSize(new Dimension(320,80));
        infoPane.setLayout(new BorderLayout());
        infoPane.add(lhead,BorderLayout.WEST);
        infoPane.add(lclass,BorderLayout.EAST);
//        infoPane.add(new JLabel(head),BorderLayout.WEST);
//        infoPane.add(new JLabel("   class"),BorderLayout.EAST);

        infoPane.setBackground(new Color(0xF0F3F4));
        infoPane.setBorder(new EmptyBorder(0, 120, 0, 120));
        pane.add(infoPane);//, BorderLayout.SOUTH);
        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                receiver = "undefined";
                removeChatBubble();
                messageType = 1;
                getNewMessage(receiver, messageType);
                System.out.println(receiver);
                pane.setForeground(new Color(0xFDFEFE));
                pane.setForeground(Color.red);//改变颜色
                pane.setBorder(BorderFactory.createLoweredBevelBorder());//组件凹陷
                pane.setForeground(Color.gray);
//                pane.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            pane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            pane.setForeground(new Color(0xFDFEFE));
            pane.setForeground(Color.red);//改变颜色
            pane.setBorder(BorderFactory.createLoweredBevelBorder());//组件凹陷
//                pane.setForeground(Color.gray);
//                pane.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        public void mouseExited(MouseEvent arg0) {
//                pane.setContentAreaFilled(false);
            pane.setForeground(Color.gray);
            pane.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        });
        return pane;
    }


    private JPanel noticeCard;
    private void createNoticeCard() {
        var publish = new JButton("Post Announcement");
        var publishPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        publishPane.setBackground(new Color(0xECF0F1));
        publish.addActionListener(e-> {
            // 判断是否是管理员
            if (user_type==1||user_type==-1) {
                JDialog textEditor = new JDialog();
                Panel tempPane = new Panel();
                tempPane.setLayout(new BoxLayout(tempPane,BoxLayout.Y_AXIS));
                JTextArea tfAnn = new JTextArea(40,40);
                tempPane.add(tfAnn);
                Button bsubmit = new Button("Submit");
                tempPane.add(bsubmit);
                textEditor.setLocation(LOCATION_X,LOCATION_Y);
                textEditor.add(tempPane);
                textEditor.pack();
                textEditor.setVisible(true);
                bsubmit.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ke) {
                        var code = PostClient.postAnnouncement(tfAnn.getText());
                        if (code >= 0) {
                            System.out.println("发布公告结果" + (code >= 0));
                            JOptionPane.showMessageDialog(null, "success!", "hint", 2);
                            textEditor.dispose();
                            updateNotice();
                        } else {
                            JOptionPane.showMessageDialog(null, "fail", "hint", 2);
                        }
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "you're not manager", "hint", 2);
            }

        });

        publishPane.add(publish);
        var pane = new JPanel(new BorderLayout());
        noticeCard = new JPanel();
        noticeCard.setBackground(new Color(0xF4F6F7));
        noticeCard.setLayout(new BoxLayout(noticeCard, BoxLayout.Y_AXIS));
        var scrollPane = new JScrollPane(noticeCard);
        scrollPane.setPreferredSize(new Dimension(100, 100));
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        scrollPane.setBackground(new Color(0xF4F6F7));
        pane.add(scrollPane, BorderLayout.CENTER);
        pane.add(publishPane, BorderLayout.NORTH);
        tabbedPane.addTab(AnnTitle,pane);
        updateNotice();
    }
    private void updateNotice() {
        var list = PostClient.getAnnouncementList();
        if (list == null) return;
        noticeCard.removeAll();
        for (var i : list) {
            var pane = createAnnounceNotice(i);
            noticeCard.add(pane);
        }
        noticeCard.revalidate();
        noticeCard.repaint();
    }
    private JPanel createEditorPane(String html) {
        var pane = new JPanel();
        pane.setBackground(new Color(0xF4F6F7));
        var editorPane = new IEditorPane();
        var webScrollPane = new JScrollPane(editorPane);
        webScrollPane.setPreferredSize(new Dimension(noticeWidth, noticeHeight));
        webScrollPane.setBorder(null);
        webScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        webScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        webScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        webScrollPane.getVerticalScrollBar().setUnitIncrement(15);
        webScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        editorPane.setText(html);
        pane.setBorder(null);
        pane.add(webScrollPane);
        return pane;
    }
    private JPanel createAnnounceNotice(int announceId) {
        var text = (String)PostClient.getAnnouncement(announceId).get(1);
        var pane = new JPanel(new BorderLayout());
        pane.setPreferredSize(new Dimension(paneWidth, noticeHeight));
        pane.setMaximumSize(new Dimension(paneWidth, noticeHeight));
        pane.add(createEditorPane(text), BorderLayout.CENTER);
        var infoPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        var sender = PostClient.getAnnouncement(announceId).get(3);
        var time = PostClient.getAnnouncement(announceId).get(4);
        infoPane.add(new JLabel("From ：" + sender));
        infoPane.add(new JLabel("  At ：" + time));
        infoPane.setBackground(new Color(0xF0F3F4));
        infoPane.setBorder(new EmptyBorder(0, 120, 0, 120));
        pane.add(infoPane, BorderLayout.SOUTH);
        return pane;
    }

    private JPanel voteCard;
    private void createVoteCard() {
            var publish = new JButton("Post vote");
            var publishPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            publishPane.setBackground(new Color(0xECF0F1));
            publish.addActionListener(e-> {
                // 判断是否是管理员
                if (user_type==1||user_type==-1) {
                    JDialog textEditor = new JDialog();
                    Panel tempPane = new Panel();
                    tempPane.setLayout(new BoxLayout(tempPane,BoxLayout.Y_AXIS));
                    JTextArea tfVote = new JTextArea(40,40);
                    tempPane.add(tfVote);
                    Button bsubmit = new Button("Submit");
                    tempPane.add(bsubmit);
                    textEditor.setLocation(LOCATION_X,LOCATION_Y);
                    textEditor.add(tempPane);
                    textEditor.pack();
                    textEditor.setVisible(true);
                    bsubmit.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent ke) {
                            var code = PostClient.postVote(tfVote.getText());
                            if (code >= 0) {
//                                setEnabled(true);
                                System.out.println("发布投票结果" + (code >= 0));
                                JOptionPane.showMessageDialog(null, "success!", "hint", 2);
                                textEditor.dispose();
                                updateVote();
                            } else {
                                JOptionPane.showMessageDialog(null, "fail", "hint", 2);
                            }
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "you're not manager", "hint", 2);
                }

            });

            publishPane.add(publish);

            var pane = new JPanel(new BorderLayout());
            voteCard = new JPanel();
            voteCard.setBackground(new Color(0xF4F6F7));
            voteCard.setLayout(new BoxLayout(voteCard, BoxLayout.Y_AXIS));
            var scrollPane = new JScrollPane(voteCard);
            scrollPane.setPreferredSize(new Dimension(100, 100));
            scrollPane.getVerticalScrollBar().setUnitIncrement(20);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
            scrollPane.setBackground(new Color(0xF4F6F7));
            pane.add(scrollPane, BorderLayout.CENTER);
            pane.add(publishPane, BorderLayout.NORTH);
            tabbedPane.addTab( voteTitle,pane);
            updateVote();
        }
    private void updateVote() {
            var voteList = PostClient.getVoteList();
            if (voteList == null) return;
            voteCard.removeAll();
            for (var i : voteList) {
                var pane = createVote(i);
                voteCard.add(pane);
            }
            voteCard.revalidate();
            voteCard.repaint();
        }
    private JPanel createVoteNotice(int voteId) {
        var text = (String)PostClient.getVote(voteId).get(1);
        var pane = new JPanel(new BorderLayout());
        pane.setPreferredSize(new Dimension(paneWidth, noticeHeight + 30));
        pane.setMaximumSize(new Dimension(paneWidth, noticeHeight + 30));
        pane.add(createEditorPane(text), BorderLayout.CENTER);
        var infoPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        var sender = PostClient.getVote(voteId).get(6);
        var time = PostClient.getVote(voteId).get(7);
        infoPane.add(new JLabel("From ：" + sender));
        infoPane.add(new JLabel("  At ：" + time));
        infoPane.setBackground(new Color(0xF0F3F4));
        infoPane.setBorder(new EmptyBorder(0, 120, 0, 120));
        pane.add(infoPane, BorderLayout.SOUTH);
        return pane;
    }
    private JPanel createVote(int voteId) {
            var pane = new JPanel(new BorderLayout());
            pane.setPreferredSize(new Dimension(paneWidth, noticeHeight));
            pane.setMaximumSize(new Dimension(paneWidth, noticeHeight));
//            var notice = PostClient.getVote(voteId);//notice是object
            var noticePane = createVoteNotice(voteId);
            var voteStatus = PostClient.getVoteStatus(voteId);
            pane.add(noticePane, BorderLayout.CENTER);

            var operator = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            operator.setBorder(new EmptyBorder(0, 120, 0, 120));
            operator.setBackground(new Color(0xF0F3F4));
            switch (voteStatus) {
                case 0:
                    // 投票阶段
                    var agree = new JButton("Agree");
                    var disagree = new JButton("Disagree");
                    var advice = new JButton("advice");
                    agree.addActionListener(e-> {
                        var code = PostClient.vote(voteId,0);
                        if (code >= 0) {
                            JOptionPane.showMessageDialog(null, "success!", "hint", 2);
                        } else {
                            JOptionPane.showMessageDialog(null, "failed!", "hint", 2);
                        }
                        updateVote();
                    });
                    disagree.addActionListener(e-> {
                        var code = PostClient.vote(voteId,1);
                        if (code >= 0) {
                            JOptionPane.showMessageDialog(null, "success!", "hint", 2);
                        } else {
                            JOptionPane.showMessageDialog(null, "failed!", "hint", 2);
                        }
                        updateVote();
                    });
                    advice.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JDialog textEditor = new JDialog();
                            Panel tempPane = new Panel();
                            tempPane.setLayout(new BoxLayout(tempPane,BoxLayout.Y_AXIS));
                            JTextArea tfAdvice = new JTextArea(20,40);
                            tempPane.add(tfAdvice);
                            String advices = PostClient.getAdvice(voteId);
                            tfAdvice.append(advices);
                            tfAdvice.setEditable(false);
                            JTextArea tfAddAdvice = new JTextArea(20,40);
                            tempPane.add(tfAddAdvice);
                            Button bsubmit = new Button("Submit");
                            tempPane.add(bsubmit);
                            textEditor.setLocation(LOCATION_X,LOCATION_Y);
                            textEditor.add(tempPane);
                            textEditor.pack();
                            textEditor.setVisible(true);

                            bsubmit.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent ke) {
                                    var code = PostClient.addAdvice(voteId,tfAddAdvice.getText());
                                    if (code >= 0) {
//                                setEnabled(true);
                                        System.out.println("添加建议" + (code >= 0));
                                        JOptionPane.showMessageDialog(null, "success!", "hint", 2);
                                        textEditor.dispose();
                                        updateVote();
                                    } else {
                                        JOptionPane.showMessageDialog(null, "failed", "hint", 2);
                                    }
                                }
                            });
                        }
                    });
                    operator.add(agree);
                    operator.add(disagree);
                    operator.add(advice);
                    break;
                case 1:
                    var result1 = new JLabel("Voted > " + "For : " +PostClient.getVote(voteId).get(4) + "  " + "Against : " + PostClient.getVote(voteId).get(5) );
                    operator.add(result1);
                    break;
                case 2:
                    var result2 = new JLabel("Ended > " + "For : " +PostClient.getVote(voteId).get(4) + "  " + "Against : " + PostClient.getVote(voteId).get(5) );
                    operator.add(result2);
                    break;
            }
            pane.add(operator, BorderLayout.SOUTH);
            return pane;
        }

    public String chooseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(new TextField()) == JFileChooser.APPROVE_OPTION) {
            var file = chooser.getSelectedFile();
            var path = file.getParent() + "/" + file.getName();
            if (new File(path).exists()) {
                return path;
            }
        }
        return null;
    }

    public String chooseDir() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(new TextField()) == JFileChooser.APPROVE_OPTION) {
            var path = chooser.getSelectedFile().getPath();
            return path;
        }
        return null;
    }
    private JPanel fileCard;
    private void createFileCard() {
        var upload = new JButton("upload");
        var uploadPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        uploadPane.setBackground(new Color(0xECF0F1));

        upload.addActionListener(e-> {
            // 上传文件
            var uploadPath = chooseFile();
            if (uploadPath == null) return;
            var fileId = FileClient.upload(uploadPath, account, 1);
            if (fileId != -1) {
                System.out.println("上传文件成功：" + fileId);
                JOptionPane.showMessageDialog(null, "success!", "hint", 1);
            }else {
                System.out.println("上传文件失败");
                JOptionPane.showMessageDialog(null, "failed", "hint", 1);
            }
            updateFile();
        });

        uploadPane.add(upload);

        var pane = new JPanel(new BorderLayout());
        fileCard = new JPanel();
        fileCard.setBackground(new Color(0xF4F6F7));
        fileCard.setLayout(new BoxLayout(fileCard, BoxLayout.Y_AXIS));
        var scrollPane = new JScrollPane(fileCard);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        scrollPane.setBackground(new Color(0xF4F6F7));
        pane.add(scrollPane, BorderLayout.CENTER);
        pane.add(uploadPane, BorderLayout.NORTH);
        tabbedPane.addTab(fileTItle,pane);
        updateFile();
    }
    private void updateFile() {
        var list = PostClient.getFileList();
        if (list == null) return;
        fileCard.removeAll();
        for (var i : list) {
            var pane = createFile(i);
            fileCard.add(pane);
        }
        fileCard.revalidate();
        fileCard.repaint();
    }
    private JPanel createFile(int fileId) {
        var fileName = (String)PostClient.getFileInfo(fileId).get(1);
        var pane = new JPanel(new BorderLayout());
        pane.setBorder(new EmptyBorder(10, 0, 0, 0));
        pane.setPreferredSize(new Dimension(paneWidth * 8 / 10, 100));
        pane.setMaximumSize(new Dimension(paneWidth * 8 / 10, 100));
        var filePane = new JPanel(new FlowLayout());
        var icon = new ImageIcon("images/icons/folder.png");
        IRoundImageIcon.scaleImage(icon, 40);
        var image = new JLabel(icon);
        filePane.add(image);
        var labelPane = new JPanel();
        labelPane.setPreferredSize(new Dimension(paneWidth * 6 / 10, 30));
        labelPane.setMaximumSize(new Dimension(paneWidth * 6 / 10, 30));
        var label = new JLabel(fileName);
        labelPane.add(label);
        var button = new JButton("Download");
        button.addActionListener(e-> {
            if (!e.getActionCommand().equals("Download")) return;
            button.setText("Downloading...");
            var downloadPath = chooseDir();
            if (downloadPath == null) return;
            var flag = FileClient.download(fileId,downloadPath + "/" + PostClient.getFileInfo(fileId).get(1));
            if (flag) {
                button.setText("Download");
                System.out.println("文件下载成功");
                JOptionPane.showMessageDialog(null, "success!", "hint", 1);
            }
        });
        filePane.add(labelPane);
        filePane.add(button);
        pane.add(filePane, BorderLayout.CENTER);
        var infoPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        var sender = PostClient.getFileInfo(fileId).get(4);
        infoPane.add(new JLabel("From ：" + sender));
        infoPane.setBackground(new Color(0xF0F3F4));
        infoPane.setBorder(new EmptyBorder(0, 0, 0, 30));
        pane.add(infoPane, BorderLayout.SOUTH);
        return pane;
    }

    private ITextPane msgInput;
    private JPanel chatPane;

    private void addChatBubble(IBubble bubble, String avatarPath, String sender) {
        var avatar = new ImageIcon(avatarPath);//getAvatar(avatarId));
        Image avt = avatar.getImage();
        avatar = IRoundImageIcon.getRoundImageIcon(avt);
        IRoundImageIcon.scaleImage(avatar,50);
        JPanel avatarPane = new JPanel();
        JLabel lavatar = new JLabel(avatar);
        lavatar.setBounds(0,0,80,80);
        lavatar.setPreferredSize(new Dimension(30, 30));
        avatarPane.add(lavatar);
        avatarPane.setOpaque(false);
        JPanel panel = new JPanel(new FlowLayout(bubble.isLeft() ? FlowLayout.LEFT : FlowLayout.RIGHT));
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        panel.setOpaque(false);

        if (bubble.getPreferredSize().width > paneWidth * 0.65) {
            bubble.setPreferredSize((int) (paneWidth * 0.65));
        }
        if (bubble.isLeft()) panel.add(avatarPane);
        panel.add(bubble);
        if (!bubble.isLeft()) panel.add(avatarPane);
        avatarPane.setPreferredSize(new Dimension(avatarPane.getPreferredSize().width, bubble.getPreferredSize().height));
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, panel.getPreferredSize().height));
        chatPane.add(panel);
    }
    private void addChatBubble(ArrayList<Message> list) {
//        int maxChatCount = 10;
//        if (list.size() > maxChatCount && !append) {
//            list = list.subList(list.size() - maxChatCount - 1, list.size());
//        }
        for (var i : list) {
//            if(((i.content).substring(0,7)).equals("%IMAGE%")){
//                int endIndex = (i.content).length()+1;
//                var fileId = Integer.parseInt((i.content).substring(7,endIndex));
//                FileClient.download(fileId,"images/icons/DownEmoji");
//                addChatBubble(createChatBubbleImage(i), 1, PostClient.getUserName(i.sender));
//            }
            addChatBubble(createChatBubble(i), PostClient.getUserAvatar(i.sender), PostClient.getUserName(i.sender));

        }
        frame.revalidate();
        frame.repaint();
    }

    //自动换行？？没写
    private IBubble createChatBubble(Message message) {
        var bubble = new IBubble(!message.sender.equals(account));
        bubble.setBackground(new Color(bubble.isLeft() ? 0xECF0F1 : 0x5DADE2));
        bubble.setForeground(new Color(bubble.isLeft() ? 0x424949 : 0xFFFFFF));
        // 解析消息
        var text = message.content;
        System.out.println(text);
        if(text.length()>=7) {
            if (text.substring(0, 7).equals("%IMAGE%")) {
                System.out.println("exe");
                imgId = Integer.parseInt(text.substring(7));
                var path = "images/sendImg/" + imgId;
                FileClient.download(imgId, path);
                ImageIcon img = new ImageIcon(path);
                bubble.insertIcon(img);
            }
        }else {
            // 移除多余空行
            text = text.replaceAll("(?m)^\\s+$", "");
            var m = Pattern.compile("(?<!\\\\)<([^<>]*)(?<!\\\\)>").matcher(text);
            while (m.find()) {
                var result = new StringBuffer();
                m.appendReplacement(result, "");
                bubble.insertText(result.toString());
            }
            var result = new StringBuffer();
            m.appendTail(result);
            bubble.insertText(result.toString());
        }
        return bubble;
    }

    private void removeChatBubble() {
        chatPane.removeAll();
        chatPane.revalidate();
        chatPane.repaint();
    }

    int imgId;
    private void createChatCard() {
        JPanel chatCard = new JPanel(new BorderLayout());
        chatCard.setBackground(Color.red);
        // 聊天区域
        chatPane = new JPanel();
        chatPane.setLayout(new BoxLayout(chatPane, BoxLayout.Y_AXIS));
//        chatPane.setPreferredSize(new Dimension(paneWidth, 100));
        chatPane.setBackground(new Color(0xFDFEFE));

        var chatScrollPane = new JScrollPane(chatPane);
//        chatScrollPane.setBackground(Color.blue);
        chatScrollPane.setBorder(null);
        chatScrollPane.setPreferredSize(new Dimension(paneWidth, c_height));
        chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        chatScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        chatCard.add(chatScrollPane, BorderLayout.CENTER);

        // 发送消息区域
        JPanel msJPane = new JPanel(new BorderLayout());
        msJPane.setBackground(new Color(0xFDFEFE));
        msJPane.setPreferredSize(new Dimension(paneWidth, chatMessageHeight));
        chatCard.add(msJPane, BorderLayout.SOUTH);

        // 发送消息工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ((FlowLayout)toolBar.getLayout()).setHgap(10);
        toolBar.setPreferredSize(new Dimension(paneWidth, msgToolBarHeight));
        toolBar.setBackground(new Color(0xFDFEFE));
        toolBar.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        toolBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(0xD0D3D4)), toolBar.getBorder()));
        // 插入图片按钮  buzhidaoduibudui==================================
        ImageIcon emoji = new ImageIcon(("images/icons/emoji.png"));
        IRoundImageIcon.scaleImage(emoji,20);
        JLabel lemoji = new JLabel(emoji);
        lemoji.setPreferredSize(new Dimension(20, 20));
        lemoji.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JDialog emojiChooser = new JDialog();
                Panel tempPane = new Panel();
                tempPane.setLayout(new FlowLayout());
                for(int i=1;i<11;i++){
                    String emojiPath = "images/avatars/"+i+".JPG";
                    ImageIcon emoji = new ImageIcon(emojiPath);
                    IRoundImageIcon.scaleImage(emoji,40);
                    JButton bEmoji = new JButton(emoji);
                    bEmoji.setPreferredSize(new Dimension(40,40));
                    tempPane.add(bEmoji);
                    bEmoji.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            sendImage(emojiPath,messageType);
                        }
                    });
                }
                emojiChooser.setLocation(600,300);
                emojiChooser.add(tempPane);
                emojiChooser.pack();
                emojiChooser.setVisible(true);
            }
        });
        toolBar.add(lemoji);
        // 画笔按钮
        ImageIcon paint = new ImageIcon(("images/icons/drawpad.png"));
        IRoundImageIcon.scaleImage(paint,20);
        JLabel lpaint = new JLabel(paint);
        lpaint.setPreferredSize(new Dimension(20, 20));
        lpaint.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
//                DrawMain drawMain = new DrawMain(chatPane);
//                String drawPath = drawMain.getPath();//获取图片路径
                createDrawPad();
            }
        });
        toolBar.add(lpaint);
        ////////////////////////////////

        msJPane.add(toolBar, BorderLayout.NORTH);
        // 发送消息文本区域
        msgInput = new ITextPane();
        msgInput.setFont(IConfig.createFont(15));
        msgInput.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 12));
        JScrollPane msJScrollPane = new JScrollPane(msgInput);

        msJScrollPane.setBorder(null);
        msJScrollPane.setPreferredSize(new Dimension(paneWidth, msgTextAreaHeight));
        msJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        msJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        msJScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));

        toolBar.setBackground(new Color(0xFDFEFE));
        msJPane.add(msJScrollPane, BorderLayout.CENTER);
        // 发送按钮
        JPanel sendPane = new JPanel(new BorderLayout());
        JButton send = new JButton("send");
        JButton sendImage = new JButton("sendImage");
        // 发送功能
        send.addActionListener(e-> {
            sendMessage(msgInput.getMessage(),messageType);
            msgInput.setText("");
        });
        send.setPreferredSize(new Dimension(100, 30));
        sendPane.setBackground(new Color(0xFDFEFE));
        sendPane.add(send, BorderLayout.EAST);
        //sendImage
        sendImage.addActionListener(e-> {
            sendImage(msgInput.getMessage(),messageType);
            msgInput.setText("");
        });
        sendImage.setPreferredSize(new Dimension(100, 30));
        sendPane.setBackground(new Color(0xFDFEFE));
        sendPane.add(sendImage, BorderLayout.WEST);
        sendPane.setBorder(new EmptyBorder(8, 0, 8, 8));
        msJPane.add(sendPane, BorderLayout.SOUTH);
        tabbedPane.addTab( chatTitle,chatCard);
    }

    int messageType = 0;

    String receiver;

    private ChatClient chat;
    private void initChat() {
        try {
            chat = new ChatClient(this);
            chat.login(account, password);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendMessage(String msg,int msg_type) {

        if (msg_type == 0) {
            chat.sendMessage(receiver, msg, msg_type);
        } else {
            chat.sendMessage("undefined", msg, msg_type);
        }
    }
    private void sendImage(String imagePath,int msg_type) {
        imgId = FileClient.upload(imagePath,account,msg_type);
        if (msg_type == 0) {
            chat.sendMessage(receiver, "%IMAGE%"+imgId, msg_type);
        } else {
            chat.sendMessage("undefined", "%IMAGE%"+imgId, msg_type);
        }
    }

    @Override
    public void receiveMessage(String target, int type) {
        System.out.println("=========与【" + (type == 0 ? PostClient.getUserName(target) : "群消息") + "】的对话=======");
        System.out.println(target + " " + receiver);
        if (target.equals(receiver)) getNewMessage(target, type);
    }

     //target 目标角色
    private void getNewMessage(String target, int type) {
        if (type == 1) target = "undefined";
        var list = PostClient.getMessageList(target, type);
        System.out.println(list);
//        for (var i : list) {
//            System.out.println(i.messageType == 0 ? "【" + PostClient.getUserName(i.sender) + "】" : "【群消息】");
//            System.out.println(i.content);

//            if(target.equals(account)){
                addChatBubble((ArrayList)list);
//            }
//        }
    }


    private JButton save;
    private int x = 600, y = 300;
    private int padwidth = 950, padheight = 600;//800
    private JFrame jf;
    private BufferedImage img;
    String saveDrawPath;
    private void createDrawPad() {
        jf = new JFrame();
        jf.setLocation(x, y);
        jf.setSize(padwidth, padheight);
        jf.setTitle("drawPad");
        jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setLayout(new BorderLayout());
        // 实例化事件监听类
        DrawListener dl = new DrawListener(jf);
        // 实现中间面板
        jf.setBackground(Color.WHITE);
//        jf.add(jf, BorderLayout.CENTER);
        // 实现性状面板
        JPanel ShapePanel = new JPanel();
        ShapePanel.setBackground(Color.black);
        ShapePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        ShapePanel.setBackground(Color.gray);

        String[] Shape = {"直线", "曲线", "圆", "喷枪", "橡皮擦", "矩形", "椭圆", "圆角矩形",
                "弧线",};
        for (int i = 0; i < Shape.length; i++) {
            JButton button = new JButton(Shape[i]);
            button.setBackground(Color.WHITE);
            button.addActionListener(dl); // 添加事件监听机制
            ShapePanel.add(button);
        }
        jf.add(ShapePanel, BorderLayout.NORTH);
        // 实现颜色面板
        JPanel ColorPanel = new JPanel();
        ColorPanel.setBackground(Color.black);
        ColorPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        ColorPanel.setBackground(Color.gray);

        Color[] colors= {Color.BLACK, Color.BLUE, Color.WHITE,
                Color.red, Color.CYAN, Color.green, Color.darkGray, Color.pink};
        for (int i = 0; i < colors.length; i++) {
            JButton button = new JButton();
            button.addActionListener(dl); // 添加事件监听机制
            button.setPreferredSize(new Dimension(30, 30));
            button.setBackground(colors[i]);
            button.setOpaque(true); //foreground设置透明
            button.setBorderPainted(false); //最后显示颜色
            ColorPanel.add(button);
        }

        save = new JButton("send");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    img = new Robot().createScreenCapture(new Rectangle(x+10,y+10,padwidth-260,padheight-160));
                } catch (AWTException e1) {
                    e1.printStackTrace();
                }
                //存储文件
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyHHmmss");
                String name = sdf.format(new Date());
                String format = "jpg";
                saveDrawPath =  "images/drawing/" + name + "." + format;
                System.out.println(saveDrawPath + name + "." + format);
//                File f = new File(path);
                try {
                    ImageIO.write(img, format, new FileOutputStream(saveDrawPath));
//                    imageIcon = new ImageIcon(path);
                } catch (IOException ei) {
                    ei.printStackTrace();
                }
                sendImage(saveDrawPath,messageType);
                jf.dispose();
            }
        });

        ShapePanel.add(save);
        jf.add(ColorPanel, BorderLayout.SOUTH);
        jf.setVisible(true);
        jf.addMouseListener(dl);
        jf.addMouseMotionListener(dl);
    }
}


