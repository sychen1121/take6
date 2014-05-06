import java.awt.Color;

import javax.swing.JButton;


public class gameEvent {
	private JButton selfCard=null;
	private int selfCardIndex;
	
	
	
	
	public void setCardColor(JButton b, int index)
	{
		if(selfCard!=null){
			selfCard.setBackground(Color.white);
			selfCard = b;
			selfCard.setBackground(Color.LIGHT_GRAY);
			selfCardIndex = index;
		}
		else
		{
			selfCard = b;
			selfCard.setBackground(Color.LIGHT_GRAY);
			selfCardIndex = index;
		}
	}
	
	public int send(){
		return selfCardIndex;
	}
}
