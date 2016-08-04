package com.k9rosie.novswar.command;

public enum CommandType {
	HELP		("novswar.command.help",	"",			"Display the help message"),
	JOIN		("novswar.command.join",	"",			"Join the current round"),
	LEAVE		("novswar.command.leave",	"",			"Leave the round"),
	MAP			("novswar.command.map",		"",			"Display map info"),
	PLAYER		("novswar.command.player",	"[name]",	"Display stats on yourself or a player"),
	STATS		("novswar.command.player",	"[name]",	"Alias to /nw player [name]"),
	SPECTATE	("novswar.command.spectate","",			"Enter spectator mode"),
	TEAM		("novswar.command.team", 	"[player | team]", "Display team info"),
	VOTE		("novswar.command.vote",	"",			"Prompt the voting screen"),
	ADMIN		("novswar.command.admin",	"",			"For Admins only");
	
	private final String permission;
	private final String arguments;
	private final String description;
	CommandType(String permission, String arguments, String description) {
		this.permission = permission;
		this.arguments = arguments;
		this.description = description;
	}
	
	public String permission() {
		return permission;
	}
	
	public String arguments() {
		return arguments;
	}
	
	public String description(){
		return description;
	}
	
	/**
	 * Gets a CommandType enum given a string command
	 * @param command - The string name of the command
	 * @return CommandType - Corresponding enum
	 */
	public static CommandType getCommand(String command) {
		CommandType result = HELP;
		for(CommandType cmd : CommandType.values()) {
			if(cmd.toString().equalsIgnoreCase(command)) {
				result = cmd;
			}
		}
		return result;
	}
	
	/**
	 * Determines whether a string command is a CommandType
	 * @param command - The string name of the command
	 * @return Boolean - True if the string is a command, false otherwise
	 */
	public static boolean contains(String command) {
		boolean result = false;
		for(CommandType cmd : CommandType.values()) {
			if(cmd.toString().equalsIgnoreCase(command)) {
				result = true;
			}
		}
		return result;
	}
	
}
