package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.world.CuboidType;
import com.k9rosie.novswar.world.NovsWorld;
import com.k9rosie.novswar.util.ChatUtil;
import com.k9rosie.novswar.world.RegionBuffer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRegionCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private NovsWar novsWar;

    public SetRegionCommand(NovsWar novsWar) {
        permissions = "novswar.command.admin.setregion";
        description = "Sets a region in the world";
        requiredNumofArgs = 2;
        playerOnly = true;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
        Player bukkitPlayer = (Player) sender;
        NovsPlayer player = novsWar.getPlayerManager().getPlayers().get(bukkitPlayer);
        World bukkitWorld = bukkitPlayer.getWorld();
        NovsWorld world = novsWar.getWorldManager().getWorlds().get(bukkitWorld);

        if (world == null) {
            ChatUtil.sendError(bukkitPlayer, "The world you're in isn't enabled in NovsWar.");
            return;
        }

        String regionName = args[2];
        CuboidType cuboidType = CuboidType.parseString(args[3]);

        if (cuboidType == null) {
        	String regionTypeList = "";
        	for(CuboidType region : CuboidType.values()) {
        		regionTypeList += (region.toString().toLowerCase()+" ");
        	}
            ChatUtil.sendError(bukkitPlayer, "Invalid region type. Use "+regionTypeList);
            return;
        }

        RegionBuffer regionBuffer = new RegionBuffer(world, regionName, cuboidType);

        player.setSettingRegion(true);
        player.setRegionBuffer(regionBuffer);

        ChatUtil.sendNotice(player, "Setting corner one...");
    }

    @Override
    public String getPermissions() {
        return permissions;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getRequiredNumofArgs() {
        return requiredNumofArgs;
    }

    @Override
    public boolean isPlayerOnly() {
        return playerOnly;
    }
}
