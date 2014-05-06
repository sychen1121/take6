
public class Member {
	private int point;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPoint() {
		return point;
	}
	public Member() {
		super();
		this.point = 0;
	}
	public void add(int c)
	{
		point= point+c;
	}
}
