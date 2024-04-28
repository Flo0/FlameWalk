package com.gestankbratwurst.flamewalk.game;

import org.bukkit.event.Event;

public interface GameState {

  void tick();

  <T extends Event> void handleEvent(T event);

  boolean isDone();

  void onStart();

  void onEnd();

  GameState getNextState();

  void onCancel();
}