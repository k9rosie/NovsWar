package com.k9rosie.novswar.config;

public class CuboidData {
    private String name;
    private String type;
    private double cornerOneX;
    private double cornerOneY;
    private double cornerOneZ;
    private double cornerTwoX;
    private double cornerTwoY;
    private double cornerTwoZ;

    public CuboidData(String name, String type, double cornerOneX, double cornerOneY, double cornerOneZ, double cornerTwoX, double cornerTwoY, double cornerTwoZ) {
        this.name = name;
        this.type = type;
        this.cornerOneX = cornerOneX;
        this.cornerOneY = cornerOneY;
        this.cornerOneZ = cornerOneZ;
        this.cornerTwoX = cornerTwoX;
        this.cornerTwoY = cornerTwoY;
        this.cornerTwoZ = cornerTwoZ;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getCornerOneX() {
        return cornerOneX;
    }

    public double getCornerOneY() {
        return cornerOneY;
    }

    public double getCornerOneZ() {
        return cornerOneZ;
    }

    public double getCornerTwoX() {
        return cornerTwoX;
    }

    public double getCornerTwoY() {
        return cornerTwoY;
    }

    public double getCornerTwoZ() {
        return cornerTwoZ;
    }
}
