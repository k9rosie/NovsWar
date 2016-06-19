package com.k9rosie.novswar.util;

public enum RegionType {
    SPAWN_GATE,
    INTERMISSION_GATE,
    DEATH_REGION;

    public static RegionType parseString(String regionType) {
        switch (regionType.toLowerCase()) {
            case "intermission_gate": return INTERMISSION_GATE;
            case "spawn_gate": return SPAWN_GATE;
            case "death_region": return DEATH_REGION;
        }
        return null;
    }
}
