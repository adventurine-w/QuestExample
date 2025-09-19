package org.adv.clickerflex.features.challenger.quest.impl;

import org.adv.clickerflex.features.challenger.ChallengerLive;
import org.adv.clickerflex.features.challenger.abstr.QuestTypeAbstr;
import org.adv.clickerflex.features.challenger.quest.ChallengerQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

public class QuestMoveBlocks implements QuestTypeAbstr {

    @Override
    public Set<Class<? extends Event>> getInvolvedEvents() {
        return Set.of(PlayerMoveEvent.class);
    }

    @Override
    public double getProgress(Player subject, ChallengerLive live, ChallengerQuest quest, Event event) {
        if(!(event instanceof PlayerMoveEvent e)) return 0;
        if (e.getFrom().getX() == e.getTo().getX()
                && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return 0; // same block → ignore
        }
        double dx = e.getTo().getX() - e.getFrom().getX();
        double dz = e.getTo().getZ() - e.getFrom().getZ();
        return Math.sqrt(dx*dx + dz*dz);
    }
}
