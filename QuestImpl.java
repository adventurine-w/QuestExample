package org.adv.clickerflex.features.challenger.quest;


import org.adv.clickerflex.Clickerflex;
import org.adv.clickerflex.features.challenger.ChallengerLive;
import org.adv.clickerflex.features.challenger.abstr.QuestPredicateExtractor;
import org.adv.clickerflex.features.challenger.abstr.QuestTypeAbstr;
import org.adv.clickerflex.features.challenger.event.QuestCompleteEvent;
import org.adv.clickerflex.mob.events.CustomMobDeathEvent;
import org.adv.clickerflex.player.CustomPlayer;
import org.adv.clickerflex.zlogging.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class QuestImpl implements Listener{
    public static HashMap<Class<? extends Event>, List<QuestType>> eventQuestMap = new HashMap<>();
    private static final QuestImpl singleton = new QuestImpl();
    public static void init(){
        singleton.initt();
    }
    public void initt(){
        for (QuestType questType : QuestType.values()) {
            for (Class<? extends Event> eventClass : questType.getInstance().getInvolvedEvents()) {
                eventQuestMap.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(questType);
            }
        }

        RegisteredListener registeredListener =
                new RegisteredListener(this,
                        (listener, event) -> onEvent(event),
                        EventPriority.MONITOR,
                        Clickerflex.getInstance(),
                        false
                );

        for (HandlerList handler : HandlerList.getHandlerLists()) {
            // Check if the handler's event class is assignable from the desired event class
            for(Class<? extends Event> clazz : eventQuestMap.keySet()) {
                try {
                    Method method = clazz.getDeclaredMethod("getHandlerList");
                    HandlerList currentHandlerList = (HandlerList) method.invoke(null);

                    if (currentHandlerList.equals(handler)) {
                        handler.register(registeredListener);
                    }
                }catch(Exception e){
                    Log.warn(e);
                }
            }
        }
    }

    public static final HashMap<Class<? extends Event>, Function<Event, Player>> eventPlayerResolver = new HashMap<>();
    static {
        registerResolver(CustomMobDeathEvent.class, e -> {
            if(e instanceof CustomMobDeathEvent ev){
                return ev.getMob().getLastAttacker();
            }
            return null;
        });
    }
    public static void registerResolver(Class<? extends Event> eventClass, Function<Event, Player> resolver){
        eventPlayerResolver.put(eventClass, resolver);
    }
    public static Player resolve(Event event){
        if(event instanceof PlayerEvent pe){
            return pe.getPlayer();
        }
        Function<Event, Player> resolver = eventPlayerResolver.get(event.getClass());
        if(resolver != null){
            return resolver.apply(event);
        }
        return null;
    }

    public void onEvent(Event event){
        x(event);
    }
    public static void x(Event event){
        if(!eventQuestMap.containsKey(event.getClass())) return;
        Player player = resolve(event);
        if(player==null) return;
        ChallengerLive live = CustomPlayer.of(player).getChallengerLive();
        if(live==null) return;
        ChallengerQuest quest = live.getCurrentQuest();
        QuestTypeAbstr instance = quest.getType().getInstance();
        if(!instance.getInvolvedEvents().contains(event.getClass())) return;
        if(quest.getType().allowsPredicate() && quest.hasPredicate()){
            Object subject = null;
            if(instance instanceof QuestPredicateExtractor<?> extractor && !quest.getPredicate().getType().equals(Void.class)){
                subject = extractor.getInvolvedSubject(event);
            }
            @SuppressWarnings("unchecked")
            QuestPredicate<Object> castPredicate = (QuestPredicate<Object>) quest.getPredicate();
            if (!castPredicate.ifPass(player, subject)) return;
        }
        double progress = quest.getType().getInstance().getProgress(player, live, quest, event);
        quest.addProgress(progress);
        if(quest.getProgress()>=quest.getGoal()){
            new QuestCompleteEvent(player, live, quest).call();
            live.nextStage();
        }
    }
}
