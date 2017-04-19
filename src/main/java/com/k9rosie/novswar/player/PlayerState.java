package com.k9rosie.novswar.player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.config.Messages;
import com.k9rosie.novswar.event.NovsWarJoinGameEvent;
import com.k9rosie.novswar.event.NovsWarPlayerRespawnEvent;
import com.k9rosie.novswar.game.DeathTimer;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;
import com.k9rosie.novswar.util.SendTitle;
import com.k9rosie.novswar.world.RegionBuffer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlayerState {
    private Game game;
    private NovsWar novswar;
    private NovsPlayer player;
    private NovsTeam team;
    private DeathTimer deathTimer;
    private ArrayList<NovsPlayer> observers;
    private HashMap<NovsPlayer, Double> attackers;
    private boolean dead; //Whether a player has died and is spectating via death cam
    private boolean spectating; //Whether a player in the lobby entered spectator mode
    private boolean voted;
    private boolean shiftToggled;
    private boolean teamChat; //True for global, false for team
    private boolean settingRegion;

    private RegionBuffer regionBuffer;

    public PlayerState(Game game, NovsPlayer player, NovsTeam team) {
        this.game = game;
        this.player = player;
        this.team = team;
        novswar = game.getGameHandler().getNovsWarInstance();
        observers = new ArrayList<>();
        attackers = new HashMap<>();
        dead = false;
        spectating = false;
        settingRegion = false;
        voted = false;
        shiftToggled = false;
        teamChat = false;
    }

    public Game getGame() {
        return game;
    }

    public NovsPlayer getPlayer() {
        return player;
    }

    public void setTeam(NovsTeam team) {
        this.team.getTeamState().getScoreboardTeam().removeEntry(player.getBukkitPlayer().getDisplayName());
        team.getTeamState().getScoreboardTeam().addEntry(player.getBukkitPlayer().getDisplayName());
        this.team.getTeamState().getPlayers().remove(player);
        team.getTeamState().getPlayers().add(player);
        this.team = team;
    }

    public NovsTeam getTeam() {
        return team;
    }

    public ArrayList<NovsPlayer> getObservers() {
        return observers;
    }

    public HashMap<NovsPlayer, Double> getAttackers() {
        return attackers;
    }

    public void setDeathTimer(DeathTimer deathTimer) {
        this.deathTimer = deathTimer;
    }

    public DeathTimer getDeathTimer() {
        return deathTimer;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isSpectating() {
        return spectating;
    }

    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
    }

    public boolean hasVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public boolean isShiftToggled() {
        return shiftToggled;
    }

    public void setShiftToggled(boolean shiftToggled) {
        this.shiftToggled = shiftToggled;
    }

    public boolean isTeamChat() {
        return teamChat;
    }

    public void setTeamChat(boolean teamChat) {
        this.teamChat = teamChat;
    }

    public boolean isSettingRegion() {
        return settingRegion;
    }

    public void setSettingRegion(boolean settingRegion) {
        this.settingRegion = settingRegion;
    }

    public void addAttacker(NovsPlayer player, Double damage) {
        if(attackers.containsKey(player)) {
            attackers.put(player, attackers.get(player) + damage);
        } else {
            attackers.put(player, damage);
        }
    }

    public NovsPlayer getAssistAttacker(NovsPlayer killer) {
        if (killer != null) {
            attackers.remove(killer);
        }

        NovsPlayer assistAttacker = null;
        if(attackers.size() > 0) {
            Iterator<Map.Entry<NovsPlayer, Double>> it = attackers.entrySet().iterator();

            double assistAttackerDamage = 0;
            while (it.hasNext()) {
                Map.Entry<NovsPlayer, Double> pair = it.next();

                if(pair.getValue() > assistAttackerDamage) {
                    assistAttackerDamage = pair.getValue();
                    assistAttacker = pair.getKey();
                }
            }
        }
        return assistAttacker;
    }

    public void joinGame() {
        boolean joinInProgress = novswar.getConfigManager().getCoreConfig().getGameJoinInProgress();
        if (!joinInProgress && (game.getGameState() == GameState.DURING_GAME) || game.getGameState() == GameState.POST_GAME) {
            ChatUtil.sendError(player, Messages.CANNOT_JOIN_GAME.toString());
            return;
        }

        if (!team.equals(novswar.getTeamManager().getDefaultTeam())) {
            ChatUtil.sendError(player, "You're already in the game.");
            return;
        }

        NovsWarJoinGameEvent event = new NovsWarJoinGameEvent(game, player);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled() == false) {
            novswar.getTeamManager().assignTeam(player);
        }
    }

    public void respawn() {
        SendTitle.sendTitle(player.getBukkitPlayer(), 0, 0, 0, " ", "");

        deathTimer.stopTimer();
        deathTimer = null;


        player.getBukkitPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(game.getScoreboard().getSidebarTitle());

        if (dead) {
            dead = false;

            // remove player from the spectator list of who they're spectating
            Player target = (Player) player.getBukkitPlayer().getSpectatorTarget();
            if (player != null) { // if they're spectating something
                novswar.getPlayerManager().getPlayer(target).getPlayerState().getObservers().remove(player);
            }

            Player bukkitPlayer = player.getBukkitPlayer();
            bukkitPlayer.teleport(game.getWorld().getTeamSpawnLoc(team));
            bukkitPlayer.setGameMode(GameMode.SURVIVAL);
            bukkitPlayer.setWalkSpeed(0.2f);
            bukkitPlayer.setFlySpeed(0.2f);

            // invoke respawn event
            NovsWarPlayerRespawnEvent event = new NovsWarPlayerRespawnEvent(player, game);
            Bukkit.getPluginManager().callEvent(event);
        }
    }
}
