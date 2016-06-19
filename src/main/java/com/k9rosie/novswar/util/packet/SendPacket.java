package com.k9rosie.novswar.util.packet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

public class SendPacket {
    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnection;

    static {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            Class<?> typeNMSPlayer = Class.forName("net.minecraft.server."+version+".EntityPlayer");
            Class<?> typeCraftPlayer = Class.forName("org.bukkit.craftbukkit."+version+".entity.CraftPlayer");
            Class<?> typePlayerConnection = Class.forName("net.minecraft.server."+version+".PlayerConnection");
            getHandle = typeCraftPlayer.getMethod("getHandle");
            playerConnection = typeNMSPlayer.getField("playerConnection");
            sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName("net.minecraft.server."+version+".Packet"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPacket(Collection<? extends Player> players, Object packet) {
        for (Player player : players) {
            sendPacket(player, packet);
        }
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object nmsPlayer = getHandle.invoke(player);
            Object connection = playerConnection.get(nmsPlayer);
            sendPacket.invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
