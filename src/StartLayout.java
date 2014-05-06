import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StartLayout {
	private JButton build, participate;
	// private JTextField ip;
	private String ip;
	private JFrame frame;
	private JPanel startingPanel;
	private boolean turn = true;
	private int choose;

	private AudioPlayer audio;
	private AudioPlayer background;

	public StartLayout() {

	}

	public int lunch() {
//		setCowHorn();
		//use the synchronized thread is good 
		playSound("");
		frame = new JFrame("誰是牛頭王Take 6!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("here?");
		build = new JButton("建立遊戲");
		
		participate = new JButton("加入遊戲");
		JButton rule = new JButton("規則說明");
		// 按鈕
		startingPanel = new JPanel();
		startingPanel.setLayout(new GridLayout(1, 3));
		startingPanel.add(build, startingPanel);
		startingPanel.add(participate, startingPanel);
		startingPanel.add(rule, startingPanel);
		frame.add(startingPanel, BorderLayout.SOUTH);
		
		// 圖片
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/image/front.jpg"));
		icon.setImage(icon.getImage().getScaledInstance(400,
				500, Image.SCALE_DEFAULT));
		JLabel picLabel = new JLabel(icon);
		frame.add(picLabel, BorderLayout.NORTH);
		// set saboteur frame
		frame.setSize(400, 560);
		//picLabel.setBackground(Color.white);
		//picLabel.setOpaque(true);
		startingPanel.setBackground(Color.white);
		frame.setBackground(Color.white);
		frame.setVisible(true);
		frame.setLocation(450, 100);
		build.addMouseListener(new buttonHandler(1));
		participate.addMouseListener(new buttonHandler(2));
		rule.addMouseListener(new ruleHandler());
		while (turn) {
			System.out.println("test");
		}
		return choose;
	}
	public class ruleHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			JOptionPane.showMessageDialog(null, "基本配備：104張牌，從1~104，五的倍數2個牛頭、十的倍數3個牛頭、十一倍數5個牛頭、55有七個牛頭\n遊戲人數：2~10人，愈多人愈好玩\n遊戲一開始翻開四張分成四堆，當作開始牌堆\n每位玩家出一張蓋牌，等到大家都蓋出來後再同時翻開，由數字小的先接\n接尾：尋找場上\"最接近且比要接的牌還小\"的後方放置如果沒有比該牌還小的牌，由出牌者\"自由任選一堆收牌\"\n場上每個牌組\"最多五張\"，如果該牌放下去為第六張，則強制收取前五張再放置第六張。\n收得牌為吃掉，吃掉的牌不能再出\n遊戲最後要計算誰吃掉的牌中，牛頭總和最低即為優勝者", "規則說明", JOptionPane.WARNING_MESSAGE  );
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
	public class buttonHandler implements MouseListener {
		private int data;

		public buttonHandler(int i) {
			data = i;

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub

			choose = data;
			turn = false;
			frame.dispose();
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

	public void setCowHorn() {// 建立牛的音樂
		this.audio = new AudioPlayer();// 建立播音樂元件
		try{
		audio.loadAudio(getClass().getResource("/midi/cow_short.wav"));// test.wav是我測試的音檔，請自行拿手邊wav檔測試)
		audio.setPlayCount(1);// 播放次數，設定0表示一直播放
		audio.play();// 開始播放
		audio.setVolume(2);
//		audio.close();
		//setBGMusic();
		}
		catch(Exception e){
		}
	}

	public void setBGMusic() {// 設定背景音樂
		this.background = new AudioPlayer();// 建立播音樂元件
		audio.loadAudio("midi/cow_short.wav", null);// test.wav是我測試的音檔，請自行拿手邊wav檔測試)
		audio.setPlayCount(0);// 播放次數，設定0表示一直播放
		audio.play();// 開始播放
		audio.setVolume(2);

	}
	
	public static synchronized void playSound(final String url) {
		  new Thread(new Runnable() {
		  // The wrapper thread is unnecessary, unless it blocks on the
		  // Clip finishing; see comments.
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
		          getClass().getResourceAsStream("/midi/cow_short.wav"));
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		        System.err.println(e.getMessage());
		      }
		    }
		  }).start();
		}

}
