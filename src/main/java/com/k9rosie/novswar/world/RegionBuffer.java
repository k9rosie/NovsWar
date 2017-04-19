package com.k9rosie.novswar.world;

import com.k9rosie.novswar.world.CuboidType;
import org.bukkit.Location;

public class RegionBuffer {
    private NovsWorld world;
    private Location cornerOne;
    private Location cornerTwo;
    private CuboidType type;
    private String name;

    public RegionBuffer(NovsWorld world, String name, CuboidType type) {
        this.world = world;
        this.name = name;
        this.type = type;
    }

    public void setCornerOne(Location cornerOne) {
        this.cornerOne = cornerOne;
    }

    public void setCornerTwo(Location cornerTwo) {
        this.cornerTwo = cornerTwo;
    }

    public void createRegion() {
        NovsCuboid cuboid = new NovsCuboid(world, cornerOne, cornerTwo, type);
        world.getCuboids().put(name, cuboid);
    }
}
