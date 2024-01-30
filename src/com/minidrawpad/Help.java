package com.minidrawpad;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
//�����˵����ܵ�������
public class Help extends JFrame {
	private DrawPad  drawpad = null;
	Help(DrawPad dp)
	{
		drawpad = dp;
	}
	
	public void MainHeip()
	  {
	  	JOptionPane.showMessageDialog(this,"СС��ͼ������ĵ���","СС��ͼ��",JOptionPane.WARNING_MESSAGE);
	  } 
	 public void AboutBook()
	  {
	  	JOptionPane.showMessageDialog(drawpad,"СС��ͼ��"+"\n"+"    �汾: 1.1.2"+"\n"
	  	   +"    ����:  ��  ��  ��"+"\n"
	  	   +"    ʱ��:  2009/12/13","СС��ͼ��",JOptionPane.WARNING_MESSAGE);
	  }
}
