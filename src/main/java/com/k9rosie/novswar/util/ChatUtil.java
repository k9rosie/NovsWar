package com.k9rosie.novswar.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.entity.Player;

public class ChatUtil {

	private static ChatColor noticeColor = ChatColor.GRAY;
	private static ChatColor errorColor = ChatColor.RED;
	private static String novsWarTag = "§7[§6NovsWar§7]§f ";
	
	
	public static void printDebug(String message) {
		if(NovsWar.getInstance().getNovsConfigCache().getConfig("core").getBoolean("core.debug")) {
        	System.out.println("[DEBUG]: "+message);
        }
	}
	
	public static void sendNotice(NovsPlayer player, String message) {
		String notice = novsWarTag + noticeColor + message;
		player.getBukkitPlayer().sendMessage(notice);
	}

	public static void sendNotice(Player player, String message) {
		String notice = novsWarTag + noticeColor + message;
		player.sendMessage(notice);
	}

	public static void sendError(NovsPlayer player, String message) {
		String error = novsWarTag + errorColor + message;
		player.getBukkitPlayer().sendMessage(error);
	}

	public static void sendError(Player player, String message) {
		String error = novsWarTag + errorColor + message;
		player.sendMessage(error);
	}
	
	public static void sendBroadcast(String message) {
		String notice = novsWarTag + noticeColor + message;
		Bukkit.broadcastMessage(notice);
	}
	
	
}
