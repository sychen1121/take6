import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.Border;

public class Client {
	// game layout 變數
	private JFrame mainFrame; // 主要的頁面
	private JPanel mainPanel;// 中間主要遊戲畫面，因為右邊要放聊天室
	private JPanel UpPanel; // 上面的頁面，只要是用來顯示四個牌堆
	private JPanel StackPanel;
	private JPanel history;
	private JButton[][] stackButton = new JButton[4][6];
	private JPanel DownMainPanel;
	private JPanel selfinfo;
	private JLabel info;
	private JButton sendto;
	private JScrollPane DownPanel;// 下方的頁面，只要是用來顯示手上的卡片
	private JFrame cF;
	private JPanel CardPanel;// 放手上卡片的panel，為了呈現與邊框有距離的感覺
	private JButton[] CardButton = new JButton[100];// 手上卡片的button，會在下方呈現
	private JTextArea historyArea;
	private JFrame wPage;// 等待開始的畫面
	private JScrollPane historyPanel;
	private JButton addButton;
	// chat layout 變數
	private JPanel chatpanel;
	private JButton typing;
	private JTextField chatField;
	private JTextArea chatArea;
	private String message;

	// 傳送參數
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String chatserver;
	// private JList list;
	private Socket clientSocket;
	static String name ;
	
	private boolean endConfirm =false;
	
	private gameEvent game = new gameEvent();
	private Player me = new Player(1);
	// 牌
	private Card[] selfcard;
	private Card[][] stackcard = new Card[4][6];

	private boolean control = false;// 控制玩家可不可以出牌 true表可以
	private boolean choose = false;
	
//	this main function is for testing
	public static void main(String[] args) throws InterruptedException {
		Client client = new Client("127.0.0.1"); //connect to server with ip 127.0.0.1
		client.layout();
		client.waitToStart();
		client.runclient();
		
		
	}

