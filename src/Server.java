import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFrame;

public class Server {
	private JTextArea textarea;
	private JButton terminate;
	private JFrame smainFrame;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket serverSocket;
	private Socket connection;
	private int i = 1;
	//private int countNumber =0;
	private static User user[] = new User[100];
	static String test;
	String nickname, msg;
	static String word2, total;
	String arry[];
	MemberList list;
	ServerStack stack;
	private boolean conti = true;
	Card[] cards = new Card[104];
	Card[][] stacks = new Card[4][6];
	Card[][] memberHand;
	private Stack tStack;
	private static int isend=0;
	private static int playerhandcountnumber=0;
	private Winner win=new Winner();
	private static String newIP;
	private History hit=new History();
	
	public static void main(String args[]) throws InterruptedException {

		Server server = new Server();
		server.runserver();

		// MemberList list = new MemberList(i);
		// ServerStack stack = new ServerStack(i,stacks,list);

	}

	public Server() {
		// set server frame
		// System.out.println("Start...");
		smainFrame = new JFrame("Take 6");
		textarea = new JTextArea();
		textarea.append("Building game..\n");
		terminate = new JButton("Start game");
		terminate.addMouseListener(new startButton());
		smainFrame.add(terminate, BorderLayout.SOUTH);
		smainFrame.add(new JScrollPane(textarea), BorderLayout.CENTER);
		smainFrame.setSize(300, 300);
		smainFrame.setVisible(true);
		smainFrame.setLocation(400, 100);
		smainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// run server
		// System.out.println("Start1...");
		// this.runserver();
		// System.out.println("Start2...");

		// System.out.println("End...");
	}

