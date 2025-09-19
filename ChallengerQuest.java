package org.adv.clickerflex.features.challenger.quest;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.adv.clickerflex.features.challenger.mob.ChallengerMob;

@Getter
@Accessors(chain = true)
public class ChallengerQuest {
    private final QuestType type;
    private final QuestPredicate<?> predicate;
    private double progress = 0;
    private final double goal;
    @Setter
    private ChallengerMob boss;

    public ChallengerQuest(QuestType type, QuestPredicate<?> predicate, double goal) {
        this.type = type;
        this.predicate = predicate;
        this.goal = goal;
    }
    public void addProgress(double progress){
        this.progress += progress;
    }
    public String getDescription(){
        String predicateStr = predicate!=null?" "+predicate.getDescription().trim():"";
        return (type.getDescription(goal).trim()+predicateStr).trim();
    }
    public boolean hasPredicate(){
        return predicate != null;
    }
}
