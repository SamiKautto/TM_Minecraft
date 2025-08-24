package tm.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

// Responsible for the Turing machine instruction generation GUI.
// Purpose is to make the process more convenient users, as
// opposed to only writing the text files.
public class tm_gui extends JFrame {
	private TextField  nameField; // Contains he name of the set of instructions, also becomes file name
	private TextField alphabetField; // Contains the alphabet
	private TextField statesField; // Contains how many states are there  in total
	private ScrollPane instructionsPane; // Contains the instructionsPanel, to grant the scroll ability
	private Label errorLabel; // Informs the user of erroneous input.
	private boolean[][][] instructionsEntered; // Keeps track of which cells have all their instructions entered; Machine is valid only when they all are turned true.
	private Button writeButton; // Will start the process to write the text file containing the Turing machine instructions
	
	public tm_gui() {
		Container contentPane = getContentPane(); // Everything is inside this
	    contentPane.setLayout(new BorderLayout());
	    
	    // Sets the common user controls: the name of this set of instructions, the alphabet,
	    // and the number of states alongside the update and write buttons
	    Panel commonControls = new Panel(new GridLayout(13,1));
	    Label nameLabel = new Label("Name of the set of Turing machine instructions");
	    nameField = new TextField("My instructions",1);
	    Label alphabetLabel = new Label("Enter the tape alphabet, separated by commas (,). First one will be considered the blank symbol.");
	    alphabetField = new TextField("",1);
	    Label statesLabel = new Label("Enter the number of states. Please only use an integer.");
	    statesField = new TextField("",1);
	    Button updateButton = new Button("Update instructions");
	    updateButton.addActionListener(new UpdateButtonPressed());
	    errorLabel = new Label("");
	    writeButton = new Button("Generate the text file");
	    writeButton.addActionListener(new WriteButtonPressed());
	    writeButton.disable();
	    
	    
	    // Add the controls to the commonControls and that to contentPane
	    commonControls.add(nameLabel);
	    commonControls.add(nameField);
	    commonControls.add(alphabetLabel);
	    commonControls.add(alphabetField);
	    commonControls.add(statesLabel);
	    commonControls.add(statesField);
	    commonControls.add(new Label("       "));
	    commonControls.add(updateButton);
	    commonControls.add(new Label("       "));
	    commonControls.add(errorLabel);
	    commonControls.add(new Label("       "));
	    commonControls.add(writeButton);
	    commonControls.add(new Label("       "));
	    contentPane.add(commonControls, BorderLayout.NORTH);
	    
	    
	    // Next is the instructions grid
	    instructionsPane = new ScrollPane();
	    contentPane.add(instructionsPane, BorderLayout.CENTER);
	    
	    
	    // Finally, miscellaneous settings
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setTitle("Turing machine instructions");
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    setSize((int)(screenSize.getWidth()/1.5), (int)(screenSize.getHeight()/1.5));
	    setLocation((int)(screenSize.getWidth()/10.0),(int)(screenSize.getHeight()/10.0));
	    
	    
	    // Aaand action!
	    setVisible(true);
	}
	
	
	// Creates the instructions panel. We assume validity checks are done before calling this
	private Panel createInstructionsPanel() {		
		String[] alphabet = alphabetField.getText().split(",");
		int width = alphabet.length;
		int height = Integer.parseInt(statesField.getText().strip());
		
		instructionsEntered = new boolean[height][width][3]; // height * width = the amount of instructions, 3 = number of elements that need filled (2 Choices + 1 set or radios)
		
		Panel instructions = new Panel(new GridLayout(height +1, width +1,2,2));
	    instructions.setBackground(new Color(0,0,0));
	    
	    for(int i = 0; i <= height; i++) {
	    	for(int j = 0; j <= width; j++) {
	    		if (i == 0 && j == 0) {
	    			Panel cell = new Panel();
	    			cell.add(new Label("Current state \\ Read symbol"));
	    			cell.setBackground(new Color(220,220,220));
	    			instructions.add(cell);
	    		} else if (i == 0) {
	    			Panel cell = new Panel();
	    			cell.add(new Label(alphabet[j-1].strip()));
	    			cell.setBackground(new Color(220,220,220));
	    			instructions.add(cell);
	    		} else if(j == 0) {
	    			Panel cell = new Panel();
	    			cell.add(new Label(Integer.toString(i)));
	    			cell.setBackground(new Color(220,220,220));
	    			instructions.add(cell);
    			} else {
	    			instructions.add(instructionPanelCell((i-1) + "-" + (j-1)));
	    		}
	    	}
	    }
	    
	    return instructions;
	}
	
	
	// Contains the necessary controls for each instruction. Input name is used to help
	// identify elements of different cells
	private Panel instructionPanelCell(String name) {
		Panel instructionCell = new Panel(new GridBagLayout());
		instructionCell.setBackground(new Color(220,220,220));
		
		// First, what symbol is to be written in the tape cell
		Label writingSymbolLabel = new Label("Writing symbol");
		Choice writingChoices = new Choice();
		writingChoices.setName(name + "-0");
		writingChoices.addItemListener(new UpdatedInstruction());
		fillWritingChoices(writingChoices, alphabetField.getText().split(","));
		
		// Second, what is the next state
		Label nextStateLabel = new Label("Next State");
		Choice stateChoices = new Choice();
		stateChoices.setName(name + "-1");
		stateChoices.addItemListener(new UpdatedInstruction());
		
		String text2 = statesField.getText().strip();
		if(isNumeric(text2)) {
			 fillStateChoices(stateChoices, Integer.parseInt(text2) );
		}
		
		// Finally, the move direction
		Label moveDirectionLabel = new Label("Move direction");
		Panel directionsPanel = new Panel(new GridLayout(1,4));
		CheckboxGroup group = new CheckboxGroup();
		Label leftLabel = new Label("<--");
		Label rightLabel = new Label("-->");
		Checkbox left = new Checkbox("",group, false);
		Checkbox right = new Checkbox("", group, false);
		left.setName(name + "-2");
		right.setName(name + "-2");
		left.addItemListener(new UpdatedInstruction());
		right.addItemListener(new UpdatedInstruction());
		directionsPanel.add(leftLabel);
		directionsPanel.add(left);
		directionsPanel.add(right);
		directionsPanel.add(rightLabel);
		
		// Add the components to the panel, with constraints placing them in 
		// proper places and setting enough padding between elements
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(2,2,2,2);
		constraints.gridx = 0;
		constraints.gridy = 0;
		instructionCell.add(writingSymbolLabel, constraints);
		constraints.gridy = 1;
		instructionCell.add(writingChoices, constraints);
		constraints.gridy = 2;
		instructionCell.add(nextStateLabel, constraints);
		constraints.gridy = 3;
		instructionCell.add(stateChoices, constraints);
		constraints.gridy = 4;
		instructionCell.add(moveDirectionLabel, constraints);
		constraints.gridy = 5;
		instructionCell.add(directionsPanel, constraints);
		
		return instructionCell;
	}
	
