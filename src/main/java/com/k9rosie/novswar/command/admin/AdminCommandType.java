package com.k9rosie.novswar.command.admin;


public enum AdminCommandType {
	HELP		("",					"Display the admin help message"),
	KICK		("<player>",			"Kicks player from the round"),
	NEXTGAME	("[bukkitWorld]",		"Force a new round on bukkitWorld"),
	RESTART		("",					"Force a round restart on the same map"),
	SETREGION	("<name> <CuboidType>",	"Creates a new region"),
	SETSPAWN	("<team>",				"Sets the spawn location for team"),
	SETTEAM		("<player> <team>",		"Forces the player to join team"),
	LISTREGIONS ("",					"Lists the regions in this world"),
	DELREGION	("<name>", 				"Deletes the region"),
	SAVEREGIONS ("",					"Saves all regions");
	
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
}
