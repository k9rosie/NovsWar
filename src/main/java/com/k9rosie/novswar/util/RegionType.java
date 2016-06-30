package com.k9rosie.novswar.util;

public enum RegionType {
    BATTLEFIELD,
    INTERMISSION_GATE,
    DEATH_REGION;

    public static RegionType parseString(String regionType) {
        switch (regionType.toLowerCase()) {
            case "battlefield": return BATTLEFIELD;
            case "intermission_gate": return INTERMISSION_GATE;
            case "death_region": return DEATH_REGION;
        }
        return null;
    }
}
