package com.k9rosie.novswar.event;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;

public class NovsWarPlayerKillEvent extends NovsWarPlayerDeathEvent {
    private NovsPlayer attacker;
    private NovsTeam attackerTeam;

    public NovsWarPlayerKillEvent(NovsPlayer victim, NovsPlayer attacker, NovsTeam victimTeam, NovsTeam attackerTeam, Game game) {
        super(victim, victimTeam, false, game);
        this.attacker = attacker;
        this.attackerTeam = attackerTeam;
    }

    public NovsPlayer getAttacker() {
        return attacker;
    }

    public NovsTeam getAttackerTeam() {
        return attackerTeam;
    }
}
