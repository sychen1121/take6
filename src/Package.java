import java.io.Serializable;


public class Package extends Object implements Serializable{
	private int category;//1:聊天室的訊息 2:遊戲中的訊息 3:Card 4:Stack 5:user name 6:deal  7:serverStack  8:送個人資訊  9:問使用者要換哪個牌堆  10:使用者選擇哪個牌堆  11:歷史訊息   99:結束遊戲    100:回應server結束遊戲
	public void setCategory(int category) {
		this.category = category;
	}
	private Winner win;
	private String message;
	private Card card;
	private Card[][] stack;
	private Card[] dealcard;
	//private ServerStack sStack;
	private Stack tStack;
	private int Point;
	private int whichStack;
	public int getWhichStack() {
		return whichStack;
	}
	public void setWhichStack(int whichStack) {
		this.whichStack = whichStack;
	}
	public int getPoint() {
		return Point;
	}
	public Stack gettStack() {
		return tStack;
	}
	/*public ServerStack getsStack() {
		return sStack;
	}
	public void setsStack(ServerStack sStack) {
		this.sStack = sStack;
	}*/
	public Package(){
		category = 0;
		message = null;
		card = null;
		stack = null;
		dealcard=null;
	}
	public Package(Winner w)
	{
		this.category = 99;
		this.win=w;
	}
	public Package(int c, String m){
		this.category = c;
		this.message = m;
	}
	public Package(Stack s){
		this.category = 7;
		this.tStack =new Stack(s);
		System.out.println("7");
	}
	public Package(Card[] m){
		System.out.println("6");
		this.category = 6;
		this.dealcard = m;
	}
	public Package(String m){
		this.category = 1;
		this.message = m;
	}
	public Package(Card c){
		this.category =3;
		this.card = c;
	}
	public Package(int p){
		this.category =8;
		this.Point = p;
	}
	public Package(Card[][] s){
		this.category = 4;
		this.stack = s;
		for(int i=0;i<4;i++)
		{
			
			for(int j=0;j<6;j++){
				if(this.stack[i][j]!=null){
				System.out.println(""+this.stack[i][j].getNum());
				}
				//System.out.println("num:"+j);
			}
		
		}
	}
	public int isType(){
		return category;
	}
	public Card[] getDealCard(){
		return dealcard;
	}
	public String getMessage(){
		return message;
	}
	public Card getCard(){
		return card;
	}
	public Card[][] getStack(){
		return stack;
	}
	public void setMessage(String m){
		this.message= m;
	}
	public void setCard(Card c){
		this.card = c;
	}
	public void setStack(Card[][] s){
		this.stack = s;
	}
	public Winner getWin()
	{
		return win;
	}
}
