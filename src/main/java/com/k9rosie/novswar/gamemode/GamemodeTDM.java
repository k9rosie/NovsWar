package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.game.Game;

public class GamemodeTDM implements Gamemode {

	private int gameTime = 60;
	private int maxScore = 4;
	private String gamemodeName = "Team Deathmatch";
	
	@Override
	public int getGameTime() {
		return gameTime;
	}

	@Override
	public int getDeathTime() {
		return 5;
	}
	
	@Override
	public int getMaxScore() {
    	return maxScore;
    }

	@Override
	public String getGamemodeName() {
		return gamemodeName;
	}

	@Override
	public void hook(Game game) {
		//Team Deathmatch
		
		
		
	}

}
