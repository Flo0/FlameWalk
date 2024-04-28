package com.gestankbratwurst.flamewalk.game.eventbus;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerEvent;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class GlobalEventbus {

  public static void defaults(EventbusState state) {
    state.registerHandler(BlockPlaceEvent.class, GlobalEventbus::autoCancel);
    state.registerHandler(BlockBreakEvent.class, GlobalEventbus::autoCancel);
    state.registerHandler(EntityDropItemEvent.class, GlobalEventbus::autoCancel);
    state.registerHandler(BlockExplodeEvent.class, event -> event.blockList().clear());
    state.registerHandler(EntityExplodeEvent.class, event -> event.blockList().clear());
  }

  private static <T extends Cancellable> void autoCancel(T event) {
    if (originatesFromCreative(event)) {
      return;
    }
    event.setCancelled(true);
  }

  // Bitte alles unterhalb dieser Zeile ignorieren.
  // Das hier ist nur ein Hack, um die Aufgabe zu vereinfachen.

  private static final Map<Class<?>, Predicate<Object>> INVOKE_DYNAMIC_CACHE = new HashMap<>();

  private static <T> boolean originatesFromCreative(T event) {
    Predicate<Object> check = INVOKE_DYNAMIC_CACHE.computeIfAbsent(event.getClass(), GlobalEventbus::createPlayerCheck);
    return check.test(event);
  }

  private static Predicate<Object> createPlayerCheck(Class<?> eventType) {
    MethodType methodType = MethodType.methodType(Player.class);
    try {
      MethodHandle methodHandle = MethodHandles.lookup().findVirtual(eventType, "getPlayer", methodType);
      if (methodHandle == null) {
        return event -> false;
      }
      return event -> {
        try {
          return methodHandle.invoke(event) instanceof Player player && player.getGameMode() == GameMode.CREATIVE;
        } catch (Throwable throwable) {
          return false;
        }
      };
    } catch (Throwable throwable) {
      return event -> false;
    }
  }

}
