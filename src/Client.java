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
		}
		
		//Operator ID typed and save localy
		public void flow2(BufferedReader Answer, DataOutputStream Reply)throws IOException{
			data.setServer(Answer.readLine());
			data.setServer(Answer.readLine());
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
		public void flow4(BufferedReader Answer, DataOutputStream Reply) throws IOException
		{
			data.setServer(Answer.readLine());
			data.setInput(data.getServer().split(" "));
			// Here we get RM20 B which inholds an Item number
			//Command separets with help of split and we chose place [2], in this way we end with an Iteam number
			if(data.getInput().equals("RM20 B")){ 
				data.setServer(Answer.readLine());
				if(!aborted()){
					data.setInput(data.getServer().split(" "));
					
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
		

		
		/* 5- Compare and check userinput and Item number
		 * 6- When the Item number has foun, program will ask the user 
		 * about the correctness of the Iteam name.
		 */

		public void flow5_6(BufferedReader Answer, DataOutputStream Reply) throws IOException
		{

			BufferedReader localAnswer = new BufferedReader(new FileReader(new File("shop.txt")));

			boolean notFound = true;

			while(notFound){
				try{
					data.setInput(localAnswer.readLine().split(","));
				}
				catch(NullPointerException e){
					notFound = true;
				}
				data.setItemNoStore(Integer.parseInt(data.getInput()[0]));
				System.out.println("store: " + data.getItemNoStore());
				System.out.println("input: " + data.getItemNoInput() + "\n");
				
				if(data.getItemNoStore() == data.getItemNoInput()){ 
					localAnswer.close();
					//Så snart at det indtastede nummer er lig et nummer i "databasen", 
					// sættes notFound = false og nedenstående kode eksekveres. 
					notFound = false;
					data.setItemName(data.getInput()[1]);
		
					data.setMessage("Item: " + data.getItemName()); 
					
					Reply.writeBytes("RM20 4 \"" + data.getMessage() + "\" \" \" \"&3\"\r\n");

					data.setInput(Answer.readLine());
//					data.setServerInput(Answer.readLine());
//					data.setServerInput(Answer.readLine());
					data.setServer(Answer.readLine());

					
					data.setInput(data.getInput().split(" "));
					if(data.getInput().equals("RM20 B"))
					data.setInput(data.getServer().split(" "));
					if(data.getServer().equals("RM20 B"))
					{
						data.setInput(Answer.readLine());
						data.setServer(Answer.readLine());
						if(!aborted()){
							data.setInput(data.getInput().split(" "));
							data.setInput(data.getServer().split(" "));
							data.setUser(data.getInput()[2]);		

							//Hvis varen er korrekt fortsættes der til sekvens 7 ellers starter man forfra i sekvens 3.						
					
							if(data.getUser().equals("\"1\""))
							{
								this.flow7(Answer, Reply);
							}
							//Fejl: Kan annullere, men kan derefter ikke v�lge samme vare igen.
							
							else if(data.getUser().equals("\"0\""))
							{
								this.flow3(Answer, Reply);
							}
						}
						else
							this.flow1(Answer, Reply);
					}
				}
			}
			data.setMessage("Item number not found");
			Reply.writeBytes("RM20 4 \"" + data.getMessage() + "\" \" \" \"&3\"\r\n");
			Answer.readLine();
			this.flow3(Answer, Reply);

		}
		



		boolean aborted() {
			// TODO Auto-generated method stub
			return !data.getServer().startsWith("RM20 A");

		}


	}
