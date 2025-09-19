package org.adv.clickerflex.features.challenger.abstr;

import lombok.Getter;
import org.adv.clickerflex.features.challenger.quest.QuestPredicate;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;

@Getter
public abstract class QuestPredicateExtractor<T> {
    private final Class<T> type;

    protected QuestPredicateExtractor() {
        this.type = captureType();
    }

    private Class<T> captureType() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) superClass.getActualTypeArguments()[0];
        return clazz;
    }

    public abstract @NotNull T getInvolvedSubject(Event event);

    public boolean isCompatibleWith(QuestPredicate<?> predicate){
        return predicate.getType().equals(Void.class) || predicate.getType().equals(type);
    }
}