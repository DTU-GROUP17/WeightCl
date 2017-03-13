import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class Main {

	public static void main(String[] args) throws Exception { 
		// TODO Auto-generated method stub
		
		// IP should change with your current IP
		Client f = new Client();
		Socket clientSocket = new Socket("IP", 8000);
		
		DataOutputStream Reply = 
				new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader Answer = 
				new BufferedReader(new
						InputStreamReader(clientSocket.getInputStream())); 
		
		f.flow1(Answer, Reply);

		clientSocket.close();

	}

}