	public void runserver() throws InterruptedException {
		//list = new MemberList(10);
		System.out.println("run start");
		try {
			serverSocket = new ServerSocket(12345, 100);
			printSocketInformation();
			textarea.append("Please connect\n");
			textarea.append("IP address:"+newIP);
			terminate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					conti = false;
				}
			});
			while (conti) {

				// 建立user 在此等待聯結
				user[i] = new User(serverSocket.accept(), i);
				if(conti==false)
				{
					break;
				}
					
				user[i].start(); // �]thread
				i++;
				if (i > 10) {
					conti = false;
					break;
				}
				textarea.append("New player is getting in\n");
				// conti=false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void printSocketInformation(){
		/* 創立一個字串清單 */
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		/* 創建一個網路介面的列舉並取得所有介面 */
		for (NetworkInterface n : Collections.list(nets)) {
		Enumeration<InetAddress> ip = n.getInetAddresses();
		/* 創立一個列舉並將取得的IP放進來 */
		for (InetAddress i : Collections.list(ip)) {
		if (i instanceof Inet4Address && NetworkInterface.getByInetAddress(i).isUp() && !(i.getHostAddress().equals("127.0.0.1"))) {
		arrayList.add(n.getName().toString());
		arrayList.add(i.getHostAddress().toString());
		/*清單裡為IPV4的型態,且是網路介面的配置,以及不為127.0.0.1的話加入清單*/
		}
		}
		}
		} catch (SocketException e) {
		e.printStackTrace();
		}
		newIP = arrayList.get(1);
		}
	
	public void run() throws InterruptedException, IOException{
		System.out.println("accept run end");
		list = new MemberList(i-1);
		playerhandcountnumber=(i-1)*10;
		stack = new ServerStack(i - 1, stacks, list);
		this.deal();
		Thread.sleep(50);
		// Package temp = new Package(memberHand[j-1]);
		dealtoClient();
		sendData(new Package(stacks));
		System.out.println("run end");
		for(int j=0;j<i-1;j++)
		{
			list.getPlayer(j).setName(user[j+1].getName());
		}
		while (true) {
			serverSocket.accept();
		}
	}
	
	private void dealtoClient() {
		// TODO Auto-generated method stub
		if (i > 0) {
			for (int j = 1; j < i; j++) {
				try {
					System.out.println("YO!!!");
					Package temp = new Package(memberHand[j - 1]);
					// System.out.println("Card:"+temp.getDealCard().length);
					// Package temp = new Package(1,"YO");
					user[j].output.writeObject(temp);
					user[j].output.flush();
				} catch (IOException e) {
				} catch (NullPointerException e) {
				}
			}
		}
	}
	public void over(){
		int hightest;
		String w;
		hightest = list.getPlayer(0).getPoint();
		w = user[1].getUserName();
		win.add("玩家 "+user[1].getUserName()+" 牛頭數: "+list.getPlayer(0).getPoint()+"\n");
		for(int j=2;j< i;j++)
		{
			if(list.getPlayer(j-1).getPoint() < hightest)
			{
				w = user[j].getUserName();
			}
			win.add("玩家 "+user[j].getUserName()+" 牛頭數: "+list.getPlayer(j-1).getPoint()+"\n");
		}
		win.setWinner(w);
		Package endPackage = new Package(win);
		sendData(endPackage);
	}
	private void changePoint() {
		if (i > 0) {
			for (int j = 1; j < i; j++) {
				try {
					// System.out.println("YO!!!");
					Package temp = new Package(list.getPlayer(j - 1).getPoint());
					// System.out.println("Card:"+temp.getDealCard().length);
					// Package temp = new Package(1,"YO");
					user[j].output.writeObject(temp);
					user[j].output.flush();
				} catch (IOException e) {
				} catch (NullPointerException e) {
				}
			}
		}
	}

	public void sendData(Package send) {
		// 送data給每位使用者
		if (i > 0) {
			for (int j = 1; j < i; j++) {
				try {
					System.out.println(send.isType());
					user[j].output.writeObject(send);
					user[j].output.flush();
				} catch (IOException e) {
				} catch (NullPointerException e) {
				}
			}
		}

	}

	public String getnickname() {
		String array[] = msg.split(":");// 將msg用":"給分開
		word2 = array[0];
		return word2;
	}

	// ////////////洗牌
	public void deal() {
		this.memberHand = new Card[i - 1][(int) (100 / (i - 1))];
		assign(); // 給牌編號
		// 做前面要加Card[] cards = new Card[104];
		shuffle();
		int temp = (int) 10*(i-1); // 共要發幾張牌

		for (int j = 0; j < temp; j++) {
			memberHand[(int) (j % (i - 1))][(int) (j / (i - 1))] = cards[j];
			cards[j].setMember((int) (j % (i - 1)));
			System.out.println("發卡給 :"+cards[j].getMember()+" 第"+j+"張");
		}
		for (int j = 0; j < 4; j++) {
			stacks[j][0] = cards[100 + j];
			for (int x = 1; x < 6; x++) {
				stacks[j][x] = null;
			}
		
		}
		/*
		 * for (int j = 1; j < i; j++) { try {
		 * user[j].output.writeObject(stacks); output.flush();
		 * user[j].output.writeObject(memberHand[j - 1]); output.flush(); }
		 * catch (IOException e) { } catch (NullPointerException e) { } }
		 */

	}

	private void shuffle() {
		int index1;
		int index2;
		Card temp;
		for (int j = 0; j < 104; j++) {
			index1 = (int) ((Math.random()) * 104);
			index2 = (int) ((Math.random()) * 104);
			temp = cards[index1];
			cards[index1] = cards[index2];
			cards[index2] = temp;
		}
	}

	private void assign() {
		for (int j = 0; j < 104; j++) {
			// cards[j].setNum(j + 1);
			/*****************/
			cards[j] = new Card(j + 1, 1, "image/" + (j + 1) + ".jpg");

			/*****************/
		}
		for (int j = 0; j < 20; j++) {
			cards[(j + 1) * 5].setPoint(2);
		}
		for (int j = 0; j < 10; j++) {
			cards[(j + 1) * 10].setPoint(3);
		}
		for (int j = 0; j < 9; j++) {
			cards[(j + 1) * 11].setPoint(5);
		}
	}

	// /////////////////洗牌結束

