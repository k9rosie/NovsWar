package com.k9rosie.novswar.config;

public class SpawnData {
    private String team;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public SpawnData(String team, double x, double y, double z, float pitch, float yaw) {
        this.team = team;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public String getTeam() {
        return team;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
