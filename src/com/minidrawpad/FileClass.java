package com.minidrawpad;

import java.awt.Color;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.*;

//�ļ��� ���ļ��Ĵ򿪡��½������棩
public class FileClass {
   private DrawPad drawpad;
   DrawArea drawarea = null;
    FileClass(DrawPad dp,DrawArea da) {
		drawpad = dp;
		drawarea = da;
	}
    
	public void newFile() {
		// TODO �½�ͼ��
		drawarea.setIndex(0);
		drawarea.setCurrentChoice(3);//����Ĭ��Ϊ��ʻ�
		drawarea.setColor(Color.black);//������ɫ
		drawarea.setStroke(1.0f);//���û��ʵĴ�ϸ
		drawarea.createNewitem();
		drawarea.repaint();
	}

	public void openFile() {
		// TODO ��ͼ��
		
		 //JFileChooser Ϊ�û�ѡ���ļ��ṩ��һ�ּ򵥵Ļ���
		 JFileChooser filechooser = new JFileChooser();
		 filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		  /* FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "JPG & GIF Images", "jpg", "gif");//����ֻ��ʾ .jpg �� .gif ͼ��
		   filechooser.setFileFilter(filter);*/
		    int returnVal = filechooser.showOpenDialog(drawpad);
		    
		    if(returnVal == JFileChooser.CANCEL_OPTION) {//�������ȷ����ť��ִ������ó���
		       return;
		    }
		    File fileName = filechooser.getSelectedFile();//getSelectedFile()����ѡ�е��ļ�
		    fileName.canRead();
		    if(fileName == null || fileName.getName().equals(""))//�ļ���������ʱ
		    {
		    	JOptionPane.showMessageDialog(filechooser,"�ļ���","�������ļ�����",JOptionPane.ERROR_MESSAGE);
		    }
		    
		    else {
		    	
					try {
						FileInputStream ifs = new FileInputStream(fileName);
						ObjectInputStream input = new ObjectInputStream(ifs);
						
						int countNumber = 0;
						Drawing inputRecord;
						countNumber = input.readInt();
						for(int i =0;i<countNumber;i++)
						{
							drawarea.setIndex(i);
							inputRecord = (Drawing)input.readObject();
							drawarea.itemList[i] = inputRecord;
						}
						drawarea.createNewitem();
						input.close();
						drawarea.repaint();
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(drawpad,"û���ҵ�Դ�ļ���","û���ҵ�Դ�ļ�",JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(drawpad,"���ļ��Ƿ�������","��ȡ����",JOptionPane.ERROR_MESSAGE);
					} catch (ClassNotFoundException e) {
						JOptionPane.showMessageDialog(drawpad,"���ܴ�������","�ѵ��ļ�ĩβ",JOptionPane.ERROR_MESSAGE);
					}
				
		    }
	}
    
	//����ͼ���ļ�����Σ��õ��ļ��ԣ�FileOupputSream������
	public void saveFile() {
		// TODO ����ͼ��
		
		 //JFileChooser Ϊ�û�ѡ���ļ��ṩ��һ�ּ򵥵Ļ���
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//setFileSelectionMode()���� JFileChooser���������û�ֻѡ���ļ���ֻѡ��Ŀ¼�����߿�ѡ���ļ���Ŀ¼��
		int result = filechooser.showSaveDialog(drawpad);
		if(result == JFileChooser.CANCEL_OPTION){
        	return ;
        }
        
        File fileName = filechooser.getSelectedFile();//getSelectedFile()����ѡ�е��ļ�
	    fileName.canWrite();//����Ӧ�ó����Ƿ�����޸Ĵ˳���·������ʾ���ļ�
	    if(fileName == null || fileName.getName().equals(""))//�ļ���������ʱ
	    {
	    	JOptionPane.showMessageDialog(filechooser,"�ļ���","�������ļ�����",JOptionPane.ERROR_MESSAGE);
	    }
	    else {
	    	try {
				fileName.delete();//ɾ���˳���·������ʾ���ļ���Ŀ¼
				FileOutputStream fos = new FileOutputStream(fileName+".xxh");//�ļ���������ֽڵķ�ʽ���
				//���������
				ObjectOutputStream output = new ObjectOutputStream(fos);
				//Drawing record;
				
				output.writeInt(drawarea.getIndex());
				
				for(int i = 0;i<drawarea.getIndex() ;i++)
				{
					Drawing p = drawarea.itemList[i];
					output.writeObject(p);
					output.flush();//ˢ�¸����Ļ��塣�˲�����д�������ѻ��������ֽڣ���������ˢ�µ��ײ����С�
					               //�����е�ͼ����Ϣǿ�Ƶ�ת���ɸ������Ի��洢���ļ���    
				}
				output.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
}
