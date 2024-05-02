package com.gestankbratwurst.flamewalk.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GameListener implements Listener {

  private final GameManager gameManager;

  public GameListener(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    this.gameManager.delegateEventToCurrentState(event);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    this.gameManager.delegateEventToCurrentState(event);
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    this.gameManager.delegateEventToCurrentState(event);
  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    this.gameManager.delegateEventToCurrentState(event);
  }

  @EventHandler
  public void onExplode(BlockExplodeEvent event) {
    this.gameManager.delegateEventToCurrentState(event);
  }

  @EventHandler
  public void onExplode(EntityExplodeEvent event) {
    this.gameManager.delegateEventToCurrentState(event);
  }

  @EventHandler
  public void onPlace(BlockPlaceEvent event) {
    this.gameManager.delegateEventToCurrentState(event);
  }

  @EventHandler
  public void onEntityChange(EntityChangeBlockEvent event) {
    this.gameManager.delegateEventToCurrentState(event);
  }

  @EventHandler
  public void onDeath(EntityDeathEvent event) {
    this.gameManager.delegateEventToCurrentState(event);
  }

  @EventHandler
  public void onHungerChange(FoodLevelChangeEvent event) {
    this.gameManager.delegateEventToCurrentState(event);
  }

  @EventHandler
  public void onDrop(EntityDropItemEvent event) {
    event.setCancelled(true);
    this.gameManager.delegateEventToCurrentState(event);
  }

  @EventHandler
  public void onDrop(PlayerDropItemEvent event) {
    event.setCancelled(true);
    this.gameManager.delegateEventToCurrentState(event);
  }


}
