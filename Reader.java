package me.turing.machine.turing_machine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// Responsible for reading the Turing Machine instructions
public class Reader {
	public static ArrayList<Tuple<Integer,String,Integer,String,Integer>> read(String filepath) {
		ArrayList<Tuple<Integer,String,Integer,String,Integer>> list = new ArrayList<Tuple<Integer,String,Integer,String,Integer>>();
		
		try {
		      File file = new File(filepath);
		      Scanner reader = new Scanner(file);
		      while (reader.hasNextLine()) {
		    	  String line = reader.nextLine();
		    	  
		    	  if(!line.isEmpty() && line.charAt(0) != '#' ) {
		    		  String[] parts = line.split(",");
		    		  if (parts.length >= 5 && isNumeric(parts[0]) && isNumeric(parts[2]) && isNumeric(parts[4])) { // Too short instructions will be ignored, as well as any parts after the first 5 
		    			  Tuple<Integer,String,Integer,String,Integer> tuple = new Tuple<Integer, String, Integer, String, Integer>(Integer.parseInt(parts[0]),parts[1],Integer.parseInt(parts[2]),parts[3],Integer.parseInt(parts[4]));
		    			  list.add(tuple);
		    		  }
		    	  }
		      }
		      reader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("@ File could not be found");
	    }
		
		if (!list.isEmpty()) {
			
			return list;
		}
		
		return null;
		
	}
	
	
	
	private static boolean isNumeric(String entry) {
		try {
			Integer.parseInt(entry);
		} catch(NumberFormatException e) {
			return false;
		}
		
		return true;
	}
}