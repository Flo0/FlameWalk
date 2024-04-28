package com.gestankbratwurst.flamewalk.game.impl;

import com.gestankbratwurst.flamewalk.game.GameManager;
import com.gestankbratwurst.flamewalk.game.GameState;
import com.gestankbratwurst.flamewalk.game.eventbus.EventbusState;

public class LobbyGameState extends EventbusState {

  public LobbyGameState(GameManager gameManager) {
    super(gameManager);
  }

  @Override
  public void tick() {

  }

  @Override
  protected void registerHandlers() {

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
    return new GamePreparationGameState(this.gameManager);
  }

  @Override
  public void onCancel() {

  }
}
