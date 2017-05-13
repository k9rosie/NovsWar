package com.k9rosie.novswar.player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.config.CoreConfig;
import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.event.*;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;
import com.k9rosie.novswar.util.SendTitle;
import com.k9rosie.novswar.world.RegionBuffer;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.*;

public class PlayerState {
    private Game game;
    private NovsWar novswar;
    private NovsPlayer player;
    private NovsTeam team;
    private DeathTimer deathTimer;
    private ArrayList<NovsPlayer> observers;
    private HashMap<NovsPlayer, AttackTimer> attackers;
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

    public HashMap<NovsPlayer, AttackTimer> getAttackers() {
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

    public void tagPlayer(NovsPlayer attacker, double damage) {
        if (attackers.containsKey(attacker)) {
            AttackTimer timer = attackers.get(attacker);
            timer.resetTimer();
            timer.incrementDamage(damage);
        } else {
            int time = novswar.getCoreConfig().getGameAssistTimer();
            attackers.put(attacker, new AttackTimer(player, attacker, damage, time));
        }
    }

    public ArrayList<AttackTimer> sortAttackers() {
        ArrayList<AttackTimer> assisters = (ArrayList<AttackTimer>) attackers.values();
        Collections.sort(assisters);
        return assisters;
    }

    public void joinGame() {
        boolean joinInProgress = novswar.getConfigManager().getCoreConfig().getGameJoinInProgress();
        if (!joinInProgress && (game.getGameState() == GameState.DURING_GAME) || game.getGameState() == GameState.POST_GAME) {
            ChatUtil.sendError(player, MessagesConfig.getCannotJoinGame());
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

    public void killPlayer() {
        String deathMessage = MessagesConfig.getDeathMessage(team.getColor().toString(), player.getBukkitPlayer().getDisplayName());
        player.getStats().incrementDeaths();

        game.printDeathMessage(deathMessage);

        ArrayList<AttackTimer> sortedAttackers = sortAttackers();
        NovsPlayer assistAttacker = sortedAttackers.get(sortedAttackers.size()-1).getAttacker();
        attackers.clear();

        game.printDeathMessage(deathMessage);
        scheduleDeath(game.getGamemode().getDeathTime());

        NovsWarPlayerDeathEvent deathEvent = new NovsWarPlayerDeathEvent(player, team, true, game);
        Bukkit.getPluginManager().callEvent(deathEvent);

        if (assistAttacker != null) {
            NovsWarPlayerAssistEvent assistEvent = new NovsWarPlayerAssistEvent(assistAttacker, player, assistAttacker.getPlayerState().getTeam(), team, game);
            Bukkit.getPluginManager().callEvent(assistEvent);
        }
    }

    public void killPlayer(NovsPlayer attacker, boolean isArrowDeath) {
        String deathMessage;

        if (isArrowDeath) {
            deathMessage = MessagesConfig.getShotMessage(attacker.getPlayerState().getTeam().getColor().toString(),
                    attacker.getBukkitPlayer().getDisplayName(),
                    team.getColor().toString(),
                    player.getBukkitPlayer().getDisplayName());
            attacker.getStats().incrementArrowKills();
            player.getStats().incrementArrowDeaths();
        } else {
            deathMessage = MessagesConfig.getKillMessage(attacker.getPlayerState().getTeam().getColor().toString(),
                    attacker.getBukkitPlayer().getDisplayName(),
                    team.getColor().toString(),
                    player.getBukkitPlayer().getDisplayName());
            attacker.getStats().incrementKills();
            player.getStats().incrementDeaths();
        }

        // Print death message to all players
        game.printDeathMessage(deathMessage);

        // Evaluate assists
        NovsPlayer assistAttacker = sortAttackers().get(0).getAttacker();
        attackers.clear();

        // Schedule death spectating
        scheduleDeath(attacker, game.getGamemode().getDeathTime());
        // Event calls
        NovsWarPlayerKillEvent playerKillEvent = new NovsWarPlayerKillEvent(player, attacker, team, attacker.getPlayerState().getTeam(), game);
        Bukkit.getPluginManager().callEvent(playerKillEvent);


        if (assistAttacker != null) {
            NovsWarPlayerAssistEvent playerAssistEvent = new NovsWarPlayerAssistEvent(assistAttacker, player, assistAttacker.getPlayerState().getTeam(), team, game);
            Bukkit.getPluginManager().callEvent(playerAssistEvent);
        }
    }

    private void scheduleDeath(int seconds) {
        dead = true;
        Player bukkitPlayer = player.getBukkitPlayer();

        bukkitPlayer.setHealth(player.getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        bukkitPlayer.setFoodLevel(20);

        for (PotionEffect effect : player.getBukkitPlayer().getActivePotionEffects()) {
            player.getBukkitPlayer().removePotionEffect(effect.getType());
        }

        CoreConfig coreConfig = novswar.getCoreConfig();

        // spawn effects
        bukkitPlayer.getWorld().spawnParticle(Particle.valueOf(coreConfig.getDeathParticleType()),
                bukkitPlayer.getLocation(),
                coreConfig.getDeathParticleCount(),
                0d, 0.5d, 0d);
        bukkitPlayer.getWorld().playSound(bukkitPlayer.getLocation(),
                Sound.valueOf(coreConfig.getDeathSoundType()),
                coreConfig.getDeathSoundVolume(),
                coreConfig.getDeathSoundPitch());

        bukkitPlayer.setWalkSpeed(0f);
        bukkitPlayer.setFlySpeed(0f);

        // Set each observer for this player to a new target
        for(NovsPlayer observer : observers) {
            game.nextSpectatorTarget(observer);
        }

        // Clear this player's observer list
        observers.clear();
        player.getBukkitPlayer().setGameMode(GameMode.SPECTATOR);

        //If there is an attacker, set spectator target.
        game.nextSpectatorTarget(player);

        DeathTimer timer = new DeathTimer(game, seconds, player);
        timer.startTimer();
        deathTimer = timer;
    }

    private void scheduleDeath(NovsPlayer spectatorTarget, int seconds) {
        dead = true;
        Player bukkitPlayer = player.getBukkitPlayer();

        bukkitPlayer.setHealth(player.getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        bukkitPlayer.setFoodLevel(20);

        for (PotionEffect effect : bukkitPlayer.getActivePotionEffects()) {
            bukkitPlayer.removePotionEffect(effect.getType());
        }

        CoreConfig coreConfig = novswar.getCoreConfig();

        // spawn effects
        bukkitPlayer.getWorld().spawnParticle(Particle.valueOf(coreConfig.getDeathParticleType()),
                bukkitPlayer.getLocation(),
                coreConfig.getDeathParticleCount(),
                0d, 0.5d, 0d);
        bukkitPlayer.getWorld().playSound(bukkitPlayer.getLocation(),
                Sound.valueOf(coreConfig.getDeathSoundType()),
                coreConfig.getDeathSoundVolume(),
                coreConfig.getDeathSoundPitch());

        bukkitPlayer.setWalkSpeed(0f);
        bukkitPlayer.setFlySpeed(0f);

        for (NovsPlayer observer : observers) {
            game.nextSpectatorTarget(observer);
        }

        observers.clear();
        player.getBukkitPlayer().setGameMode(GameMode.SPECTATOR);
        setSpectatorTarget(spectatorTarget);
        DeathTimer timer = new DeathTimer(game, seconds, player);
        timer.startTimer();
        deathTimer = timer;
    }

    public void setSpectatorTarget(NovsPlayer target) {
        player.getBukkitPlayer().teleport(target.getBukkitPlayer().getLocation());
        player.getBukkitPlayer().setSpectatorTarget(target.getBukkitPlayer());
        target.getPlayerState().getObservers().add(player);
        ChatUtil.sendNotice(player, "Spectating "+target.getBukkitPlayer().getName());
    }


    public void quitSpectating() {
        if(spectating) {
            spectating = false; // must occur BEFORE gamemode change
            player.getBukkitPlayer().teleport(game.getNovsWarInstance().getWorldManager().getLobbyWorld().getTeamSpawns().get(game.getNovsWarInstance().getTeamManager().getDefaultTeam()));
            player.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
            ChatUtil.sendBroadcast(player.getBukkitPlayer().getName()+" stopped spectating.");
        }
    }
}
