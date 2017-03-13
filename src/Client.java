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
		
		//Weight simulator asks about Item number
		public void flow3(BufferedReader Answer, DataOutputStream Reply) throws IOException
		{
			//Sekvens 3 kunne kombineres med sekvens 4.
			data.setMessage("Type Itemm number");
			Reply.writeBytes("RM20 4 \"" + data.getMessage() + "\" \" \" \"&3\"\r\n");
			this.flow4(Answer, Reply);
		}
		
// Item number reads and saves localy
		public void sequence4(BufferedReader Answer, DataOutputStream Reply) throws IOException
		{
			data.setInput(Answer.readLine());
			data.setInput(data.getInput().split());
			// Here we get RM20 B which inholds an Item number
			//Command separets with help of split and we chose place [2], in this way we end with an Iteam number
			if(data.getInput().equals("RM20 B")){ 
				data.setInput(Answer.readLine());
				if(!aborted()){
					data.setInput(data.getInput().split(" "));
					
					System.out.println("" + data.getInput());
					System.out.println("" + data.getInput()[2]);
					
					String temp;
					temp = data.getInput()[2].replaceAll("\"","");
					System.out.println(temp);
					
					data.setItemNoInput(Integer.parseInt(temp));
					this.flow5_6(Answer, Reply);
				}
				else
					this.flow1(Answer, Reply);
			}
		}
		



	}
