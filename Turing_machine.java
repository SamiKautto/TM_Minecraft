package me.turing.machine.turing_machine;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.Switch.Face; // Ignore this warning, this is necessary
import org.bukkit.block.data.Lightable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



@SuppressWarnings("deprecation")
public final class Turing_machine extends JavaPlugin{
	ArrayList<Tuple<Integer,String,Integer,String,Integer>> masterInstructions = null;
	Map<String, String> masterLookupTable = null;
	int moveLimit = 1000; // How many blocks are moved in a single tape move command? (For each direction, so the real number is double this)

    @Override
    public void onEnable() {
    	getLogger().info("onEnable has been invoked!");
    }
    
    @Override
    public void onDisable() {
    	getLogger().info("onDisable has been invoked!");
    }
     
    
    //-----------------------
    // Generates / deletes the scoreboards
    private void generateScoreboards(ArrayList<Tuple<Integer,String,Integer,String,Integer>> instructions) {
    	for(int i = 0; i < instructions.size(); i++) {
    		String s = "s" + instructions.get(i).a + "i" + instructions.get(i).b;
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard objectives add " + s + " dummy"); // sets up the scoreboard
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set @e[tag=scoreHolder,limit=1] " + s + " 0"); // sets the value to 0
    	}
    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scoreboard objectives add state dummy");
    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scoreboard players set @e[tag=scoreHolder,limit=1] state 1");
    }
    
    private void deleteScoreboards(ArrayList<Tuple<Integer,String,Integer,String,Integer>> instructions) {
    	for(int i = 0; i < instructions.size(); i++) {
    		String s = "s" + instructions.get(i).a + "i" + instructions.get(i).b;
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scoreboard objectives remove " + s);
    	}
    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scoreboard objectives remove state");
    }
    
    
    //-----------------------
    // Converts an integer into a command to move the tape, with 0 = move right and 1 = move left
    private String moveTape(int i) {
    	return moveTape(i, 0, -15);
    }
    
    private String moveTape(int i, int tapeX, int tapeZ) {
    	if (i == 0) {
    		return "clone " + (tapeX - moveLimit) + " -60 " + tapeZ + " " + (tapeX + moveLimit) + " -60 " + tapeZ + " " + (tapeX - moveLimit + 1) + " -60 " + tapeZ + " replace move";
    	} else if (i == 1) {
    		return "clone " + (tapeX - moveLimit) + " -60 " + tapeZ + " " + (tapeX + moveLimit) + " -60 " + tapeZ + " " + (tapeX - moveLimit - 1) + " -60 " + tapeZ + " replace move";
    	}
    	
    	return "";
    }
    
    
    // --------------------
    // Automates sending messages to the server
    
    private void message(World world, String message) {
    	for (Player p : world.getPlayers())
        {
        	p.sendMessage(message);
        }
    }
    
    
    // ----------------------
    // Automates placing blocks
    
    // Orientates the given block based on the given orientation
    private void orientateBlock(Block block, BlockFace facing) {
    	if (facing != null) {
	    	BlockData data = block.getBlockData();
	    	
	    	if (data instanceof Directional) {
	    		Directional directional = (Directional) data;
	            directional.setFacing(facing);
	            block.setBlockData(directional);
	    	} else if (data instanceof Rotatable) {
	    		Rotatable rotatable = (Rotatable) data;
	        	rotatable.setRotation(facing);
	        	block.setBlockData(rotatable);
	    	}
    	}
    }
    
    private void placeBlock(World world, int x, int y, int z, Material mat) {
    	placeBlock(world, x, y, z, mat, null, null);
    }
    
    private void placeBlock(World world, int x, int y, int z, Material mat, BlockFace facing) {
    	placeBlock(world, x, y, z, mat, facing, null);
    }
    
