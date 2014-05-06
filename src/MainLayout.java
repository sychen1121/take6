import java.awt.*;
import java.awt.image.BufferedImage;


import javax.swing.*;

public class MainLayout {
	
	private JFrame mainFrame; //主要的頁面
	
	private JPanel UpPanel; //上面的頁面，只要是用來顯示四個牌堆
	private JScrollPane DownPanel;//下方的頁面，只要是用來顯示手上的卡片
	
	private JPanel CardPanel;//放手上卡片的panel，為了呈現與邊框有距離的感覺
	private JButton[] CardButton=new JButton[100];//手上卡片的button，會在下方呈現
	
	private JButton addButton;
	public MainLayout(){
		mainFrame=new JFrame("矮人礦坑");
		mainFrame.setLayout(new BorderLayout());
		
		CardPanel = new JPanel();
		CardPanel.setBackground(Color.black);
		//CardPanel.setPreferredSize(new Dimension(2500,200));
		
		CardPanel.setLocation(0, 500);
		CardPanel.setLayout(new FlowLayout());
		for(int i=0;i<15;i++)
		{
			CardButton[i]=new JButton(""+i);
			CardPanel.add(CardButton[i]);
			CardButton[i].setPreferredSize(new Dimension(100,150));
		}
		DownPanel = new JScrollPane(CardPanel);
		DownPanel.setPreferredSize(new Dimension(0, 200));
		mainFrame.add(DownPanel,BorderLayout.SOUTH);
		
		
		
	}
	public void lunch(){
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1000,700);
		mainFrame.setVisible(true);
	}
}
