package com.minidrawpad;

import com.minidrawpad.DrawPad;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.event.MouseMotionAdapter;

//��ͼ���ࣨ����ͼ�εĻ��ƺ�����¼���
public class DrawArea extends JPanel {
	DrawPad drawpad = null;
	Drawing[] itemList = new Drawing[5000];; // ����ͼ����
	private int currentChoice = 3;// ����Ĭ�ϻ���ͼ��״̬Ϊ��ʻ�
	int index = 0;// ��ǰ�Ѿ����Ƶ�ͼ����Ŀ
	private Color color = Color.black;// ��ǰ���ʵ���ɫ
	int R, G, B;// ������ŵ�ǰ��ɫ�Ĳ�ֵ
	int f1, f2;// ������ŵ�ǰ����ķ��
	String stytle;// ��ŵ�ǰ����
	float stroke = 1.0f;// ���û��ʵĴ�ϸ ��Ĭ�ϵ��� 1.0
    
	DrawArea(DrawPad dp) {
		drawpad = dp;
		// ��������ó�ʮ����
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		// setCursor ����������״ ��getPredefinedCursor()����һ������ָ�����͵Ĺ��Ķ���

		setBackground(Color.white);// ���û������ı����ǰ�ɫ
		addMouseListener(new MouseA());// �������¼�
		addMouseMotionListener(new MouseB());
		createNewitem();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;// ������ʻ�
		int j = 0;
		while (j <= index) {
			draw(g2d, itemList[j]);
			j++;
		}

	}

	void draw(Graphics2D g2d, Drawing i) {
		i.draw(g2d);// �����ʴ���������������У�������ɸ��ԵĻ�ͼ
	}

	// �½�һ��ͼ�εĻ�����Ԫ����ĳ����
	void createNewitem() {
		if (currentChoice == 14)// �������������Ӧ������Ϊ�ı������ʽ
			setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		else
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		switch (currentChoice) {
		case 3:
			itemList[index] = new Pencil();
			break;
		case 4:
			itemList[index] = new Line();
			break;
		case 5:
			itemList[index] = new Rect();
			break;
		case 6:
			itemList[index] = new fillRect();
			break;
		case 7:
			itemList[index] = new Oval();
			break;
		case 8:
			itemList[index] = new fillOval();
			break;
		case 9:
			itemList[index] = new Circle();
			break;
		case 10:
			itemList[index] = new fillCircle();
			break;
		case 11:
			itemList[index] = new RoundRect();
			break;
		case 12:
			itemList[index] = new fillRoundRect();
			break;
		case 13:
			itemList[index] = new Rubber();
			break;
		case 14:
			itemList[index] = new Word();
			break;
		}
		itemList[index].type = currentChoice;
		itemList[index].R = R;
		itemList[index].G = G;
		itemList[index].B = B;
		itemList[index].stroke = stroke;

	}

	public void setIndex(int x) {// ����index�Ľӿ�
		index = x;
	}

	public int getIndex() {// ����index�Ľӿ�
		return index;
	}

	public void setColor(Color color)// ������ɫ��ֵ
	{
		this.color = color;
	}

	public void setStroke(float f)// ���û��ʴ�ϸ�Ľӿ�
	{
		stroke = f;
	}

	public void chooseColor()// ѡ��ǰ��ɫ
	{
		color = JColorChooser.showDialog(drawpad, "��ѡ����ɫ", color);
		try {
			R = color.getRed();
			G = color.getGreen();
			B = color.getBlue();
		} catch (Exception e) {
			R = 0;
			G = 0;
			B = 0;
		}
		itemList[index].R = R;
		itemList[index].G = G;
		itemList[index].B = B;
	}

	public void setStroke()// ���ʴ�ϸ�ĵ���
	{
		String input;
		input = JOptionPane.showInputDialog("�����뻭�ʵĴ�ϸ( >0 )");
		try {
			stroke = Float.parseFloat(input);

		} catch (Exception e) {
			stroke = 1.0f;

		}
		itemList[index].stroke = stroke;

	}

