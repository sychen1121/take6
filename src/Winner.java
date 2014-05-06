import java.io.Serializable;


public class Winner implements Serializable{
	private String winner;
	private String record="";
	public Winner(){
		record="";
		winner="";
	}
	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	
	
	public void add(String s)
	{
		record = record + s;
	}

	
	
	
}
