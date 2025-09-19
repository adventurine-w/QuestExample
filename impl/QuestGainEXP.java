package org.adv.clickerflex.features.challenger.quest.impl;

import org.adv.clickerflex.essentials.enums.Currency;
import org.adv.clickerflex.features.challenger.ChallengerLive;
import org.adv.clickerflex.features.challenger.abstr.QuestPredicateExtractor;
import org.adv.clickerflex.features.challenger.abstr.QuestTypeAbstr;
import org.adv.clickerflex.features.challenger.quest.ChallengerQuest;
import org.adv.clickerflex.mob.events.PlayerDamageCustomMobEvent;
import org.adv.clickerflex.player.data.containers.CurrencySource;
import org.adv.clickerflex.player.data.events.PlayerCurrencyGainEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class QuestGainEXP extends QuestPredicateExtractor<CurrencySource> implements QuestTypeAbstr {
    @Override
    public @NotNull CurrencySource getInvolvedSubject(Event event) {
        if(event instanceof PlayerCurrencyGainEvent ev && ev.getCurrency() == Currency.EXP){
            return ev.getSource();
        }return CurrencySource.NONE;
    }

    @Override
    public Set<Class<? extends Event>> getInvolvedEvents() {
        return Set.of(PlayerDamageCustomMobEvent.class);
    }

    @Override
    public double getProgress(Player subject, ChallengerLive live, ChallengerQuest quest, Event event) {
        if(event instanceof PlayerCurrencyGainEvent ev && ev.getCurrency() == Currency.EXP){
            return ev.getAmt();
        }return 0;
    }
}
