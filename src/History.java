
public class History {
	//private String Temp;
	private String history;
	
	public History() {
		super();
		//Temp = null;
		history = new String("");
	}
	public void add(String s)
	{
		history = history + s;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	
}
