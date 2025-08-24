package me.turing.machine.turing_machine;

//--------------------------
    // Gives test instructions, based on the given integer.
	// Used for testing generation, they themselves are mostly just
	// repeated instructions1 with minor modifications.
public class testInstructions {
    
   	public static int[][] instructions(int i) {
    	
    	if(i == 1) {
	    	//3-state busy beaver as one of the test TMs.
	        int[][] instructions1 ={
	        		{1,0,2,1,0},
	        		{1,1,3,1,1},
	        		{2,0,1,1,1},
	        		{2,1,2,1,0},
	        		{3,0,2,1,1},
	        		{3,1,-1,1,0}
	        		};
	        
	        return instructions1;
    	} else if(i == 2) {
    		//longer machine, testing generation of multi-segmented machines
            int[][] instructions2 ={
            		{1,0,2,1,0},
            		{1,1,3,1,1},
            		{2,0,1,1,1},
            		{2,1,2,1,0},
            		{3,0,2,1,1},
            		{3,1,-1,1,0},
            		{4,0,5,1,0},
            		{4,1,6,1,1},
            		{5,0,6,1,1},
            		{5,1,5,1,0},
            		{6,0,4,1,1},
            		{6,1,3,1,0},
            		{7,0,2,1,0},
            		{7,1,3,1,1},
            		{8,0,1,1,1},
            		{8,1,2,1,0},
            		{9,0,2,1,1},
            		{9,1,4,1,0},
            		{10,0,5,1,0},
            		{10,1,6,1,1},
            		{11,0,6,1,1},
            		{11,1,5,1,0},
            		{12,0,4,1,1},
            		{12,1,3,1,0},
            		{13,0,2,1,0},
            		{13,1,3,1,1},
            		{14,0,1,1,1},
            		{14,1,2,1,0},
            		{15,0,2,1,1},
            		{15,1,-1,1,0},
            		{16,0,5,1,0},
            		{16,1,6,1,1},
            		{17,0,6,1,1},
            		{17,1,5,1,0},
            		{18,0,4,1,1},
            		{18,1,3,1,0},
            		{19,0,2,1,0},
            		{19,1,3,1,1},
            		{20,0,1,1,1},
            		{20,1,2,1,0},
            		{21,0,2,1,1},
            		{21,1,4,1,0},
            		{22,0,5,1,0},
            		{22,1,6,1,1},
            		{23,0,6,1,1},
            		{23,1,5,1,0},
            		{24,0,4,1,1},
            		{24,1,5,1,0}
            		};
            return instructions2;
            
    	} else if(i == 3) {
    		// machine with length divisible by 15, to test how to work with a machine which ends in the cut-off point of a segment
            int[][] instructions3 ={
            		{1,0,2,1,0},
            		{1,1,3,1,1},
            		{2,0,1,1,1},
            		{2,1,2,1,0},
            		{3,0,2,1,1},
            		{3,1,-1,1,0},
            		{4,0,5,1,0},
            		{4,1,6,1,1},
            		{5,0,6,1,1},
            		{5,1,5,1,0},
            		{6,0,4,1,1},
            		{6,1,3,1,0},
            		{7,0,2,1,0},
            		{7,1,3,1,1},
            		{8,0,1,1,1},
            		{8,1,2,1,0},
            		{9,0,2,1,1},
            		{9,1,4,1,0},
            		{10,0,5,1,0},
            		{10,1,6,1,1},
            		{11,0,6,1,1},
            		{11,1,5,1,0},
            		{12,0,4,1,1},
            		{12,1,3,1,0},
            		{13,0,2,1,0},
            		{13,1,3,1,1},
            		{14,0,1,1,1},
            		{14,1,2,1,0},
            		{15,0,2,1,1},
            		{15,1,-1,1,0}
            		};
            return instructions3;
    	} else if(i == 4) {
    		
    		// Shorter machine whose length is with divisible by 15
    		int[][] instructions4 ={
            		{1,0,2,1,0},
            		{1,1,3,1,1},
            		{2,0,1,1,1},
            		{2,1,2,1,0},
            		{3,0,2,1,1},
            		{3,1,-1,1,0},
            		{4,0,5,1,0},
            		{4,1,6,1,1},
            		{5,0,6,1,1},
            		{5,1,5,1,0},
            		{6,0,4,1,1},
            		{6,1,3,1,0},
            		{7,0,2,1,0},
            		{7,1,3,1,1},
            		{8,0,1,1,1},
    		};
    		return instructions4;
    		
    	} else if(i == 5) {
    		// Machine with just one instructions
    		int[][] instructions5 ={
            		{1,0,2,1,0}
    		};
    		return instructions5;
    	
    		
    	} else if(i == 6) {
    		// Machine with the length of 16
    		int[][] instructions6 ={
            		{1,0,2,1,0},
            		{1,1,3,1,1},
            		{2,0,1,1,1},
            		{2,1,2,1,0},
            		{3,0,2,1,1},
            		{3,1,-1,1,0},
            		{4,0,5,1,0},
            		{4,1,6,1,1},
            		{5,0,6,1,1},
            		{5,1,5,1,0},
            		{6,0,4,1,1},
            		{6,1,3,1,0},
            		{7,0,2,1,0},
            		{7,1,3,1,1},
            		{8,0,1,1,1},
            		{8,1,1,1,1}
    		};
    		return instructions6;

    	} else {
    		int[][] empty = {};
    		return empty;
    	}
    }
}
