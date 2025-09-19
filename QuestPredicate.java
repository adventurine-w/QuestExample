package org.adv.clickerflex.features.challenger.quest;

import lombok.Getter;
import org.adv.clickerflex.mob.LivingCustomMob;
import org.adv.clickerflex.player.CustomPlayer;
import org.adv.clickerflex.player.data.containers.CurrencySource;
import org.adv.clickerflex.utils.generic.WordUtils;
import org.adv.clickerflex.world.area.Area;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.ParameterizedType;
import java.util.function.BiPredicate;

// Player + T
// Extra Checks
// QuestType says Kill Mobs, but QuestPredicate adds an extra check:
// - Kill X Mobs in Spruce;
// - Kill X Mobs above Level 50;
public abstract class QuestPredicate<T> {
    private final BiPredicate<Player, T> predicate;
    @Getter
    private final Class<T> type;
    @Getter
    private final String description;
    private static final QuestPredicate<Void> EMPTY = new QuestPredicate<>((p,v)->true, ""){};

    protected QuestPredicate(BiPredicate<Player, T> predicate, String description) {
        this.predicate = predicate;
        this.type = captureType();
        this.description = description;
    }

    private Class<T> captureType() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) superClass.getActualTypeArguments()[0];
        return clazz;
    }

    public boolean ifPass(Player player, T subject) {
        return predicate.test(player, subject);
    }

    public static QuestPredicate<Void> isInArea(Area area) {
        return new QuestPredicate<>((p, v) -> CustomPlayer.of(p).isInArea(area), "<7>in "+area.getColor()+area.getName()) {};
    }
    public static QuestPredicate<LivingCustomMob> isEntityType(EntityType entityType) {
        return new QuestPredicate<>((p, mob) -> mob.getEntity().getType() == entityType, "<7>whom are "+ WordUtils.camelToProper(entityType.name())+"s") {};
    }
    public static QuestPredicate<CurrencySource> isFromMob(){
        return new QuestPredicate<>((p, source) -> source.isFromMob(), "<7>from mobs") {};
    }

    public static QuestPredicate<Void> empty(){
        return EMPTY;
    }
}
