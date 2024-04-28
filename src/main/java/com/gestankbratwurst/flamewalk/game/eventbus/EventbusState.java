package com.gestankbratwurst.flamewalk.game.eventbus;

import com.gestankbratwurst.flamewalk.game.GameManager;
import com.gestankbratwurst.flamewalk.game.GameState;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class EventbusState implements GameState {

  private final Map<Class<? extends Event>, Consumer<? extends Event>> eventHandlers;
  protected final GameManager gameManager;

  protected EventbusState(GameManager gameManager) {
    this.gameManager = gameManager;
    this.eventHandlers = new HashMap<>();
    GlobalEventbus.defaults(this);
    registerHandlers();
  }

  protected <T extends Event> void registerHandler(Class<T> eventClass, Consumer<T> handler) {
    this.eventHandlers.put(eventClass, handler);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Event> void handleEvent(T event) {
    Consumer<T> handler = (Consumer<T>) this.eventHandlers.get(event.getClass());
    if (handler != null) {
      handler.accept(event);
    }
  }

  protected abstract void registerHandlers();

}
