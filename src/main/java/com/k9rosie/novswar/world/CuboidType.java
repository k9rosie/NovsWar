package com.k9rosie.novswar.world;

public enum CuboidType {
    BATTLEFIELD,
    INTERMISSION_GATE,
    DEATH_REGION,
    OBJECTIVE,
    TEAM_SPAWN;

    public static CuboidType parseString(String regionType) {
        switch (regionType.toLowerCase()) {
            case "battlefield": return CuboidType.BATTLEFIELD;
            case "intermission_gate": return CuboidType.INTERMISSION_GATE;
            case "death_region": return CuboidType.DEATH_REGION;
            case "objective": return CuboidType.OBJECTIVE;
            case "team_spawn": return CuboidType.TEAM_SPAWN;
        }
        return null;
    }
}