	public Client(String host) {
		name = JOptionPane.showInputDialog("請輸入暱稱");
		chatserver = host;
		// this.layout();
		// this.runclient();
	}
	public void waitToStart(){
		
		wPage = new JFrame ("等待開始");
		JLabel t =new JLabel ("等待房主開始...");
		//wPage.add(t);
		wPage.setSize(500, 240);
		t.setFont(new Font("標楷體", Font.BOLD, 32));
		wPage.getContentPane().add(t);
		wPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//wPage.pack();
		wPage.setVisible(true);
		wPage.setLocation(450, 100);
	
	}
	
	
	
	
	public void layout() {
		{
			mainFrame = new JFrame("誰是牛頭王 - "+name);
			mainFrame.setLayout(new BorderLayout());
			mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());
			CardPanel = new JPanel();
			CardPanel.setBackground(Color.black);
			// CardPanel.setPreferredSize(new Dimension(2500,200));

			CardPanel.setLocation(0, 500);
			CardPanel.setLayout(new FlowLayout());
			for (int i = 0; i < 15; i++) {
				CardButton[i] = new JButton("" + i);
				CardPanel.add(CardButton[i]);
				CardButton[i].setPreferredSize(new Dimension(80, 120));
				CardButton[i].setBackground(Color.white);
				CardButton[i].addMouseListener(new CardButtonHandler(i));// CardButton
																			// Listener
																			// end
			}
			DownMainPanel = new JPanel();
			DownMainPanel.setLayout(new BorderLayout());
			DownPanel = new JScrollPane(CardPanel);

			DownMainPanel.add(DownPanel);

			DownMainPanel.setPreferredSize(new Dimension(0, 150));

			selfinfo = new JPanel();
			selfinfo.setLayout(new GridLayout(2, 1));

			info = new JLabel("你獲得的牛頭數 : " + me.point);
			info.setPreferredSize(new Dimension(10, 10));
			info.setBackground(new Color(205,170,125));
			info.setOpaque(true);
			//info.setBorder(BorderFactory.createLineBorder(Color.black));  //設置邊框
			
			sendto = new JButton();
			sendto.setBackground(new Color(205,170,125));
			sendto.setPreferredSize(new Dimension(10, 10));
			sendto.addMouseListener(new sendButtonHandler());
			ImageIcon icon = new ImageIcon(this.getClass().getResource("/image/button1.png"));
			icon.setImage(icon.getImage().getScaledInstance(145,
					73, Image.SCALE_DEFAULT));
			sendto.setIcon(icon);
			selfinfo.add(info);
			selfinfo.add(sendto);
			DownMainPanel.add(selfinfo, BorderLayout.WEST);
			DownMainPanel.add(DownPanel, BorderLayout.CENTER);
			selfinfo.setPreferredSize(new Dimension(150, 1));
			DownPanel.setPreferredSize(new Dimension(700, 150));
			mainPanel.add(DownMainPanel, BorderLayout.SOUTH);
			/**************************** set chat section ****************************************/
			chatpanel = new JPanel();
			chatpanel.setLayout(new BorderLayout());
			typing = new JButton("輸入");
			chatField = new JTextField();
			chatArea = new JTextArea();
			chatArea.setPreferredSize(new Dimension(220, 550));
			JPanel text = new JPanel();
			text.add(chatArea);
			JScrollPane chatS = new JScrollPane(text); 
		
			chatS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			chatS.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			chatpanel.add(chatS, BorderLayout.NORTH);
			chatpanel.add(chatField, BorderLayout.CENTER);
			chatpanel.add(typing, BorderLayout.EAST);
			chatpanel.setPreferredSize(new Dimension(240, 0));
			chatField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					message = chatField.getText();
					chatField.setText("");
					sendData(new Package(1, name + ":" + message + "\n"));
					//chatpanel.updateUI();
				}
			});
			typing.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					message = chatField.getText();
					chatField.setText("");
					sendData(new Package(1, name + ":" + message + "\n"));
					//chatpanel.updateUI();
				}
			});

			mainFrame.pack();
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			UpPanel = new JPanel();
			UpPanel.setLayout(new BorderLayout());
			StackPanel = new JPanel();
			StackPanel.setLayout(null);
			StackPanel.setBackground(new Color(205,170,125));
			history = new JPanel();
			UpPanel.add(StackPanel, BorderLayout.CENTER);
			UpPanel.add(history, BorderLayout.WEST);
			historyArea = new JTextArea();
			historyArea.setPreferredSize(new Dimension(130, 550));
			historyArea.setBackground(new Color(238 ,149 ,114));
			
			history.setPreferredSize(new Dimension(150, 630));
			history.setBackground(new Color(205,170,125));
			historyPanel = new JScrollPane(historyArea);
			historyPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			historyPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			history.add(historyPanel);
			//history.setBorder(BorderFactory.createLineBorder(Color.black));  //設置邊框
			
			
			mainPanel.add(UpPanel, BorderLayout.NORTH);
			mainPanel.setBackground(new Color(205,170,125));
			mainFrame.add(mainPanel, BorderLayout.CENTER);
			mainFrame.add(chatpanel, BorderLayout.EAST);
			mainFrame.setSize(1350, 750);
			
		}
	}

	public void runclient() throws InterruptedException {
		try {
			// 建立連線
			clientSocket = new Socket(InetAddress.getByName(chatserver), 12345);
			chatArea.append("連線成功\n");
			getStream();
			// System.out.println("test temp");
			Package temp = new Package(1, name + "已進入遊戲\n");
			System.out.println(temp.getMessage());

			sendData(temp);
			sendData(new Package(5, name));
			System.out.println("test after send data");
			process();
			System.out.println("test3");
		} catch (IOException e) {
			chatArea.append("連線失敗\n");
			wPage.dispose();
			Starting.main(null);
			System.exit(0);
		}
	}

	// 取得socket的串流參照
	public void getStream() {
		try {
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			output.flush(); // flush output buffer to send header information
			input = new ObjectInputStream(clientSocket.getInputStream());
			chatArea.append("串流建立成功\n");
		} catch (IOException e) {

			e.printStackTrace();
		} catch (NullPointerException e) {

			chatArea.append("無法建立串流\n");
		}
	}

	// 接收訊息
	public void process() {
		while (true) {
			Package readPge;
			try {
				readPge = (Package) input.readObject();
				System.out.println("read successful");
				implementCategory(readPge);// 依照傳輸不同的物件做不同的是

			} catch (IOException e) {
				try {
					System.out.println("readPag:IOException");
					input.close();
					output.close();
					clientSocket.close();
					System.exit(0);
				} catch (IOException e1) {
					// textarea.append("主機沒有回應\n");
				}
			} catch (ClassNotFoundException e) {
				chatArea.append("主機沒有回應\n");
				e.printStackTrace();
			}
		}
	}

	// 傳送訊息
	public void sendData(Package send) {// send to server
		try {
			output.writeObject(send);
			// output.writeObject(send.getMessage());
			System.out.println("test sending data");
			output.flush(); // flush data to output
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("catch");
			System.out.println(e.getMessage());
			try {
				input.close();
				output.close();
				clientSocket.close();

			} catch (IOException e1) {
				chatArea.append("主機已斷線\n");
			}
		}
	}

	public void implementCategory(Package read) {
		System.out.println("impleing...");
		int type = read.isType();
		if (type == 1) {
			chatArea.append(read.getMessage()); // 顯示訊息
		} else if (type == 2) {// not implement,
			System.out.println("this is the message about the game");
		} else if (type == 3) {// not implement
			System.out.println("this is the card object");
		} else if (type == 6) {// not implement
			System.out.println("this is the deal");
			me.setHand(read.getDealCard());
			me.sort();
			CardPanel.removeAll();
			for (int i = 0; i < 10; i++) {
				CardButton[i] = new JButton("" + me.hand[i].getNum());
				CardPanel.add(CardButton[i]);
				ImageIcon icon = new ImageIcon(this.getClass().getResource("/"+me.handImg(i)));
				icon.setImage(icon.getImage().getScaledInstance(85, 130,
						Image.SCALE_DEFAULT));
				CardButton[i].setIcon(icon);
				CardButton[i].setPreferredSize(new Dimension(80, 120));
				CardButton[i].setBackground(Color.white);
				CardButton[i].addMouseListener(new CardButtonHandler(i));// CardButton
																			// Listener
																			// end
			}
		} else if (type == 4) {// not implement
			System.out.println("this is the stack");
			stackcard = read.getStack();
			for (int i = 0; i < 4; i++) {

				for (int j = 0; j < 6; j++) {
					if (read.getStack()[i][j] != null) {
						System.out.println("" + read.getStack()[i][j].getNum());
					}
					// System.out.println("num:"+j);

				}

			}

			// StackPanel.removeAll();
			int x = 200, y = 410;
			for (int i = 0; i < 4; i++) {
				x = 50;
				for (int j = 0; j < stackcard[i].length; j++) {
					if (stackcard[i][j] != null) {
						stackButton[i][j] = new JButton(""
								+ stackcard[i][j].getNum());
						StackPanel.add(stackButton[i][j]);
						stackButton[i][j].setBounds(x, y, 80, 120);
						stackButton[i][j].setPreferredSize(new Dimension(80,
								120));
						stackButton[i][j].setBackground(Color.LIGHT_GRAY);
						ImageIcon icon = new ImageIcon(this.getClass().getResource("/"+stackcard[i][j].getImg()));
						icon.setImage(icon.getImage().getScaledInstance(85,
								120, Image.SCALE_DEFAULT));
						stackButton[i][j].setIcon(icon);
						// StackPanel.removeAll();
						x = x + 100;
					}

				}
				y = y - 130;
			}
			control = true;
			wPage.dispose();
			mainFrame.setVisible(true);
		} else if (type == 7) {// not implement
			System.out.println("this is the Server stack!!!");
			stackcard = read.gettStack().getStackcard();
			for (int i = 0; i < 4; i++) {

				for (int j = 0; j < 6; j++) {
					if (stackcard[i][j] != null) {
						System.out.println("" + stackcard[i][j].getNum());
					}
					System.out.println("num:" + j);
				}

			}
			StackPanel.removeAll();
			StackPanel.updateUI();
			int x = 200, y = 410;
			for (int i = 0; i < 4; i++) {
				x = 50;
				for (int j = 0; j < stackcard[i].length; j++) {
					if (stackcard[i][j] != null) {
						stackButton[i][j] = new JButton(""
								+ stackcard[i][j].getNum());
						StackPanel.add(stackButton[i][j]);
						stackButton[i][j].setBounds(x, y, 80, 120);
						stackButton[i][j].setPreferredSize(new Dimension(80,
								120));
						stackButton[i][j].setBackground(Color.LIGHT_GRAY);
						ImageIcon icon = new ImageIcon(this.getClass().getResource("/"+stackcard[i][j].getImg()));
						icon.setImage(icon.getImage().getScaledInstance(85,
								120, Image.SCALE_DEFAULT));
						stackButton[i][j].setIcon(icon);

						x = x + 100;
					}

				}
				y = y - 130;
			}
			CardPanel.updateUI();
			control = true;

		} else if (type == 8) {// not implement
			System.out.println("this is the update info");
			me.setPoint(read.getPoint());
			info.setText("你獲得的牛頭數 : " + me.point);
			info.updateUI();
		} else if (type == 9) {// not implement
			/*System.out.println("this is the change card?");
			cF = new JFrame("選擇要換哪一疊牌");
			JPanel cP = new JPanel();
			cF.setLayout(new FlowLayout());
			cP.setLayout(new GridLayout(4, 1));
			Button b1 = new Button("1");
			b1.addMouseListener(new cButtonHandler(0));
			Button b2 = new Button("2");
			b2.addMouseListener(new cButtonHandler(1));
			Button b3 = new Button("3");
			b3.addMouseListener(new cButtonHandler(2));
			Button b4 = new Button("4");
			b4.addMouseListener(new cButtonHandler(3));
			cP.add(b4);
			cP.add(b3);
			cP.add(b2);
			cP.add(b1);
			cF.add(cP);
			cF.setSize(500, 300);
			cF.setVisible(true);*/
			String []c = {"1","2","3","4"};
			String favoriteC = (String) JOptionPane.showInputDialog(null, 
			        "你出的牌比場上的牌還要小，請選擇一個牌堆收取替換?",
			        "選擇哪一個牌堆",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        c, 
			        c[0]);
			int flag = Integer.parseInt(favoriteC);
			Package tempPackage = new Package();
			tempPackage.setCategory(10);
			tempPackage.setWhichStack(flag-1);
			sendData(tempPackage);	
		}else if (type == 11) {// not implement,
			historyArea.append(read.getMessage());
		}else if (type == 99) {// not implement,
			control = false;
			System.out.println("this is the end message");
			/*JFrame endFrame = new JFrame("結果公布");
			endFrame.setLayout(new GridLayout(3,1));
			JPanel endPage = new JPanel();
			JLabel endText = new JLabel("最後贏家 : "+read.getWin().getWinner()+"\n");
			JLabel endText2 = new JLabel(read.getWin().getRecord());
			JButton confirm =new JButton("確認"); 
			confirm.addMouseListener(new endHandler());
			endFrame.add(endText);
			endFrame.add(endText2);
			endFrame.add(confirm);
			endText.setFont(new Font("標楷體", Font.BOLD, 32));
			endText.setForeground(Color.blue);
			endText2.setFont(new Font("微軟正黑體", Font.BOLD, 20 ));
			//endFrame.add(endPage);
			endText.setBackground(Color.LIGHT_GRAY);
			endFrame.pack();
			endFrame.setVisible(true);
			endFrame.setLocation(450, 100);
			while(!endConfirm){}
			endFrame.dispose();*/
			int answer = JOptionPane.showConfirmDialog(null,"最後贏家 : "+read.getWin().getWinner()+"\n"+read.getWin().getRecord(), "結果公布", JOptionPane.OK_CANCEL_OPTION);
			Package end = new Package();
			end.setCategory(100);
			System.exit(0);
		}
		else {// not implement
			System.out.println("this is the stack onject");
		}
	}
	class cButtonHandler implements MouseListener{
		private int flag;
		public cButtonHandler(int i)
		{
			flag = i;
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			Package tempPackage = new Package();
			tempPackage.setCategory(10);
			tempPackage.setWhichStack(flag);
			sendData(tempPackage);
			cF.dispose();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	class CardButtonHandler implements MouseListener {

		private int data;

		public CardButtonHandler(int i) {
			data = i;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub

			game.setCardColor(CardButton[data], data);
			choose = true;
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	class sendButtonHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		
			if(control && choose)
			{
				// TODO Auto-generated method stub
				Card temp = me.getHand( game.send() );
				//Package tempP = new Package(temp);
				//System.out.println(tempP.isType());
				sendData(new Package(temp));
				CardPanel.remove(CardButton[game.send()]);
				CardPanel.updateUI();
				//System.out.println(temp.getClass());
				System.out.println("after send");
				System.out.println(game.send());
				control = false;
				choose = false;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
	class endHandler implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			endConfirm = true;
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
