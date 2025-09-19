package org.adv.clickerflex.features.challenger.quest.impl;

import org.adv.clickerflex.features.challenger.ChallengerLive;
import org.adv.clickerflex.features.challenger.abstr.QuestPredicateExtractor;
import org.adv.clickerflex.features.challenger.abstr.QuestTypeAbstr;
import org.adv.clickerflex.features.challenger.quest.ChallengerQuest;
import org.adv.clickerflex.mob.LivingCustomMob;
import org.adv.clickerflex.mob.events.CustomMobDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class QuestKillMobs extends QuestPredicateExtractor<LivingCustomMob> implements QuestTypeAbstr {
    @Override
    public @NotNull LivingCustomMob getInvolvedSubject(Event event) {
        if(event instanceof CustomMobDeathEvent ev){
            return ev.getMob();
        }throw new IllegalArgumentException("Event is not of type CustomMobDeathEvent");
    }
    @Override
    public Set<Class<? extends Event>> getInvolvedEvents() {
        return Set.of(CustomMobDeathEvent.class);
    }

    @Override
    public double getProgress(Player subject, ChallengerLive live, ChallengerQuest quest, Event event) {
        return 1;
    }

}
