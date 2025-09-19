package org.adv.clickerflex.features.challenger.abstr;

import org.adv.clickerflex.features.challenger.ChallengerLive;
import org.adv.clickerflex.features.challenger.quest.ChallengerQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Set;

public interface QuestTypeAbstr {
    Set<Class<? extends Event>> getInvolvedEvents();
    double getProgress(Player subject, ChallengerLive live, ChallengerQuest quest, Event event);
}
