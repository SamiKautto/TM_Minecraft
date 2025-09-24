package me.turing.machine.turing_machine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Responsible for reading some settings not addressed in the machine instructions
public class SettingsReader {
	public static HashMap<String, Integer> read(String filepath) {
		HashMap<String,Integer> settings = new HashMap<>();
		
		try {
		      File file = new File(filepath);
		      Scanner reader = new Scanner(file);
		      while (reader.hasNextLine()) {
		    	  String line = reader.nextLine();
		    	  if(!line.isEmpty() && line.charAt(0) != '#') {
		    		  String[] parts = line.split(":");
		    		  if (isInteger(parts[1].strip())) {
		    			  settings.put(parts[0].strip().toLowerCase(), Integer.parseInt(parts[1].strip()));
		    		  }
		    	  }
		      }
		      reader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("@ File could not be found");
	    }
		
		
		return settings;
		
	}
	
	
	
	
	// Test if the entry can be converted into an integer
	private static boolean isInteger(String entry) {
		try {
			Integer.parseInt(entry);
		} catch(NumberFormatException e) {
			return false;
		}
		
		return true;
	}
}