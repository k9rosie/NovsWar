package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AdminCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private NovsWar novsWar;

    private HashMap<String, NovsCommand> commands;
    private DelRegionCommand delRegion;
    private HelpAdminCommand help;
    private KickCommand kick;
    private ListRegionsCommand listregions;
    private NextGameCommand nextgame;
    private RestartCommand restart;
    private SaveRegionsCommand saveregions;
    private SetRegionCommand setregion;
    private SetSpawnCommand setspawn;
    private SetTeamCommand setteam;

    public AdminCommand(NovsWar novsWar) {
        permissions = "novswar.command.admin";
        description = "Base admin command";
        requiredNumofArgs = 0;
        playerOnly = false;
        this.novsWar = novsWar;

        delRegion = new DelRegionCommand(novsWar);
        help = new HelpAdminCommand(this);
        kick = new KickCommand(novsWar);
        listregions = new ListRegionsCommand(novsWar);
        nextgame = new NextGameCommand(novsWar);
        restart = new RestartCommand(novsWar);
        saveregions = new SaveRegionsCommand(novsWar);
        setregion = new SetRegionCommand(novsWar);
        setspawn = new SetSpawnCommand(novsWar);
        setteam = new SetTeamCommand(novsWar);
    }

    public void initialize() {
        commands.put("delregion", delRegion);
        commands.put("help", help);
        commands.put("kick", kick);
        commands.put("listregions", listregions);
        commands.put("nextgame", nextgame);
        commands.put("restart", restart);
        commands.put("saveregions", saveregions);
        commands.put("setregion", setregion);
        commands.put("setteam", setteam);
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) { // if the command is used by itself
            sender.sendMessage("reserve for send stats"); // TODO: send stuff like TPS data, database storage info, etc.
        } else if (args.length >= 2) {
            NovsCommand novsCommand = commands.get(args[2]);

            if (novsCommand == null) {
                ChatUtil.sendError(sender, MessagesConfig.getCommandNonexistent());
                return;
            } else {
                if (novsCommand.getRequiredNumofArgs() > args.length-2) { // subtract 1 from args.length because requiredNumofArgs definitions don't take in account for the first argument
                    ChatUtil.sendError(sender, MessagesConfig.getInvalidParameters());
                    return;
                }

                if (!sender.hasPermission(novsCommand.getPermissions())) {
                    ChatUtil.sendError(sender, MessagesConfig.getNoPermission());
                    return;
                }

                if (novsCommand.isPlayerOnly() && !(sender instanceof Player)) {
                    ChatUtil.sendError(sender, "Only in game players can issue this command");
                    return;
                }

                novsCommand.execute(sender, args);
                return;
            }
        }
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

    public HashMap<String, NovsCommand> getCommands() {
        return commands;
    }

    public DelRegionCommand getDelRegionCommand() {
        return delRegion;
    }

    public HelpAdminCommand getHelpCommand() {
        return help;
    }

    public KickCommand getKickCommand() {
        return kick;
    }

    public ListRegionsCommand getListregionsCommand() {
        return listregions;
    }

    public NextGameCommand getNextgameCommand() {
        return nextgame;
    }

    public RestartCommand getRestartCommand() {
        return restart;
    }

    public SaveRegionsCommand getSaveregionsCommand() {
        return saveregions;
    }

    public SetRegionCommand getSetregionCommand() {
        return setregion;
    }

    public SetSpawnCommand getSetspawnCommand() {
        return setspawn;
    }

    public SetTeamCommand getSetteamCommand() {
        return setteam;
    }
}
