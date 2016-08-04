package com.k9rosie.novswar.command.admin;


public enum AdminCommandType {
	HELP		("",					"Display the admin help message"),
	KICK		("<player>",			"Kicks player from the round"),
	NEXTGAME	("[bukkitWorld]",		"Force a new round on bukkitWorld"),
	RESTART		("",					"Force a round restart on the same map"),
	SETREGION	("<name> <RegionType>",	"Creates a new region"),
	SETSPAWN	("<team>",				"Sets the spawn location for team"),
	SETTEAM		("<player> <team>",		"Forces the player to join team");
	
	private final String arguments;
	private final String description;
	AdminCommandType(String arguments, String description) {
		this.arguments = arguments;
		this.description = description;
	}
	
	public String arguments() {
		return arguments;
	}
	
	public String description(){
		return description;
	}
	
	/**
	 * Gets a AdminCommandType enum given a string command
	 * @param command - The string name of the command
	 * @return AdminCommandType - Corresponding enum
	 */
	public static AdminCommandType getCommand(String command) {
		AdminCommandType result = HELP;
		for(AdminCommandType cmd : AdminCommandType.values()) {
			if(cmd.toString().equalsIgnoreCase(command)) {
				result = cmd;
			}
		}
		return result;
	}
	
	/**
	 * Determines whether a string command is a AdminCommandType
	 * @param command - The string name of the command
	 * @return Boolean - True if the string is a command, false otherwise
	 */
	public static boolean contains(String command) {
		boolean result = false;
		for(AdminCommandType cmd : AdminCommandType.values()) {
			if(cmd.toString().equalsIgnoreCase(command)) {
				result = true;
			}
		}
		return result;
	}
}
