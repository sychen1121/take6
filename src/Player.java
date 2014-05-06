
public class Player {
	Card[] hand;
	int point = 0;
	int id;
	
	/*****for testing**********/
	public void run(){
		hand = new Card[10];
		for(int i = 0; i<10;i++){
			hand[i] = new Card((i+1)*2,1,"image/"+((i+1)*2)+".jpg");
		}
	}
	/*************************/
	
	
	Player(int id){
		this.id = id;
	}
	
	public void setHand(Card[] hand){
		this.hand = hand;
	}
	public Card getHand(int index){
		return hand[index];
	}
	public int handNum(int index){
		return hand[index].getNum();
	}
	public String handImg(int index){
		return hand[index].getImg();
	}
	public int handLength(){
		return hand.length;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
	/**
	 * ¾ã²z¤âµP
	 */
	public void sort(){
		Card temp;
		System.out.println("Sort start...");
		for( int i = 0; i < 10-1; i++){
			for( int j = i+1; j < 10; j++){
				if( hand[j].getNum()<hand[i].getNum() ){
					temp = hand[j];
					hand[j] = hand[i];
					hand[i] = temp;
				}
			}
		}
	}
	
}
