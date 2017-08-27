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

public class SetRegionCommand extends NovsCommand {

    public SetRegionCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        //nw admin setregion regionOne battlefield
        if (getArgs().length != 4) {
            ChatUtil.sendError((Player) getSender(), MessagesConfig.getInvalidParameters());
            return;
        } else {
            Player bukkitPlayer = (Player) getSender();
            NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get(bukkitPlayer);
            World bukkitWorld = bukkitPlayer.getWorld();
            NovsWorld world = getNovsWar().getWorldManager().getWorlds().get(bukkitWorld);

            if (world == null) {
                ChatUtil.sendError(bukkitPlayer, "The world you're in isn't enabled in NovsWar.");
                return;
            }

            String regionName = getArgs()[2];
            CuboidType cuboidType = CuboidType.parseString(getArgs()[3]);

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


    }
}
