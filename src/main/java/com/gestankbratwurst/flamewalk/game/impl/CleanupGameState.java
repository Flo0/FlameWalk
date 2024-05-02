package com.gestankbratwurst.flamewalk.game.impl;

import com.gestankbratwurst.flamewalk.FlameWalk;
import com.gestankbratwurst.flamewalk.game.GameManager;
import com.gestankbratwurst.flamewalk.game.GameState;
import com.gestankbratwurst.flamewalk.game.eventbus.EventbusState;

public class CleanupGameState extends EventbusState {

  public CleanupGameState(FlameWalk plugin) {
    super(plugin);
  }

  @Override
  public void tick() {

  }

  @Override
  public boolean isDone() {
    return false;
  }

  @Override
  public void onStart() {

  }

  @Override
  public void onEnd() {

  }

  @Override
  public GameState getNextState() {
    return new LobbyGameState(this.plugin);
  }

  @Override
  public void onCancel() {

  }

  @Override
  protected void registerHandlers() {

  }
}
