import java.io.Serializable;


public class Card implements Serializable{
	
		private int num;
		private int point;
		public int getPoint() {
			return point;
		}

		private String img = null;
		
		public int getNum() {
			return num;
		}

		private int member;
		public int getMember() {
			return member;
		}

		
		
		public Card(){
			
		}
		public Card(int num){
			this.num = num;
		}
		public Card(int num, int point, String img){
			this.num = num;
			this.point = point;
			this.img = img;
		}
		public Card(int num, int member, int point, String img){
			this.num = num;
			this.member = member;
			this.point = point;
			this.img = img;
		}
		
		public String toString(){
			return Integer.toString(num);
		}
		public void setNum(int j){
			num = j;
		}
		public void setMember(int j){
			member = j;
		}
		public void setPoint(int j){
			point = j;
		}
		public String getImg(){
			return img;
		}
		
		public Card(Card c)
		{
			num = c.num;
			member = c.member;
			point = c.point;
			img = c.img;
		}
	
}
