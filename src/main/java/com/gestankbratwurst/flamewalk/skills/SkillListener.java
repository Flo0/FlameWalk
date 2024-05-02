package com.gestankbratwurst.flamewalk.skills;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SkillListener implements Listener {

  private final SkillManager skillManager;

  public SkillListener(SkillManager skillManager) {
    this.skillManager = skillManager;
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    skillManager.terminate(event.getPlayer());
  }

  @EventHandler
  public void onInteract(PlayerInteractEvent event) {
    skillManager.delegateSkillTrigger(event.getPlayer(), event);
  }

}
