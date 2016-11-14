package Glex;

import java.io.*;
import java.util.ArrayList;

public class IOHelper {
	public static ArrayList<String> readFile(String filePath) {
		ArrayList<String> filestrs = new ArrayList<String>();
		try {
	        File file=new File(filePath);
	        if(file.isFile() && file.exists()){
	            InputStreamReader read = new InputStreamReader(new FileInputStream(file));
	            BufferedReader bufferedReader = new BufferedReader(read);
	            String lineTxt = null;
	            while((lineTxt = bufferedReader.readLine()) != null){
	                filestrs.add(lineTxt);
	            }
	            read.close();
	        }else{
	        	System.out.println("file not exist");
	        }
	    } catch (Exception e) {
	        System.out.println("file open failed");
	        e.printStackTrace();
	    }
		return filestrs;
	}
	
	
	public static void saveFile(ArrayList<String> list,String filePath) {
       
		try {
			FileWriter fw = new FileWriter(filePath);
			for (String str: list){
	            fw.write(str);
	            fw.write("\n");
	        }
	        fw.flush();
	        fw.close();
		} catch (IOException e) {
			System.out.println("file save failed");
			e.printStackTrace();
		}
        
	}

}
