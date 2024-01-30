package com.minidrawpad;

import java.awt.*;
import java.awt.event.*;
import java.io.InputStreamReader;
import java.io.Reader;



import javax.swing.*;

// ��������
public class DrawPad extends JFrame implements ActionListener {

	/**
	 * @param FileName DrawPad
	 * @author Liu Jun Guang s
	 * @param V 1.0.0 
	 */
	private static final long serialVersionUID = -2551980583852173918L;
	private JToolBar buttonpanel;//���尴ť���
	private JMenuBar bar ;//����˵���
	private JMenu file,color,stroke,help;//����˵�
	private JMenuItem newfile,openfile,savefile,exit;//file �˵��еĲ˵���
	private JMenuItem helpin,helpmain,colorchoice,strokeitem;//help �˵��еĲ˵���
	private Icon nf,sf,of;//�ļ��˵����ͼ�����
	private JLabel startbar;//״̬��
 
	private DrawArea drawarea;//������Ķ���
	private Help  helpobject; //����һ�����������
	private FileClass fileclass ;//�ļ�����
	String[] fontName; 
	//���幤����ͼ�������
	private String names[] = {"newfile","openfile","savefile","pen","line"
			,"rect","frect","oval","foval","circle","fcircle"
			,"roundrect","froundrect","rubber","color"
			,"stroke","word"};//���幤����ͼ�������
	private Icon icons[];//����ͼ������
	