/***********************              User                      *************************/
	public class User extends Thread {
		public Socket socket;
		public String name;
	

		public ObjectOutputStream output;
		public ObjectInputStream input;
		public int x; // 每個人的ID
		boolean live = true;

		public User(Socket client, int i) {
			socket = client;
			x = i;
			this.getStream();
		}

		public String getUserName() {
			return name;
		}

		public void setUserName(String n) {
			this.name = n;
		}

		// 取得socket的串流參照
		public void getStream() {
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
				output.flush();
				input = new ObjectInputStream(socket.getInputStream());
				textarea.append("串流建立成功\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void run() {
			while (live) {
				try {
					// 寫入訊息
					Package readPge = (Package) input.readObject(); // 因為package不是serializable
					// Package readPge= new Package((String)input.readObject());
					System.out.println(readPge.getMessage());
					implementCategory(readPge);

					// total=(String) getnickname();
					// System.out.println("我是total"+total);

				} catch (IOException e) {

					try {
						// 關閉input output Stream
						input.close();
						output.close();
						live = false;
						String temp = "User[" + x + "]離開聊天室\n";
						sendData(new Package(1, temp));
						textarea.append("User[" + x + "]離開聊天室\n");

					} catch (IOException e1) {

						System.exit(0);
					}
				} catch (ClassNotFoundException e) {

					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		@SuppressWarnings("deprecation")
		public void implementCategory(Package read) throws InterruptedException, ClassNotFoundException, IOException {
			int type = read.isType();
			if (type == 1) {
				textarea.append(read.getMessage());
				sendData(read);
				// 顯示訊息
			} else if (type == 2) {// not implement,
				System.out.println("this is the message about the game");
			} else if (type == 3) {// not implement
				System.out.println("this is the card object");
				System.out.println(read.getCard().getNum());
				if (stack.setCard(x, read.getCard())) {
					/*System.out.println("Stack return");
					tStack = new Stack(stack.getStackcard());
					sendData(new Package(tStack));
					changePoint();
					for (int j = 1; j < i; j++) {
						hit.add("玩家 "+user[j].getUserName()+" 目前牛頭數:"+list.getPlayer(j-1).getPoint()+"\n");
					}
					Package h = new Package();
					h.setCategory(11);
					h.setMessage(hit.getHistory());
					sendData(h);
					hit.setHistory("");
					if(isend==104)
					{
						int hightest;
						String w;
						hightest = list.getPlayer(0).getPoint();
						w = user[1].getUserName();
						win.add("玩家 "+user[1].getUserName()+" 牛頭數: "+list.getPlayer(0).getPoint()+"\n");
						for(int j=2;j< i;j++)
						{
							if(list.getPlayer(j-1).getPoint() < hightest)
							{
								w = user[j].getUserName();
							}
							win.add("玩家 "+user[j].getUserName()+" 牛頭數: "+list.getPlayer(j-1).getPoint()+"\n");
						}
						win.setWinner(w);
						Package endPackage = new Package(win);
						sendData(endPackage);
						Thread.sleep(100);
						System.exit(0);
					}*/
				}
			} else if (type == 4) {// not implement
				System.out.println("this is the stack onject");
			} else if (type == 5) {// get the name of the user
				this.name=read.getMessage();
				//list.getPlayer(x-1).setName(user[x].getUserName());
				//System.out.println("ID :"+x+",name : "+user[x].getUserName());
			
			}else if (type == 10) {// not implement,
				stack.setWhichbeChange(read.getWhichStack());
				stack.setChanged(true);
			}
			else if (type == 100) {// not implement
				input.close();
				output.close();
				System.exit(0);
			}
			else
			{
				
			}
		}

	}

	// /////inner
	// Class///////////////////////////////////////////////////////////////////////////

	public class ServerStack implements Serializable {
		private int membernumber;
		private Card[] tempCard;
		private MemberList operation;// 用來操作加減分時用的指標
		private Card[][] stackcard = new Card[4][6];
		private int now = 0;
		private boolean changed=false;
		public boolean isChanged() {
			return changed;
		}

		public void setChanged(boolean changed) {
			this.changed = changed;
		}

		public int getWhichbeChange() {
			return whichbeChange;
		}

		public void setWhichbeChange(int whichbeChange) {
			this.whichbeChange = whichbeChange;
		}

		private int whichbeChange;
		int[] most = new int[4];// 每堆牌的目前有幾張

		public ServerStack(int n, Card[][] c, MemberList list) {
			membernumber = n;
			tempCard = new Card[membernumber];
			operation = list;
			stackcard = c;
		}

		// 將玩家出的牌先存起來
		public boolean setCard(int i, Card c) throws ClassNotFoundException, IOException, InterruptedException {
			isend++;
			//System.out.println(" now:" + now);
			tempCard[i - 1] = c;
			tempCard[i-1].setMember(i);
			hit.add("玩家 "+i+":"+user[i].name+" 出"+c.getNum()+"\n");
			now++;
			if (now == membernumber) {
				System.out.println("card add start... :" + membernumber
						+ " now:" + now);
				tThread t1 = new tThread(i);
				t1.start();
				now = 0;
				return true;
			} else {
				return false;
			}
		}

		// 開始執行遊戲
		public void lunch() throws ClassNotFoundException, IOException, InterruptedException {
			findSort();
			System.out.println("lunch");
			for (int i = 0; i < membernumber; i++) {
				
				
				if (changeStack(tempCard[i])) {
					Package tempPackage =new Package();
					tempPackage.setCategory(9);
					try {
						user[tempCard[i].getMember()].output.writeObject(tempPackage);
						user[tempCard[i].getMember()].output.flush();
					} catch (IOException e) {
					} catch (NullPointerException e) {
					}
					System.out.println("changing");
					Package readPge = null;
					while( (!changed) ){}
					//user[i].implementCategory(readPge);
					System.out.println("changed");
					changed=false;
					int total = 0;
					for (int j = 0; j < 5; j++) {
						if(stackcard[stack.getWhichbeChange()][j]!=null){
						total = total + stackcard[stack.getWhichbeChange()][j].getPoint();
						stackcard[stack.getWhichbeChange()][j] = null;
						}
					}
					stackcard[stack.getWhichbeChange()][0] = tempCard[i];
					operation.getPlayer(tempCard[i].getMember()-1).add(total);
					System.out.println("換牌成功...");
					hit.add("玩家 "+user[tempCard[i].getMember()].getUserName()+" 換掉牌堆"+(stack.getWhichbeChange()+1)+"\n");
					hit.add("取得牛頭數: "+total+"\n");	
						
					
					
					
				} else {
					setOn(tempCard[i]);
				}

			}
		}
		
		
		public class tThread extends Thread{
			private int i;
			public tThread(int i)
			{
				this.i = i;
			}
			public void run()
			{
				try {
					lunch();
				} catch (ClassNotFoundException | IOException
						| InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Stack return");
				tStack = new Stack(stack.getStackcard());
				sendData(new Package(tStack));
				changePoint();
				for (int j = 1; j < (operation.getNumber()+1); j++) {
					hit.add("玩家 "+user[j].getUserName()+" 目前牛頭數:"+operation.getPlayer(j-1).getPoint()+"\n");
				}
				Package h = new Package();
				h.setCategory(11);
				h.setMessage(hit.getHistory());
				sendData(h);
				hit.setHistory("");
				//System.out.println("time to end");
				if(isend>=playerhandcountnumber)
				{
					//System.out.println("Ending");
					over();
					
					
				}
			}
		}
		
		
		private void setOn(Card card) {
			// TODO Auto-generated method stub
			int[] temp = new int[4];
			int[] difference = new int[4];
			int lowest;
			int which;
			for (int i = 0; i < 4; i++) {
				temp[i] = getMost(i);
			}
			for (int i = 0; i < 4; i++) {
				if (card.getNum() > temp[i]) {
					difference[i] = card.getNum() - temp[i];
				} else {
					difference[i] = 110;
				}
			}
			lowest = difference[0];
			which = 0;
			for (int i = 1; i < 4; i++) {
				if (difference[i] < lowest) {
					lowest = difference[i];
					which = i;
				}
			}
			add(which, card, operation.getPlayer(card.getMember()-1));

		}

		private boolean changeStack(Card card) {
			// TODO Auto-generated method stub
			if (card.getNum() < getMost(0) && card.getNum() < getMost(1)
					&& card.getNum() < getMost(2) && card.getNum() < getMost(3)) {
				return true;
			} else {
				return false;
			}
		}

		// 將玩家出的牌由小到大排好
		public void findSort() {
			Card t = new Card();
			for (int i = 0; i < membernumber - 1; i++) {
				for (int j = i + 1; j < membernumber; j++) {
					if (tempCard[i].getNum() > tempCard[j].getNum()) {
						t = tempCard[j];
						tempCard[j] = tempCard[i];
						tempCard[i] = t;
					}
				}
			}
		}

		public Card[][] getStackcard() {
			return stackcard;
		}

		public void setStackcard(Card[][] stackcard) {
			this.stackcard = stackcard;
		}

		public int getMost(int i) {

			for (int j = 0; j < 6; j++) {
				if (stackcard[i][j] == null) {
					break;
				}
				most[i] = j;
			}
			// System.out.println("i:"+i+" x:"+x);
			return stackcard[i][most[i]].getNum();
		}

		// 更新stack
		public void setStack(Card[][] c) {
			for (int i = 0; i < c.length; i++) {
				for (int j = 0; j < c[i].length; j++) {
					stackcard[i][j] = new Card(c[i][j]);
				}
			}

		}

		public void add(int i, Card c, Member P) {
			if (most[i] + 1 < 5) {
				stackcard[i][most[i] + 1] = c;
				System.out.println("Add card:" + i + " most: " + (most[i] + 1)
						+ " num: " + stackcard[i][most[i] + 1].getNum());
			} else {
				int total = 0;
				for (int j = 0; j < 5; j++) {
					total = total + stackcard[i][j].getPoint();
					stackcard[i][j] = null;
				}
				stackcard[i][0] = c;
				P.add(total);
				hit.add("玩家 "+user[c.getMember()].getUserName()+" 不幸在牌堆"+i+"放置第六張牌，必須收回前五張牌"+"\n");
				hit.add("取得牛頭數: "+total+"\n");
			}

		}

	}
	
	///////////////////      button handler area    ////////////////////////////////////////////////////
	
	public class startButton implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			smainFrame.dispose();
			conti=false;
			try {
				run();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