    private void placeBlock(World world, int x, int y, int z, Material mat, BlockFace facing, String arg) {
    	Block block = world.getBlockAt(x, y, z);
    	block.setType(mat);
    	orientateBlock(block, facing);
    	
    	// perform various extra tasks based on the nature of the block and the argument
    	if (arg != null) {
    		BlockState state = block.getState();
    		BlockData data = block.getBlockData();
    		
    		//inserts command into an command block
    		if (state instanceof CommandBlock) {
	    		CommandBlock commandBlock = (CommandBlock) state;
	            commandBlock.setCommand(arg);
	            commandBlock.update();
    		} 
    		
    		//inserts text into  a sign
    		if (state instanceof Sign) {
    			Sign sign = (Sign) block.getState();
    			
    			int len = 15;
    			for(int i = 0; i <= 3; i++) {
    				sign.setLine(i, arg.substring(Math.min(i*len, arg.length()), Math.min((i+1)*len, arg.length())) );
    			}
    			sign.update();
    		}
    		
    		// turns redstone torches off, they generate on by default
    		if (data instanceof Lightable && arg == "off") {
    			Lightable lightable = (Lightable) data;
    			lightable.setLit(false);
    			block.setBlockData(lightable);
    		}
    	}
    	
    }
    
    // A special method for placing buttons
	private void placeButton(World world, int x, int y, int z, Material mat, BlockFace facing, Face face) { // Ignore
    	Block block = world.getBlockAt(x, y, z);
    	block.setType(mat);
    	orientateBlock(block, facing);
    	BlockData data = block.getBlockData();
    	
	    if (data instanceof Switch) {
			Switch sw = (Switch) data;
			sw.setFace(face); // Ignore
			block.setBlockData(sw);
		}
    }
    
    
    
    //---------------------------------------------
    // Responsible for generating the component that starts the looping of the Turing Machine (hence the name),
    // as well as necessary actions at the start of the loop, most importantly extending the tape if it runs out.

    private void generateLooper(World world, Map<String,String> lookupTable, ArrayList<Tuple<Integer,String,Integer,String,Integer>> instructions, String blank) {
    	generateLooper(world, 10, -5, 0, -15, lookupTable, instructions, blank);
    }
    
    private void generateLooper(World world, int startingX, int startingZ, int tapeX, int tapeZ, Map<String,String> lookupTable, ArrayList<Tuple<Integer,String,Integer,String,Integer>> instructions, String blank) {
    	
        message(world, "Generating Looper");
        
    	placeBlock(world, startingX,    -60, startingZ +1, Material.REPEATER, BlockFace.NORTH);
    	placeBlock(world, startingX +1, -60, startingZ +3, Material.REPEATER, BlockFace.WEST);
    	placeBlock(world, startingX,    -60, startingZ +4, Material.REPEATER, BlockFace.NORTH);
    	placeBlock(world, startingX,    -60, startingZ +3, Material.REDSTONE_WIRE);
    	placeBlock(world, startingX,    -60, startingZ +2, Material.COMMAND_BLOCK, BlockFace.UP, "execute if block " + tapeX + " -60 " + tapeZ + " minecraft:air run setblock " + tapeX + " -60 " + tapeZ + " " + lookupTable.get(blank) + " replace");
    	placeBlock(world, startingX +2, -60, startingZ +3, Material.COMMAND_BLOCK, BlockFace.UP, "setblock " + startingX + " -60 " + startingZ + " minecraft:air replace");
    	placeBlock(world, startingX -1, -60, startingZ +2, Material.BIRCH_SIGN, BlockFace.WEST, "                 Tape extender");
    	
    }
    
    
    //----------------------------------------
    // Responsible for generating the flags, which are used to check which combination
    // of input and state is currently in effect
    
    private void generateSemaphores(World world, ArrayList<Tuple<Integer,String,Integer,String,Integer>> instructions, Map<String,String> lookupTable) {
    	generateSemaphores(world, 10, -5, 0, -15, instructions, lookupTable);
    }
    
