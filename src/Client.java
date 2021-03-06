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

					data.setServer(Answer.readLine());
//					data.setServerInput(Answer.readLine());
//					data.setServerInput(Answer.readLine());
					data.setServer(Answer.readLine());

					
					data.setInput(data.getServer().split(" "));
					if(data.getInput().equals("RM20 B"))
					data.setInput(data.getServer().split(" "));
					if(data.getServer().equals("RM20 B"))
					{
						data.setServer(Answer.readLine());
						data.setServer(Answer.readLine());
						if(!aborted()){
							data.setInput(data.getServer().split(" "));
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
		
		//Display asks about tara and a user confirm.
				public void flow7(BufferedReader Answer, DataOutputStream Reply) throws IOException
				{
					data.setMessage("Place the bowl");
					Reply.writeBytes("RM20 4 \"" + data.getMessage() + "\" \" \" \"&3\"\r\n");

					data.setServer(Answer.readLine());

					if(data.getInput().equals("RM20 B"))
						data.setServer(Answer.readLine());
					else
						this.flow7(Answer, Reply);

					if(!aborted())
						this.flow8(Answer, Reply);
					else
						this.flow1(Answer, Reply);

				}
				
				//Wieght tares and tara saves localy
				public void flow8(BufferedReader Answer, DataOutputStream Reply) throws IOException
				{
					Reply.writeBytes("T\r\n");

					data.setServer(Answer.readLine());
					if(data.getServer().startsWith("T S")){
						data.setInput(data.getServer().split(" +"));
						data.setTara(Double.parseDouble(data.getInput()[2]));
						this.flow9(Answer, Reply);
					}
					else this.flow7(Answer, Reply);
				}
				
				//The operator is instructed to fill item, and then press enter.

				public void flow9(BufferedReader Answer, DataOutputStream Reply) throws IOException{
					data.setMessage("Fill item");
					Reply.writeBytes("RM20 4 \"" + data.getMessage() + "\" \" \" \"&3\"\r\n");

					data.setServer(Answer.readLine());

					if(data.getServer().equals("RM20 B"))
						data.setServer(Answer.readLine());
					else
						this.flow9(Answer, Reply);

					if(!aborted())
						this.flow10(Answer, Reply);
					else
						this.flow10(Answer, Reply);
				}
				
				// Netto registering
				public void flow10(BufferedReader Answer, DataOutputStream Reply) throws IOException{

					Reply.writeBytes("S\r\n");

					data.setServer(Answer.readLine());
					if(data.getServer().startsWith("S S")){
						data.setInput(data.getServer().split(" +"));
						data.setNetto(Double.parseDouble(data.getInput()[2])-data.getTara());
						this.flow11(Answer, Reply);
					}
					else this.flow9(Answer, Reply);
				}
				
				// 11- The operator is instructed to remove the net and tare.
				public void flow11(BufferedReader Answer, DataOutputStream Reply) throws IOException{
					data.setMessage("Remove devices");
					Reply.writeBytes("RM20 4 \"" + data.getMessage() + "\" \" \" \"&3\"\r\n");

					data.setServer(Answer.readLine());


					if(data.getServer().equals("RM20 B"))
						data.setServer(Answer.readLine());

					else
						this.flow11(Answer, Reply);


					if(!aborted())
						this.flow12(Answer, Reply);
					else
						this.flow1(Answer, Reply);
				}
				
		//12- Weight tare, so it is ready for a new lap
				public void flow12(BufferedReader Answer, DataOutputStream Reply) throws IOException{

					Reply.writeBytes("T\r\n");
					this.flow13(Answer, Reply);
				}
		// Minus brutto register
				public void flow13(BufferedReader Answer, DataOutputStream Reply) throws IOException{

					data.setServer(Answer.readLine());
					if(data.getServer().startsWith("T S")){
						data.setInput(data.getServer().split(" +"));
						data.setBruttoCheck(Double.parseDouble(data.getInput()[2]));
						this.flow13(Answer, Reply);
					}
					else this.flow14(Answer, Reply);
				}
				
				// 14- Gross Control OK if this is the case.
				public void flow14(BufferedReader Answer, DataOutputStream Reply) throws IOException{
					if(data.getBruttoCheck() >= 2 || data.getBruttoCheck() <= -2){

						data.setMessage("balancing rejected");
						Reply.writeBytes("RM20 4 \"" + data.getMessage() + "\" \" \" \"&3\"\r\n");

						data.setServer(Answer.readLine());

						if(data.getServer().equals("RM20 B"))
							data.setServer(Answer.readLine());
						else
							this.flow14(Answer, Reply);

						if(data.getServer().startsWith("RM20 A"))
							this.flow7(Answer, Reply);
					}
					else{

						data.setMessage("Balancing approved!");

						Reply.writeBytes("RM20 4 \""+ data.getMessage() +"\"\r\n");
						Answer.readLine();
						Answer.readLine();
						this.flow15(Answer, Reply);
					}
				}
				
				// 15- Quantity in stock depreciated and history updated.
				public void flow15(BufferedReader Answer, DataOutputStream Reply) throws IOException{

					try{
						log();
					}
					catch(FileNotFoundException e){
						data.setMessage("Logfejl. Afvist!");
						Reply.writeBytes("RM20 4 \"" + data.getMessage() + "\" \" \" \"&3\"\r\n");

						data.setServer(Answer.readLine());

						if(data.getServer().equals("RM20 B"))
							data.setServer(Answer.readLine());
						else
							this.flow15(Answer, Reply);

						if(data.getServer().startsWith("RM20 A"))
							this.flow1(Answer, Reply);
					}


					this.flow1(Answer, Reply);
				}

				public void log() throws IOException{

					BufferedWriter bw = new BufferedWriter(new FileWriter(new File("Log.txt"), true));
					Calendar c = Calendar.getInstance();
					DecimalFormat df = new DecimalFormat("00");
					String timeStamp = "" + c.get(Calendar.YEAR) + "-" + df.format(c.get(Calendar.MONTH) + 1) + "-" + df.format(c.get(Calendar.DATE)) + "-" + df.format(c.get(Calendar.HOUR_OF_DAY)) + ":" + df.format(c.get(Calendar.MINUTE)) + ":" + df.format(c.get(Calendar.SECOND));

					bw.write(timeStamp + ", " + data.getOpratorID() + ", " + data.getItemNoInput() + ", " + data.getItemName() + ", " + data.getNetto() + " kg.");
					bw.newLine();
					bw.flush();
					bw.close();
				}



		boolean aborted() {
			// TODO Auto-generated method stub
			return !data.getServer().startsWith("RM20 A");

		}


	}
