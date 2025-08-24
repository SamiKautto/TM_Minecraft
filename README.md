This programming project is about generating Turing machines in Minecraft Java Edition.

Briefly, how it works is that the server plugin reads text files that contain Turing machine instructions and converts the instructions into a Turing machine made of Minecraft elements (Command Blocks and other blocks and entities). A graphical user interface (GUI) is provided to make the generation of valid sets of instructions more convenient.

Files:
- Lookup Table.txt contains a lookup table, used to convert the Turing machine alphabet into Minecraft blocks. The user is expected to edit this as necessary, this is here to give a baseline.
- TM_GUI-0.0.1-SNAPSHOT.jar launches the GUI which makes it more convenient to create valid Turing machine instructions. These are created as text files, which the Minecraft plugin uses for machine generation in-game.
- Turing_machine-0.0.1-SNAPSHOT.jar is the server plugin for Minecraft Bukkit servers.
- User Instructions V1.txt contains user instructions.
- plugin.yml lets the plugin know what new console commands to include. Only relevant for programmers.
- pom.xml provides to the IDE necessary information, including the location where it can find Bukkit repository. Only relevant for programmers.
- All the .java files are the source code of the project. Only relevant for programmers.


Bugs:
None known currently, but their existence can't be ruled out.


This project is not associated with Mojang or Microsoft. 
