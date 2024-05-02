package com.gestankbratwurst.flamewalk.skills.projectiles;

import com.gestankbratwurst.flamewalk.api.tasks.TaskManager;

import java.util.LinkedList;
import java.util.List;

public class SkillProjectileManager {

  private final List<SkillProjectile> activeProjectiles;

  public SkillProjectileManager() {
    activeProjectiles = new LinkedList<>();
    TaskManager.runTaskTimer(this::tick, 1L, 1L);
  }

  private void tick() {
    this.activeProjectiles.removeIf(SkillProjectile::tick);
  }

  public void launchProjectile(SkillProjectile projectile) {
    this.activeProjectiles.add(projectile);
  }

}
