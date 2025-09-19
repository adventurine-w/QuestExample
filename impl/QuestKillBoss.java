package org.adv.clickerflex.features.challenger.quest.impl;

import org.adv.clickerflex.features.challenger.ChallengerLive;
import org.adv.clickerflex.features.challenger.abstr.QuestTypeAbstr;
import org.adv.clickerflex.features.challenger.quest.ChallengerQuest;
import org.adv.clickerflex.mob.events.CustomMobDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Set;

public class QuestKillBoss implements QuestTypeAbstr {
    @Override
    public Set<Class<? extends Event>> getInvolvedEvents() {
        return Set.of(CustomMobDeathEvent.class);
    }

    @Override
    public double getProgress(Player subject, ChallengerLive live, ChallengerQuest quest, Event event) {
        if(event instanceof CustomMobDeathEvent ev){
            if(live.getSpawnedMobs().contains(ev.getMob()) || ev.getMob().getID().equals(quest.getBoss().getAttributes().getID())){
                return 1;
            }
        }
        return 0;
    }
}
