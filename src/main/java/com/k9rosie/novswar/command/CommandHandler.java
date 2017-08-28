package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.admin.AdminCommand;
import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.util.ChatUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandHandler implements CommandExecutor {

    private NovsWar novsWar;
    private HashMap<String, NovsCommand> commands;

    private AdminCommand admin;
    private BaseCommand base;
    private ChatCommand chat;
    private DeathmessageCommand deathmessage;
    private HelpCommand help;
    private JoinCommand join;
    private LeaveCommand leave;
    private MapCommand map;
    private PlayerCommand player;
    private SpectateCommand spectate;
    private TeamCommand team;
    private VoteCommand vote;

    public CommandHandler(NovsWar novsWar) {
        this.novsWar = novsWar;
        commands = new HashMap<>();

        admin = new AdminCommand(novsWar);
        base = new BaseCommand();
        chat = new ChatCommand(novsWar);
        deathmessage = new DeathmessageCommand(novsWar);
        help = new HelpCommand(this);
        join = new JoinCommand(novsWar);
        leave = new LeaveCommand(novsWar);
        map = new MapCommand(novsWar);
        player = new PlayerCommand(novsWar);
        spectate = new SpectateCommand(novsWar);
        team = new TeamCommand(novsWar);
        vote = new VoteCommand(novsWar);
    }

    public void initialize() {
        admin.initialize();
        commands.put("novswar", base);
        commands.put("nw", base); // nw alias
        commands.put("chat", chat);
        commands.put("c", chat);
        commands.put("deathmessage", deathmessage);
        commands.put("help", help);
        commands.put("join", join);
        commands.put("leave", leave);
        commands.put("map", map);
        commands.put("player", player);
        commands.put("spectate", spectate);
        commands.put("team", team);
        commands.put("vote", vote);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        NovsCommand novsCommand;

        if (args.length == 1) {
            novsCommand = commands.get(args[0]);
        } else {
            novsCommand = commands.get(args[1]);
        }

        if (novsCommand == null) {
            ChatUtil.sendError(sender, MessagesConfig.getCommandNonexistent());
            return true;
        } else {
            if (novsCommand.getRequiredNumofArgs() > args.length-1) { // subtract 1 from args.length because requiredNumofArgs definitions don't take in account for the first argument
                ChatUtil.sendError(sender, MessagesConfig.getInvalidParameters());
                return true;
            }

            if (!sender.hasPermission(novsCommand.getPermissions())) {
                ChatUtil.sendError(sender, MessagesConfig.getNoPermission());
                return true;
            }

            if (novsCommand.isPlayerOnly() && !(sender instanceof Player)) {
                ChatUtil.sendError(sender, "Only in game players can issue this command");
                return true;
            }

            novsCommand.execute(sender, args);
            return true;
        }
    }

    public HashMap<String, NovsCommand> getCommands() {
        return commands;
    }

    public AdminCommand getAdminCommand() {
        return admin;
    }

    public BaseCommand getBaseCommand() {
        return base;
    }

    public ChatCommand getChatCommand() {
        return chat;
    }

    public DeathmessageCommand getDeathmessageCommand() {
        return deathmessage;
    }

    public HelpCommand getHelpCommand() {
        return help;
    }

    public JoinCommand getJoinCommand() {
        return join;
    }

    public LeaveCommand getLeaveCommand() {
        return leave;
    }

    public MapCommand getMapCommand() {
        return map;
    }

    public PlayerCommand getPlayerCommand() {
        return player;
    }

    public SpectateCommand getSpectateCommand() {
        return spectate;
    }

    public TeamCommand getTeamCommand() {
        return team;
    }

    public VoteCommand getVoteCommand() {
        return vote;
    }
}
