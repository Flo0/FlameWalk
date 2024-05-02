package com.gestankbratwurst.flamewalk.cd;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

  private final Map<UUID, Map<String, Long>> cooldowns;
  private final Map<UUID, Map<String, Long>> lastUses;

  public CooldownManager() {
    this.cooldowns = new HashMap<>();
    this.lastUses = new HashMap<>();
  }

  public void setActionCooldown(UUID uuid, String actionId, long cooldown) {
    this.cooldowns.computeIfAbsent(uuid, key -> new HashMap<>()).put(actionId, cooldown);
  }

  public void setActionLastUse(UUID uuid, String actionId, long lastUse) {
    this.lastUses.computeIfAbsent(uuid, key -> new HashMap<>()).put(actionId, lastUse);
  }

  public void setActionLastUse(UUID uuid, String actionId) {
    this.setActionLastUse(uuid, actionId, System.currentTimeMillis());
  }

  public long getActionCooldown(UUID uuid, String actionId) {
    return this.cooldowns.getOrDefault(uuid, new HashMap<>()).getOrDefault(actionId, 0L);
  }

  public long getCooldownLeft(UUID uuid, String actionId) {
    long lastCast = this.lastUses.getOrDefault(uuid, new HashMap<>()).getOrDefault(actionId, 0L);
    long now = System.currentTimeMillis();
    long cooldown = this.cooldowns.getOrDefault(uuid, new HashMap<>()).getOrDefault(actionId, 0L);
    return Math.max(0, lastCast + cooldown - now);
  }

}
