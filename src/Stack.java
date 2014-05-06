import java.io.Serializable;


public class Stack implements Serializable{
	private Card[][] stackcard = new Card[4][6];
	
	public Stack(Card[][] c)
	{
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<6;j++)
			{
				if(c[i][j]!=null)
				{
					stackcard[i][j] = new Card(c[i][j]);
				}
				else
				{
					stackcard[i][j]=null;
				}
			}
		}
	}
	public Stack(Stack c)
	{
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<6;j++)
			{
				if(c.getStackcard()[i][j]!=null)
				{
					stackcard[i][j] = new Card(c.getStackcard()[i][j]);
				}
				else
				{
					stackcard[i][j]=null;
				}
			}
		}
	}
	public Card[][] getStackcard() {
		return stackcard;
	}
	public void setStackcard(Card[][] stackcard) {
		
		
	}
	
	public int getMost(int i)
	{
		return stackcard[i][stackcard[i].length-1].getNum();
	}
	
	
	//§ó·sstack
	public void setStack(Card[][] c)
	{
		for(int i=0;i<c.length;i++)
		{
			for(int j=0;j<c[i].length;j++)
			{
				stackcard[i][j]=new Card(c[i][j]);
			}
		}
		
	}
	
	public void add(int i,Card c,Member P){
		if(stackcard[i].length<5)
		{
			stackcard[i][stackcard[i].length]=new Card(c);
		}
		else
		{
			int total=0;
			for(int j=0;j<5;j++)
			{
				total=total+stackcard[i][j].getNum();
				stackcard[i][j]=null;
			}
			stackcard[i][0]=new Card(c);
			P.add(total);
		}
		
	}
	
	
	
	
}
