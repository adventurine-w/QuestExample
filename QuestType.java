package org.adv.clickerflex.features.challenger.quest;

import lombok.Getter;
import org.adv.clickerflex.features.challenger.ChallengerType;
import org.adv.clickerflex.features.challenger.abstr.QuestPredicateExtractor;
import org.adv.clickerflex.features.challenger.abstr.QuestTypeAbstr;
import org.adv.clickerflex.features.challenger.quest.impl.*;
import org.adv.clickerflex.utils.mc.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

import static org.adv.clickerflex.utils.generic.SF.sf;

public enum QuestType {
    KILL_MOBS(goal->{
        return "<7>Kill <c>"+sf(goal)+" <7>mobs";
    }, ItemCreator.of(Material.IRON_SWORD), new QuestKillMobs()),
    DEAL_DAMAGE(goal->{
        return "<7>Deal <c>"+sf(goal)+" <7>damage";
    }, ItemCreator.of(Material.STICK), new QuestDealDamage()),
    DEAL_CRIT_DAMAGE(goal->{
        return "<7>Deal <c_red>"+sf(goal)+" <7>critical damage";
    }, ItemCreator.of(Material.PARROT_SPAWN_EGG), new QuestDealCritDamage()),
    GAIN_EXP(goal->{
        return "<7>Gain <3>"+sf(goal)+" <7>EXP";
    }, ItemCreator.of(Material.PARROT_SPAWN_EGG), new QuestDealCritDamage()),
    MOVE_BLOCKS(goal->{
        return "<7>Move <a>"+sf(goal)+" <7>blocks";
    }, ItemCreator.of(Material.DIAMOND_BOOTS), new QuestMoveBlocks()),
    KILL_BOSS(goal->{
        return "<c>Kill The Boss!";
    }, type->ItemCreator.of(type.getSprite()), new QuestKillBoss(), true),
    ;
    public static final QuestType[] cachedValues = values();

    private final Function<Double, String> descF;
    private final Function<ChallengerType, ItemCreator> spriteF;
    @Getter
    private final QuestTypeAbstr instance;
    private final boolean disallowAllPredicate;

    QuestType(Function<Double, String> descF, ItemCreator sprite, QuestTypeAbstr instance) {
        this(descF, x->sprite, instance, false);
    }
    QuestType(Function<Double, String> descF, Function<ChallengerType, ItemCreator> spriteF, QuestTypeAbstr instance) {
        this(descF, spriteF, instance, false);
    }
    QuestType(Function<Double, String> descF, ItemCreator sprite, QuestTypeAbstr instance, boolean disallowAllPredicate) {
        this(descF, x->sprite, instance, disallowAllPredicate);
    }
    QuestType(Function<Double, String> descF, Function<ChallengerType, ItemCreator> spriteF, QuestTypeAbstr instance, boolean disallowAllPredicate) {
        this.descF = descF;
        this.spriteF = spriteF;
        this.instance = instance;
        this.disallowAllPredicate = disallowAllPredicate;
    }
    public String getDescription(double goal){
        return descF.apply(goal);
    }
    public ItemStack getSprite(ChallengerType type){
        return spriteF.apply(type).convertToGUIItem().build();
    }
    public boolean allowsPredicate(){
        return !disallowAllPredicate || instance instanceof QuestPredicateExtractor<?>;
    }
}