	// Lists the options for the writing symbol in an instructions cell
	private void fillWritingChoices(Choice choice, String arr[]) {
		choice.add("");
		for(String item : arr) {
			choice.add(item.strip());
		}
	}
	
	// Lists the options for the next state in an instructions cell
	private void fillStateChoices(Choice choice, int number) {
		choice.add("");
		choice.add("-1"); // Halting state, is always the option
		
		for(int i = 1; i <= number; i++) {
			choice.add(Integer.toString(i));
		}
	}
	
	// Checks if a String can be safely converted into an integer
	private static boolean isNumeric(String entry) {
		try {
			Integer.parseInt(entry);
		} catch(NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	// Checks if all the instructions were entered, i.e. the machine is valid
	private boolean allInstructionsEntered() {
		boolean allEntered = true;
		boolean allChecked = false;
		int i = 0;
		int j = 0;
		int k = 0;
		int maxI = instructionsEntered.length;
		int maxJ = instructionsEntered[0].length;
		int maxK = instructionsEntered[0][0].length;
		
		// like a forEach, but doesn't continue iteration if any value found is false.
		while(allEntered && !allChecked) {
			if( !instructionsEntered[i][j][k] ) {
				allEntered = false;
			} else {
				k++;
				if(k == maxK) {
					k = 0;
					j++;
					if(j == maxJ) {
						j = 0;
						i++;
						if(i == maxI) {
							allChecked = true;
						}
					}
				}
			}
		}
		return allEntered;
	}
	
	// What is done when a change in one of the multiple choice elements in the instructions panel is detected
	private class UpdatedInstruction implements ItemListener {
		
		// This is ugly, but I'm too tired to figure out a better solution
		public void itemStateChanged(ItemEvent evt) {
			String string = evt.getSource().toString().strip();
			String[] temp = string.split("\\[")[1].split(",")[0].split("-");
			int[] indices = {Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])};
			
			 // If Choice entered a blank, then the length of "parts" is just 1, as the other side of "=" would be empty
			String[] parts = string.split("\\]")[0].split("=");
			if ((indices[2] == 0 || indices[2] == 1) && parts.length == 1) {
				instructionsEntered[indices[0]][indices[1]][indices[2]] = false;
			} else {
				instructionsEntered[indices[0]][indices[1]][indices[2]] = true;
			}
			
			if (allInstructionsEntered()) {
				writeButton.enable();
			} else {
				writeButton.disable();
			}
		}
	}
	
