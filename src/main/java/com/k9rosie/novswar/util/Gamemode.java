package com.k9rosie.novswar.util;

public enum Gamemode {
    CAPTURE_THE_FLAG,
    TEAM_DEATHMATCH;

    public static Gamemode parseString(String gamemode) {
        switch (gamemode.toLowerCase()) {
            case "ctf": return CAPTURE_THE_FLAG;
            case "tdm": return TEAM_DEATHMATCH;
            default: return TEAM_DEATHMATCH;
        }
    }
}
