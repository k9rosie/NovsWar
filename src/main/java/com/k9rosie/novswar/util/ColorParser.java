package com.k9rosie.novswar.util;

import org.bukkit.ChatColor;

public class ColorParser {

    public static ChatColor parseString(String color) {
        switch (color.toLowerCase()) {
            case "aqua": return ChatColor.AQUA;
            case "black": return ChatColor.BLACK;
            case "blue": return ChatColor.BLUE;
            case "bold": return ChatColor.BOLD;
            case "darkaqua": return ChatColor.DARK_AQUA;
            case "darkblue": return ChatColor.DARK_BLUE;
            case "darkgray": return ChatColor.DARK_GRAY;
            case "darkgreen": return ChatColor.DARK_GREEN;
            case "darkpurple": return ChatColor.DARK_PURPLE;
            case "darkred": return ChatColor.DARK_RED;
            case "gold": return ChatColor.GOLD;
            case "gray": return ChatColor.GRAY;
            case "green": return ChatColor.GREEN;
            case "italic": return ChatColor.ITALIC;
            case "lightpurple": return ChatColor.LIGHT_PURPLE;
            case "magic": return ChatColor.MAGIC;
            case "red": return ChatColor.RED;
            case "reset": return ChatColor.RESET;
            case "strikethrough": return ChatColor.STRIKETHROUGH;
            case "underline": return ChatColor.UNDERLINE;
            case "white": return ChatColor.WHITE;
            case "yellow": return ChatColor.YELLOW;
            default: return ChatColor.WHITE;
        }
    }
}
