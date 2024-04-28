package com.gestankbratwurst.flamewalk.game;

import com.gestankbratwurst.flamewalk.FlameWalk;
import com.gestankbratwurst.flamewalk.api.tasks.TaskManager;
import com.gestankbratwurst.flamewalk.game.impl.GamePreparationGameState;
import com.gestankbratwurst.flamewalk.game.impl.LobbyGameState;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GameManager {

  @Getter
  private GameState currentState;
  @Getter
  private final FlameWalk plugin;
  private final Set<UUID> activePlayers;

  public GameManager(FlameWalk plugin) {
    this.activePlayers = new HashSet<>();
    this.plugin = plugin;
    Bukkit.getPluginManager().registerEvents(new GameListener(this), plugin);
    TaskManager.runTaskTimer(this::tickCurrentState, 1, 1);
    this.currentState = new LobbyGameState(this);
  }

  public List<UUID> getActivePlayers() {
    return List.copyOf(this.activePlayers);
  }

  public boolean isActivePlayer(UUID playerID) {
    return this.activePlayers.contains(playerID);
  }

  public void addActivePlayer(UUID playerID) {
    this.activePlayers.add(playerID);
  }

  public void removeActivePlayer(UUID playerID) {
    this.activePlayers.remove(playerID);
  }

  public void start() {
    if (this.currentState instanceof LobbyGameState) {
      this.currentState.onEnd();
    } else {
      this.currentState.onCancel();
    }
    this.currentState = new GamePreparationGameState(this);
    this.currentState.onStart();
  }

  protected <T extends Event> void delegateEventToCurrentState(T event) {
    if (this.currentState == null) {
      return;
    }
    this.currentState.handleEvent(event);
  }

  private void tickCurrentState() {
    if (this.currentState == null) {
      return;
    }
    this.currentState.tick();
    if (this.currentState.isDone()) {
      this.currentState.onEnd();
      this.currentState = this.currentState.getNextState();
      this.currentState.onStart();
    }
  }

  public int getActivePlayerCount() {
    return this.activePlayers.size();
  }
}
