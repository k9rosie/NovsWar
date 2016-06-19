package com.k9rosie.novswar.util.packet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class NametagEdit {
    /*
        "a": teamName
        "b": displayName
        "c": prefix
        "d": suffix
        "h": members
        "i": paramInt
        "j": packOption

    */

    private Class<?> packetClass;
    private Object packet;

    public NametagEdit(String teamName, String displayName, ChatColor color){
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            packetClass = Class.forName("net.minecraft.server."+version+".PacketPlayOutScoreboardTeam");
            packet = packetClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setField("a", teamName);
        setField("b", displayName);
        setField("c", "§"+color.getChar());
        setField("d", "");
        setField("i", 0);
        setField("h", new ArrayList<>());
        setField("j", 1);

    }

    public NametagEdit(String teamName, String displayName, String prefix, String suffix){
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            packetClass = Class.forName("net.minecraft.server."+version+".PacketPlayOutScoreboardTeam");
            packet = packetClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setField("a", teamName);
        setField("b", displayName);
        setField("c", prefix);
        setField("d", suffix);
        setField("i", 0);
        setField("h", new ArrayList<>());
        setField("j", 1);
    }

    public NametagEdit(String teamName, String displayName, String prefix){
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            packetClass = Class.forName("net.minecraft.server."+version+".PacketPlayOutScoreboardTeam");
            packet = packetClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setField("a", teamName);
        setField("b", displayName);
        setField("c", prefix);
        setField("d", "");
        setField("i", 0);
        setField("h", new ArrayList<>());
        setField("j", 1);
    }

    public void addPlayer(Player player){
        try {
            add(player);
        }
        catch(Exception e){
            e.printStackTrace();
            player.sendMessage("Failed to add you to team");
        }
    }

    public void addPlayers(Collection<? extends Player> players) {
        try {
            for (Player player : players) {
                add(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToPlayer(Player player){
        SendPacket.sendPacket(player, packet);
    }

    public void updateAll(){
        SendPacket.sendPacket(Bukkit.getServer().getOnlinePlayers(), packet);
    }

    private void setField(String field, Object value) {
        try {
            Field f = packet.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(packet, value);
            f.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void add(Player player) throws NoSuchFieldException, IllegalAccessException{
        Field f = packet.getClass().getDeclaredField("h");
        f.setAccessible(true);
        ((Collection) f.get(packet)).add(player.getName());
    }


}