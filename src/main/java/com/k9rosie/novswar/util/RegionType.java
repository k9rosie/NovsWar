package com.k9rosie.novswar.util;

public enum RegionType {
    SPAWN_GATE,
    INTERMISSION_GATE;

    public static RegionType parseString(String regionType) {
        switch (regionType.toLowerCase()) {
            case "intermission_gate": return INTERMISSION_GATE;
            case "spawn_gate": return SPAWN_GATE;
            default: return INTERMISSION_GATE;
        }
    }
}
