import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Starting{
	private JButton build, participate;
	//private JTextField ip;
	private String ip;
	private JFrame saboteur;
	private JPanel startingPanel;
	private Server server;
	private Client client;
	public static void main(String[] args) throws InterruptedException{
		//layout();
		int c;		
		StartLayout layout = new StartLayout();
		c=layout.lunch();
		if(c==1)
		{
		Server server = new Server();
		server.runserver();
		}
		else if(c==2)
		{
			String address = JOptionPane.showInputDialog(null,"請輸入連線IP","連線中...",JOptionPane.INFORMATION_MESSAGE);
			Client client = new Client(address);
			client.layout();
			client.waitToStart();
			client.runclient();
		}
		else
		{
			System.out.println("System error...");
			System.exit(0);
		}
	}
	
	
	
	
	
	
	
	
	
	////////////////////////inner  class/////////////////////////////////////////////////
	
	
	
}
