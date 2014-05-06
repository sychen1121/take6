
public class MemberList {
	private int number;
	private Member[] player=new Member[10];
	
	public MemberList(int i)
	{
		number = i;
		for(int j=0;j<i;j++){
			player[j]=new Member();
		}
		
	}
	public Member getPlayer(int i)
	{
		return player[i];
	}
	public int getNumber()
	{
		return number;
	}
}