	private String tiptext[] = {//����������Ƶ���Ӧ�İ�ť�ϸ�����Ӧ����ʾ
			"�½�һ��ͼƬ","��ͼƬ","����ͼƬ","��ʻ�","��ֱ��"
			,"�����ĵľ���","������","�����ĵ���Բ","�����Բ"
			,"�����ĵ�Բ","���Բ","��Բ�Ǿ���","���Բ�Ǿ���"
			,"��Ƥ��","��ɫ","ѡ�������Ĵ�ϸ","���ֵ�����"};
	 JButton button[];//���幤�����еİ�ť��
	private JCheckBox bold,italic;//����������ķ�񣨸�ѡ��
	private JComboBox stytles ;//�������е��������ʽ�������б�
	public DrawPad(String string) {
		// TODO ������Ĺ��캯��
		super(string);
	    //�˵��ĳ�ʼ��
	    file = new JMenu("�ļ�");
	    color = new JMenu("��ɫ");
	    stroke = new JMenu("����");
	    help = new JMenu("����");
	    bar = new JMenuBar();//�˵����ĳ�ʼ��
	    
	    //�˵�����Ӳ˵�
	    bar.add(file);
	    bar.add(color);
	    bar.add(stroke);
	    bar.add(help);
	    
	    //��������Ӳ˵���
	    setJMenuBar(bar);
	    
	    //�˵�����ӿ�ݼ�
	    file.setMnemonic('F');//����ALT+��F��
	    color.setMnemonic('C');//����ALT+��C��
	    stroke.setMnemonic('S');//����ALT+��S��
	    help.setMnemonic('H');//����ALT+��H��
	   
	    //File �˵���ĳ�ʼ��
	    try {
			Reader reader = new InputStreamReader(getClass().getResourceAsStream("images/drawPad_icon"));//��ȡ�ļ�����·��Ϊ��׼
		} catch (Exception e) {
			// TODO �ļ���ȡ����
			JOptionPane.showMessageDialog(this,"ͼƬ��ȡ����","����",JOptionPane.ERROR_MESSAGE);
		}
//	    nf = new ImageIcon(getClass().getResource("images/drawPad_icon/newfile.jpg"));//����ͼ��
//	    sf = new ImageIcon(getClass().getResource("images/drawPad_icon/savefile.jpg"));
//	    of = new ImageIcon(getClass().getResource("images/drawPad_icon/openfile.jpg"));
	    newfile = new JMenuItem("�½�");
	    openfile = new JMenuItem("��");
	    savefile = new JMenuItem("����");
	    exit = new JMenuItem("�˳�");
	    
	    //File �˵�����Ӳ˵���
	    file.add(newfile);
	    file.add(openfile);
	    file.add(savefile);
	    file.add(exit);
	    
	    //File �˵�����ӿ�ݼ�
	    newfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));
	    openfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK));
	    savefile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
	    exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,InputEvent.CTRL_MASK));
	   
	    //File �˵����ע�����
	    newfile.addActionListener(this);
	    openfile.addActionListener(this);
	    savefile.addActionListener(this);
	    exit.addActionListener(this);
	    
	    //Color �˵���ĳ�ʼ��
	    colorchoice = new JMenuItem("��ɫ��");
	    colorchoice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK));
	    colorchoice.addActionListener(this);
	    color.add(colorchoice);
	    //Help �˵���ĳ�ʼ��
	    helpmain = new JMenuItem("��������");
	    helpin = new JMenuItem("����СС��ͼ��");
	   
	    //Help �˵�����Ӳ˵���
	    help.add(helpmain);
	    help.addSeparator();//��ӷָ���
	    help.add(helpin);
	    
	    //Help �˵����ע�����
	    helpin.addActionListener(this);
	    helpmain.addActionListener(this);
	    
	    //Stroke �˵���ĳ�ʼ��
	    strokeitem = new JMenuItem("���û���");
	    strokeitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,InputEvent.CTRL_MASK));
	    stroke.add(strokeitem);
	    strokeitem.addActionListener(this);
	    
	    //�������ĳ�ʼ��
	    buttonpanel = new JToolBar( JToolBar.HORIZONTAL);
	    icons = new ImageIcon[names.length];
	    button = new JButton[names.length];
	    for(int i = 0 ;i<names.length;i++)
	    {
	        icons[i] = new ImageIcon(getClass().getResource("images/drawPad_icon/"+names[i]+".jpg"));//���ͼƬ������·��Ϊ��׼��
	    	button[i] = new JButton("",icons[i]);//�����������еİ�ť
	    	button[i].setToolTipText(tiptext[i]);//����������Ƶ���Ӧ�İ�ť�ϸ�����Ӧ����ʾ
	    	buttonpanel.add(button[i]);
	    	button[i].setBackground(Color.red);
	    	if(i<3)button[i].addActionListener(this);
	        else if(i<=16) button[i].addActionListener(this);
	    }
	   CheckBoxHandler CHandler = new CheckBoxHandler();//������ʽ������
	   bold = new  JCheckBox("����"); 
	   bold.setFont(new Font(Font.DIALOG,Font.BOLD,30));//��������
	   bold.addItemListener(CHandler);//boldע�����
	   italic = new  JCheckBox("б��");
	   italic.addItemListener(CHandler);//italicע�����
	   italic.setFont(new Font(Font.DIALOG,Font.ITALIC,30));//��������
	   GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();//�������������õ�����
       fontName = ge.getAvailableFontFamilyNames();
	   stytles = new JComboBox(fontName);//�����б�ĳ�ʼ��
	   stytles.addItemListener(CHandler);//stytlesע�����
	   stytles.setMaximumSize(new Dimension(400,50));//���������б�����ߴ�
	   stytles.setMinimumSize(new  Dimension(250,40));
	   stytles.setFont(new Font(Font.DIALOG,Font.BOLD,20));//��������
	  
      //���������������ʽ��
	   buttonpanel.add(bold);
	   buttonpanel.add(italic);
	   buttonpanel.add(stytles);
	   
	   //״̬���ĳ�ʼ��
	    startbar = new JLabel("�ҵ�СС��ͼ��");
	   
	    
	    //�滭���ĳ�ʼ��
	    drawarea = new DrawArea(this);
	    helpobject = new Help(this);
	    fileclass = new FileClass(this,drawarea);
	   
	    
	    Container con = getContentPane();//�õ��������
	    con.add(buttonpanel, BorderLayout.NORTH);
	    con.add(drawarea,BorderLayout.CENTER);
	    con.add(startbar,BorderLayout.SOUTH);
	    Toolkit tool = getToolkit();//�õ�һ��Tolkit��Ķ�����Ҫ���ڵõ���Ļ�Ĵ�С��
	    Dimension dim = tool.getScreenSize();//�õ���Ļ�Ĵ�С ������Dimension����
	    setBounds(40,40,dim.width-70,dim.height-100);
	    setVisible(true);
	    validate();
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	//����״̬����ʾ���ַ�
	public void setStratBar(String s) {
		startbar.setText(s);
	}
	public void actionPerformed(ActionEvent e) {
		// TODO �¼��Ĵ���
		for(int i = 3; i<=13;i++)
		{
			if(e.getSource() ==button[i])
			{
				drawarea.setCurrentChoice(i);
				drawarea.createNewitem();
				drawarea.repaint();
		    }
			
		}
	    if(e.getSource() == newfile||e.getSource() == button[0])//�½�
		{fileclass.newFile();}
		else if(e.getSource() == openfile||e.getSource() == button[1])//��
		{fileclass.openFile();}
		else if(e.getSource() == savefile||e.getSource() == button[2])//����
		{fileclass.saveFile();}
		else if(e.getSource() == exit)//�˳�����
		{System.exit(0);}
		else if(e.getSource() == colorchoice||e.getSource() == button[14])//������ɫ�Ի���
		{
			drawarea.chooseColor();//��ɫ��ѡ��
	    }
		else if(e.getSource() == button[15]||e.getSource()==strokeitem)//���ʴ�ϸ
		{
			drawarea.setStroke();//���ʴ�ϸ�ĵ���
		}
		else if(e.getSource() == button[16])//�������
		{   JOptionPane.showMessageDialog(null, "�뵥��������ȷ���������ֵ�λ�ã�","��ʾ"
				,JOptionPane.INFORMATION_MESSAGE); 
			drawarea.setCurrentChoice(14);
			drawarea.createNewitem();
			drawarea.repaint();
		}
		
		else if(e.getSource() == helpin)//������Ϣ
		{helpobject.AboutBook();}
		else if(e.getSource() == helpmain)//��������
		{helpobject.MainHeip();}
		
		
	}
	
	//������ʽ�����ࣨ���塢б�塢�������ƣ�
	public  class CheckBoxHandler implements ItemListener
	{

		
		public void itemStateChanged(ItemEvent ie) {
			// TODO ������ʽ�����ࣨ���塢б�塢�������ƣ�
			if(ie.getSource() == bold)//�������
			{
				if(ie.getStateChange() == ItemEvent.SELECTED)
				drawarea.setFont(1, Font.BOLD);
				else 
					drawarea.setFont(1, Font.PLAIN);
			}
			else if(ie.getSource() == italic)//����б��
			{
				if(ie.getStateChange() == ItemEvent.SELECTED)
					drawarea.setFont(2, Font.ITALIC);
				else drawarea.setFont(2, Font.PLAIN);
				
			}
			else if(ie.getSource() == stytles)//���������
			{   
				drawarea.stytle = fontName[stytles.getSelectedIndex()];
			}
		}
		
	}
}


