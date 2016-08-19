package com.k9rosie.novswar.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;

public class ChatFormat {

	private static ChatColor noticeColor = ChatColor.DARK_GREEN;
	
	
	public static void printDebug(String message) {
		if(NovsWar.getInstance().getNovsConfigCache().getConfig("core").getBoolean("core.debug")) {
        	System.out.println("[DEBUG]: "+message);
        }
	}
	
	public static void sendNotice(NovsPlayer player, String message) {
		String notice = noticeColor + "[NovsWar]: " + message;
		player.getBukkitPlayer().sendMessage(notice);
	}
	
	public static void sendBroadcast(String message) {
		Bukkit.broadcastMessage(noticeColor +""+ ChatColor.BOLD + "[NovsWar]:" + message);
	}
	
	
}
