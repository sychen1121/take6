import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Layout {
		private JFrame mainFrame;
		private Panel LeftPanel;
		private Panel RightPanel;
		private Button[] road = new Button[45];
		private Panel CardPanel;
		private Panel RightDownPanel;
		private Panel drawCradPanel;
		private Panel RightUpPanel;
		private JPanel chatpanel;
		private JButton typing;
		private JTextField chatField;
		private JButton[] selfCard=new JButton[7];
		private JButton drawCrad = new JButton();
		private JButton[] dwarf = new JButton[6];
		private JTextArea drawCardBlank = new JTextArea("");
		private JTextArea chatArea;
		private String message;
		
		public Layout(String name) {
			mainFrame = new JFrame("矮人礦坑");
			/****************************set chat section****************************************/
			chatpanel=new JPanel();
			chatpanel.setLayout(new BorderLayout());
			//bar=new JMenuBar();
			typing=new JButton("輸入");
			chatField =new JTextField();
			chatArea = new JTextArea();
			chatArea.setSize(200,100);
			//chatpanel.add(bar,BorderLayout.NORTH);
			chatpanel.add(new JScrollPane(chatArea), BorderLayout.NORTH);
			chatpanel.add(chatField,BorderLayout.CENTER);
			chatpanel.add(typing,BorderLayout.EAST);
			
			chatField.addActionListener(
					   new ActionListener(){           
					      public void actionPerformed( ActionEvent event ){    	
					    	  chatField.setText( "" );
					      } 
					   } 
					); 	
			
			
			/****************************set left page*******************************************/
			LeftPanel = new Panel();
			LeftPanel.setLayout(new GridLayout(5,9));
			for(int i=0;i<45;i++)
			{
				road[i]=new Button();
				LeftPanel.add(road[i]);
			}
			/****************************End left page*******************************************/
			
			
			Label label1 = new Label(" ");
			Label label2 = new Label(" ");
			
			
			/****************************set right page*******************************************/
			RightPanel = new Panel();
			RightPanel.setLayout(new GridLayout(2,1));
			RightPanel.setSize(200,700);
			
			
							/****************************set right  up page*******************************************/
								RightUpPanel=new Panel(new GridLayout(2,3));
								RightPanel.add(RightUpPanel);		
			
			
							/****************************set right  down page*******************************************/
								RightDownPanel=new Panel();
								CardPanel = new Panel();
								CardPanel.setLayout(new GridLayout(1,5));
			
								drawCradPanel = new Panel();
								Icon vv=new ImageIcon(this.getClass().getResource("/image/drawCrad.png"));
								drawCradPanel.setLayout(new GridLayout(1,3));		
			
								RightDownPanel.setLayout(new GridLayout(2,1));
								
								
								for(int i=0;i<5;i++)
								{
									selfCard[i]=new JButton("Card");
									selfCard[i].setBackground(Color.WHITE);
									CardPanel.add(selfCard[i]);
								}
								for(int i=0;i<6;i++)
								{
									dwarf[i]=new JButton();
									Icon icon=new ImageIcon(this.getClass().getResource("/image/littleman1.png"));
									dwarf[i].setIcon(icon);
									RightUpPanel.add(dwarf[i]);
								}
								
								
								///////////////////////////////////////////////////////////*****改變圖片大小***/
	/*							JLabel label = new JLabel(vv);
								label.setBounds(0, 0,vv.getIconWidth(), vv.getIconHeight());
								
								int w = (int) (vv.getIconWidth()*1.5);
								int h = (int) (vv.getIconHeight()*1.5);
								int type = BufferedImage.TYPE_INT_RGB;
								BufferedImage dst = new BufferedImage(w, h, type);
						        Graphics2D g2 = dst.createGraphics();
						        g2.drawImage(vv.getImage(), 0, 0, w, h, drawCrad);
						        g2.dispose();*/
						        ///////////////////////////////////////////////////////////////*****改變圖片大小***/
								drawCrad.setIcon(vv);
								drawCrad.setBackground(Color.WHITE);
								drawCrad.setOpaque(true);
								drawCradPanel.add(drawCrad);
								drawCradPanel.add(label1);	

								
								RightDownPanel.add(CardPanel,BorderLayout.NORTH);
								RightDownPanel.add(drawCradPanel,BorderLayout.SOUTH);
								
								RightPanel.add(RightDownPanel);
			/****************************End right page*******************************************/
								mainFrame.pack();
								mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
		
		public void launch(){
			
			mainFrame.setLayout(new BorderLayout());
			mainFrame.add(LeftPanel, BorderLayout.CENTER);
			mainFrame.add(RightPanel, BorderLayout.EAST);
			mainFrame.add(chatpanel,BorderLayout.SOUTH);
			mainFrame.setSize(1000,700);
			mainFrame.setVisible(true);
			
		}
		public static void main(String args[]){
			Layout layout = new Layout("asd");
			layout.launch();
		}

}
