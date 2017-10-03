import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VoidLogWriter{
	
	private FileWriter logWriter;

	public VoidLogWriter(){
		
		int i = 0;
		String name = ("log" + i + ".txt");
		
		try{	
			
			File logFile = new File(name);
			
			while(!logFile.createNewFile()){
				
				i++;
				name = ("log" + i + ".txt");
				logFile = new File(name);
				
			}
		
		}catch (IOException e){
		
			System.out.println("Error: Something went wrong while attempting to create a logfile.");
		
		}
		
		try{
		
			logWriter = new FileWriter(name);
		
		}catch (IOException e){
		
			System.out.println("Error: Something went wrong while attempting to create a writer for the logfile.");
		
		}
		
	}
	
	public void write(String logEntry){
	
		try{
		
			logWriter.write(logEntry + "\n");
		
		}catch (IOException e){
		
			System.out.println("Error: IOException thrown while writing to the logfile.");
		
		}
	
	}
	
	public void close(){
	
		try{
		
			logWriter.close();
		
		}catch (IOException e){
		
			System.out.println("Error: IOException thrown while closing the logfile.");
		
		}
	
	}

}