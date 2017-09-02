package com.k9rosie.novswar.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.k9rosie.novswar.player.NovsPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtil {

	private static ChatColor noticeColor = ChatColor.GRAY;
	private static ChatColor errorColor = ChatColor.RED;
	public static final String PLUGIN_TAG = "§7[§6NovsWar§7]§f ";
	
	public static void sendNotice(NovsPlayer player, String message) {
		String notice = PLUGIN_TAG + noticeColor + message;
		player.getBukkitPlayer().sendMessage(notice);
	}

	public static void sendNotice(Player player, String message) {
		String notice = PLUGIN_TAG + noticeColor + message;
		player.sendMessage(notice);
	}

    public static void sendNotice(CommandSender sender, String message) {
        String notice = PLUGIN_TAG + noticeColor + message;
        sender.sendMessage(notice);
    }

	public static void sendError(NovsPlayer player, String message) {
		String error = PLUGIN_TAG + errorColor + message;
		player.getBukkitPlayer().sendMessage(error);
	}

	public static void sendError(Player player, String message) {
		String error = PLUGIN_TAG + errorColor + message;
		player.sendMessage(error);
	}

	public static void sendError(CommandSender sender, String message) {
		String error = PLUGIN_TAG + errorColor + message;
		sender.sendMessage(error);
	}
	
	public static void sendBroadcast(String message) {
		String notice = PLUGIN_TAG + noticeColor + message;
		Bukkit.broadcastMessage(notice);
	}
	
	
}
