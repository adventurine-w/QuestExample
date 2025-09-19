package org.adv.clickerflex.features.challenger.quest.impl;

import org.adv.clickerflex.features.challenger.ChallengerLive;
import org.adv.clickerflex.features.challenger.abstr.QuestPredicateExtractor;
import org.adv.clickerflex.features.challenger.abstr.QuestTypeAbstr;
import org.adv.clickerflex.features.challenger.quest.ChallengerQuest;
import org.adv.clickerflex.mob.LivingCustomMob;
import org.adv.clickerflex.mob.events.PlayerDamageCustomMobEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class QuestDealDamage extends QuestPredicateExtractor<LivingCustomMob> implements QuestTypeAbstr {
    @Override
    public @NotNull LivingCustomMob getInvolvedSubject(Event event) {
        if(event instanceof PlayerDamageCustomMobEvent ev){
            return ev.getVictim();
        }throw new IllegalArgumentException("Event is not of type PlayerDamageCustomMobEvent");
    }

    @Override
    public Set<Class<? extends Event>> getInvolvedEvents() {
        return Set.of(PlayerDamageCustomMobEvent.class);
    }

    @Override
    public double getProgress(Player subject, ChallengerLive live, ChallengerQuest quest, Event event) {
        if(event instanceof PlayerDamageCustomMobEvent ev){
            return ev.getDamage();
        }return 0;
    }
}
