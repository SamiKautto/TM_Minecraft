This programming project is about generating Turing machines in Minecraft Java Edition.

Briefly, how it works is that the server plugin reads text files that contain Turing machine instructions and converts the instructions into a Turing machine made of Minecraft elements (Command Blocks and other blocks and entities). A graphical user interface (GUI) is provided to make the generation of valid sets of instructions more convenient.

Files:
- Lookup Table.txt contains a lookup table, used to convert the Turing machine alphabet into Minecraft blocks. The user is expected to edit this as necessary, this is here to give a baseline.
- Settings.txt contains settings which affect some aspects of the machine. Currently only includes the limit to how many blocks are affected with commands.
- All other .txt files are example files of the Turing machine instructions.
- LICENSE contains the license of this project.
- TM_GUI.jar launches the GUI which makes it more convenient to create valid Turing machine instructions. These are created as text files, which the Minecraft plugin uses for machine generation in-game.
- Turing_machine.jar is the server plugin for Minecraft Bukkit servers.
- User Instructions v4.docx contains user instructions.
- plugin.yml lets the plugin know what new console commands to include. Only relevant for programmers.
- pom.xml provides to the IDE necessary information, including the location where it can find Bukkit repository. Only relevant for programmers.
- All the .java files are the source code of the project. Only relevant for programmers.


Issues:
If the user is using the software as intended, then there are none known. Though their existence can't be ruled out. There are some issues that arise from unintended use, like there being no duplicate protection when the user gives the alphabet. But as these problems arise from unintended use, solving them is a low priority.

Code is at times terrible, though. This is the result of programming while tired.


Notice:
This project is not associated with Mojang or Microsoft. 