	// What is done when the update button is pressed: check if can create the instruction
	// management area. If not, notify user. Otherwise, update the field.
	private class UpdateButtonPressed implements ActionListener {
		@Override
	    public void actionPerformed(ActionEvent evt) {
			boolean erroneousStates = !isNumeric(statesField.getText());
			boolean erroneousAlphabet = alphabetField.getText().strip().isBlank();
			
			if(erroneousStates && erroneousAlphabet) {
				errorLabel.setText("Please enter valid values to the fields.");
			} else if (erroneousStates) {
				errorLabel.setText("Please enter only an integer to the number of states field.");
			} else if (erroneousAlphabet) {
				errorLabel.setText("Please enter something in the alphabet field.");
			} else {
				errorLabel.setText("");
				writeButton.disable();
				instructionsPane.removeAll(); // First, clear the instructionsPane of daughter elements as they might be outdated
		        instructionsPane.add(createInstructionsPanel());
		        instructionsPane.validate(); // Automatically refreshes the graphics
			}
	    }
	}
	
	
	// What is done when the write button is pressed.
	private class WriteButtonPressed implements ActionListener {
		@Override
	    public void actionPerformed(ActionEvent evt) {
			if (allInstructionsEntered()) {
				// First, figure the file path of the jar file and thus the directory to be written in
				File jarFile = new File(tm_gui.class.getProtectionDomain().getCodeSource().getLocation().getPath());
				String path = jarFile.getParentFile().getAbsolutePath().replaceAll("%20", " ");
				writeToFile(path);
			} else {
				// Give this in case of an very unexpected situation
				errorLabel.setText("Something went wrong: writeButton is enabled despite not all instructions entered");
			}
		}
	}
	
	// Write to the text file, according to the instructions the user has given
	private void writeToFile(String filePath) {		
		try {
			String completePath = filePath + "\\" + nameField.getText() + ".txt";
			File myObj = new File(completePath);
			myObj.createNewFile(); // We do not care if the file already exists - it will be replaced if necessary
			PrintWriter writer = new PrintWriter(completePath);
			writer.println("# Each row a different instruction");
			writer.println("# Format: CURRENT_STATE (integer), TAPE_SYMBOL (any string but comma), NEXT_STATE (integer), WRITE_SYMBOL (any string but comma), MOVE_TAPE_DIR (0 = right, 1 = left)");
			
			String[] alphabet = alphabetField.getText().split(",");
			int width = alphabet.length;
			int height = Integer.parseInt(statesField.getText().strip());
			
			for(int i = 1; i <= height; i++) {
		    	for(int j = 1; j <= width; j++) {
		    		Panel instructionsPanel = (Panel)instructionsPane.getComponent(0);
		    		Panel cell = (Panel)instructionsPanel.getComponent(j + i * (width+1)); // One of the instruction-containing cells, of type Panel
		    		
		    		Choice writingSymbolChoice = (Choice)cell.getComponent(1);
		    		Choice nextStateChoice = (Choice)cell.getComponent(3);
		    		Panel directionsPanel = (Panel)cell.getComponent(5);
		    		Checkbox left = (Checkbox)directionsPanel.getComponent(1);
		    		Checkbox right = (Checkbox)directionsPanel.getComponent(2);
		    		
		    		int dir = -1; // by not having one of the values by default, this helps detect bugs as something will go wrong in the Minecraft end of the project
		    		if(left.getState()) {
		    			dir = 1;
		    		} else if (right.getState()) {
		    			dir = 0;
		    		}
		    		
		    		writer.println(i + "," + alphabet[j-1] + "," + nextStateChoice.getSelectedItem() + "," + writingSymbolChoice.getSelectedItem() + "," + dir);
		    	}
			}
			
			writer.close();
			
			
		    } catch (IOException e) {
		    	System.out.println("An error occurred while writing to a text file.");
		    	e.printStackTrace();
		    }
		    
	}

	public static void main(String[] args) {
	      SwingUtilities.invokeLater(new Runnable() {
	    	  
	         @Override
	         public void run() {
	            new tm_gui();
	         }
	      });

	}

}