    private void generateSemaphores(World world, int startingX, int startingZ, int tapeX, int tapeZ, ArrayList<Tuple<Integer,String,Integer,String,Integer>> instructions, Map<String,String> lookupTable) {
    	
    	message(world, "Generating semaphores");
    	
    	placeBlock(world, startingX -1, -59, startingZ +8, Material.REDSTONE_TORCH);
    	placeBlock(world, startingX -1, -60, startingZ +8, Material.WHITE_WOOL);
    	
    	int instructionsLength = instructions.size();
    	//int totalOffset = instructionsLength / 15;
    	int offset = 0;
    	
    	for (int i = 0; i < instructionsLength; i++) {
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "forceload add " + (startingX + i + offset) + " " + (startingZ +8));
    		
    		if (i > 0 && i%15 == 0) {
    			placeBlock(world, startingX + i + offset, -60, startingZ +5, Material.WHITE_WOOL);
    			placeBlock(world, startingX + i + offset, -59, startingZ +5, Material.REPEATER, BlockFace.WEST);
    			placeBlock(world, startingX + i + offset, -60, startingZ +8, Material.WHITE_WOOL);
    			placeBlock(world, startingX + i + offset, -59, startingZ +8, Material.REPEATER, BlockFace.WEST);
    			offset++;
    		}
    		
       		placeBlock(world, startingX + i + offset, -60, startingZ +5, Material.COMMAND_BLOCK, BlockFace.SOUTH, "execute if block " + tapeX + " -60 " + tapeZ + " " + lookupTable.get(instructions.get(i).b) + " if score @e[tag=scoreHolder,limit=1] state matches " + instructions.get(i).a + " run scoreboard players set @e[tag=scoreHolder,limit=1] s" + instructions.get(i).a + "i" + instructions.get(i).b + " 1");
    		placeBlock(world, startingX + i + offset, -59, startingZ +5, Material.REDSTONE_WIRE);
    		
    		placeBlock(world, startingX + i + offset, -60, startingZ +8, Material.WHITE_WOOL);
    		
    		//forced to use a console commands to generate the chain (green) command blocks, due to some import incompatibility in trying to both give the command block a command, and trying to turn on the 'conditional' setting.
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "setblock " + (startingX + i + offset) + " -60 " + (startingZ +6) + " chain_command_block[facing=south]{Command:\"/setblock ~ -59 ~2 minecraft:redstone_wire replace\",auto:1b} replace");
    	}
    	
    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "forceload add " + (startingX + instructionsLength + offset -1) + " " + (startingZ +9));
    	placeBlock(world, startingX + instructionsLength + offset -1, -60, startingZ +9, Material.WHITE_WOOL);
    	placeBlock(world, startingX + instructionsLength + offset -1, -59, startingZ +9, Material.REPEATER, BlockFace.NORTH);
    	placeBlock(world, startingX + instructionsLength + offset -1, -60, startingZ +10, Material.COMMAND_BLOCK, BlockFace.SOUTH, "fill ~ -59 ~-2 " + (startingX) + " -59 ~-2 minecraft:air replace minecraft:redstone_wire");
    	placeBlock(world, startingX + instructionsLength + offset -1, -59, startingZ +10, Material.REDSTONE_WIRE);
    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "setblock " + (startingX + instructionsLength + offset -1) + " -60 " + (startingZ +11) + " chain_command_block[facing=south]{Command:\"/setblock ~ -59 ~2 minecraft:redstone_block replace\",auto:1b} replace");
    	
    	placeBlock(world, startingX-1, -60, startingZ +5, Material.BIRCH_SIGN, BlockFace.WEST, "                   Semaphores");
    	placeBlock(world, startingX-1, -60, startingZ +6, Material.BIRCH_SIGN, BlockFace.WEST, "                   Continue");
    }
    
    //---------------------------
    // Generates the part that actually executes the instructions
    
    private void generateExecution(World world, ArrayList<Tuple<Integer,String,Integer,String,Integer>> instructions, Map<String,String> lookupTable) {
    	generateExecution(world, 10, -5, 0, -15, instructions, lookupTable);
    }
    
    private void generateExecution(World world, int startingX, int startingZ, int tapeX, int tapeZ, ArrayList<Tuple<Integer,String,Integer,String,Integer>> instructions, Map<String,String> lookupTable) {
		message(world, "Generating instruction executor");
		
		int offset = (instructions.size() / 15)-1;
		if (instructions.size()%15 == 0) {
			offset--;
		}
		
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "forceload add " + (startingX + instructions.size() + offset + 1) + " " + (startingZ +14));
		placeBlock(world, startingX + instructions.size() + offset + 1, -60, startingZ +14, Material.COMMAND_BLOCK, BlockFace.UP, "setblock ~-1 ~1 ~-1 minecraft:air replace");
		placeBlock(world, startingX + instructions.size() + offset + 1, -59, startingZ +14, Material.REDSTONE_WIRE);
		
		// Order is reversed to make sure the flag setter and the corresponding execution have
		// the same x-coordinate, just for convenience of debugging and testing
		for (int i = instructions.size(); i > 0; i--) {
    		
    		if (i < instructions.size() && i%15 == 0) {
    			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "forceload add " + (startingX + i + offset) + " " + (startingZ +14));
    			placeBlock(world, startingX + i + offset, -60, startingZ +14, Material.WHITE_WOOL);
     			placeBlock(world, startingX + i + offset, -59, startingZ +14, Material.REPEATER, BlockFace.EAST);
     			offset--;
    		}
    		 
     		
     		String s = "s" + instructions.get(i-1).a + "i" + instructions.get(i-1).b;
     		placeBlock(world, startingX + i + offset, -60, startingZ +14, Material.COMMAND_BLOCK, BlockFace.SOUTH, "execute if score @e[tag=scoreHolder,limit=1] " + s + " matches 1 run scoreboard players set @e[tag=scoreHolder,limit=1] " + s + " 0");
     		placeBlock(world, startingX + i + offset, -59, startingZ +14, Material.REDSTONE_WIRE);
     		
     		//forced to use a console commands to generate the green command blocks, due to some import incompatibility in trying to both give the command block a command, and trying to turn on the 'conditional' setting.
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "setblock " + (startingX + i + offset) + " -60 " + (startingZ +15) + " chain_command_block[conditional=true, facing=south]{Command:\"/setblock " + tapeX + " -60 " + tapeZ + " minecraft:air replace\",auto:1b} replace");
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "setblock " + (startingX + i + offset) + " -60 " + (startingZ +16) + " chain_command_block[conditional=true, facing=south]{Command:\"/setblock " + tapeX + " -60 " + tapeZ + " " + lookupTable.get(instructions.get(i-1).d) + " replace\",auto:1b} replace");
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "setblock " + (startingX + i + offset) + " -60 " + (startingZ +17) + " chain_command_block[conditional=true, facing=south]{Command:\"/scoreboard players set @e[tag=scoreHolder,limit=1] state " + instructions.get(i-1).c + "\",auto:1b} replace");
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "setblock " + (startingX + i + offset) + " -60 " + (startingZ +18) + " chain_command_block[conditional=true, facing=south]{Command:\"/" + moveTape(instructions.get(i-1).e) + "\",auto:1b} replace");
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "setblock " + (startingX + i + offset) + " -60 " + (startingZ +19) + " chain_command_block[conditional=true, facing=south]{Command:\"/execute unless score @e[tag=scoreHolder,limit=1] state matches -1 run setblock " + (startingX) + " -60 " + (startingZ) + " minecraft:redstone_block replace\",auto:1b} replace");
    	 }
		
    	placeBlock(world, startingX-1, -60, startingZ +14, Material.BIRCH_SIGN, BlockFace.WEST, "                    Reset       Semaphores");
    	placeBlock(world, startingX-1, -60, startingZ +15, Material.BIRCH_SIGN, BlockFace.WEST, "                 Clear Writing   Position");
    	placeBlock(world, startingX-1, -60, startingZ +16, Material.BIRCH_SIGN, BlockFace.WEST, "                 Write");
    	placeBlock(world, startingX-1, -60, startingZ +17, Material.BIRCH_SIGN, BlockFace.WEST, "                  Set Next     State");
    	placeBlock(world, startingX-1, -60, startingZ +18, Material.BIRCH_SIGN, BlockFace.WEST, "                 Move Tape");
    	placeBlock(world, startingX-1, -60, startingZ +19, Material.BIRCH_SIGN, BlockFace.WEST, "                   Continue     Cycle?");
    	 
    }
    
    
    //----------------------------
    // Generates the user controls
    
    private void generateControls(World world) {
    	generateControls(world, 10, -5, 0, -15);
    }
    
    private void generateControls(World world, int startingX, int startingZ, int tapeX, int tapeZ) {
    	message(world, "Generating user controls");
    	
    	// First, mark the reading head
    	for (int z = tapeZ -5; z <= tapeZ +5; z++) {
     		placeBlock(world, tapeX, -61, z, Material.RED_CONCRETE);
    	}
    	placeBlock(world, tapeX + 1, -61, tapeZ, Material.RED_CONCRETE);
    	placeBlock(world, tapeX - 1, -61, tapeZ, Material.RED_CONCRETE);
    	
    	
    	// Next, set up controls
    	
    	// Start button
    	placeBlock(world, 0, -60, -2, Material.IRON_BLOCK);
    	placeButton(world, 0, -59, -2, Material.DARK_OAK_BUTTON, BlockFace.SOUTH, Face.FLOOR); // Ignore
    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "summon minecraft:armor_stand 0 -59 -3 {Invisible:true,Invulnerable:true,NoBasePlate:true,NoGravity:true,CustomNameVisible:true, CustomName:\"Press the button to start / continue execution\"}");
    	placeBlock(world, 0, -62, -2, Material.COMMAND_BLOCK, BlockFace.UP, "setblock " + startingX + " -60 " + (startingZ + 1) + " minecraft:repeater[facing=north] replace");
    	placeBlock(world, 0, -61, -2, Material.REDSTONE_WIRE);
    	placeBlock(world, 0, -62, -3, Material.COMMAND_BLOCK, BlockFace.UP, "setblock " + startingX + " -60 " + startingZ + " minecraft:air replace");
    	placeBlock(world, 0, -61, -3, Material.REDSTONE_WIRE);
    	placeBlock(world, 0, -62, -4, Material.COMMAND_BLOCK, BlockFace.UP, "setblock " + startingX + " -60 " + startingZ + " minecraft:redstone_block replace");
    	placeBlock(world, 0, -61, -4, Material.REDSTONE_WIRE);
    	
    	// Reset button
    	placeBlock(world, -8, -60, 1, Material.IRON_BLOCK);
    	placeButton(world, -8, -59, 1, Material.DARK_OAK_BUTTON, BlockFace.SOUTH, Face.FLOOR); // Ignore
    	placeBlock(world, -8, -62, 1, Material.COMMAND_BLOCK, BlockFace.UP, "scoreboard players set @e[tag=scoreHolder,limit=1] state 1");
    	placeBlock(world, -8, -61, 1, Material.REDSTONE_WIRE);
    	placeBlock(world, -9, -62, 1, Material.COMMAND_BLOCK, BlockFace.UP, "fill " + (tapeX - moveLimit) + " -60 " + tapeZ + " " + (tapeX + moveLimit) + " -60 " + tapeZ + " minecraft:air");
    	placeBlock(world, -9, -61, 1, Material.REDSTONE_WIRE);
    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "summon minecraft:armor_stand -9 -59 1 {Invisible:true,Invulnerable:true,NoBasePlate:true,NoGravity:true,CustomNameVisible:true, CustomName:\"Reset\"}");
    	
    	// Pause button
    	placeBlock(world, -8, -60, 3, Material.IRON_BLOCK);
    	placeButton(world, -8,-59, 3, Material.DARK_OAK_BUTTON, BlockFace.SOUTH, Face.FLOOR); //Ignore
    	placeBlock(world, -8, -62, 3, Material.COMMAND_BLOCK, BlockFace.UP, "setblock " + startingX + " -60 " + (startingZ + 1) + " minecraft:air replace");
    	placeBlock(world, -8, -61, 3, Material.REDSTONE_WIRE);
    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "summon minecraft:armor_stand -9 -59 3 {Invisible:true,Invulnerable:true,NoBasePlate:true,NoGravity:true,CustomNameVisible:true, CustomName:\"Pause\"}");

    }
    
    //-----------------------------
    // What is done when certain custom commands are given?
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	
    	// Flattens the work area, so that the work area has no obstructions
    	if (cmd.getName().equalsIgnoreCase("initialize")) {
    		World world = Bukkit.getWorld("world");
    		
    		message(world, "Clearing the work area, please wait...");
    		
    		// First, increase the limit for how many blocks can be manipulated with commands, default (32768) is inconveniently small for this kind of project
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "gamerule commandModificationBlockLimit " + max(moveLimit *2, 100000000));
    		
    		// Clear Work Area
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "forceload add -50 -50 50 50");
    		for (int x = -50; x <= 50; x++) {
    			for(int z = -50; z <= 50; z++) {
    				for (int y = -50; y >= -63; y--) {
    					Block block = world.getBlockAt(x, y, z);
    					if (y <= -61) {
    						block.setType(Material.SMOOTH_STONE);
    					} else {
    						block.setType(Material.AIR);
    					}
    				}
    			}
    		}  		
    		
    		// Force load more area for the tape
    		for (int i = 0; i <= moveLimit + 16; i = i +16) {
    			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "forceload add " + i + " -15");
        		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "forceload add -" + i + " -15");
    		}  
    		
    		message(world, "Flattening finished, teleporting player");
    		// Teleport player to (0,0)
    		for (Player p : world.getPlayers())
            {
    			Location loc = new Location(world, 0.0,-58.0,0.0);
            	p.teleport(loc);
            }
    		
    		return true;

    	} else {
    		
    		// Clears the Turing Machine as well as the scoreboards
    		if (cmd.getName().equalsIgnoreCase("clearturing")) {
    	
	    		World world = Bukkit.getWorld("world");
	    		
	    		// Fill in instructions
	    		if (args != null && args.length > 0) { // Will be done every time when argument is given
	    			masterInstructions = Reader.read(".\\plugins\\" + args[0] + ".txt");
	    		} else if (masterInstructions == null) { // instructions empty but no argument == user mistake
	    			message(world, "Please specify which Turing machine instructions are in use, for this is needed for scoreboard removal. This will be remembered in further use.");
	    			return true;
	    		}
	    		
	    		message(world, "Clearing the Turing Machine, please wait...");
	    		int tapeX = 0;
	    		int tapeZ = -15;
	    		int startingZ = -5;
	    		
	    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "fill " + (tapeX - moveLimit) + " -60 " + tapeZ + " " + (tapeX + moveLimit) + " -60 " + tapeZ + " minecraft:air");
	    		
	    		for (int x = -50; x <= 50; x++) {
	    			for(int y = -50; y >= -63; y--) {
	    				for (int z = -50; z <= 50; z++) {
	    					Block block = world.getBlockAt(x, y, z);
	    					if (y <= -61) {
	    						block.setType(Material.SMOOTH_STONE);
	    					} else {
	    						block.setType(Material.AIR);
	    					}
	    				}
	    			}
	    		}
	    		
	    		for (int x = 51; x <= (masterInstructions.size() * 1.2); x++) {
	    			for (int y = -60; y <= -59; y++) {
	    				for (int z = startingZ +5; z <= (startingZ +19); z++) {
	    					Block block = world.getBlockAt(x, y, z);
	    					block.setType(Material.AIR);
	    				}
	    			}
	    		}
	    		
	    		// Clears the environment of items, armor stands and other entities, except for players
	    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"kill @e[type=!player]");
	    		deleteScoreboards(masterInstructions);
	    		//deleteScoreboards(testInstructions.instructions(2));
	    		message(world, "Finished clearing the Turing Machine");
	    		
	    		return true;
	    		
	    		
	    		//Generates the Turing Machine
	    	} else if (cmd.getName().equalsIgnoreCase("generateturing")) {
	    		
	    		World world = Bukkit.getWorld("world");
	    		String blank = "";
	    		
	    		// Fill in instructions
	    		if (args != null && args.length > 0) { // Will be done every time when argument is given
	    			masterInstructions = Reader.read(".\\plugins\\" + args[0] + ".txt");
	    			blank = Reader.extractBlank(".\\plugins\\" + args[0] + ".txt");
	    		} else if (masterInstructions == null) { // instructions empty but no argument = user mistake
	    			message(world, "Please specify which Turing machine instructions to use. This will be remembered in further use.");
	    			return true;
	    		}
	    		
	    		masterLookupTable = LookupTable.createTable(".\\plugins\\Lookup Table.txt");
	    		
	    			    		
	    		if (masterInstructions != null && masterLookupTable != null) {
		            message(world, "Generating the Turing machine, please wait...");
		            
		            // This armorstand holds the scoreboards, so that they don't need to be
		            // assigned to the user, and thus require the user to enter their username
		            // -> user agnostic machine
		            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "summon minecraft:armor_stand 0 7 0 {Invisible:true,NoBasePlate:true,NoGravity:true,Tags:[\"scoreHolder\"],CustomName:\"Score Holder\"}");
		            
		            generateLooper(world, masterLookupTable, masterInstructions, blank);
		            generateSemaphores(world, masterInstructions, masterLookupTable);
		            generateExecution(world, masterInstructions, masterLookupTable);
		            generateScoreboards(masterInstructions);
		            generateControls(world);
		            
		            message(world, "Finished generating the Turing machine");
	    		} else {
	    			message(world, "An error happened while reading files, can't generate");
	    		}
	    		
	    		return true;
	    	}
    	}
    	
    	return false; 
    }

	private int max(int a, int b) {
		if (a > b) {
			return a;
		} else {
			return b;
		}
	}


}
