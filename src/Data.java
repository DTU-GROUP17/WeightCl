
public class Data {
	
	String Message, server, opratorID, itemName, User;
	int itemNoInput, itemNoStore;
	String[] Input = new String[10];
	double tara, netto, bruttoCheck;
	
	
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		this.Message = message;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getOpratorID() {
		return opratorID;
	}
	public void setOpratorID(String opratorID) {
		this.opratorID = opratorID;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getUser() {
		return User;
	}
	public void setUser(String user) {
		User = user;
	}
	public int getItemNoInput() {
		return itemNoInput;
	}
	public void setItemNoInput(int itemNoInput) {
		this.itemNoInput = itemNoInput;
	}
	public int getItemNoStore() {
		return itemNoStore;
	}
	public void setItemNoStore(int itemNoStore) {
		this.itemNoStore = itemNoStore;
	}
	public String[] getInput() {
		return Input;
	}
	public void setInput(String[] input) {
		Input = input;
	}
	public double getTara() {
		return tara;
	}
	public void setTara(double tara) {
		this.tara = tara;
	}
	public double getNetto() {
		return netto;
	}
	public void setNetto(double netto) {
		this.netto = netto;
	}
	public double getBruttoCheck() {
		return bruttoCheck;
	}
	public void setBruttoCheck(double bruttoCheck) {
		this.bruttoCheck = bruttoCheck;
	}
	
	

}
