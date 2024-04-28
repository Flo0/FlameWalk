package com.gestankbratwurst.flamewalk.game.impl.main;

import com.gestankbratwurst.flamewalk.game.GameManager;
import com.gestankbratwurst.flamewalk.game.GameState;
import com.gestankbratwurst.flamewalk.game.eventbus.EventbusState;
import com.gestankbratwurst.flamewalk.game.impl.CleanupGameState;
import org.bukkit.Location;

public class MainGameState extends EventbusState {

  private final Nexus nexus;

  public MainGameState(GameManager gameManager) {
    super(gameManager);
    Location spawn = gameManager.getPlugin().getWorldManager().getGameWorld().getSpawnLocation();
    this.nexus = new Nexus(spawn.clone().add(0, 1.5, 0));
  }

  @Override
  public void tick() {
    this.nexus.tick();
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
    return new CleanupGameState(this.gameManager);
  }

  @Override
  public void onCancel() {

  }
}
