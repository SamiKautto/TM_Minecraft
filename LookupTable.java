package me.turing.machine.turing_machine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

// Responsible for reading a text file to create the lookup table,
// which is used to convert tape symbols into blocks.
public class LookupTable {
	public static HashMap<String,String> createTable(String filepath) {
		HashMap<String,String> items = new HashMap<>();
		
		try {
		      File file = new File(filepath);
		      Scanner reader = new Scanner(file);
		      while (reader.hasNextLine()) {
		    	  String line = reader.nextLine();
		    	  
		    	  if(!line.isEmpty() && line.charAt(0) != '#' ) {
		    		  String[] parts = line.split(",");
		    		  if (parts.length > 1 && parts[1].strip().startsWith("minecraft:")) { // valid mapping
		    			  items.put(parts[0].strip(), parts[1].strip());
		    		  }
		    	  }
		      }
		      reader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("@ File could not be found");
	    }
		
		if (!items.isEmpty()) {
			return items;
		}
		
		return null;
	}
	
	// Yields the key of the first pair, and this becomes the blank symbol
	public static String blankSymbol(String filepath) {
		String symbol = "";
		boolean found = false;
		
		try {
		      File file = new File(filepath);
		      Scanner reader = new Scanner(file);
		      while (reader.hasNextLine() && !found) {
		    	  String line = reader.nextLine();
		    	  
		    	  if(!line.isEmpty() && line.charAt(0) != '#' ) {
		    		  symbol = line.split(",")[0].strip();
		    		  found = true;
		    	  }
		      }
		      reader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("@ File could not be found");
	    }
		
		return symbol;
	}
}