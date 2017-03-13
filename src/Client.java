import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

public class Client {

	Data data = new Data();


	//Weight asking for operator ID and wait for Answer
	public void flow1(BufferedReader Answer, DataOutputStream Reply)throws IOException{
		{
			data.setMessage("Enter ID");
			Reply.writeBytes("RM20 8 \"" + data.getMessage() + "\" \" \" \"&3\"\r\n");
			Reply.flush();
			this.flow2(Answer, Reply);			
		}
		
		//Operator ID typed and save localy
		public void flow2(BufferedReader Answer, DataOutputStream Reply)throws IOException{
			data.setInput(Answer.readLine());
			data.setInput(Answer.readLine());
			if(data.getServer().equals("RM20 B")){
				data.setServer(Answer.readLine());
				if (!aborted()) {
					data.setInput(data.getServer().split(" "));
					data.setOpratorID(data.getInput()[2]); // Should be check
					this.flow3(Answer, Reply);
				}
				else
					this.flow1(Answer, Reply);
			}
		}
		



	}