	public void setCurrentChoice(int i)// ���ֵ�����
	{
		currentChoice = i;
	}

	public void setFont(int i, int font)// ��������
	{
		if (i == 1) {
			f1 = font;
		} else
			f2 = font;
	}

	// TODO ����¼�MouseA��̳���MouseAdapter
	// �������������Ӧ�¼��Ĳ��������İ��¡��ͷš��������ƶ����϶�����ʱ����һ���������ʱ�˳�����ʱ���������� )
	class MouseA extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent me) {
			// TODO ������
			drawpad.setStratBar("�������ڣ�[" + me.getX() + " ," + me.getY() + "]");
		}

		@Override
		public void mouseExited(MouseEvent me) {
			// TODO ����˳�
			drawpad.setStratBar("����˳��ڣ�[" + me.getX() + " ," + me.getY() + "]");
		}

		@Override
		public void mousePressed(MouseEvent me) {
			// TODO ��갴��
			drawpad.setStratBar("��갴���ڣ�[" + me.getX() + " ," + me.getY() + "]");// ����״̬����ʾ

			itemList[index].x1 = itemList[index].x2 = me.getX();
			itemList[index].y1 = itemList[index].y2 = me.getY();

			// �����ǰѡ��Ϊ��ʻ�����Ƥ�� �����������Ĳ���
			if (currentChoice == 3 || currentChoice == 13) {
				itemList[index].x1 = itemList[index].x2 = me.getX();
				itemList[index].y1 = itemList[index].y2 = me.getY();
				index++;
				createNewitem();// �����µ�ͼ�εĻ�����Ԫ����
			}
			// ���ѡ��ͼ�ε��������룬���������Ĳ���
			if (currentChoice == 14) {
				itemList[index].x1 = me.getX();
				itemList[index].y1 = me.getY();
				String input;
				input = JOptionPane.showInputDialog("��������Ҫд������֣�");
				itemList[index].s1 = input;
				itemList[index].x2 = f1;
				itemList[index].y2 = f2;
				itemList[index].s2 = stytle;

				index++;
				currentChoice = 14;
				createNewitem();// �����µ�ͼ�εĻ�����Ԫ����
				repaint();
			}

		}

		@Override
		public void mouseReleased(MouseEvent me) {
			// TODO ����ɿ�
			drawpad.setStratBar("����ɿ��ڣ�[" + me.getX() + " ," + me.getY() + "]");
			if (currentChoice == 3 || currentChoice == 13) {
				itemList[index].x1 = me.getX();
				itemList[index].y1 = me.getY();
			}
			itemList[index].x2 = me.getX();
			itemList[index].y2 = me.getY();
			repaint();
			index++;
			createNewitem();// �����µ�ͼ�εĻ�����Ԫ����
		}

	}

	// ����¼�MouseB�̳���MouseMotionAdapter
	// �����������Ĺ������϶�
	class MouseB extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent me)// �����϶�
		{
			drawpad.setStratBar("����϶��ڣ�[" + me.getX() + " ," + me.getY() + "]");
			if (currentChoice == 3 || currentChoice == 13) {
				itemList[index - 1].x1 = itemList[index].x2 = itemList[index].x1 = me
						.getX();
				itemList[index - 1].y1 = itemList[index].y2 = itemList[index].y1 = me
						.getY();
				index++;
				createNewitem();// �����µ�ͼ�εĻ�����Ԫ����
			} else {
				itemList[index].x2 = me.getX();
				itemList[index].y2 = me.getY();
			}
			repaint();
		}

		public void mouseMoved(MouseEvent me)// �����ƶ�
		{
			drawpad.setStratBar("����ƶ��ڣ�[" + me.getX() + " ," + me.getY() + "]");
		}
	}

}
